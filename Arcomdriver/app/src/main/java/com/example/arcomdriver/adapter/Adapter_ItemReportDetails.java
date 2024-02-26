package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_ItemReportsDetails;

import java.util.ArrayList;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Sep 2022*/

public class Adapter_ItemReportDetails extends RecyclerView.Adapter<Adapter_ItemReportDetails.QrViewHolder> {
    private ArrayList<Model_ItemReportsDetails> itemList;
    private Context context;
    public Adapter_ItemReportDetails(ArrayList<Model_ItemReportsDetails> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_reportsitems,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {
        holder.rp_productQty_tv.setText(itemList.get(i).getRd_ProductQty());
        holder.rp_productName_tv.setText(itemList.get(i).getRd_productName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView rp_productName_tv,rp_productQty_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            rp_productName_tv = itemView.findViewById(R.id.rp_productName_tv);
            rp_productQty_tv = itemView.findViewById(R.id.rp_productQty_tv);
        }
    }


}
