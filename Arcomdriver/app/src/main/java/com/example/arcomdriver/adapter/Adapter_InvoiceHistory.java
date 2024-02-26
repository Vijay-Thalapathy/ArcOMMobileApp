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
import com.example.arcomdriver.model.Model_InvoiceHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Sep 2022*/
public class Adapter_InvoiceHistory extends RecyclerView.Adapter<Adapter_InvoiceHistory.QrViewHolder> {
    private ArrayList<Model_InvoiceHistory> itemList_invoice;
    private Context context;
    public Adapter_InvoiceHistory(ArrayList<Model_InvoiceHistory> list1) {
        this.itemList_invoice = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_viewinvoicehistory,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.InvName_tv.setText(itemList_invoice.get(i).getCustomername());

        if(itemList_invoice.get(i).getInvoiceNum().startsWith("DRFIN")){
            holder.Or_status_img.setVisibility(View.VISIBLE);
            holder.InvNum_tv.setText(itemList_invoice.get(i).getInvoiceNum());
            holder.InvNum_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            holder.Or_status_img.setVisibility(View.GONE);
            holder.InvNum_tv.setText("IN_ "+itemList_invoice.get(i).getInvoiceNum());
            holder.InvNum_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.InvAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList_invoice.get(i).getTotalamount())));

        if(itemList_invoice.get(i).getInvoicedate().equals("null")){
            holder.InvDate_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList_invoice.get(i).getInvoicedate());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.InvDate_tv.setText(date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        if(itemList_invoice.get(i).getStatus().equals("Paid")){
            holder.InvStatus_tv.setText(itemList_invoice.get(i).getStatus());
            holder.InvStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }else{
            holder.InvStatus_tv.setText("Payment pending");
            holder.InvStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }

    }

    @Override
    public int getItemCount() {
        return itemList_invoice.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView InvNum_tv,InvDate_tv,InvName_tv,InvAmt_tv,InvStatus_tv;
        AppCompatImageView Or_status_img;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            InvNum_tv = itemView.findViewById(R.id.InvNum_tv);
            InvDate_tv = itemView.findViewById(R.id.InvDate_tv);
            InvName_tv = itemView.findViewById(R.id.InvName_tv);
            InvAmt_tv = itemView.findViewById(R.id.InvAmt_tv);
            InvStatus_tv = itemView.findViewById(R.id.InvStatus_tv);
            Or_status_img = itemView.findViewById(R.id.Or_status_img);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<Model_InvoiceHistory> arrayList1) {
        itemList_invoice = new ArrayList<>();
        itemList_invoice.clear();
        itemList_invoice.addAll(arrayList1);
        notifyDataSetChanged();
    }
}
