package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_RptListItem;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Nov 2022*/
public class Adapter_RptItems extends RecyclerView.Adapter<Adapter_RptItems.QrViewHolder> {
    private ArrayList<Model_RptListItem> itemList;
    private Context context;
    private Activity activity;
    public Adapter_RptItems(Activity activity, ArrayList<Model_RptListItem> list) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_reportsitemslist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        holder.rp_productQty_tv.setText(itemList.get(i).getRpt_qty());
        holder.rp_productName_tv.setText(itemList.get(i).getRpt_name());

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
