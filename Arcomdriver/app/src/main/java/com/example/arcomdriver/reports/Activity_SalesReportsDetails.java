package com.example.arcomdriver.reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ReportDetails;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_ReportsDetails;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class Activity_SalesReportsDetails extends Activity_Menu {
    private CoordinatorLayout cl;

    Adapter_ReportDetails adapter;
    ArrayList<Model_ReportsDetails> listDetails = new ArrayList<>();
    private RecyclerView mRecyclerView;


    LottieAnimationView img_noProducts;

    RelativeLayout filter_rll;

    AppCompatImageView filter_image;

    static final int DATE_PICKER1_ID = 1;
    static final int DATE_PICKER2_ID = 2;

    static final int DATE_PICKER3_ID = 3;
    static final int DATE_PICKER4_ID = 4;

    AppCompatTextView etFromDate,etToDate,etDFromDate,etDToDate;

    private int mYear, mMonth, mDay;

    String str_DeliveryRep,str_DeliveryRepID="0", str_salesRep,str_salesRepID="0",str_customer,str_customerID="0",str_fulfilmentID="0",str_fulfilment,user_id,token,str_ordersStatus="Choose Order Status";

    private ArrayList<String> arCustomerID,arCustomerName;
    private ArrayList<String> arFulfilmentName,arFulfillID,arOrdersFillName,arSalesRepName,arSalesRepID,arDeliveryPersonName,arDeliveryPersonID;

    String settingsvalue ="0";
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_productlist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_salesreportdetails, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Sales Order History");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                }

            }
        } finally {
            sqLiteController1.close();
        }



        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);




        cl = findViewById(R.id.cl);
        filter_rll = findViewById(R.id.filter_rll);
        filter_image = findViewById(R.id.filter_image);
        mRecyclerView = findViewById(R.id.rc_reportsDetailsList);
        img_noProducts = findViewById(R.id.img_noProducts);

        adapter = new Adapter_ReportDetails(listDetails);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {



            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        filter_rll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterAlert();
            }
        });
        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterAlert();
            }
        });


        SQLiteController sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {

                Cursor C_In = sqLiteController.readTableInvoiceFormat();
                if (C_In.moveToFirst()) {
                    do {
                        String invoice_name = C_In.getString(C_In.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("AddinList")){

                            settingsvalue  = C_In.getString(C_In.getColumnIndex("invoice_value"));

                            System.out.println("----------settingsvalue---------"+settingsvalue);



                        }
                    } while (C_In.moveToNext());
                }


                arCustomerName = new ArrayList<String>();
                arCustomerName.add("Choose Customer");
                arCustomerID = new ArrayList<String>();
                arCustomerID.add("0");
                //Customer
                Cursor customer_c = sqLiteController.readTableCustomer();
                if (customer_c != null && customer_c.moveToFirst()) {
                    do {

                        String customer = String.valueOf(customer_c.getString(customer_c.getColumnIndex("customername")));
                        String status = String.valueOf(customer_c.getString(customer_c.getColumnIndex("status")));
                        if(!customer.equals("null")){
                            arCustomerName.add(customer_c.getString(customer_c.getColumnIndex("customername")));
                            arCustomerID.add(customer_c.getString(customer_c.getColumnIndex("Id")));


                        }

                    } while (customer_c.moveToNext());
                }


                arFulfilmentName = new ArrayList<String>();
                arFulfilmentName.add("Choose Fulfillment Type");
                arFulfillID = new ArrayList<String>();
                arFulfillID.add("0");
                //fulfill
                Cursor fulfill_c = sqLiteController.readTableFulfill();
                if (fulfill_c != null && fulfill_c.moveToFirst()) {
                    do {
                        String FullNAme_ = fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name"));


                        if (settingsvalue.contains("Warehouse")) {
                            if(FullNAme_.equals("Fulfillment warehouse")){
                                arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                                arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                            }
                        }

                        if (settingsvalue.contains("Route")) {
                            if(FullNAme_.equals("Ship via route")){
                                arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                                arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                            }

                            if(FullNAme_.equals("Ship via carrier")){
                                arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                                arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                            }
                        }

                        if(settingsvalue.contains("Order")) {
                            if(FullNAme_.equals("Direct delivery")){
                                arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                                arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                            }

                            if(FullNAme_.equals("Customer pickup")){
                                arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                                arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                            }
                        }

                        if(settingsvalue.contains("0")) {
                            arFulfilmentName.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_name")));
                            arFulfillID.add(fulfill_c.getString(fulfill_c.getColumnIndex("fulfillment_id")));
                        }

                    } while (fulfill_c.moveToNext());
                }


                arSalesRepName = new ArrayList<String>();
                arSalesRepName.add("Choose Sales Rep");
                arSalesRepID = new ArrayList<String>();
                arSalesRepID.add("0");

                arDeliveryPersonName = new ArrayList<String>();
                arDeliveryPersonName.add("Choose Delivery Rep");
                arDeliveryPersonID = new ArrayList<String>();
                arDeliveryPersonID.add("0");
                //salesRep
                Cursor sales_c = sqLiteController.readTableSalesRep();
                if (sales_c != null && sales_c.moveToFirst()) {
                    do {
                        arSalesRepName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arSalesRepID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));

                        arDeliveryPersonName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arDeliveryPersonID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));

                    } while (sales_c.moveToNext());
                }

            }
        } finally {
            sqLiteController.close();
        }

        arOrdersFillName = new ArrayList<String>();
        arOrdersFillName.add("Choose Order Status");
        arOrdersFillName.add("New Order");
        arOrdersFillName.add("Confirmed");
        arOrdersFillName.add("Cancelled");
        arOrdersFillName.add("Fulfilled");


     /*   Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
        sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate2 = sd2.format(date2);
        String OrderDate ="submitstartdate="+currentDate2+"&submitenddate="+currentDate2+"&";*/
        String URL =Const.hostName+"order/reports/salesorderhistory?"+"&&"+"PageSize=500&PageNumber=1&OrderBy=orderdate";
        System.out.println("URL---"+URL);
        GetSalesList(URL);
    }

    public void FilterAlert() {
        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_SalesReportsDetails.this);
                LayoutInflater inflater = Activity_SalesReportsDetails.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_salesfilterlalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_clear = v.findViewById(R.id.fl_btn_clear);
                AppCompatButton btn_submit = v.findViewById(R.id.fl_btn_search);
                Spinner spDCustomer = v.findViewById(R.id.spDCustomer);
                Spinner spFulfillmentType = v.findViewById(R.id.spDFulfillmentType);
                Spinner orders_sp = v.findViewById(R.id.spDOrderStatus);
                Spinner sp_salesRep = v.findViewById(R.id.sp_DsalesRep);
                Spinner sp_DeliveryRep = v.findViewById(R.id.sp_DeliveryRep);

                etFromDate = v.findViewById(R.id.et_from_date);
                etToDate = v.findViewById(R.id.et_to_date);

                etDFromDate = v.findViewById(R.id.et_Dfrom_date);
                etDToDate = v.findViewById(R.id.et_Dto_date);
               /* Date date2 = new Date();
                SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
                sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDate2 = sd2.format(date2);*/
               // tv_rd_orderDate.setText(currentDate2);
               // tv_rd_deliveryDate.setText(currentDate2);

                Date date = new Date();
                SimpleDateFormat df  = new SimpleDateFormat("MM/dd/yyyy");
                Calendar c1 = Calendar.getInstance();
                String currentDate = df.format(date);// get current date here

                // now add 30 day in Calendar instance
                c1.add(Calendar.DAY_OF_YEAR, -30);
                df = new SimpleDateFormat("MM/dd/yyyy");
                Date resultDate = c1.getTime();
                String  dueDate = df.format(resultDate);


                //etFromDate.setText(dueDate);
                //etToDate.setText(currentDate);

                etFromDate.setText("MM/DD/YYYY");
                etToDate.setText("MM/DD/YYYY");

                etDFromDate.setText("MM/DD/YYYY");
                etDToDate.setText("MM/DD/YYYY");

                v.findViewById(R.id.et_from_date).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance().hideKeyboard(Activity_SalesReportsDetails.this);
                        showDialog(DATE_PICKER1_ID);
                    }
                });
                v.findViewById(R.id.et_to_date).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance().hideKeyboard(Activity_SalesReportsDetails.this);
                        showDialog(DATE_PICKER2_ID);
                    }
                });

                v.findViewById(R.id.et_Dfrom_date).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance().hideKeyboard(Activity_SalesReportsDetails.this);
                        showDialog(DATE_PICKER3_ID);
                    }
                });
                v.findViewById(R.id.et_Dto_date).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.getInstance().hideKeyboard(Activity_SalesReportsDetails.this);
                        showDialog(DATE_PICKER4_ID);
                    }
                });

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utils.getInstance().hideKeyboard(Activity_SalesReportsDetails.this);

                        String OrderDate="";
                        String DeliveryDate="";
                        String CusID="";
                        String Fulfillment="";
                        String Status="";
                        String sales="";
                        String delivery="";

                        if(!etFromDate.getText().toString().equals("MM/DD/YYYY")){

                            OrderDate ="submitstartdate="+etFromDate.getText().toString()+"&submitenddate="+etToDate.getText().toString()+"&";

                        }else if(!etDFromDate.getText().toString().equals("MM/DD/YYYY")){

                            DeliveryDate ="datefulfilledstartDate="+etDFromDate.getText().toString()+"&datefulfilledendDate="+etDToDate.getText().toString()+"&";

                        }else if(!str_customerID.equals("0")){

                            CusID ="customerid="+str_customerID+"&";

                        }else if(!str_fulfilmentID.equals("0")){

                            Fulfillment ="fulfillmentypeid="+str_fulfilmentID+"&";

                        }else if(!str_ordersStatus.equals("Choose Order Status")){

                            Status ="submitstatus="+str_ordersStatus+"&";

                        }else if(!str_salesRepID.equals("0")){

                            sales ="salesrepid="+str_salesRepID+"&";

                        }else if(!str_DeliveryRepID.equals("0")){

                            delivery ="deliveryagentid="+str_DeliveryRepID+"&";

                        }

                        String URL =Const.hostName+"order/reports/salesorderhistory?"+OrderDate+DeliveryDate+CusID+Fulfillment+Status+sales+delivery+"PageSize=500&PageNumber=1&OrderBy=orderdate";
                        System.out.println("URL---"+URL);

                        GetSalesList(URL);
                        dialog.dismiss();

                    }
                });

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arCustomerName);
                arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spDCustomer.setAdapter(arrayAdapter);
                spDCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_customer = arCustomerName.get(position);
                        str_customerID = arCustomerID.get(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });

                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arFulfilmentName);
                arrayAdapter3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spFulfillmentType.setAdapter(arrayAdapter3);
                spFulfillmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_fulfilment = arFulfilmentName.get(position);
                        str_fulfilmentID= arFulfillID.get(position);
                        System.out.println("str_fulfilmentID---"+str_fulfilmentID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });


                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arOrdersFillName);
                arrayAdapter1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                orders_sp.setAdapter(arrayAdapter1);
                orders_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_ordersStatus = arOrdersFillName.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });


                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arSalesRepName);
                arrayAdapter2.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                sp_salesRep.setAdapter(arrayAdapter2);
                sp_salesRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_salesRep = arSalesRepName.get(position);
                        str_salesRepID = arSalesRepID.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });

                ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arDeliveryPersonName);
                arrayAdapter4.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                sp_DeliveryRep.setAdapter(arrayAdapter4);
                sp_DeliveryRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_DeliveryRep = arDeliveryPersonName.get(position);
                        str_DeliveryRepID = arDeliveryPersonID.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });


            }
        });

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
            case DATE_PICKER2_ID:
                datePickerDialog = new DatePickerDialog(this, pickerListener2, mYear, mMonth,mDay);
                //datePickerDialog.getDatePicker().setMinDate (System.currentTimeMillis() - 1000);
                return datePickerDialog;

            case DATE_PICKER3_ID:
                Calendar d2 = Calendar.getInstance();
                d2.add(d2.MONTH,2);
                datePickerDialog = new DatePickerDialog(this, pickerListener3, mYear, mMonth,mDay);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                // datePickerDialog.getDatePicker().setMaxDate(d.getTimeInMillis() - 1000);
                return datePickerDialog;
            case DATE_PICKER4_ID:
                datePickerDialog = new DatePickerDialog(this, pickerListener4, mYear, mMonth,mDay);
                //datePickerDialog.getDatePicker().setMinDate (System.currentTimeMillis() - 1000);
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            etFromDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());

        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            etToDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());
        }
    };


    private DatePickerDialog.OnDateSetListener pickerListener3 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            etDFromDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());

        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener4 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            etDToDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());
        }
    };

    private void GetSalesList(String API_url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_SalesReportsDetails.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_SalesReportsDetails.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        AlertDialog Progress_dialog = builder.create();
        Progress_dialog.show();

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_noProducts.setVisibility(View.VISIBLE);
                      /*  if (Progress_dialog != null) {
                            if (Progress_dialog.isShowing()) {
                                Progress_dialog.dismiss();
                            }
                        }*/
                    }
                });
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    OkHttpClient client1 = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30,TimeUnit.SECONDS)
                            .readTimeout(30,TimeUnit.SECONDS)
                            .build();

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    //RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(API_url)
                            // .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println("jsonObject---"+jsonObject);
                            JSONArray data = jsonObject.getJSONArray("data");
                            listDetails.clear();
                            if(data.length()>0){
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsC = data.getJSONObject(i);
                                    String ordernumber = jsC.getString("ordernumber");
                                    String orderdate = jsC.getString("orderdate");
                                    String customername = jsC.getString("customername");
                                    String status = jsC.getString("status");
                                    String amount = jsC.getString("amount");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listDetails.add(new Model_ReportsDetails("SO "+ordernumber,orderdate,customername,amount,status));
                                            adapter.notifyDataSetChanged();
                                            img_noProducts.setVisibility(View.GONE);
                                            if (Progress_dialog != null) {
                                                if (Progress_dialog.isShowing()) {
                                                    Progress_dialog.dismiss();
                                                }
                                            }
                                        }
                                    });



                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Activity_SalesReportsDetails.this, "No records found!", Toast.LENGTH_SHORT).show();
                                        listDetails.clear();
                                        adapter.notifyDataSetChanged();
                                        img_noProducts.setVisibility(View.VISIBLE);
                                        if (Progress_dialog != null) {
                                            if (Progress_dialog.isShowing()) {
                                                Progress_dialog.dismiss();
                                            }
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            }

        };
        task.execute((Void[])null);
    }


    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }

}
