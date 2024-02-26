package com.example.arcomdriver.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_CustomerPricing;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 24 Jan 2024*/
public class ActivityCustomerPricing extends Activity_Menu {

    private CoordinatorLayout cl;

    public static String  OrderId,token,user_email,user_id;
    private SQLiteController sqLiteController;

    private RecyclerView mRecyclerView;

    private LottieAnimationView txt_no_record;

    private Adapter_CustomerPricing adapter_CustomerPrice;

    private ArrayList<Model_CustomerPricingList> list_CustomerPrice = new ArrayList<>();

    public AlertDialog Progress_dialog;

    private SwipeRefreshLayout srl;


    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_routepayment);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_customer_pricing, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.GONE);
        In_download_ic.setVisibility(View.GONE);
        In_print_img.setVisibility(View.GONE);
        txt_page.setText("Customer Pricing Rule");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCustomerPricing.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivityCustomerPricing.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {

                //user
                Cursor user_c = sqLiteController.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    user_email = user_c.getString(user_c.getColumnIndex("Email"));
                    //  Get_OrderList(token);

                }
            }
        } finally {
            sqLiteController.close();
        }

        mRecyclerView = findViewById(R.id.rcPricingList);

        txt_no_record = findViewById(R.id.txt_no_record);
        srl = findViewById(R.id.swipe);

        adapter_CustomerPrice = new Adapter_CustomerPricing(list_CustomerPrice);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityCustomerPricing.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(ActivityCustomerPricing.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter_CustomerPrice);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(ActivityCustomerPricing.this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("PriceID",list_CustomerPrice.get(position).getPricing_id());
                Intent in = new Intent(ActivityCustomerPricing.this, ActivityCustomerPricingInfo.class);
                in.putExtras(bundle);
                startActivity(in);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));



        GetCustomerPricing();

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                GetCustomerPricing();
            }
        });


    }

    private void GetCustomerPricing() {
        if (srl.isRefreshing()) srl.setRefreshing(false);
        try {
            App.getInstance().GetCustomerPricing(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_no_record.setVisibility(View.VISIBLE);
                            if (Progress_dialog != null) {
                                if (Progress_dialog.isShowing()) {
                                    Progress_dialog.dismiss();
                                }
                            }
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(data.length()>0){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(list_CustomerPrice.size()>0)list_CustomerPrice.clear();
                                        txt_no_record.setVisibility(View.GONE);
                                    }
                                });
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    String id = js.getString("id");
                                    String pricename = js.getString("pricename");
                                    String startdatetime = js.getString("startdatetime");
                                    String enddatetime = js.getString("enddatetime");
                                    String productcountlist = js.getString("productcountlist");
                                    String customercountlist = js.getString("customercountlist");
                                    String isactive = js.getString("isactive");


                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    Date date1 = null;
                                    String startDate ="";
                                    String endDate ="";
                                    try {
                                        date = sdf.parse(startdatetime);
                                        date1 = sdf.parse(enddatetime);
                                        startDate =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date);
                                        endDate  =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date1);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    String finalStartDate = startDate;
                                    String finalEndDate = endDate;

                                    Double pCount =Double.valueOf(productcountlist);
                                    Double cCount =Double.valueOf(customercountlist);
                                    DecimalFormat format = new DecimalFormat("0.#");


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            list_CustomerPrice.add(new Model_CustomerPricingList(id,pricename, finalStartDate, finalEndDate,String.valueOf(format.format(pCount)),String.valueOf(format.format(cCount)),isactive));
                                        }
                                    });

                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_no_record.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_CustomerPrice.notifyDataSetChanged();
                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}