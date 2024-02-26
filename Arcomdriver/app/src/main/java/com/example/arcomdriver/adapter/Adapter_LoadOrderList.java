package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_LoadOrderList;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 01 Oct 2022*/
public class Adapter_LoadOrderList extends RecyclerView.Adapter<Adapter_LoadOrderList.QrViewHolder> {
    private ArrayList<Model_LoadOrderList> itemList_cu;
    private Context context;
    public Adapter_LoadOrderList(ArrayList<Model_LoadOrderList> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routeloadorderlist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.loOrderOntrk_tv.setText("On Truck : "+itemList_cu.get(i).getPr_orderOntruck());
        holder.loOrderReq_tv.setText("Required : "+itemList_cu.get(i).getPr_orderRequired());
        holder.loOrderNAme_tv.setText("SO "+itemList_cu.get(i).getPr_orderNum());
        holder.loOrderAm_tv.setText("Amount : "+itemList_cu.get(i).getPr_orderAmt());
        holder.loOrderCus_tv.setText("Customer : "+itemList_cu.get(i).getPr_orderCust());

        System.out.println("---On Truck :--"+itemList_cu.get(i).getPr_orderOntruck());
        System.out.println("---Required:--"+itemList_cu.get(i).getPr_orderRequired());

        if(itemList_cu.get(i).getPr_orderIsLoad().equals("NotLoaded")) {
            holder.btn_Rl_loadTruck.setText("Load Truck");
            holder.img_OrderImgStatus.setImageResource(R.drawable.loading_ic_p);
            holder.btn_Rl_loadTruck.setBackgroundResource(R.drawable.shape_btngraey);
        }else if(itemList_cu.get(i).getPr_orderIsLoad().equals("Loaded")){
            holder.btn_Rl_loadTruck.setText("Loaded");
            holder.img_OrderImgStatus.setImageResource(R.drawable.ic_tick);
            holder.btn_Rl_loadTruck.setBackgroundResource(R.drawable.shape_btngreen_bg);
        }else if(itemList_cu.get(i).getPr_orderIsLoad().equals("Partially Loaded")){
            holder.btn_Rl_loadTruck.setText("Partially Loaded");
            holder.img_OrderImgStatus.setImageResource(R.drawable.loading_ic_p);
            holder.btn_Rl_loadTruck.setBackgroundResource(R.drawable.shape_btn_bg);
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
        AppCompatTextView loOrderNAme_tv, loOrderReq_tv, loOrderOntrk_tv, loOrderAm_tv, loOrderCus_tv;

        AppCompatImageView img_OrderImgStatus;

        AppCompatButton btn_Rl_loadTruck;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            loOrderOntrk_tv = itemView.findViewById(R.id.loOrderOntrk_tv);
            loOrderReq_tv = itemView.findViewById(R.id.loOrderReq_tv);
            loOrderNAme_tv = itemView.findViewById(R.id.loOrderNAme_tv);
            loOrderAm_tv = itemView.findViewById(R.id.loOrderAm_tv);
            loOrderCus_tv = itemView.findViewById(R.id.loOrderCus_tv);
            img_OrderImgStatus = itemView.findViewById(R.id.img_OrderImgStatus);
            btn_Rl_loadTruck = itemView.findViewById(R.id.loadTruck_btn);
        }
    }
}
