package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_StopList;

import java.util.ArrayList;
import java.util.Locale;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Nov 2022*/
public class Adapter_StopList extends RecyclerView.Adapter<Adapter_StopList.QrViewHolder> {
    private ArrayList<Model_StopList> itemList_cu;
    private Context context;
    public Adapter_StopList(ArrayList<Model_StopList> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_routestoplist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

      /// holder.st_count_tv.setText(itemList_cu.get(i).getSt_count());
        String s=itemList_cu.get(i).getSt_name().substring(0,2);

        holder.st_count_tv.setText(s.toUpperCase(Locale.ROOT));

       holder.st_name_tv.setText(itemList_cu.get(i).getSt_name()+" ("+itemList_cu.get(i).getSt_type()+")");
       holder.st_address_tv.setText(itemList_cu.get(i).getSt_address());

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
        AppCompatTextView st_count_tv,st_name_tv,st_address_tv;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            st_count_tv = itemView.findViewById(R.id.st_count_tv);
            st_name_tv = itemView.findViewById(R.id.st_name_tv);
            st_address_tv = itemView.findViewById(R.id.st_address_tv);
        }
    }
}
