package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_PorderList;
import com.example.arcomdriver.model.Model_StopList;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Oct 2022*/
public class Adapter_POrderList extends RecyclerView.Adapter<Adapter_POrderList.QrViewHolder> {
    private ArrayList<Model_PorderList> itemList_cu;
    private Context context;
    public Adapter_POrderList(ArrayList<Model_PorderList> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routeporderlist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        holder.PorderNum_tv.setText(itemList_cu.get(i).getpOrderNum());
        holder.PorderQtytv.setText(itemList_cu.get(i).getpOrderQty());
        holder.PorderAmttv.setText("$" + Utils.truncateDecimal(Double.valueOf(itemList_cu.get(i).getpOrderAmt())));

        if(itemList_cu.get(i).getpOrderDelivery().equals("null")){
            holder.PorderDelivery_tv.setText("N/A");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(itemList_cu.get(i).getpOrderDelivery());
                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                holder.PorderDelivery_tv.setText(date_order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        AppCompatTextView PorderNum_tv,PorderQtytv,PorderAmttv,PorderDelivery_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            PorderNum_tv = itemView.findViewById(R.id.PorderNum_tv);
            PorderQtytv = itemView.findViewById(R.id.PorderQtytv);
            PorderAmttv = itemView.findViewById(R.id.PorderAmttv);
            PorderDelivery_tv = itemView.findViewById(R.id.PorderDelivery_tv);
        }
    }
}
