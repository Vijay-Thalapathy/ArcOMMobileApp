package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_OmsReportsList;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 17 Oct 2022*/
public class Adapter_OMSReportList extends RecyclerView.Adapter<Adapter_OMSReportList.QrViewHolder> {
    private ArrayList<Model_OmsReportsList> itemList;
    private Context context;
    public Adapter_OMSReportList(ArrayList<Model_OmsReportsList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_omsreportsitem,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {
        holder.Reports_itemName_tv.setText(itemList.get(i).getReports_name());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Reports_itemName_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            Reports_itemName_tv = itemView.findViewById(R.id.Reports_itemName_tv);
        }
    }


}
