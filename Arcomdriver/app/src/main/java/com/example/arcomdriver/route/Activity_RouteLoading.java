package com.example.arcomdriver.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_AddMultiWarehouseItem;
import com.example.arcomdriver.adapter.Adapter_LoadOrderList;
import com.example.arcomdriver.adapter.Adapter_LoadProductList;
import com.example.arcomdriver.adapter.Adapter_OrderProducts;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_AddMultiWarehouseItem;
import com.example.arcomdriver.model.Model_LoadOrderList;
import com.example.arcomdriver.model.Model_OrderProductsList;
import com.example.arcomdriver.model.Model_ProductsDListItem;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_RouteLoading extends Activity_Menu {

    private CoordinatorLayout cl;
    String user_id,token,Email,Route_ID;

    public AlertDialog Progress_dialog;
    private RecyclerView rcOrderList;
    private RecyclerView rcProductList;
    Adapter_LoadOrderList ListOrderAdapter;
    ArrayList<Model_LoadOrderList> listOrders = new ArrayList<>();

    Adapter_LoadProductList ListProductsAdapter;

    RecyclerView mMultiRecyclerView;
    Adapter_AddMultiWarehouseItem AddMultiadapter;
    ArrayList<Model_AddMultiWarehouseItem> AddMultilist = new ArrayList<>();

    //EyeList
    RecyclerView rcOrderProductsView;
    Adapter_OrderProducts OrderProductsAdapter;
    ArrayList<Model_OrderProductsList> OrderProductsList = new ArrayList<>();

    ArrayList<String> AddedList = new ArrayList<>();

    ArrayList<Model_ProductsDListItem> AllListProducts = new ArrayList<>();
    private SharedPreferences rdSharedPreferences;

    String StopJSON;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_routesod);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_routeloading, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Route Planning (SOD)");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            StopJSON = extras.getString("StopJSON");
            System.out.println("StopJSON--"+StopJSON);
        }

        Route_ID =getRouteID();

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RouteLoading.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_RouteLoading.this.getLayoutInflater();
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

        rcOrderList = findViewById(R.id.rcOrderList);
        rcProductList = findViewById(R.id.rcProductList);

        ListOrderAdapter = new Adapter_LoadOrderList(listOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcOrderList.setLayoutManager(layoutManager);
        rcOrderList.setItemAnimator(new DefaultItemAnimator());
        rcOrderList.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rcOrderList.setAdapter(ListOrderAdapter);
        rcOrderList.addOnItemTouchListener(new RecyclerViewTouchListener(this, rcOrderList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                AppCompatImageView img_OrderStatus =  view.findViewById(R.id.img_OrderImgStatus);
                AppCompatButton loadTruck_btn =  view.findViewById(R.id.loadTruck_btn);

                loadTruck_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println("Clicked-----"+ listOrders.get(position).getPr_orderIsLoad());

                        if(listOrders.get(position).getPr_orderIsLoad().equals("NotLoaded")) {
                            AddedList.clear();
                            //Load Truck
                            for (int i = 0; i < AllListProducts.size(); i++) {
                                if(AllListProducts.get(i).getEl_OID().equals(listOrders.get(position).getPr_orderID_())) {
                                    AllListProducts.get(i).setEl_isLoaded("Loaded");
                                    AddedList.add(AllListProducts.get(i).getEl_isLoaded());


                                    loadTruck_btn.setText("Loaded");
                                    listOrders.get(position).setPr_orderIsLoad("Loaded");
                                    loadTruck_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btngreen_bg));
                                    img_OrderStatus.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_tick));

                                    listOrders.get(position).setPr_orderOntruck(String.valueOf(AddedList.size()));
                                    ListOrderAdapter.notifyItemChanged(position);
                                }

                            }

                        }else if(listOrders.get(position).getPr_orderIsLoad().equals("Loaded")){
                            for (int i = 0; i < AllListProducts.size(); i++) {
                                if(AllListProducts.get(i).getEl_OID().equals(listOrders.get(position).getPr_orderID_())) {
                                    AllListProducts.get(i).setEl_isLoaded("NotLoaded");

                                    loadTruck_btn.setText("Load Truck");
                                    listOrders.get(position).setPr_orderIsLoad("NotLoaded");
                                    loadTruck_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btngraey));
                                    img_OrderStatus.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.loading_ic_p));
                                }

                            }

                            listOrders.get(position).setPr_orderOntruck(String.valueOf("0"));
                            ListOrderAdapter.notifyItemChanged(position);

                        }else if(listOrders.get(position).getPr_orderIsLoad().equals("Partially Loaded")){
                            OrderProductsListAlert(listOrders.get(position).getPr_orderID_(), listOrders.get(position).getPr_orderIsLoad(), String.valueOf(position));

                        }

                    }
                });

                view.findViewById(R.id.addWarehouseEye_rll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderProductsListAlert(listOrders.get(position).getPr_orderID_(), listOrders.get(position).getPr_orderIsLoad(), String.valueOf(position));
                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));



        ListProductsAdapter = new Adapter_LoadProductList(AllListProducts);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rcProductList.setLayoutManager(layoutManager1);
        rcProductList.setItemAnimator(new DefaultItemAnimator());
        rcProductList.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rcProductList.setAdapter(ListProductsAdapter);
        rcProductList.addOnItemTouchListener(new RecyclerViewTouchListener(this, rcProductList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                System.out.println("OrderID---"+ AllListProducts.get(position).getEl_productID());

               /* view.findViewById(R.id.addOrderEye_rll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      ///  WarehouseOrderAlert(listProLoad.get(position).getPr_itemID_());
                    }
                });*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));


        AppCompatButton btn_ViaTOrder = findViewById(R.id.btn_ViaTOrder);
        AppCompatButton btn_ViaTPreview = findViewById(R.id.btn_ViaTPreview);
        AppCompatButton btn_products = findViewById(R.id.btn_products);
        AppCompatButton btn_preview = findViewById(R.id.btn_preview);

        btn_ViaTOrder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

               // Progress_dialog.show();
                btn_ViaTOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_ViaTPreview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));

              /*  btn_products.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));
                btn_products.setText("Products");*/

                btn_products.setVisibility(View.GONE);
                btn_preview.setVisibility(View.GONE);

                rcProductList.setVisibility(View.INVISIBLE);
                rcOrderList.setVisibility(View.VISIBLE);

            }
        });
        btn_ViaTPreview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                //  Progress_dialog.show();
                btn_ViaTPreview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn3));
                btn_ViaTOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));

                btn_products.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));
                btn_products.setText("Orders");


                btn_products.setVisibility(View.VISIBLE);
                btn_preview.setVisibility(View.VISIBLE);

                rcProductList.setVisibility(View.INVISIBLE);
                rcOrderList.setVisibility(View.VISIBLE);




            }
        });
        btn_products.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                btn_products.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));

                rcProductList.setVisibility(View.INVISIBLE);
                rcOrderList.setVisibility(View.VISIBLE);

            }
        });

        btn_preview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                btn_preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn3));
                btn_products.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));


                rcProductList.setVisibility(View.VISIBLE);
                rcOrderList.setVisibility(View.INVISIBLE);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListProductsAdapter.notifyDataSetChanged();
                    }
                });



            }
        });

        findViewById(R.id.btn_Lnext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> Productid_ar = new ArrayList<String>();;

                Utils.getInstance().hideKeyboard(Activity_RouteLoading.this);

                for (int i = 0; i < AllListProducts.size(); i++) {

                    if(AllListProducts.get(i).getEl_isLoaded().equals("Loaded")){
                        Productid_ar.add(AllListProducts.get(i).getEl_isLoaded());
                    }

                }

                System.out.println("Productid_ar-----"+ String.valueOf(Productid_ar.size()));

                if( Productid_ar.size() > 0){


                    Date date1 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String currentDate1 = sdf.format(date1);

                    JSONObject jsonOrder;
                    JSONObject load_json;
                    JSONArray TruckArray = new JSONArray();


                    for (int i = 0; i < listOrders.size(); i++) {
                        JSONArray loadArray = new JSONArray();

                        String IsLoad = listOrders.get(i).getPr_orderIsLoad();
                        String orderID = listOrders.get(i).getPr_orderID_();
                        String orderNum = listOrders.get(i).getPr_orderNum();
                        String orderRequired = listOrders.get(i).getPr_orderRequired();
                        String orderAmt = listOrders.get(i).getPr_orderAmt();
                        String CustName = listOrders.get(i).getPr_orderCust();
                        String CustID= listOrders.get(i).getPr_orderCustID();
                        String warehouseid= listOrders.get(i).getPr_warehouseid();

                        if (IsLoad.equals("Loaded")||IsLoad.equals("Partially Loaded")) {

                            for (int j = 0; j < AllListProducts.size(); j++) {

                                String Products_OID = AllListProducts.get(j).getEl_OID();

                                String irl = AllListProducts.get(j).getEl_isLoaded();

                                if (irl.equals("Loaded")) {

                                    if(orderID.equals(Products_OID)){

                                        System.out.println("El_OID---"+Products_OID);
                                        System.out.println("orderID---"+orderID);

                                        load_json = new JSONObject();
                                        try {
                                            load_json.put("productid", AllListProducts.get(j).getEl_productID());
                                            load_json.put("productname", AllListProducts.get(j).getEl_proName());
                                            load_json.put("prodqty",  AllListProducts.get(j).getEl_proQty());
                                            load_json.put("isloaded", true);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        loadArray.put(load_json);

                                    }


                                }




                            }

                            jsonOrder = new JSONObject();
                            try {
                                jsonOrder.put("orderid", orderID);
                                jsonOrder.put("ordernumber", orderNum);
                                jsonOrder.put("requiredqty", orderRequired);
                                jsonOrder.put("totalamount", orderAmt);
                                jsonOrder.put("expectdatefulfill", currentDate1);
                                jsonOrder.put("warehouseid", warehouseid);
                                jsonOrder.put("customerid", CustID);
                                jsonOrder.put("customername", CustName);
                                jsonOrder.put("isloaded", true);
                                jsonOrder.put("orderprodarr", loadArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            TruckArray.put(jsonOrder);
                        }
                    }

                    System.out.println("TruckLoading---"+TruckArray);

                    Bundle bundle = new Bundle();
                    bundle.putString("StopJSON",StopJSON.toString());
                    bundle.putString("TruckJSON",TruckArray.toString());
                    Intent in = new Intent(Activity_RouteLoading.this, Activity_RoutePayment.class);
                    in.putExtras(bundle);
                    startActivity(in);

                }else {
                    Toast.makeText(Activity_RouteLoading.this, "Orders are not loaded in the truck!", Toast.LENGTH_SHORT).show();
                }



            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        OrdersList(Route_ID);

    }


    @SuppressLint("NotifyDataSetChanged")
    private void OrdersList(String RouteID) {
        try {
            App.getInstance().GetPlannedOrder(RouteID,token,new Callback(){

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
                          String res_order = response.body().string();
                        try {
                            JSONObject jbOrder = new JSONObject(res_order);
                            String originaddress = jbOrder.getString("originaddress");
                            JSONArray jrOrders = jbOrder.getJSONArray("getDraftedRouteOrdersDetails");
                            if(listOrders.size()>0) listOrders.clear();
                            boolean isExsits =false;
                            for (int j=0; j<jrOrders.length(); j++) {
                                JSONObject jbOrders = jrOrders.getJSONObject(j);
                                String orderSID_ = jbOrders.getString("orderID");
                                String totalAmount = jbOrders.getString("totalAmount");
                                String qty = jbOrders.getString("qty");
                                String customerName = jbOrders.getString("customerName");
                                String customerID = jbOrders.getString("customerID");
                                String orderNumber = jbOrders.getString("orderNumber");
                                String warehouseid = jbOrders.getString("warehouseid");

                                if(listOrders.size()>0){

                                    for (int k = 0; k < listOrders.size(); k++) {

                                        if(listOrders.get(k).getPr_orderID_().equals(orderSID_)){

                                            int getProduct = Integer.parseInt(listOrders.get(k).getPr_orderRequired());

                                            getProduct += Integer.parseInt(qty);

                                            listOrders.get(k).setPr_orderRequired(String.valueOf(getProduct));

                                            k= listOrders.size();
                                            isExsits= false;

                                        }else {
                                            isExsits =true;
                                        }
                                    }
                                }else {
                                    isExsits =true;
                                }
                                if(isExsits){
                                    listOrders.add(new Model_LoadOrderList(orderSID_,orderNumber,qty,"0",totalAmount,customerName,"NotLoaded",customerID,warehouseid));

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListOrderAdapter.notifyDataSetChanged();
                                    }
                                });

                            }

                            if(AllListProducts.size()>0) AllListProducts.clear();
                            for (int j=0; j<jrOrders.length(); j++) {
                                JSONObject jbOrders = jrOrders.getJSONObject(j);
                                String orderID_ = jbOrders.getString("orderID");
                                String qty = jbOrders.getString("qty");
                                String productName = jbOrders.getString("productName");
                                //String totalAmount = jbOrders.getString("totalAmount");
                                String totalAmount = jbOrders.getString("lineAmount");
                                String productID = jbOrders.getString("productID");
                                String orderedOn = jbOrders.getString("orderedOn");
                                //String isLoaded = jbOrders.getString("isLoaded");

                                AllListProducts.add(new Model_ProductsDListItem(productName,qty,orderedOn,"NotLoaded",originaddress,orderID_,productID,totalAmount));


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListProductsAdapter.notifyDataSetChanged();
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
    }

    public void AddLoadAlert(String position, String ProName, String ProQty, String ProAdd, String LoadIs, String LORder, String LPRoducts) {
        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RouteLoading.this);
                LayoutInflater inflater = Activity_RouteLoading.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_addload_alert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_LoadMulti = v.findViewById(R.id.btn_LoadMulti);
               // RelativeLayout addWarehse_rll = v.findViewById(R.id.addWarehse_rll);
                AppCompatTextView pd_name_tv = v.findViewById(R.id.pd_name_tv);
                AppCompatTextView req_tv = v.findViewById(R.id.req_tv);
                AppCompatTextView req2_tv = v.findViewById(R.id.req2_tv);
                AppCompatTextView tv_qty = v.findViewById(R.id.tv_qty);
                AppCompatTextView warehouse_tv = v.findViewById(R.id.warehouse_tv);
                req_tv.setText("Requested quantity : "+ProQty);
                req2_tv.setText("Loaded quantity : "+ProQty);
                tv_qty.setText(ProQty);
                pd_name_tv.setText(ProName);
                warehouse_tv.setText("Warehouse Location : "+ProAdd);

                if(LoadIs.equals("Loaded")){
                    btn_LoadMulti.setText("UnLoad Truck");
                }else {
                    btn_LoadMulti.setText("Load Truck");
                }

              /*  mMultiRecyclerView = v.findViewById(R.id.rc_WarehouseLocItems);

                AddMultiadapter = new Adapter_AddMultiWarehouseItem(Activity_RouteLoading.this,AddMultilist);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_RouteLoading.this);
                mMultiRecyclerView.setLayoutManager(layoutManager);
                mMultiRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mMultiRecyclerView.addItemDecoration(new ItemDividerDecoration(Activity_RouteLoading.this, LinearLayoutManager.VERTICAL));
                mMultiRecyclerView.setAdapter(AddMultiadapter);

                mMultiRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_RouteLoading.this, mMultiRecyclerView, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        view.findViewById(R.id.Wdelete_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddMultilist.remove(position);
                                AddMultiadapter.notifyItemChanged(position);
                            }
                        });




                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));

*/

                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_LoadMulti.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        if(LoadIs.equals("Loaded")){

                            OrderProductsList.get(Integer.parseInt(position)).setOrderProItem_isLoaded("NotLoaded");
                            OrderProductsAdapter.notifyDataSetChanged();

                            for (int i = 0; i < AllListProducts.size(); i++) {

                                if(AllListProducts.get(i).getEl_OID().equals(LORder)) {

                                    if (AllListProducts.get(i).getEl_productID().equals(LPRoducts)) {
                                        AllListProducts.get(i).setEl_isLoaded("NotLoaded");
                                        AddedList.remove(0);
                                    }

                                }
                            }


                        }else {

                            OrderProductsList.get(Integer.parseInt(position)).setOrderProItem_isLoaded("Loaded");
                            OrderProductsAdapter.notifyDataSetChanged();

                            for (int i = 0; i < AllListProducts.size(); i++) {

                                if(AllListProducts.get(i).getEl_OID().equals(LORder)) {

                                    if (AllListProducts.get(i).getEl_productID().equals(LPRoducts)) {
                                        AllListProducts.get(i).setEl_isLoaded("Loaded");
                                        AddedList.add("Loaded");
                                    }

                                }
                            }

                        }


                        dialog.dismiss();
                    }
                });

             /*   addWarehse_rll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddMultilist.add(new Model_AddMultiWarehouseItem("Regional Warehouse,38473,US","1"));
                        AddMultiadapter.notifyDataSetChanged();
                    }
                });*/

             /*
                AddMultilist.add(new Model_AddMultiWarehouseItem("Zonal Warehouse,38473,US","2"));
                AddMultiadapter.notifyDataSetChanged();*/

            }
        });
    }
    public void OrderProductsListAlert(String rdOrderID, String IsLoad, String Order_position) {

        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {

                AlertDialog Progress_dialog_;

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RouteLoading.this);
                LayoutInflater inflater = Activity_RouteLoading.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_order_products_list,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                AlertDialog.Builder builder_ = new AlertDialog.Builder(Activity_RouteLoading.this, R.style.LoadingStyle);
                builder_.setCancelable(false);
                LayoutInflater layoutInflater1 = Activity_RouteLoading.this.getLayoutInflater();
                View v1 = layoutInflater1.inflate(R.layout.layout_avi, null);
                builder_.setView(v1);
                Progress_dialog_ = builder_.create();
                Progress_dialog_.show();

                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                AppCompatImageView close_img = v.findViewById(R.id.close_img);

                rcOrderProductsView = v.findViewById(R.id.rc_orderProducts);

                OrderProductsAdapter = new Adapter_OrderProducts(Activity_RouteLoading.this, OrderProductsList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_RouteLoading.this);
                rcOrderProductsView.setLayoutManager(layoutManager);
                rcOrderProductsView.setItemAnimator(new DefaultItemAnimator());
                rcOrderProductsView.addItemDecoration(new ItemDividerDecoration(Activity_RouteLoading.this, LinearLayoutManager.VERTICAL));
                rcOrderProductsView.setAdapter(OrderProductsAdapter);

                rcOrderProductsView.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_RouteLoading.this, rcOrderProductsView, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        view.findViewById(R.id.img_Prefresh).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddLoadAlert(
                                        String.valueOf(position),
                                        OrderProductsList.get(position).getOrderProItem_proName(),
                                        OrderProductsList.get(position).getOrderProItem_proQty(),
                                        OrderProductsList.get(position).getOrderProItem_Address(),
                                        OrderProductsList.get(position).getOrderProItem_isLoaded(),
                                        OrderProductsList.get(position).getOrderProItem_OrderID(),
                                        OrderProductsList.get(position).getOrderProItem_ProductID());
                            }
                        });

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

                        if(AddedList.size()>0){

                            if(OrderProductsList.size() ==AddedList.size()){
                                listOrders.get(Integer.parseInt(Order_position)).setPr_orderIsLoad("Loaded");
                                ListOrderAdapter.notifyItemChanged(Integer.parseInt(Order_position));

                            }else {
                                listOrders.get(Integer.parseInt(Order_position)).setPr_orderIsLoad("Partially Loaded");
                                ListOrderAdapter.notifyItemChanged(Integer.parseInt(Order_position));
                            }
                        }else {
                            listOrders.get(Integer.parseInt(Order_position)).setPr_orderIsLoad("NotLoaded");
                            ListOrderAdapter.notifyItemChanged(Integer.parseInt(Order_position));
                        }

                        listOrders.get(Integer.parseInt(Order_position)).setPr_orderOntruck(String.valueOf(AddedList.size()));
                        ListOrderAdapter.notifyItemChanged(Integer.parseInt(Order_position));

                        System.out.println("---EyeList.size--"+ OrderProductsList.size());
                        System.out.println("---AddedList.size--"+AddedList.size());

                    }
                });

                if(OrderProductsList.size()>0) OrderProductsList.clear();
                if(AddedList.size()>0) AddedList.clear();

                for (int i = 0; i < AllListProducts.size(); i++) {

                    if(AllListProducts.get(i).getEl_OID().equals(rdOrderID)){

                        OrderProductsList.add(new Model_OrderProductsList(AllListProducts.get(i).getEl_proName(), AllListProducts.get(i).getEl_proQty(), AllListProducts.get(i).getEl_DeliveryOn(), AllListProducts.get(i).getEl_isLoaded(), AllListProducts.get(i).getEl_Address(), AllListProducts.get(i).getEl_OID(), AllListProducts.get(i).getEl_productID()));

                       if(AllListProducts.get(i).getEl_isLoaded().equals("Loaded")){
                           AddedList.add(AllListProducts.get(i).getEl_isLoaded());
                       }

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

                }

            }
        });
    }

    private String getRouteID() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteId_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteId_pref, null);
        }
        return "0";
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}