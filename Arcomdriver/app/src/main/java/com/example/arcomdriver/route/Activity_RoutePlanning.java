package com.example.arcomdriver.route;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ItemProducts;
import com.example.arcomdriver.adapter.Adapter_POrderList;
import com.example.arcomdriver.adapter.Adapter_StopList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_CustomerStop;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.model.Model_ItemProducts;
import com.example.arcomdriver.model.Model_PorderList;
import com.example.arcomdriver.model.Model_StopList;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_RoutePlanning extends Activity_Menu {
    private CoordinatorLayout cl;
    String user_id,token,Email,Route_ID;

    public AlertDialog Progress_dialog;

    private RecyclerView rc_StopList,rc_OrderList;

    Adapter_StopList stopAdapter;
    ArrayList<Model_StopList> listStop = new ArrayList<>();

    Adapter_POrderList OrderAdapter;
    ArrayList<Model_PorderList> listOrder= new ArrayList<>();

    AppCompatTextView pr_routeName_tv,pr_driverName_tv,pr_orderCount_tv,pr_custCount_tv,pr_routeStatus_tv;


    private SharedPreferences rdSharedPreferences;
    public static final String rdPref = "rdPreferences";

    Spinner spAddnxt;

    private ArrayList<String> arStopID = new ArrayList<>();
    private ArrayList<String> arStopName = new ArrayList<>();
    private ArrayList<String> arSeq = new ArrayList<>();

    AppCompatEditText et_stopsName,et_addressLine1,et_reason;
    AppCompatTextView verifyView_tv;

    String Str_arStopID,Str_arSeq;

    String stopFlag ="0";

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_routesod);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_routesplanning, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Route Planning (SOD)");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Route_ID =getRouteID();

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RoutePlanning.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_RoutePlanning.this.getLayoutInflater();
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


        rc_StopList = findViewById(R.id.rc_StopList);
        rc_OrderList = findViewById(R.id.rc_OrderList);

        pr_routeName_tv = findViewById(R.id.pr_routeName_tv);
        pr_driverName_tv = findViewById(R.id.pr_driverName_tv);
        pr_orderCount_tv = findViewById(R.id.pr_orderCount_tv);
        pr_custCount_tv = findViewById(R.id.pr_custCount_tv);
        pr_routeStatus_tv = findViewById(R.id.pr_routeStatus_tv);

        stopAdapter = new Adapter_StopList(listStop);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rc_StopList.setLayoutManager(layoutManager);
        rc_StopList.setItemAnimator(new DefaultItemAnimator());
        rc_StopList.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rc_StopList.setAdapter(stopAdapter);

        rc_StopList.addOnItemTouchListener(new RecyclerViewTouchListener(this, rc_StopList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));


        OrderAdapter = new Adapter_POrderList(listOrder);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rc_OrderList.setLayoutManager(layoutManager1);
        rc_OrderList.setItemAnimator(new DefaultItemAnimator());
        rc_OrderList.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rc_OrderList.setAdapter(OrderAdapter);

        rc_OrderList.addOnItemTouchListener(new RecyclerViewTouchListener(this, rc_OrderList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));




        AppCompatTextView btn_stop = findViewById(R.id.btn_stop);
        AppCompatTextView btn_Order = findViewById(R.id.btn_Order);

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

               // Progress_dialog.show();
                btn_stop.setBackgroundResource(R.drawable.shape_btn1);
                btn_Order.setBackgroundResource(R.drawable.shape_btn4);
                rc_StopList.setVisibility(View.VISIBLE);
                rc_OrderList.setVisibility(View.GONE);
            }
        });
        btn_Order.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                //  Progress_dialog.show();
                btn_Order.setBackgroundResource(R.drawable.shape_btn3);
                btn_stop.setBackgroundResource(R.drawable.shape_btn2);
                rc_StopList.setVisibility(View.GONE);
                rc_OrderList.setVisibility(View.VISIBLE);


            }
        });

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);

                Date date1 = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDate1 = sdf.format(date1);

                JSONObject stops_json = new JSONObject();
                JSONArray stopsArray = new JSONArray();
                for (int i = 0; i < listStop.size(); i++) {

                    String irl = listStop.get(i).getSt_count();

                    if (irl.equals("1")) {

                        stops_json = new JSONObject();
                        try {
                            stops_json.put("routeDayID", Route_ID);//route day ID
                            stops_json.put("stopID", listStop.get(i).getSt_stopID());
                            stops_json.put("type", listStop.get(i).getSt_type());
                            stops_json.put("sequence",  listStop.get(i).getSt_sequence());
                            stops_json.put("address",  listStop.get(i).getSt_address());
                            stops_json.put("stopName", listStop.get(i).getSt_name());
                            stops_json.put("stopNameID", listStop.get(i).getSt_stopID());
                            stops_json.put("distance", "0");
                            stops_json.put("duration", "0");
                            stops_json.put("expectedDeliveryOn", currentDate1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        stopsArray.put(stops_json);
                    }
                }

                System.out.println("stopsPlaning2---"+stopsArray);

                Bundle bundle = new Bundle();
                bundle.putString("StopJSON",stopsArray.toString());
                Intent in = new Intent(Activity_RoutePlanning.this, Activity_RouteLoading.class);
                in.putExtras(bundle);
                startActivity(in);

              //  PostAddStops2();

            }
        });
        findViewById(R.id.btn_Addstop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStopsAlert();

            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        GetPlannedOrder(Route_ID);
        GetPlannedStops(Route_ID);


    }

    private void GetPlannedOrder(String RouteID) {
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
                        String res = response.body().string();
                        try {
                            JSONObject jbOrder = new JSONObject(res);
                            System.out.println("Planning---"+jbOrder.toString());
                            String routeName = jbOrder.getString("routename");
                           // String routeStatus = jbOrder.getString("routeStatus");
                            //String driverName = jbOrder.getString("driverName");

                            JSONArray jrOrders = jbOrder.getJSONArray("getDraftedRouteOrdersDetails");
                            if(listOrder.size()>0)listOrder.clear();
                            boolean isExsits =false;
                            for (int j=0; j<jrOrders.length(); j++) {
                                JSONObject jbOrders = jrOrders.getJSONObject(j);
                                String orderID_ = jbOrders.getString("orderID");
                                String totalAmount = jbOrders.getString("totalAmount");
                                String qty = jbOrders.getString("qty");
                                String customerID = jbOrders.getString("customerID");
                                String orderNumber = jbOrders.getString("orderNumber");
                                String orderStatus = jbOrders.getString("orderStatus");
                                String orderedOn = jbOrders.getString("orderedOn");

                                if(listOrder.size()>0){

                                    for (int k = 0; k < listOrder.size(); k++) {

                                        if(listOrder.get(k).getpOrderID().equals(orderID_)){

                                            int getProduct = Integer.parseInt(listOrder.get(k).getpOrderQty());

                                            getProduct += Integer.parseInt(qty);

                                            listOrder.get(k).setpOrderQty(String.valueOf(getProduct));

                                            k=listOrder.size();
                                            isExsits= false;

                                        }else {
                                            isExsits =true;
                                        }
                                    }
                                }else {
                                    isExsits =true;
                                }
                                if(isExsits){
                                    listOrder.add(new Model_PorderList(orderID_,"SO "+orderNumber,qty,totalAmount,orderedOn));

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        OrderAdapter.notifyDataSetChanged();
                                    }
                                });

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    pr_routeName_tv.setText(routeName);
                                    pr_driverName_tv.setText(String.valueOf(getRouteDriverName()));
                                    pr_orderCount_tv.setText(String.valueOf(listOrder.size()));
                                    pr_custCount_tv.setText(String.valueOf(listOrder.size()));
                                   // pr_routeStatus_tv.setText(String.valueOf(getRouteStatus()));

                                    if(String.valueOf(getRouteStatus()).equals("Draft")){
                                        pr_routeStatus_tv.setText("Planned");
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.colorGray));
                                    }else if(String.valueOf(getRouteStatus()).equals("Planned")){
                                        pr_routeStatus_tv.setText("Planned");
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorOrange));
                                    }else if(String.valueOf(getRouteStatus()).equals("In-Transit")){
                                        pr_routeStatus_tv.setText("Live");
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorGreen));
                                    }else if(String.valueOf(getRouteStatus()).equals("Completed")){
                                        pr_routeStatus_tv.setText("Completed");
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorLightGreen));
                                    }

                                 /*   if(routeStatus.equals("Draft")){
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.colorGray));
                                    }else if(routeStatus.equals("Planned")){
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorOrange));
                                    }else if(routeStatus.equals("In-Transit")){
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorGreen));
                                    }else if(routeStatus.equals("Completed")){
                                        pr_routeStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorLightGreen));
                                    }*/
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
    private void GetPlannedStops(String RouteID) {

        try {
            App.getInstance().GetPlannedStops(RouteID,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONArray jry = new JSONArray(res);
                            System.out.println("PlanningStops---"+jry.toString());

                            if(listStop.size()>0)listStop.clear();
                            int ShipTotal =0;
                            for (int j=0; j<jry.length(); j++) {
                                JSONObject jbStops = jry.getJSONObject(j);
                                String stopID = jbStops.getString("stopID");
                                String routeDayID = jbStops.getString("routeDayID");
                                String stopName = jbStops.getString("stopName");
                                String address = jbStops.getString("address");
                                String type = jbStops.getString("type");
                                String sequence = jbStops.getString("sequence");
                                ShipTotal += 1;

                                int finalShipTotal = ShipTotal;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listStop.add(new Model_StopList("0",stopName,address,stopID,sequence,type,routeDayID));
                                        Collections.sort(listStop, new Comparator<Model_StopList>() {
                                            @Override
                                            public int compare(Model_StopList lhs, Model_StopList rhs) {
                                                return lhs.getSt_sequence().compareTo(rhs.getSt_sequence());
                                            }
                                        });
                                        stopAdapter.notifyDataSetChanged();
                                        // Collections.reverse(listStop);
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


    public void AddStopsAlert() {

        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RoutePlanning.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateOrder.this);
                LayoutInflater inflater = Activity_RoutePlanning.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_addstop,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();

                AlertDialog.Builder builder2 = new AlertDialog.Builder(Activity_RoutePlanning.this, R.style.LoadingStyle);
                builder2.setCancelable(false);
                LayoutInflater layoutInflater1 = Activity_RoutePlanning.this.getLayoutInflater();
                View v1 = layoutInflater1.inflate(R.layout.layout_avi, null);
                builder2.setView(v1);
                Progress_dialog = builder2.create();
                Progress_dialog.show();

                spAddnxt =  v.findViewById(R.id.spAddnxt);
                et_stopsName =  v.findViewById(R.id.et_stopsName);
                et_addressLine1 =  v.findViewById(R.id.et_addressLine1);
                et_reason =  v.findViewById(R.id.et_reason);
                verifyView_tv =  v.findViewById(R.id.verifyView_tv);

                spAddnxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Str_arStopID = arStopID.get(position);
                        Str_arSeq = arSeq.get(position);
                        System.out.println("Str_arStopID--"+Str_arStopID);
                        System.out.println("Str_arSeq--"+Str_arSeq);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                v.findViewById(R.id.btn_AddStop).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Connectivity.isConnected(Activity_RoutePlanning.this) &&
                                Connectivity.isConnectedFast(Activity_RoutePlanning.this)) {
                            if (Utils.getInstance().checkIsEmpty(et_stopsName)) {
                                Toast.makeText(Activity_RoutePlanning.this, "Enter the Stop Name!", Toast.LENGTH_SHORT).show();
                               // Utils.getInstance().snackBarMessage(v,"Enter the Stop Name!");
                            } else if (Utils.getInstance().checkIsEmpty(et_addressLine1)) {
                                Toast.makeText(Activity_RoutePlanning.this, "Enter the Address!", Toast.LENGTH_SHORT).show();
                                //Utils.getInstance().snackBarMessage(v,"Enter the Address!");
                            }else if (Utils.getInstance().checkIsEmpty(et_reason)) {
                                Toast.makeText(Activity_RoutePlanning.this, "Enter the Reason!", Toast.LENGTH_SHORT).show();
                                //Utils.getInstance().snackBarMessage(v,"Enter the Reason");
                            } else if (Str_arStopID.equals("0")) {
                                Toast.makeText(Activity_RoutePlanning.this, "Select Stops!", Toast.LENGTH_SHORT).show();
                                //Utils.getInstance().snackBarMessage(v,"Select Stops!");
                            } else if (stopFlag.equals("0")) {
                                Toast.makeText(Activity_RoutePlanning.this, "Address Not Verified! Please enter the valid Address!! ", Toast.LENGTH_SHORT).show();
                                //Utils.getInstance().snackBarMessage(v,"Please Verify the Address!");
                            }else {
                                //Success
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("stopname",et_stopsName.getText().toString());
                                    json.put("address",et_addressLine1.getText().toString());
                                    json.put("notes",et_reason.getText().toString());
                                    json.put("routedayid",Route_ID);
                                    json.put("userId",user_id);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                System.out.println("Stops---"+json.toString());

                                postAddStops(json.toString(),et_stopsName.getText().toString(),et_addressLine1.getText().toString());

                                dialog.dismiss();
                            }
                        }else {
                            Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                        }
                    }
                });

                v.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                v.findViewById(R.id.verify_btn_clk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);

                        stopFlag ="0";

                        if (Utils.getInstance().checkIsEmpty(et_addressLine1)) {
                            Toast.makeText(Activity_RoutePlanning.this, "Please enter address!", Toast.LENGTH_SHORT).show();
                          //  Utils.getInstance().snackBarMessage(v,"Enter the Address!");
                        }else {
                            String Json_map= "{\"address\": {\r\n    \"regionCode\": \"US\",\r\n    \"addressLines\": [\r\n        \""+et_addressLine1.getText().toString()+"\"\r\n    ]\r\n}\r\n}";
                            System.out.println("Stops---"+Json_map.toString());
                            PostAddressMap(Json_map);
                        }

                    }
                });

                if (arStopID.size() > 0) arStopID.clear();
                if (arStopName.size() > 0) arStopName.clear();
                if (arSeq.size() > 0) arSeq.clear();
                arStopName.add("Select next stop");
                arStopID.add("0");
                arSeq.add("0");

                for (int j=0; j<listStop.size(); j++) {

                    String getSt_name = listStop.get(j).getSt_name();
                    String getSt_stopID = listStop.get(j).getSt_stopID();
                    String getSt_sequence = listStop.get(j).getSt_sequence();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arStopName.add(getSt_name);
                            arStopID.add(getSt_stopID);
                            arSeq.add(getSt_sequence);

                            ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_RoutePlanning.this, R.layout.spinner_item,arStopName);
                            adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spAddnxt.setAdapter(adapterBType);

                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }

                                }
                            });
                        }
                    });

                }
            }
        });
    }

    private void postAddStops(String json,String stopName,String Address) {
        Utils.getInstance().loadingDialog(Activity_RoutePlanning.this, "Please wait.");
        try {
            App.getInstance().PostAddStops(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().dismissDialog();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.getInstance().dismissDialog();
                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("SuccessStop---"+jsonObject);
                                    String data = jsonObject.getString("data");
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Activity_RoutePlanning.this, "Stop Added Successfully!", Toast.LENGTH_SHORT).show();
                                                Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        int selected_seq =Integer.parseInt(Str_arSeq);

                                                        if(listStop.size()==1){

                                                            listStop.add(new Model_StopList("1",stopName,Address,data,String.valueOf(selected_seq + 1),"stop",Route_ID));

                                                        }else if(listStop.size()>=2){

                                                            listStop.add(new Model_StopList("1",stopName,Address,data,String.valueOf(selected_seq + 1),"stop",Route_ID));


                                                            for (int j=selected_seq +1; j<listStop.size(); j++) {

                                                                listStop.get(j-1).setSt_sequence(String.valueOf(j+1));


                                                            }
                                                        }

                                                        Collections.sort(listStop, new Comparator<Model_StopList>() {
                                                            @Override
                                                            public int compare(Model_StopList lhs, Model_StopList rhs) {
                                                                return lhs.getSt_sequence().compareTo(rhs.getSt_sequence());
                                                            }
                                                        });
                                                        stopAdapter.notifyDataSetChanged();
                                                        // Collections.reverse(listStop);
                                                    }
                                                });

                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void PostAddStops2(String json,String stopName,String Address) {
        Utils.getInstance().loadingDialog(Activity_RoutePlanning.this, "Please wait.");
        try {
            App.getInstance().PostAddStops2(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().dismissDialog();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.getInstance().dismissDialog();
                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("SuccessStop---"+jsonObject);
                                    String data = jsonObject.getString("data");
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Activity_RoutePlanning.this, "Stop Added Successfully!", Toast.LENGTH_SHORT).show();
                                                Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                    }
                                                });

                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void PostAddressMap(String json) {
        Utils.getInstance().loadingDialog(Activity_RoutePlanning.this, "Please wait.");
        try {
            App.getInstance().PostAddressMap(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().dismissDialog();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        System.out.println("Success---");
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.getInstance().dismissDialog();
                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("Success---"+jsonObject);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("verdict");


                                    if (jsonObject2.getString("validationGranularity").equals("PREMISE") || jsonObject2.getString("validationGranularity").equals("ROUTE") ||  jsonObject2.getString("addressComplete").equals("true")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                stopFlag ="1";
                                                verifyView_tv.setText("Verified");
                                                verifyView_tv.setTextColor(getResources().getColor(R.color.ColorGreen));
                                                Toast.makeText(Activity_RoutePlanning.this, "Address Verified Successfully!", Toast.LENGTH_SHORT).show();
                                                Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);

                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            stopFlag ="0";
                                            verifyView_tv.setText("Not Verified");
                                            verifyView_tv.setTextColor(getResources().getColor(R.color.ColorRed));
                                            Utils.getInstance().hideKeyboard(Activity_RoutePlanning.this);
                                            Toast.makeText(Activity_RoutePlanning.this, "Please enter Valid address!!", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            }
                        });
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String getRouteID() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteId_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteId_pref, null);
        }
        return "0";
    }

    private String getRouteDriverName() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteDriverName_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteDriverName_pref, null);
        }
        return "N/A";
    }

    private String getRouteStatus() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteStatus_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteStatus_pref, null);
        }
        return "N/A";
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}