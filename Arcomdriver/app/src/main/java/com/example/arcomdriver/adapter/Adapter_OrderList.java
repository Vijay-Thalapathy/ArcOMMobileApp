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
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_OrderList;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Oct 2022*/
public class Adapter_OrderList extends RecyclerView.Adapter<Adapter_OrderList.QrViewHolder> {
    private ArrayList<Model_OrderList> itemList_order;
    private Context context;
    public Adapter_OrderList(ArrayList<Model_OrderList> list1) {
        this.itemList_order = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_vieworderlist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(itemList_order.get(i).getOrder_number().equals("")){
            holder.orderNum_tv.setText("N/A");
        }else{
            if(itemList_order.get(i).getOrder_number().startsWith("DRFOM")){
                holder.Or_status_img.setVisibility(View.VISIBLE);
                holder.orderNum_tv.setText(itemList_order.get(i).getOrder_number());
                holder.orderNum_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }else {
                holder.Or_status_img.setVisibility(View.GONE);
                holder.orderNum_tv.setText("SO "+itemList_order.get(i).getOrder_number());
                holder.orderNum_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));

                if (itemList_order.get(i).getNetStatus().equals("Offline")){
                    if(itemList_order.get(i).getSyncStatus().equals("non_Sync")){
                        holder.Or_status_img.setVisibility(View.VISIBLE);
                    }else {
                        holder.Or_status_img.setVisibility(View.GONE);
                    }
                }

            }

        }

        if(itemList_order.get(i).getOrder_name().equals("")){
            holder.orderName_tv.setText("N/A");
        }else{
            holder.orderName_tv.setText(itemList_order.get(i).getOrder_name());
        }

        if(itemList_order.get(i).getOrder_date().equals("null")){
            holder.orderDate_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList_order.get(i).getOrder_date());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.orderDate_tv.setText(date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(itemList_order.get(i).getOrder_amount())));

        if(itemList_order.get(i).getOrder_status().equals("Cancelled")){
            holder.orderStatus_tv.setText(itemList_order.get(i).getOrder_status());
            holder.orderStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }else if(itemList_order.get(i).getOrder_status().equals("Confirmed")){
            holder.orderStatus_tv.setText(itemList_order.get(i).getOrder_status());
            holder.orderStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }else if(itemList_order.get(i).getOrder_status().equals("Fulfilled")){
            holder.orderStatus_tv.setText(itemList_order.get(i).getOrder_status());
            holder.orderStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorOrange));
        }else if(itemList_order.get(i).getOrder_status().equals("Draft")){
            holder.orderStatus_tv.setText(itemList_order.get(i).getOrder_status());
            holder.orderStatus_tv.setTextColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.orderStatus_tv.setText(itemList_order.get(i).getOrder_status());
            holder.orderStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorButton));
        }

    }

    @Override
    public int getItemCount() {
        return itemList_order.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView Or_status_img;
        AppCompatTextView orderStatus_tv,orderAmt_tv,orderDate_tv,orderNum_tv,orderName_tv;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            orderStatus_tv = itemView.findViewById(R.id.orderStatus_tv);
            orderAmt_tv = itemView.findViewById(R.id.orderAmt_tv);
            orderDate_tv = itemView.findViewById(R.id.orderDate_tv);
            orderNum_tv = itemView.findViewById(R.id.orderNum_tv);
            orderName_tv = itemView.findViewById(R.id.orderName_tv);
            Or_status_img = itemView.findViewById(R.id.Or_statusIc_img);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<Model_OrderList> arrayList1) {
        itemList_order = new ArrayList<>();
        itemList_order.clear();
        itemList_order.addAll(arrayList1);
        notifyDataSetChanged();
    }





}
