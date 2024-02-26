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
import com.example.arcomdriver.model.Model_ProductWarehouseHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 28 Oct 2022*/
public class Adapter_ProductWarehoseHistory extends RecyclerView.Adapter<Adapter_ProductWarehoseHistory.QrViewHolder> {
    private ArrayList<Model_ProductWarehouseHistory> itemList_produts;
    private Context context;
    public Adapter_ProductWarehoseHistory(ArrayList<Model_ProductWarehouseHistory> list1) {
        this.itemList_produts = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_producwarehousetlist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.pdr_name_tv.setText("Location Type \n"+itemList_produts.get(i).getLocationType());
        holder.pdr_unit_tv.setText("Location \n"+itemList_produts.get(i).getLocation());

        holder.pdr_price_tv.setText("Committed \n"+itemList_produts.get(i).getCommitted());


        if(itemList_produts.get(i).getOnhand().equals("null")){
            holder.pdr_stock_tv.setText("On hand \n"+"0");
        }else {
            holder.pdr_stock_tv.setText("On hand \n"+itemList_produts.get(i).getOnhand());
        }

        if(itemList_produts.get(i).getAvailable().equals("null")){
            holder.pdr_status_tv.setText("Available \n"+"0");
        }else {
            holder.pdr_status_tv.setText("Available \n"+itemList_produts.get(i).getAvailable());
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
        }
    }

}
