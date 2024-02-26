package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.model.Model_OmsReportsList;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/
public class Adapter_CustomerPricing extends RecyclerView.Adapter<Adapter_CustomerPricing.ViewHolder> {
    private ArrayList<Model_CustomerPricingList> itemList;
    private Context context;
    public Adapter_CustomerPricing(ArrayList<Model_CustomerPricingList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_customerpricingitem,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.cp_name_tv.setText(itemList.get(i).getPricing_name());
        holder.cp_customerCount_tv.setText(itemList.get(i).getPricing_customercountlist());
        holder.cp_productsCount_tv.setText(itemList.get(i).getPricing_productcountlist());
        holder.cp_startDate_tv.setText(itemList.get(i).getPricing_startdatetime());
        holder.cp_endDate_tv.setText(itemList.get(i).getPricing_enddatetime());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView cp_name_tv,cp_customerCount_tv,cp_productsCount_tv,cp_startDate_tv,cp_endDate_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cp_name_tv = itemView.findViewById(R.id.cp_name_tv);
            cp_customerCount_tv = itemView.findViewById(R.id.cp_customerCount_tv);
            cp_productsCount_tv = itemView.findViewById(R.id.cp_productsCount_tv);
            cp_startDate_tv = itemView.findViewById(R.id.cp_startDate_tv);
            cp_endDate_tv = itemView.findViewById(R.id.cp_endDate_tv);
        }
    }


}
