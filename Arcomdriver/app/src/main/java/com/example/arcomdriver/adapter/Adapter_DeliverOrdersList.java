package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_DeliverOrdersItem;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 06 Nov 2022*/
public class Adapter_DeliverOrdersList extends RecyclerView.Adapter<Adapter_DeliverOrdersList.QrViewHolder> {
    private ArrayList<Model_DeliverOrdersItem> itemList_cu;
    private Context context;
    public Adapter_DeliverOrdersList(ArrayList<Model_DeliverOrdersItem> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.deliverordersview_row,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {


        if(itemList_cu.get(i).getIsSelected().equals("true")){
            holder.do_chkSelected.setChecked(true);
        }else {
            holder.do_chkSelected.setChecked(false);
        }


        holder.do_orderID_tv.setText("SO "+itemList_cu.get(i).getDo_OrderNumber());

         holder.do_Amt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.parseDouble(itemList_cu.get(i).getDo_OrderAmt())));
        holder.do_Customer_tv.setText(itemList_cu.get(i).getDo_OrderCustomer());
        holder.do_status_tv.setText(itemList_cu.get(i).getDo_OrderStatus());

        if(itemList_cu.get(i).getDo_OrderDate().equals("null")){
            holder.do_Date_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList_cu.get(i).getDo_OrderDate());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.do_Date_tv.setText(date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(itemList_cu.get(i).getDo_OrderStatus().equals("Unpaid")){
            holder.do_status_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }else{
            holder.do_status_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return itemList_cu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView do_orderID_tv, do_Amt_tv, do_Customer_tv, do_Date_tv, do_status_tv;

        CheckBox do_chkSelected;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            do_orderID_tv = itemView.findViewById(R.id.do_orderID_tv);
            do_Amt_tv = itemView.findViewById(R.id.do_Amt_tv);
            do_Customer_tv = itemView.findViewById(R.id.do_Customer_tv);
            do_Date_tv = itemView.findViewById(R.id.do_Date_tv);
            do_status_tv = itemView.findViewById(R.id.do_status_tv);
            do_chkSelected = itemView.findViewById(R.id.do_chkSelected);
        }
    }
}
