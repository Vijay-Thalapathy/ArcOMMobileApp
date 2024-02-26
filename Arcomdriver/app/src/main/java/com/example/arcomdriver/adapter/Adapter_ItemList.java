package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Sep 2022*/

public class Adapter_ItemList extends RecyclerView.Adapter<Adapter_ItemList.QrViewHolder> {
    private ArrayList<Model_ItemList> itemList;
    private Context context;
    public Adapter_ItemList(ArrayList<Model_ItemList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {


        if (itemList.get(i).getProduct_imageurl() != null) {
            Picasso.with(holder.product_image_view.getContext()).load(Const.ImageProducts + itemList.get(i).getProduct_imageurl())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.product_image_view);
        }else{
            holder.product_image_view.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
        }

        double TotalAmt = Double.parseDouble(itemList.get(i).getPrice_PerUnit()) * Double.parseDouble(itemList.get(i).getProduct_quantity());
        holder.orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(TotalAmt)));

        holder.ItPrice_et.setText(String.valueOf(Double.valueOf(itemList.get(i).getPrice_PerUnit())));

        if(itemList.get(i).getAll_description().equals("")){
            holder.itemName_tv.setText(itemList.get(i).getProduct_name());
        }else {
            holder.itemName_tv.setText(itemList.get(i).getProduct_name()+" - "+ itemList.get(i).getAll_description());
        }

        if(itemList.get(i).getUpscCode().equals("null")){
            holder.it_UpscCode_tv.setText("N/A");
        }else {
            holder.it_UpscCode_tv.setText(itemList.get(i).getUpscCode());
        }
        holder.cart_product_quantity_text_View.setText(itemList.get(i).getProduct_quantity());


        System.out.println("getPriceTag---"+itemList.get(i).getPriceTag());

        if(itemList.get(i).getPriceTag().equals("true")){
            holder.tag_img.setVisibility(View.VISIBLE);
        }else {
            holder.tag_img.setVisibility(View.GONE);
        }

        if(itemList.get(i).getIstaxable().equals("true")){
            holder.AddchkSelected.setChecked(true);
        }else {
            holder.AddchkSelected.setChecked(false);
        }

        holder.AddchkSelected.setTag(itemList.get(i));

        if (Activity_OrdersHistory.PricingSetup.equals("1")){
            holder.ItPrice_et.setFocusable(true);
            holder.ItPrice_et.setFocusableInTouchMode(true);
            holder.ItPrice_et.setClickable(true);
        }else {
            holder.ItPrice_et.setFocusable(false);
            holder.ItPrice_et.setFocusableInTouchMode(false);
            holder.ItPrice_et.setClickable(false);
        }

        if (Activity_OrdersHistory.tax.equals("1")){
            holder.tax_ll.setVisibility(View.VISIBLE);
        }else {
            holder.tax_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView itemName_tv,itemSubName_tv,orderAmt_tv,it_UpscCode_tv;
        AppCompatImageView product_image_view,tag_img;
        AppCompatEditText cart_product_quantity_text_View,ItPrice_et;
        AppCompatCheckBox AddchkSelected;
        LinearLayout tax_ll;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image_view = itemView.findViewById(R.id.product_image_view);
            orderAmt_tv = itemView.findViewById(R.id.orderAmt_tv);
            tax_ll = itemView.findViewById(R.id.tax_ll);
            itemSubName_tv = itemView.findViewById(R.id.itemSubName_tv);
            itemName_tv = itemView.findViewById(R.id.itemName_tv);
            itemName_tv = itemView.findViewById(R.id.itemName_tv);
            ItPrice_et = itemView.findViewById(R.id.ItPrice_et);
            AddchkSelected = itemView.findViewById(R.id.AddchkSelected);
            it_UpscCode_tv = itemView.findViewById(R.id.it_UpscCode_tv);
            cart_product_quantity_text_View = itemView.findViewById(R.id.cart_product_quantity_text_View);
            tag_img = itemView.findViewById(R.id.tag_img);
        }
    }

}
