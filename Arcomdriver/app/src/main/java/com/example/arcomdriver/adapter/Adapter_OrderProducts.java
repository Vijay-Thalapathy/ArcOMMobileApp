package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_OrderProductsList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Oct 2022*/
public class Adapter_OrderProducts extends RecyclerView.Adapter<Adapter_OrderProducts.QrViewHolder> {
    private ArrayList<Model_OrderProductsList> itemList;

    private Context context;
    private Activity activity;
    public Adapter_OrderProducts(Activity activity, ArrayList<Model_OrderProductsList> list) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_eyelistitem,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        holder.ey_orderNum_tv.setText(itemList.get(i).getOrderProItem_proName());

        holder.ey_qty_tv.setText("Invoice Item Quantity:\n\n "+itemList.get(i).getOrderProItem_proQty());

        if(itemList.get(i).getOrderProItem_DeliveryOn().equals("null")){
            holder.ey_date_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList.get(i).getOrderProItem_DeliveryOn());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.ey_date_tv.setText("Delivery On:\n\n "+date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(itemList.get(i).getOrderProItem_isLoaded().equals("Loaded")){
            holder.img_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tick));

        }else {
            holder.img_status.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_ic_p));

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView ey_orderNum_tv,ey_warehouse_tv,ey_amt_tv,ey_qty_tv,ey_date_tv;
        AppCompatImageView img_status,img_Prefresh;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);

            ey_orderNum_tv = itemView.findViewById(R.id.ey_orderNum_tv);
            img_status = itemView.findViewById(R.id.img_status);
            img_Prefresh = itemView.findViewById(R.id.img_Prefresh);
            ey_qty_tv = itemView.findViewById(R.id.ey_qty_tv);
            ey_date_tv = itemView.findViewById(R.id.ey_date_tv);

            }

    }
}
