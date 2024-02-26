package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_InvoiceList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Sep 2022*/
public class Adapter_InvoiceList extends RecyclerView.Adapter<Adapter_InvoiceList.QrViewHolder> {
    private ArrayList<Model_InvoiceList> itemList;
    private Context context;
    public Adapter_InvoiceList(ArrayList<Model_InvoiceList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_invoicelist,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        if(itemList.get(i).getInvoice_description().equals("")){
            holder.Inp_Name_tv.setText(itemList.get(i).getInvoice_productname());
        }else {
            holder.Inp_Name_tv.setText(itemList.get(i).getInvoice_productname()+" - "+ itemList.get(i).getInvoice_description());
        }
        holder.Inp_Qty_tv.setText(itemList.get(i).getInvoice_quantity());

        if(itemList.get(i).getInvoice_upccode().equals("null")){
            holder.Inupsc_tv.setText("N/A");
        }else {
            holder.Inupsc_tv.setText(itemList.get(i).getInvoice_upccode());
        }

        if(itemList.get(i).getItemistaxable().equals("true")){
            holder.itemTax_tv.setText("Taxable");
        }else {
            holder.itemTax_tv.setText("Non-Taxable");
        }

        holder.Inp_Amt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList.get(i).getInvoice_baseamount())));
        holder.InPerPriceAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(itemList.get(i).getInvoice_priceperunit())));

        String imageUri = Const.ImageProducts +itemList.get(i).getInvoice_productimage();
        Picasso.with(context).load(imageUri).fit().centerInside()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(  holder.Inp_img);

        if (Activity_OrdersHistory.tax.equals("1")){
            holder.taxI_ll.setVisibility(View.VISIBLE);
        }else {
            holder.taxI_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Inp_Name_tv,Inp_Amt_tv,Inupsc_tv,Inp_Qty_tv,itemTax_tv,InPerPriceAmt_tv;
        AppCompatImageView Inp_img;
        LinearLayout taxI_ll;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            Inp_img = itemView.findViewById(R.id.Inp_img);
            Inp_Name_tv = itemView.findViewById(R.id.Inp_Name_tv);
            Inp_Amt_tv = itemView.findViewById(R.id.Inp_Amt_tv);
            Inp_Qty_tv = itemView.findViewById(R.id.Inp_Qty_tv);
            Inupsc_tv = itemView.findViewById(R.id.Inupsc_tv);
            itemTax_tv = itemView.findViewById(R.id.itemTax_tv);
            InPerPriceAmt_tv = itemView.findViewById(R.id.InPerPriceAmt_tv);
            taxI_ll = itemView.findViewById(R.id.taxI_ll);
        }
    }
}
