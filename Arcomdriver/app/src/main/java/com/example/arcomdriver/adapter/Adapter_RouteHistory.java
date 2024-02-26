package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_RouteHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 06 Nov 2022*/
public class Adapter_RouteHistory extends RecyclerView.Adapter<Adapter_RouteHistory.QrViewHolder> {
    private ArrayList<Model_RouteHistory> itemList_cu;
    private Context context;
    public Adapter_RouteHistory(ArrayList<Model_RouteHistory> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routehistory,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.routeName_tv.setText(itemList_cu.get(i).getRouteName());
        holder.driverName_tv.setText(itemList_cu.get(i).getDriverName());
        if(itemList_cu.get(i).getOrdersCount().equals("null")){
            holder.orderCount_tv.setText("N/A");
        }else {
            holder.orderCount_tv.setText(itemList_cu.get(i).getOrdersCount());
        }

        if(itemList_cu.get(i).getCustomersCount().equals("null")){
            holder.customerCount_tv.setText("N/A");
        }else {
            holder.customerCount_tv.setText(itemList_cu.get(i).getCustomersCount());
        }


        holder.routeStatus_tv.setText(itemList_cu.get(i).getRouteStatus());

        if(itemList_cu.get(i).getRouteStatus().equals("Draft") || itemList_cu.get(i).getRouteStatus().equals("Planned")){
            holder.btn_routeTrip.setText("Start Trip");
            holder.btn_routeTrip.setBackgroundResource(R.drawable.shape_btngreen_bg);
        }else  if(itemList_cu.get(i).getRouteStatus().equals("In-Transit") || itemList_cu.get(i).getRouteStatus().equals("Completed")){
            holder.btn_routeTrip.setText("End Trip");
            holder.btn_routeTrip.setBackgroundResource(R.drawable.shape_redbg);
        }

        if(itemList_cu.get(i).getRouteStatus().equals("Draft")){
            holder.routeStatus_tv.setText("Planned");
            holder.routeStatus_tv.setTextColor(context.getResources().getColor(R.color.colorGray));
        }else if(itemList_cu.get(i).getRouteStatus().equals("Planned")){
            holder.routeStatus_tv.setText("Planned");
            holder.routeStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorOrange));
        }else if(itemList_cu.get(i).getRouteStatus().equals("In-Transit")){
            holder.routeStatus_tv.setText("Live");
            holder.routeStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
        }else if(itemList_cu.get(i).getRouteStatus().equals("Completed")){
            holder.routeStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorLightGreen));
            holder.routeStatus_tv.setText("Completed");

            holder.btn_routeTrip.setVisibility(View.GONE);
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

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView routeName_tv,driverName_tv,orderCount_tv,customerCount_tv,routeStatus_tv;
        AppCompatButton btn_routeTrip;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName_tv = itemView.findViewById(R.id.routeName_tv);
            driverName_tv = itemView.findViewById(R.id.driverName_tv);
            orderCount_tv = itemView.findViewById(R.id.orderCount_tv);
            customerCount_tv = itemView.findViewById(R.id.customerCount_tv);
            routeStatus_tv = itemView.findViewById(R.id.routeStatus_tv);
            btn_routeTrip = itemView.findViewById(R.id.btn_routeTrip);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<Model_RouteHistory> arrayList1) {
        itemList_cu = new ArrayList<>();
        itemList_cu.clear();
        itemList_cu.addAll(arrayList1);
        notifyDataSetChanged();
    }

}
