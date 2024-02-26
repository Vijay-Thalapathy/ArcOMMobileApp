package com.example.arcomdriver.reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ItemReportDetails;
import com.example.arcomdriver.adapter.Adapter_RptItems;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_ItemReportsDetails;
import com.example.arcomdriver.model.Model_RptListItem;
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
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class Activity_DeliveryItemReportsDetails extends Activity_Menu {
    private CoordinatorLayout cl;

    Adapter_ItemReportDetails adapter;
    ArrayList<Model_ItemReportsDetails> listDetails = new ArrayList<>();
    private RecyclerView mRecyclerView;


    LottieAnimationView img_noProducts;

    RelativeLayout filter_rll;

    AppCompatImageView filter_image;

    String str_DeliveryRep,str_DeliveryRepID="0", str_salesRep,str_salesRepID="0",str_customer,str_customerID="0",str_OrderId="0",str_OrderNum,user_id,token;
    private ArrayList<String> arCustomerID,arCustomerName;
    private ArrayList<String> arProductID,arProductName,arOrderNum,arOrderID,arSalesRepName,arSalesRepID,arDeliveryPersonName,arDeliveryPersonID;
    String settingsvalue ="0",str_productsName,str_productsId="0";

    String ReportFlag;

    RecyclerView Rc_report_itemsList;

    Adapter_RptItems adapterList;
    ArrayList<Model_RptListItem> ListV = new ArrayList<>();

    JSONObject jsCAry ;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_productlist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_itemsreportdetails, null, false);
        parentView.addView(contentView,0);

        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ReportFlag = extras.getString("ReportFlag");

            if(ReportFlag.equals("3")){
                txt_page.setText("Today's Delivery - Order View");
            }else {
                txt_page.setText("Today's Delivery - Item View");
            }


            System.out.println("ReportFlag---"+ReportFlag);
        }

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


        cl = findViewById(R.id.cl);
        filter_rll = findViewById(R.id.filter_rll);
        filter_image = findViewById(R.id.filter_image);
        mRecyclerView = findViewById(R.id.rc_itemsDetailsList);
        img_noProducts = findViewById(R.id.img_noProducts);

        adapter = new Adapter_ItemReportDetails(listDetails);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(ReportFlag.equals("3")){
                    ItemsListAlert(listDetails.get(position).getRd_ordernumber());

                }


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


                arOrderNum = new ArrayList<String>();
                arOrderNum.add("Choose Order Number");
                arOrderID = new ArrayList<String>();
                arOrderID.add("0");
                //Cursor order_c = sqLiteController.readJointTableOrder();
                Cursor order_c = sqLiteController.readJointTableOrderShipment();
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                        @SuppressLint("Range") String id = order_c.getString(order_c.getColumnIndex("order_id"));
                        @SuppressLint("Range") String fulfillmentstatus = order_c.getString(order_c.getColumnIndex("fulfillmentstatus"));

                        System.out.println("ordernumber---"+ordernumber);
                        if(!ordernumber.startsWith("DRFOM")){
                            arOrderNum.add(ordernumber);
                            arOrderID.add(id);
                        }

                    } while (order_c.moveToNext());

                }


                arSalesRepName = new ArrayList<String>();
                arSalesRepName.add("Choose Sales Rep");
                arSalesRepID = new ArrayList<String>();
                arSalesRepID.add("0");
                //salesRep
                Cursor sales_c = sqLiteController.readTableSalesRep();
                if (sales_c != null && sales_c.moveToFirst()) {
                    do {
                        arSalesRepName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arSalesRepID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));
                    } while (sales_c.moveToNext());
                }

                arDeliveryPersonName = new ArrayList<String>();
                arDeliveryPersonName.add("Choose Delivery Rep");
                arDeliveryPersonID = new ArrayList<String>();
                arDeliveryPersonID.add("0");

               /* Cursor delivery_c = sqLiteController.readTableDeliveryRep();
                if (delivery_c != null && delivery_c.moveToFirst()) {
                    do {
                        arDeliveryPersonName.add(delivery_c.getString(delivery_c.getColumnIndex("delivery_rep_name")));
                        arDeliveryPersonID.add(delivery_c.getString(delivery_c.getColumnIndex("delivery_rep_id")));

                    } while (delivery_c.moveToNext());
                }*/

                //salesRep
                Cursor sales_c1 = sqLiteController.readTableSalesRep();
                if (sales_c1 != null && sales_c1.moveToFirst()) {
                    do {
                        arDeliveryPersonName.add(sales_c1.getString(sales_c1.getColumnIndex("salesrep_name")));
                        arDeliveryPersonID.add(sales_c1.getString(sales_c1.getColumnIndex("salesrep_id")));
                    } while (sales_c1.moveToNext());
                }


                arProductName = new ArrayList<String>();
                arProductName.add("Choose Products Name");
                arProductID = new ArrayList<String>();
                arProductID.add("0");
                Cursor product_c = sqLiteController.readAllTableProduct();

                if (product_c != null && product_c.moveToFirst()) {
                    String productName ="";
                    String productId ="";
                    String istaxable ="";
                    String Upsc_code ="";
                    String all_product_status ="";
                    String all_description ="";
                    String productImage ="null";
                    String productPrice ="0";
                    do {

                        productName =product_c.getString(product_c.getColumnIndex("all_product_name"));
                        productId =product_c.getString(product_c.getColumnIndex("all_product_id"));
                        productImage =product_c.getString(product_c.getColumnIndex("all_product_img"));
                        productPrice =product_c.getString(product_c.getColumnIndex("all_product_price"));
                        istaxable =product_c.getString(product_c.getColumnIndex("all_istax"));
                        Upsc_code =product_c.getString(product_c.getColumnIndex("all_upsc"));
                        all_description =product_c.getString(product_c.getColumnIndex("all_description"));
                        all_product_status =product_c.getString(product_c.getColumnIndex("all_product_status"));

                        arProductName.add(productName);
                        arProductID.add(productId);

                    } while (product_c.moveToNext());
                }

            }
        } finally {
            sqLiteController.close();
        }

        if(ReportFlag.equals("2")){
            Date date2 = new Date();
            SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
            //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDate2 = sd2.format(date2);
            String URL =Const.hostName+"order/reports/todaydelivery?"+"&&"+"PageSize=500&PageNumber=1&tdrtype=1&deliverydate="+currentDate2;
            System.out.println("URL---"+URL);
            GetSalesList(URL);
        }else {
            Date date2 = new Date();
            SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
            //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDate2 = sd2.format(date2);
            String URL =Const.hostName+"order/reports/todaydelivery?"+"&&"+"PageSize=500&PageNumber=1&tdrtype=2&deliverydate="+currentDate2;
            System.out.println("URL---"+URL);
            GetSalesList(URL);
        }


    }

    public void FilterAlert() {
        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DeliveryItemReportsDetails.this);
                LayoutInflater inflater = Activity_DeliveryItemReportsDetails.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_deliveryitemfilterlalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_clear = v.findViewById(R.id.fl_btn_clear);
                AppCompatButton btn_submit = v.findViewById(R.id.fl_btn_search);
                Spinner spDCustomer = v.findViewById(R.id.spDCustomer);
                Spinner spRProducts = v.findViewById(R.id.spRProducts);
                Spinner spOrderNum = v.findViewById(R.id.spOrderNum);
                Spinner orders_sp = v.findViewById(R.id.spDOrderStatus);
                Spinner sp_salesRep = v.findViewById(R.id.sp_DsalesRep);
                Spinner sp_DeliveryRep = v.findViewById(R.id.sp_DeliveryRep);

                Date date = new Date();
                SimpleDateFormat df  = new SimpleDateFormat("MM/dd/yyyy");
                Calendar c1 = Calendar.getInstance();
                String currentDate = df.format(date);// get current date here


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
                        Utils.getInstance().hideKeyboard(Activity_DeliveryItemReportsDetails.this);

                        String OrderID="";
                        String ProID="";
                        String CusID="";
                        String sales="";
                        String delivery="";

                        if(!str_OrderId.equals("0")){

                            OrderID ="orderid="+str_OrderId+"&";

                        }else if(!str_productsId.equals("0")){

                            ProID ="productid="+str_productsId+"&";

                        }else if(!str_customerID.equals("0")){

                            CusID ="customerid="+str_customerID+"&";

                        }else if(!str_salesRepID.equals("0")){

                            sales ="salesrepid="+str_salesRepID+"&";

                        }else if(!str_DeliveryRepID.equals("0")){

                            delivery ="deliveryagentid="+str_DeliveryRepID+"&";

                        }

                        if(ReportFlag.equals("2")){
                            String URL =Const.hostName+"order/reports/todaydelivery?"+OrderID+CusID+ProID+sales+delivery+"PageSize=500&PageNumber=1&tdrtype=1&deliverydate="+currentDate;
                            System.out.println("URL---"+URL);
                            GetSalesList(URL);
                        }else {
                            String URL =Const.hostName+"order/reports/todaydelivery?"+OrderID+CusID+ProID+sales+delivery+"PageSize=500&PageNumber=1&tdrtype=2&deliverydate="+currentDate;
                            System.out.println("URL---"+URL);
                            GetSalesList(URL);
                        }



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

                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arOrderNum);
                arrayAdapter3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spOrderNum.setAdapter(arrayAdapter3);
                spOrderNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_OrderNum = arOrderNum.get(position);
                        str_OrderId = arOrderID.get(position);
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


                ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arProductName);
                arrayAdapter5.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spRProducts.setAdapter(arrayAdapter5);
                spRProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_productsName = arProductName.get(position);
                        str_productsId = arProductID.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });


            }
        });

    }

    private void GetSalesList(String API_url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DeliveryItemReportsDetails.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_DeliveryItemReportsDetails.this.getLayoutInflater();
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
                            jsCAry = new JSONObject(res);
                            System.out.println("jsonObject---"+jsCAry);
                            JSONArray data = jsCAry.getJSONArray("data");
                            listDetails.clear();
                            if(data.length()>0){
                                boolean isExsits =false;
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsC = data.getJSONObject(i);

                                    String ordernumber = jsC.getString("ordernumber");
                                    String productname = jsC.getString("productname");
                                    String quantity = jsC.getString("quantity");
                                    String ordergroup = jsC.getString("ordergroup");

                                    if(ReportFlag.equals("2")){
                                        listDetails.add(new Model_ItemReportsDetails(ordernumber,productname,quantity));
                                    }else {
                                        if(listDetails.size()>0){

                                            for (int k = 0; k < listDetails.size(); k++) {

                                                if(listDetails.get(k).getRd_ordernumber().equals(ordernumber)){

                                                    int getProduct = Integer.parseInt(listDetails.get(k).getRd_ProductQty());

                                                    getProduct += Integer.parseInt(quantity);

                                                    listDetails.get(k).setRd_ProductQty(String.valueOf(getProduct));

                                                    k=listDetails.size();
                                                    isExsits= false;

                                                }else {
                                                    isExsits =true;
                                                }
                                            }
                                        }else {
                                            isExsits =true;
                                        }
                                        if(isExsits){
                                            listDetails.add(new Model_ItemReportsDetails(ordernumber,ordergroup,quantity));

                                        }


                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

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
                                        Toast.makeText(Activity_DeliveryItemReportsDetails.this, "No records found!", Toast.LENGTH_SHORT).show();
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

    public void ItemsListAlert(String rdOrderID) {
        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {

                AlertDialog Progress_dialog_;

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DeliveryItemReportsDetails.this);
                LayoutInflater inflater = Activity_DeliveryItemReportsDetails.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_reportsalert_itemlist,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                AlertDialog.Builder builder_ = new AlertDialog.Builder(Activity_DeliveryItemReportsDetails.this, R.style.LoadingStyle);
                builder_.setCancelable(false);
                LayoutInflater layoutInflater1 = Activity_DeliveryItemReportsDetails.this.getLayoutInflater();
                View v1 = layoutInflater1.inflate(R.layout.layout_avi, null);
                builder_.setView(v1);
                Progress_dialog_ = builder_.create();
                Progress_dialog_.show();

               // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                AppCompatImageView close_img = v.findViewById(R.id.close_img);

                Rc_report_itemsList = v.findViewById(R.id.Rc_report_itemsList);

                adapterList = new Adapter_RptItems(Activity_DeliveryItemReportsDetails.this,ListV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_DeliveryItemReportsDetails.this);
                Rc_report_itemsList.setLayoutManager(layoutManager);
                Rc_report_itemsList.setItemAnimator(new DefaultItemAnimator());
                Rc_report_itemsList.addItemDecoration(new ItemDividerDecoration(Activity_DeliveryItemReportsDetails.this, LinearLayoutManager.VERTICAL));
                Rc_report_itemsList.setAdapter(adapterList);

                Rc_report_itemsList.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_DeliveryItemReportsDetails.this, Rc_report_itemsList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));


                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("jsonObjectSendData---"+ jsCAry);
                            JSONArray data =  jsCAry.getJSONArray("data");
                            ListV.clear();
                            if(data.length()>0){
                                boolean isExsits =false;
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsC = data.getJSONObject(i);

                                    String ordernumber = jsC.getString("ordernumber");
                                    String productname = jsC.getString("productname");
                                    String quantity = jsC.getString("quantity");
                                    String ordergroup = jsC.getString("ordergroup");

                                    if(ordernumber.equals(rdOrderID)){
                                        ListV.add(new Model_RptListItem(productname,quantity));
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            adapterList.notifyDataSetChanged();
                                            if (Progress_dialog_ != null) {
                                                if (Progress_dialog_.isShowing()) {
                                                    Progress_dialog_.dismiss();
                                                }
                                            }
                                        }
                                    });



                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListV.clear();
                                        adapterList.notifyDataSetChanged();
                                        if (Progress_dialog_ != null) {
                                            if (Progress_dialog_.isShowing()) {
                                                Progress_dialog_.dismiss();
                                            }
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }



    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }

}
