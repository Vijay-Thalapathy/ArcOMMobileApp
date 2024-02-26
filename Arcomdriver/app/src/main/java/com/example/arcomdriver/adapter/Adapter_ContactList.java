package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_ContactList;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 06 Sep 2022*/
public class Adapter_ContactList extends RecyclerView.Adapter<Adapter_ContactList.QrViewHolder> {
    private ArrayList<Model_ContactList> itemList_produts;
    private Context context;
    public Adapter_ContactList(ArrayList<Model_ContactList> list1) {
        this.itemList_produts = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_customeritem,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(itemList_produts.get(i).getFirstNm().equals("null")){
            holder.ct_name.setText("N/A");
        }else {
            holder.ct_name.setText(itemList_produts.get(i).getFirstNm()+itemList_produts.get(i).getLastNm());
        }

        if(itemList_produts.get(i).getJobTittle().equals("null")){
            holder.ct_jbTittle.setText("N/A");
        }else {
            holder.ct_jbTittle.setText(itemList_produts.get(i).getJobTittle());
        }

        if(itemList_produts.get(i).getPEmail().equals("null")||itemList_produts.get(i).getPEmail().isEmpty()){
            holder.ct_Email.setText("N/A");
        }else {
            holder.ct_Email.setText(itemList_produts.get(i).getPEmail());
        }

       holder.ct_Mob.setText(itemList_produts.get(i).getPContact());
       holder.ct_add1_tv.setText( itemList_produts.get(i).getDCAddress()+","+itemList_produts.get(i).getDCCountry().trim()+","+itemList_produts.get(i).getDCState().trim()+","+itemList_produts.get(i).getDCCity().trim()+","+itemList_produts.get(i).getDCPinCode().trim());
       holder.ct_add2_tv.setText( itemList_produts.get(i).getBCAddress()+","+itemList_produts.get(i).getBCCountry().trim()+","+itemList_produts.get(i).getBCState().trim()+","+itemList_produts.get(i).getBCCity().trim()+","+itemList_produts.get(i).getBCPinCode().trim());

        if(itemList_produts.get(i).getIsPrimary().equals("true")){
            holder.CtSelected.setVisibility(View.VISIBLE);
            holder.CtSelected.setChecked(true);
            holder.CtSelected.setEnabled(false);

            holder.ctisPrimary.setText("Yes");
        }else {
            holder.CtSelected.setVisibility(View.GONE);

            holder.ctisPrimary.setText("No");
        }

    }

    @Override
    public int getItemCount() {
        return itemList_produts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView ct_name,ct_jbTittle,ct_Email,ct_Mob,ct_add1_tv,ctisPrimary,ct_add2_tv;
        CheckBox CtSelected;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            ct_name = itemView.findViewById(R.id.ct_name);
            ct_jbTittle = itemView.findViewById(R.id.ct_jbTittle);
            ct_Email = itemView.findViewById(R.id.ct_Email);
            ct_Mob = itemView.findViewById(R.id.ct_Mob);
            CtSelected = itemView.findViewById(R.id.CtSelected);
            ctisPrimary = itemView.findViewById(R.id.ctisPrimary);
            ct_add1_tv = itemView.findViewById(R.id.ct_add1_tv);
            ct_add2_tv = itemView.findViewById(R.id.ct_add2_tv);
        }
    }




}
