package com.example.arcomdriver.reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_OMSReportList;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_OmsReportsList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import java.util.ArrayList;

public class ActivityReportHistory extends Activity_Menu {

    private CoordinatorLayout cl;

    public static String  OrderId,token,user_email,user_id;
    private SQLiteController sqLiteController;

    private RecyclerView mRecyclerView;

    private LottieAnimationView txt_no_record;

    private Adapter_OMSReportList adapter_OmsReports;

    private ArrayList<Model_OmsReportsList> list_OmsReports = new ArrayList<>();


    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_routepayment);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_reports_history, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.GONE);
        In_download_ic.setVisibility(View.GONE);
        In_print_img.setVisibility(View.GONE);
        txt_page.setText("Reports");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        AppCompatButton btn_All = findViewById(R.id.btn_Oms);
        AppCompatButton btn_paid = findViewById(R.id.btn_warehouse);
        AppCompatButton btn_Unpaid = findViewById(R.id.btn_route);

        if (Activity_OrdersHistory.WarehouseFlag.equals("true")){
            //show Warehouse
            btn_paid.setVisibility(View.VISIBLE);
        }else {
            //Hide Warehouse
            btn_paid.setVisibility(View.GONE);
        }

        if (Activity_OrdersHistory.RouteFlag.equals("true")){
            //show Route
            btn_Unpaid.setVisibility(View.VISIBLE);
        }else {
            //Hide Route
            btn_Unpaid.setVisibility(View.GONE);
        }

        if (Activity_OrdersHistory.OrderFlag.equals("true")){
            //show OMS
            btn_All.setVisibility(View.VISIBLE);
        }else {
            //Hide OMS
            btn_All.setVisibility(View.GONE);
        }

        btn_All.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_All.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn_bg));
               // btn_All.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_paid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn5));
                btn_Unpaid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));


            }
        });
        btn_paid.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_paid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn7));
                btn_All.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));
                btn_Unpaid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));

            }
        });
        btn_Unpaid.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_Unpaid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn3));
                btn_All.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));
                btn_paid.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn5));

            }
        });


        mRecyclerView = findViewById(R.id.rcOmsList);
        txt_no_record = findViewById(R.id.txt_no_record);

        adapter_OmsReports = new Adapter_OMSReportList(list_OmsReports);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityReportHistory.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(ActivityReportHistory.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter_OmsReports);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(ActivityReportHistory.this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

             runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        if(list_OmsReports.get(position).getReports_id().equals("1")){
                            Intent intent = new Intent(ActivityReportHistory.this, Activity_SalesReportsDetails.class);
                            startActivity(intent);
                        }else if(list_OmsReports.get(position).getReports_id().equals("2")){
                            Bundle bundle = new Bundle();
                            bundle.putString("ReportFlag","2");
                            Intent in = new Intent(ActivityReportHistory.this, Activity_DeliveryItemReportsDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        } else{
                            Bundle bundle = new Bundle();
                            bundle.putString("ReportFlag","3");
                            Intent in = new Intent(ActivityReportHistory.this, Activity_DeliveryItemReportsDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        list_OmsReports.clear();
        list_OmsReports.add(new Model_OmsReportsList("1","Sales Order History"));
        list_OmsReports.add(new Model_OmsReportsList("2","Today's Delivery - Item View"));
        list_OmsReports.add(new Model_OmsReportsList("3","Today's Delivery - Order View"));
        adapter_OmsReports.notifyDataSetChanged();




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}