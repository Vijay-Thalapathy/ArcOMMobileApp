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
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_CustomerStop;

import java.util.ArrayList;

import okhttp3.internal.Util;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Sep 2022*/
public class Adapter_CustomerStop extends RecyclerView.Adapter<Adapter_CustomerStop.QrViewHolder> {
    private ArrayList<Model_CustomerStop> itemList_cu;
    private Context context;
    public Adapter_CustomerStop(ArrayList<Model_CustomerStop> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routecustop,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

       holder.st_count_tv.setText(itemList_cu.get(i).getSt_count());
       holder.st_name_tv.setText(itemList_cu.get(i).getSt_name());
       holder.st_address_tv.setText(itemList_cu.get(i).getSt_address());
       holder.st_duration_tv.setText(itemList_cu.get(i).getSt_Duration_hours());

       if(itemList_cu.get(i).getSt_sequence().equals("1")){
           holder.ic_truck.setVisibility(View.VISIBLE);
       }else {
           holder.ic_truck.setVisibility(View.GONE);
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
        AppCompatTextView st_count_tv,st_name_tv,st_address_tv,st_duration_tv;

        AppCompatImageView ic_truck;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            st_count_tv = itemView.findViewById(R.id.st_count_tv);
            st_name_tv = itemView.findViewById(R.id.st_name_tv);
            st_address_tv = itemView.findViewById(R.id.st_address_tv);
            st_duration_tv = itemView.findViewById(R.id.st_duration_tv);
            ic_truck = itemView.findViewById(R.id.ic_truck);
        }
    }
}
