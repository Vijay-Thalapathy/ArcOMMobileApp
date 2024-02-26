package com.example.arcomdriver.adapter;

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
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.model.Model_InventoryProductList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/
public class Adapter_InventoryProducts extends RecyclerView.Adapter<Adapter_InventoryProducts.ViewHolder> {
    private ArrayList<Model_InventoryProductList> itemList;
    private Context context;
    public Adapter_InventoryProducts(ArrayList<Model_InventoryProductList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_inv_item,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.invp_name.setText(itemList.get(i).getInProductName());
        holder.invp_unit.setText(itemList.get(i).getInProductUnitMeasure());

        double getInProductMargin =0;
        if(!itemList.get(i).getInProductMargin().equals("null")){
            getInProductMargin = Double.parseDouble(itemList.get(i).getInProductMargin());
        }

        double getInProductProfit =0;
        if(!itemList.get(i).getInProductProfit().equals("null")){
            getInProductProfit = Double.parseDouble(itemList.get(i).getInProductProfit());
        }

        holder.CustomerMargin_tv.setText("Margin : "+getInProductMargin+"% | Profit : $"+getInProductProfit+"");
        holder.sellingMargin_tv.setText("Margin : "+itemList.get(i).getSmargin()+"% | Profit : $"+itemList.get(i).getSprofit()+"");

        double getInProductPrice = Double.parseDouble(itemList.get(i).getInProductPrice());
        holder.invp_selling.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(getInProductPrice)));


        double getVendor =0;
        if(!itemList.get(i).getVendorcost().equals("null")){
            getVendor = Double.parseDouble(itemList.get(i).getVendorcost());
        }
        holder.invp_vendor.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(getVendor));




        double getInProductCusPrice = Double.parseDouble(itemList.get(i).getInProductCusPrice());
        holder.invp_customer.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(getInProductCusPrice)));


        if (itemList.get(i).getInProductImage() != null) {
            Picasso.with(holder.invp_product_image.getContext()).load(Const.ImageProducts+"files/products/" + itemList.get(i).getInProductImage())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.invp_product_image);
        }else{
            holder.invp_product_image.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView invp_product_image;
        AppCompatTextView invp_name,invp_unit,invp_vendor,invp_selling,invp_customer,sellingMargin_tv,CustomerMargin_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invp_product_image = itemView.findViewById(R.id.invp_product_image);
            invp_name = itemView.findViewById(R.id.invp_name);
            invp_unit = itemView.findViewById(R.id.invp_unit);
            invp_vendor = itemView.findViewById(R.id.invp_vendor);
            invp_selling = itemView.findViewById(R.id.invp_selling);
            invp_customer = itemView.findViewById(R.id.invp_customer);
            sellingMargin_tv = itemView.findViewById(R.id.sellingMargin_tv);
            CustomerMargin_tv = itemView.findViewById(R.id.CustomerMargin_tv);
        }
    }


}
