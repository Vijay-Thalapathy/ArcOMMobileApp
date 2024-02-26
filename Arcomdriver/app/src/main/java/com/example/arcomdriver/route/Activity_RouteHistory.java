package com.example.arcomdriver.route;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_RouteHistory;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_RouteHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_RouteHistory extends Activity_Menu {
    private CoordinatorLayout cl;

    Adapter_RouteHistory adapter;
    ArrayList<Model_RouteHistory> listRoute = new ArrayList<>();
    private RecyclerView mRecyclerView;
    LottieAnimationView img_noRoute;

    AppCompatTextView txt_no_record;
    private SearchView searchBar;
    ArrayList<Model_RouteHistory> filteredModelList = new ArrayList<>();
    private SwipeRefreshLayout srl;
    Spinner RtStatus_sp;

    String user_id,token,user_email,SelectedDate;
    AppCompatTextView et_Date,rt_total_tv,rt_assigned_tv,rt_Unassigned_tv,rt_completed_tv,rt_iprogress_tv;
    static final int DATE_PICKER1_ID = 1;
    private int mYear, mMonth, mDay;

    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusName = new ArrayList<>();

    public AlertDialog Progress_dialog;


    private SharedPreferences rdSharedPreferences;
    public static final String rdPref = "rdPreferences";
    public static final String RouteId_pref = "RouteId_pref";
    public static final String RouteDriverName_pref = "RouteDriverName_pref";
    public static final String RouteStatus_pref = "RouteStatus_pref";
    public static final String RouteDriverID_pref = "RouteDriverID_pref";
    public static final String RouteTruckID_pref = "RouteTruckID_pref";

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_customerlist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_routehistory, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Routes");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RouteHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_RouteHistory.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


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
                    user_email = user_c.getString(user_c.getColumnIndex("Email"));

                }

            }
        } finally {
            sqLiteController1.close();
        }


        cl = findViewById(R.id.cl);
        srl = findViewById(R.id.swipe);
        searchBar = (SearchView) findViewById(R.id.search_rt_);
        mRecyclerView = findViewById(R.id.rt_recycler);
        img_noRoute = findViewById(R.id.img_noRoute);
        txt_no_record = findViewById(R.id.txt_no_record);
        RtStatus_sp = findViewById(R.id.RtStatus_sp);
        et_Date = findViewById(R.id.et_Date);
        rt_total_tv = findViewById(R.id.rt_total_tv);
        rt_assigned_tv = findViewById(R.id.rt_assigned_tv);
        rt_Unassigned_tv = findViewById(R.id.rt_Unassigned_tv);
        rt_completed_tv = findViewById(R.id.rt_completed_tv);
        rt_iprogress_tv = findViewById(R.id.rt_iprogress_tv);

        Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd");
        //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
        SelectedDate = sd2.format(date2);
        et_Date.setText(SelectedDate);

        findViewById(R.id.et_Date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_RouteHistory.this);
                showDialog(DATE_PICKER1_ID);
            }
        });


        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_RouteHistory.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_RouteHistory.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                GetRouteList(SelectedDate,SelectedDate,"All");
            }
        });

        adapter = new Adapter_RouteHistory(listRoute);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {


                if(filteredModelList.size()>0){
                    rdSharedPreferences = getSharedPreferences(rdPref,MODE_PRIVATE);
                    SharedPreferences.Editor editor = rdSharedPreferences.edit();
                    editor.putString(RouteId_pref,  String.valueOf(filteredModelList.get(position).getRouteID()));
                    editor.putString(RouteDriverName_pref,  String.valueOf(filteredModelList.get(position).getDriverName()));
                    editor.putString(RouteStatus_pref,  String.valueOf(filteredModelList.get(position).getRouteStatus()));
                    editor.putString(RouteDriverID_pref,  String.valueOf(filteredModelList.get(position).getDriverid()));
                    editor.putString(RouteTruckID_pref,  String.valueOf(filteredModelList.get(position).getTruckid()));
                    editor.apply();

                    view.findViewById(R.id.btn_routeTrip).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(filteredModelList.get(position).getRouteStatus().equals("Draft") || filteredModelList.get(position).getRouteStatus().equals("Planned")){
                                Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);
                            }else if(filteredModelList.get(position).getRouteStatus().equals("In-Transit") || filteredModelList.get(position).getRouteStatus().equals("Completed")){
                              /*  Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);*/
                            }

                        }
                    });


                    if(filteredModelList.get(position).getRouteStatus().equals("Draft") || filteredModelList.get(position).getRouteStatus().equals("Planned")){
                             /*   Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);*/
                    }else if(filteredModelList.get(position).getRouteStatus().equals("In-Transit") || filteredModelList.get(position).getRouteStatus().equals("Completed")){
                        Intent intent = new Intent(Activity_RouteHistory.this, Activity_Route360.class);
                        startActivity(intent);
                    }



                }else {
                    rdSharedPreferences = getSharedPreferences(rdPref,MODE_PRIVATE);
                    SharedPreferences.Editor editor = rdSharedPreferences.edit();
                    editor.putString(RouteId_pref,  String.valueOf(listRoute.get(position).getRouteID()));
                    editor.putString(RouteDriverName_pref,  String.valueOf(listRoute.get(position).getDriverName()));
                    editor.putString(RouteStatus_pref,  String.valueOf(listRoute.get(position).getRouteStatus()));
                    editor.putString(RouteDriverID_pref,  String.valueOf(listRoute.get(position).getDriverid()));
                    editor.putString(RouteTruckID_pref,  String.valueOf(listRoute.get(position).getTruckid()));
                    editor.apply();


                    view.findViewById(R.id.btn_routeTrip).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(listRoute.get(position).getRouteStatus().equals("Draft") || listRoute.get(position).getRouteStatus().equals("Planned")){
                                Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);
                            }else if(listRoute.get(position).getRouteStatus().equals("In-Transit") || listRoute.get(position).getRouteStatus().equals("Completed")){
                              /*  Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);*/
                            }
                        }
                    });

                    if(listRoute.get(position).getRouteStatus().equals("Draft") || listRoute.get(position).getRouteStatus().equals("Planned")){
                              /*  Intent intent = new Intent(Activity_RouteHistory.this, Activity_RouteSOD.class);
                                startActivity(intent);*/
                    }else if(listRoute.get(position).getRouteStatus().equals("In-Transit") || listRoute.get(position).getRouteStatus().equals("Completed")){
                        Intent intent = new Intent(Activity_RouteHistory.this, Activity_Route360.class);
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something
                //SEARCH FILTER
                filteredModelList = filter(listRoute, newText.toString());
                adapter.setFilter(filteredModelList);
                if(filteredModelList.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    img_noRoute.setVisibility(View.GONE);
                    txt_no_record.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    img_noRoute.setVisibility(View.VISIBLE);
                    txt_no_record.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                Utils.getInstance().hideKeyboard(Activity_RouteHistory.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                GetRouteList(SelectedDate,SelectedDate,"All");
            }
        });

        RtStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String str_orderStatus = arStatusName.get(position);
                Progress_dialog.show();
                if(str_orderStatus.equals("All")){
                    GetRouteList(SelectedDate,SelectedDate,"All");
                }else if(str_orderStatus.equals("Assigned")){
                    GetRouteList(SelectedDate,SelectedDate,"Assigned");
                }else if(str_orderStatus.equals("Un-Assigned")) {
                    GetRouteList(SelectedDate,SelectedDate, "Un-Assigned");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        arStatusName.add("All");
        arStatusName.add("Assigned");
        arStatusName.add("Un-Assigned");
        arStatusID.add("0");
        arStatusID.add("1");
        arStatusID.add("2");
        ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_RouteHistory.this, R.layout.spinner_item,arStatusName);
        adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RtStatus_sp.setAdapter(adapterBType);
            }
        });

        GetKPI(SelectedDate);

    }

    @Override
    protected Dialog onCreateDialog(final  int id) {
        DatePickerDialog datePickerDialog = null;
        switch (id) {
            case DATE_PICKER1_ID:
                Calendar d = Calendar.getInstance();
                d.add(d.MONTH,2);
                datePickerDialog = new DatePickerDialog(this, pickerListener1, mYear, mMonth,mDay);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                // datePickerDialog.getDatePicker().setMaxDate(d.getTimeInMillis() - 1000);
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String Date_evt = new StringBuilder()
                            .append(selectedYear)
                            .append("-")
                            .append(selectedMonth+1)
                            .append("-")
                            .append(selectedDay).toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(Date_evt);
                        SelectedDate =new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    et_Date.setText(SelectedDate.toString());
                    Progress_dialog.show();
                    GetKPI(SelectedDate);
                    GetRouteList(SelectedDate,SelectedDate,"All");

                }
            });



        }
    };

    private void GetKPI(String date_str) {
        try {
            App.getInstance().GetKpi(date_str,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println("res------"+response.toString());
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        System.out.println("res------"+res.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            if(succeeded){
                                JSONObject data = jsonObject.getJSONObject("data");
                                String totalRoute = data.getString("totalRoute");
                                String unAssigned = data.getString("unAssigned");
                                String cancelled = data.getString("cancelled");
                                String inTransit = data.getString("inTransit");
                                String completed = data.getString("completed");
                                String planned = data.getString("planned");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int TotalAss = Integer.parseInt(inTransit)+Integer.parseInt(completed)+Integer.parseInt(planned);
                                        rt_total_tv.setText(totalRoute);
                                        rt_assigned_tv.setText(String.valueOf(TotalAss));
                                        rt_Unassigned_tv.setText(unAssigned);
                                        rt_completed_tv.setText(completed);
                                        rt_iprogress_tv.setText(inTransit);

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
    private void GetRouteList(String SDate_st,String EDate_st,String Status_) {
        if (srl.isRefreshing()) srl.setRefreshing(false);
        try {
            App.getInstance().GetRouteList(SDate_st,EDate_st,token,new Callback(){

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
                            img_noRoute.setVisibility(View.VISIBLE);
                            txt_no_record.setVisibility(View.VISIBLE);
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
                        System.out.println("res------"+response.toString());
                        String res = response.body().string();
                        System.out.println("res------"+res.toString());
                        try {
                            JSONArray jsAry = new JSONArray(res);
                            if(listRoute.size()>0)listRoute.clear();
                            if(jsAry.length()>0){
                                for (int i=0; i<jsAry.length(); i++) {
                                    JSONObject jbOrder = jsAry.getJSONObject(i);
                                    String routeID = jbOrder.getString("routeid");
                                    String routeName = jbOrder.getString("route");
                                    String routeStatus = jbOrder.getString("routestatus");
                                    String driverName = jbOrder.getString("drivername");
                                    String driverid = jbOrder.getString("driverid");
                                    String truckid = jbOrder.getString("truckid");
                                    String totalrouteorders = jbOrder.getString("totalrouteorders");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Status_.equals("All")){
                                                listRoute.add(new Model_RouteHistory(routeID,routeName, routeStatus, driverName,totalrouteorders, totalrouteorders,driverid,truckid));
                                            }else if(Status_.equals("Assigned")){
                                                listRoute.add(new Model_RouteHistory(routeID,routeName, routeStatus, driverName, totalrouteorders, totalrouteorders,driverid,truckid));
                                            }else if(Status_.equals("Un-Assigned")) {
                                                if(routeStatus.equals("unAssigned")){
                                                    listRoute.add(new Model_RouteHistory(routeID,routeName, routeStatus, driverName, totalrouteorders, totalrouteorders,driverid,truckid));

                                                }
                                            }


                                            if(listRoute.size()>0){
                                                img_noRoute.setVisibility(View.GONE);
                                                txt_no_record.setVisibility(View.GONE);
                                            }else {
                                                img_noRoute.setVisibility(View.VISIBLE);
                                                txt_no_record.setVisibility(View.VISIBLE);
                                            }

                                            adapter.notifyDataSetChanged();
                                            Collections.reverse(listRoute);


                                        }
                                    });
                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        img_noRoute.setVisibility(View.VISIBLE);
                                        txt_no_record.setVisibility(View.VISIBLE);
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



    private ArrayList<Model_RouteHistory> filter(ArrayList<Model_RouteHistory> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_RouteHistory> filteredModelList = new ArrayList<>();

        for (Model_RouteHistory model : models) {

            final String getOrder_name = model.getRouteName().toLowerCase();
            final String getOrder_status = model.getDriverName().toLowerCase();

            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);

            }else if (getOrder_status.contains(search_txt)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }








}
