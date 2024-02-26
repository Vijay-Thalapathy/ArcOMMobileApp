package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.model.Model_SalesItemList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.salesreturn.Activity_CreateSalesReturn;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Sep 2022*/

public class Adapter_SalesItemList extends RecyclerView.Adapter<Adapter_SalesItemList.QrViewHolder> {
    private ArrayList<Model_SalesItemList> itemList;
    private Context context;

    private Activity activity;

    private ArrayList<String> arReasonID = new ArrayList<>();
    private ArrayList<String> arReasonName = new ArrayList<>();
    public Adapter_SalesItemList(Activity activity,ArrayList<Model_SalesItemList> list) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_itemsales,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        try {
            App.getInstance().GetReason(Activity_CreateSalesReturn.token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject js = new JSONObject(res);
                            JSONArray data = js.getJSONArray("lstReasonResponse");

                            if (arReasonID.size() > 0) arReasonID.clear();
                            if (arReasonName.size() > 0) arReasonName.clear();


                            if (data.length() > 0) {

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js0 = data.getJSONObject(i);

                                    arReasonName.add(js0.getString("reason"));
                                    arReasonID.add(js0.getString("optionid"));

                                }
                                ArrayAdapter<String> adapterPGroup = new ArrayAdapter<String>(context, R.layout.spinner_item,arReasonName);
                                adapterPGroup.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.sp_reason.setAdapter(adapterPGroup);
                                        if(itemList.size()>0){
                                            holder.sp_reason.setSelection(itemList.indexOf(itemList.get(i).getReturnReason()));
                                        }

                                        // adapterPGroup.notifyDataSetChanged();
                                        // notifyDataSetChanged();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String LtypeS= arReasonID.get(position);
                String LName= arReasonName.get(position);

                itemList.get(i).setReturnReason(LtypeS);
              //  itemList.get(i).setLocationTypeID(LtypeS);

                Activity_CreateSalesReturn.getInstance().follow_convert();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if (itemList.get(i).getProduct_imageurl() != null) {
            Picasso.with(holder.product_image_view.getContext()).load(Const.ImageProducts + itemList.get(i).getProduct_imageurl())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.product_image_view);
        }else{
            holder.product_image_view.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
        }

        double TotalAmt = Double.parseDouble(itemList.get(i).getPrice_PerUnit()) * Double.parseDouble(itemList.get(i).getProduct_quantity());
        holder.orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.valueOf(TotalAmt)));

        holder.ItPrice_et.setText(String.valueOf(Double.valueOf(itemList.get(i).getPrice_PerUnit())));

        if(itemList.get(i).getAll_description().equals("")){
            holder.itemName_tv.setText(itemList.get(i).getProduct_name());
        }else {
            holder.itemName_tv.setText(itemList.get(i).getProduct_name()+" - "+ itemList.get(i).getAll_description());
        }

        if(itemList.get(i).getUpscCode().equals("null")){
            holder.it_UpscCode_tv.setText("N/A");
        }else {
            holder.it_UpscCode_tv.setText(itemList.get(i).getUpscCode());
        }
        holder.cart_product_quantity_text_View.setText(itemList.get(i).getProduct_quantity());


        System.out.println("getPriceTag---"+itemList.get(i).getPriceTag());

        if(itemList.get(i).getPriceTag().equals("true")){
            holder.tag_img.setVisibility(View.VISIBLE);
        }else {
            holder.tag_img.setVisibility(View.GONE);
        }

        if(itemList.get(i).getIstaxable().equals("true")){
            holder.AddchkSelected.setChecked(true);
        }else {
            holder.AddchkSelected.setChecked(false);
        }

        holder.AddchkSelected.setTag(itemList.get(i));

        if (Activity_OrdersHistory.PricingSetup.equals("1")){
            holder.ItPrice_et.setFocusable(true);
            holder.ItPrice_et.setFocusableInTouchMode(true);
            holder.ItPrice_et.setClickable(true);
        }else {
            holder.ItPrice_et.setFocusable(false);
            holder.ItPrice_et.setFocusableInTouchMode(false);
            holder.ItPrice_et.setClickable(false);
        }

        if (Activity_OrdersHistory.tax.equals("1")){
            holder.tax_ll.setVisibility(View.VISIBLE);
        }else {
            holder.tax_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView itemName_tv,itemSubName_tv,orderAmt_tv,it_UpscCode_tv;
        AppCompatImageView product_image_view,tag_img;
        AppCompatEditText cart_product_quantity_text_View,ItPrice_et;
        AppCompatCheckBox AddchkSelected;
        Spinner sp_reason;
        LinearLayout tax_ll;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image_view = itemView.findViewById(R.id.product_image_view);
            orderAmt_tv = itemView.findViewById(R.id.orderAmt_tv);
            tax_ll = itemView.findViewById(R.id.tax_ll);
            itemSubName_tv = itemView.findViewById(R.id.itemSubName_tv);
            itemName_tv = itemView.findViewById(R.id.itemName_tv);
            itemName_tv = itemView.findViewById(R.id.itemName_tv);
            ItPrice_et = itemView.findViewById(R.id.ItPrice_et);
            AddchkSelected = itemView.findViewById(R.id.AddchkSelected);
            it_UpscCode_tv = itemView.findViewById(R.id.it_UpscCode_tv);
            cart_product_quantity_text_View = itemView.findViewById(R.id.cart_product_quantity_text_View);
            tag_img = itemView.findViewById(R.id.tag_img);

            sp_reason = itemView.findViewById(R.id.sp_reason);
        }
    }

}
