package com.example.arcomdriver.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PDFPrint;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.driver.ActivitySignIn;
import com.example.arcomdriver.driver.Activity_Splash;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_OrderList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_InvoiceProductItems;
import com.example.arcomdriver.model.Model_OrderList;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_OrdersHistory extends Activity_Menu {
    private CoordinatorLayout cl;
    private RecyclerView mRecyclerView;
    AppCompatTextView Username_tv,OrderTotal_tv,OrderFulfilled_tv,OrderYettoful_tv,OrderToday_tv,deliverytoday_tv;

    Adapter_OrderList adapter;
    ArrayList<Model_OrderList> listOrder = new ArrayList<>();
    ArrayList<Model_OrderList> filteredModelList = new ArrayList<>();
    private SQLiteController sqLiteController;

    int TodayDelivered =0;
    int PackTotal =0;
    int PickTotal =0;
    int ShipTotal =0;
    int DeliveryTotal =0;
    int ConfirmedTotal =0;
    private SearchView searchBar;
    private String currentDateTime,cus_str,str_reason,cancel_str,currentDate,str_ordersfill="All",user_id,currentDate1;
    private AppCompatSpinner cancelReason_sp;
    private ArrayList<String> arReasonID;
    private ArrayList<String> arReasonName;

    private ArrayList<String> arOrdersFillID;
    private ArrayList<String> arOrdersFillName;
    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    private AppCompatSpinner orders_sp;
    private SwipeRefreshLayout srl;


    ArrayList<Model_InvoiceProductItems> listInvoice = new ArrayList<>();


    public static String APIOrderHTML ="null";

    String RowOrderHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";


    public AlertDialog Progress_dialog;


    int PERMISSION_CODE = 111;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE};
    private SQLiteController sqLiteControllerHome;

    public static String currency_symbol="$";

    public static String WarehouseFlag="false";
    public static String RouteFlag="false";

    public static String OrderFlag="true";

    public static String OMEnableFlag="true";
    public static String PricingSetup="1",tax="1";
    public static String token,TotalInvoice_s="0",PaidInvoice_s="0",UnPaidInvoice_s="0";

    LottieAnimationView img_noInvoice;


    public static  SharedPreferences cmSharedPreferences;
    public static final String ohPref = "OhPreferences";
    public static final String CmpLogo= "CmpLogo";

    String Companylogo;

    private SharedPreferences InSharedPreferences;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_viewlist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_viewlist, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Presale Order");
        createOrder_img.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OrdersHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_OrdersHistory.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        sqLiteControllerHome = new SQLiteController(this);
        sqLiteControllerHome.open();
        try {
            long count = sqLiteControllerHome.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteControllerHome.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    @SuppressLint("Range") String user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                    GetCompanylogo();
                }

                Cursor C_Address = sqLiteControllerHome.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("PricingSetup")){
                            PricingSetup  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));
                        }
                        if(invoice_name.equals("tax")){
                            tax  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));

                            System.out.println("tax-----"+tax);

                        }

                        System.out.println("invoice_name--"+invoice_name);

                        if(invoice_name.equals("AddinList")){
                            String settingsvalue  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));
                            System.out.println("settingsvalue--"+settingsvalue);
                            if (settingsvalue != null && !settingsvalue.equals("")) {
                                if (settingsvalue.contains("Warehouse")) {
                                    WarehouseFlag ="true";

                                } else {
                                    WarehouseFlag ="false";

                                }

                                if (settingsvalue.contains("Route")) {
                                    RouteFlag ="true";
                                }else {
                                    RouteFlag ="false";
                                }

                                if (settingsvalue.contains("Order")) {
                                    OrderFlag ="true";
                                }else {
                                    OrderFlag ="false";
                                }

                            }else {
                                WarehouseFlag ="false";
                                RouteFlag ="false";
                            }

                        }

                        if(invoice_name.equals("isuomenabled")){
                            String settingsvalue  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));
                            if (settingsvalue != null && !settingsvalue.equals("")) {
                                if (settingsvalue.contains("1")) {
                                    OMEnableFlag ="true";
                                } else {
                                    OMEnableFlag ="false";
                                }
                            }else {
                                OMEnableFlag ="true";
                            }

                        }
                    } while (C_Address.moveToNext());
                }

                Cursor userCurrency = sqLiteControllerHome.readTableCurrency();
                if (userCurrency != null && userCurrency.moveToFirst()) {
                    currency_symbol = userCurrency.getString(userCurrency.getColumnIndex("currency_symbol"));
                }

            }
        } finally {
            sqLiteControllerHome.close();
        }

        Username_tv = (AppCompatTextView) findViewById(R.id.Username_tv);

        SQLiteController sqLiteController_u = new SQLiteController(this);
        sqLiteController_u.open();
        try {
            long count = sqLiteController_u.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController_u.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                     user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                    Username_tv.setText("Hello, "+UserName+"!");

                }

            }
        } finally {
            sqLiteController_u.close();
        }

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        currentDateTime = sdf.format(date1);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Date date33 = new Date();
        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(date33);

        Date dateTime = new Date();
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        currentDate1 = sdfTime.format(dateTime);

        Calendar calE = Calendar.getInstance();
        calE.set(Calendar.DAY_OF_MONTH, calE.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date resultDate = calE.getTime();
        String  monthEnd = sdfTime.format(resultDate);


        Calendar calS = Calendar.getInstance();
        calS.set(Calendar.DAY_OF_MONTH, 1);
        Date resultDate5 = calS.getTime();
        String  monthStart= sdfTime.format(resultDate5);


        Calendar calWS = Calendar.getInstance();
        calWS.set(Calendar.DAY_OF_WEEK, calWS.getFirstDayOfWeek());
        Date resultDate2 = calWS.getTime();
        String  WeekStart = sdfTime.format(resultDate2);

        Calendar calWE = Calendar.getInstance();
        calWE.set(Calendar.DAY_OF_WEEK, -7);
        Date resultDate3 = calWE.getTime();
        String  WeekEnd = sdfTime.format(resultDate3);

        Calendar calPR= Calendar.getInstance();
        calPR.add(Calendar.DAY_OF_MONTH, -1);
        Date resultDatR = calPR.getTime();
        String  PreviousDate = sdfTime.format(resultDatR);

        Calendar calMI= Calendar.getInstance();
        calMI.add(Calendar.HOUR, -1);
        Date resultMIN = calMI.getTime();
        String  MiOne = sdfTime.format(resultMIN);

        System.out.println("MiOne---"+MiOne);

        System.out.println("currentDate1---"+currentDate1);
        System.out.println("monthStart---"+monthStart);
        System.out.println("monthEnd---"+monthEnd);
        System.out.println("WeekStart---"+WeekStart);
        System.out.println("WeekEnd---"+WeekEnd);
        System.out.println("PreviousDate---"+PreviousDate);

        try {
            JSONObject json = new JSONObject();
            json.put("screenid","073664b1-cd21-46a5-8514-6f4bbfbcfef1");
            json.put("ownerid","d4ab35c2-4dce-454f-8bf3-77e42dc6c95d");
            json.put("todayDate",currentDate1);
            json.put("thisWeekStartDate",WeekStart);
            json.put("thisWeekEndDate",WeekEnd);
            json.put("thisMonthStartDate",monthStart);
            json.put("thisMonthEndDate",monthEnd);
            System.out.println("res---"+json.toString());
            postKPI(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject();
            json.put("screenid","e5328976-ebcc-4a7f-ab95-c0e4bc6f787f");
            json.put("ownerid","d4ab35c2-4dce-454f-8bf3-77e42dc6c95d");
            json.put("todayDate",currentDate1);
            json.put("thisWeekStartDate",WeekStart);
            json.put("thisWeekEndDate",WeekEnd);
            json.put("thisMonthStartDate",monthStart);
            json.put("thisMonthEndDate",monthEnd);
            System.out.println("res---"+json.toString());
            postInvoiceKPI(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        cl = findViewById(R.id.cl);
        srl = findViewById(R.id.swipe);
        img_noInvoice = findViewById(R.id.img_noInvoice);
        searchBar = (SearchView) findViewById(R.id.search_view);
        mRecyclerView = findViewById(R.id.rc_presaleList);
        OrderTotal_tv = findViewById(R.id.OrderTotal_tv);
        OrderFulfilled_tv = findViewById(R.id.OrderFulfilled_tv);
        OrderYettoful_tv = findViewById(R.id.OrderYettoful_tv);
        OrderToday_tv = findViewById(R.id.OrderToday_tv);
        deliverytoday_tv = findViewById(R.id.deliverytoday_tv);
        orders_sp = findViewById(R.id.orders_sp);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                Utils.getInstance().GetMasterInsert(Activity_OrdersHistory.this,"InvoiceRefresh",Progress_dialog,PreviousDate);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (srl.isRefreshing()) srl.setRefreshing(false);
                        FillPresaleList(str_ordersfill);
                    }
                }, 6000);


            }
        });

        createOrder_img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","0");
                Intent in = new Intent(Activity_OrdersHistory.this, Activity_CreateOrder.class);
                in.putExtras(bundle);
                startActivity(in);
                overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_OrdersHistory.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_OrdersHistory.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_OrdersHistory.this, Activity_InvoiceHistory.class));
            }
        });

        adapter = new Adapter_OrderList(listOrder);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                APIOrderHTML="null";

                if(filteredModelList.size()>0){

                    view.findViewById(R.id.viewImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_OrdersHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                            Menu popMenu = popupMenu.getMenu();

                            if(filteredModelList.get(position).getOrder_number().startsWith("DRFOM")){
                                popMenu.findItem(R.id.generate_orders).setVisible(false);
                                popMenu.findItem(R.id.cancel_order).setVisible(false);
                                popMenu.findItem(R.id.confirm_orders).setVisible(false);
                            }else {
                                if (filteredModelList.get(position).getOrder_status().equals("New Order")||filteredModelList.get(position).getOrder_status().equals("Cancelled")){
                                    popMenu.findItem(R.id.confirm_orders).setVisible(true);
                                }else {
                                    popMenu.findItem(R.id.confirm_orders).setVisible(false);
                                }
                                if (filteredModelList.get(position).getOrder_status().equals("Cancelled")||filteredModelList.get(position).getOrder_status().equals("Fulfilled")){
                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                    popMenu.findItem(R.id.cancel_order).setVisible(false);
                                }
                                SQLiteController sqLiteController2 = new SQLiteController(Activity_OrdersHistory.this);
                                sqLiteController2.open();
                                try {
                                    long count = sqLiteController2.fetchOrderCount();
                                    if (count > 0) {
                                        //Cursor order_c = sqLiteController2.readShipTableItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ORDER_ID,filteredModelList.get(position).getOrder_id());
                                        Cursor order_c = sqLiteController2.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_ORDER_ID,filteredModelList.get(position).getOrder_id());
                                        if (order_c != null && order_c.moveToFirst()) {
                                            do {
                                                String status = order_c.getString(order_c.getColumnIndex("Insubmitstatus"));
                                                if(status.equals("Payment pending")){
                                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                }else if(status.equals("Paid")){
                                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                }else {
                                                    if (filteredModelList.get(position).getOrder_status().equals("Cancelled")){
                                                        popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                    }else {
                                                        popMenu.findItem(R.id.generate_orders).setVisible(true);
                                                    }
                                                }

                                            } while (order_c.moveToNext());
                                        }

                                        if(order_c.getCount() == 0){
                                            if (filteredModelList.get(position).getOrder_status().equals("Cancelled")){
                                                popMenu.findItem(R.id.generate_orders).setVisible(false);
                                            }else {
                                                popMenu.findItem(R.id.generate_orders).setVisible(true);
                                            }

                                        }

                                        if(order_c.getCount() == 0){
                                            popMenu.findItem(R.id.generate_orders).setVisible(true);
                                        }
                                    }

                                } finally {
                                    sqLiteController2.close();
                                }
                            }

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.confirm_orders:
                                            ConfirmAlert(
                                                    filteredModelList.get(position).getOrder_number(),
                                                    filteredModelList.get(position).getOrder_name(),
                                                    filteredModelList.get(position).getOrder_id()
                                            );
                                            return true;

                                        case R.id.cancel_order:
                                            CancelAlert(
                                                    filteredModelList.get(position).getOrder_number(),
                                                    filteredModelList.get(position).getOrder_name(),
                                                    filteredModelList.get(position).getOrder_id()
                                            );
                                            return true;

                                        case R.id.Download_invoice:
                                            RepaceString(filteredModelList.get(position).getOrder_id(),filteredModelList.get(position).getOrder_status());
                                            return true;

                                        case R.id.generate_orders:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("In_Flag","1");
                                            bundle.putString("In_OrderID",filteredModelList.get(position).getOrder_id());
                                            Intent in = new Intent(Activity_OrdersHistory.this, Activity_CreateInvoice.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
                                            return true;

                                        default:
                                            return false;
                                    }
                                }
                            });
                            // Showing the popup menu
                            popupMenu.show();

                        }
                    });
                    view.findViewById(R.id.orderNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("OrderId",filteredModelList.get(position).getOrder_id());
                            bundle.putString("CustomerName",filteredModelList.get(position).getOrder_name());
                            Intent in = new Intent(Activity_OrdersHistory.this, ActivityOrdersDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });

                }else {

                    view.findViewById(R.id.viewImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_OrdersHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                            Menu popMenu = popupMenu.getMenu();

                            if(listOrder.get(position).getOrder_number().startsWith("DRFOM")){
                                popMenu.findItem(R.id.generate_orders).setVisible(false);
                                popMenu.findItem(R.id.cancel_order).setVisible(false);
                                popMenu.findItem(R.id.confirm_orders).setVisible(false);
                            }else {
                                if (listOrder.get(position).getOrder_status().equals("New Order")||listOrder.get(position).getOrder_status().equals("Cancelled")){
                                    popMenu.findItem(R.id.confirm_orders).setVisible(true);
                                }else {
                                    popMenu.findItem(R.id.confirm_orders).setVisible(false);
                                }
                                if (listOrder.get(position).getOrder_status().equals("Cancelled")||listOrder.get(position).getOrder_status().equals("Fulfilled")){
                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                    popMenu.findItem(R.id.cancel_order).setVisible(false);
                                }
                                SQLiteController sqLiteController2 = new SQLiteController(Activity_OrdersHistory.this);
                                sqLiteController2.open();
                                try {
                                    long count = sqLiteController2.fetchOrderCount();

                                    if (count > 0) {
                                        Cursor order_c = sqLiteController2.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_ORDER_ID,listOrder.get(position).getOrder_id());
                                        //Cursor order_c = sqLiteController2.readShipTableItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ORDER_ID,listOrder.get(position).getOrder_id());
                                        if (order_c != null && order_c.moveToFirst()) {
                                            do {
                                                String status = order_c.getString(order_c.getColumnIndex("Insubmitstatus"));
                                                if(status.equals("Payment pending")){
                                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                }else if(status.equals("Paid")){
                                                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                }else {
                                                    if (listOrder.get(position).getOrder_status().equals("Cancelled")){
                                                        popMenu.findItem(R.id.generate_orders).setVisible(false);
                                                    }else {
                                                        popMenu.findItem(R.id.generate_orders).setVisible(true);
                                                    }
                                                }

                                            } while (order_c.moveToNext());
                                        }

                                        if(order_c.getCount() == 0){
                                            if (listOrder.get(position).getOrder_status().equals("Cancelled")){
                                                popMenu.findItem(R.id.generate_orders).setVisible(false);
                                            }else {
                                                popMenu.findItem(R.id.generate_orders).setVisible(true);
                                            }

                                        }
                                    }

                                } finally {
                                    sqLiteController2.close();
                                }
                            }

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.confirm_orders:
                                            ConfirmAlert(
                                                    listOrder.get(position).getOrder_number(),
                                                    listOrder.get(position).getOrder_name(),
                                                    listOrder.get(position).getOrder_id()
                                            );
                                            return true;

                                        case R.id.cancel_order:
                                            CancelAlert(
                                                    listOrder.get(position).getOrder_number(),
                                                    listOrder.get(position).getOrder_name(),
                                                    listOrder.get(position).getOrder_id()
                                            );
                                            return true;

                                        case R.id.Download_invoice:
                                            RepaceString(listOrder.get(position).getOrder_id(),listOrder.get(position).getOrder_number());
                                            return true;

                                        case R.id.generate_orders:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("In_Flag","1");
                                            bundle.putString("In_OrderID",listOrder.get(position).getOrder_id());
                                            Intent in = new Intent(Activity_OrdersHistory.this, Activity_CreateInvoice.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            // Showing the popup menu
                            popupMenu.show();

                        }
                    });
                    view.findViewById(R.id.orderNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("-----ClickDetails");
                            System.out.println("OrderId-----"+listOrder.get(position).getOrder_id());
                            System.out.println("CustomerName-----"+listOrder.get(position).getOrder_name());
                            Bundle bundle = new Bundle();
                            bundle.putString("OrderId",listOrder.get(position).getOrder_id());
                            bundle.putString("CustomerName",listOrder.get(position).getOrder_name());
                            Intent in = new Intent(Activity_OrdersHistory.this, ActivityOrdersDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });

                }



            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        //CancelledReason
        SQLiteController sqLiteController = new SQLiteController(Activity_OrdersHistory.this);
        sqLiteController.open();
        try {
            long fetchCancelReasonCount = sqLiteController.fetchCancelReasonCount();
            if (fetchCancelReasonCount > 0) {
                arReasonName = new ArrayList<String>();
                arReasonID = new ArrayList<String>();

                Cursor CancelledReason_c = sqLiteController.readTableCancelledReason();
                if (CancelledReason_c.moveToFirst()) {
                    do {
                        arReasonName.add(CancelledReason_c.getString(CancelledReason_c.getColumnIndex("cancelreason_name")));
                        arReasonID.add(CancelledReason_c.getString(CancelledReason_c.getColumnIndex("cancelreason_id")));
                    } while (CancelledReason_c.moveToNext());
                }
            }
        } finally {
            sqLiteController.close();
        }

        arOrdersFillName = new ArrayList<String>();
        arOrdersFillName.add("All");
        arOrdersFillName.add("Cancelled");
        arOrdersFillName.add("Confirmed");
        arOrdersFillName.add("New Orders");
        arOrdersFillName.add("Fulfilled");
        arOrdersFillID = new ArrayList<String>();

        //shipment
        SQLiteController sqLiteController3 = new SQLiteController(Activity_OrdersHistory.this);
        sqLiteController3.open();
        try {

            Cursor shipment_c = sqLiteController3.readTableShipment();
            if (shipment_c != null && shipment_c.moveToFirst()) {
                do {
                    @SuppressLint("Range") String fulfillmentstatus = shipment_c.getString(shipment_c.getColumnIndex("fulfillmentstatus"));
                    @SuppressLint("Range") String deliverydate = shipment_c.getString(shipment_c.getColumnIndex("deliverydate"));

                   /* if(!deliverydate.equals("null")){
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = null;
                        try {
                            date = sdf1.parse(deliverydate);
                            String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                            System.out.println("deliverydate--"+deliverydate);
                            System.out.println("currentDate--"+currentDate);

                            if(date_order.equals(currentDate)){

                                if(fulfillmentstatus.equals("Shipped")){
                                    TodayDelivered +=1;
                                    deliverytoday_tv.setText(String.valueOf(TodayDelivered));
                                }

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }*/

                } while (shipment_c.moveToNext());


            }

           //OrderPacked_tv.setText(String.valueOf(PackTotal));
           //OrderPicked_tv.setText(String.valueOf(PickTotal));
           //OrderShip_tv.setText(String.valueOf(ShipTotal));
           //OrderDelivery_tv.setText(String.valueOf(DeliveryTotal));

        } finally {
            sqLiteController3.close();
        }

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arOrdersFillName);
        arrayAdapter1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        orders_sp.setAdapter(arrayAdapter1);
        orders_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_ordersfill = arOrdersFillName.get(position);

                if(str_ordersfill.equals("All")){
                    FillPresaleList("All");
                }else  if(str_ordersfill.equals("Fulfilled orders")){
                    FillPresaleList("Fulfilled");
                }else  if(str_ordersfill.equals("Yet to fulfill")){
                    FillPresaleList("Fulfilled");
                }else  if(str_ordersfill.equals("New Orders")){
                    FillPresaleList("New Order");
                }else  if(str_ordersfill.equals("Cancelled")){
                    FillPresaleList("Cancelled");
                }else  if(str_ordersfill.equals("Confirmed")){
                    FillPresaleList("Confirmed");
                }else {
                    FillPresaleList(str_ordersfill);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something
                //SEARCH FILTER
               filteredModelList = filter(listOrder, newText.toString());
                adapter.setFilter(filteredModelList);
                if(filteredModelList.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    img_noInvoice.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    img_noInvoice.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                Utils.getInstance().hideKeyboard(Activity_OrdersHistory.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);

    }

    @SuppressLint("Range")
    private void FillPresaleList(String str_ordersfill) {
        int FulfilledTotal =0;
        final int[] TodaysOrder = {0};
        //Orders table
        sqLiteController = new SQLiteController(this);

        sqLiteController.open();
        try {
            long count = sqLiteController.fetchOrderCount();
            if (count > 0) {
                if (listOrder.size() > 0){
                    listOrder.clear();
                }
                //user
                Cursor user_c = sqLiteController.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    token = user_c.getString(user_c.getColumnIndex("token"));
                }

                //Order
                Cursor order_c = sqLiteController.readJointTableOrder();
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                        @SuppressLint("Range") String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                        @SuppressLint("Range") String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                        @SuppressLint("Range") String id = order_c.getString(order_c.getColumnIndex("order_id"));
                        @SuppressLint("Range") String customername = order_c.getString(order_c.getColumnIndex("customername"));
                        @SuppressLint("Range") String SyncStatus = order_c.getString(order_c.getColumnIndex("SyncStatus"));
                        @SuppressLint("Range") String netStatus = order_c.getString(order_c.getColumnIndex("netStatus"));
                        @SuppressLint("Range") String creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));

                        if(str_ordersfill.equals("All")){

                            if (submitstatus.equals("Fulfilled")) {
                                FulfilledTotal += 1;
                                OrderFulfilled_tv.setText(String.valueOf(FulfilledTotal));
                            }

                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    try {
                                        date = sdf.parse(creratedon);
                                        String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                        System.out.println("creratedon--"+date_order);
                                        System.out.println("currentDate--"+currentDate);

                                        if(date_order.equals(currentDate)){
                                            TodaysOrder[0] +=1;
                                            OrderToday_tv.setText(String.valueOf(TodaysOrder[0]));
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });*/


                            listOrder.add(new Model_OrderList(id, customername, ordernumber, submitstatus, submitdate, totalamount,SyncStatus,netStatus));
                        }else if(str_ordersfill.equals(submitstatus)){
                            listOrder.add(new Model_OrderList(id, customername, ordernumber, submitstatus, submitdate, totalamount,SyncStatus,netStatus));
                        }




                    } while (order_c.moveToNext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* if (!Connectivity.isConnected(Activity_OrdersHistory.this) &&
                                    !Connectivity.isConnectedFast(Activity_OrdersHistory.this)) {

                            }*/
                            OrderTotal_tv.setText(String.valueOf(order_c.getCount()));
                            img_noInvoice.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                           // Collections.reverse(list);
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
                        img_noInvoice.setVisibility(View.VISIBLE);
                        if (Progress_dialog != null) {
                            if (Progress_dialog.isShowing()) {
                                Progress_dialog.dismiss();
                            }
                        }

                    }
                });
            }

        } finally {
            sqLiteController.close();
        }

    }

    private ArrayList<Model_OrderList> filter(ArrayList<Model_OrderList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_OrderList> filteredModelList = new ArrayList<>();

        for (Model_OrderList model : models) {

            final String getOrder_name = model.getOrder_name().toLowerCase();
            final String getOrder_status = model.getOrder_status().toLowerCase();
            final String getOrder_number = model.getOrder_number();

            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);

            }else if (getOrder_status.contains(search_txt)) {
                filteredModelList.add(model);

            }else if (getOrder_number.contains(search_txt)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }

    public void ConfirmAlert(String orderNo , String OrderName, String OrderId) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OrdersHistory.this);
                LayoutInflater inflater = Activity_OrdersHistory.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_confirmalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_Confirm =v.findViewById(R.id.btn_Confirm_);

                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (Connectivity.isConnected(Activity_OrdersHistory.this) &&
                                Connectivity.isConnectedFast(Activity_OrdersHistory.this)) {
                            try {
                                JSONObject json = new JSONObject();
                                json.put("id",OrderId);
                                json.put("confirmedby",user_id);
                                json.put("submitstatus","Confirmed");
                                json.put("confirmedDate",currentDate1);
                                PostConfirmStatusUpdate(json.toString(),OrderId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(Activity_OrdersHistory.this, "No Network Connection! Please check internet Connection and try again later!", Toast.LENGTH_SHORT).show();
                            /*sqLiteController = new SQLiteController(Activity_OrdersHistory.this);
                            sqLiteController.open();
                            try {
                                sqLiteController.updatePresaleConfirmOrder(OrderId,"Confirmed");
                            } finally {
                                sqLiteController.close();
                                Intent intent = new Intent(Activity_OrdersHistory.this, Activity_OrdersHistory.class);
                                startActivity(intent);
                                Toast.makeText(Activity_OrdersHistory.this, "Order has Confirmed Successfully!", Toast.LENGTH_SHORT).show();
                            }*/
                        }

                    }
                });


            }
        });
    }
    private void PostConfirmStatusUpdate(String json,String OrderId) {
        Utils.getInstance().loadingDialog(Activity_OrdersHistory.this, "Please wait.");
        try {
            App.getInstance().PostConfirmStatusUpdate(json,token, new Callback() {
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
                                Utils.getInstance().dismissDialog();
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    String messages = jsonObject.getString("messages");
                                    if ( succeeded == true) {
                                        sqLiteController = new SQLiteController(Activity_OrdersHistory.this);
                                        sqLiteController.open();
                                        try {
                                            sqLiteController.updatePresaleConfirmOrder(OrderId,"Confirmed");
                                        } finally {
                                            sqLiteController.close();
                                            Intent intent = new Intent(Activity_OrdersHistory.this, Activity_OrdersHistory.class);
                                            startActivity(intent);
                                            Toast.makeText(Activity_OrdersHistory.this, "Order has Confirmed Successfully!", Toast.LENGTH_SHORT).show();
                                        }
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

    public void CancelAlert(String orderNo , String OrderName, String OrderId) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OrdersHistory.this);
                LayoutInflater inflater = Activity_OrdersHistory.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_cancelalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_submit = v.findViewById(R.id.btn_submit);
                cancelReason_sp = v.findViewById(R.id.cancelReason_sp);
               // AppCompatTextView order_number = v.findViewById(R.id.order_number);
                AppCompatEditText edit_query_des = v.findViewById(R.id.edit_query_des);
              //  order_number.setText(orderNo+" | "+OrderName);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arReasonName);
                arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                cancelReason_sp.setAdapter(arrayAdapter);
                cancelReason_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_reason = arReasonName.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

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
                        Utils.getInstance().hideKeyboard(Activity_OrdersHistory.this);
                        if (str_reason.equals("")) {
                            Utils.getInstance().snackBarMessage(v,"Cancellation Reason!");
                        }else {
                            dialog.dismiss();
                            if (Connectivity.isConnected(Activity_OrdersHistory.this) &&
                                    Connectivity.isConnectedFast(Activity_OrdersHistory.this)) {
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put("id",OrderId);
                                    json.put("cancellationreason",str_reason);
                                    json.put("comments",edit_query_des.getText().toString());
                                    json.put("submitstatus","Cancelled");
                                    json.put("modifiedon",currentDateTime);
                                    json.put("modifiedby",user_id);
                                    postCancelled(v,json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                            /*sqLiteController = new SQLiteController(Activity_OrdersHistory.this);
                            sqLiteController.open();
                            try {
                                sqLiteController.updatePresaleOrder(OrderId,"Cancelled",str_reason,edit_query_des.getText().toString());
                            } finally {
                                sqLiteController.close();
                                Toast.makeText(Activity_OrdersHistory.this, "Order has cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Activity_OrdersHistory.this, Activity_OrdersHistory.class);
                                startActivity(intent);
                            }*/
                                //Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                                Toast.makeText(Activity_OrdersHistory.this, "No Network Connection! Please check internet Connection and try again later.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });


            }
        });
    }

    private void postCancelled(final View v, final JSONObject json) {
        Utils.getInstance().loadingDialog(Activity_OrdersHistory.this, "Please wait.");
        try {
            App.getInstance().PostCancelPresale(json.toString(),token, new Callback() {
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
                                Utils.getInstance().GetMasterInsert(Activity_OrdersHistory.this,"OrderRefresh",Progress_dialog,"");
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {

                                        new Handler().postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                Utils.getInstance().dismissDialog();
                                                FillPresaleList(str_ordersfill);
                                                Toast.makeText(Activity_OrdersHistory.this, "Presale Order cancelled Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 6000);

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
    private void postKPI(String json) {
        try {
            App.getInstance().filterkpi(json.toString(),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        System.out.println("res---"+res);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    final JSONArray jr = new JSONArray(res);
                                    for (int i = 0; i < jr.length(); i++) {
                                        JSONObject js = jr.getJSONObject(i);
                                        String type = js.getString("type");
                                        String persistanceid = js.getString("persistanceid");
                                        String count = js.getString("count");
                                        if (type.equals("kpi")){

                                            if(persistanceid.equals("67a25351-9cd3-4a7e-ad47-a97d1b6f62f7")){
                                                //Total
                                                OrderTotal_tv.setText(count);
                                            }else if(persistanceid.equals("d9438fa4-2e46-4d9e-aaea-4f8cf53ed92c")){
                                                //Fulfill
                                                OrderFulfilled_tv.setText(count);
                                            }else if(persistanceid.equals("8325dcce-02bf-46f4-9b13-0ecc9c15348a")){
                                                //Yet Fulfill
                                                OrderYettoful_tv.setText(count);
                                            }else if(persistanceid.equals("bb7995ff-4e37-49ee-aae8-fcad699feffe")){
                                                //Todays
                                                OrderToday_tv.setText(count);
                                            }else if(persistanceid.equals("8e96db28-9736-4185-8417-ed8fdb7afe8e")){
                                                //Delivery
                                                deliverytoday_tv.setText(count);
                                            }

                                        }
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                } finally {

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


    private void postInvoiceKPI(String json) {
        try {
            App.getInstance().InvoiceFilterkpi(json.toString(),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        System.out.println("res---"+res);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    final JSONArray jr = new JSONArray(res);
                                    for (int i = 0; i < jr.length(); i++) {
                                        JSONObject js = jr.getJSONObject(i);
                                        String type = js.getString("type");
                                        String persistanceid = js.getString("persistanceid");
                                        String count = js.getString("count");

                                        if (type.equals("kpi")){

                                            if(persistanceid.equals("36e0fd02-7239-4f20-83b6-d3012b7852db")){
                                                //Total
                                                TotalInvoice_s=count;
                                            }else if(persistanceid.equals("d7ef00e1-d912-42d0-861c-b4535a25cde0")){
                                                //Paid
                                                PaidInvoice_s=count;
                                            }else if(persistanceid.equals("f49b1eef-8d01-4356-af8a-08daca1c29a5")){
                                                //UnPaid
                                                UnPaidInvoice_s=count;
                                            }

                                        }
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                } finally {

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


    @SuppressLint("Range")
    private void RepaceString(String OrderId_l,String no ) {

        SQLiteController sqLiteControllerC = new SQLiteController(Activity_OrdersHistory.this);
        sqLiteControllerC.open();
        try {
            long fetchAddressCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchAddressCount > 0) {
                Cursor C_Address = sqLiteControllerC.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("PresaleOrderTemplate")){

                            APIOrderHTML  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));

                        }



                    } while (C_Address.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();

            SQLiteController sqLiteController_h = new SQLiteController(this);
            sqLiteController_h.open();
            try {
                long count = sqLiteController_h.fetchOrderCount();
                if (count > 0) {
                    //Order
                    Cursor order_c = sqLiteController_h.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderId_l);
                    if (order_c != null && order_c.moveToFirst()) {
                        do {
                            String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                            String totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                            String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                            String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                            String datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                            String pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                            String  salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                            String id = order_c.getString(order_c.getColumnIndex("order_id"));
                            String billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                            String shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                            String totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                            String freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                            String totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                            String totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                            String discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                            String discountamountbase = order_c.getString(order_c.getColumnIndex("discountamountbase"));
                            String totalamount1 = order_c.getString(order_c.getColumnIndex("totalamount"));
                            String freightamountbase = order_c.getString(order_c.getColumnIndex("freightamountbase"));
                            String discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                            String termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));

                            String Outpur_Str = Utils.convertToIndianCurrency(totalamount);
                            Log.i("Outpur_Str", Outpur_Str);

                            APIOrderHTML = APIOrderHTML.replace("$totalUnit$", "1");
                            APIOrderHTML = APIOrderHTML.replace("$SubTotal$", currency_symbol+totallineitemamount);
                            APIOrderHTML = APIOrderHTML.replace("$DiscountPercentage$", "("+discountpercentage+" % )");
                            APIOrderHTML = APIOrderHTML.replace("$DiscountAmount$", currency_symbol+discountamount);
                            APIOrderHTML = APIOrderHTML.replace("$ShippingAmount$", currency_symbol+freightamount);
                            APIOrderHTML = APIOrderHTML.replace("$TaxPercentage$", "("+totaltaxbase+" % )");
                            APIOrderHTML = APIOrderHTML.replace("$TaxAmount$", currency_symbol+totaltax);
                            APIOrderHTML = APIOrderHTML.replace("$TotalAmount$", currency_symbol+totalamount1);
                            APIOrderHTML = APIOrderHTML.replace("$TermsandConditions$", termsconditions);
                            APIOrderHTML = APIOrderHTML.replace("$TotalAmountInWords$", Outpur_Str);

                            Cursor C_Address = sqLiteController_h.readTableAddress();
                            if (C_Address.moveToFirst()) {
                                do {
                                    String BId = C_Address.getString(C_Address.getColumnIndex("address_id"));
                                    String line2 = C_Address.getString(C_Address.getColumnIndex("line2"));
                                    String stateorprovince = C_Address.getString(C_Address.getColumnIndex("stateorprovince"));
                                    String city = C_Address.getString(C_Address.getColumnIndex("city"));
                                    String postalcode = C_Address.getString(C_Address.getColumnIndex("postalcode"));
                                    String country = C_Address.getString(C_Address.getColumnIndex("country"));


                                    if(billtoaddressid.equals(BId)){

                                        APIOrderHTML = APIOrderHTML.replace("$BillingAddress$", line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                    }

                                    if(shiptoaddressid.equals(BId)){
                                        APIOrderHTML = APIOrderHTML.replace("$ShippingAddress$", line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                    }



                                } while (C_Address.moveToNext());
                            }

                            APIOrderHTML = APIOrderHTML.replace("$PresaleOrderNumber$", " SO "+ordernumber);
                            APIOrderHTML = APIOrderHTML.replace("$TotalAmount$", currency_symbol+totalamount);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date = sdf.parse(submitdate);
                                        date1 = sdf.parse(datefulfilled);
                                        date2 = sdf.parse(pricingdate);
                                        String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                        String delivery_date =new SimpleDateFormat("MM/dd/yyyy").format(date1);
                                        String pricing_date =new SimpleDateFormat("MM/dd/yyyy").format(date2);

                                        APIOrderHTML = APIOrderHTML.replace("$PresaleOrderNumber$", ordernumber);
                                        APIOrderHTML = APIOrderHTML.replace("$OrderDate$", date_order);
                                        APIOrderHTML = APIOrderHTML.replace("$DeliveryDate$", delivery_date);
                                        APIOrderHTML = APIOrderHTML.replace("$ReferencePONumber$", "0");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }


                            });



                        } while (order_c.moveToNext());
                    }


                    Cursor Product_c = sqLiteController_h.readProductJoinTables(OrderId_l);

                    if (Product_c != null && Product_c.moveToFirst()) {
                        if (listInvoice.size() > 0) {
                            listInvoice.clear();
                        }
                        do {
                            @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                           // @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                            @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                            @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                            @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                            @SuppressLint("Range") String itemistaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));


                          //  Cursor product_c1 = sqLiteController_h.readAllTableProduct();
                            Cursor product_c1 = sqLiteController_h.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);

                            if (product_c1 != null && product_c1.moveToFirst()) {
                                do {

                                    @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                    if(product_id.equals(productid)){
                                        // @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                          @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                        @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                        @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                        listInvoice.add(new Model_InvoiceProductItems(product_name__, quantity, priceperunit, baseamount,itemistaxable,all_upsc,all_description));



                                    }

                                } while (product_c1.moveToNext());
                            }



                        } while (Product_c.moveToNext());
                    }


                }
            } finally {
                sqLiteController_h.close();
                CreateOrderPDF();
            }

        }





    }

    private void CreateOrderPDF() {

        String localRowhtml = "";


        for (int i = 0; i < listInvoice.size(); i++) {

            localRowhtml += RowOrderHtml;


            if(listInvoice.get(i).getAll_upsc().equals("null")){
                localRowhtml = localRowhtml.replace("prodname", listInvoice.get(i).getProduct_name());

            }else {
                localRowhtml = localRowhtml.replace("prodname", listInvoice.get(i).getProduct_name()+"\n- "+listInvoice.get(i).getAll_upsc());

            }
            localRowhtml = localRowhtml.replace("UPC Code", listInvoice.get(i).getAll_description());
            localRowhtml = localRowhtml.replace("price", listInvoice.get(i).getProduct_price());
            localRowhtml = localRowhtml.replace("Qty", listInvoice.get(i).getProduct_quantity());

            if (tax.equals("1")){
                if(listInvoice.get(i).getItemistaxable().equals("true")){
                    localRowhtml = localRowhtml.replace("Tax", "Taxable");

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                }
            }else {
                localRowhtml = localRowhtml.replace("Tax", "-");

            }

            localRowhtml = localRowhtml.replace("amount", listInvoice.get(i).getPrice_PerUnit());


        }

       // localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

        APIOrderHTML = APIOrderHTML.replace("$OrderProduct$", localRowhtml);

        APIOrderHTML=APIOrderHTML.replace("$CompanyLogo$", Companylogo);

        FileManager.getInstance().cleanTempFolder(getApplicationContext());
        // Create Temp File to save Pdf To
        final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
        // Generate Pdf From Html


        PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIOrderHTML+
                "</body>\n" +
                "</html>", new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                // Open Pdf Viewer
                Uri pdfUri = Uri.fromFile(savedPDFFile);

                Bundle bundle = new Bundle();
                bundle.putString("Order_flag","1");
                Intent intentPdfViewer = new Intent(Activity_OrdersHistory.this, PdfViewerActivity.class);
                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);
                intentPdfViewer.putExtras(bundle);
                startActivity(intentPdfViewer);

               /* StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                File sharingFile = new File(savedPDFFile.getPath());
                Uri outputPdfUri = FileProvider.getUriForFile(Activity_OrdersHistory.this, Activity_OrdersHistory.this.getPackageName() + ".provider", savedPDFFile);
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "testuurmi@gmail.com" });
                shareIntent.putExtra(Intent.EXTRA_STREAM, outputPdfUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //Write Permission might not be necessary
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share PDF using.."));*/

            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void GetTerms() {
        //  Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetTermsDetails(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Utils.getInstance().dismissDialog();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            SQLiteController sqlTerms= new SQLiteController(Activity_OrdersHistory.this);
                            sqlTerms.open();
                            try {
                                sqlTerms.deleteTableTerms();
                                JSONObject jsonObject = new JSONObject(res);
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject js = data.getJSONObject(i);
                                        String id = js.getString("id");
                                        String termname = js.getString("termname");
                                        String netduedays = js.getString("netduedays");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                SQLiteController sqlT= new SQLiteController(Activity_OrdersHistory.this);
                                                sqlT.open();
                                                try {
                                                    sqlT.insertTerms(id,termname,netduedays);
                                                } finally {
                                                    sqlT.close();
                                                }

                                            }
                                        });

                                    }
                                }

                            } finally {
                                sqlTerms.close();
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
    private void GetCompanylogo() {
        Companylogo =getCompanyLogo();
        //  Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetCompanylogo(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Utils.getInstance().dismissDialog();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(res);
                                    JSONArray data = jsonObject.getJSONArray("fileproperties");
                                    JSONObject jvb = data.getJSONObject(0);
                                    String filepath = jvb.getString("imagedata");
                                    Companylogo = "data:image/png;base64,"+filepath.toString();

                                    cmSharedPreferences = getSharedPreferences(ohPref, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = cmSharedPreferences.edit();
                                    editor.putString(CmpLogo, String.valueOf(Companylogo));
                                    editor.apply();

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
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

    private String getCompanyLogo() {
        InSharedPreferences = getSharedPreferences(Activity_OrdersHistory.ohPref, MODE_PRIVATE);
        if (InSharedPreferences.contains(Activity_OrdersHistory.CmpLogo)) {
            return InSharedPreferences.getString(Activity_OrdersHistory.CmpLogo, null);
        }
        return " ";
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
