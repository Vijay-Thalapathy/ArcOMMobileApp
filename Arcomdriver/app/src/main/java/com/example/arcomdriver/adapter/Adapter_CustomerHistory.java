package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_CustomerHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 04 Sep 2022*/
public class Adapter_CustomerHistory extends RecyclerView.Adapter<Adapter_CustomerHistory.QrViewHolder> {
    private ArrayList<Model_CustomerHistory> itemList_cu;
    private Context context;
    public Adapter_CustomerHistory(ArrayList<Model_CustomerHistory> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_customerhistory,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.CsvNum_tv.setText(itemList_cu.get(i).getCustomernumber());

        if(itemList_cu.get(i).getIndustry().equals("null")||itemList_cu.get(i).getIndustry().isEmpty()){
            holder.CsIndustry_tv.setText("N/A");
        }else {
            holder.CsIndustry_tv.setText(itemList_cu.get(i).getIndustry());
        }

        if(itemList_cu.get(i).getCustomername().equals("null")){
            holder.CsName_tv.setText("N/A");
        }else {
            holder.CsName_tv.setText(itemList_cu.get(i).getCustomername());
        }

        if(itemList_cu.get(i).getStatus().equals("Inactive")){
            holder.CsStatus_tv.setText("Inactive");
            holder.CsStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorRed));
        }else if(itemList_cu.get(i).getStatus().equals("Active")){
            holder.CsStatus_tv.setText("Active");
            holder.CsStatus_tv.setTextColor(context.getResources().getColor(R.color.ColorGreen));
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
        AppCompatTextView CsvNum_tv,CsName_tv,CsStatus_tv,CsIndustry_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            CsvNum_tv = itemView.findViewById(R.id.CsvNum_tv);
            CsName_tv = itemView.findViewById(R.id.CsName_tv);
            CsStatus_tv = itemView.findViewById(R.id.CsStatus_tv);
            CsIndustry_tv = itemView.findViewById(R.id.CsIndustry_tv);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<Model_CustomerHistory> arrayList1) {
        itemList_cu = new ArrayList<>();
        itemList_cu.clear();
        itemList_cu.addAll(arrayList1);
        notifyDataSetChanged();
    }
}
