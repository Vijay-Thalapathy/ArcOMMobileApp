package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_OmsReportsList;
import com.example.arcomdriver.model.Model_ReportsDetails;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Nov 2022*/
public class Adapter_ReportDetails extends RecyclerView.Adapter<Adapter_ReportDetails.QrViewHolder> {
    private ArrayList<Model_ReportsDetails> itemList;
    private Context context;
    public Adapter_ReportDetails(ArrayList<Model_ReportsDetails> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_reportsdetails,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {
        holder.rp_salesNum_tv.setText(itemList.get(i).getRd_id());
        holder.rp_cusName_tv.setText(itemList.get(i).getRd_customerName());
        holder.rp_amt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(itemList.get(i).getRd_amt())));


        if(itemList.get(i).getRd_date().equals("null")){
            holder.rp_date_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList.get(i).getRd_date());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.rp_date_tv.setText(date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        if(itemList.get(i).getRd_status().equals("Cancelled")){
            holder.rp_status_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }else if(itemList.get(i).getRd_status().equals("Confirmed")){
            holder.rp_status_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }else if(itemList.get(i).getRd_status().equals("Fulfilled")){
            holder.rp_status_tv.setTextColor(context.getResources().getColor(R.color.ColorOrange));
        }else if(itemList.get(i).getRd_status().equals("New Order")){
            holder.rp_status_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.rp_status_tv.setText(itemList.get(i).getRd_status());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView rp_salesNum_tv,rp_date_tv,rp_cusName_tv,rp_amt_tv,rp_status_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            rp_salesNum_tv = itemView.findViewById(R.id.rp_salesNum_tv);
            rp_date_tv = itemView.findViewById(R.id.rp_date_tv);
            rp_cusName_tv = itemView.findViewById(R.id.rp_cusName_tv);
            rp_amt_tv = itemView.findViewById(R.id.rp_amt_tv);
            rp_status_tv = itemView.findViewById(R.id.rp_status_tv);
        }
    }


}
