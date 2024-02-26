package com.example.arcomdriver.route.deliverorders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_DeliverOrdersList;
import com.example.arcomdriver.adapter.Adapter_OrderProducts;
import com.example.arcomdriver.adapter.Adapter_deliverOProducts;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_DeliverOProducts;
import com.example.arcomdriver.model.Model_DeliverOrdersItem;
import com.example.arcomdriver.model.Model_OrderProductsList;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.example.arcomdriver.route.Activity_RouteLoading;

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
 * @author : SivaramYogesh*/
public class Activity_DeliverOrders extends Activity_Menu {

    private CoordinatorLayout cl;
    String user_id, token, Email;

    public AlertDialog Progress_dialog;

    RecyclerView mRecyclerProducts;

    ArrayList<Model_DeliverOrdersItem> stList = new ArrayList<>();
    Adapter_DeliverOrdersList mAdapter;

    CheckBox chkAllSelected;
    LottieAnimationView txt_no_record;

    String RouteID,CustID;


    RecyclerView rcOrderProductsView;
    Adapter_deliverOProducts OrderProductsAdapter;
    ArrayList<Model_DeliverOProducts> OrderProductsList = new ArrayList<>();

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_deliver);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deliver_orderlist, null, false);
        parentView.addView(contentView, 0);
        txt_page.setText("Deliver Orders");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            RouteID = extras.getString("Route_ID");
            CustID = extras.getString("CustomerID");

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DeliverOrders.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_DeliverOrders.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        SQLiteController sqLiteController1 = new SQLiteController(this);
        sqLiteController1.open();
        try {
            long count = sqLiteController1.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController1.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    Email = user_c.getString(user_c.getColumnIndex("Email"));
                }

            }
        } finally {
            sqLiteController1.close();
        }

        chkAllSelected = (CheckBox) findViewById(R.id.do_chk_all);
        chkAllSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    for (int i = 0; i < stList.size(); i++) {
                        Model_DeliverOrdersItem singleStudent = stList.get(i);
                        singleStudent.setIsSelected("true");

                        // mAdapter.notifyItemChanged(i);
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (!cb.isChecked()){
                    for (int i = 0; i < stList.size(); i++) {
                        Model_DeliverOrdersItem singleStudent = stList.get(i);
                        singleStudent.setIsSelected("false");

                        // mAdapter.notifyItemChanged(i);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

        findViewById(R.id.btn_back_do).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerProducts = (RecyclerView) findViewById(R.id.rc_deliverOrderList);
        txt_no_record = (LottieAnimationView) findViewById(R.id.img_noProducts);

        mAdapter = new Adapter_DeliverOrdersList(stList);
        RecyclerView.LayoutManager layoutManage1r = new LinearLayoutManager(this);
        mRecyclerProducts.setLayoutManager(layoutManage1r);
        mRecyclerProducts.setItemAnimator(new DefaultItemAnimator());
        mRecyclerProducts.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerProducts.setAdapter(mAdapter);

        mRecyclerProducts.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerProducts, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                view.findViewById(R.id.do_chkSelected).setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        if (cb.isChecked()) {
                            stList.get(position).setIsSelected("true");
                            mAdapter.notifyDataSetChanged();

                        } else if (!cb.isChecked()){
                            stList.get(position).setIsSelected("false");
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });

                OrderProductsListAlert(stList.get(position).getDo_Products());





            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        GetOrdersList();


        findViewById(R.id.btn_next_do).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
    private void GetOrdersList() {

        try {
            App.getInstance().GetDeliverOrders(RouteID,CustID,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  //  if(stList.size()>0)stList.clear();
                                    txt_no_record.setVisibility(View.GONE);
                                    mRecyclerProducts.setVisibility(View.VISIBLE);
                                }
                            });

                            JSONArray jr = new JSONArray(res);
                            System.out.println("jr---"+jr);
                            for (int i = 0; i < jr.length(); i++) {
                                JSONObject js = jr.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String paymentstatus = js.getString("paymentstatus");
                                String ordernumber = js.getString("ordernumber");
                                String ordereddate = js.getString("ordereddate");
                                String customer = js.getString("customer");
                                String totalorderamount = js.getString("totalorderamount");

                                JSONArray JR_products = js.getJSONArray("liveroutecustomerorderproducts");


                                stList.add(new Model_DeliverOrdersItem("false",orderid,ordernumber,ordereddate,customer,totalorderamount,paymentstatus,String.valueOf(JR_products)));


                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();

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
                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_no_record.setVisibility(View.VISIBLE);


                            }
                        });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OrderProductsListAlert(String Products_js) {

        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {

                AlertDialog Progress_dialog_;

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DeliverOrders.this);
                LayoutInflater inflater = Activity_DeliverOrders.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_delivers_pro_list,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                AlertDialog.Builder builder_ = new AlertDialog.Builder(Activity_DeliverOrders.this, R.style.LoadingStyle);
                builder_.setCancelable(false);
                LayoutInflater layoutInflater1 = Activity_DeliverOrders.this.getLayoutInflater();
                View v1 = layoutInflater1.inflate(R.layout.layout_avi, null);
                builder_.setView(v1);
                Progress_dialog_ = builder_.create();
                Progress_dialog_.show();

                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                AppCompatImageView close_img = v.findViewById(R.id.close_img);

                rcOrderProductsView = v.findViewById(R.id.rc_orderProducts);

                OrderProductsAdapter = new Adapter_deliverOProducts(Activity_DeliverOrders.this, OrderProductsList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_DeliverOrders.this);
                rcOrderProductsView.setLayoutManager(layoutManager);
                rcOrderProductsView.setItemAnimator(new DefaultItemAnimator());
                rcOrderProductsView.addItemDecoration(new ItemDividerDecoration(Activity_DeliverOrders.this, LinearLayoutManager.VERTICAL));
                rcOrderProductsView.setAdapter(OrderProductsAdapter);

                rcOrderProductsView.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_DeliverOrders.this, rcOrderProductsView, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));

                close_img.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                if(OrderProductsList.size()>0) OrderProductsList.clear();

                try {
                    JSONArray array2 = new JSONArray(Products_js);

                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject js = array2.getJSONObject(i);
                        String productname = js.getString("productname");
                        String orderquantity = js.getString("orderquantity");
                        String price = js.getString("price");
                        String productimage = js.getString("productimage");

                        System.out.println("productname---"+productname);
                      //  OrderProductsList.add(new Model_DeliverOProducts(AllListProducts.get(i).getEl_proName(), AllListProducts.get(i).getEl_proQty(), AllListProducts.get(i).getEl_DeliveryOn(), AllListProducts.get(i).getEl_isLoaded(), AllListProducts.get(i).getEl_Address(), AllListProducts.get(i).getEl_OID(), AllListProducts.get(i).getEl_productID()));

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OrderProductsAdapter.notifyDataSetChanged();
                            if (Progress_dialog_ != null) {
                                if (Progress_dialog_.isShowing()) {
                                    Progress_dialog_.dismiss();
                                }
                            }
                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}