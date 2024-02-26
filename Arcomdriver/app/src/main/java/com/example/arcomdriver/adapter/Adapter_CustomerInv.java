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
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_CustInv;
import com.example.arcomdriver.model.Model_InventoryProductList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Jan 2024*/
public class Adapter_CustomerInv extends RecyclerView.Adapter<Adapter_CustomerInv.ViewHolder> {
    private ArrayList<Model_CustInv> itemList;
    private Context context;
    public Adapter_CustomerInv(ArrayList<Model_CustInv> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_custinv_item,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.custInv_name.setText(itemList.get(i).getCustInvname());

        if(itemList.get(i).getCustInvgroupid().equals("null")){
            holder.custInv_group.setText("N/A");
        }else {
            holder.custInv_group.setText(itemList.get(i).getCustInvgroupid());
        }

        if(itemList.get(i).getCustInvzoneid().equals("null")){
            holder.custInv_zone.setText("N/A");
        }else {
            holder.custInv_zone.setText(itemList.get(i).getCustInvzoneid());
        }


        holder.custInv_startDate.setText(itemList.get(i).getCustInvstartDate());
        holder.custInv_endDate.setText(itemList.get(i).getCustInvendDate());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView custInv_name,custInv_group,custInv_zone,custInv_startDate,custInv_endDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            custInv_name = itemView.findViewById(R.id.custInv_name);
            custInv_group = itemView.findViewById(R.id.custInv_group);
            custInv_zone = itemView.findViewById(R.id.custInv_zone);
            custInv_startDate = itemView.findViewById(R.id.custInv_startDate);
            custInv_endDate = itemView.findViewById(R.id.custInv_endDate);
        }
    }


}
