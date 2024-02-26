package com.example.arcomdriver.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_OfflineList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_UpdateCustomer;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.ActivitySignIn;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_OfflineList;
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
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_OfflineSync extends Activity_Menu {
    private CoordinatorLayout cl;
    private RecyclerView mRecyclerView;
    AppCompatButton syncBtn;
    AppCompatTextView text_m;
    CheckBox chkAllOSelected;
    String user_id,token,Status="Presale",currentDate1;


    ArrayList<Model_OfflineList> OfList = new ArrayList<Model_OfflineList>();;
    ArrayList<Model_OfflineList> filteredModelList = new ArrayList<Model_OfflineList>();;
    Adapter_OfflineList mAdapter;
    private SwipeRefreshLayout srl;
    AppCompatSpinner OfStatus_sp;
    androidx.appcompat.widget.SearchView searchBar;

    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusName = new ArrayList<>();
    //Toolbar toolbar;

    public AlertDialog Progress_dialog;

    LottieAnimationView txt_no_record;

    String Flag="0";

    String OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync;

    @SuppressLint({"Range", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_offlinelist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_offlinelist, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Offline Sync");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Flag = extras.getString("Flag");


        }


        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OfflineSync.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_OfflineSync.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

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
                    System.out.println("user_id---"+user_id);
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                }

                //ReadMasterSync
                Cursor Csync = sqLiteController1.readTableMasterSync();
                if (Csync.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String master_sync_name = Csync.getString(Csync.getColumnIndex("master_sync_name"));
                        @SuppressLint("Range") String master_sync_time = Csync.getString(Csync.getColumnIndex("master_sync_time"));

                        System.out.println("master_sync_name----"+master_sync_name);
                        System.out.println("master_sync_time----"+master_sync_time);

                        if(master_sync_name.equals("Orders")){
                            OrderLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Orders_products")){
                            OrderProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Shipments")){
                            ShipmentLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Addresses")){
                            AddressLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Invoices")){
                            InvoiceLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Invoices_products")){
                            InvoiceProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Customers")){
                            CustomerLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Products")){
                            ProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Products_assets")){
                            ProductAssertLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }
                    } while (Csync.moveToNext());
                }


            }
        } finally {
            sqLiteController1.close();
        }


        cl = findViewById(R.id.cl);
        //toolbar = findViewById(R.id.toolbar);
        text_m = findViewById(R.id.text_m);
        txt_no_record = findViewById(R.id.txt_no_record);
        srl = findViewById(R.id.swipe);
        searchBar = (SearchView) findViewById(R.id.search_OfflineListview);
        chkAllOSelected =findViewById(R.id.chkAllOSelected);
        txt_no_record = findViewById(R.id.txt_no_record);
        syncBtn = findViewById(R.id.syncBtn);
        syncBtn.setFocusable(true);
        syncBtn.setClickable(true);
        syncBtn.setFocusableInTouchMode(true);
        syncBtn.setText("Sync");
        mRecyclerView = findViewById(R.id.rc_OfflineList);
        OfStatus_sp = findViewById(R.id.OfStatus_sp);



        syncBtn.setOnTouchListener(new View.OnTouchListener(){

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                {
                    if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                            Connectivity.isConnectedFast(Activity_OfflineSync.this)) {

                        if(Status.equals("Presale")){


                            String Order_name = "";
                            String Order_id = "";
                            String OrderU_id = "";
                            for (int i = 0; i < OfList.size(); i++) {
                                Model_OfflineList OfflineList = OfList.get(i);

                                if (OfflineList.isSelected()) {

                                    syncBtn.setBackgroundResource(R.drawable.shape_btngraey);
                                    syncBtn.setText("Loading..");
                                    syncBtn.setFocusable(false);
                                    syncBtn.setClickable(false);
                                    syncBtn.setFocusableInTouchMode(false);

                                    Order_name = OfflineList.getOrder_name();
                                    Order_id = OfflineList.getOrderId();
                                    OrderU_id = OfflineList.getOrderId();


                                    if(Order_name.equals("CreateOrder")){

                                        //Order_id = OfflineList.getOrderId();
                                        System.out.println("Order_id---"+Order_id);

                                        String ordernumber = "";
                                        String totalamount  = "";
                                        String submitdate  = "";
                                        String submitstatus  = "";
                                        String datefulfilled  = "";
                                        String pricingdate  = "";
                                        String salesrepid  = "";
                                        String totallineitemamount  = "";
                                        String discountpercentage  = "";
                                        String discountamount  = "";
                                        String freightamount  = "";
                                        String totaltaxbase  = "";
                                        String totaltax  = "";
                                        String shiptoaddressid  = "";
                                        String billtoaddressid  = "";
                                        String deliverynote  = "";
                                        String shipnote  = "";
                                        String termsconditions  = "";
                                        String memo  = "";
                                        String customerid  = "";
                                        String createdby  = "";
                                        String creratedon  = "";
                                        String warehouseid  = "";
                                        String customeristaxable = "";
                                        String draftnumber = "";
                                        String lastsyncon = "";
                                        String shipmenttypeid = "";
                                        String or_taxId = "";
                                        String or_termsId = "";
                                        String or_dueDate = "";
                                        String agent = "";

                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long count = sqLiteController.fetchCount();
                                            if (count > 0) {

                                                JSONObject postedJSON = new JSONObject();
                                                JSONArray array =new JSONArray();

                                                Cursor Product_c = sqLiteController.readProductJoinTables(Order_id);
                                                if (Product_c != null && Product_c.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                        @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                        @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                        @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                        @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                        @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                        @SuppressLint("Range") String warehouseid_o = Product_c.getString(Product_c.getColumnIndex("warehouseid"));

                                                        double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);

                                                        postedJSON = new JSONObject();
                                                        try {
                                                            postedJSON.put("sid", "");
                                                            postedJSON.put("id", productid);
                                                            postedJSON.put("invoiceid", "");
                                                            postedJSON.put("orderpid", "");
                                                            postedJSON.put("transactioncurrencyid", "");
                                                            postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");
                                                            postedJSON.put("baseamount", TotalBaseAmt);
                                                            postedJSON.put( "exchangerate", "0");
                                                            postedJSON.put("baseamountbase", "0");
                                                            postedJSON.put( "description", "");
                                                            postedJSON.put( "extendedamount", "0");
                                                            postedJSON.put("extendedamountbase", "0");
                                                            postedJSON.put( "iscopied", false);
                                                            postedJSON.put( "ispriceoverridden", false);
                                                            postedJSON.put("isproductoverridden", false);
                                                            postedJSON.put( "lineitemnumber", "0");
                                                            postedJSON.put("manualdiscountamount", "0");
                                                            postedJSON.put("manualdiscountamountbase", "0");
                                                            postedJSON.put( "priceperunit", priceperunit);
                                                            postedJSON.put("priceperunitbase", "0");
                                                            postedJSON.put("productdescription", "");
                                                            postedJSON.put("productname", productname);
                                                            postedJSON.put("productid", productid);
                                                            postedJSON.put("quantity", quantity);
                                                            postedJSON.put("quantitybackordered", "0");
                                                            postedJSON.put("quantitycancelled", "0");
                                                            postedJSON.put("quantityshipped", "0");
                                                            postedJSON.put( "quantitypicked", "");
                                                            postedJSON.put( "quantitypacked", "");
                                                            postedJSON.put("requestdeliveryby", "");
                                                            postedJSON.put( "salesorderispricelocked", false);
                                                            postedJSON.put("salesrepid", "");
                                                            postedJSON.put( "shiptoaddressid", "00000000-0000-0000-0000-000000000000");
                                                            postedJSON.put( "tax", "0");
                                                            postedJSON.put( "taxbase", "0");
                                                            postedJSON.put("willcall", false);
                                                            postedJSON.put( "productimage", "");
                                                            postedJSON.put( "imageurl", "");
                                                            postedJSON.put( "uomname", "");
                                                            postedJSON.put( "upccode", "");
                                                            postedJSON.put( "ordered", "");
                                                            postedJSON.put("picked", "");
                                                            postedJSON.put("packed", "");
                                                            postedJSON.put("createdby", "");
                                                            postedJSON.put("createdon", "");
                                                            postedJSON.put( "modifiedby", user_id);
                                                            postedJSON.put("modifiedon", currentDate1);
                                                            postedJSON.put("isactive", true);
                                                            postedJSON.put( "orderid", "00000000-0000-0000-0000-000000000000");
                                                            postedJSON.put( "name", productname);
                                                            postedJSON.put("orderquantity",  quantity);
                                                            postedJSON.put( "warehouseid", warehouseid_o);///issue
                                                            postedJSON.put("isHigh", true);
                                                            if(istaxable.equals("true")){
                                                                postedJSON.put("itemistaxable", true);
                                                            }else {
                                                                postedJSON.put("itemistaxable", false);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        array.put(postedJSON);


                                                    } while (Product_c.moveToNext());
                                                }


                                                //Order
                                                Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,Order_id);
                                                if (order_c != null && order_c.moveToFirst()) {
                                                    do {
                                                        ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                                                        totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                        submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                                                        submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                                                        datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                                                        pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                        salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                        totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                        discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                        discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                        freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                        totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                        totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                        shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                        billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                        deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                        shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                        termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                        memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                        customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                        createdby = order_c.getString(order_c.getColumnIndex("createdby"));
                                                        creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));
                                                        warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                        customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                        draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                        lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                                                        shipmenttypeid = order_c.getString(order_c.getColumnIndex("shipmenttypeid"));
                                                        or_taxId = order_c.getString(order_c.getColumnIndex("or_taxId"));
                                                        or_termsId = order_c.getString(order_c.getColumnIndex("or_termsId"));
                                                        or_dueDate = order_c.getString(order_c.getColumnIndex("or_dueDate"));
                                                        agent = order_c.getString(order_c.getColumnIndex("agent"));

                                                        System.out.println("ordernumber---"+ordernumber);


                                                        JSONObject json = new JSONObject();
                                                        try {
                                                            json.put("id","00000000-0000-0000-0000-000000000000");
                                                            json.put("billtoaddressid",billtoaddressid);
                                                            json.put("customerid",customerid);
                                                            json.put("datefulfilled",datefulfilled);
                                                            json.put("discountamount",String.valueOf(discountamount));
                                                            json.put("discountpercentage",discountpercentage);
                                                            json.put("freightamount",freightamount);
                                                            json.put("ordernumber","0");
                                                            json.put("shiptoaddressid",shiptoaddressid);
                                                            json.put("submitdate",submitdate);
                                                            json.put("submitstatus","New Order");
                                                            json.put("totalamount",totalamount);
                                                            json.put("totallineitemamount",totallineitemamount);
                                                            json.put("totaltax",totaltax);
                                                            json.put("totaltaxbase",totaltaxbase.trim());
                                                            json.put("createdby",createdby);
                                                            json.put("creratedon",currentDate1);
                                                            json.put("poreferencenum","0");
                                                            json.put("salesrepid",salesrepid);
                                                            json.put("pricingdate",currentDate1);
                                                            json.put("isactive", true);
                                                            json.put("deliverynote",deliverynote);
                                                            json.put("shipnote",shipnote);
                                                            json.put("termsconditions",termsconditions);
                                                            json.put("memo",memo);
                                                            json.put("quoteid","00000000-0000-0000-0000-000000000000");
                                                            json.put("warehouseid",warehouseid);
                                                            json.put("draftnumber",draftnumber);
                                                            json.put("lastsyncon",lastsyncon);
                                                            //json.put("taxid",or_taxId);
                                                            //json.put("netterms",or_termsId);
                                                            //json.put("duedate",or_dueDate);
                                                            if(or_taxId.equals("00000000-0000-0000-0000-000000000000")){
                                                                json.put("taxid",null);
                                                            }else{
                                                                json.put("taxid",or_taxId);
                                                            }

                                                            if(or_termsId.equals("0")){
                                                                json.put("netterms",null);
                                                            }else{
                                                                json.put("netterms",or_termsId);
                                                            }

                                                            if(or_dueDate.equals("MM/DD/YYYY")){
                                                                json.put("duedate",null);
                                                            }else {
                                                                json.put("duedate",or_dueDate);
                                                            }

                                                            json.put("orderitem",array);
                                                            if(customeristaxable.equals("true")){
                                                                json.put("customeristaxable", true);
                                                            }else {
                                                                json.put("customeristaxable", false);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        //OfflineList.setUploadStatus("1");
                                                        //mAdapter.notifyDataSetChanged();

                                                        System.out.println("Save Presale---"+json.toString());
                                                        Progress_dialog.show();
                                                        postPartRequest(json.toString(),Order_id,shipmenttypeid,agent);
                                                    } while (order_c.moveToNext());
                                                }

                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }


                                    }
                                    else if(Order_name.equals("UpdateOrder")){

                                        System.out.println("Update");

                                        // OrderU_id = OfflineList.getOrderId();
                                        System.out.println("OrderU_id---"+OrderU_id);

                                        String ordernumber = "";
                                        String totalamount  = "";
                                        String submitdate  = "";
                                        String submitstatus  = "";
                                        String datefulfilled  = "";
                                        String pricingdate  = "";
                                        String salesrepid  = "";
                                        String totallineitemamount  = "";
                                        String discountpercentage  = "";
                                        String discountamount  = "";
                                        String freightamount  = "";
                                        String totaltaxbase  = "";
                                        String totaltax  = "";
                                        String shiptoaddressid  = "";
                                        String billtoaddressid  = "";
                                        String deliverynote  = "";
                                        String shipnote  = "";
                                        String termsconditions  = "";
                                        String memo  = "";
                                        String customerid  = "";
                                        String createdby  = "";
                                        String creratedon  = "";
                                        String warehouseid  = "";
                                        String customeristaxable = "";
                                        String draftnumber = "";
                                        String lastsyncon = "";
                                        String shipmenttypeid = "";
                                        String or_taxId = "";
                                        String or_termsId = "";
                                        String or_dueDate = "";
                                        String deletedby = "";

                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long count = sqLiteController.fetchCount();
                                            if (count > 0) {

                                                JSONObject postedJSON = new JSONObject();
                                                JSONArray array =new JSONArray();

                                                Cursor Product_c = sqLiteController.readProductJoinTables(OrderU_id);
                                                if (Product_c != null && Product_c.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                        @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                        @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                        @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                        @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                        @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                        @SuppressLint("Range") String warehouseid_o = Product_c.getString(Product_c.getColumnIndex("warehouseid"));

                                                        double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);

                                                        postedJSON = new JSONObject();
                                                        try {
                                                            postedJSON.put("id",productid);
                                                            postedJSON.put("isactive", true);
                                                            postedJSON.put("productid", productid);
                                                            postedJSON.put("name", productname);
                                                            postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                                                            postedJSON.put("quantityonhand", "0");
                                                            postedJSON.put("orderquantity", quantity);
                                                            postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                                                            postedJSON.put("priceperunit",  priceperunit);
                                                            postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                                                            postedJSON.put("delete", "null");
                                                            postedJSON.put("warehouseid", warehouseid_o);//warehouseid
                                                            postedJSON.put("modifiedby", user_id);
                                                            postedJSON.put("modifiedon", currentDate1);
                                                            postedJSON.put("isHigh", true);
                                                            postedJSON.put("quantity", quantity);//quantity
                                                            postedJSON.put("available", "0");
                                                            postedJSON.put("committed", "0");
                                                            if(istaxable.equals("true")){
                                                                postedJSON.put("itemistaxable", true);
                                                            }else {
                                                                postedJSON.put("itemistaxable", false);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        array.put(postedJSON);


                                                    } while (Product_c.moveToNext());
                                                }


                                                //Order
                                                Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderU_id);
                                                if (order_c != null && order_c.moveToFirst()) {
                                                    do {
                                                        ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                                                        totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                        submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                                                        submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                                                        datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                                                        pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                        salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                        totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                        discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                        discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                        freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                        totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                        totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                        shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                        billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                        deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                        shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                        termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                        memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                        customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                        createdby = order_c.getString(order_c.getColumnIndex("createdby"));
                                                        creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));
                                                        warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                        customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                        draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                        lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                                                        shipmenttypeid = order_c.getString(order_c.getColumnIndex("shipmenttypeid"));
                                                        or_taxId = order_c.getString(order_c.getColumnIndex("or_taxId"));
                                                        or_termsId = order_c.getString(order_c.getColumnIndex("or_termsId"));
                                                        or_dueDate = order_c.getString(order_c.getColumnIndex("or_dueDate"));
                                                        deletedby = order_c.getString(order_c.getColumnIndex("deletedby"));

                                                        System.out.println("ordernumber---"+ordernumber);


                                                        JSONObject json = new JSONObject();
                                                        try {
                                                            json.put("id",OrderU_id);
                                                            json.put("billtoaddressid",billtoaddressid);
                                                            json.put("customerid",customerid);
                                                            json.put("datefulfilled",datefulfilled);
                                                            json.put("discountamount",String.valueOf(discountamount));//discount vara amout
                                                            json.put("discountpercentage",discountpercentage);
                                                            json.put("freightamount",freightamount);
                                                            json.put("totaltaxbase",totaltaxbase.trim());
                                                            json.put("ordernumber",ordernumber);// order number
                                                            json.put("shiptoaddressid",shiptoaddressid);//shipID
                                                            json.put("submitdate",submitdate);
                                                            json.put("submitstatus",submitstatus);
                                                            json.put("totalamount",totalamount); //totalpayyable
                                                            json.put("totallineitemamount",totallineitemamount);
                                                            json.put("totaltax",totaltax);
                                                            json.put("poreferencenum","0");
                                                            json.put("salesrepid",salesrepid);
                                                            json.put("pricingdate",currentDate1);
                                                            json.put("isactive", true);//boolean true
                                                            json.put("deliverynote",deliverynote);
                                                            json.put("shipnote",shipnote);
                                                            json.put("termsconditions",termsconditions);
                                                            json.put("memo",memo);
                                                            json.put("warehouseid",warehouseid);
                                                            if(!deletedby.equals("0")){
                                                                json.put("deletedRows",deletedby);
                                                            }
                                                            json.put("modifiedon",currentDate1);
                                                            json.put("draftnumber",draftnumber);
                                                            json.put("lastsyncon",lastsyncon);
                                                           // json.put("taxid",or_taxId);
                                                            //json.put("netterms",or_termsId);
                                                            //json.put("duedate",or_dueDate);

                                                            if(or_taxId.equals("00000000-0000-0000-0000-000000000000")){
                                                                json.put("taxid",null);
                                                            }else{
                                                                json.put("taxid",or_taxId);
                                                            }

                                                            if(or_termsId.equals("0")){
                                                                json.put("netterms",null);
                                                            }else{
                                                                json.put("netterms",or_termsId);
                                                            }

                                                            if(or_dueDate.equals("MM/DD/YYYY")){
                                                                json.put("duedate",null);
                                                            }else {
                                                                json.put("duedate",or_dueDate);
                                                            }

                                                            json.put("orderitem",array);
                                                            if(customeristaxable.equals("true")){
                                                                json.put("customeristaxable", true);
                                                            }else {
                                                                json.put("customeristaxable", false);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        //OfflineList.setUploadStatus("1");
                                                        //mAdapter.notifyDataSetChanged();

                                                        System.out.println("Update Presale ---"+json.toString());

                                                        System.out.println("OrderU_id----"+OrderU_id);
                                                        System.out.println("draftnumber----"+draftnumber);
                                                        Progress_dialog.show();
                                                        postUpdatePresale(json.toString(),OrderU_id,shipmenttypeid,draftnumber);
                                                    } while (order_c.moveToNext());
                                                }

                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }


                                    }

                                }

                            }

                        }else {

                            System.out.println("Invoice--"+"Invoice");

                            String InvoiceS_id = "";
                            for (int i = 0; i < OfList.size(); i++) {
                                Model_OfflineList OfflineList = OfList.get(i);

                                if (OfflineList.isSelected()) {

                                    syncBtn.setBackgroundResource(R.drawable.shape_btngraey);
                                    syncBtn.setText("Loading..");
                                    syncBtn.setFocusable(false);
                                    syncBtn.setClickable(false);
                                    syncBtn.setFocusableInTouchMode(false);

                                    InvoiceS_id = OfflineList.getOrderId();

                                    System.out.println("InvoiceS_id---"+InvoiceS_id);

                                    String orderid = "";
                                    String paymenttypeid = "";
                                    String paymentmethod = "";
                                    String billtoaddressid = "";
                                    String customerid = "";
                                    String discountamount = "";
                                    String timestamp = "";
                                    String discountpercentage = "";
                                    String freightamount = "";
                                    String shiptoaddressid = "";
                                    String invoicedate = "";
                                    String submitstatus = "";
                                    String totalamount = "";
                                    String totallineitemamount = "";
                                    String totaltax = "";
                                    String totaltaxbase = "";
                                    String salesrepid = "";
                                    String pricingdate = "";
                                    String deliverynote = "";
                                    String shipnote = "";
                                    String termsconditions = "";
                                    String memo = "";
                                    String warehouseid = "";
                                    String customeristaxable = "";
                                    String draftnumber = "";
                                    String lastsyncon = "";
                                    String in_taxid = "";
                                    String in_terms = "";
                                    String in_due = "";
                                    String deletedby = "";


                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        long count = sqLiteController.fetchCount();
                                        if (count > 0) {

                                            JSONObject postedJSON = new JSONObject();
                                            JSONArray array =new JSONArray();

                                            Cursor Product_c = sqLiteController.readInvoiceProductJoinTables(InvoiceS_id);
                                            if (Product_c != null && Product_c.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = Product_c.getString(Product_c.getColumnIndex("id"));
                                                    @SuppressLint("Range") String Orderproductid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                    @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                    @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                    @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                    @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                    @SuppressLint("Range") String itemistaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                    @SuppressLint("Range") String itemorderid = Product_c.getString(Product_c.getColumnIndex("itemorderid"));
                                                    @SuppressLint("Range") String itemwarehouseid = Product_c.getString(Product_c.getColumnIndex("itemwarehouseid"));
                                                    @SuppressLint("Range") String orderpid = Product_c.getString(Product_c.getColumnIndex("orderpid"));


                                                    double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);

                                                    System.out.println("id-----"+id);
                                                    System.out.println("orderpid-----"+orderpid);
                                                    System.out.println("productid-----"+Orderproductid);


                                                    postedJSON = new JSONObject();
                                                    try {
                                                        postedJSON.put("id",id);
                                                        postedJSON.put("isactive", true);

                                                        if(itemorderid.equals("0")){
                                                        }else {
                                                            postedJSON.put("orderid",itemorderid);
                                                        }

                                                        if(itemorderid.equals("0")){
                                                        }else {
                                                            postedJSON.put("orderpid", orderpid);
                                                        }

                                                        postedJSON.put("productid", Orderproductid);
                                                        postedJSON.put("name", productname);
                                                        postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                                                        postedJSON.put("quantityonhand", "0");
                                                        postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                                                        postedJSON.put("priceperunit",  priceperunit);
                                                        postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                                                        postedJSON.put("delete", "null");
                                                        postedJSON.put("warehouseid", itemwarehouseid);//warehouseid
                                                        postedJSON.put("modifiedby", user_id);
                                                        postedJSON.put("modifiedon", currentDate1);
                                                        postedJSON.put("isHigh", true);
                                                        postedJSON.put("quantity", quantity);//quantity
                                                        postedJSON.put("available", "0");
                                                        postedJSON.put("committed", "0");
                                                        if(itemistaxable.equals("true")){
                                                            postedJSON.put("itemistaxable", true);
                                                        }else {
                                                            postedJSON.put("itemistaxable", false);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    array.put(postedJSON);


                                                } while (Product_c.moveToNext());
                                            }


                                            //Order
                                            Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,InvoiceS_id);
                                            if (order_c != null && order_c.moveToFirst()) {
                                                do {
                                                    orderid = order_c.getString(order_c.getColumnIndex("orderid"));
                                                    paymenttypeid = order_c.getString(order_c.getColumnIndex("paymenttypeid"));
                                                    paymentmethod = order_c.getString(order_c.getColumnIndex("paymentmethod"));
                                                    billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                    customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                    discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                    timestamp = order_c.getString(order_c.getColumnIndex("timestamp"));
                                                    discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                    freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                    shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                    invoicedate = order_c.getString(order_c.getColumnIndex("invoicedate"));
                                                    submitstatus = order_c.getString(order_c.getColumnIndex("Insubmitstatus"));
                                                    totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                    totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                    totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                    totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                    salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                    pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                    deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                    shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                    termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                    memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                    warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                    customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                    customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                    draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                    lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                                                    in_taxid = order_c.getString(order_c.getColumnIndex("in_taxid"));
                                                    in_terms = order_c.getString(order_c.getColumnIndex("in_terms"));
                                                    in_due = order_c.getString(order_c.getColumnIndex("in_due"));
                                                    deletedby = order_c.getString(order_c.getColumnIndex("deletedby"));

                                                    // System.out.println("ordernumber---"+ordernumber);


                                                    JSONObject json = new JSONObject();
                                                    try {
                                                        if(orderid.equals("0")){
                                                            json.put("orderid","00000000-0000-0000-0000-000000000000");
                                                        }else {
                                                            json.put("orderid",orderid);
                                                        }
                                                        json.put("paymenttypeid",paymenttypeid);
                                                        json.put("paymentid","00000000-0000-0000-0000-000000000000");
                                                        json.put("paymentmethod",paymentmethod);
                                                        json.put("name","");
                                                        json.put("billtoaddressid",billtoaddressid);
                                                        json.put("customerid",customerid);
                                                        json.put( "vendorid","00000000-0000-0000-0000-000000000000");
                                                        json.put("datefulfilled","00000000-0000-0000-0000-000000000000");
                                                        json.put("description","");
                                                        json.put( "discountamount",String.valueOf(discountamount));
                                                        json.put("timestamp",timestamp);
                                                        json.put("transactioncurrencyid","00000000-0000-0000-0000-000000000000");
                                                        json.put("exchangerate","0");
                                                        json.put( "discountamountbase","0");
                                                        json.put("discountpercentage",discountpercentage);
                                                        json.put("freightamount",freightamount);
                                                        json.put("freightamountbase","0");
                                                        json.put( "freighttermscode","0");
                                                        json.put( "ispricelocked",true);
                                                        json.put( "lastbackofficesubmit",timestamp);
                                                        json.put( "ordernumber","0");
                                                        json.put("quoteid","00000000-0000-0000-0000-000000000000");
                                                        json.put( "requestdeliveryby","00000000-0000-0000-0000-000000000000");
                                                        json.put( "shiptoaddressid",shiptoaddressid);
                                                        json.put("invoicedate",invoicedate);
                                                        json.put("submitstatus",submitstatus);
                                                        json.put("submitstatusdescription","");
                                                        json.put("totalamount",String.valueOf(totalamount));
                                                        json.put("totalamountbase","0");
                                                        json.put("totalamountlessfreight","0");
                                                        json.put("totalamountlessfreightbase","0");
                                                        json.put("totaldiscountamount","0");
                                                        json.put("totaldiscountamountbase","0");
                                                        json.put("totallineitemamount",String.valueOf(totallineitemamount));
                                                        json.put( "totallineitemamountbase","0");
                                                        json.put("totallineitemdiscountamount","0");
                                                        json.put( "totallineitemdiscountamountbase","0");
                                                        json.put("totaltax",String.valueOf(totaltax));
                                                        json.put("totaltaxbase",totaltaxbase.trim());
                                                        json.put("willcall",true);
                                                        json.put("salesrepid",salesrepid);
                                                        json.put("pricingdate",pricingdate);
                                                        json.put( "deliverynote",deliverynote);
                                                        json.put("shipnote",shipnote);
                                                        json.put("termsconditions",termsconditions);
                                                        json.put("memo",memo);
                                                        json.put("referenceorder","0");
                                                        json.put("createdby",user_id);
                                                        json.put("createdon",currentDate1);
                                                        json.put("modifiedby",user_id);
                                                        json.put("modifiedon",currentDate1);
                                                        json.put("isactive",true);
                                                        json.put("isPresale",true);
                                                        if(!deletedby.equals("0")){
                                                            json.put("deletedRows",deletedby);
                                                        }
                                                        json.put("warehouseid",warehouseid);
                                                        json.put("draftnumber",draftnumber);
                                                        json.put("lastsyncon",lastsyncon);

                                                        if(in_taxid.equals("00000000-0000-0000-0000-000000000000")){
                                                            json.put("taxid",null);
                                                        }else{
                                                            json.put("taxid",in_taxid);
                                                        }

                                                        if(in_terms.equals("0")){
                                                            json.put("netterms",null);
                                                        }else{
                                                            json.put("netterms",in_terms);
                                                        }

                                                        if(in_due.equals("MM/DD/YYYY")){
                                                            json.put("duedate",null);
                                                        }else {
                                                            json.put("duedate",in_due);
                                                        }

                                                        json.put("invoiceproduct",array);
                                                        if(customeristaxable.equals("true")){
                                                            json.put("customeristaxable", true);
                                                        }else {
                                                            json.put("customeristaxable", false);
                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    //OfflineList.setUploadStatus("1");
                                                    //mAdapter.notifyDataSetChanged();

                                                    System.out.println("JSONInvoice---"+json.toString());
                                                    Progress_dialog.show();
                                                    postInvoicePartRequest(json.toString(),InvoiceS_id,orderid,totalamount);
                                                } while (order_c.moveToNext());
                                            }

                                        }
                                    } finally {
                                        sqLiteController.close();
                                    }

                                }

                            }
                        }




                    }else {
                        Utils.getInstance().snackBarMessage(view,"No Network Connection! Please check internet Connection and try again later.");
                    }

                    return true;
                }
                return false;
            }
        });

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        currentDate1 = sdf.format(date1);

        if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                Connectivity.isConnectedFast(Activity_OfflineSync.this)) {
            syncBtn.setBackgroundResource(R.drawable.shape_btngreen_bg);
            syncBtn.setText("Sync");
        }else {
            syncBtn.setBackgroundResource(R.drawable.shape_redbg);
            syncBtn.setText("Offline");

        }


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (srl.isRefreshing()) srl.setRefreshing(false);
                if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                        Connectivity.isConnectedFast(Activity_OfflineSync.this)) {
                    syncBtn.setBackgroundResource(R.drawable.shape_btngreen_bg);
                    syncBtn.setText("Sync");
                }else {
                    syncBtn.setBackgroundResource(R.drawable.shape_redbg);
                    syncBtn.setText("Offline");

                }
            }
        });

      /*  AppCompatButton btn_order = findViewById(R.id.btn_order);
        AppCompatButton btn_recurring_order = findViewById(R.id.btn_recurring_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_order.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_recurring_order.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn4));

            }
        });
        btn_recurring_order.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                btn_order.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));
                btn_recurring_order.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn3));
            }
        });*/


       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_OfflineSync.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });*/


        mAdapter = new Adapter_OfflineList(OfList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_OfflineSync.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(Activity_OfflineSync.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_OfflineSync.this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                view.findViewById(R.id.Or_delete_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(Status.equals("Presale")){
                            SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                            sqLiteController.open();
                            try {
                                sqLiteController.deleteOrder(OfList.get(position).getOrderId());
                                sqLiteController.deleteProductItems(OfList.get(position).getOrderId());
                            } finally {
                                sqLiteController.close();
                            }
                            OfList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(Activity_OfflineSync.this, "Item has been deleted successfully!", Toast.LENGTH_SHORT).show();

                        }else {
                            SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                            sqLiteController.open();
                            try {
                                sqLiteController.deleteInvoice(OfList.get(position).getOrderId());
                                sqLiteController.deleteInvoiceProductItems(OfList.get(position).getOrderId());
                            } finally {
                                sqLiteController.close();
                            }
                            OfList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(Activity_OfflineSync.this, "Item has been deleted successfully!", Toast.LENGTH_SHORT).show();

                        }


                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {


            }

        }));







        chkAllOSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    for (int i = 0; i < OfList.size(); i++) {
                        Model_OfflineList singleStudent = OfList.get(i);
                        singleStudent.setSelected(true);

                        OfList.get(i).setSelected(true);
                        // mAdapter.notifyItemChanged(i);
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (!cb.isChecked()){
                    for (int i = 0; i < OfList.size(); i++) {
                        Model_OfflineList singleStudent = OfList.get(i);
                        singleStudent.setSelected(false);

                        OfList.get(i).setSelected(false);
                        // mAdapter.notifyItemChanged(i);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

       /* findViewById(R.id.syncBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                        Connectivity.isConnectedFast(Activity_OfflineSync.this)) {

                    if(Status.equals("Presale")){


                        String Order_name = "";
                        String Order_id = "";
                        String OrderU_id = "";
                        for (int i = 0; i < OfList.size(); i++) {
                            Model_OfflineList OfflineList = OfList.get(i);

                            if (OfflineList.isSelected()) {

                                syncBtn.setBackgroundResource(R.drawable.shape_btngraey);
                                syncBtn.setText("Loading..");
                                syncBtn.setFocusable(false);
                                syncBtn.setClickable(false);
                                syncBtn.setFocusableInTouchMode(false);

                                Order_name = OfflineList.getOrder_name();
                                Order_id = OfflineList.getOrderId();
                                OrderU_id = OfflineList.getOrderId();


                                if(Order_name.equals("CreateOrder")){

                                    //Order_id = OfflineList.getOrderId();
                                    System.out.println("Order_id---"+Order_id);

                                    String ordernumber = "";
                                    String totalamount  = "";
                                    String submitdate  = "";
                                    String submitstatus  = "";
                                    String datefulfilled  = "";
                                    String pricingdate  = "";
                                    String salesrepid  = "";
                                    String totallineitemamount  = "";
                                    String discountpercentage  = "";
                                    String discountamount  = "";
                                    String freightamount  = "";
                                    String totaltaxbase  = "";
                                    String totaltax  = "";
                                    String shiptoaddressid  = "";
                                    String billtoaddressid  = "";
                                    String deliverynote  = "";
                                    String shipnote  = "";
                                    String termsconditions  = "";
                                    String memo  = "";
                                    String customerid  = "";
                                    String createdby  = "";
                                    String creratedon  = "";
                                    String warehouseid  = "";
                                    String customeristaxable = "";
                                    String draftnumber = "";
                                    String lastsyncon = "";
                                    String shipmenttypeid = "";

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        long count = sqLiteController.fetchCount();
                                        if (count > 0) {

                                            JSONObject postedJSON = new JSONObject();
                                            JSONArray array =new JSONArray();

                                            Cursor Product_c = sqLiteController.readProductJoinTables(Order_id);
                                            if (Product_c != null && Product_c.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                    @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                    @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                    @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                    @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                    @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                    @SuppressLint("Range") String warehouseid_o = Product_c.getString(Product_c.getColumnIndex("warehouseid"));

                                                    double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);

                                                    postedJSON = new JSONObject();
                                                    try {
                                                        postedJSON.put("sid", "");
                                                        postedJSON.put("id", productid);
                                                        postedJSON.put("invoiceid", "");
                                                        postedJSON.put("orderpid", "");
                                                        postedJSON.put("transactioncurrencyid", "");
                                                        postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");
                                                        postedJSON.put("baseamount", TotalBaseAmt);
                                                        postedJSON.put( "exchangerate", "0");
                                                        postedJSON.put("baseamountbase", "0");
                                                        postedJSON.put( "description", "");
                                                        postedJSON.put( "extendedamount", "0");
                                                        postedJSON.put("extendedamountbase", "0");
                                                        postedJSON.put( "iscopied", false);
                                                        postedJSON.put( "ispriceoverridden", false);
                                                        postedJSON.put("isproductoverridden", false);
                                                        postedJSON.put( "lineitemnumber", "0");
                                                        postedJSON.put("manualdiscountamount", "0");
                                                        postedJSON.put("manualdiscountamountbase", "0");
                                                        postedJSON.put( "priceperunit", priceperunit);
                                                        postedJSON.put("priceperunitbase", "0");
                                                        postedJSON.put("productdescription", "");
                                                        postedJSON.put("productname", productname);
                                                        postedJSON.put("productid", productid);
                                                        postedJSON.put("quantity", quantity);
                                                        postedJSON.put("quantitybackordered", "0");
                                                        postedJSON.put("quantitycancelled", "0");
                                                        postedJSON.put("quantityshipped", "0");
                                                        postedJSON.put( "quantitypicked", "");
                                                        postedJSON.put( "quantitypacked", "");
                                                        postedJSON.put("requestdeliveryby", "");
                                                        postedJSON.put( "salesorderispricelocked", false);
                                                        postedJSON.put("salesrepid", "");
                                                        postedJSON.put( "shiptoaddressid", "00000000-0000-0000-0000-000000000000");
                                                        postedJSON.put( "tax", "0");
                                                        postedJSON.put( "taxbase", "0");
                                                        postedJSON.put("willcall", false);
                                                        postedJSON.put( "productimage", "");
                                                        postedJSON.put( "imageurl", "");
                                                        postedJSON.put( "uomname", "");
                                                        postedJSON.put( "upccode", "");
                                                        postedJSON.put( "ordered", "");
                                                        postedJSON.put("picked", "");
                                                        postedJSON.put("packed", "");
                                                        postedJSON.put("createdby", "");
                                                        postedJSON.put("createdon", "");
                                                        postedJSON.put( "modifiedby", user_id);
                                                        postedJSON.put("modifiedon", currentDate1);
                                                        postedJSON.put("isactive", true);
                                                        postedJSON.put( "orderid", "00000000-0000-0000-0000-000000000000");
                                                        postedJSON.put( "name", productname);
                                                        postedJSON.put("orderquantity",  quantity);
                                                        postedJSON.put( "warehouseid", warehouseid_o);///issue
                                                        postedJSON.put("isHigh", true);
                                                        if(istaxable.equals("true")){
                                                            postedJSON.put("itemistaxable", true);
                                                        }else {
                                                            postedJSON.put("itemistaxable", false);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    array.put(postedJSON);


                                                } while (Product_c.moveToNext());
                                            }


                                            //Order
                                            Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,Order_id);
                                            if (order_c != null && order_c.moveToFirst()) {
                                                do {
                                                    ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                                                    totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                    submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                                                    submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                                                    datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                                                    pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                    salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                    totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                    discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                    discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                    freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                    totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                    totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                    shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                    billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                    deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                    shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                    termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                    memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                    customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                    createdby = order_c.getString(order_c.getColumnIndex("createdby"));
                                                    creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));
                                                    warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                    customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                    draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                    lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                                                    shipmenttypeid = order_c.getString(order_c.getColumnIndex("shipmenttypeid"));

                                                    System.out.println("ordernumber---"+ordernumber);


                                                    JSONObject json = new JSONObject();
                                                    try {
                                                        json.put("id","00000000-0000-0000-0000-000000000000");
                                                        json.put("billtoaddressid",billtoaddressid);
                                                        json.put("customerid",customerid);
                                                        json.put("datefulfilled",datefulfilled);
                                                        json.put("discountamount",String.valueOf(discountamount));
                                                        json.put("discountpercentage",discountpercentage);
                                                        json.put("freightamount",freightamount);
                                                        json.put("ordernumber","0");
                                                        json.put("shiptoaddressid",shiptoaddressid);
                                                        json.put("submitdate",submitdate);
                                                        json.put("submitstatus","New Order");
                                                        json.put("totalamount",totalamount);
                                                        json.put("totallineitemamount",totallineitemamount);
                                                        json.put("totaltax",totaltax);
                                                        json.put("totaltaxbase",totaltaxbase);
                                                        json.put("createdby",createdby);
                                                        json.put("creratedon",currentDate1);
                                                        json.put("poreferencenum","0");
                                                        json.put("salesrepid",salesrepid);
                                                        json.put("pricingdate",currentDate1);
                                                        json.put("isactive", true);
                                                        json.put("deliverynote",deliverynote);
                                                        json.put("shipnote",shipnote);
                                                        json.put("termsconditions",termsconditions);
                                                        json.put("memo",memo);
                                                        json.put("quoteid","00000000-0000-0000-0000-000000000000");
                                                        json.put("warehouseid",warehouseid);
                                                        json.put("draftnumber",draftnumber);
                                                        json.put("lastsyncon",lastsyncon);
                                                        json.put("orderitem",array);
                                                        if(customeristaxable.equals("true")){
                                                            json.put("customeristaxable", true);
                                                        }else {
                                                            json.put("customeristaxable", false);
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    //OfflineList.setUploadStatus("1");
                                                    //mAdapter.notifyDataSetChanged();

                                                    System.out.println("Presale---"+json.toString());
                                                    // postCreateOrder(json.toString(),Order_id);

                                                    postPartRequest(json.toString(),Order_id,shipmenttypeid);
                                                } while (order_c.moveToNext());
                                            }

                                        }
                                    } finally {
                                        sqLiteController.close();
                                    }


                                }
                                else if(Order_name.equals("UpdateOrder")){

                                    System.out.println("Update");

                                   // OrderU_id = OfflineList.getOrderId();
                                    System.out.println("OrderU_id---"+OrderU_id);

                                    String ordernumber = "";
                                    String totalamount  = "";
                                    String submitdate  = "";
                                    String submitstatus  = "";
                                    String datefulfilled  = "";
                                    String pricingdate  = "";
                                    String salesrepid  = "";
                                    String totallineitemamount  = "";
                                    String discountpercentage  = "";
                                    String discountamount  = "";
                                    String freightamount  = "";
                                    String totaltaxbase  = "";
                                    String totaltax  = "";
                                    String shiptoaddressid  = "";
                                    String billtoaddressid  = "";
                                    String deliverynote  = "";
                                    String shipnote  = "";
                                    String termsconditions  = "";
                                    String memo  = "";
                                    String customerid  = "";
                                    String createdby  = "";
                                    String creratedon  = "";
                                    String warehouseid  = "";
                                    String customeristaxable = "";
                                    String draftnumber = "";
                                    String lastsyncon = "";
                                    String shipmenttypeid = "";

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        long count = sqLiteController.fetchCount();
                                        if (count > 0) {

                                            JSONObject postedJSON = new JSONObject();
                                            JSONArray array =new JSONArray();

                                            Cursor Product_c = sqLiteController.readProductJoinTables(OrderU_id);
                                            if (Product_c != null && Product_c.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                    @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                    @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                    @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                    @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                    @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                    @SuppressLint("Range") String warehouseid_o = Product_c.getString(Product_c.getColumnIndex("warehouseid"));

                                                    double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);

                                                    postedJSON = new JSONObject();
                                                    try {
                                                        postedJSON.put("id",productid);
                                                        postedJSON.put("isactive", true);
                                                        postedJSON.put("productid", productid);
                                                        postedJSON.put("name", productname);
                                                        postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                                                        postedJSON.put("quantityonhand", "0");
                                                        postedJSON.put("orderquantity", quantity);
                                                        postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                                                        postedJSON.put("priceperunit",  priceperunit);
                                                        postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                                                        postedJSON.put("delete", "null");
                                                        postedJSON.put("warehouseid", warehouseid_o);//warehouseid
                                                        postedJSON.put("modifiedby", user_id);
                                                        postedJSON.put("modifiedon", currentDate1);
                                                        postedJSON.put("isHigh", true);
                                                        postedJSON.put("quantity", quantity);//quantity
                                                        postedJSON.put("available", "0");
                                                        postedJSON.put("committed", "0");
                                                        if(istaxable.equals("true")){
                                                            postedJSON.put("itemistaxable", true);
                                                        }else {
                                                            postedJSON.put("itemistaxable", false);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    array.put(postedJSON);


                                                } while (Product_c.moveToNext());
                                            }


                                            //Order
                                            Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderU_id);
                                            if (order_c != null && order_c.moveToFirst()) {
                                                do {
                                                    ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                                                    totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                    submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                                                    submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                                                    datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                                                    pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                    salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                    totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                    discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                    discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                    freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                    totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                    totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                    shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                    billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                    deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                    shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                    termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                    memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                    customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                    createdby = order_c.getString(order_c.getColumnIndex("createdby"));
                                                    creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));
                                                    warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                    customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                    draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                    lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                                                    shipmenttypeid = order_c.getString(order_c.getColumnIndex("shipmenttypeid"));

                                                    System.out.println("ordernumber---"+ordernumber);


                                                    JSONObject json = new JSONObject();
                                                    try {
                                                        json.put("id",OrderU_id);
                                                        json.put("billtoaddressid",billtoaddressid);
                                                        json.put("customerid",customerid);
                                                        json.put("datefulfilled",datefulfilled);
                                                        json.put("discountamount",String.valueOf(discountamount));//discount vara amout
                                                        json.put("discountpercentage",discountpercentage);
                                                        json.put("freightamount",freightamount);
                                                        json.put("totaltaxbase",totaltaxbase);
                                                        json.put("ordernumber",ordernumber);// order number
                                                        json.put("shiptoaddressid",shiptoaddressid);//shipID
                                                        json.put("submitdate",submitdate);
                                                        json.put("submitstatus",submitstatus);
                                                        json.put("totalamount",totalamount); //totalpayyable
                                                        json.put("totallineitemamount",totallineitemamount);
                                                        json.put("totaltax",totaltax);
                                                        json.put("poreferencenum","0");
                                                        json.put("salesrepid",salesrepid);
                                                        json.put("pricingdate",currentDate1);
                                                        json.put("isactive", true);//boolean true
                                                        json.put("deliverynote",deliverynote);
                                                        json.put("shipnote",shipnote);
                                                        json.put("termsconditions",termsconditions);
                                                        json.put("memo",memo);
                                                        json.put("warehouseid",warehouseid);
                                                        json.put("deletedRows","");
                                                        json.put("modifiedon",currentDate1);
                                                        json.put("draftnumber",draftnumber);
                                                        json.put("lastsyncon",lastsyncon);
                                                        json.put("orderitem",array);
                                                        if(customeristaxable.equals("true")){
                                                            json.put("customeristaxable", true);
                                                        }else {
                                                            json.put("customeristaxable", false);
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    //OfflineList.setUploadStatus("1");
                                                    //mAdapter.notifyDataSetChanged();

                                                    System.out.println("PresaleUpdate---"+json.toString());
                                                    // postCreateOrder(json.toString(),Order_id);

                                                    System.out.println("OrderU_id----"+OrderU_id);
                                                    System.out.println("draftnumber----"+draftnumber);

                                                    postUpdatePresale(json.toString(),OrderU_id,shipmenttypeid,draftnumber);
                                                } while (order_c.moveToNext());
                                            }

                                        }
                                    } finally {
                                        sqLiteController.close();
                                    }


                                }

                            }

                        }
                    }else {

                       System.out.println("Invoice--"+"Invoice");

                        String InvoiceS_id = "";
                        for (int i = 0; i < OfList.size(); i++) {
                            Model_OfflineList OfflineList = OfList.get(i);

                            if (OfflineList.isSelected()) {

                                syncBtn.setBackgroundResource(R.drawable.shape_btngraey);
                                syncBtn.setText("Loading..");
                                syncBtn.setFocusable(false);
                                syncBtn.setClickable(false);
                                syncBtn.setFocusableInTouchMode(false);

                                InvoiceS_id = OfflineList.getOrderId();

                                System.out.println("InvoiceS_id---"+InvoiceS_id);

                                String orderid = "";
                                String paymenttypeid = "";
                                String paymentmethod = "";
                                String billtoaddressid = "";
                                String customerid = "";
                                String discountamount = "";
                                String timestamp = "";
                                String discountpercentage = "";
                                String freightamount = "";
                                String shiptoaddressid = "";
                                String invoicedate = "";
                                String submitstatus = "";
                                String totalamount = "";
                                String totallineitemamount = "";
                                String totaltax = "";
                                String totaltaxbase = "";
                                String salesrepid = "";
                                String pricingdate = "";
                                String deliverynote = "";
                                String shipnote = "";
                                String termsconditions = "";
                                String memo = "";
                                String warehouseid = "";
                                String customeristaxable = "";
                                String draftnumber = "";
                                String lastsyncon = "";


                                SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                sqLiteController.open();
                                try {
                                    long count = sqLiteController.fetchCount();
                                    if (count > 0) {

                                        JSONObject postedJSON = new JSONObject();
                                        JSONArray array =new JSONArray();

                                        Cursor Product_c = sqLiteController.readInvoiceProductJoinTables(InvoiceS_id);
                                        if (Product_c != null && Product_c.moveToFirst()) {
                                            do {
                                                @SuppressLint("Range") String id = Product_c.getString(Product_c.getColumnIndex("id"));
                                                @SuppressLint("Range") String Orderproductid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                @SuppressLint("Range") String itemistaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                                                @SuppressLint("Range") String itemorderid = Product_c.getString(Product_c.getColumnIndex("itemorderid"));
                                                @SuppressLint("Range") String itemwarehouseid = Product_c.getString(Product_c.getColumnIndex("itemwarehouseid"));
                                                @SuppressLint("Range") String orderpid = Product_c.getString(Product_c.getColumnIndex("orderpid"));


                                                double TotalBaseAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);


                                                postedJSON = new JSONObject();
                                                try {
                                                    postedJSON.put("id",id);
                                                    postedJSON.put("isactive", true);

                                                    if(itemorderid.equals("0")){
                                                    }else {
                                                        postedJSON.put("orderid",itemorderid);
                                                    }

                                                    if(itemorderid.equals("0")){
                                                    }else {
                                                        postedJSON.put("orderpid", orderpid);
                                                    }

                                                    postedJSON.put("productid", Orderproductid);
                                                    postedJSON.put("name", productname);
                                                    postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                                                    postedJSON.put("quantityonhand", "0");
                                                    postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                                                    postedJSON.put("priceperunit",  priceperunit);
                                                    postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                                                    postedJSON.put("delete", "null");
                                                    postedJSON.put("warehouseid", itemwarehouseid);//warehouseid
                                                    postedJSON.put("modifiedby", user_id);
                                                    postedJSON.put("modifiedon", currentDate1);
                                                    postedJSON.put("isHigh", true);
                                                    postedJSON.put("quantity", quantity);//quantity
                                                    postedJSON.put("available", "0");
                                                    postedJSON.put("committed", "0");
                                                    if(itemistaxable.equals("true")){
                                                        postedJSON.put("itemistaxable", true);
                                                    }else {
                                                        postedJSON.put("itemistaxable", false);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                array.put(postedJSON);


                                            } while (Product_c.moveToNext());
                                        }


                                        //Order
                                        Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,InvoiceS_id);
                                        if (order_c != null && order_c.moveToFirst()) {
                                            do {
                                                orderid = order_c.getString(order_c.getColumnIndex("orderid"));
                                                paymenttypeid = order_c.getString(order_c.getColumnIndex("paymenttypeid"));
                                                paymentmethod = order_c.getString(order_c.getColumnIndex("paymentmethod"));
                                                billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                                                customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                                                discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                                                timestamp = order_c.getString(order_c.getColumnIndex("timestamp"));
                                                discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                                                freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                                                shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                                                invoicedate = order_c.getString(order_c.getColumnIndex("invoicedate"));
                                                submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                                                totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                                                totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                                                totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                                                totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                                                salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                                                pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                                                deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                                                shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                                                termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                                                memo = order_c.getString(order_c.getColumnIndex("memo"));
                                                warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                                                customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                                                draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                                                lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));

                                                // System.out.println("ordernumber---"+ordernumber);


                                                JSONObject json = new JSONObject();
                                                try {
                                                    if(orderid.equals("0")){
                                                        json.put("orderid","00000000-0000-0000-0000-000000000000");
                                                    }else {
                                                        json.put("orderid",orderid);
                                                    }
                                                    json.put("paymenttypeid",paymenttypeid);
                                                    json.put("paymentid","00000000-0000-0000-0000-000000000000");
                                                    json.put("paymentmethod",paymentmethod);
                                                    json.put("name","");
                                                    json.put("billtoaddressid",billtoaddressid);
                                                    json.put("customerid",customerid);
                                                    json.put( "vendorid","00000000-0000-0000-0000-000000000000");
                                                    json.put("datefulfilled","00000000-0000-0000-0000-000000000000");
                                                    json.put("description","");
                                                    json.put( "discountamount",String.valueOf(discountamount));
                                                    json.put("timestamp",timestamp);
                                                    json.put("transactioncurrencyid","00000000-0000-0000-0000-000000000000");
                                                    json.put("exchangerate","0");
                                                    json.put( "discountamountbase","0");
                                                    json.put("discountpercentage",discountpercentage);
                                                    json.put("freightamount",freightamount);
                                                    json.put("freightamountbase","0");
                                                    json.put( "freighttermscode","0");
                                                    json.put( "ispricelocked",true);
                                                    json.put( "lastbackofficesubmit",timestamp);
                                                    json.put( "ordernumber","0");
                                                    json.put("quoteid","00000000-0000-0000-0000-000000000000");
                                                    json.put( "requestdeliveryby","00000000-0000-0000-0000-000000000000");
                                                    json.put( "shiptoaddressid",shiptoaddressid);
                                                    json.put("invoicedate",invoicedate);
                                                    json.put("submitstatus",submitstatus);
                                                    json.put("submitstatusdescription","");
                                                    json.put("totalamount",String.valueOf(totalamount));
                                                    json.put("totalamountbase","0");
                                                    json.put("totalamountlessfreight","0");
                                                    json.put("totalamountlessfreightbase","0");
                                                    json.put("totaldiscountamount","0");
                                                    json.put("totaldiscountamountbase","0");
                                                    json.put("totallineitemamount",String.valueOf(totallineitemamount));
                                                    json.put( "totallineitemamountbase","0");
                                                    json.put("totallineitemdiscountamount","0");
                                                    json.put( "totallineitemdiscountamountbase","0");
                                                    json.put("totaltax",String.valueOf(totaltax));
                                                    json.put("totaltaxbase",totaltaxbase);
                                                    json.put("willcall",true);
                                                    json.put("salesrepid",salesrepid);
                                                    json.put("pricingdate",pricingdate);
                                                    json.put( "deliverynote",deliverynote);
                                                    json.put("shipnote",shipnote);
                                                    json.put("termsconditions",termsconditions);
                                                    json.put("memo",memo);
                                                    json.put("referenceorder","0");
                                                    json.put("createdby",user_id);
                                                    json.put("createdon",currentDate1);
                                                    json.put("modifiedby",user_id);
                                                    json.put("modifiedon",currentDate1);
                                                    json.put("isactive",true);
                                                    json.put("isPresale",true);
                                                    json.put("deletedRows","");
                                                    json.put("warehouseid",warehouseid);
                                                    json.put("draftnumber",draftnumber);
                                                    json.put("lastsyncon",lastsyncon);
                                                    json.put("invoiceproduct",array);
                                                    if(customeristaxable.equals("true")){
                                                        json.put("customeristaxable", true);
                                                    }else {
                                                        json.put("customeristaxable", false);
                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                //OfflineList.setUploadStatus("1");
                                                //mAdapter.notifyDataSetChanged();

                                                System.out.println("JSONInvoice---"+json.toString());

                                                postInvoicePartRequest(json.toString(),InvoiceS_id,orderid,totalamount);
                                            } while (order_c.moveToNext());
                                        }

                                    }
                                } finally {
                                    sqLiteController.close();
                                }

                            }

                        }
                    }




                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

            }
        });*/


        final androidx.appcompat.widget.SearchView.OnQueryTextListener queryTextListener = new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something
                //SEARCH FILTER
                filteredModelList = filter(OfList, newText.toString());
                mAdapter.setFilter(filteredModelList);
                if(filteredModelList.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    txt_no_record.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    txt_no_record.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                Utils.getInstance().hideKeyboard(Activity_OfflineSync.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);



        arStatusName.add("Presale");
        arStatusName.add("Invoice");

        arStatusID.add("0");
        arStatusID.add("1");

        ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_OfflineSync.this, R.layout.spinner_item,arStatusName);
        adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        OfStatus_sp.setAdapter(adapterBType);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(Flag.equals("0")){

                }else if(Flag.equals("1")){
                    OfStatus_sp.setSelection(arStatusName.indexOf("Presale"));
                }else if(Flag.equals("2")){
                    OfStatus_sp.setSelection(arStatusName.indexOf("Invoice"));
                }

                //eta_spinner_state.setSelection(aState.indexOf(str_state));
                //sp_businessCategory.setSelection(aStateId.indexOf("1"));

            }
        });

        OfStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                Status = arStatusName.get(position);
                Progress_dialog.show();
                System.out.println("Status---"+Status);

                if(arStatusName.get(position).equals("Presale")){
                    text_m.setText("Draft Order#");
                    ViewList();
                }else {
                    //Utils.getInstance().loadingDialog(Activity_OfflineSync.this, "Please wait.");
                    text_m.setText("Draft Invoice#");
                    ViewInvoiceList();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        ViewList();
    }

    private ArrayList<Model_OfflineList> filter(ArrayList<Model_OfflineList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_OfflineList> filteredModelList = new ArrayList<>();

        for (Model_OfflineList model : models) {

            final String getOrder_name = model.getOrderId().toLowerCase();


            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }


    private void ViewList() {
        SQLiteController sqLiteController = new SQLiteController(this);
       // Utils.getInstance().loadingDialog(Activity_OfflineSync.this, "Please wait.");
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                if (OfList.size() > 0){
                    OfList.clear();
                }

                Date date1 = new Date();
                SimpleDateFormat sdf1= new SimpleDateFormat("MM/dd/yyyy");
                //sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDate1 = sdf1.format(date1);

                //Order
                Cursor order_c = sqLiteController.readJointTableOrder();
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String order_name = order_c.getString(order_c.getColumnIndex("order_name"));
                        @SuppressLint("Range") String totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                        @SuppressLint("Range") String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                        @SuppressLint("Range") String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                        @SuppressLint("Range") String id = order_c.getString(order_c.getColumnIndex("order_id"));
                        @SuppressLint("Range") String customername = order_c.getString(order_c.getColumnIndex("customername"));
                        @SuppressLint("Range") String netStatus = order_c.getString(order_c.getColumnIndex("netStatus"));
                        @SuppressLint("Range") String SyncStatus = order_c.getString(order_c.getColumnIndex("SyncStatus"));
                        @SuppressLint("Range") String lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                        @SuppressLint("Range") String draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                        @SuppressLint("Range") String creratedon = order_c.getString(order_c.getColumnIndex("creratedon"));
                        String date_order = "";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = null;

                        try {
                            date = sdf.parse(submitdate);
                            date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date_order.equals(currentDate1)){

                            if(netStatus.equals("Offline")){

                                if(SyncStatus.equals("non_Sync")){
                                    OfList.add(new Model_OfflineList(false,id,ordernumber,lastsyncon,"0",draftnumber,"Presale",order_name));

                                }else {
                                    OfList.add(new Model_OfflineList(false,id,ordernumber,lastsyncon,"1",draftnumber,"Presale",order_name));

                                }


                            }

                        }



                    } while (order_c.moveToNext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            syncBtn.setFocusable(true);
                            syncBtn.setClickable(true);
                            syncBtn.setFocusableInTouchMode(true);

                          /*  if (Progress_dialog != null) {
                                if (Progress_dialog.isShowing()) {
                                    Progress_dialog.dismiss();
                                }
                            }*/

                            if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                                    Connectivity.isConnectedFast(Activity_OfflineSync.this)) {
                                syncBtn.setBackgroundResource(R.drawable.shape_btngreen_bg);
                                syncBtn.setText("Sync");
                            }else {
                                syncBtn.setBackgroundResource(R.drawable.shape_redbg);
                                syncBtn.setText("Offline");

                            }

                            if (OfList.size() > 0){
                                txt_no_record.setVisibility(View.GONE);
                            }else {
                                txt_no_record.setVisibility(View.VISIBLE);
                            }
                            // Collections.reverse(list);
                          //  Utils.getInstance().dismissDialog();

                        }
                    });

                }

            }

        } finally {
            sqLiteController.close();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (Progress_dialog != null) {
                        if (Progress_dialog.isShowing()) {
                            Progress_dialog.dismiss();
                        }
                    }
                    //  Utils.getInstance().dismissDialog();
                }
            }, 6000);

        }

    }
    private void ViewInvoiceList() {
       // Progress_dialog.show();
        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                if (OfList.size() > 0){
                    OfList.clear();
                }
                Date date1 = new Date();
                SimpleDateFormat sdf1= new SimpleDateFormat("MM/dd/yyyy");
                //sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDate1 = sdf1.format(date1);
                //Invoice
                Cursor order_c = sqLiteController.readJointTableInvoice();
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String id = order_c.getString(order_c.getColumnIndex("id"));
                        @SuppressLint("Range") String netStatus = order_c.getString(order_c.getColumnIndex("netStatus"));
                        @SuppressLint("Range") String SyncStatus = order_c.getString(order_c.getColumnIndex("SyncStatus"));
                        @SuppressLint("Range") String lastsyncon = order_c.getString(order_c.getColumnIndex("lastsyncon"));
                        @SuppressLint("Range") String draftnumber = order_c.getString(order_c.getColumnIndex("draftnumber"));
                        @SuppressLint("Range") String createdon = order_c.getString(order_c.getColumnIndex("createdon"));

                        String date_order = "";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = null;

                        try {
                            date = sdf.parse(createdon);
                            date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                        if(date_order.equals(currentDate1)) {

                            if(netStatus.equals("Offline")){

                                if(SyncStatus.equals("non_Sync")){
                                    OfList.add(new Model_OfflineList(false,id,ordernumber,lastsyncon,"0",draftnumber,"Invoice",""));

                                }else {
                                    OfList.add(new Model_OfflineList(false,id,ordernumber,lastsyncon,"1",draftnumber,"Invoice",""));

                                }


                            }
                        }



                    } while (order_c.moveToNext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            syncBtn.setFocusable(true);
                            syncBtn.setClickable(true);
                            syncBtn.setFocusableInTouchMode(true);

                          /*  if (Progress_dialog != null) {
                                if (Progress_dialog.isShowing()) {
                                    Progress_dialog.dismiss();
                                }
                            }*/
                            if (Connectivity.isConnected(Activity_OfflineSync.this) &&
                                    Connectivity.isConnectedFast(Activity_OfflineSync.this)) {
                                syncBtn.setBackgroundResource(R.drawable.shape_btngreen_bg);
                                syncBtn.setText("Sync");
                            }else {
                                syncBtn.setBackgroundResource(R.drawable.shape_redbg);
                                syncBtn.setText("Offline");

                            }

                            if (OfList.size() > 0){
                                txt_no_record.setVisibility(View.GONE);
                            }else {
                                txt_no_record.setVisibility(View.VISIBLE);
                            }
                            // Collections.reverse(list);
                            //Utils.getInstance().dismissDialog();

                        }
                    });

                }

            }

        } finally {
            sqLiteController.close();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (Progress_dialog != null) {
                        if (Progress_dialog.isShowing()) {
                            Progress_dialog.dismiss();
                        }
                    }
                  //  Utils.getInstance().dismissDialog();
                }
            }, 6000);

        }

    }

    private void postPartRequest(String json,String Order_Draft_id,String shipmenttypeid,String agentID) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.hostName+"order/orders/savepresale")
                            .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("CreateOrder----"+jsonObject.toString());
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            String data = jsonObject.getString("data");
                            String messages = jsonObject.getString("messages");
                            //Toast.makeText(Activity_OfflineSync.this, messages, Toast.LENGTH_SHORT).show();
                            if ( succeeded == true) {

                                JSONObject json = new JSONObject();
                                try {
                                    if (Activity_OrdersHistory.WarehouseFlag.equals("false")) {
                                        //Hide
                                        if(shipmenttypeid.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                        }else {
                                            json.put("shipmenttypeid",null);
                                        }
                                    }else {
                                        json.put("shipmenttypeid","87b9ad6e-fda7-4c6d-87e6-793c6cd0a964");
                                        if(shipmenttypeid.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                            json.put("deliverytype","fulfillmentwarehouse");
                                        }else if(shipmenttypeid.equals("474bece4-7bc2-43a0-a7c4-e74b1de134f1")){
                                            json.put("deliverytype","customerpickup");
                                        }
                                    }
                                    if(shipmenttypeid.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                        json.put("deliveryshipmenttypeid",null);
                                    }else {
                                        json.put("deliveryshipmenttypeid",shipmenttypeid);
                                    }
                                    json.put("isInsert",true);
                                    json.put("isactive",true);
                                    json.put("fulfillmentstatus","New Order");
                                    json.put("requestdeliveryby",user_id);
                                    json.put("attempt","1");
                                    json.put("creratedon",currentDate1);
                                    json.put("orderid",data);

                                    if(shipmenttypeid.equals("28d68737-5d45-47f4-907c-4f66e6b01600")){
                                        json.put("deliveryagentid",agentID);
                                    }

                                    System.out.println("Fullpost----"+json.toString());

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        sqLiteController.UpdateSyncStatus(Order_Draft_id,"Sync");
                                    } finally {
                                        sqLiteController.close();
                                    }


                                    postPartCreateFull(json.toString(),data,Order_Draft_id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                              /*  JSONObject json = new JSONObject();
                                try {
                                    json.put("shipmenttypeid",shipmenttypeid);
                                    json.put("isInsert",true);
                                    json.put("isactive",true);
                                    json.put("fulfillmentstatus","New Order");
                                    json.put("orderid",data);

                                    System.out.println("Fullpost----"+json.toString());

                                    // postCreateFull(json.toString());

                                    postPartCreateFull(json.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/


                            }

                        } catch (JSONException | IOException e) {
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
                        Utils.getInstance().dismissDialog();
                    }
                });

            }

        };
        task.execute((Void[])null);
    }
    private void postUpdatePresale(String json,String Order_id,String shipmenttypeid,String draftnumber) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.hostName+"order/orders/updatepresale")
                            .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("CreateOrder----"+jsonObject.toString());
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            String data = jsonObject.getString("data");
                            String messages = jsonObject.getString("messages");
                            //Toast.makeText(Activity_OfflineSync.this, messages, Toast.LENGTH_SHORT).show();
                            if ( succeeded == true) {

                               /* JSONObject json = new JSONObject();
                                try {
                                    json.put("shipmenttypeid",shipmenttypeid);
                                    json.put("isInsert",true);
                                    json.put("isactive",true);
                                    json.put("fulfillmentstatus","New Order");
                                    json.put("orderid",data);

                                    System.out.println("Fullpost----"+json.toString());

                                    postPartCreateFull(json.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/

                                SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                sqLiteController.open();
                                try {
                                    sqLiteController.UpdateSyncStatus(Order_id,"Sync");
                                } finally {
                                    sqLiteController.close();
                                }

                                GetOrderUpdate(data,draftnumber);

                               // Get_OrderUpdateList(data,draftnumber);
                            }

                        } catch (JSONException | IOException e) {
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
                        Utils.getInstance().dismissDialog();
                    }
                });

            }

        };
        task.execute((Void[])null);
    }
    private void postInvoicePartRequest(String json,String Order_Draft_id,String orderid_,String totalamount) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.hostName+"order/invoice")
                            .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("SuccessCreateIn---"+jsonObject);
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            String Inv_data = jsonObject.getString("data");
                            if ( succeeded == true) {

                                JSONObject json = new JSONObject();
                                try {
                                    if(orderid_.equals("0")){
                                        json.put("id","00000000-0000-0000-0000-000000000000");
                                    }else {
                                        json.put("id",orderid_);
                                    }

                                    //json.put("fulfillmentstatus","Payment pending");
                                    json.put("invoiceid",Inv_data);
                                    json.put("invoicerid",user_id);
                                    json.put("modifiedon",currentDate1);
                                    json.put("modifiedby",user_id);

                                    System.out.println("InvoiceFulnvoice----"+json.toString());

                                    postPartCreateInvoiceFull(json.toString(),Inv_data,Order_Draft_id,orderid_,totalamount);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                SQLiteController sqLiteController1 = new SQLiteController(Activity_OfflineSync.this);
                                sqLiteController1.open();
                                try {
                                    sqLiteController1.UpdateSyncStatus(Order_Draft_id,"Sync");
                                } finally {
                                    sqLiteController1.close();

                                }

                            }

                        } catch (JSONException | IOException e) {
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
                       // Utils.getInstance().dismissDialog();
                    }
                });

            }

        };
        task.execute((Void[])null);
    }
    private void postPartCreateFull(String json,String data,String Order_Draft_id) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.hostName+"order/presale/summaryfulfillment")
                            .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("Fullfillment---"+jsonObject);
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            if ( succeeded == true) {

                                //New API Order Insert
                                GetOrderInsert(data,Order_Draft_id);
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

    private void GetOrderInsert(String orderID, String Order_Draft_id ) {
        try {
            App.getInstance().GetCreateOrderLogin(OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                        sqLiteController.open();
                        sqLiteController.deleteProductItems(Order_Draft_id);
                        sqLiteController.deleteOrder(Order_Draft_id);
                        try {
                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                            if (fetchOrderCount > 0) {
                            }
                        } finally {
                            sqLiteController.close();
                        }
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");

                            for (int i = 0; i < Products_assets.length(); i++) {
                                JSONObject js = Products_assets.getJSONObject(i);
                                String id = js.getString("id");
                                String assetname = js.getString("assetname");
                                String productid = js.getString("productid");
                                String imageurl = "";
                                //String imageurl = js.getString("imageurl");
                                String filepath = js.getString("filepath");
                                String assettype = js.getString("assettype");
                                String isdefault = js.getString("isdefault");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorAssertTable = sqLiteController.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_ID,id);
                                            if (CursorAssertTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Products_assets_id = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_id"));
                                                    sqLiteController.deleteProductAssertIDItems(Products_assets_id);
                                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                                } while (CursorAssertTable.moveToNext());
                                            }
                                            if(CursorAssertTable.getCount() == 0){
                                                sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < Products.length(); i++) {
                                JSONObject js = Products.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("name");
                                String description = js.getString("description");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");

                                String finalPrice = price;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String all_product_id = CAllOrders.getString(CAllOrders.getColumnIndex("all_product_id"));

                                                        sqLiteController.deleteAllProductIDItems(all_product_id);

                                                        sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);



                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            String customername ="";
                            for (int i = 0; i < Customers.length(); i++) {
                                JSONObject jsC = Customers.getJSONObject(i);
                                String id = jsC.getString("id");
                                String customertypeid = jsC.getString("customertypeid");
                                customername = jsC.getString("businessname").equals("null") ?  jsC.getString("customername") : jsC.getString("businessname");
                                String billingaddressid = jsC.getString("billingaddressid");
                                String shippingaddressid = jsC.getString("shippingaddressid");
                                String deliveryaddressid = jsC.getString("deliveryaddressid");
                                String customernumber = jsC.getString("customernumber");
                                String industry = jsC.getString("industry");
                                String createdby = jsC.getString("createdby");
                                String createdon = jsC.getString("createdon");
                                String status = jsC.getString("status");
                                String modifiedby = jsC.getString("modifiedby");
                                String modifiedon = jsC.getString("modifiedon");
                                String externalid = jsC.getString("externalid");
                                String externalupdated = jsC.getString("externalupdated");
                                String isactive = jsC.getString("isactive");
                                String istaxable = jsC.getString("istaxable");
                                String taxid = jsC.getString("taxid");
                                String netterms = jsC.getString("netterms");

                                String finalCustomername = customername;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchCustomerCount = sqLiteController.fetchCustomerCount();
                                            if (fetchCustomerCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_CUSTOMER,DbHandler.CUSTOMER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String Id = CAllOrders.getString(CAllOrders.getColumnIndex("Id"));

                                                        sqLiteController.deleteCustomerItems(Id);

                                                        sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                                billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < shipments.length(); i++) {
                                JSONObject js = shipments.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String shipmenttypeid = js.getString("shipmenttypeid");
                                String warehouseid = js.getString("warehouseid");
                                String fulfillmentstatus = js.getString("fulfillmentstatus");
                                String routenum = js.getString("routenum");
                                String routedate = js.getString("routedate");
                                String truck = js.getString("truck");
                                String driverid = js.getString( "driverid");
                                String carrierid = js.getString("carrierid");
                                String shippeddate = js.getString("shippeddate");
                                String trackingnumber = js.getString( "trackingnumber");
                                String deliverydate = js.getString( "deliverydate");
                                String deliverytime = js.getString( "deliverytime");
                                String pickerid = js.getString("pickerid");
                                String packerid = js.getString( "packerid");
                                String shipperid = js.getString("shipperid");
                                String invoicerid = js.getString( "invoicerid");
                                String paymenterid = js.getString("paymenterid");
                                String uuid = js.getString( "uuid");
                                String isdisabled = js.getString( "isdisabled");
                                String disabledby = js.getString( "disabledby");
                                String disabledbehalfof = js.getString("disabledbehalfof");
                                String isdeleted = js.getString("isdeleted");
                                String deletedby = js.getString( "deletedby");
                                String deletedbehalfof = js.getString( "deletedbehalfof");
                                String deletedon = js.getString("deletedon");
                                String createdby = js.getString("createdby");
                                String createdbehalfof = js.getString("createdbehalfof");
                                String creratedon = js.getString("creratedon");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String datafromtypeid = js.getString("datafromtypeid");
                                String datafromid = js.getString("datafromid");
                                String tenantid = js.getString("tenantid");
                                String deliveryagentid = js.getString("deliveryagentid");
                                String deliveryshipmenttypeid = js.getString("deliveryshipmenttypeid");
                                String routedayid = js.getString("routedayid");
                                String routeid = js.getString("routeid");
                                String truckid = js.getString("truckid");
                                String invoiceid = js.getString("invoiceid");
                                String workorderid = js.getString("workorderid");
                                String purchaseorderid = js.getString("purchaseorderid");
                                String undeliveredreasonid = js.getString("undeliveredreasonid");
                                String undeliveredreason = js.getString("undeliveredreason");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String shipment_idd = CAllOrders.getString(CAllOrders.getColumnIndex("shipment_id"));
                                                        //@SuppressLint("Range") String ordernumberS = CAllOrders.getString(CAllOrders.getColumnIndex("ordernumber"));

                                                        sqLiteController.deleteShipItems(shipment_idd);

                                                        //Products Insert Method
                                                        sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    //Products Insert Method
                                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < address.length(); i++) {
                                JSONObject js = address.getJSONObject(i);
                                String id = js.getString("id");
                                String line1 = js.getString("line1");
                                String line2 = js.getString("line2");
                                String line3 = js.getString("line3");
                                String stateorprovince = js.getString("stateorprovince");
                                String addresstypecode = js.getString("addresstypecode");
                                String country = js.getString("country");
                                String city = js.getString("city");
                                String postalcode = js.getString("postalcode");
                                String postofficebox = js.getString("postofficebox");
                                String name = js.getString("name");
                                String isprimaryaddress = js.getString("isprimaryaddress");
                                String isactive = js.getString("isactive");
                                String domainEvents = js.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ADDRESS,DbHandler.ADDRESS_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String address_id = CAllOrders.getString(CAllOrders.getColumnIndex("address_id"));

                                                        sqLiteController.deleteAddressItems(address_id);

                                                        sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < Orders.length(); i++) {
                                JSONObject js = Orders.getJSONObject(i);
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String customeridtype = js.getString("customeridtype");
                                String datefulfilled = js.getString("datefulfilled");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String poreferencenum = js.getString("poreferencenum");
                                String ispricelocked = js.getString("ispricelocked");
                                String quoteid = js.getString("quoteid");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String submitdate = js.getString("submitdate");
                                String submitstatus = js.getString("submitstatus");
                                String totalamount = js.getString("totalamount");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String createdby = js.getString("createdby");
                                String creratedon = js.getString("creratedon");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String cancellationreason = js.getString("cancellationreason");
                                String comments = js.getString("comments");
                                String lastbackofficesubmit = js.getString("lastbackofficesubmit");
                                String opportunityid = js.getString("opportunityid");
                                String ordernumber = js.getString("ordernumber");
                                String cancellationDate = js.getString("cancellationDate");
                                String confirmedby = js.getString("confirmedby");
                                String confirmeddate = js.getString("confirmeddate");
                                String notes = js.getString("notes");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String warehouseid = js.getString("warehouseid");
                                String modifiedon = js.getString("modifiedon");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");
                                String customeristaxable = js.getString("customeristaxable");

                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String order_id = CAllOrders.getString(CAllOrders.getColumnIndex("order_id"));

                                                        sqLiteController.deleteOrder(order_id);

                                                        if (draftnumber.equals("null")) {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                        }else {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                        }


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    if (draftnumber.equals("null")) {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                    }else {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                    }
                                                }

                                                sqLiteController.deleteProductItems(id);
                                            }
                                        } finally {
                                            sqLiteController.close();


                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < OrderProducts.length(); i++) {
                                JSONObject js = OrderProducts.getJSONObject(i);
                                String productname = js.getString("productname");
                                String quantity = js.getString("quantity");
                                String baseamount = js.getString("baseamount");
                                String priceperunit = js.getString("priceperunit");
                                String productid = js.getString("productid");
                                String orderid = js.getString("orderid");
                                String pid = js.getString("id");
                                String itemistaxable = js.getString("itemistaxable");
                                String warehouseid = js.getString("warehouseid");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRO_ID,pid);

                                                if(CAllOrders.getCount() == 0){
                                                    //Products Insert Method
                                                    sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();

                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject js = invoice.getJSONObject(i);

                                String orderid = js.getString("orderid");
                                String externalid = js.getString("externalid");
                                String externalstatus = js.getString("externalstatus");
                                String paymenttypeid = js.getString("paymenttypeid");
                                String paymentid = js.getString("paymentid");
                                String paymentmethod = js.getString("paymentmethod");
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String vendorid = js.getString("vendorid");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String ispricelocked = js.getString("ispricelocked");
                                String ordernumber = js.getString("ordernumber");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String invoicedate = js.getString("invoicedate");
                                String submitstatus = js.getString("submitstatus");
                                String submitstatusdescription = js.getString("submitstatusdescription");
                                String totalamount = js.getString("totalamount");
                                String totalamountbase = js.getString("totalamountbase");
                                String totalamountlessfreight = js.getString("totalamountlessfreight");
                                String totalamountlessfreightbase = js.getString("totalamountlessfreightbase");
                                String totaldiscountamount = js.getString("totaldiscountamount");
                                String totaldiscountamountbase = js.getString("totaldiscountamountbase");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totallineitemamountbase = js.getString("totallineitemamountbase");
                                String totallineitemdiscountamount = js.getString("totallineitemdiscountamount");
                                String totallineitemdiscountamountbase = js.getString("totallineitemdiscountamountbase");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String willcall = js.getString("willcall");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String referenceorder = js.getString("referenceorder");
                                String createdon = js.getString("createdon");
                                String createdby = js.getString("createdby");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String warehouseid = js.getString("warehouseid");
                                String customeristaxable = js.getString("customeristaxable");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");


                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorInvoiceTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,id);
                                            if (CursorInvoiceTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = CursorInvoiceTable.getString(CursorInvoiceTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoice(id);
                                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                    } else {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                    }
                                                } while (CursorInvoiceTable.moveToNext());
                                            }
                                            if(CursorInvoiceTable.getCount() == 0){
                                                if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                } else {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                }
                                            }
                                            sqLiteController.deleteInvoiceProductsItems(id);
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoiceproduct.length(); i++) {
                                JSONObject js_P = invoiceproduct.getJSONObject(i);
                                String invoiceid_IP = js_P.getString("invoiceid");
                                String transactioncurrencyid_IP = js_P.getString("transactioncurrencyid");
                                String uomid = js_P.getString("uomid");
                                String baseamount = js_P.getString("baseamount");
                                String exchangerate_Ip = js_P.getString("exchangerate");
                                String baseamountbase = js_P.getString("baseamountbase");
                                String description_Ip = js_P.getString("description");
                                String extendedamount = js_P.getString("extendedamount");
                                String extendedamountbase = js_P.getString("extendedamountbase");
                                String iscopied = js_P.getString("iscopied");
                                String ispriceoverridden = js_P.getString("ispriceoverridden");
                                String isproductoverridden = js_P.getString("isproductoverridden");
                                String lineitemnumber = js_P.getString("lineitemnumber");
                                String manualdiscountamount = js_P.getString("manualdiscountamount");
                                String manualdiscountamountbase = js_P.getString("manualdiscountamountbase");
                                String priceperunit = js_P.getString("priceperunit");
                                String priceperunitbase = js_P.getString("priceperunitbase");
                                String productdescription = js_P.getString("productdescription");
                                String productname = js_P.getString("productname");
                                String productid = js_P.getString("productid");
                                String quantity = js_P.getString("quantity");
                                String quantitybackordered = js_P.getString("quantitybackordered");
                                String quantitycancelled = js_P.getString("quantitycancelled");
                                String quantityshipped = js_P.getString("quantityshipped");
                                String quantitypicked = js_P.getString("quantitypicked");
                                String quantitypacked = js_P.getString("quantitypacked");
                                String requestdeliverybyip = js_P.getString("requestdeliveryby");
                                String salesorderispricelocked = js_P.getString("salesorderispricelocked");
                                String shiptoaddressidip = js_P.getString("shiptoaddressid");
                                String tax = js_P.getString("tax");
                                String taxbase = js_P.getString("taxbase");
                                String willcallip = js_P.getString("willcall");
                                String createdonip = js_P.getString("createdon");
                                String createdbyip = js_P.getString("createdby");
                                String modifiedbyip = js_P.getString("modifiedby");
                                String modifiedbehalfofip = js_P.getString("modifiedbehalfof");
                                String modifiedonip = js_P.getString("modifiedon");
                                String orderpid = js_P.getString("orderpid");
                                String itemistaxable = js_P.getString("itemistaxable");
                                String idip = js_P.getString("id");
                                String isactiveip = js_P.getString("isactive");
                                String domainEvents = js_P.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor InvoiceProductTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE_PRODUCTS,DbHandler.IN_PRODUCTS_ID,idip);
                                            /*if (InvoiceProductTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = InvoiceProductTable.getString(InvoiceProductTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoiceProductsALL(id);
                                                    sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                                } while (InvoiceProductTable.moveToNext());
                                            }*/
                                            if(InvoiceProductTable.getCount() == 0){
                                                sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Date date1 = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String currentDateTime = sdf.format(date1);

                                    System.out.println("currentDateTime-----"+currentDateTime);

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                        for (String s: elements) {
                                            System.out.println("systemOrders-----------"+s);
                                            sqLiteController.UpdateMasterSync(s,currentDateTime);

                                        }

                                    } finally {
                                        sqLiteController.close();
                                    }


                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }

                                 /*   Intent intent = new Intent(Activity_OfflineSync.this, Activity_OfflineSync.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);*/

                                    Bundle bundle = new Bundle();
                                    bundle.putString("Flag","1");
                                    Intent in = new Intent(Activity_OfflineSync.this, Activity_OfflineSync.class);
                                    in.putExtras(bundle);
                                    overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
                                    startActivity(in);

                                   // ViewList();
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

    private void GetOrderUpdate(String orderID, String Order_Draft_id ) {
        try {
            App.getInstance().GetCreateOrderLogin(OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");

                            for (int i = 0; i < Products_assets.length(); i++) {
                                JSONObject js = Products_assets.getJSONObject(i);
                                String id = js.getString("id");
                                String assetname = js.getString("assetname");
                                String productid = js.getString("productid");
                                String imageurl = "";
                                //String imageurl = js.getString("imageurl");
                                String filepath = js.getString("filepath");
                                String assettype = js.getString("assettype");
                                String isdefault = js.getString("isdefault");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorAssertTable = sqLiteController.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_ID,id);
                                            if (CursorAssertTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Products_assets_id = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_id"));
                                                    sqLiteController.deleteProductAssertIDItems(Products_assets_id);
                                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                                } while (CursorAssertTable.moveToNext());
                                            }
                                            if(CursorAssertTable.getCount() == 0){
                                                sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < Products.length(); i++) {
                                JSONObject js = Products.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("name");
                                String description = js.getString("description");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");

                                String finalPrice = price;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String all_product_id = CAllOrders.getString(CAllOrders.getColumnIndex("all_product_id"));

                                                        sqLiteController.deleteAllProductIDItems(all_product_id);

                                                        sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);



                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            String customername ="";
                            for (int i = 0; i < Customers.length(); i++) {
                                JSONObject jsC = Customers.getJSONObject(i);
                                String id = jsC.getString("id");
                                String customertypeid = jsC.getString("customertypeid");
                                customername = jsC.getString("businessname").equals("null") ?  jsC.getString("customername") : jsC.getString("businessname");
                                String billingaddressid = jsC.getString("billingaddressid");
                                String shippingaddressid = jsC.getString("shippingaddressid");
                                String deliveryaddressid = jsC.getString("deliveryaddressid");
                                String customernumber = jsC.getString("customernumber");
                                String industry = jsC.getString("industry");
                                String createdby = jsC.getString("createdby");
                                String createdon = jsC.getString("createdon");
                                String status = jsC.getString("status");
                                String modifiedby = jsC.getString("modifiedby");
                                String modifiedon = jsC.getString("modifiedon");
                                String externalid = jsC.getString("externalid");
                                String externalupdated = jsC.getString("externalupdated");
                                String isactive = jsC.getString("isactive");
                                String istaxable = jsC.getString("istaxable");
                                String taxid = jsC.getString("taxid");
                                String netterms = jsC.getString("netterms");

                                String finalCustomername = customername;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchCustomerCount = sqLiteController.fetchCustomerCount();
                                            if (fetchCustomerCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_CUSTOMER,DbHandler.CUSTOMER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String Id = CAllOrders.getString(CAllOrders.getColumnIndex("Id"));

                                                        sqLiteController.deleteCustomerItems(Id);

                                                        sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                                billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < shipments.length(); i++) {
                                JSONObject js = shipments.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String shipmenttypeid = js.getString("shipmenttypeid");
                                String warehouseid = js.getString("warehouseid");
                                String fulfillmentstatus = js.getString("fulfillmentstatus");
                                String routenum = js.getString("routenum");
                                String routedate = js.getString("routedate");
                                String truck = js.getString("truck");
                                String driverid = js.getString( "driverid");
                                String carrierid = js.getString("carrierid");
                                String shippeddate = js.getString("shippeddate");
                                String trackingnumber = js.getString( "trackingnumber");
                                String deliverydate = js.getString( "deliverydate");
                                String deliverytime = js.getString( "deliverytime");
                                String pickerid = js.getString("pickerid");
                                String packerid = js.getString( "packerid");
                                String shipperid = js.getString("shipperid");
                                String invoicerid = js.getString( "invoicerid");
                                String paymenterid = js.getString("paymenterid");
                                String uuid = js.getString( "uuid");
                                String isdisabled = js.getString( "isdisabled");
                                String disabledby = js.getString( "disabledby");
                                String disabledbehalfof = js.getString("disabledbehalfof");
                                String isdeleted = js.getString("isdeleted");
                                String deletedby = js.getString( "deletedby");
                                String deletedbehalfof = js.getString( "deletedbehalfof");
                                String deletedon = js.getString("deletedon");
                                String createdby = js.getString("createdby");
                                String createdbehalfof = js.getString("createdbehalfof");
                                String creratedon = js.getString("creratedon");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String datafromtypeid = js.getString("datafromtypeid");
                                String datafromid = js.getString("datafromid");
                                String tenantid = js.getString("tenantid");
                                String deliveryagentid = js.getString("deliveryagentid");
                                String deliveryshipmenttypeid = js.getString("deliveryshipmenttypeid");
                                String routedayid = js.getString("routedayid");
                                String routeid = js.getString("routeid");
                                String truckid = js.getString("truckid");
                                String invoiceid = js.getString("invoiceid");
                                String workorderid = js.getString("workorderid");
                                String purchaseorderid = js.getString("purchaseorderid");
                                String undeliveredreasonid = js.getString("undeliveredreasonid");
                                String undeliveredreason = js.getString("undeliveredreason");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String shipment_idd = CAllOrders.getString(CAllOrders.getColumnIndex("shipment_id"));
                                                        //@SuppressLint("Range") String ordernumberS = CAllOrders.getString(CAllOrders.getColumnIndex("ordernumber"));

                                                        sqLiteController.deleteShipItems(shipment_idd);

                                                        //Products Insert Method
                                                        sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    //Products Insert Method
                                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < address.length(); i++) {
                                JSONObject js = address.getJSONObject(i);
                                String id = js.getString("id");
                                String line1 = js.getString("line1");
                                String line2 = js.getString("line2");
                                String line3 = js.getString("line3");
                                String stateorprovince = js.getString("stateorprovince");
                                String addresstypecode = js.getString("addresstypecode");
                                String country = js.getString("country");
                                String city = js.getString("city");
                                String postalcode = js.getString("postalcode");
                                String postofficebox = js.getString("postofficebox");
                                String name = js.getString("name");
                                String isprimaryaddress = js.getString("isprimaryaddress");
                                String isactive = js.getString("isactive");
                                String domainEvents = js.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ADDRESS,DbHandler.ADDRESS_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String address_id = CAllOrders.getString(CAllOrders.getColumnIndex("address_id"));

                                                        sqLiteController.deleteAddressItems(address_id);

                                                        sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < Orders.length(); i++) {
                                JSONObject js = Orders.getJSONObject(i);
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String customeridtype = js.getString("customeridtype");
                                String datefulfilled = js.getString("datefulfilled");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String poreferencenum = js.getString("poreferencenum");
                                String ispricelocked = js.getString("ispricelocked");
                                String quoteid = js.getString("quoteid");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String submitdate = js.getString("submitdate");
                                String submitstatus = js.getString("submitstatus");
                                String totalamount = js.getString("totalamount");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String createdby = js.getString("createdby");
                                String creratedon = js.getString("creratedon");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String cancellationreason = js.getString("cancellationreason");
                                String comments = js.getString("comments");
                                String lastbackofficesubmit = js.getString("lastbackofficesubmit");
                                String opportunityid = js.getString("opportunityid");
                                String ordernumber = js.getString("ordernumber");
                                String cancellationDate = js.getString("cancellationDate");
                                String confirmedby = js.getString("confirmedby");
                                String confirmeddate = js.getString("confirmeddate");
                                String notes = js.getString("notes");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String warehouseid = js.getString("warehouseid");
                                String modifiedon = js.getString("modifiedon");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");
                                String customeristaxable = js.getString("customeristaxable");

                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String order_id = CAllOrders.getString(CAllOrders.getColumnIndex("order_id"));

                                                        sqLiteController.deleteOrder(order_id);

                                                        if (draftnumber.equals("null")) {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                        }else {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                        }


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    if (draftnumber.equals("null")) {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                    }else {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                    }
                                                }

                                                sqLiteController.deleteProductItems(id);
                                            }
                                        } finally {
                                            sqLiteController.close();


                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < OrderProducts.length(); i++) {
                                JSONObject js = OrderProducts.getJSONObject(i);
                                String productname = js.getString("productname");
                                String quantity = js.getString("quantity");
                                String baseamount = js.getString("baseamount");
                                String priceperunit = js.getString("priceperunit");
                                String productid = js.getString("productid");
                                String orderid = js.getString("orderid");
                                String pid = js.getString("id");
                                String itemistaxable = js.getString("itemistaxable");
                                String warehouseid = js.getString("warehouseid");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRO_ID,pid);

                                                if(CAllOrders.getCount() == 0){
                                                    //Products Insert Method
                                                    sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();

                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject js = invoice.getJSONObject(i);

                                String orderid = js.getString("orderid");
                                String externalid = js.getString("externalid");
                                String externalstatus = js.getString("externalstatus");
                                String paymenttypeid = js.getString("paymenttypeid");
                                String paymentid = js.getString("paymentid");
                                String paymentmethod = js.getString("paymentmethod");
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String vendorid = js.getString("vendorid");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String ispricelocked = js.getString("ispricelocked");
                                String ordernumber = js.getString("ordernumber");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String invoicedate = js.getString("invoicedate");
                                String submitstatus = js.getString("submitstatus");
                                String submitstatusdescription = js.getString("submitstatusdescription");
                                String totalamount = js.getString("totalamount");
                                String totalamountbase = js.getString("totalamountbase");
                                String totalamountlessfreight = js.getString("totalamountlessfreight");
                                String totalamountlessfreightbase = js.getString("totalamountlessfreightbase");
                                String totaldiscountamount = js.getString("totaldiscountamount");
                                String totaldiscountamountbase = js.getString("totaldiscountamountbase");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totallineitemamountbase = js.getString("totallineitemamountbase");
                                String totallineitemdiscountamount = js.getString("totallineitemdiscountamount");
                                String totallineitemdiscountamountbase = js.getString("totallineitemdiscountamountbase");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String willcall = js.getString("willcall");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String referenceorder = js.getString("referenceorder");
                                String createdon = js.getString("createdon");
                                String createdby = js.getString("createdby");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String warehouseid = js.getString("warehouseid");
                                String customeristaxable = js.getString("customeristaxable");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");


                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorInvoiceTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,id);
                                            if (CursorInvoiceTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = CursorInvoiceTable.getString(CursorInvoiceTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoice(id);
                                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                    } else {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                    }
                                                } while (CursorInvoiceTable.moveToNext());
                                            }
                                            if(CursorInvoiceTable.getCount() == 0){
                                                if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                } else {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                }
                                            }
                                            sqLiteController.deleteInvoiceProductsItems(id);
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoiceproduct.length(); i++) {
                                JSONObject js_P = invoiceproduct.getJSONObject(i);
                                String invoiceid_IP = js_P.getString("invoiceid");
                                String transactioncurrencyid_IP = js_P.getString("transactioncurrencyid");
                                String uomid = js_P.getString("uomid");
                                String baseamount = js_P.getString("baseamount");
                                String exchangerate_Ip = js_P.getString("exchangerate");
                                String baseamountbase = js_P.getString("baseamountbase");
                                String description_Ip = js_P.getString("description");
                                String extendedamount = js_P.getString("extendedamount");
                                String extendedamountbase = js_P.getString("extendedamountbase");
                                String iscopied = js_P.getString("iscopied");
                                String ispriceoverridden = js_P.getString("ispriceoverridden");
                                String isproductoverridden = js_P.getString("isproductoverridden");
                                String lineitemnumber = js_P.getString("lineitemnumber");
                                String manualdiscountamount = js_P.getString("manualdiscountamount");
                                String manualdiscountamountbase = js_P.getString("manualdiscountamountbase");
                                String priceperunit = js_P.getString("priceperunit");
                                String priceperunitbase = js_P.getString("priceperunitbase");
                                String productdescription = js_P.getString("productdescription");
                                String productname = js_P.getString("productname");
                                String productid = js_P.getString("productid");
                                String quantity = js_P.getString("quantity");
                                String quantitybackordered = js_P.getString("quantitybackordered");
                                String quantitycancelled = js_P.getString("quantitycancelled");
                                String quantityshipped = js_P.getString("quantityshipped");
                                String quantitypicked = js_P.getString("quantitypicked");
                                String quantitypacked = js_P.getString("quantitypacked");
                                String requestdeliverybyip = js_P.getString("requestdeliveryby");
                                String salesorderispricelocked = js_P.getString("salesorderispricelocked");
                                String shiptoaddressidip = js_P.getString("shiptoaddressid");
                                String tax = js_P.getString("tax");
                                String taxbase = js_P.getString("taxbase");
                                String willcallip = js_P.getString("willcall");
                                String createdonip = js_P.getString("createdon");
                                String createdbyip = js_P.getString("createdby");
                                String modifiedbyip = js_P.getString("modifiedby");
                                String modifiedbehalfofip = js_P.getString("modifiedbehalfof");
                                String modifiedonip = js_P.getString("modifiedon");
                                String orderpid = js_P.getString("orderpid");
                                String itemistaxable = js_P.getString("itemistaxable");
                                String idip = js_P.getString("id");
                                String isactiveip = js_P.getString("isactive");
                                String domainEvents = js_P.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor InvoiceProductTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE_PRODUCTS,DbHandler.IN_PRODUCTS_ID,idip);
                                            /*if (InvoiceProductTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = InvoiceProductTable.getString(InvoiceProductTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoiceProductsALL(id);
                                                    sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                                } while (InvoiceProductTable.moveToNext());
                                            }*/
                                            if(InvoiceProductTable.getCount() == 0){
                                                sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Date date1 = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String currentDateTime = sdf.format(date1);

                                    System.out.println("currentDateTime-----"+currentDateTime);

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                        for (String s: elements) {
                                            System.out.println("systemOrders-----------"+s);
                                            sqLiteController.UpdateMasterSync(s,currentDateTime);

                                        }

                                    } finally {
                                        sqLiteController.close();
                                    }


                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }

                                    ViewList();
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

    private void postPartCreateInvoiceFull(String json,String Inv_data,String Order_Draft_id,String orderid_,String totalamount) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    RequestBody body = RequestBody.create(JSON,json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.hostName+"order/warehouse/fulfillmentupdate")
                            .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();

                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("FullfillmentInvoice---"+jsonObject);
                            boolean succeeded = jsonObject.getBoolean("succeeded");
                            if ( succeeded == true) {

                              // Get_OrderInvoiceList(Inv_data,Order_Draft_id);

                                //GetInvoiceInsert(Inv_data,"invoice",Order_Draft_id);

                                GetInvoiceInsert(Inv_data,Order_Draft_id);

                                /*         JSONObject json2 = new JSONObject();
                                json2.put("invoiceid",Inv_data);
                                json2.put("paymentmethodid","4E142367-5F99-4AA7-99ED-931756978EE5");
                                json2.put("paymentdate",currentDate1);
                                json2.put("payable",totalamount);
                                json2.put("paid",totalamount);
                                json2.put("due","0");
                                json2.put("checknumber","");
                                json2.put("paymentmemo","");
                                json2.put("isactive",true);
                                json2.put("createdby",user_id);
                                json2.put("creratedon",currentDate1);
                                postDeliver1(json2.toString(),orderid_,Inv_data,Order_Draft_id);

                                System.out.println("postDeliver1.toString()--"+json.toString());*/

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

    private void GetInvoiceInsert(String InvoiceID, String invoiceDraft_id) {
        try {
            App.getInstance().GetCreateOrderLogin(OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");

                            for (int i = 0; i < Products_assets.length(); i++) {
                                JSONObject js = Products_assets.getJSONObject(i);
                                String id = js.getString("id");
                                String assetname = js.getString("assetname");
                                String productid = js.getString("productid");
                                String imageurl = "";
                                //String imageurl = js.getString("imageurl");
                                String filepath = js.getString("filepath");
                                String assettype = js.getString("assettype");
                                String isdefault = js.getString("isdefault");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorAssertTable = sqLiteController.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_ID,id);
                                            if (CursorAssertTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Products_assets_id = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_id"));
                                                    sqLiteController.deleteProductAssertIDItems(Products_assets_id);
                                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                                } while (CursorAssertTable.moveToNext());
                                            }
                                            if(CursorAssertTable.getCount() == 0){
                                                sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < Products.length(); i++) {
                                JSONObject js = Products.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("name");
                                String description = js.getString("description");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");

                                String finalPrice = price;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String all_product_id = CAllOrders.getString(CAllOrders.getColumnIndex("all_product_id"));

                                                        sqLiteController.deleteAllProductIDItems(all_product_id);

                                                        sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);



                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < shipments.length(); i++) {
                                JSONObject js = shipments.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String shipmenttypeid = js.getString("shipmenttypeid");
                                String warehouseid = js.getString("warehouseid");
                                String fulfillmentstatus = js.getString("fulfillmentstatus");
                                String routenum = js.getString("routenum");
                                String routedate = js.getString("routedate");
                                String truck = js.getString("truck");
                                String driverid = js.getString( "driverid");
                                String carrierid = js.getString("carrierid");
                                String shippeddate = js.getString("shippeddate");
                                String trackingnumber = js.getString( "trackingnumber");
                                String deliverydate = js.getString( "deliverydate");
                                String deliverytime = js.getString( "deliverytime");
                                String pickerid = js.getString("pickerid");
                                String packerid = js.getString( "packerid");
                                String shipperid = js.getString("shipperid");
                                String invoicerid = js.getString( "invoicerid");
                                String paymenterid = js.getString("paymenterid");
                                String uuid = js.getString( "uuid");
                                String isdisabled = js.getString( "isdisabled");
                                String disabledby = js.getString( "disabledby");
                                String disabledbehalfof = js.getString("disabledbehalfof");
                                String isdeleted = js.getString("isdeleted");
                                String deletedby = js.getString( "deletedby");
                                String deletedbehalfof = js.getString( "deletedbehalfof");
                                String deletedon = js.getString("deletedon");
                                String createdby = js.getString("createdby");
                                String createdbehalfof = js.getString("createdbehalfof");
                                String creratedon = js.getString("creratedon");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String datafromtypeid = js.getString("datafromtypeid");
                                String datafromid = js.getString("datafromid");
                                String tenantid = js.getString("tenantid");
                                String deliveryagentid = js.getString("deliveryagentid");
                                String deliveryshipmenttypeid = js.getString("deliveryshipmenttypeid");
                                String routedayid = js.getString("routedayid");
                                String routeid = js.getString("routeid");
                                String truckid = js.getString("truckid");
                                String invoiceid = js.getString("invoiceid");
                                String workorderid = js.getString("workorderid");
                                String purchaseorderid = js.getString("purchaseorderid");
                                String undeliveredreasonid = js.getString("undeliveredreasonid");
                                String undeliveredreason = js.getString("undeliveredreason");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String shipment_idd = CAllOrders.getString(CAllOrders.getColumnIndex("shipment_id"));
                                                        //@SuppressLint("Range") String ordernumberS = CAllOrders.getString(CAllOrders.getColumnIndex("ordernumber"));

                                                        sqLiteController.deleteShipItems(shipment_idd);

                                                        //Products Insert Method
                                                        sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    //Products Insert Method
                                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            String customername ="";
                            for (int i = 0; i < Customers.length(); i++) {
                                JSONObject jsC = Customers.getJSONObject(i);
                                String id = jsC.getString("id");
                                String customertypeid = jsC.getString("customertypeid");
                                customername = jsC.getString("businessname").equals("null") ?  jsC.getString("customername") : jsC.getString("businessname");
                                String billingaddressid = jsC.getString("billingaddressid");
                                String shippingaddressid = jsC.getString("shippingaddressid");
                                String deliveryaddressid = jsC.getString("deliveryaddressid");
                                String customernumber = jsC.getString("customernumber");
                                String industry = jsC.getString("industry");
                                String createdby = jsC.getString("createdby");
                                String createdon = jsC.getString("createdon");
                                String status = jsC.getString("status");
                                String modifiedby = jsC.getString("modifiedby");
                                String modifiedon = jsC.getString("modifiedon");
                                String externalid = jsC.getString("externalid");
                                String externalupdated = jsC.getString("externalupdated");
                                String isactive = jsC.getString("isactive");
                                String istaxable = jsC.getString("istaxable");
                                String taxid = jsC.getString("taxid");
                                String netterms = jsC.getString("netterms");

                                String finalCustomername = customername;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchCustomerCount = sqLiteController.fetchCustomerCount();
                                            if (fetchCustomerCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_CUSTOMER,DbHandler.CUSTOMER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String Id = CAllOrders.getString(CAllOrders.getColumnIndex("Id"));

                                                        sqLiteController.deleteCustomerItems(Id);

                                                        sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                                billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < address.length(); i++) {
                                JSONObject js = address.getJSONObject(i);
                                String id = js.getString("id");
                                String line1 = js.getString("line1");
                                String line2 = js.getString("line2");
                                String line3 = js.getString("line3");
                                String stateorprovince = js.getString("stateorprovince");
                                String addresstypecode = js.getString("addresstypecode");
                                String country = js.getString("country");
                                String city = js.getString("city");
                                String postalcode = js.getString("postalcode");
                                String postofficebox = js.getString("postofficebox");
                                String name = js.getString("name");
                                String isprimaryaddress = js.getString("isprimaryaddress");
                                String isactive = js.getString("isactive");
                                String domainEvents = js.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ADDRESS,DbHandler.ADDRESS_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String address_id = CAllOrders.getString(CAllOrders.getColumnIndex("address_id"));

                                                        sqLiteController.deleteAddressItems(address_id);

                                                        sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < Orders.length(); i++) {
                                JSONObject js = Orders.getJSONObject(i);
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String customeridtype = js.getString("customeridtype");
                                String datefulfilled = js.getString("datefulfilled");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String poreferencenum = js.getString("poreferencenum");
                                String ispricelocked = js.getString("ispricelocked");
                                String quoteid = js.getString("quoteid");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String submitdate = js.getString("submitdate");
                                String submitstatus = js.getString("submitstatus");
                                String totalamount = js.getString("totalamount");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String createdby = js.getString("createdby");
                                String creratedon = js.getString("creratedon");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String cancellationreason = js.getString("cancellationreason");
                                String comments = js.getString("comments");
                                String lastbackofficesubmit = js.getString("lastbackofficesubmit");
                                String opportunityid = js.getString("opportunityid");
                                String ordernumber = js.getString("ordernumber");
                                String cancellationDate = js.getString("cancellationDate");
                                String confirmedby = js.getString("confirmedby");
                                String confirmeddate = js.getString("confirmeddate");
                                String notes = js.getString("notes");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String warehouseid = js.getString("warehouseid");
                                String modifiedon = js.getString("modifiedon");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");
                                String customeristaxable = js.getString("customeristaxable");

                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String order_id = CAllOrders.getString(CAllOrders.getColumnIndex("order_id"));

                                                        sqLiteController.deleteOrder(order_id);

                                                        if (draftnumber.equals("null")) {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                        }else {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                        }


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    if (draftnumber.equals("null")) {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                    }else {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                    }
                                                }

                                                sqLiteController.deleteProductItems(id);
                                            }
                                        } finally {
                                            sqLiteController.close();


                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < OrderProducts.length(); i++) {
                                JSONObject js = OrderProducts.getJSONObject(i);
                                String productname = js.getString("productname");
                                String quantity = js.getString("quantity");
                                String baseamount = js.getString("baseamount");
                                String priceperunit = js.getString("priceperunit");
                                String productid = js.getString("productid");
                                String orderid = js.getString("orderid");
                                String pid = js.getString("id");
                                String itemistaxable = js.getString("itemistaxable");
                                String warehouseid = js.getString("warehouseid");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRO_ID,pid);

                                                if(CAllOrders.getCount() == 0){
                                                    //Products Insert Method
                                                    sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();

                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject js = invoice.getJSONObject(i);

                                String orderid = js.getString("orderid");
                                String externalid = js.getString("externalid");
                                String externalstatus = js.getString("externalstatus");
                                String paymenttypeid = js.getString("paymenttypeid");
                                String paymentid = js.getString("paymentid");
                                String paymentmethod = js.getString("paymentmethod");
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String vendorid = js.getString("vendorid");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String ispricelocked = js.getString("ispricelocked");
                                String ordernumber = js.getString("ordernumber");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String invoicedate = js.getString("invoicedate");
                                String submitstatus = js.getString("submitstatus");
                                String submitstatusdescription = js.getString("submitstatusdescription");
                                String totalamount = js.getString("totalamount");
                                String totalamountbase = js.getString("totalamountbase");
                                String totalamountlessfreight = js.getString("totalamountlessfreight");
                                String totalamountlessfreightbase = js.getString("totalamountlessfreightbase");
                                String totaldiscountamount = js.getString("totaldiscountamount");
                                String totaldiscountamountbase = js.getString("totaldiscountamountbase");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totallineitemamountbase = js.getString("totallineitemamountbase");
                                String totallineitemdiscountamount = js.getString("totallineitemdiscountamount");
                                String totallineitemdiscountamountbase = js.getString("totallineitemdiscountamountbase");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String willcall = js.getString("willcall");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String referenceorder = js.getString("referenceorder");
                                String createdon = js.getString("createdon");
                                String createdby = js.getString("createdby");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String warehouseid = js.getString("warehouseid");
                                String customeristaxable = js.getString("customeristaxable");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");


                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        sqLiteController.deleteInvoiceProductItems(invoiceDraft_id);
                                        sqLiteController.deleteInvoice(invoiceDraft_id);
                                        try {
                                            Cursor CursorInvoiceTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,id);
                                            if (CursorInvoiceTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = CursorInvoiceTable.getString(CursorInvoiceTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoice(id);
                                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                    } else {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                    }
                                                } while (CursorInvoiceTable.moveToNext());
                                            }
                                            if(CursorInvoiceTable.getCount() == 0){
                                                if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                } else {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                }
                                            }
                                            sqLiteController.deleteInvoiceProductsItems(id);
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoiceproduct.length(); i++) {
                                JSONObject js_P = invoiceproduct.getJSONObject(i);
                                String invoiceid_IP = js_P.getString("invoiceid");
                                String transactioncurrencyid_IP = js_P.getString("transactioncurrencyid");
                                String uomid = js_P.getString("uomid");
                                String baseamount = js_P.getString("baseamount");
                                String exchangerate_Ip = js_P.getString("exchangerate");
                                String baseamountbase = js_P.getString("baseamountbase");
                                String description_Ip = js_P.getString("description");
                                String extendedamount = js_P.getString("extendedamount");
                                String extendedamountbase = js_P.getString("extendedamountbase");
                                String iscopied = js_P.getString("iscopied");
                                String ispriceoverridden = js_P.getString("ispriceoverridden");
                                String isproductoverridden = js_P.getString("isproductoverridden");
                                String lineitemnumber = js_P.getString("lineitemnumber");
                                String manualdiscountamount = js_P.getString("manualdiscountamount");
                                String manualdiscountamountbase = js_P.getString("manualdiscountamountbase");
                                String priceperunit = js_P.getString("priceperunit");
                                String priceperunitbase = js_P.getString("priceperunitbase");
                                String productdescription = js_P.getString("productdescription");
                                String productname = js_P.getString("productname");
                                String productid = js_P.getString("productid");
                                String quantity = js_P.getString("quantity");
                                String quantitybackordered = js_P.getString("quantitybackordered");
                                String quantitycancelled = js_P.getString("quantitycancelled");
                                String quantityshipped = js_P.getString("quantityshipped");
                                String quantitypicked = js_P.getString("quantitypicked");
                                String quantitypacked = js_P.getString("quantitypacked");
                                String requestdeliverybyip = js_P.getString("requestdeliveryby");
                                String salesorderispricelocked = js_P.getString("salesorderispricelocked");
                                String shiptoaddressidip = js_P.getString("shiptoaddressid");
                                String tax = js_P.getString("tax");
                                String taxbase = js_P.getString("taxbase");
                                String willcallip = js_P.getString("willcall");
                                String createdonip = js_P.getString("createdon");
                                String createdbyip = js_P.getString("createdby");
                                String modifiedbyip = js_P.getString("modifiedby");
                                String modifiedbehalfofip = js_P.getString("modifiedbehalfof");
                                String modifiedonip = js_P.getString("modifiedon");
                                String orderpid = js_P.getString("orderpid");
                                String itemistaxable = js_P.getString("itemistaxable");
                                String idip = js_P.getString("id");
                                String isactiveip = js_P.getString("isactive");
                                String domainEvents = js_P.getString("domainEvents");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor InvoiceProductTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE_PRODUCTS,DbHandler.IN_PRODUCTS_ID,idip);
                                            /*if (InvoiceProductTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = InvoiceProductTable.getString(InvoiceProductTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoiceProductsALL(id);
                                                    sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                                } while (InvoiceProductTable.moveToNext());
                                            }*/
                                            if(InvoiceProductTable.getCount() == 0){
                                                sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Date date1 = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String currentDateTime = sdf.format(date1);

                                    System.out.println("currentDateTime-----"+currentDate1);

                                    SQLiteController sqLiteController = new SQLiteController(Activity_OfflineSync.this);
                                    sqLiteController.open();
                                    try {
                                        String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                        for (String s: elements) {
                                            System.out.println("systemOrders-----------"+s);
                                            sqLiteController.UpdateMasterSync(s,currentDate1);

                                        }

                                    } finally {
                                        sqLiteController.close();
                                    }


                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }
                                  //  ViewInvoiceList();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("Flag","2");
                                    Intent in = new Intent(Activity_OfflineSync.this, Activity_OfflineSync.class);
                                    in.putExtras(bundle);
                                    overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
                                    startActivity(in);

                                  /*  Intent intent = new Intent(Activity_OfflineSync.this, Activity_OfflineSync.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);*/
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
        // super.onBackPressed();
    }

}