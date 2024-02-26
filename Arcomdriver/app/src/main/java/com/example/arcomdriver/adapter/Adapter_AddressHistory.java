package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_AddressHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 02 Sep 2022*/
public class Adapter_AddressHistory extends RecyclerView.Adapter<Adapter_AddressHistory.QrViewHolder> {
    private ArrayList<Model_AddressHistory> itemList_cu;
    private Context context;
    public Adapter_AddressHistory(ArrayList<Model_AddressHistory> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_viewaddresslist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(itemList_cu.get(i).getAddresstypecode().equals("1")){
            holder.AdTittle_tv.setText("Billing Address");
        }else if(itemList_cu.get(i).getAddresstypecode().equals("2")){
            holder.AdTittle_tv.setText("Shipping Address");
        }else {
            holder.AdTittle_tv.setText("Delivery Address");
        }

        holder.Ad_tv.setText(itemList_cu.get(i).getLine2()+", "+itemList_cu.get(i).getCity()+", "+itemList_cu.get(i).getStateorprovince()+", "+itemList_cu.get(i).getCountry()+", "+itemList_cu.get(i).getPostalcode());

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
        AppCompatTextView Ad_tv,AdTittle_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            Ad_tv = itemView.findViewById(R.id.Ad_tv);
            AdTittle_tv = itemView.findViewById(R.id.AdTittle_tv);
        }
    }

}
