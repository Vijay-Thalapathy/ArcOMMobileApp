package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Nov 2022*/
public class Adapter_SummaryList extends RecyclerView.Adapter<Adapter_SummaryList.QrViewHolder> {
    private ArrayList<Model_SummaryList> itemList;
    private Context context;
    public Adapter_SummaryList(ArrayList<Model_SummaryList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_summary,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList.get(i).getProduct_price())));
        holder.PerPriceAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList.get(i).getPrice_PerUnit())));


        if(itemList.get(i).getAll_dec().equals("")){
            holder.itemName_tv.setText(itemList.get(i).getProduct_name());
        }else {
            holder.itemName_tv.setText(itemList.get(i).getProduct_name()+" - "+ itemList.get(i).getAll_dec());
        }


        if(itemList.get(i).getAll_upsc().equals("null")){
            holder.Upsc_sm_tv.setText("N/A");
        }else {
            holder.Upsc_sm_tv.setText(itemList.get(i).getAll_upsc());
        }



        if(itemList.get(i).getIstaxable().equals("true")){
            holder.itemTax_tv.setText("Taxable");
        }else {
            holder.itemTax_tv.setText("Non-Taxable");
        }

        holder.summary_tvQty.setText(itemList.get(i).getProduct_quantity());

        if(itemList.get(i).getProduct_imageurl().equals("null")){
            holder.product_image_view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
        }else {

            String imageUri = Const.ImageProducts +itemList.get(i).getProduct_imageurl();
            Picasso.with(context).load(imageUri).fit().centerInside()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(  holder.product_image_view);

        }

        if (Activity_OrdersHistory.tax.equals("1")){
            holder.CustomerTax_ll.setVisibility(View.VISIBLE);
        }else {
            holder.CustomerTax_ll.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView itemName_tv,itemSubName_tv,orderAmt_tv,PerPriceAmt_tv,Upsc_sm_tv;
        AppCompatImageView product_image_view;
        AppCompatTextView summary_tvQty,itemTax_tv;
        LinearLayout CustomerTax_ll;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image_view = itemView.findViewById(R.id.product_image_view);
            orderAmt_tv = itemView.findViewById(R.id.orderAmt_tv);
            itemSubName_tv = itemView.findViewById(R.id.itemSubName_tv);
            itemName_tv = itemView.findViewById(R.id.itemName_tv);
            summary_tvQty = itemView.findViewById(R.id.summary_tvQty);
            itemTax_tv = itemView.findViewById(R.id.itemTax_tv);
            PerPriceAmt_tv = itemView.findViewById(R.id.PerPriceAmt_tv);
            CustomerTax_ll = itemView.findViewById(R.id.CustomerTax_ll);
            Upsc_sm_tv = itemView.findViewById(R.id.Upsc_sm_tv);
        }
    }


}
