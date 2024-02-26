package com.example.arcomdriver.order;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ItemList;
import com.example.arcomdriver.adapter.Adapter_ItemProducts;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_CustomerProductsPricing;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.model.Model_ItemProducts;
import com.example.arcomdriver.products.Activity_CreateProduct;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.example.arcomdriver.recyclerhelper.WrapContentLinearLayoutManager;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
public class Activity_UpdateOrder extends Activity_Menu {
    private CoordinatorLayout cl;
    private RecyclerView mRecyclerView;
    private AppCompatTextView et_pricingDate,txt_payable_amount,tv_tax,et_orderDate,tv_shippingCharge,et_deliveryDate,txt_no_record,txt_sub_total,tv_discount;
    private AppCompatEditText et_tax,et_discount,et_shippingCharge,et_deliveryNotes,et_terms,et_packShipNote,et_memo;
    private Adapter_ItemList adapter;
    private ArrayList<Model_ItemList> list = new ArrayList<>();
    private ArrayList<String> deleteList = new ArrayList<>();
    final Calendar myCalendar= Calendar.getInstance();
    private Calendar c;
    private int year;
    private int month;
    private int day;
    private double TotalSum=0.00;
    private double TotalPayableAmt=0.00;
    private double Tax_Amt=0.00;
    private double Ship_Amt=0.00;
    private double Dis_Amt=0.00;
    private String sYear,user_id,OrderId,CustomerName,str_DeliveryPName,str_DeliveryPID="00000000-0000-0000-0000-000000000000";
    static final int DATE_PICKER1_ID = 1;
    static final int DATE_PICKER2_ID = 2;
    static final int DATE_PICKER3_ID = 3;
    private int mYear, mMonth, mDay;
    AppCompatTextView bill_tv,del_tv,tv_dueDate;
    AppCompatEditText et_AddRef;
    private ArrayList<String> arTaxID,arTerms,arTaxable,arDeliveryID,arBillingID,arCustomerID,arSalesRepID,arFulfillID,arWarehouseID,arProduct_dec,arProduct_tax,arProduct_upsc,arProductID,arProductImage,arProduct_price;
    private ArrayList<String> arCustomerName,arSalesRepName,arFulfilmentName,arWarehouseName,arProductName,arDeliveryPersonName,arDeliveryPersonID;;
    Spinner sp_DeliveryPerson,spONetTerms,sp_salesRep,spFulfillmentType,spWarehouse,spProduct;
    String  deliveryshipmenttypeid,deliveryagentid,or_termsId="null",Str_TermsID="0",DueDate="MM/DD/YYYY",str_TaxID,shipmenttypeid,Str_dec,Str_CS="0",str_Taxable,submitstatus,ordernumber,updiscountpercentage,uptotaltaxbase,upfreightamount,up_salesrepid,up_customerid,deliveryDate_str,token,str_customerID,str_DeliveryID,str_BilingID,str_ProductID,Str_UpscCode,str_ProductPrice,str_customer="Choose Customer",str_salesRep,str_fulfilment,str_warehouse,str_product,str_ProductUrl,str_salesRepID,str_warehouseID="00000000-0000-0000-0000-000000000000",warehouseid,str_fulfilmentID;


    RecyclerView mRecyclerProducts;
    ArrayList<Model_ItemProducts> stList;
    Adapter_ItemProducts mAdapter;
    SearchView searchBar;
    CheckBox chkAllSelected;
    AppCompatTextView txt_no_record2;
    ArrayList<Model_ItemProducts> filteredModelList;

    CheckBox CS_chkSelected;
    private double TotalTaxSum=0.00;
    private String taxpercentage="0.00",CusID="null";
    //Toolbar toolbar;

    Date fromDate, toDate;

    private SharedPreferences tkSharedPreferences;
    public static final String tkPref = "tkPreferences";
    public static final String order_Count = "orderCount";
    public static final String order_CrDate = "order_CrDate";

    private ArrayList<String> arTermsID = new ArrayList<>();
    private ArrayList<String> arTermsName = new ArrayList<>();

    private ArrayList<String> arTermsDays = new ArrayList<>();

    public AlertDialog Progress_dialog;

    LinearLayout warehouse_ll;

    boolean wrBln =false;


    String alert ="true",settingsvalue ="0", default_netterms ="0",RefString="0",DisString="0",customerpricingsetup="1",settingsvalue_tax="1";;

    LinearLayout deliveryPerson_ll;

    AppCompatTextView  tv_customerSp;

    private ArrayList<Model_CustomerProductsPricing> CustPrice = new ArrayList<>();

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_updateorder);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_updateorder, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Edit Order");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateOrder.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_UpdateOrder.this.getLayoutInflater();
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
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                }

                Cursor C_In = sqLiteController1.readTableInvoiceFormat();
                if (C_In.moveToFirst()) {
                    do {
                        String invoice_name = C_In.getString(C_In.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("AddinList")){

                            settingsvalue  = C_In.getString(C_In.getColumnIndex("invoice_value"));



                        }


                        if(invoice_name.equals("default-netterms")){

                            default_netterms  = C_In.getString(C_In.getColumnIndex("invoice_value"));



                        }

                        if(invoice_name.equals("customerpricingsetup")){

                            customerpricingsetup  = C_In.getString(C_In.getColumnIndex("invoice_value"));

                        }

                        if(invoice_name.equals("tax")){

                            settingsvalue_tax  = C_In.getString(C_In.getColumnIndex("invoice_value"));

                        }

                    } while (C_In.moveToNext());
                }

            }
        } finally {
            sqLiteController1.close();
        }


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

         cl = findViewById(R.id.cl);
        //toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.rc_ItemsList);
        CS_chkSelected = findViewById(R.id.CS_chkSelected);
        txt_no_record = findViewById(R.id.txt_no_record);
        et_orderDate = findViewById(R.id.et_orderDate);
        et_pricingDate = findViewById(R.id.et_pricingDate);
        LinearLayout od_er_ll = findViewById(R.id.od_er_ll);
        et_AddRef = findViewById(R.id.et_AddRef);
        et_deliveryDate = findViewById(R.id.et_deliveryDate);
        //spCustomer = findViewById(R.id.spCustomer);
       // spCustomer.setEnabled(false);
       // spCustomer.setClickable(false);
        tv_customerSp = findViewById(R.id.tv_customerSp);
        spONetTerms = findViewById(R.id.spONetTerms);
        tv_dueDate = findViewById(R.id.tv_dueDate);
        sp_salesRep = findViewById(R.id.sp_salesRep);
        spFulfillmentType = findViewById(R.id.spFulfillmentType);
        spWarehouse = findViewById(R.id.spWarehouse);
        deliveryPerson_ll = findViewById(R.id.deliveryPerson_ll);
        sp_DeliveryPerson = findViewById(R.id.sp_DeliveryPerson);
        warehouse_ll = findViewById(R.id.warehouse_ll);
        LinearLayout ll_custTax = findViewById(R.id.ll_custTax);
        if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
            //Hide Warehouse
            warehouse_ll.setVisibility(View.GONE);
        }else {
            //Show Warehouse
            warehouse_ll.setVisibility(View.VISIBLE);
        }

        System.out.println("settingsvalue_tax--"+settingsvalue_tax);

        if(settingsvalue_tax.equals("1")) {
            //show
            ll_custTax.setVisibility(View.VISIBLE);
        }else {
            //hide
            ll_custTax.setVisibility(View.GONE);
        }

        txt_sub_total = findViewById(R.id.txt_sub_total);
        txt_sub_total.setText(Activity_OrdersHistory.currency_symbol+"0.00");

        AppCompatTextView tv_tittle_ship = findViewById(R.id.tv_tittle_ship);
        AppCompatTextView tv_totl_tittle = findViewById(R.id.tv_totl_tittle);
        tv_tittle_ship.setText("Shipping Charges ("+ Activity_OrdersHistory.currency_symbol+")");
        tv_totl_tittle.setText("Total ("+ Activity_OrdersHistory.currency_symbol+")");

        et_discount = findViewById(R.id.et_discount);
        tv_discount = findViewById(R.id.tv_discount);
        tv_discount.setText(Activity_OrdersHistory.currency_symbol+"0.00");

        tv_shippingCharge = findViewById(R.id.tv_shippingCharge);
        et_shippingCharge = findViewById(R.id.et_shippingCharge);
        tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+"0.00");
        et_shippingCharge.setHint("("+ Activity_OrdersHistory.currency_symbol+")"+"0.00");

        et_tax = findViewById(R.id.et_tax);
        tv_tax = findViewById(R.id.tv_tax);
        tv_tax.setText(Activity_OrdersHistory.currency_symbol+"0.00");

        txt_payable_amount = findViewById(R.id.txt_payable_amount);
        txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+"0.00");

        bill_tv = findViewById(R.id.bill_tv);
        del_tv = findViewById(R.id.del_tv);
        et_deliveryNotes = findViewById(R.id.et_deliveryNotes);
        et_terms = findViewById(R.id.et_terms);
        et_packShipNote = findViewById(R.id.et_packShipNote);
        et_memo = findViewById(R.id.et_memo);

        findViewById(R.id.CustomerUp_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","1");
                bundle.putString("OrderId",OrderId);
                bundle.putString("CustomerName",CustomerName);
                Intent in = new Intent(Activity_UpdateOrder.this, Activity_CreateCustomer.class);
                in.putExtras(bundle);
                startActivity(in);

            }
        });

        Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
        //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate2 = sd2.format(date2);
        et_orderDate.setText(currentDate2);
        et_pricingDate.setText(currentDate2);

        adapter = new Adapter_ItemList(list);
        RecyclerView.LayoutManager layoutManager = new WrapContentLinearLayoutManager(Activity_UpdateOrder.this, LinearLayoutManager.VERTICAL, false);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);


        SQLiteController sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                arCustomerName = new ArrayList<String>();
                arCustomerName.add("Choose Customer");
                arCustomerID = new ArrayList<String>();
                arCustomerID.add("0");
                arBillingID = new ArrayList<String>();
                arBillingID.add("0");
                arDeliveryID = new ArrayList<String>();
                arDeliveryID.add("0");
                arTaxable = new ArrayList<String>();
                arTaxable.add("false");
                arTaxID = new ArrayList<String>();
                arTaxID.add("0");
                arTerms = new ArrayList<String>();
                arTerms.add("0");
                //Customer
                Cursor customer_c = sqLiteController.readTableCustomer();
                if (customer_c != null && customer_c.moveToFirst()) {
                    do {

                        String customer = String.valueOf(customer_c.getString(customer_c.getColumnIndex("customername")));
                        String status = String.valueOf(customer_c.getString(customer_c.getColumnIndex("status")));
                        if(status.equals("Active")){
                            arCustomerName.add(customer_c.getString(customer_c.getColumnIndex("customername")));
                            arCustomerID.add(customer_c.getString(customer_c.getColumnIndex("Id")));
                            arBillingID.add(customer_c.getString(customer_c.getColumnIndex("billingaddressid")));
                            arDeliveryID.add(customer_c.getString(customer_c.getColumnIndex("shippingaddressid")));
                            arTaxable.add(customer_c.getString(customer_c.getColumnIndex("istaxable")));
                            arTaxID.add(customer_c.getString(customer_c.getColumnIndex("taxID")));
                            arTerms.add(customer_c.getString(customer_c.getColumnIndex("netTermsID")));
                        }


                    } while (customer_c.moveToNext());
                }


                arTermsName = new ArrayList<String>();
                arTermsName.add("Choose Net Terms");
                arTermsID = new ArrayList<String>();
                arTermsID.add("0");
                arTermsDays = new ArrayList<String>();
                arTermsDays.add("0");
                //Terms
                Cursor c_terms = sqLiteController.readTableTerms();
                if (c_terms != null && c_terms.moveToFirst()) {
                    do {
                        arTermsName.add(c_terms.getString(c_terms.getColumnIndex("terms_name")));
                        arTermsID.add(c_terms.getString(c_terms.getColumnIndex("terms_id")));
                        arTermsDays.add(c_terms.getString(c_terms.getColumnIndex("terms_days")));

                    } while (c_terms.moveToNext());
                }

                arSalesRepName = new ArrayList<String>();
                arSalesRepName.add("Choose Sales Rep");
                arSalesRepID = new ArrayList<String>();
                arSalesRepID.add("0");

                //salesRep


                arDeliveryPersonName = new ArrayList<String>();
                arDeliveryPersonName.add("Choose Delivery Person");
                arDeliveryPersonID = new ArrayList<String>();
                arDeliveryPersonID.add("0");

                /*Cursor delivery_c = sqLiteController.readTableDeliveryRep();
                if (delivery_c != null && delivery_c.moveToFirst()) {
                    do {
                        arDeliveryPersonName.add(delivery_c.getString(delivery_c.getColumnIndex("delivery_rep_name")));
                        arDeliveryPersonID.add(delivery_c.getString(delivery_c.getColumnIndex("delivery_rep_id")));

                    } while (delivery_c.moveToNext());
                }*/

                Cursor sales_c = sqLiteController.readTableSalesRep();
                if (sales_c != null && sales_c.moveToFirst()) {
                    do {
                        arSalesRepName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arSalesRepID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));

                        arDeliveryPersonName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arDeliveryPersonID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));

                    } while (sales_c.moveToNext());
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

                arWarehouseName = new ArrayList<String>();
                arWarehouseName.add("Choose Warehouse");
                arWarehouseID = new ArrayList<String>();
                arWarehouseID.add("0");
                //warehouse
                Cursor warehouse_c = sqLiteController.readTableWarehouse();
                if (warehouse_c != null && warehouse_c.moveToFirst()) {
                    do {
                        arWarehouseName.add(warehouse_c.getString(warehouse_c.getColumnIndex("warehouse_name")));
                        arWarehouseID.add(warehouse_c.getString(warehouse_c.getColumnIndex("warehouse_id")));

                    } while (warehouse_c.moveToNext());
                }

                arProductName = new ArrayList<String>();
                arProductName.add("Choose Product");
                arProductID = new ArrayList<String>();
                arProductID.add("0");
                arProductImage = new ArrayList<String>();
                arProductImage.add("null");
                arProduct_price = new ArrayList<String>();
                arProduct_price.add("0");
                arProduct_tax = new ArrayList<String>();
                arProduct_tax.add("0");
                arProduct_upsc = new ArrayList<String>();
                arProduct_upsc.add("0");
                arProduct_dec = new ArrayList<String>();
                arProduct_dec.add("");
                //product
                Cursor product_c = sqLiteController.readAllTableProduct();
                if (product_c != null && product_c.moveToFirst()) {
                    do {
                        arProductName.add(product_c.getString(product_c.getColumnIndex("all_product_name")));
                        arProductID.add(product_c.getString(product_c.getColumnIndex("all_product_id")));
                        arProductImage.add(product_c.getString(product_c.getColumnIndex("all_product_img")));
                        arProduct_price.add(product_c.getString(product_c.getColumnIndex("all_product_price")));
                        arProduct_tax.add(product_c.getString(product_c.getColumnIndex("all_istax")));
                        arProduct_upsc.add(product_c.getString(product_c.getColumnIndex("all_upsc")));
                        arProduct_dec.add(product_c.getString(product_c.getColumnIndex("all_description")));

                    } while (product_c.moveToNext());
                }


            }
        } finally {
            sqLiteController.close();
        }


        ArrayAdapter<String> TermsAdapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arTermsName);
        TermsAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spONetTerms.setAdapter(TermsAdapter);
        spONetTerms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String str_TermsName = arTermsName.get(position);
                Str_TermsID = arTermsID.get(position);
                String str_TermsDays = arTermsDays.get(position);

                if(!str_TermsName.equals("Choose Net Terms")){
                    Double price =Double.valueOf(str_TermsDays);
                    DecimalFormat format = new DecimalFormat("#");

                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, Integer.parseInt(String.valueOf(format.format(price))));
                    dt = c.getTime();

                    SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
                    //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String OldDueDate= sd2.format(dt);

                    tv_dueDate.setText(OldDueDate);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = null;
                    try {
                        date = sdf.parse(OldDueDate);
                        DueDate =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CusID = extras.getString("CusID");

            OrderId = extras.getString("OrderId");
            CustomerName = extras.getString("CustomerName");
            SQLiteController sqLiteController2 = new SQLiteController(this);
            sqLiteController2.open();
            try {
                long count = sqLiteController2.fetchCount();
                if (count > 0) {
                    //Order
                    Cursor order_c = sqLiteController2.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderId);
                    if (order_c != null && order_c.moveToFirst()) {
                        do {
                            ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                            String totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                            String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                            submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                            String datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                            String pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                            upfreightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                            uptotaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                            updiscountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                            up_salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                            String id = order_c.getString(order_c.getColumnIndex("order_id"));
                            up_customerid = order_c.getString(order_c.getColumnIndex("customerid"));
                            if(!customerpricingsetup.equals("0")){
                                GetCustomerProducts(up_customerid);
                            }

                            warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                            String deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                            String shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                            String termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                            String memo = order_c.getString(order_c.getColumnIndex("memo"));
                            String customeristaxable = order_c.getString(order_c.getColumnIndex("customeristaxable"));
                            or_termsId = order_c.getString(order_c.getColumnIndex("or_termsId"));
                            System.out.println("or_termsId--"+or_termsId);
                            String or_taxId = order_c.getString(order_c.getColumnIndex("or_taxId"));
                            String poreferencenum = order_c.getString(order_c.getColumnIndex("poreferencenum"));


                           /* String warehouseid = order_c.getString(order_c.getColumnIndex("warehouseid"));
                            String deliveryshipmenttypeid = order_c.getString(order_c.getColumnIndex("deliveryshipmenttypeid"));

                            System.out.println("warehouseid--"+warehouseid);
                            System.out.println("deliveryshipmenttypeid--"+deliveryshipmenttypeid);*/

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(or_termsId.equals("null")){
                                        System.out.println("default_netterms--"+default_netterms);

                                        spONetTerms.setSelection(arTermsID.indexOf(default_netterms.toLowerCase()));
                                    }else {
                                        System.out.println("selectedTermID--"+or_termsId);

                                        spONetTerms.setSelection(arTermsID.indexOf(or_termsId));
                                    }

                                    if(customeristaxable.equals("true")){
                                        Str_CS ="true";
                                        CS_chkSelected.setChecked(true);
                                        if(!et_tax.getText().toString().equals("")){
                                            Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString())/100;
                                            tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));


                                            TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                                        }
                                    }else{
                                        Str_CS ="false";
                                        CS_chkSelected.setChecked(false);
                                        if(!et_tax.getText().toString().equals("")){
                                            Tax_Amt = TotalTaxSum * Double.parseDouble("0.00")/100;
                                            tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));


                                            TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                                        }
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    Date date1 = null;
                                    try {
                                        date = sdf.parse(submitdate);
                                        date1 = sdf.parse(datefulfilled);
                                        String OrderDate_str =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                        String DeliveryDate_strr =new SimpleDateFormat("MM/dd/yyyy").format(date1);
                                        et_orderDate.setText(OrderDate_str);
                                        et_deliveryDate.setText(DeliveryDate_strr);

                                        et_shippingCharge.setText(upfreightamount);
                                        et_discount.setText(updiscountpercentage);
                                        et_tax.setText(uptotaltaxbase);

                                        et_deliveryNotes.setText(deliverynote);
                                        et_terms.setText(termsconditions);
                                        et_packShipNote .setText(shipnote);
                                        et_memo .setText(memo);
                                        et_AddRef .setText(poreferencenum);



                                        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                                        Date date2 = null;
                                        try {
                                            date2 = sdf2.parse(DeliveryDate_strr);
                                            deliveryDate_str =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date2);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }



                                }
                            });



                        } while (order_c.moveToNext());
                    }

                    /*Cursor Product_c = sqLiteController2.readProductJoinTables(OrderId);
                    if (Product_c != null && Product_c.moveToFirst()) {
                        if(list.size()>0){
                            list.clear();
                        }
                        String priceTag ="false";
                        do {
                            @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                            @SuppressLint("Range") String OrderProduct_id = Product_c.getString(Product_c.getColumnIndex("pid"));
                            @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                            @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                            @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                            @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));


                            System.out.println("CustPrice---"+CustPrice.size());

                            for (int h = 0; h < CustPrice.size(); h++) {

                                if (CustPrice.get(h).getProductid().equals(productid)) {
                                    priceTag ="true";
                                }

                            }

                            //product
                            Cursor product_c1 = sqLiteController2.readAllTableProduct();
                            if (product_c1 != null && product_c1.moveToFirst()) {
                                String productImage ="null";
                                do {

                                    @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                    SQLiteController sqLiteController88= new SQLiteController(Activity_UpdateOrder.this);
                                    sqLiteController88.open();
                                    try {
                                        Cursor CursorAssertTable = sqLiteController88.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
                                        if (CursorAssertTable.moveToFirst()) {
                                            do {
                                                String Products_assets_default = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_default"));

                                                if(Products_assets_default.equals("true")){
                                                    productImage = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_file"));
                                                }


                                            } while (CursorAssertTable.moveToNext());
                                        }
                                        if(CursorAssertTable.getCount() == 0){
                                            productImage ="null";
                                        }
                                    } finally {
                                        sqLiteController88.close();
                                    }


                                    if(product_id.equals(productid)){
                                        @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                        @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                        @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                        double TotalAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);
                                        list.add(new Model_ItemList(productid, product_name__, productImage,quantity,String.valueOf(TotalAmt),priceperunit,OrderProduct_id,istaxable,all_upsc,all_description,priceTag));

                                        adapter.notifyDataSetChanged();

                                        if(list.size()>0){
                                            mRecyclerView.setVisibility(View.VISIBLE);
                                            txt_no_record.setVisibility(View.GONE);
                                        }else {
                                            mRecyclerView.setVisibility(View.GONE);
                                            txt_no_record.setVisibility(View.VISIBLE);
                                        }

                                        TotalSum =0.00;
                                        TotalTaxSum =0.0;

                                        for (int i = 0; i < list.size(); i++) {
                                            double TotalNewAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                            TotalSum = TotalSum + TotalNewAmt;
                                        }

                                        for (int i = 0; i < list.size(); i++) {

                                            if(list.get(i).getIstaxable().equals("true")){
                                                double TotalNewAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                                TotalTaxSum = TotalTaxSum + TotalNewAmt;
                                            }

                                        }


                                        txt_sub_total.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalSum));


                                        Dis_Amt = TotalSum * Double.parseDouble(updiscountpercentage)/100;
                                        tv_discount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Dis_Amt));


                                        Ship_Amt = Double.parseDouble(upfreightamount);
                                        tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Ship_Amt));


                                        if(Str_CS.equals("true")){
                                            if(!et_tax.getText().toString().isEmpty()){
                                                Tax_Amt = TotalTaxSum * Double.parseDouble(uptotaltaxbase)/100;
                                                tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));
                                            }else {
                                                Tax_Amt =0.0;
                                            }
                                        }else {
                                            Tax_Amt =0.0;
                                        }

                                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                        txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                                    }

                                } while (product_c1.moveToNext());
                            }

                        } while (Product_c.moveToNext());
                    }*/
                }
            } finally {
                sqLiteController2.close();
            }
        }

           if(list.size()>0){
            mRecyclerView.setVisibility(View.VISIBLE);
            txt_no_record.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            txt_no_record.setVisibility(View.VISIBLE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                EditText cart_product_quantity_text_View = view.findViewById(R.id.cart_product_quantity_text_View);
                EditText ItPrice_et = view.findViewById(R.id.ItPrice_et);
                AppCompatTextView orderAmt_tv = view.findViewById(R.id.orderAmt_tv);

                view.findViewById(R.id.tag_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewTooltip
                                .on(v)
                                .color(getResources().getColor(R.color.ColorButton))
                                .position(ViewTooltip.Position.BOTTOM)
                                .duration(500)
                                .text("Customer price applied for product")
                                .show();
                    }
                });


                ItPrice_et.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            if(Double.parseDouble(ItPrice_et.getText().toString())>0){
                                list.get(position).setPrice_PerUnit(ItPrice_et.getText().toString());

                                double TotalAmt = Double.parseDouble(list.get(position).getProduct_quantity()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                                orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));
                                Calculation();
                            }else {
                                list.get(position).setPrice_PerUnit("0");
                                orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                Calculation();
                            }

                        }else {
                            list.get(position).setPrice_PerUnit("0");
                            orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                            Calculation();
                        }


                    }
                });


                view.findViewById(R.id.cart_product_minus_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Integer.parseInt(cart_product_quantity_text_View.getText().toString())!=1){
                            cart_product_quantity_text_View.setText(String.valueOf(Integer.parseInt(cart_product_quantity_text_View.getText().toString()) - 1));

                            double TotalAmt = Double.parseDouble(cart_product_quantity_text_View.getText().toString()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                            orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                            list.get(position).setProduct_quantity(cart_product_quantity_text_View.getText().toString());
                            Calculation();
                        }

                    }
                });

                view.findViewById(R.id.cart_product_plus_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cart_product_quantity_text_View.setText(String.valueOf(Integer.parseInt(cart_product_quantity_text_View.getText().toString()) + 1));

                        double TotalAmt = Double.parseDouble(cart_product_quantity_text_View.getText().toString()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                        orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                        list.get(position).setProduct_quantity(cart_product_quantity_text_View.getText().toString());
                        Calculation();


                    }
                });

                view.findViewById(R.id.cart_product_delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        deleteList.add(list.get(position).getOrder_productid());
                        list.remove(position);
                        //adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();
                        Calculation();
                        Toast.makeText(Activity_UpdateOrder.this, "Item has been deleted successfully!", Toast.LENGTH_SHORT).show();

                    }
                });

                view.findViewById(R.id.AddchkSelected).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        if (cb.isChecked()) {
                            list.get(position).setIstaxable(String.valueOf(true));
                            adapter.notifyDataSetChanged();
                            Calculation();

                        } else if (!cb.isChecked()){
                            list.get(position).setIstaxable(String.valueOf(false));
                            adapter.notifyDataSetChanged();
                            Calculation();
                        }



                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        findViewById(R.id.barUpdate_rll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(Activity_UpdateOrder.this);
                integrator.setPrompt("Scan a barcode or QRcode");
                integrator.setOrientationLocked(true);
                integrator.initiateScan();

                //        Use this for more customization
                //        IntentIntegrator integrator = new IntentIntegrator(this);
                //        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                //        integrator.setPrompt("Scan a barcode");
                //        integrator.setCameraId(0);  // Use a specific camera of the device
                //        integrator.setBeepEnabled(false);
                //        integrator.setBarcodeImageEnabled(true);
                //        integrator.initiateScan();
            }
        });

        CS_chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    Str_CS = "true";
                    if(!et_tax.getText().toString().equals("")){
                        Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString())/100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));


                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                        txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                    }

                } else if (!cb.isChecked()){
                    Str_CS = "false";

                    if(!et_tax.getText().toString().equals("")){
                        Tax_Amt = TotalTaxSum * Double.parseDouble("0.00")/100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));


                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                        txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                    }
                }

            }
        });

/*
        findViewById(R.id.addMultiple_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMultipleItemAlert();
                //  startActivity(new Intent(Activity_CreateOrder.this, Activity_AddItem.class));
            }
        });
*/


        findViewById(R.id.et_orderDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                showDialog(DATE_PICKER1_ID);
            }
        });
        findViewById(R.id.et_deliveryDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                showDialog(DATE_PICKER2_ID);
            }
        });
        findViewById(R.id.et_pricingDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                showDialog(DATE_PICKER3_ID);
            }
        });

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alert.equals("true")){
                    alert="false";
                    AddMultipleItemAlert();
                }
               // AddItemAlert();
              //  startActivity(new Intent(Activity_CreateOrder.this, Activity_AddItem.class));
            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_UpdateOrder.this, Activity_OrdersHistory.class));
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_UpdateOrder.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_UpdateOrder.this, Activity_InvoiceHistory.class));
            }
        });

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate1 = sdf.format(date1);

        Date date6 = new Date();
        SimpleDateFormat sdf6 = new SimpleDateFormat("ddMMyy");
        //sdf6.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentForDate = sdf6.format(date6);

        findViewById(R.id.btn_orderComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_discount.getText().toString())) {
                    DisString =  et_discount.getText().toString();
                }else{
                    DisString ="0";
                }
                RefString =  et_AddRef.getText().toString();
                String sFrom = et_orderDate.getText().toString();
                String sTo = et_deliveryDate.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    fromDate = sdf.parse(sFrom);
                    toDate = sdf.parse(sTo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                if (et_orderDate.getText().toString().equals("MM/DD/YYYY")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Order Date!");
                } else if (str_customer.equals("Choose Customer")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Customer!");
                }  else if (str_salesRep.equals("Choose Sales Rep")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Sales Rep!");
                } else if (et_deliveryDate.getText().toString().equals("MM/DD/YYYY")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Delivery Date!");
                }else if (str_fulfilment.equals("Choose Fulfillment Type")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Fulfillment Type!");
                } /*else if (str_warehouse.equals("Choose Warehouse")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Warehouse!");
                }*//*else if (Str_TermsID.equals("0")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Net Terms!");
                }else if (DueDate.equals("MM/DD/YYYY")) {
                    Utils.getInstance().snackBarMessage(v,"Choose Due Date!");
                }*/else if (Utils.checkDates(fromDate,toDate)) {
                    Utils.getInstance().snackBarMessage(v,"Delivery Date should be greater than or equal to ordered date!");
                }else if (TotalSum ==0.00) {
                    Utils.getInstance().snackBarMessage(v,"Sub total amount greater than zero!");
                }else if (bill_tv.getText().toString().equals("No address found!")) {
                    Utils.getInstance().snackBarMessage(v,"Please select billing address!");
                }else if (del_tv.getText().toString().equals("No address found!")) {
                    Utils.getInstance().snackBarMessage(v,"Please select Shipping  address");
                } else if (Double.parseDouble(DisString) > 100){
                    Utils.getInstance().snackBarMessage(v,"Enter the valid discount %");
                } else if (TotalPayableAmt ==0.00) {
                    Utils.getInstance().snackBarMessage(v,"Select the Products!");
                } else {

                    if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                        //Hide
                        wrBln =true;
                    }else {
                        //Show
                        if (str_warehouse.equals("Choose Warehouse")) {
                            Utils.getInstance().snackBarMessage(v,"Choose Warehouse!");
                            wrBln =false;
                        }else {
                            wrBln =true;
                        }
                    }


                    if(settingsvalue.contains("Order")) {
                        if(str_fulfilment.equals("Direct delivery")){
                            if(str_DeliveryPName.equals("Choose Delivery Person")) {
                                Utils.getInstance().snackBarMessage(v,"Choose Delivery Person!");
                                wrBln =false;
                            }else {
                                wrBln =true;
                            }

                        }
                    }

                    Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                    if(wrBln){
                        if (Connectivity.isConnected(Activity_UpdateOrder.this) &&
                                Connectivity.isConnectedFast(Activity_UpdateOrder.this)) {
                            JSONObject postedJSON = new JSONObject();
                            JSONArray array =new JSONArray();

                                for (int i = 0; i < list.size(); i++ ){

                                double TotalBaseAmt = Double.parseDouble(list.get(i).getPrice_PerUnit().trim()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                postedJSON = new JSONObject();
                                try {

                                    if(list.get(i).getOrder_productid().equals("")){
                                        postedJSON.put("id",null);
                                    }else {
                                        postedJSON.put("id",list.get(i).getOrder_productid().trim());
                                    }

                                    System.out.println("ProductName--"+list.get(i).getProduct_name().trim());

                                    postedJSON.put("isactive", true);
                                    postedJSON.put("productid", list.get(i).getProduct_id().trim());
                                    postedJSON.put("name", list.get(i).getProduct_name().trim());
                                    postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                                    postedJSON.put("quantityonhand", "0");
                                    postedJSON.put("orderquantity", list.get(i).getProduct_quantity().trim());
                                    postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                                    postedJSON.put("priceperunit",  list.get(i).getPrice_PerUnit().trim());
                                    postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                                    postedJSON.put("delete", "null");
                                    postedJSON.put("warehouseid", str_warehouseID);//warehouseid
                                    postedJSON.put("modifiedby", user_id);
                                    postedJSON.put("modifiedon", currentDate1);
                                    postedJSON.put("isHigh", true);
                                    postedJSON.put("quantity", list.get(i).getProduct_quantity().trim());//quantity
                                    postedJSON.put("available", "0");
                                    postedJSON.put("committed", "0");
                                    if(list.get(i).getIstaxable().equals("true")){
                                        postedJSON.put("itemistaxable", true);
                                    }else {
                                        postedJSON.put("itemistaxable", false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                array.put(postedJSON);
                            }

                            JSONObject json = new JSONObject();
                            try {
                                json.put("id",OrderId);
                                json.put("billtoaddressid",str_BilingID);
                                json.put("customerid",str_customerID);
                                json.put("datefulfilled",deliveryDate_str);
                                json.put("discountamount",String.valueOf(Dis_Amt));//discount vara amout
                                if(et_discount.getText().toString().equals("")){
                                    json.put("discountpercentage","0");
                                }else {
                                    json.put("discountpercentage",et_discount.getText().toString());
                                }
                                if(et_shippingCharge.getText().toString().equals("")){
                                    json.put("freightamount","0");
                                }else {
                                    json.put("freightamount",et_shippingCharge.getText().toString());
                                }
                                if(et_tax.getText().toString().equals("")){
                                    json.put("totaltaxbase","0");
                                }else {
                                    json.put("totaltaxbase",et_tax.getText().toString().trim());
                                }
                                json.put("ordernumber",ordernumber);// order number
                                json.put("shiptoaddressid",str_DeliveryID);//shipID
                                json.put("submitdate",currentDate1);
                                json.put("submitstatus",submitstatus);
                                json.put("totalamount",String.valueOf(TotalPayableAmt)); //totalpayyable
                                json.put("totallineitemamount",String.valueOf(TotalSum));
                                json.put("totaltax",String.valueOf(Tax_Amt));
                                json.put("poreferencenum",RefString);
                                json.put("salesrepid",str_salesRepID);
                                json.put("pricingdate",currentDate1);
                                json.put("isactive", true);//boolean true
                                json.put("deliverynote",et_deliveryNotes.getText().toString());
                                json.put("shipnote",et_packShipNote.getText().toString());
                                json.put("termsconditions",et_terms.getText().toString());
                                json.put("memo",et_memo.getText().toString());
                                json.put("warehouseid",str_warehouseID);
                                json.put("deletedRows","");
                                json.put("modifiedon",currentDate1);
                                json.put("draftnumber","null");
                                json.put("lastsyncon","null");
                             /*   json.put("taxid",str_TaxID);
                                json.put("netterms",Str_TermsID);
                                json.put("duedate",DueDate);*/

                                if(deleteList.size()>0){

                                    StringBuilder sb = new StringBuilder();
                                    for(String item: deleteList){
                                        if(sb.length() > 0){
                                            sb.append(',');
                                        }
                                        sb.append(item);
                                    }
                                    String result = sb.toString();

                                    json.put("deletedRows",result);

                                    System.out.println("deletedRows---"+result.toString());

                                }else {
                                   // json.put("deletedRows","null");
                                }



                                if(str_TaxID.equals("00000000-0000-0000-0000-000000000000")){
                                    json.put("taxid",null);
                                }else{
                                    json.put("taxid",str_TaxID);
                                }

                                if(Str_TermsID.equals("0")){
                                    json.put("netterms",null);
                                }else{
                                    json.put("netterms",Str_TermsID);
                                }

                                if(DueDate.equals("MM/DD/YYYY")){
                                    json.put("duedate",null);
                                }else {
                                    json.put("duedate",DueDate);
                                }
                                json.put("orderitem",array);
                                if(Str_CS.equals("true")){
                                    json.put("customeristaxable", true);
                                }else {
                                    json.put("customeristaxable", false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("PresaleUpdate---"+json.toString());

                            PostUpdatePresale(json.toString());

                        }else {
                       /* if(OrderId.startsWith("DRF")){

                        }else {
                            Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                        }*/
                            SQLiteController sqLiteController = new SQLiteController(Activity_UpdateOrder.this);
                            sqLiteController.open();
                            try {

                                int OrderCount_ =0;
                                if(getOrderDate().equals(currentForDate)){
                                    OrderCount_ =Integer.parseInt(getOrderCount());
                                }else {
                                    OrderCount_ =Integer.parseInt("0");
                                }

                                OrderCount_ += 1;
                                tkSharedPreferences = getSharedPreferences(tkPref,MODE_PRIVATE);
                                SharedPreferences.Editor editor = tkSharedPreferences.edit();
                                editor.putString(order_Count,  String.valueOf(OrderCount_));
                                editor.putString(order_CrDate,  String.valueOf(currentForDate));
                                editor.apply();

                                String OrderCount = new StringBuilder()
                                        .append("DRFOM-")
                                        .append(currentForDate)
                                        .append("-")
                                        .append("0000")
                                        .append(OrderCount_).toString();

                                String ets_dis="0";
                                String ets_ship="0";
                                String ets_tax="0";
                                String OrderVStaus="UpdateOrder";
                                String Sdeleterows="0";

                                if(et_discount.getText().toString().equals("")){
                                    ets_dis ="0";

                                }else {
                                    ets_dis =et_discount.getText().toString();

                                }
                                if(et_shippingCharge.getText().toString().equals("")){
                                    ets_ship ="0";
                                }else {
                                    ets_ship =et_shippingCharge.getText().toString();
                                }

                                if(et_tax.getText().toString().equals("")){
                                    ets_tax ="0";
                                }else {
                                    ets_tax =et_tax.getText().toString();
                                }

                                if(OrderId.startsWith("DRFOM")){
                                    OrderVStaus ="CreateOrder";
                                }else {
                                    OrderVStaus ="UpdateOrder";
                                }

                                if(deleteList.size()>0){

                                    StringBuilder sb = new StringBuilder();
                                    for(String item: deleteList){
                                        if(sb.length() > 0){
                                            sb.append(',');
                                        }
                                        sb.append(item);
                                    }
                                    Sdeleterows = sb.toString();

                                    System.out.println("deletedRows---"+Sdeleterows.toString());

                                }else {
                                    // json.put("deletedRows","null");
                                }

                                sqLiteController.UpdateOrders(
                                        OrderId,
                                        OrderVStaus,
                                        str_BilingID,
                                        str_customerID,
                                        "",
                                        deliveryDate_str,
                                        "",
                                        String.valueOf(Dis_Amt),
                                        "",
                                        "",
                                        "",
                                        "",
                                        ets_dis,
                                        ets_ship,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ordernumber,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "00000000-0000-0000-0000-000000000000",
                                        "",
                                        "",
                                        "",
                                        str_DeliveryID,
                                        "",
                                        currentDate1,
                                        submitstatus,
                                        "",
                                        String.valueOf(TotalPayableAmt),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        String.valueOf(TotalSum),
                                        "",
                                        "",
                                        "",
                                        String.valueOf(Tax_Amt),
                                        ets_tax,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        Sdeleterows,
                                        "",
                                        "",
                                        user_id,
                                        "",
                                        currentDate1,
                                        "",
                                        "",
                                        currentDate1,
                                        "",
                                        str_salesRepID,
                                        currentDate1,
                                        "",
                                        "",
                                        "",
                                        String.valueOf(true),
                                        et_deliveryNotes.getText().toString(),
                                        et_packShipNote.getText().toString(),
                                        et_terms.getText().toString(),
                                        et_memo.getText().toString(),
                                        "",
                                        "",
                                        "",
                                        RefString,
                                        "",
                                        "",
                                        "",
                                        str_warehouseID,Str_CS,"Offline","non_Sync","null",OrderCount,str_fulfilmentID,str_TaxID,Str_TermsID,DueDate);


                                sqLiteController.deleteProductItems(String.valueOf(OrderId));

                                for (int i = 0; i < list.size(); i++ ){

                                    double TotalBaseAmt = Double.parseDouble(list.get(i).getPrice_PerUnit().trim()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                    sqLiteController.insertProducts(list.get(i).getProduct_id().trim(), String.valueOf(OrderId),list.get(i).getProduct_id().trim(),list.get(i).getPrice_PerUnit().trim(),String.valueOf(TotalBaseAmt),list.get(i).getProduct_quantity().trim(),list.get(i).getProduct_name().trim(),list.get(i).getIstaxable(),str_warehouseID);

                                }

                            } finally {
                                Intent intent = new Intent(Activity_UpdateOrder.this, com.example.arcomdriver.order.Activity_OrdersHistory.class);
                                startActivity(intent);
                                Toast.makeText(Activity_UpdateOrder.this, "Presale Order Updated Successfully", Toast.LENGTH_SHORT).show();
                                sqLiteController.close();
                                Utils.getInstance().dismissDialog();
                            }


                        }
                    }

                }
            }
        });


        SQLiteController sqLiteController3 = new SQLiteController(Activity_UpdateOrder.this);
        sqLiteController3.open();
        try {

            Cursor shipment_c = sqLiteController3.readTableShipment();
            if (shipment_c != null && shipment_c.moveToFirst()) {
                do {
                    @SuppressLint("Range") String orderid = shipment_c.getString(shipment_c.getColumnIndex("orderid"));
                    if(OrderId.equals(orderid)){

                        shipmenttypeid = shipment_c.getString(shipment_c.getColumnIndex("shipmenttypeid"));
                        deliveryshipmenttypeid = shipment_c.getString(shipment_c.getColumnIndex("deliveryshipmenttypeid"));
                        System.out.println("deliveryshipmenttypeid==="+deliveryshipmenttypeid);
                        deliveryagentid = shipment_c.getString(shipment_c.getColumnIndex("deliveryagentid"));


                    }


                } while (shipment_c.moveToNext());


            }
        } finally {
            sqLiteController3.close();
        }




        findViewById(R.id.tv_customerSp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinnerDialog();
            }
        });


      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arCustomerName);
        arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spCustomer.setAdapter(arrayAdapter);*/

        if(!CusID.equals("null") && !CusID.equals("1") ){

            for (int i = 0; i < arCustomerID.size(); i++) {
                String  SCusID = arCustomerID.get(i);
                if(SCusID.contains(CusID)) {

                    str_customer = arCustomerName.get(i);
                    str_customerID = arCustomerID.get(i);

                    getCustomerInfo(i,"1");

                    tv_customerSp.setText(str_customer);

                    System.out.println("str_customer---"+str_customer);
                    System.out.println("str_customerID---"+str_customerID);

                    if(!customerpricingsetup.equals("0")){
                        GetCustomerProducts(str_customerID);
                    }



                }
            }

            //spCustomer.setSelection(arCustomerID.indexOf(CusID));

        }else {
            for (int i = 0; i < arCustomerID.size(); i++) {
                String  SCusID = arCustomerID.get(i);
                if(SCusID.contains(up_customerid)) {

                    str_customer = arCustomerName.get(i);
                    str_customerID = arCustomerID.get(i);

                    getCustomerInfo(i,"2");

                    tv_customerSp.setText(str_customer);

                    System.out.println("str_customer---"+str_customer);
                    System.out.println("str_customerID---"+str_customerID);


                }
            }

            //spCustomer.setSelection(arCustomerID.indexOf(up_customerid));
        }

    /*    spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_customer = arCustomerName.get(position);
                str_customerID = arCustomerID.get(position);

                getCustomerInfo(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });*/

        ArrayAdapter<String> arrayAdapter_d= new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arDeliveryPersonName);
        arrayAdapter_d.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        sp_DeliveryPerson.setAdapter(arrayAdapter_d);

        sp_DeliveryPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_DeliveryPName = arDeliveryPersonName.get(position);
                str_DeliveryPID = arDeliveryPersonID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });





        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arSalesRepName);
        arrayAdapter2.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        sp_salesRep.setAdapter(arrayAdapter2);
        sp_salesRep.setSelection(arSalesRepID.indexOf(up_salesrepid));
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

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arFulfilmentName);
        arrayAdapter3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spFulfillmentType.setAdapter(arrayAdapter3);
        spFulfillmentType.setSelection(arFulfillID.indexOf(deliveryshipmenttypeid));
        spFulfillmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_fulfilment = arFulfilmentName.get(position);
                str_fulfilmentID = arFulfillID.get(position);

                if(settingsvalue.contains("Order")) {
                    if(!(str_fulfilment ==null)){
                        if(str_fulfilment.equals("Direct delivery")){
                            deliveryPerson_ll.setVisibility(View.VISIBLE);
                            sp_DeliveryPerson.setSelection(arDeliveryPersonID.indexOf(deliveryagentid));
                        }

                        if(str_fulfilment.equals("Customer pickup")){
                            deliveryPerson_ll.setVisibility(View.GONE);
                        }
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arWarehouseName);
        arrayAdapter4.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spWarehouse.setAdapter(arrayAdapter4);
        spWarehouse.setSelection(arWarehouseID.indexOf(warehouseid));
        spWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_warehouse = arWarehouseName.get(position);
                str_warehouseID = arWarehouseID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        et_discount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if(TotalSum >0){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dis_Amt = TotalSum * Double.parseDouble(et_discount.getText().toString())/100;
                                tv_discount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Dis_Amt));

                                TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;

                                txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
                            }
                        });

                    }else {
                        tv_discount.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                        // et_discount.setText("0");
                    }

                }else {
                    tv_discount.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dis_Amt =0.0;
                            TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - 0.0;
                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
                        }
                    });
                    //et_discount.setText("0");
                }


            }
        });
        et_shippingCharge.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if(TotalSum >0){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Ship_Amt = Double.parseDouble(et_shippingCharge.getText().toString());
                                tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Ship_Amt));

                                TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
                            }
                        });

                    }else {
                        tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+"0.00");

                        // et_shippingCharge.setText("0");
                    }

                }else {
                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Ship_Amt =0.0;
                            TotalPayableAmt = TotalSum + Tax_Amt + 0.0 - Dis_Amt;
                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
                        }
                    });
                    //et_shippingCharge.setText("0");
                }


            }
        });
        et_tax.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if(TotalTaxSum >0){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(Str_CS.equals("true")){
                                    Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString())/100;
                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));


                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                                }else {
                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                }
                                }
                        });

                    }else {
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                        //et_tax.setText("0");
                    }

                }else {
                    tv_tax.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tax_Amt =0.0;
                            TotalPayableAmt = TotalSum + 0.0 + Ship_Amt - Dis_Amt;
                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
                        }
                    });
                }


            }
        });


    }

    private void getCustomerInfo(int cs_pos,String type) {

        if(!str_customer.equals("Choose Customer")){
            str_BilingID = arBillingID.get(cs_pos);
            str_DeliveryID = arDeliveryID.get(cs_pos);
            str_Taxable = arTaxable.get(cs_pos);
            str_TaxID = arTaxID.get(cs_pos);
            String selectedTermID = arTerms.get(cs_pos);

            System.out.println("CUSTERMER_TAX----"+default_netterms);

            if(str_TaxID.equals("null")){
                str_TaxID="00000000-0000-0000-0000-000000000000";
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(type.equals("2")){

                    }else {
                        if(selectedTermID.equals("null")){
                        System.out.println("default_netterms--"+default_netterms);
                        spONetTerms.setSelection(arTermsID.indexOf(default_netterms.toLowerCase()));
                    }else {
                        System.out.println("selectedTermID--"+or_termsId);
                        spONetTerms.setSelection(arTermsID.indexOf(or_termsId));
                    }

                    }


                    //OrdersSpinner
                    SQLiteController sqLiteControllerC = new SQLiteController(Activity_UpdateOrder.this);
                    sqLiteControllerC.open();
                    try {
                        long fetchAddressCount = sqLiteControllerC.fetchAddressCount();
                        if (fetchAddressCount > 0) {
                            Cursor C_Address = sqLiteControllerC.readTableAddress();
                            if (C_Address.moveToFirst()) {
                                do {
                                    @SuppressLint("Range") String BId = C_Address.getString(C_Address.getColumnIndex("address_id"));
                                    @SuppressLint("Range") String line2 = C_Address.getString(C_Address.getColumnIndex("line2"));
                                    @SuppressLint("Range") String stateorprovince = C_Address.getString(C_Address.getColumnIndex("stateorprovince"));
                                    @SuppressLint("Range") String city = C_Address.getString(C_Address.getColumnIndex("city"));
                                    @SuppressLint("Range") String postalcode = C_Address.getString(C_Address.getColumnIndex("postalcode"));
                                    @SuppressLint("Range") String country = C_Address.getString(C_Address.getColumnIndex("country"));

                                    if(str_BilingID.equals(BId)){

                                        bill_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                    }

                                    if(str_DeliveryID.equals(BId)){
                                        del_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                    }



                                } while (C_Address.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteControllerC.close();
                    }



                    taxpercentage = "0.00";

                    SQLiteController sqLiteController_tx = new SQLiteController(Activity_UpdateOrder.this);
                    sqLiteController_tx.open();
                    try {
                        long fetchCsTaxCount = sqLiteController_tx.fetchCsTaxCount();
                        if (fetchCsTaxCount > 0) {

                            Cursor Cs_tax = sqLiteController_tx.readTableCs_Tax();
                            if (Cs_tax.moveToFirst()) {
                                do {
                                    @SuppressLint("Range") String cs_tax_id = Cs_tax.getString(Cs_tax.getColumnIndex("cs_tax_id"));

                                    if(str_TaxID.equals(cs_tax_id)) {

                                        @SuppressLint("Range") String cs_tax_percent = Cs_tax.getString(Cs_tax.getColumnIndex("cs_tax_percent"));
                                        et_tax.setText(Utils.truncateDecimal(Double.parseDouble(cs_tax_percent)));
                                        Calculation();

                                    }

                                } while (Cs_tax.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteController_tx.close();
                    }


                }
            });


        }
    }

    private void Calculation() {

        if(list.size()>0){
            mRecyclerView.setVisibility(View.VISIBLE);
            txt_no_record.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            txt_no_record.setVisibility(View.VISIBLE);
        }

        TotalSum =0.00;
        TotalTaxSum =0.0;

        for (int i = 0; i < list.size(); i++) {
            double TotalAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

            TotalSum = TotalSum + TotalAmt;
        }

        for (int i = 0; i < list.size(); i++) {

            if(list.get(i).getIstaxable().equals("true")){
                double TotalAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                TotalTaxSum = TotalTaxSum + TotalAmt;
            }

        }

        txt_sub_total.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalSum));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(!et_discount.getText().toString().isEmpty()){
                    Dis_Amt = TotalSum * Double.parseDouble(et_discount.getText().toString())/100;
                    tv_discount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Dis_Amt));
                }else {
                    Dis_Amt =0.0;
                }


                if(!et_shippingCharge.getText().toString().isEmpty()){
                    Ship_Amt = Double.parseDouble(et_shippingCharge.getText().toString());
                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Ship_Amt));

                }else {
                    Ship_Amt =0.0;
                }

                if(Str_CS.equals("true")){
                    if(!et_tax.getText().toString().isEmpty()){
                        Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString())/100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));
                    }else {
                        Tax_Amt =0.0;
                    }
                }

                TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;

                txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));
            }
        });

       // et_tax.setText("0");
       // et_shippingCharge.setText("0");
      //  et_discount.setText("0");

    }

    public void AddItemAlert() {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateOrder.this);
                LayoutInflater inflater = Activity_UpdateOrder.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_additemdislog,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                spProduct = v.findViewById(R.id.spProduct);
                AppCompatImageView pro_image_view = v.findViewById(R.id.pro_image_view);
                AppCompatEditText et_qty = v.findViewById(R.id.et_qty);
                AppCompatTextView tv_pricePerUnit = v.findViewById(R.id.tv_pricePerUnit);
                AppCompatTextView et_amt = v.findViewById(R.id.et_amt);
                AppCompatTextView et_package = v.findViewById(R.id.et_package);

                et_qty.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            if(Double.parseDouble(str_ProductPrice) >0){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        double TotalAmt = Double.parseDouble(str_ProductPrice) * Double.parseDouble(et_qty.getText().toString());
                                        et_amt.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));
                                    }
                                });

                            }else {
                                et_amt.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                            }

                        }else {
                            et_amt.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                        }


                    }
                });

                ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arProductName);
                arrayAdapter5.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                spProduct.setAdapter(arrayAdapter5);
                spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_product = arProductName.get(position);
                        str_ProductUrl = arProductImage.get(position);
                        str_ProductPrice = arProduct_price.get(position);
                        str_ProductID = arProductID.get(position);
                        str_Taxable = arProduct_tax.get(position);
                        Str_UpscCode = arProduct_upsc.get(position);
                        Str_dec = arProduct_dec.get(position);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(str_ProductID.equals("0")){
                                    pro_image_view.setBackgroundResource(R.drawable.image_placeholder);
                                }else {
                                  /*  byte[] decodedString = Base64.decode(str_ProductUrl, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    pro_image_view.setImageBitmap(decodedByte);*/

                                    String imageUri = Const.ImageProducts +str_ProductUrl;
                                    Picasso.with(getApplicationContext()).load(imageUri).fit().centerInside()
                                            .placeholder(R.drawable.image_placeholder)
                                            .error(R.drawable.image_placeholder)
                                            .into(pro_image_view);

                                    tv_pricePerUnit.setText(str_ProductPrice);
                                    double TotalAmt = Double.parseDouble(str_ProductPrice) * Double.parseDouble(et_qty.getText().toString());
                                    et_amt.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));
                                }

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });


                v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (str_ProductID.equals("0")) {
                                    Utils.getInstance().snackBarMessage(v,"Choose Product!");
                                }else if (Utils.getInstance().checkIsEmpty(et_qty)) {
                                    Utils.getInstance().snackBarMessage(v,"Enter the Quantity!");
                                }else {
                                    double TotalAmt = Double.parseDouble(str_ProductPrice) * Double.parseDouble(et_qty.getText().toString());
                                    list.add(new Model_ItemList(str_ProductID, str_product, str_ProductUrl,et_qty.getText().toString(),String.valueOf(TotalAmt),str_ProductPrice,"",str_Taxable,Str_UpscCode,Str_dec,"false"));

                                    adapter.notifyDataSetChanged();
                                    Calculation();

                                    Toast.makeText(Activity_UpdateOrder.this, "Product has been saved successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }



                            }});
                    }
                });

                v.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                // datePickerDialog.getDatePicker().setMaxDate(d.getTimeInMillis() - 1000);
                return datePickerDialog;
            case DATE_PICKER2_ID:
                datePickerDialog = new DatePickerDialog(this, pickerListener2, mYear, mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate (System.currentTimeMillis() - 1000);
                return datePickerDialog;

            case DATE_PICKER3_ID:
                datePickerDialog = new DatePickerDialog(this, pickerListener3, mYear, mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate (System.currentTimeMillis() - 1000);
                return datePickerDialog;
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et_orderDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());
        }
    };
    private DatePickerDialog.OnDateSetListener pickerListener3= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et_pricingDate.setText(new StringBuilder()
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


            et_deliveryDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date = null;
            try {
                date = sdf.parse(et_deliveryDate.getText().toString());
                deliveryDate_str =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    };

    private void PostUpdatePresale(String json) {

        Progress_dialog.show();

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate1 = sdf.format(date1);

        try {
            App.getInstance().PostUpdatePresale(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    String data = jsonObject.getString("data");

                                    if ( succeeded == true) {
                                        JSONObject json = new JSONObject();
                                        try {
                                            if (Activity_OrdersHistory.WarehouseFlag.equals("false")) {
                                                //Hide
                                                if(str_fulfilmentID.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                                }else {
                                                    json.put("shipmenttypeid",null);
                                                }
                                            }else {
                                                json.put("shipmenttypeid","87b9ad6e-fda7-4c6d-87e6-793c6cd0a964");
                                                if(str_fulfilmentID.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                                    json.put("deliverytype","fulfillmentwarehouse");
                                                }else if(str_fulfilmentID.equals("474bece4-7bc2-43a0-a7c4-e74b1de134f1")){
                                                    json.put("deliverytype","customerpickup");
                                                }
                                            }
                                            if(str_fulfilmentID.equals("87b9ad6e-fda7-4c6d-87e6-793c6cd0a964")){
                                                json.put("deliveryshipmenttypeid",null);
                                            }else {
                                                json.put("deliveryshipmenttypeid",str_fulfilmentID);
                                            }
                                            json.put("isInsert",true);
                                            json.put("isactive",true);
                                            json.put("fulfillmentstatus","New Order");
                                            json.put("requestdeliveryby",user_id);
                                            json.put("attempt","1");
                                            json.put("creratedon",currentDate1);
                                            json.put("orderid",data);

                                            if(str_fulfilment.equals("Direct delivery")){
                                                json.put("deliveryagentid",str_DeliveryPID);
                                            }


                                            System.out.println("Shipment Status Update----"+json.toString());

                                            postCreateFull(json.toString(),data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
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

    private void postCreateFull(String json,String data) {
        try {
            App.getInstance().PostInvoiceSummaryFul(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {

                                        Utils.getInstance().GetMasterInsert(Activity_UpdateOrder.this,"UpdateOrder",Progress_dialog,"");

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


    public void AddMultipleItemAlert() {

        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                stList = new ArrayList<Model_ItemProducts>();
                filteredModelList = new ArrayList<Model_ItemProducts>();
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateOrder.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateOrder.this);
                LayoutInflater inflater = Activity_UpdateOrder.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_addmultipleitemdislog,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();

                searchBar = (SearchView) v.findViewById(R.id.search_product);
                chkAllSelected = (CheckBox) v.findViewById(R.id.chkAllSelected);
                chkAllSelected.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        if (cb.isChecked()) {
                            for (int i = 0; i < stList.size(); i++) {
                                Model_ItemProducts singleStudent = stList.get(i);
                                singleStudent.setSelected(true);

                                stList.get(i).setProduct_quantity("1");
                               // mAdapter.notifyItemChanged(i);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else if (!cb.isChecked()){
                            for (int i = 0; i < stList.size(); i++) {
                                Model_ItemProducts singleStudent = stList.get(i);
                                singleStudent.setSelected(false);

                                stList.get(i).setProduct_quantity("0");
                                //mAdapter.notifyItemChanged(i);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                txt_no_record2 = (AppCompatTextView) v.findViewById(R.id.txt_no_record2);
                mRecyclerProducts = (RecyclerView) v.findViewById(R.id.ry_productslist);
                mAdapter = new Adapter_ItemProducts(stList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_UpdateOrder.this);
                mRecyclerProducts.setLayoutManager(mLayoutManager);
                mRecyclerProducts.setItemAnimator(new DefaultItemAnimator());
                mRecyclerProducts.addItemDecoration(new ItemDividerDecoration(Activity_UpdateOrder.this, LinearLayoutManager.VERTICAL));
                // set the adapter object to the Recyclerview
                mRecyclerProducts.setAdapter(mAdapter);
                mRecyclerProducts.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_UpdateOrder.this, mRecyclerProducts, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        EditText MtQty_et = view.findViewById(R.id.MtQty_et);
                        EditText p4_et = view.findViewById(R.id.p4_tv);
                        AppCompatTextView mtAmount_tv = view.findViewById(R.id.mtAmount_tv);


                        if(filteredModelList.size()>0){

                            p4_et.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {}

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    if (!TextUtils.isEmpty(s)) {
                                        if(Double.parseDouble(p4_et.getText().toString())>0){
                                            filteredModelList.get(position).setPrice_PerUnit(p4_et.getText().toString());
                                            //notifyDataSetChanged();
                                            double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));
                                        }else {
                                            filteredModelList.get(position).setPrice_PerUnit("0");
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                        }

                                    }else {
                                        filteredModelList.get(position).setPrice_PerUnit("0");
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                    }


                                }
                            });

                            view.findViewById(R.id.MtProduct_minus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(Integer.parseInt(MtQty_et.getText().toString())!=0){
                                        filteredModelList.get(position).setTotalItem_price(String.valueOf(0.00));
                                        MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) - 1));

                                        double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                                        filteredModelList.get(position).setTotalItem_price(String.valueOf(TotalAmt));
                                        filteredModelList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                        if(Integer.parseInt(MtQty_et.getText().toString())==0){
                                            filteredModelList.get(position).setSelected(false);
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    }

                                }
                            });

                            view.findViewById(R.id.MtProduct_plus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    filteredModelList.get(position).setTotalItem_price(String.valueOf(0.00));
                                    MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) + 1));

                                    double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                    mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                                    filteredModelList.get(position).setTotalItem_price(String.valueOf(TotalAmt));
                                    filteredModelList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                    if(Integer.parseInt(MtQty_et.getText().toString())==1){
                                        filteredModelList.get(position).setSelected(true);
                                        mAdapter.notifyItemChanged(position);
                                    }

                                }
                            });

                            view.findViewById(R.id.AddchkSelected).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CheckBox cb = (CheckBox) v;

                                    if (cb.isChecked()) {
                                        filteredModelList.get(position).setIstaxable(String.valueOf(true));
                                        mAdapter.notifyDataSetChanged();

                                    } else if (!cb.isChecked()){
                                        filteredModelList.get(position).setIstaxable(String.valueOf(false));
                                        mAdapter.notifyDataSetChanged();
                                    }



                                }
                            });


                        }else {

                            p4_et.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {}

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    if (!TextUtils.isEmpty(s)) {
                                        if(Double.parseDouble(p4_et.getText().toString())>0){
                                            stList.get(position).setPrice_PerUnit(p4_et.getText().toString());
                                            //notifyDataSetChanged();
                                            double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));
                                        }else {
                                            stList.get(position).setPrice_PerUnit("0");
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                        }

                                    }else {
                                        stList.get(position).setPrice_PerUnit("0");
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+"0.00");
                                    }


                                }
                            });

                            view.findViewById(R.id.MtProduct_minus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(Integer.parseInt(MtQty_et.getText().toString())!=0){
                                        stList.get(position).setTotalItem_price(String.valueOf(0.00));
                                        MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) - 1));

                                        double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                                        stList.get(position).setTotalItem_price(String.valueOf(TotalAmt));
                                        stList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                        if(Integer.parseInt(MtQty_et.getText().toString())==0){
                                            stList.get(position).setSelected(false);
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    }

                                }
                            });

                            view.findViewById(R.id.MtProduct_plus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    stList.get(position).setTotalItem_price(String.valueOf(0.00));
                                    MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) + 1));

                                    double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                    mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalAmt));

                                    stList.get(position).setTotalItem_price(String.valueOf(TotalAmt));
                                    stList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                    if(Integer.parseInt(MtQty_et.getText().toString())==1){
                                        stList.get(position).setSelected(true);
                                        mAdapter.notifyItemChanged(position);
                                    }

                                }
                            });

                            view.findViewById(R.id.AddchkSelected).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CheckBox cb = (CheckBox) v;

                                    if (cb.isChecked()) {
                                        stList.get(position).setIstaxable(String.valueOf(true));
                                        mAdapter.notifyDataSetChanged();

                                    } else if (!cb.isChecked()){
                                        stList.get(position).setIstaxable(String.valueOf(false));
                                        mAdapter.notifyDataSetChanged();
                                    }



                                }
                            });


                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));


                v.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert="true";
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert="true";
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {

                        String Product_id = "";
                        String Product_name = "";
                        String Product_imageurl = "";
                        String Product_quantity = "";
                        String Product_price = "";
                        String istaxable = "";
                        String TotalItem_price = "";
                        String Price_PerUnit = "";
                        String UpscCode = "";
                        String all_description = "";
                        String priceTag = "false";

                        for (int i = 0; i < stList.size(); i++) {
                            Model_ItemProducts singleStudent = stList.get(i);

                            if (singleStudent.isSelected()) {


                                Product_id = singleStudent.getProduct_id();
                                Product_name = singleStudent.getProduct_name();
                                Product_imageurl = singleStudent.getProduct_imageurl();
                                Product_quantity = singleStudent.getProduct_quantity();
                                Product_price = singleStudent.getProduct_price();
                                TotalItem_price = singleStudent.getTotalItem_price();
                                Price_PerUnit = singleStudent.getPrice_PerUnit();
                                istaxable = singleStudent.getIstaxable();
                                UpscCode = singleStudent.getUpsc_code();
                                all_description = singleStudent.getDescription();


                                for (int h = 0; h < CustPrice.size(); h++) {

                                    if (CustPrice.get(h).getProductid().equals(Product_id)) {
                                        Price_PerUnit =CustPrice.get(h).getCustomerprice();
                                        priceTag ="true";
                                    }

                                }


                                double TotalAmt = Double.parseDouble(Price_PerUnit) * Double.parseDouble(Product_quantity);

                                boolean isExsits =false;

                                if(list.size()>0){

                                    for (int j = 0; j < list.size(); j++) {

                                        if(list.get(j).getProduct_id().equals(Product_id)){

                                            int getProduct = Integer.parseInt(list.get(j).getProduct_quantity());

                                            getProduct += Integer.parseInt(Product_quantity);

                                            list.get(j).setProduct_quantity(String.valueOf(getProduct));
                                            list.get(j).setIstaxable(String.valueOf(istaxable));
                                            list.get(j).setPrice_PerUnit(String.valueOf(Price_PerUnit));

                                            j=list.size();
                                            isExsits= false;

                                        }else {
                                            isExsits =true;
                                        }
                                    }
                                }else {
                                    isExsits =true;
                                }
                                if(isExsits){
                                    list.add(new Model_ItemList(Product_id, Product_name, Product_imageurl,Product_quantity,String.valueOf(TotalAmt),Price_PerUnit,"",istaxable,UpscCode,all_description,priceTag));

                                }

                                adapter.notifyDataSetChanged();
                                Calculation();
                              //  Toast.makeText(Activity_UpdateOrder.this, "Product has been saved successfully!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        alert="true";

                        dialog.dismiss();
                    }
                });

                SQLiteController sqLiteController = new SQLiteController(Activity_UpdateOrder.this);
                sqLiteController.open();
                try {
                    long count = sqLiteController.fetchCount();
                    if (count > 0) {
                        //product
                        Cursor product_c = sqLiteController.readAllTableProduct();

                        if (product_c != null && product_c.moveToFirst()) {
                            String productName ="";
                            String Upsc_code ="";
                            String productId ="";
                            String istaxable ="";
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

                                stList.add(new Model_ItemProducts(false,productId,productName
                                        ,productImage,"0","0",productPrice,productPrice,"",istaxable,Upsc_code,all_description));

                                //  productList.add(st);
                                mAdapter.notifyDataSetChanged();

                            } while (product_c.moveToNext());
                        }


                    }
                } finally {
                    sqLiteController.close();
                }

                final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Do something
                        //SEARCH FILTER
                        filteredModelList = filter(stList, newText.toString());
                        mAdapter.setFilter(filteredModelList);
                        if(filteredModelList.size()>0){
                            mRecyclerProducts.setVisibility(View.VISIBLE);
                            txt_no_record2.setVisibility(View.GONE);
                        }else {
                            mRecyclerProducts.setVisibility(View.GONE);
                            txt_no_record2.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Do something
                        Utils.getInstance().hideKeyboard(Activity_UpdateOrder.this);
                        return true;
                    }
                };

                searchBar.setOnQueryTextListener(queryTextListener);
            }
        });
    }


    private ArrayList<Model_ItemProducts> filter(ArrayList<Model_ItemProducts> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_ItemProducts> filteredModelList = new ArrayList<>();

        for (Model_ItemProducts model : models) {

            final String getOrder_name = model.getProduct_name().toLowerCase();


            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }

    private String getOrderCount() {
        tkSharedPreferences = getSharedPreferences(Activity_UpdateOrder.tkPref,MODE_PRIVATE);
        if (tkSharedPreferences.contains(Activity_UpdateOrder.order_Count)) {
            return tkSharedPreferences.getString(Activity_UpdateOrder.order_Count, null);
        }
        return "0";
    }

    private String getOrderDate() {
        tkSharedPreferences = getSharedPreferences(Activity_UpdateOrder.tkPref,MODE_PRIVATE);
        if (tkSharedPreferences.contains(Activity_UpdateOrder.order_CrDate)) {
            return tkSharedPreferences.getString(Activity_UpdateOrder.order_CrDate, null);
        }
        return "0";
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, result.getFormatName(), Toast.LENGTH_SHORT).show();
                //tvScanContent.setText(result.getContents());
                //tvScanFormat.setText(result.getFormatName());
                GetUPList(String.valueOf(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("Range")
    private void GetUPList(String B_code) {
        boolean isExsits =false;
        Utils.getInstance().loadingDialog(this, "Please wait.");
        SQLiteController sqLiteController = new SQLiteController(Activity_UpdateOrder.this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetch_All_PRODUCT_Count();
            if (count > 0) {

                //product
                Cursor product_c = sqLiteController.readAllTableProduct();

                if (product_c != null && product_c.moveToFirst()) {
                    String productName ="null";
                    String productId ="";
                    String istaxable ="";
                    String productImage ="null";
                    String productPrice ="0";
                    String all_upsc ="0";
                    String upscCode ="";
                    String all_description ="";
                    String priceTag = "false";
                    do {

                        all_upsc =product_c.getString(product_c.getColumnIndex("all_upsc"));

                        if(B_code.equals(all_upsc)){

                            productName =product_c.getString(product_c.getColumnIndex("all_product_name"));
                            productId =product_c.getString(product_c.getColumnIndex("all_product_id"));
                            productImage =product_c.getString(product_c.getColumnIndex("all_product_img"));
                            productPrice =product_c.getString(product_c.getColumnIndex("all_product_price"));
                            istaxable =product_c.getString(product_c.getColumnIndex("all_istax"));
                            upscCode =product_c.getString(product_c.getColumnIndex("all_upsc"));
                            all_description =product_c.getString(product_c.getColumnIndex("all_description"));

                        }

                    } while (product_c.moveToNext());

                    if(list.size()>0){
                        for (int j = 0; j < list.size(); j++) {
                            if(list.get(j).getProduct_id().equals(productId)){
                                  /* Toast.makeText(this, "Warning " +list.get(j).getProduct_name() + "  Alreday Added!", Toast.LENGTH_SHORT).show();
                                isExsits= false;
                                break;*/
                                int getProduct = Integer.parseInt(list.get(j).getProduct_quantity());

                                getProduct += Integer.parseInt("1");
                                list.get(j).setProduct_quantity(String.valueOf(getProduct));


                                j=list.size();
                                isExsits= false;
                            }else {
                                isExsits =true;
                            }
                        }
                    }else {
                        isExsits =true;
                    }
                    if(productName.equals("null")){
                        isExsits= false;
                        Toast.makeText(this, "Product Not found for this BarCode!", Toast.LENGTH_SHORT).show();

                    }
                    if(isExsits){
                        double TotalAmt = Double.parseDouble(productPrice) * Double.parseDouble("0");
                        list.add(new Model_ItemList(productId, productName, productImage, "1", String.valueOf(TotalAmt), productPrice, "", istaxable,upscCode,all_description,priceTag));
                    }

                    adapter.notifyDataSetChanged();
                    Calculation();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().dismissDialog();
                        }
                    });
                }


            }
        } finally {
            sqLiteController.close();
        }
    }

    public void showSpinnerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateOrder.this);
        LayoutInflater inflater = Activity_UpdateOrder.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_searchdislog,null);
        builder.setView(v);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        AppCompatImageView rippleViewClose = (AppCompatImageView) v.findViewById(R.id.close);
        ListView listView = (ListView) v.findViewById(R.id.list);
        EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        AppCompatTextView txt_no_record = (AppCompatTextView) v.findViewById(R.id.txt_no_record);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arCustomerName);
        arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        listView.setAdapter(arrayAdapter);

        final int[] pos_ = new int[1];
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
/*
                list.clear();
                adapter.notifyDataSetChanged();
                Calculation();*/


                for (int j = 0; j < arCustomerName.size(); j++) {
                    if (arrayAdapter.getItem(position).equalsIgnoreCase(arCustomerName.get(j).toString())) {
                        pos_[0] = j;
                    }
                }

                System.out.println("position---"+ pos_[0]);

                tv_customerSp.setText(arCustomerName.get(pos_[0]));
                str_customer = arCustomerName.get(pos_[0]);
                str_customerID = arCustomerID.get(pos_[0]);

                if(!customerpricingsetup.equals("0")){
                    GetCustomerProducts(str_customerID);
                }



                getCustomerInfo(pos_[0],"1");

                dialog.dismiss();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

                arrayAdapter.getFilter().filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (count == 0){
                            txt_no_record.setVisibility(View.VISIBLE);
                            // Toast.makeText(Activity_CreateOrder.this, "0 item", Toast.LENGTH_SHORT).show();
                        } else {
                            txt_no_record.setVisibility(View.GONE);
                            // Toast.makeText(Activity_CreateOrder.this, String.format("Items: %d", count), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void GetCustomerProducts(String str_customerID) {
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate1 = sdf.format(date1);
        Progress_dialog.show();
        try {
            App.getInstance().GetCustomerProducts(str_customerID,currentDate1,"",token,new Callback(){

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
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("data");
                            System.out.println("data---"+data);
                            if(CustPrice.size()>0)CustPrice.clear();
                            if (Progress_dialog != null) {
                                if (Progress_dialog.isShowing()) {
                                    Progress_dialog.dismiss();
                                }
                            }

                            if(data.length()>0){

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    String pricename = js.getString("pricename");
                                    String productid = js.getString("productid");
                                    String customerprice = js.getString("customerprice");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustPrice.add(new Model_CustomerProductsPricing(pricename, productid, customerprice));
                                        }
                                    });

                                }

                                runOnUiThread(new Runnable() {
                                    @SuppressLint("Range")
                                    @Override
                                    public void run() {

                                        SQLiteController sqLiteController2 = new SQLiteController(getApplicationContext());
                                        sqLiteController2.open();
                                        try {
                                            long count = sqLiteController2.fetchCount();
                                            if (count > 0) {
                                                Cursor Product_c = sqLiteController2.readProductJoinTables(OrderId);
                                                if (Product_c != null && Product_c.moveToFirst()) {
                                                    if(list.size()>0){
                                                        list.clear();
                                                    }

                                                    do {
                                                        @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                        @SuppressLint("Range") String OrderProduct_id = Product_c.getString(Product_c.getColumnIndex("pid"));
                                                        @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                        @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                        @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                        @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));


                                                        System.out.println("CustPrice---"+CustPrice.size());

                                                        String priceTag ="false";
                                                        for (int h = 0; h < CustPrice.size(); h++) {

                                                            if (CustPrice.get(h).getProductid().equals(productid)) {
                                                                priceTag ="true";
                                                            }

                                                        }

                                                        //product
                                                       // Cursor product_c1 = sqLiteController2.readAllTableProduct();
                                                        Cursor product_c1 = sqLiteController2.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);

                                                        if (product_c1 != null && product_c1.moveToFirst()) {
                                                            String productImage ="null";
                                                            do {

                                                                @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                                SQLiteController sqLiteController88= new SQLiteController(Activity_UpdateOrder.this);
                                                                sqLiteController88.open();
                                                                try {
                                                                    Cursor CursorAssertTable = sqLiteController88.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
                                                                    if (CursorAssertTable.moveToFirst()) {
                                                                        do {
                                                                            @SuppressLint("Range") String Products_assets_default = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_default"));

                                                                            if(Products_assets_default.equals("true")){
                                                                                productImage = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_file"));
                                                                            }


                                                                        } while (CursorAssertTable.moveToNext());
                                                                    }
                                                                    if(CursorAssertTable.getCount() == 0){
                                                                        productImage ="null";
                                                                    }
                                                                } finally {
                                                                    sqLiteController88.close();
                                                                }

                                                                if(product_id.equals(productid)){
                                                                    @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                    @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                                                    @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                                                    double TotalAmt = Double.parseDouble(priceperunit) * Double.parseDouble(quantity);
                                                                    list.add(new Model_ItemList(productid, product_name__, productImage,quantity,String.valueOf(TotalAmt),priceperunit,OrderProduct_id,istaxable,all_upsc,all_description,priceTag));

                                                                    adapter.notifyDataSetChanged();

                                                                    if(list.size()>0){
                                                                        mRecyclerView.setVisibility(View.VISIBLE);
                                                                        txt_no_record.setVisibility(View.GONE);
                                                                    }else {
                                                                        mRecyclerView.setVisibility(View.GONE);
                                                                        txt_no_record.setVisibility(View.VISIBLE);
                                                                    }

                                                                    TotalSum =0.00;
                                                                    TotalTaxSum =0.0;

                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        double TotalNewAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                                                        TotalSum = TotalSum + TotalNewAmt;
                                                                    }

                                                                    for (int i = 0; i < list.size(); i++) {

                                                                        if(list.get(i).getIstaxable().equals("true")){
                                                                            double TotalNewAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                                                            TotalTaxSum = TotalTaxSum + TotalNewAmt;
                                                                        }

                                                                    }


                                                                    txt_sub_total.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalSum));


                                                                    Dis_Amt = TotalSum * Double.parseDouble(updiscountpercentage)/100;
                                                                    tv_discount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Dis_Amt));


                                                                    Ship_Amt = Double.parseDouble(upfreightamount);
                                                                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Ship_Amt));


                                                                    if(Str_CS.equals("true")){
                                                                        if(!et_tax.getText().toString().isEmpty()){
                                                                            Tax_Amt = TotalTaxSum * Double.parseDouble(uptotaltaxbase)/100;
                                                                            tv_tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Tax_Amt));
                                                                        }else {
                                                                            Tax_Amt =0.0;
                                                                        }
                                                                    }else {
                                                                        Tax_Amt =0.0;
                                                                    }

                                                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt - Dis_Amt;
                                                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(TotalPayableAmt));

                                                                }

                                                            } while (product_c1.moveToNext());
                                                        }

                                                    } while (Product_c.moveToNext());
                                                }
                                            }
                                        } finally {
                                            sqLiteController2.close();
                                        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}