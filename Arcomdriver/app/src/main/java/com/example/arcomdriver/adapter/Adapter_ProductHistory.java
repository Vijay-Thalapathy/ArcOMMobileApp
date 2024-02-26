package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_ProductHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 22 Oct 2022*/
public class Adapter_ProductHistory extends RecyclerView.Adapter<Adapter_ProductHistory.QrViewHolder> {
    private ArrayList<Model_ProductHistory> itemList_produts;
    private Context context;
    public Adapter_ProductHistory(ArrayList<Model_ProductHistory> list1) {
        this.itemList_produts = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_productlist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(Activity_OrdersHistory.OMEnableFlag.equals("true")){
            holder.pdr_unit_tv.setVisibility(View.VISIBLE);
        }else {
            holder.pdr_unit_tv.setVisibility(View.GONE);
        }

        if(itemList_produts.get(i).getDescription().equals("")||itemList_produts.get(i).getDescription().equals("null")){
            holder.pdr_name_tv.setText(itemList_produts.get(i).getProductname());
        }else {
            holder.pdr_name_tv.setText(itemList_produts.get(i).getProductname()+" - "+ itemList_produts.get(i).getDescription());
        }

        if(itemList_produts.get(i).getUpccod().equals("null")){
            holder.pdr_stock_tv.setText(itemList_produts.get(i).getProductnumber());
        }else {
            holder.pdr_stock_tv.setText(itemList_produts.get(i).getProductnumber()+" | "+ itemList_produts.get(i).getUpccod());
        }

        if(!itemList_produts.get(i).getPrice().equals("null")){
            holder.pdr_price_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList_produts.get(i).getPrice())));

        }else {
            holder.pdr_price_tv.setText("0");

        }

        holder.pdr_unit_tv.setText("Each");

        if(itemList_produts.get(i).getStatus().equals("Active")){
            holder.pdr_status_tv.setText(itemList_produts.get(i).getStatus());
            holder.pdr_status_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }else if(itemList_produts.get(i).getStatus().equals("Inactive")){
            holder.pdr_status_tv.setText(itemList_produts.get(i).getStatus());
            holder.pdr_status_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }

       // System.out.println("Image---"+itemList_produts.get(i).getProductimage());
       // System.out.println("ImageURL---"+Const.ImageProducts);

        if (itemList_produts.get(i).getProductimage() != null) {
            Picasso.with(holder.productList_image_view.getContext()).load(Const.ImageProducts+"files/products/" + itemList_produts.get(i).getProductimage())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.productList_image_view);
        }else{
            holder.productList_image_view.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
        }

    }

    @Override
    public int getItemCount() {
        return itemList_produts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView pdr_name_tv,pdr_unit_tv,pdr_stock_tv,pdr_price_tv,pdr_status_tv;
        AppCompatImageView productList_image_view;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            pdr_name_tv = itemView.findViewById(R.id.pdr_name_tv);
            pdr_unit_tv = itemView.findViewById(R.id.pdr_unit_tv);
            pdr_stock_tv = itemView.findViewById(R.id.pdr_stock_tv);
            pdr_price_tv = itemView.findViewById(R.id.pdr_price_tv);
            pdr_status_tv = itemView.findViewById(R.id.pdr_status_tv);
            productList_image_view = itemView.findViewById(R.id.productList_image_view);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<Model_ProductHistory> arrayList1) {
        itemList_produts = new ArrayList<>();
        itemList_produts.addAll(arrayList1);
        notifyDataSetChanged();
    }
}
