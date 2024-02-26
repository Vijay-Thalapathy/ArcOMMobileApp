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
import com.example.arcomdriver.model.Model_ProductsDListItem;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 07 Oct 2022*/
public class Adapter_LoadProductList extends RecyclerView.Adapter<Adapter_LoadProductList.QrViewHolder> {
    private ArrayList<Model_ProductsDListItem> itemList_cu;
    private Context context;
    public Adapter_LoadProductList(ArrayList<Model_ProductsDListItem> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routeloadprolist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(itemList_cu.get(i).getEl_isLoaded().equals("Loaded")){
            holder.lo_proTruck_tv.setText("On Truck : "+itemList_cu.get(i).getEl_proQty());
        }else {
            holder.lo_proTruck_tv.setText("On Truck : "+"0");
        }


        holder.lo_proReq_tv.setText("Required : "+itemList_cu.get(i).getEl_proQty());
        holder.lo_proName_tv.setText(itemList_cu.get(i).getEl_proName());
        holder.lo_proAmt_tv.setText("Amount : $ "+itemList_cu.get(i).getEl_totalAmount());
        holder.lo_proWarehse_tv.setText("Warehouse : "+itemList_cu.get(i).getEl_proQty());

        if(itemList_cu.get(i).getEl_isLoaded().equals("NotLoaded")) {
            holder.img_productImgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_ic_p));
        }else if(itemList_cu.get(i).getEl_isLoaded().equals("Loaded")){
            holder.img_productImgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tick));
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
        AppCompatTextView lo_proWarehse_tv, lo_proAmt_tv, lo_proTruck_tv, lo_proReq_tv, lo_proName_tv;

        AppCompatImageView img_productImgStatus;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            lo_proWarehse_tv = itemView.findViewById(R.id.lo_proWarehse_tv);
            lo_proAmt_tv = itemView.findViewById(R.id.lo_proAmt_tv);
            lo_proTruck_tv = itemView.findViewById(R.id.lo_proTruck_tv);
            lo_proReq_tv = itemView.findViewById(R.id.lo_proReq_tv);
            lo_proName_tv = itemView.findViewById(R.id.lo_proName_tv);
            img_productImgStatus = itemView.findViewById(R.id.img_productImgStatus);
        }
    }
}
