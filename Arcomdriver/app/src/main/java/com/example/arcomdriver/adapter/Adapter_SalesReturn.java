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
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.model.Model_SalesReturnList;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/
public class Adapter_SalesReturn extends RecyclerView.Adapter<Adapter_SalesReturn.ViewHolder> {
    private ArrayList<Model_SalesReturnList> itemList;
    private Context context;
    public Adapter_SalesReturn(ArrayList<Model_SalesReturnList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_salesreturnitem,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.sr_Num_tv.setText(itemList.get(i).getSrm_returnnumber());
        holder.sr_Date_tv.setText(itemList.get(i).getSrm_returneddate());
        holder.sr_CusName_tv.setText(itemList.get(i).getSrm_customername());
        holder.sr_Status_tv.setText(itemList.get(i).getSrm_returnstatus());

        String dl_address="0";
        if(itemList.get(i).getSrm_totalamount().startsWith("-")){

            String[] Array1 = itemList.get(i).getSrm_totalamount().split("-");
            dl_address = Array1 [1];

            holder.sr_ReAmt_tv.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(dl_address)));

        }else {
          //  dl_address = itemList.get(i).getSrm_totalamount();

            holder.sr_ReAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(itemList.get(i).getSrm_totalamount())));

        }





        if(itemList.get(i).getSrm_returnstatus().equals("Draft")){
            holder.sr_Status_tv.setTextColor(context.getResources().getColor(R.color.ColorOrange));
        }else if(itemList.get(i).getSrm_returnstatus().equals("Returned")){
            holder.sr_Status_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }


        /*if(itemList.get(i).getSrm_orderinvnumber().equals("")){
            holder.sr_OrStatus_tv.setVisibility(View.GONE);
        }else {
            holder.sr_OrStatus_tv.setVisibility(View.VISIBLE);
        }*/

        System.out.println("InvoiceNumber---"+itemList.get(i).getSrm_orderinvnumber());
        System.out.println("OrderNumber---"+itemList.get(i).getSrm_ordernumber());

        if(itemList.get(i).getSrm_ordernumber().equals("")){
            holder.sr_Ref_tv.setText("-");
        }else {
            holder.sr_Ref_tv.setText(itemList.get(i).getSrm_ordernumber());
        }

        if(itemList.get(i).getSrm_orderinvnumber().equals("")){
            holder.sr_OrStatus_tv.setText("-");
            holder.sr_OrStatus_tv.setVisibility(View.GONE);
        }else {
            holder.sr_OrStatus_tv.setText(itemList.get(i).getSrm_orderinvnumber());
            holder.sr_OrStatus_tv.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView sr_Num_tv,sr_Date_tv,sr_Ref_tv,sr_CusName_tv,sr_Status_tv,sr_ReAmt_tv,sr_OrStatus_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sr_Num_tv = itemView.findViewById(R.id.sr_Num_tv);
            sr_Date_tv = itemView.findViewById(R.id.sr_Date_tv);
            sr_Ref_tv = itemView.findViewById(R.id.sr_Ref_tv);
            sr_CusName_tv = itemView.findViewById(R.id.sr_CusName_tv);
            sr_Status_tv = itemView.findViewById(R.id.sr_Status_tv);
            sr_ReAmt_tv = itemView.findViewById(R.id.sr_ReAmt_tv);
            sr_OrStatus_tv = itemView.findViewById(R.id.sr_OrStatus_tv);
        }
    }


}
