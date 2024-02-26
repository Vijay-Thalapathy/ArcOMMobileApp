package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_AddMultiWarehouseItem;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Oct 2022*/
public class Adapter_AddMultiWarehouseItem extends RecyclerView.Adapter<Adapter_AddMultiWarehouseItem.QrViewHolder> {
    private ArrayList<Model_AddMultiWarehouseItem> itemList;
    private Context context;
    private Activity activity;
    public Adapter_AddMultiWarehouseItem(Activity activity, ArrayList<Model_AddMultiWarehouseItem> list) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_multiwarehouseitem,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        holder.et_warehouseQty.setText(itemList.get(i).getWarehouseQty());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        Spinner warehouse_loc_sp;
        AppCompatEditText et_warehouseQty;

        AppCompatImageView Wdelete_img;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);

            warehouse_loc_sp = itemView.findViewById(R.id.warehouse_loc_sp);
            et_warehouseQty = itemView.findViewById(R.id.et_warehouseQty);
            Wdelete_img = itemView.findViewById(R.id.Wdelete_img);
            }

    }
}
