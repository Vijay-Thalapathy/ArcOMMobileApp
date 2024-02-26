package com.example.arcomdriver.salesreturn;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ItemProducts;
import com.example.arcomdriver.adapter.Adapter_SalesItemList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.model.Model_CustomerProductsPricing;
import com.example.arcomdriver.model.Model_ItemProducts;
import com.example.arcomdriver.model.Model_SalesItemList;
import com.example.arcomdriver.order.ActivityOrdersDetails;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.example.arcomdriver.recyclerhelper.WrapContentLinearLayoutManager;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Jan 2023*/
public class Activity_CreateSalesReturn extends Activity_Menu {
    private CoordinatorLayout cl;
    private RecyclerView mRecyclerView;
    private AppCompatTextView tv_addCharge,txt_payable_amount, tv_tax, et_ReturnedDate, tv_shippingCharge, txt_no_record, txt_sub_total;
    private AppCompatEditText et_addCharge,et_tax, et_shippingCharge, et_deliveryNotes, et_terms, et_packShipNote, et_memo;
    private Adapter_SalesItemList adapter_model;
    private ArrayList<Model_SalesItemList> list = new ArrayList<>();

    private double TotalSum = 0.00;
    private double TotalPayableAmt = 0.00;
    private double Tax_Amt = 0.0;
    private double Ship_Amt = 0.0;

    private String str_TaxID, user_id;
    static final int DATE_PICKER1_ID = 1;

    public static String token;
    private int mYear, mMonth, mDay;
    AppCompatTextView bill_tv, del_tv;
    private ArrayList<String> arTaxID,arTerms, arTaxable, arOrderID, arDeliveryID, arBillingID, arCustomerID, arSalesRepID, arFulfillID,arProduct_dec, arWarehouseID,arProduct_upsc, arProductID, arProductImage, arProduct_price, arProduct_tax;
    private ArrayList<String> arINNum,arINID,arOrderNum, arCustomerName, arSalesRepName, arFulfilmentName, arWarehouseName, arProductName;
    Spinner sp_salesRep,  spWarehouse;
    String Str_CS = "0", str_Taxable, str_OrderId="0",str_InvoiceID, returnedDate_str, str_OrderNum="Choose Order Number", str_customerID, str_DeliveryID, str_BilingID,Str_upscCode,Str_dec, Str_taxable, str_ProductID, str_ProductPrice, str_customer="Choose Customer", str_salesRep, str_fulfilment="", str_warehouse, str_product, str_ProductUrl, str_salesRepID="00000000-0000-0000-0000-000000000000", str_warehouseID="00000000-0000-0000-0000-000000000000", str_fulfilmentID,orderNo = "0", OrderStatu = "New Order", Orderidi = "00000000-0000-0000-0000-000000000000";
    RecyclerView mRecyclerProducts;
    LinearLayout CustomerTax_ll;
    ArrayList<Model_ItemProducts> stList;
    Adapter_ItemProducts mAdapter;
    SearchView searchBar;
    CheckBox chkAllSelected;
    AppCompatTextView txt_no_record2;
    ArrayList<Model_ItemProducts> filteredModelList;

    CheckBox CS_chkSelected;
    private double TotalTaxSum = 0.00;

    public AlertDialog Progress_dialog;
    LinearLayout warehouse_ll;

    boolean wrBln =false;

    private ArrayList<String> deleteList = new ArrayList<>();

    String currentDateTime;

    AppCompatTextView tv_customerInSp,tv_OrderInSp;

    RadioGroup radioGroupSelect;
    RadioButton radioNone,radioOrder,radioInvoice;

    Spinner spFulfillmentType;

/*    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusIDName = new ArrayList<>();*/

    AppCompatTextView SR_tittle_tv,sr_OrStatus_tv;

    LinearLayout SR_ll_tv;

    String SelectedDrop ="0",CusID="null", settingsvalue="0",alert ="true";

    String refFlag="eb636df2-1489-4084-89a7-6c0d7adbff24";

    ArrayAdapter<String> SalesNumAdapter;

    ListView SListView;

    private static Activity_CreateSalesReturn Viewdetails;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_createsalesreturn, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("New Sales Return");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CusID = extras.getString("CusID");

            System.out.println("-----CusID---"+CusID);


        }


        Viewdetails = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_CreateSalesReturn.this.getLayoutInflater();
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
                        if(invoice_name.equals("tax")){

                            settingsvalue  = C_In.getString(C_In.getColumnIndex("invoice_value"));

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

        radioGroupSelect = (RadioGroup) findViewById(R.id.radioGroupSelect);
        radioNone = (RadioButton) findViewById(R.id.radioNone);
        radioOrder = (RadioButton) findViewById(R.id.radioOrder);
        radioInvoice = (RadioButton) findViewById(R.id.radioInvoice);

       // spStatus = findViewById(R.id.spPStatus);
        spFulfillmentType = findViewById(R.id.spFulfillmentType);
        spFulfillmentType.setEnabled(false);
        spFulfillmentType.setClickable(false);
        spFulfillmentType.setFocusable(false);
        SR_ll_tv = findViewById(R.id.SR_ll_tv);
        sr_OrStatus_tv = findViewById(R.id.sr_OrStatus_tv);

        CustomerTax_ll = findViewById(R.id.CustomerTax_ll);

        /*if (Activity_OrdersHistory.tax.equals("1")) {
            CustomerTax_ll.setVisibility(View.VISIBLE);
        } else {
            CustomerTax_ll.setVisibility(View.GONE);//Gone
        }*/

        tv_customerInSp = findViewById(R.id.tv_customerSRSp);
        tv_OrderInSp = findViewById(R.id.tv_OrderInSp);
        SR_tittle_tv = findViewById(R.id.SR_tittle_tv);

        mRecyclerView = findViewById(R.id.rc_ItemsList);
        CS_chkSelected = findViewById(R.id.CS_chkSelected);
        txt_no_record = findViewById(R.id.txt_no_record);
        et_ReturnedDate = findViewById(R.id.et_ReturnedDate);
        sp_salesRep = findViewById(R.id.sp_salesRep);
         spWarehouse = findViewById(R.id.spWarehouse);
        warehouse_ll = findViewById(R.id.warehouse_ll);

        System.out.println("settingsvalue---"+settingsvalue);

        if(settingsvalue.equals("1")) {
            //show
            CustomerTax_ll.setVisibility(View.VISIBLE);

        }else {
            //hide
            CustomerTax_ll.setVisibility(View.GONE);

        }

        if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
            //Hide Warehouse
            warehouse_ll.setVisibility(View.GONE);
        }else {
            //Show Warehouse
            warehouse_ll.setVisibility(View.VISIBLE);
        }
        txt_sub_total = findViewById(R.id.txt_sub_total);
        txt_sub_total.setText("-"+Activity_OrdersHistory.currency_symbol + "0.00");

        AppCompatTextView tv_tittle_ship = findViewById(R.id.tv_tittle_ship);
        AppCompatTextView tv_totl_tittle = findViewById(R.id.tv_totl_tittle);
        tv_tittle_ship.setText("Shipping Charges (" + Activity_OrdersHistory.currency_symbol + ")");
        tv_totl_tittle.setText("Total (" + Activity_OrdersHistory.currency_symbol + ")");

        et_addCharge = findViewById(R.id.et_addCharge);
        tv_addCharge = findViewById(R.id.tv_addCharge);
        tv_addCharge.setText(Activity_OrdersHistory.currency_symbol + "0.00");

        tv_shippingCharge = findViewById(R.id.tv_shippingCharge);
        tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol + "0.00");

        et_shippingCharge = findViewById(R.id.et_shippingCharge);
        et_shippingCharge.setHint("(" + Activity_OrdersHistory.currency_symbol + ")" + "0.00");
        et_shippingCharge.setText("0");

        et_tax = findViewById(R.id.et_tax);
        tv_tax = findViewById(R.id.tv_tax);
        tv_tax.setText(Activity_OrdersHistory.currency_symbol + "0.00");

        txt_payable_amount = findViewById(R.id.txt_payable_amount);
        txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + "0.00");

        bill_tv = findViewById(R.id.bill_tv);
        del_tv = findViewById(R.id.del_tv);
        et_deliveryNotes = findViewById(R.id.et_deliveryNotes);
        et_terms = findViewById(R.id.et_terms);
        et_packShipNote = findViewById(R.id.et_packShipNote);
        et_memo = findViewById(R.id.et_memo);

        Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate2 = sd2.format(date2);
        et_ReturnedDate.setText(currentDate2);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = sdf.parse(et_ReturnedDate.getText().toString());
            returnedDate_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        findViewById(R.id.cus_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","3");
                Intent in = new Intent(Activity_CreateSalesReturn.this, Activity_CreateCustomer.class);
                in.putExtras(bundle);
                startActivity(in);

            }
        });


        radioGroupSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!str_customer.equals("Choose Customer")) {

                    if (checkedId == R.id.radioNone) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SR_ll_tv.setVisibility(View.GONE);
                                sr_OrStatus_tv.setVisibility(View.GONE);
                                sr_OrStatus_tv.setText("");
                                if (list.size() > 0) list.clear();
                                adapter_model.notifyDataSetChanged();
                                Calculation();
                                SelectedDrop ="0";
                                refFlag ="eb636df2-1489-4084-89a7-6c0d7adbff24";
                            }
                        });

                    }else if (checkedId == R.id.radioOrder) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SR_ll_tv.setVisibility(View.VISIBLE);
                                sr_OrStatus_tv.setVisibility(View.VISIBLE);
                                sr_OrStatus_tv.setText("");
                                if (list.size() > 0) list.clear();
                                adapter_model.notifyDataSetChanged();
                                Calculation();
                                SR_tittle_tv.setText("Sales Order *");
                                tv_OrderInSp.setText("Choose Sales Order");

                                SelectedDrop ="1";
                                refFlag ="c47b8833-795c-42b6-a61b-384af8db8a6d";

                                SalesNumAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arOrderNum);
                                SalesNumAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                            }
                        });

                    }else if (checkedId == R.id.radioInvoice) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SR_ll_tv.setVisibility(View.VISIBLE);
                                sr_OrStatus_tv.setVisibility(View.VISIBLE);
                                sr_OrStatus_tv.setText("");
                                if (list.size() > 0) list.clear();
                                adapter_model.notifyDataSetChanged();
                                Calculation();
                                SR_tittle_tv.setText("Sales Invoice *");
                                tv_OrderInSp.setText("Choose Invoice");

                                SelectedDrop ="2";
                                refFlag ="4fa2564b-5cdb-4a64-9386-94b8f5a8d9a8";

                                SalesNumAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arINNum);
                                SalesNumAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                            }
                        });

                    }
                }else {
                    radioNone.setChecked(true);
                    radioOrder.setChecked(false);
                    radioInvoice.setChecked(false);
                    Toast.makeText(Activity_CreateSalesReturn.this, "Please choose customer name!", Toast.LENGTH_SHORT).show();
                }


            }

        });

        adapter_model = new Adapter_SalesItemList(Activity_CreateSalesReturn.this,list);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new WrapContentLinearLayoutManager(Activity_CreateSalesReturn.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter_model);

        if (list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            txt_no_record.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            txt_no_record.setVisibility(View.VISIBLE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                EditText cart_product_quantity_text_View = view.findViewById(R.id.cart_product_quantity_text_View);
                AppCompatTextView orderAmt_tv = view.findViewById(R.id.orderAmt_tv);
                EditText ItPrice_et = view.findViewById(R.id.ItPrice_et);

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
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            if (Double.parseDouble(ItPrice_et.getText().toString()) > 0) {
                                list.get(position).setPrice_PerUnit(ItPrice_et.getText().toString());

                                double TotalAmt = Double.parseDouble(list.get(position).getProduct_quantity()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                                orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));
                                Calculation();
                            } else {
                                list.get(position).setPrice_PerUnit("0");
                                orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                Calculation();
                            }

                        } else {
                            list.get(position).setPrice_PerUnit("0");
                            orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                            Calculation();
                        }


                    }
                });


                view.findViewById(R.id.cart_product_minus_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Double.parseDouble(cart_product_quantity_text_View.getText().toString()) != 1) {
                            cart_product_quantity_text_View.setText(String.valueOf(Integer.parseInt(cart_product_quantity_text_View.getText().toString()) - 1));

                            double TotalAmt = Double.parseDouble(cart_product_quantity_text_View.getText().toString()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                            orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                            list.get(position).setProduct_quantity(cart_product_quantity_text_View.getText().toString());
                            Calculation();
                        }

                    }
                });

                view.findViewById(R.id.cart_product_plus_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       if(SelectedDrop.equals("0")){
                           cart_product_quantity_text_View.setText(String.valueOf(Integer.parseInt(cart_product_quantity_text_View.getText().toString()) + 1));

                           double TotalAmt = Double.parseDouble(cart_product_quantity_text_View.getText().toString()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                           orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                           list.get(position).setProduct_quantity(cart_product_quantity_text_View.getText().toString());
                           Calculation();
                        }else {
                           if( Double.parseDouble(list.get(position).getProduct_quantity())>=Double.parseDouble(list.get(position).getDefaultQTy()) ){
                           }else {
                               cart_product_quantity_text_View.setText(String.valueOf(Integer.parseInt(cart_product_quantity_text_View.getText().toString()) + 1));

                               double TotalAmt = Double.parseDouble(cart_product_quantity_text_View.getText().toString()) * Double.parseDouble(list.get(position).getPrice_PerUnit());
                               orderAmt_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                               list.get(position).setProduct_quantity(cart_product_quantity_text_View.getText().toString());
                               Calculation();
                           }
                       }





                    }
                });

                view.findViewById(R.id.cart_product_delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //passing order product ID
                        deleteList.add(list.get(position).getProduct_id());

                        list.remove(position);
                        adapter_model.notifyDataSetChanged();
                        Calculation();
                        Toast.makeText(Activity_CreateSalesReturn.this, "Item has been deleted successfully!", Toast.LENGTH_SHORT).show();
                        //adapter_model.notifyItemChanged(position);
                    }
                });

                view.findViewById(R.id.AddchkSelected).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        if (cb.isChecked()) {
                            list.get(position).setIstaxable(String.valueOf(true));

                            list.get(position).setReturnReason(String.valueOf(list.get(position).getReturnReason()));
                            adapter_model.notifyDataSetChanged();
                            Calculation();

                        } else if (!cb.isChecked()) {
                            list.get(position).setIstaxable(String.valueOf(false));
                            list.get(position).setReturnReason(String.valueOf(list.get(position).getReturnReason()));
                            adapter_model.notifyDataSetChanged();
                            Calculation();
                        }


                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));


        CS_chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    Str_CS = "true";
                    if (!et_tax.getText().toString().equals("")) {
                        Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                        txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                    }

                } else if (!cb.isChecked()) {
                    Str_CS = "false";

                    if (!et_tax.getText().toString().equals("")) {
                        Tax_Amt = TotalTaxSum * Double.parseDouble("0.00") / 100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                        txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                    }
                }

            }
        });


        findViewById(R.id.et_ReturnedDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
                showDialog(DATE_PICKER1_ID);
            }
        });

        findViewById(R.id.bar_rll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(Activity_CreateSalesReturn.this);
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

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SelectedDrop.equals("0")){
                    if(alert.equals("true")){
                        alert="false";
                        AddMultipleItemAlert();
                    }
                }


            }
        });


        Date date1 = new Date();
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDateTime = sdf4.format(date1);


        Date date6 = new Date();
        SimpleDateFormat sdf6 = new SimpleDateFormat("ddMMyy");
        //sdf6.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentForDate = sdf6.format(date6);


        findViewById(R.id.btn_orderComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("str_customerID---"+str_customerID);

                Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
                 if (str_customer.equals("Choose Customer")) {
                    Utils.getInstance().snackBarMessage(v, "Choose Customer!");
                }else if (et_ReturnedDate.getText().toString().equals("MM/DD/YYYY")) {
                    Utils.getInstance().snackBarMessage(v, "Choose Returned Date!");
                }else {

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

                     if(wrBln){
                         if (TotalSum == 0.00) {
                             wrBln =false;
                             Utils.getInstance().snackBarMessage(v, "Sub total amount greater than zero!");
                         }else if (TotalPayableAmt == 0.0) {
                             wrBln =false;
                             Utils.getInstance().snackBarMessage(v, "Total amount greater than zero!!");
                         }else if (bill_tv.getText().toString().equals("No address found!")) {
                             wrBln =false;
                             Utils.getInstance().snackBarMessage(v,"Please update the customer address!");
                         }else if (del_tv.getText().toString().equals("No address found!")) {
                             wrBln =false;
                             Utils.getInstance().snackBarMessage(v,"Please update the customer address!");
                         }/*else if (TotalSum>Double.parseDouble(et_addCharge.getText().toString())) {
                             wrBln =false;
                             Utils.getInstance().snackBarMessage(v," Additional deduction amount greater than Sub total!");
                         }*/else {
                             wrBln =true;
                         }
                     }





                    Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
                    if(wrBln){
                        if (Connectivity.isConnected(Activity_CreateSalesReturn.this) &&
                                Connectivity.isConnectedFast(Activity_CreateSalesReturn.this)) {
                            JSONObject postedJSON = new JSONObject();
                            JSONArray array = new JSONArray();

                            //TODO getProduct_id = getOrder_productid
                            //TODO getOrder_productid = getProduct_id

                            for (int i = 0; i < list.size(); i++) {

                                String reasonNo =list.get(i).getReturnReason();

                                if(!reasonNo.equals("0")){

                                    System.out.println("ProductName---"+list.get(i).getProduct_name());

                                    double TotalBaseAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                    postedJSON = new JSONObject();
                                    try {
                                        //TODO getProduct_id = getOrder_productid
                                        //TODO getOrder_productid = getProduct_id

                                        if (str_OrderId.equals("0")) {
                                            //TODO OrderProductID
                                            if (list.get(i).getProduct_id().equals("null")) {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            }
                                        } else {
                                            if (list.get(i).getProduct_id().equals("null")) {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                //TODO OrderProductID
                                                postedJSON.put("id", list.get(i).getProduct_id().trim());
                                            }
                                        }

                                        postedJSON.put("isactive", true);
                                        postedJSON.put("uuid", null);
                                        postedJSON.put("createdby", user_id);
                                        postedJSON.put("createdon", currentDateTime);
                                        postedJSON.put("modifiedby", user_id);
                                        postedJSON.put("modifiedon", currentDateTime);
                                        postedJSON.put("agent", null);
                                        postedJSON.put("datafromtypeid", "0");
                                        postedJSON.put("datafromid", "0");
                                        postedJSON.put("tenantid", null);
                                        postedJSON.put("salesreturnid", null);
                                        postedJSON.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
                                        //TODO ProductID
                                        postedJSON.put("productid", list.get(i).getOrder_productid().trim());
                                        postedJSON.put("productname", list.get(i).getProduct_name());
                                        postedJSON.put("productdescription", list.get(i).getAll_description());
                                        postedJSON.put("priceperunit", list.get(i).getPrice_PerUnit().trim());
                                        postedJSON.put("uomid", null);
                                        postedJSON.put("orderedquantity", list.get(i).getProduct_quantity().trim());
                                        postedJSON.put("returnedquantity", list.get(i).getProduct_quantity().trim());
                                        if (list.get(i).getIstaxable().equals("true")) {
                                            postedJSON.put("itemistaxable", true);
                                            postedJSON.put("isitemtaxable", true);
                                        } else {
                                            postedJSON.put("itemistaxable", false);
                                            postedJSON.put("isitemtaxable", false);
                                        }
                                        postedJSON.put("discountamount", null);
                                        postedJSON.put("totalamount", TotalBaseAmt);
                                        postedJSON.put("returnreasonid", list.get(i).getReturnReason().trim());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    array.put(postedJSON);
                                }

                            }

                            JSONObject json = new JSONObject();
                            try {
                                 //
                                json.put("id", "00000000-0000-0000-0000-000000000000");
                                json.put("customerid", str_customerID);
                                json.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
                                json.put("referencetypeid", refFlag);
                                json.put("returneddate", currentDateTime);


                                if(SelectedDrop.equals("0")){
                                    json.put("orderid", "00000000-0000-0000-0000-000000000000");
                                    json.put("invoiceid", "00000000-0000-0000-0000-000000000000");
                                    json.put("isNoneSalesReturn", true);
                                }else {
                                    json.put("orderid", str_OrderId);
                                    json.put("invoiceid", str_InvoiceID);
                                    json.put("isNoneSalesReturn", false);
                                }

                                json.put("paymentmode", str_fulfilment);
                                json.put("warehouseid", str_warehouseID);
                                json.put("salesrepid", str_salesRepID);
                                json.put("returnstatus", "Returned");
                                json.put("returnnumber", null);
                                json.put("billingaddressid", str_BilingID);
                                json.put("shippingaddressid", str_DeliveryID);
                                json.put("subtotal", String.valueOf(TotalSum));
                                json.put("discountpercentage", "0");
                                json.put("discountamount", "0");
                                json.put("shippingamount", et_shippingCharge.getText().toString());
                                json.put("adddeductionamount",  et_addCharge.getText().toString());
                                json.put("deliverycharges", "0");

                                if (Str_CS.equals("true")) {
                                    json.put("customeristaxable", true);
                                } else {
                                    json.put("customeristaxable", false);
                                }
                                if(str_TaxID.equals("00000000-0000-0000-0000-000000000000")){
                                    json.put("taxid",null);
                                }else{
                                    json.put("taxid",str_TaxID);
                                }
                                json.put("taxpercentage", et_tax.getText().toString());
                                json.put("taxamount", String.valueOf(Tax_Amt));
                                json.put("totalamount", String.valueOf(TotalPayableAmt));
                                json.put("isesigned", false);
                                json.put("useresignid", null);
                                json.put("shipnotes", et_packShipNote.getText().toString());
                                json.put("pickpacknotes", et_deliveryNotes.getText().toString());
                                json.put("termsandconditions", et_terms.getText().toString());
                                json.put("memo", et_memo.getText().toString());
                                json.put("isactive", true);
                                json.put("uuid", null);
                                json.put("createdby", user_id);
                                json.put("createdon", currentDateTime);
                                json.put("modifiedby", user_id);
                                json.put("modifiedon", currentDateTime);
                                json.put("agent", null);
                                json.put("datafromtypeid", "0");
                                json.put("datafromid", "0");
                                json.put("tenantid", null);


                                json.put("salesreturnproduct", array);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println("SalesJSON ----"+json.toString());

                            AlertIn(String.valueOf(json));

                        }
                    }

                }
            }
        });

        findViewById(R.id.btn_salesDraft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
                if (str_customer.equals("Choose Customer")) {
                    Utils.getInstance().snackBarMessage(v, "Choose Customer!");
                }else if (et_ReturnedDate.getText().toString().equals("MM/DD/YYYY")) {
                    Utils.getInstance().snackBarMessage(v, "Choose Returned Date!");
                }else {

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

                    if(wrBln){
                        if (TotalSum == 0.00) {
                            wrBln =false;
                            Utils.getInstance().snackBarMessage(v, "Sub total amount greater than zero!");
                        }else if (TotalPayableAmt == 0.0) {
                            wrBln =false;
                            Utils.getInstance().snackBarMessage(v, "Total amount greater than zero!!");
                        }else if (bill_tv.getText().toString().equals("No address found!")) {
                            wrBln =false;
                            Utils.getInstance().snackBarMessage(v,"Please update the customer address!");
                        }else if (del_tv.getText().toString().equals("No address found!")) {
                            wrBln =false;
                            Utils.getInstance().snackBarMessage(v,"Please update the customer address!");
                        }else {
                            wrBln =true;
                        }
                    }





                    Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
                    if(wrBln){
                        if (Connectivity.isConnected(Activity_CreateSalesReturn.this) &&
                                Connectivity.isConnectedFast(Activity_CreateSalesReturn.this)) {
                            JSONObject postedJSON = new JSONObject();
                            JSONArray array = new JSONArray();

                            //TODO getProduct_id = getOrder_productid
                            //TODO getOrder_productid = getProduct_id

                            for (int i = 0; i < list.size(); i++) {

                                String reasonNo =list.get(i).getReturnReason();

                                if(!reasonNo.equals("0")){

                                    System.out.println("ProductName---"+list.get(i).getProduct_name());

                                    double TotalBaseAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                                    postedJSON = new JSONObject();
                                    try {
                                        //TODO getProduct_id = getOrder_productid
                                        //TODO getOrder_productid = getProduct_id

                                        if (str_OrderId.equals("0")) {
                                            //TODO OrderProductID
                                            if (list.get(i).getProduct_id().equals("null")) {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            }
                                        } else {
                                            if (list.get(i).getProduct_id().equals("null")) {
                                                postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                //TODO OrderProductID
                                                postedJSON.put("id", list.get(i).getProduct_id().trim());
                                            }
                                        }

                                        postedJSON.put("isactive", true);
                                        postedJSON.put("uuid", null);
                                        postedJSON.put("createdby", user_id);
                                        postedJSON.put("createdon", currentDateTime);
                                        postedJSON.put("modifiedby", user_id);
                                        postedJSON.put("modifiedon", currentDateTime);
                                        postedJSON.put("agent", null);
                                        postedJSON.put("datafromtypeid", "0");
                                        postedJSON.put("datafromid", "0");
                                        postedJSON.put("tenantid", null);
                                        postedJSON.put("salesreturnid", null);
                                        postedJSON.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
                                        //TODO ProductID
                                        postedJSON.put("productid", list.get(i).getOrder_productid().trim());
                                        postedJSON.put("productname", list.get(i).getProduct_name());
                                        postedJSON.put("productdescription", list.get(i).getAll_description());
                                        postedJSON.put("priceperunit", list.get(i).getPrice_PerUnit().trim());
                                        postedJSON.put("uomid", null);
                                        postedJSON.put("orderedquantity", list.get(i).getProduct_quantity().trim());
                                        postedJSON.put("returnedquantity", list.get(i).getProduct_quantity().trim());
                                        if (list.get(i).getIstaxable().equals("true")) {
                                            postedJSON.put("itemistaxable", true);
                                            postedJSON.put("isitemtaxable", true);
                                        } else {
                                            postedJSON.put("itemistaxable", false);
                                            postedJSON.put("isitemtaxable", false);
                                        }
                                        postedJSON.put("discountamount", null);
                                        postedJSON.put("totalamount", TotalBaseAmt);
                                        postedJSON.put("returnreasonid", list.get(i).getReturnReason().trim());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    array.put(postedJSON);
                                }

                            }

                            JSONObject json = new JSONObject();
                            try {
                                //
                                json.put("id", "00000000-0000-0000-0000-000000000000");
                                json.put("customerid", str_customerID);
                                json.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
                                json.put("referencetypeid", refFlag);
                                json.put("returneddate", currentDateTime);


                                if(SelectedDrop.equals("0")){
                                    json.put("orderid", "00000000-0000-0000-0000-000000000000");
                                    json.put("invoiceid", "00000000-0000-0000-0000-000000000000");
                                    json.put("isNoneSalesReturn", true);
                                }else {
                                    json.put("orderid", str_OrderId);
                                    json.put("invoiceid", str_InvoiceID);
                                    json.put("isNoneSalesReturn", false);
                                }

                                json.put("paymentmode", str_fulfilment);
                                json.put("warehouseid", str_warehouseID);
                                json.put("salesrepid", str_salesRepID);
                                json.put("returnstatus", "Draft");
                                json.put("returnnumber", null);
                                json.put("billingaddressid", str_BilingID);
                                json.put("shippingaddressid", str_DeliveryID);
                                json.put("subtotal", String.valueOf(TotalSum));
                                json.put("discountpercentage", "0");
                                json.put("discountamount", "0");
                                json.put("shippingamount", et_shippingCharge.getText().toString());
                                json.put("adddeductionamount",  et_addCharge.getText().toString());
                                json.put("deliverycharges", "0");

                                if (Str_CS.equals("true")) {
                                    json.put("customeristaxable", true);
                                } else {
                                    json.put("customeristaxable", false);
                                }
                                if(str_TaxID.equals("00000000-0000-0000-0000-000000000000")){
                                    json.put("taxid",null);
                                }else{
                                    json.put("taxid",str_TaxID);
                                }
                                json.put("taxpercentage", et_tax.getText().toString());
                                json.put("taxamount", String.valueOf(Tax_Amt));
                                json.put("totalamount", String.valueOf(TotalPayableAmt));
                                json.put("isesigned", false);
                                json.put("useresignid", null);
                                json.put("shipnotes", et_packShipNote.getText().toString());
                                json.put("pickpacknotes", et_deliveryNotes.getText().toString());
                                json.put("termsandconditions", et_terms.getText().toString());
                                json.put("memo", et_memo.getText().toString());
                                json.put("isactive", true);
                                json.put("uuid", null);
                                json.put("createdby", user_id);
                                json.put("createdon", currentDateTime);
                                json.put("modifiedby", user_id);
                                json.put("modifiedon", currentDateTime);
                                json.put("agent", null);
                                json.put("datafromtypeid", "0");
                                json.put("datafromid", "0");
                                json.put("tenantid", null);


                                json.put("salesreturnproduct", array);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println("SalesJSON ----"+json.toString());

                            AlertIn(String.valueOf(json));

                        }
                    }

                }
            }
        });

        SQLiteController sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {

                /*arOrderNum = new ArrayList<String>();
                arOrderNum.add("Choose Order Number");
                arOrderID = new ArrayList<String>();
                arOrderID.add("0");
                Cursor order_c = sqLiteController.readJointTableOrderInvoice();
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                        @SuppressLint("Range") String id = order_c.getString(order_c.getColumnIndex("order_id"));

                        arOrderNum.add(ordernumber);
                        arOrderID.add(id);

                    } while (order_c.moveToNext());

                }*/

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

                arSalesRepName = new ArrayList<String>();
                arSalesRepName.add("Choose Sales Rep");
                arSalesRepID = new ArrayList<String>();
                arSalesRepID.add("00000000-0000-0000-0000-000000000000");
                //salesRep
                Cursor sales_c = sqLiteController.readTableSalesRep();
                if (sales_c != null && sales_c.moveToFirst()) {
                    do {
                        arSalesRepName.add(sales_c.getString(sales_c.getColumnIndex("salesrep_name")));
                        arSalesRepID.add(sales_c.getString(sales_c.getColumnIndex("salesrep_id")));

                    } while (sales_c.moveToNext());
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

        findViewById(R.id.tv_customerSRSp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinnerDialog();
            }
        });

        findViewById(R.id.tv_OrderInSp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!str_customer.equals("Choose Customer")) {

                    showOrderSpinnerDialog();  
                }else {
                    Toast.makeText(Activity_CreateSalesReturn.this, "Please choose customer name!", Toast.LENGTH_SHORT).show();
                }
              
            }
        });

        /*arStatusID.add("0");
        arStatusIDName.add("New");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arStatusIDName);
        arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spStatus.setAdapter(arrayAdapter);
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Str_StatusName = arStatusIDName.get(position);
               // System.out.println("Str_StatusName--"+Str_StatusName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arSalesRepName);
        arrayAdapter2.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        sp_salesRep.setAdapter(arrayAdapter2);
        sp_salesRep.setSelection(arSalesRepID.indexOf(user_id.toLowerCase(Locale.ROOT)));
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


        arFulfilmentName = new ArrayList<String>();
         //arFulfilmentName.add("Choose Payment Mode");
        //arFulfilmentName.add("Check");
        //arFulfilmentName.add("Cash");
        arFulfilmentName.add("Credit");
        arFulfillID = new ArrayList<String>();
        // arFulfillID.add("0");
        //arFulfillID.add("4e142367-5f99-4aa7-99ed-931756978ee5");
        //arFulfillID.add("7e1c2265-33c4-4f38-a1b3-55a80662a99c");
        arFulfillID.add("7e1c2265-33c4-4f38-a1b3-55a80662a99c");

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arFulfilmentName);
        arrayAdapter3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spFulfillmentType.setAdapter(arrayAdapter3);
        spFulfillmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_fulfilment = arFulfilmentName.get(position);
                str_fulfilmentID = arFulfillID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arWarehouseName);
        arrayAdapter4.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spWarehouse.setAdapter(arrayAdapter4);
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


        et_addCharge.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!et_addCharge.getText().toString().startsWith(".")) {

                                tv_addCharge.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Double.parseDouble(et_addCharge.getText().toString())));

                                TotalPayableAmt = TotalSum -  Double.parseDouble(et_addCharge.getText().toString());

                                if(String.valueOf(TotalPayableAmt).startsWith("-")){
                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                }else {
                                    txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                }


                            }else {
                                et_addCharge.setText("");
                            }

                        }
                    });
                } else {
                    tv_addCharge.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                }

            }
        });


       /* et_shippingCharge.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if (TotalSum > 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!et_shippingCharge.getText().toString().startsWith(".")) {
                                    Ship_Amt = Double.parseDouble(et_shippingCharge.getText().toString());
                                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Ship_Amt));

                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                                    txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));
                                }else {
                                    et_shippingCharge.setText("");
                                }

                            }
                        });

                    } else {
                        tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol + "0.00");

                        // et_shippingCharge.setText("0");
                    }

                } else {
                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Ship_Amt = 0.0;
                            TotalPayableAmt = TotalSum + Tax_Amt + 0.0;
                            txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));
                        }
                    });
                    //et_shippingCharge.setText("0");
                }


            }
        });*/

        et_tax.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if (TotalTaxSum > 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!et_tax.getText().toString().startsWith(".")) {
                                    if (Str_CS.equals("true")) {
                                        Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                                        TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;

                                        if(String.valueOf(TotalPayableAmt).startsWith("-")){
                                            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                        }else {
                                            txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                        }
                                       // txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                    } else {
                                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                    }
                                }else {
                                    et_tax.setText("");
                                }


                            }
                        });

                    } else {
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                        //et_tax.setText("0");
                    }

                } else {
                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tax_Amt = 0.0;
                            TotalPayableAmt = TotalSum + 0.0 + Ship_Amt;
                            txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));
                        }
                    });
                }


            }
        });




        if(!CusID.equals("null")){
            CustomerList();
        }

    }

    private void CustomerList() {

        if (list.size() > 0){
            adapter_model.notifyDataSetChanged();
            Calculation();
            sr_OrStatus_tv.setText("-");
        }



        for (int i = 0; i < arCustomerID.size(); i++) {
            String  SCustID = arCustomerID.get(i);
            if(SCustID.contains(CusID)) {
                str_customer = arCustomerName.get(i);
                str_customerID = arCustomerID.get(i);
                tv_customerInSp.setText(str_customer);

                GetCustomerInfo(i);
            }
        }

        JSONObject json = new JSONObject();
        try {
            //  json.put("referenceid","C47B8833-795C-42B6-A61B-384AF8DB8A6D");
            json.put("referenceid","4FA2564B-5CDB-4A64-9386-94B8F5A8D9A8");
            json.put("customerid",str_customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRef(json.toString());

        System.out.println("-----POSTREF---"+json.toString());


        System.out.println("-----pos_[0]_customerNew---"+str_customer);
        System.out.println("-------pos_[0]_customerIDNew---"+str_customerID);


    }

    public void AlertIn(String json) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this);
        LayoutInflater inflater = Activity_CreateSalesReturn.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_alertviewdislog,null);
        builder.setView(v);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        getWindow().setGravity(Gravity.BOTTOM);

        AppCompatButton load = (AppCompatButton) v.findViewById(R.id.btn_hSales);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCreateSales(json.toString(), str_OrderId,currentDateTime);
                dialog.dismiss();
            }
        });

        AppCompatButton btn_cancel = (AppCompatButton) v.findViewById(R.id.btn_hcancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        AppCompatImageView close_img = (AppCompatImageView) v.findViewById(R.id.close_img);
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



    private void postCreateSales(String json, String orderIDSeleted, String ModifiedDateTime) {
        Progress_dialog.show();
        try {
            App.getInstance().PostSales(json, token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("REturns----"+jsonObject);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    String data = jsonObject.getString("data");

                                    JSONArray jr =jsonObject.getJSONArray("messages");
                                    if(jr.length()>0){
                                        System.out.println("jr----"+jr.getString(0));
                                    }

                                    if (succeeded == true) {
                                        GenerateInvoice(jr.getString(0),data);
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

    private void GenerateInvoice(String msg,String data) {

        JSONObject postedJSON = new JSONObject();
        JSONArray array = new JSONArray();

        for (int i = 0; i < list.size(); i++) {

            System.out.println("ProductName---"+list.get(i).getProduct_name());

            double TotalBaseAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());


            postedJSON = new JSONObject();
            try {
                //TODO getProduct_id = getOrder_productid
                //TODO getOrder_productid = getProduct_id
                postedJSON.put("productid", list.get(i).getOrder_productid().trim());
                postedJSON.put("prodid", list.get(i).getOrder_productid().trim());
                postedJSON.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
                postedJSON.put("baseamount", TotalBaseAmt);
                postedJSON.put("exchangerate", "0");
                postedJSON.put("baseamountbase", TotalBaseAmt);
                postedJSON.put("baseamount", TotalBaseAmt);
                postedJSON.put("extendedamount", "0");
                postedJSON.put("exchangerate", "0");
                postedJSON.put("extendedamountbase", "0");
                postedJSON.put("iscopied", true);
                postedJSON.put("ispriceoverridden", true);
                postedJSON.put("isproductoverridden", true);
                postedJSON.put("lineitemnumber", "0");
                postedJSON.put("manualdiscountamountbase", "0");
                postedJSON.put("priceperunit", list.get(i).getPrice_PerUnit().trim());
                postedJSON.put("quantity", list.get(i).getProduct_quantity().trim());
                postedJSON.put("priceperunitbase", "0");
                postedJSON.put("quantitybackordered", "0");
                postedJSON.put("quantitycancelled", "0");
                postedJSON.put("quantityshipped", "0");
                postedJSON.put("salesorderispricelocked", true);
                postedJSON.put("shiptoaddressid", str_DeliveryID);
                postedJSON.put("tax", "0");
                postedJSON.put("isHigh", true);
                postedJSON.put("productimage", null);
                postedJSON.put("returnreasonid", "");
                postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");
                postedJSON.put("returnedquantity", list.get(i).getProduct_quantity().trim());
                postedJSON.put("picked", "0");
                postedJSON.put("discountamount", "0");
                postedJSON.put("totalamount", TotalBaseAmt);
                postedJSON.put("isHigh", true);
                postedJSON.put("quantityonhand", "0");
                postedJSON.put("uomname", "Each");
                postedJSON.put("isactive", true);
                postedJSON.put("stock available", "0");
                postedJSON.put("available", "0");
                postedJSON.put("committed", "0");
                if (list.get(i).getIstaxable().equals("true")) {
                    postedJSON.put("itemistaxable", true);
                } else {
                    postedJSON.put("itemistaxable", false);
                }
                if (et_tax.getText().toString().equals("")) {
                    postedJSON.put("totaltaxbase", "0");
                } else {
                    postedJSON.put("totaltaxbase", et_tax.getText().toString().trim());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(postedJSON);
        }

        JSONObject json = new JSONObject();
        try {
            if(SelectedDrop.equals("0")){
                json.put("orderid", "00000000-0000-0000-0000-000000000000");
                json.put("invoiceid", "00000000-0000-0000-0000-000000000000");
            }else {
                json.put("orderid", str_OrderId);
                json.put("invoiceid", str_InvoiceID);
            }

            json.put("customerid", str_customerID);
            json.put("referencetypeid", refFlag);
            json.put("salesrepid", str_salesRepID);
            json.put("returneddate", currentDateTime);
            json.put("paymentmode", str_fulfilment);
            json.put("warehouseid", str_warehouseID);
            if (Str_CS.equals("true")) {
                json.put("customeristaxable", true);
            } else {
                json.put("customeristaxable", false);
            }
            json.put("adddeductionamount",  et_addCharge.getText().toString());
            json.put("shipnotes", et_packShipNote.getText().toString());
            json.put("pickpacknotes", et_deliveryNotes.getText().toString());
            json.put("termsandconditions", et_terms.getText().toString());
            json.put("memo", et_memo.getText().toString());
            json.put("id", "00000000-0000-0000-0000-000000000000");
            json.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
            json.put("billingaddressid", str_BilingID);
            json.put("shippingaddressid", str_DeliveryID);
            json.put("totallineitemamount", String.valueOf(TotalSum));
            if (et_tax.getText().toString().equals("")) {
                json.put("totaltaxbase", "0");
            } else {
                json.put("totaltaxbase", et_tax.getText().toString().trim());
            }
            json.put("shippingamount", "0");
            if(str_TaxID.equals("00000000-0000-0000-0000-000000000000")){
                json.put("taxid",null);
            }else{
                json.put("taxid",str_TaxID);
            }
            json.put("totaltax", String.valueOf(Tax_Amt));
            json.put("totalamount", String.valueOf(TotalPayableAmt));
            json.put("isesigned", false);
            json.put("useresignid", null);
            json.put("isactive", true);
            json.put("paymentmethod", str_fulfilment);
            postedJSON.put("salesreturnid", data);
            json.put("createdby", user_id);
            json.put("createdon", currentDateTime);
            json.put("invoicedate", currentDateTime);
            json.put("pricingdate", currentDateTime);
            json.put("invoiceproduct", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("InvoiceJSON ----"+json.toString());

       postCreateInvoice(json.toString(),currentDateTime,msg,data);
    }

    private void postCreateInvoice(String json,String ModifiedDateTime,String msg,String returnID) {
        try {
            App.getInstance().PostInvoiceCreate(json, token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("CreateInvoice--"+jsonObject.toString());
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    String data = jsonObject.getString("data");
                                    if (succeeded == true) {
                                        showSuccess(msg,returnID);
                                        Toast.makeText(Activity_CreateSalesReturn.this, "Invoice Generated Successful", Toast.LENGTH_SHORT).show();

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




    private void showSuccess(String msg,String returnID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_successalert, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        tittleAlert_tv.setText(msg);

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("SalesID",returnID);
                Intent in1 = new Intent(Activity_CreateSalesReturn.this, ActivitySalesShareInvoice.class);
                in1.putExtras(bundle1);
                startActivity(in1);
                dialog.dismiss();
            }
        });
    }



    public void AddMultipleItemAlert() {

        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                stList = new ArrayList<Model_ItemProducts>();
                filteredModelList = new ArrayList<Model_ItemProducts>();
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateInvoice.this);
                LayoutInflater inflater = Activity_CreateSalesReturn.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_addmultipleitemdislog, null);
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
                        } else if (!cb.isChecked()) {
                            for (int i = 0; i < stList.size(); i++) {
                                Model_ItemProducts singleStudent = stList.get(i);
                                singleStudent.setSelected(false);

                                stList.get(i).setProduct_quantity("0");
                                //  mAdapter.notifyItemChanged(i);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                txt_no_record2 = (AppCompatTextView) v.findViewById(R.id.txt_no_record2);
                mRecyclerProducts = (RecyclerView) v.findViewById(R.id.ry_productslist);
                mAdapter = new Adapter_ItemProducts(stList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_CreateSalesReturn.this);
                mRecyclerProducts.setLayoutManager(mLayoutManager);
                mRecyclerProducts.setItemAnimator(new DefaultItemAnimator());
                mRecyclerProducts.addItemDecoration(new ItemDividerDecoration(Activity_CreateSalesReturn.this, LinearLayoutManager.VERTICAL));
                // set the adapter object to the Recyclerview
                mRecyclerProducts.setAdapter(mAdapter);
                mRecyclerProducts.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_CreateSalesReturn.this, mRecyclerProducts, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        EditText MtQty_et = view.findViewById(R.id.MtQty_et);
                        EditText p4_et = view.findViewById(R.id.p4_tv);
                        AppCompatTextView mtAmount_tv = view.findViewById(R.id.mtAmount_tv);


                        if (filteredModelList.size() > 0) {

                            p4_et.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    if (!TextUtils.isEmpty(s)) {
                                        if (Double.parseDouble(p4_et.getText().toString()) > 0) {
                                            filteredModelList.get(position).setPrice_PerUnit(p4_et.getText().toString());
                                            //notifyDataSetChanged();
                                            double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));
                                        } else {
                                            filteredModelList.get(position).setPrice_PerUnit("0");
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                        }

                                    } else {
                                        filteredModelList.get(position).setPrice_PerUnit("0");
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                    }


                                }
                            });


                            view.findViewById(R.id.MtProduct_minus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Integer.parseInt(MtQty_et.getText().toString()) != 0) {

                                        MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) - 1));

                                        double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                                        filteredModelList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                        if (Integer.parseInt(MtQty_et.getText().toString()) == 0) {
                                            filteredModelList.get(position).setSelected(false);
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    }

                                }
                            });

                            view.findViewById(R.id.MtProduct_plus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) + 1));

                                    double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(filteredModelList.get(position).getPrice_PerUnit());
                                    mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                                    filteredModelList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                    if (Integer.parseInt(MtQty_et.getText().toString()) == 1) {
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

                                    } else if (!cb.isChecked()) {
                                        filteredModelList.get(position).setIstaxable(String.valueOf(false));
                                        mAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        } else {

                            p4_et.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    if (!TextUtils.isEmpty(s)) {
                                        if (Double.parseDouble(p4_et.getText().toString()) > 0) {
                                            stList.get(position).setPrice_PerUnit(p4_et.getText().toString());
                                            //notifyDataSetChanged();
                                            double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));
                                        } else {
                                            stList.get(position).setPrice_PerUnit("0");
                                            mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                        }

                                    } else {
                                        stList.get(position).setPrice_PerUnit("0");
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + "0.00");
                                    }


                                }
                            });


                            view.findViewById(R.id.MtProduct_minus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Integer.parseInt(MtQty_et.getText().toString()) != 0) {

                                        MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) - 1));

                                        double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                        mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                                        stList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                        if (Integer.parseInt(MtQty_et.getText().toString()) == 0) {
                                            stList.get(position).setSelected(false);
                                            mAdapter.notifyItemChanged(position);
                                        }
                                    }

                                }
                            });

                            view.findViewById(R.id.MtProduct_plus).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    MtQty_et.setText(String.valueOf(Integer.parseInt(MtQty_et.getText().toString()) + 1));

                                    double TotalAmt = Integer.parseInt(MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
                                    mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalAmt));

                                    stList.get(position).setProduct_quantity(MtQty_et.getText().toString());

                                    if (Integer.parseInt(MtQty_et.getText().toString()) == 1) {
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
                                        stList.get(position).setIstaxable("true");
                                        mAdapter.notifyDataSetChanged();

                                    } else if (!cb.isChecked()) {
                                        stList.get(position).setIstaxable("false");
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
                        dialog.dismiss();
                        alert="true";
                    }
                });
                v.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        alert="true";
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
                        String Description = "";
                        String Product_price = "";
                        String TotalItem_price = "";
                        String Price_PerUnit = "";
                        String istaxable = "";
                        String Order_productid = "";
                        String Upsc_code = "";
                        String priceTag = "false";

                        for (int i = 0; i < stList.size(); i++) {
                            Model_ItemProducts singleStudent = stList.get(i);

                            if (singleStudent.isSelected()) {


                                Product_id = singleStudent.getProduct_id();
                                Order_productid = singleStudent.getOrder_productid();
                                Product_name = singleStudent.getProduct_name();
                                Product_imageurl = singleStudent.getProduct_imageurl();
                                Product_quantity = singleStudent.getProduct_quantity();
                                Product_price = singleStudent.getProduct_price();
                                TotalItem_price = singleStudent.getTotalItem_price();
                                Price_PerUnit = singleStudent.getPrice_PerUnit();
                                istaxable = singleStudent.getIstaxable();
                                Upsc_code = singleStudent.getUpsc_code();
                                Description = singleStudent.getDescription();

                                double TotalAmt = Double.parseDouble(Price_PerUnit) * Double.parseDouble(Product_quantity);

                                boolean isExsits = false;

                                if (list.size() > 0) {

                                    for (int j = 0; j < list.size(); j++) {

                                        if (list.get(j).getOrder_productid().equals(Product_id)) {

                                            int getProduct = Integer.parseInt(list.get(j).getProduct_quantity());

                                            getProduct += Integer.parseInt(Product_quantity);

                                            list.get(j).setProduct_quantity(String.valueOf(getProduct));
                                            list.get(j).setIstaxable(String.valueOf(istaxable));
                                            list.get(j).setPrice_PerUnit(String.valueOf(Price_PerUnit));

                                            j = list.size();
                                            isExsits = false;

                                        } else {
                                            isExsits = true;
                                        }
                                    }
                                } else {
                                    isExsits = true;
                                }
                                if (isExsits) {

                                    list.add(new Model_SalesItemList("null", Product_name, Product_imageurl, Product_quantity, String.valueOf(TotalAmt), Price_PerUnit, Product_id, istaxable,Upsc_code,Description,priceTag,"0",Product_quantity));

                                }

                                adapter_model.notifyDataSetChanged();
                                Calculation();
                                // Toast.makeText(Activity_CreateInvoice.this, "Product has been saved successfully!", Toast.LENGTH_SHORT).show();

                            }


                        }

                        dialog.dismiss();
                        alert="true";
                    }
                });

                SQLiteController sqLiteController = new SQLiteController(Activity_CreateSalesReturn.this);
                sqLiteController.open();
                try {
                    long count = sqLiteController.fetchCount();
                    if (count > 0) {
                        //product
                        Cursor product_c = sqLiteController.readAllTableProduct();

                        if (product_c != null && product_c.moveToFirst()) {
                            String productName = "";
                            String Upsc_code = "";
                            String productId = "";
                            String all_description = "";
                            String istaxable = "";
                            String productImage = "null";
                            String productPrice = "0";

                            do {

                                productName = product_c.getString(product_c.getColumnIndex("all_product_name"));
                                productId = product_c.getString(product_c.getColumnIndex("all_product_id"));
                                //productImage = product_c.getString(product_c.getColumnIndex("all_product_img"));
                                productPrice = product_c.getString(product_c.getColumnIndex("all_product_price"));
                                istaxable = product_c.getString(product_c.getColumnIndex("all_istax"));
                                Upsc_code = product_c.getString(product_c.getColumnIndex("all_upsc"));
                                all_description = product_c.getString(product_c.getColumnIndex("all_description"));

                                SQLiteController sqLiteController2= new SQLiteController(Activity_CreateSalesReturn.this);
                                sqLiteController2.open();
                                try {
                                    Cursor CursorAssertTable = sqLiteController2.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,productId);
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
                                    sqLiteController2.close();
                                }



                                stList.add(new Model_ItemProducts(false, productId, productName
                                        , productImage, "0", "0", productPrice, productPrice, "", istaxable,Upsc_code,all_description));

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
                        if (filteredModelList.size() > 0) {
                            mRecyclerProducts.setVisibility(View.VISIBLE);
                            txt_no_record2.setVisibility(View.GONE);
                        } else {
                            mRecyclerProducts.setVisibility(View.GONE);
                            txt_no_record2.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Do something
                        Utils.getInstance().hideKeyboard(Activity_CreateSalesReturn.this);
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

    public static Activity_CreateSalesReturn getInstance() {
        return Viewdetails;
    }

    public void follow_convert() {

      //  String getLeadsId = list_uploads.get(itemPosition).getLeadsId();

        Calculation();


    }


    private void Calculation() {

        if (list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            txt_no_record.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            txt_no_record.setVisibility(View.VISIBLE);
        }

        TotalSum = 0.0;
        TotalTaxSum = 0.0;

        for (int i = 0; i < list.size(); i++) {

            if(!list.get(i).getReturnReason().equals("0")){

                double TotalAmt = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                TotalSum = TotalSum + TotalAmt;


                if (list.get(i).getIstaxable().equals("true")) {
                    double TotalAmtw = Double.parseDouble(list.get(i).getPrice_PerUnit()) * Double.parseDouble(list.get(i).getProduct_quantity());

                    TotalTaxSum = TotalTaxSum + TotalAmtw;
                }




            }

        }


      /*  for (int i = 0; i < list.size(); i++) {



        }*/
        txt_sub_total.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalSum));


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

              /*  if (!et_discount.getText().toString().isEmpty()) {
                    Dis_Amt = TotalSum * Double.parseDouble(et_discount.getText().toString()) / 100;
                    tv_discount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Dis_Amt));
                } else {
                    Dis_Amt = 0.0;
                }*/


                /*if (!et_shippingCharge.getText().toString().isEmpty()) {
                    Ship_Amt = Double.parseDouble(et_shippingCharge.getText().toString());
                    tv_shippingCharge.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Ship_Amt));

                } else {
                    Ship_Amt = 0.0;
                }*/


                if (Str_CS.equals("true")) {
                    if (!et_tax.getText().toString().isEmpty()) {
                        Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                        tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));
                    } else {
                        Tax_Amt = 0.0;
                    }
                }

                TotalPayableAmt = TotalSum -  Double.parseDouble(et_addCharge.getText().toString());

               // TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;

                System.out.println("Ship_Amt----"+Ship_Amt);
                System.out.println("Tax_Amt----"+Tax_Amt);
                System.out.println("TotalSum----"+TotalSum);
                System.out.println("TotalPayableAmt----"+TotalPayableAmt);

                txt_payable_amount.setText("-"+Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        DatePickerDialog datePickerDialog = null;
        switch (id) {
            case DATE_PICKER1_ID:
                Calendar d = Calendar.getInstance();
                d.add(d.MONTH, 2);
                datePickerDialog = new DatePickerDialog(this, pickerListener1, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                // datePickerDialog.getDatePicker().setMaxDate(d.getTimeInMillis() - 1000);
                return datePickerDialog;
      /*      case DATE_PICKER2_ID:
                datePickerDialog = new DatePickerDialog(this, pickerListener2, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog;*/
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et_ReturnedDate.setText(new StringBuilder()
                    .append(selectedMonth + 1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date = null;
            try {
                date = sdf.parse(et_ReturnedDate.getText().toString());
                returnedDate_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

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
        boolean isExsits = false;
        Utils.getInstance().loadingDialog(this, "Please wait.");
        SQLiteController sqLiteController = new SQLiteController(Activity_CreateSalesReturn.this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetch_All_PRODUCT_Count();
            if (count > 0) {

                //product
                Cursor product_c = sqLiteController.readAllTableProduct();

                if (product_c != null && product_c.moveToFirst()) {
                    String productName = "null";
                    String productId = "";
                    String istaxable = "";
                    String all_description = "";
                    String productImage = "null";
                    String productPrice = "0";
                    String all_upsc = "0";
                    String upscCode = "0";
                    String PriceTag = "false";
                    do {

                        all_upsc = product_c.getString(product_c.getColumnIndex("all_upsc"));

                        if (B_code.equals(all_upsc)) {

                            productName = product_c.getString(product_c.getColumnIndex("all_product_name"));
                            productId = product_c.getString(product_c.getColumnIndex("all_product_id"));
                            //productImage = product_c.getString(product_c.getColumnIndex("all_product_img"));
                            productPrice = product_c.getString(product_c.getColumnIndex("all_product_price"));
                            istaxable = product_c.getString(product_c.getColumnIndex("all_istax"));
                            upscCode = product_c.getString(product_c.getColumnIndex("all_upsc"));
                            all_description = product_c.getString(product_c.getColumnIndex("all_description"));

                            SQLiteController sqLiteController2= new SQLiteController(Activity_CreateSalesReturn.this);
                            sqLiteController2.open();
                            try {
                                Cursor CursorAssertTable = sqLiteController2.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,productId);
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
                                sqLiteController2.close();
                            }

                        }

                    } while (product_c.moveToNext());


                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); j++) {

                            if (list.get(j).getOrder_productid().equals(productId)) {
                                int getProduct = Integer.parseInt(list.get(j).getProduct_quantity());

                                getProduct += Integer.parseInt("1");

                                list.get(j).setProduct_quantity(String.valueOf(getProduct));

                                j = list.size();
                                isExsits = false;
                            } else {
                                isExsits = true;
                            }
                        }
                    } else {
                        isExsits = true;
                    }
                    if (productName.equals("null")) {
                        isExsits = false;
                        Toast.makeText(this, "Product Not found for this BarCode!", Toast.LENGTH_SHORT).show();
                    }
                    if (isExsits) {

                        double TotalAmt = Double.parseDouble(productPrice) * Double.parseDouble("0");
                        list.add(new Model_SalesItemList("null", productName, productImage, "1", String.valueOf(TotalAmt), productPrice, productId, istaxable,upscCode,all_description,PriceTag,"0","1"));
                    }

                    adapter_model.notifyDataSetChanged();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this);
        LayoutInflater inflater = Activity_CreateSalesReturn.this.getLayoutInflater();
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

                list.clear();
                adapter_model.notifyDataSetChanged();
                Calculation();
                sr_OrStatus_tv.setText("-");


                for (int j = 0; j < arCustomerName.size(); j++) {
                    if (arrayAdapter.getItem(position).equalsIgnoreCase(arCustomerName.get(j).toString())) {
                        pos_[0] = j;
                    }
                }

                System.out.println("position---"+ pos_[0]);

                tv_customerInSp.setText(arCustomerName.get(pos_[0]));
                str_customer = arCustomerName.get(pos_[0]);
                str_customerID = arCustomerID.get(pos_[0]);

                JSONObject json = new JSONObject();
                try {
                  //  json.put("referenceid","C47B8833-795C-42B6-A61B-384AF8DB8A6D");
                    json.put("referenceid","4FA2564B-5CDB-4A64-9386-94B8F5A8D9A8");
                    json.put("customerid",str_customerID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postRef(json.toString());

                System.out.println("-----POSTREF---"+json.toString());


                System.out.println("-----pos_[0]_customerNew---"+str_customer);
                System.out.println("-------pos_[0]_customerIDNew---"+str_customerID);

                GetCustomerInfo(pos_[0]);

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

    private void postRef(String json) {
        Progress_dialog.show();
        try {
            App.getInstance().PostCustomerRef(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
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
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Progress_dialog != null) {
                                    if (Progress_dialog.isShowing()) {
                                        Progress_dialog.dismiss();
                                    }
                                }
                                try {
                                    Utils.getInstance().dismissDialog();
                                    arOrderNum = new ArrayList<String>();
                                    arOrderNum.clear();

                                    arOrderID = new ArrayList<String>();
                                    arOrderID.clear();

                                    arINNum = new ArrayList<String>();
                                    arINNum.clear();

                                    arINID = new ArrayList<String>();
                                    arINID.clear();

                                    final JSONArray jr = new JSONArray(res);
                                    System.out.println("-----jr---"+jr.toString());

                                    if(jr.length()>0){


                                    }else {
                                        Toast.makeText(Activity_CreateSalesReturn.this, "No sales & Invoice records were found!", Toast.LENGTH_SHORT).show();
                                        radioNone.setChecked(true);
                                        radioOrder.setChecked(false);
                                        radioInvoice.setChecked(false);

                                    }
                                    for (int i = 0; i < jr.length(); i++) {
                                        JSONObject js = jr.getJSONObject(i);
                                        String orderid = js.getString("orderid");
                                        String ordernumber = js.getString("ordernumber");
                                        String invoiceid = js.getString("invoiceid");
                                        String invoicenumber = js.getString("invoicenumber");

                                        arOrderNum.add(ordernumber);
                                        arOrderID.add(orderid);

                                        arINNum.add(invoicenumber);
                                        arINID.add(invoiceid);



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


    private void GetCustomerInfo(int pos) {
        if (!str_customer.equals("Choose Customer")) {
            str_BilingID = arBillingID.get(pos);
            str_DeliveryID = arDeliveryID.get(pos);
            str_Taxable = arTaxable.get(pos);
            str_TaxID = arTaxID.get(pos);
            String SeletedTerms = arTerms.get(pos);

            if(str_TaxID.equals("null")){
                str_TaxID="00000000-0000-0000-0000-000000000000";
            }


            if (str_Taxable.equals("true")) {
                Str_CS = "true";
                CS_chkSelected.setChecked(true);
                if (!et_tax.getText().toString().equals("")) {
                    Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                }
            } else {
                Str_CS = "false";
                CS_chkSelected.setChecked(false);
                if (!et_tax.getText().toString().equals("")) {
                    Tax_Amt = TotalTaxSum * Double.parseDouble("0.00") / 100;
                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    bill_tv.setText("No address found!");
                    del_tv.setText("No address found!");

                    //OrdersAddress
                    SQLiteController sqLiteControllerC = new SQLiteController(Activity_CreateSalesReturn.this);
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

                                    if (str_BilingID.equals(BId)) {

                                        bill_tv.setText(line2 + ", " + stateorprovince + ", \n" + city + ", " + postalcode + ", " + country);

                                    }

                                    if (str_DeliveryID.equals(BId)) {
                                        del_tv.setText(line2 + ", " + stateorprovince + ", \n" + city + ", " + postalcode + ", " + country);

                                    }


                                } while (C_Address.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteControllerC.close();
                    }


                    SQLiteController sqLiteController = new SQLiteController(Activity_CreateSalesReturn.this);
                    sqLiteController.open();
                    try {
                        long fetchCsStateCount = sqLiteController.fetchCsStateCount();
                        if (fetchCsStateCount > 0) {

                            Cursor Cs_State = sqLiteController.readTableCs_State();
                            if (Cs_State.moveToFirst()) {
                                do {
                                    @SuppressLint("Range") String cs_id = Cs_State.getString(Cs_State.getColumnIndex("cs_id"));
                                    if(str_customerID.equals(cs_id)) {
                                        @SuppressLint("Range") String cs_state_id = Cs_State.getString(Cs_State.getColumnIndex("cs_state_id"));

                                        SQLiteController sqLiteController_tx = new SQLiteController(Activity_CreateSalesReturn.this);
                                        sqLiteController_tx.open();
                                        try {
                                            long fetchCsTaxCount = sqLiteController_tx.fetchCsTaxCount();
                                            if (fetchCsTaxCount > 0) {

                                                Cursor Cs_tax = sqLiteController_tx.readTableCs_Tax();
                                                if (Cs_tax.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String cs_tax_stateis = Cs_tax.getString(Cs_tax.getColumnIndex("cs_tax_stateis"));

                                                        if(cs_state_id.equals(cs_tax_stateis)) {

                                                            @SuppressLint("Range") String cs_tax_percent = Cs_tax.getString(Cs_tax.getColumnIndex("cs_tax_percent"));
                                                            et_tax.setText(cs_tax_percent);
                                                            Calculation();

                                                        }

                                                    } while (Cs_tax.moveToNext());
                                                }
                                            }
                                        } finally {
                                            sqLiteController_tx.close();
                                        }
                                    }

                                } while (Cs_State.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteController.close();
                    }

                }
            });


        }
    }


    public void showOrderSpinnerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateSalesReturn.this);
        LayoutInflater inflater = Activity_CreateSalesReturn.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_searchdislog,null);
        builder.setView(v);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        getWindow().setGravity(Gravity.BOTTOM);

        AppCompatImageView rippleViewClose = (AppCompatImageView) v.findViewById(R.id.close);
        SListView = (ListView) v.findViewById(R.id.list);
        EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        AppCompatTextView ald_tittle_tv = (AppCompatTextView) v.findViewById(R.id.ald_tittle_tv);
        ald_tittle_tv.setText("Select or Search");
        searchBox.setHint("    Enter Number");

        AppCompatTextView txt_no_record = (AppCompatTextView) v.findViewById(R.id.txt_no_record);



        if(SelectedDrop.equals("1")){
            if (arOrderNum.size()>0){
                txt_no_record.setVisibility(View.GONE);
                SListView.setAdapter(SalesNumAdapter);
            } else {
                txt_no_record.setVisibility(View.VISIBLE);
            }
        }

        if(SelectedDrop.equals("2")){
            if (arINNum.size()>0){
                txt_no_record.setVisibility(View.GONE);
                SListView.setAdapter(SalesNumAdapter);
            } else {
                txt_no_record.setVisibility(View.VISIBLE);
            }
        }



        final int[] pos_ = new int[1];
        SListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {



                System.out.println("SelectedDrop---"+SelectedDrop);

                if(SelectedDrop.equals("1")){

                    for (int j = 0; j < arOrderNum.size(); j++) {
                        if (SalesNumAdapter.getItem(position).equalsIgnoreCase(arOrderNum.get(j).toString())) {
                            pos_[0] = j;
                        }
                    }

                    System.out.println("position---"+ pos_[0]);

                    sr_OrStatus_tv.setText(arINNum.get(pos_[0]));
                    tv_OrderInSp.setText(arOrderNum.get(pos_[0]));
                    str_OrderNum = arOrderNum.get(pos_[0]);
                    str_OrderId = arOrderID.get(pos_[0]);

                    GetOrderDetails();
                }

                if(SelectedDrop.equals("2")){

                    for (int j = 0; j < arINNum.size(); j++) {
                        if (SalesNumAdapter.getItem(position).equalsIgnoreCase(arINNum.get(j).toString())) {
                            pos_[0] = j;
                        }
                    }

                    System.out.println("position---"+ pos_[0]);

                    sr_OrStatus_tv.setText(arOrderNum.get(pos_[0]));
                    tv_OrderInSp.setText(arINNum.get(pos_[0]));
                    str_OrderNum = arINNum.get(pos_[0]);
                    str_InvoiceID = arINID.get(pos_[0]);


                    GetInvoiceDetails();

                }

                System.out.println("-----pos_[0]str_OrderNum---"+str_OrderNum);
                System.out.println("-------pos_[0]str_OrderId---"+str_OrderId);



                dialog.dismiss();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SalesNumAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

                SalesNumAdapter.getFilter().filter(s, new Filter.FilterListener() {
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



    @SuppressLint("Range")
    public void GetOrderDetails() {

        if (!str_OrderNum.equals("Choose Order Number")) {

            if (Connectivity.isConnected(Activity_CreateSalesReturn.this) &&
                    Connectivity.isConnectedFast(Activity_CreateSalesReturn.this)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Progress_dialog.show();
                       // Utils.getInstance().loadingDialog(Activity_CreateSalesReturn.this, "Please wait.");
                        try {
                            App.getInstance().PostPresaleOrder(str_OrderId, "",token, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                       // Utils.getInstance().dismissDialog();
                                        final String res = response.body().string();

                                        runOnUiThread(new Runnable() {
                                            @SuppressLint("Range")
                                            @Override
                                            public void run() {
                                                if (list.size() > 0) list.clear();

                                                try {
                                                    final JSONObject jsonObject = new JSONObject(res);
                                                    JSONObject jsData = jsonObject.getJSONObject("data");
                                                    String customerid = jsData.getString("customerid");
                                                    String submitdate = jsData.getString("submitdate");
                                                    String datefulfilled = jsData.getString("datefulfilled");
                                                    String salesrepid = jsData.getString("salesrepid");
                                                    String warehouseid = jsData.getString("warehouseid");
                                                    String paymenttypeid = jsData.getString("paymenttypeid");
                                                    String deliverynote = jsData.getString("deliverynote");
                                                    String shipnote = jsData.getString("shipnote");
                                                    String termsconditions = jsData.getString("termsconditions");
                                                    String memo = jsData.getString("memo");
                                                    String discountpercentage = jsData.getString("discountpercentage");
                                                    String discountamount = jsData.getString("discountamount");
                                                    String freightamount = jsData.getString("freightamount");
                                                    String taxperecentage = jsData.getString("taxperecentage");
                                                    String customeristaxable = jsData.getString("customeristaxable");
                                                    String netterms = jsData.getString("netterms");
                                                    orderNo = jsData.getString("ordernumber");
                                                    OrderStatu = jsData.getString("status");
                                                    Orderidi = jsData.getString("orderId");


                                                    JSONArray OrderProducts = jsData.getJSONArray("orderProducts");
                                                    String description = "";
                                                    for (int i = 0; i < OrderProducts.length(); i++) {
                                                        JSONObject js = OrderProducts.getJSONObject(i);
                                                        String productname = js.getString("productname");
                                                        String quantity = js.getString("quantity");
                                                        String baseamount = js.getString("baseamount");
                                                        String priceperunit = js.getString("priceperunit");
                                                        String productid = js.getString("productid");
                                                        String orderid = js.getString("orderid");
                                                        String id = js.getString("id");
                                                        String itemistaxable = js.getString("itemistaxable");
                                                        String upccode = js.getString("upccode");

                                                        SQLiteController sqLiteController = new SQLiteController(Activity_CreateSalesReturn.this);
                                                        sqLiteController.open();
                                                        try {
                                                            long count = sqLiteController.fetchOrderCount();
                                                            if (count > 0) {
                                                                Cursor product_c1 = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);

                                                               // Cursor product_c1 = sqLiteController.readAllTableProduct();
                                                                if (product_c1 != null && product_c1.moveToFirst()) {
                                                                    String productImage ="null";
                                                                    do {

                                                                        @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                                        SQLiteController sqLiteController2= new SQLiteController(Activity_CreateSalesReturn.this);
                                                                        sqLiteController2.open();
                                                                        try {
                                                                            Cursor CursorAssertTable = sqLiteController2.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
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
                                                                            sqLiteController2.close();
                                                                        }


                                                                        if(product_id.equals(productid)){
                                                                            //@SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                                            @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                            @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                                                            @SuppressLint("Range") String all_product_price = product_c1.getString(product_c1.getColumnIndex("all_product_price"));
                                                                            description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                                                            double TotalAmt = Double.parseDouble(all_product_price) * Double.parseDouble(quantity);
                                                                            list.add(new Model_SalesItemList(id, productname, productImage, quantity, String.valueOf(TotalAmt), all_product_price, productid, itemistaxable,upccode, description,"false","0",quantity));

                                                                            adapter_model.notifyDataSetChanged();

                                                                        }

                                                                    } while (product_c1.moveToNext());
                                                                }


                                                            }
                                                        } finally {
                                                            sqLiteController.close();
                                                        }

                                                        Calculation();

                                                    }

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if (customeristaxable.equals("true")) {
                                                                Str_CS = "true";
                                                                CS_chkSelected.setChecked(true);
                                                                if (!et_tax.getText().toString().equals("")) {
                                                                    Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                                                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                                                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt ;
                                                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                                                }
                                                            } else {
                                                                Str_CS = "false";
                                                                CS_chkSelected.setChecked(false);
                                                                if (!et_tax.getText().toString().equals("")) {
                                                                    Tax_Amt = TotalTaxSum * Double.parseDouble("0.00") / 100;
                                                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                                                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                                                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                                                }
                                                            }

                                                            for (int i = 0; i < arCustomerID.size(); i++) {
                                                                String  SCustID = arCustomerID.get(i);
                                                                if(SCustID.contains(customerid)) {
                                                                    str_customer = arCustomerName.get(i);
                                                                    str_customerID = arCustomerID.get(i);
                                                                    tv_customerInSp.setText(str_customer);

                                                                    GetCustomerInfo(i);
                                                                }
                                                            }

                                                            sp_salesRep.setSelection(arSalesRepID.indexOf(salesrepid));
                                                            spWarehouse.setSelection(arWarehouseID.indexOf(warehouseid));
                                                            //spFulfillmentType.setSelection(arFulfillID.indexOf(paymenttypeid));

                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                            Date date = null;
                                                            Date date1 = null;
                                                            try {
                                                                date = sdf.parse(submitdate);
                                                                date1 = sdf.parse(datefulfilled);
                                                                String OrderDate_str = new SimpleDateFormat("MM/dd/yyyy").format(date);
                                                                String deliveryDatej = new SimpleDateFormat("MM/dd/yyyy").format(date1);

                                                                if(deliverynote.equals("")&&deliverynote.isEmpty()){
                                                                    et_deliveryNotes.setText("");
                                                                }else {
                                                                    if(deliverynote.equals("null")){
                                                                        et_deliveryNotes.setText("");
                                                                    }else {
                                                                        et_deliveryNotes.setText(deliverynote);
                                                                    }

                                                                }

                                                                if(shipnote.equals("")&&shipnote.isEmpty()){
                                                                    et_packShipNote.setText("");
                                                                }else {
                                                                    if(shipnote.equals("null")){
                                                                        et_packShipNote.setText("");
                                                                    }else {
                                                                        et_packShipNote.setText(shipnote);
                                                                    }

                                                                }

                                                                if(termsconditions.equals("")&&termsconditions.isEmpty()){
                                                                    et_terms.setText("");
                                                                }else {
                                                                    if(termsconditions.equals("null")){
                                                                        et_terms.setText("");
                                                                    }else {
                                                                        et_terms.setText(termsconditions);
                                                                    }

                                                                }

                                                                if(memo.equals("")&&memo.isEmpty()){
                                                                    et_memo.setText("");
                                                                }else {
                                                                    if(memo.equals("null")){
                                                                        et_memo.setText("");
                                                                    }else {
                                                                        et_memo.setText(memo);
                                                                    }

                                                                }

                                                                Double Dst = Double.parseDouble(discountamount)+Double.parseDouble("0.0");//freightamount
                                                                et_addCharge.setText(String.valueOf(Dst));

                                                                System.out.println("taxperecentage----"+taxperecentage);
                                                                et_tax.setText(taxperecentage);

                                                                /*SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                                                                Date date2 = null;
                                                                try {
                                                                    date2 = sdf2.parse(OrderDate_str);
                                                                    returnedDate_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date2);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }*/

                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                           // Utils.getInstance().dismissDialog();


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
                                            }
                                        });


                                    }

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        } else {
            tv_customerInSp.setText("Choose Customer");
            tv_customerInSp.setEnabled(true);
            spWarehouse.setSelection(arWarehouseID.indexOf("0"));
            et_deliveryNotes.setText("");
            et_packShipNote.setText("");
            et_terms.setText("");
            et_memo.setText("");
            et_shippingCharge.setText("0");
            et_tax.setText("0");
            TotalPayableAmt = 0.0;
            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(0.0));
            orderNo = "0";
            OrderStatu = "New Order";
            Orderidi = "00000000-0000-0000-0000-000000000000";
            if (list.size() > 0) list.clear();
            adapter_model.notifyDataSetChanged();
            Calculation();
        }


    }

    @SuppressLint("Range")
    public void GetInvoiceDetails() {

        if (!str_OrderNum.equals("Choose Order Number")) {

            if (Connectivity.isConnected(Activity_CreateSalesReturn.this) &&
                    Connectivity.isConnectedFast(Activity_CreateSalesReturn.this)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Progress_dialog.show();
                       // Utils.getInstance().loadingDialog(Activity_CreateSalesReturn.this, "Please wait.");
                        try {
                            App.getInstance().GetInvoiceSummary(str_InvoiceID, token, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                      //  Utils.getInstance().dismissDialog();
                                        final String res = response.body().string();

                                        runOnUiThread(new Runnable() {
                                            @SuppressLint("Range")
                                            @Override
                                            public void run() {
                                                if (list.size() > 0) list.clear();

                                                try {
                                                    final JSONObject jsData = new JSONObject(res);
                                                    String customerid = jsData.getString("customerid");
                                                    String submitdate = jsData.getString("invoicedate");
                                                    String datefulfilled = jsData.getString("datefulfilled");
                                                    String salesrepid = jsData.getString("salesrepid");
                                                    String warehouseid = jsData.getString("warehouseid");
                                                    String paymenttypeid = jsData.getString("paymenttypeid");
                                                    String deliverynote = jsData.getString("deliverynote");
                                                    String shipnote = jsData.getString("shipnote");
                                                    String termsconditions = jsData.getString("termsconditions");
                                                    String memo = jsData.getString("memo");
                                                    String discountpercentage = jsData.getString("discountpercentage");
                                                    String discountamount = jsData.getString("discountamount");
                                                    String freightamount = jsData.getString("freightamount");
                                                    //String taxperecentage = jsData.getString("taxperecentage");
                                                    String taxperecentage = "0";
                                                    String customeristaxable = jsData.getString("customeristaxable");
                                                    String netterms = jsData.getString("netterms");
                                                    orderNo = jsData.getString("ordernumber");
                                                    OrderStatu = jsData.getString("submitstatus");
                                                    str_OrderId = jsData.getString("orderid");


                                                    JSONArray OrderProducts = jsData.getJSONArray("invoiceproduct");
                                                    String description = "";
                                                    for (int i = 0; i < OrderProducts.length(); i++) {
                                                        JSONObject js = OrderProducts.getJSONObject(i);
                                                        String productname = js.getString("productname");
                                                        String quantity = js.getString("quantity");
                                                        String baseamount = js.getString("baseamount");
                                                        String priceperunit = js.getString("priceperunit");
                                                        String productid = js.getString("productid");
                                                        String orderid = js.getString("orderid");
                                                        String id = js.getString("id");
                                                        String itemistaxable = js.getString("itemistaxable");
                                                        String upccode = js.getString("upccode");

                                                        String rty = String.valueOf(new DecimalFormat("#").format(Double.parseDouble(quantity)));




                                                        SQLiteController sqLiteController = new SQLiteController(Activity_CreateSalesReturn.this);
                                                        sqLiteController.open();
                                                        try {
                                                            long count = sqLiteController.fetchOrderCount();
                                                            if (count > 0) {
                                                               // Cursor product_c1 = sqLiteController.readAllTableProduct();
                                                                Cursor product_c1 = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);

                                                                if (product_c1 != null && product_c1.moveToFirst()) {
                                                                    String productImage ="null";
                                                                    do {

                                                                        @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                                        SQLiteController sqLiteController2= new SQLiteController(Activity_CreateSalesReturn.this);
                                                                        sqLiteController2.open();
                                                                        try {
                                                                            Cursor CursorAssertTable = sqLiteController2.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
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
                                                                            sqLiteController2.close();
                                                                        }


                                                                        if(product_id.equals(productid)){
                                                                            //@SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                                            @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                            @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                                                            description = product_c1.getString(product_c1.getColumnIndex("all_description"));
                                                                            @SuppressLint("Range") String all_product_price = product_c1.getString(product_c1.getColumnIndex("all_product_price"));


                                                                            double TotalAmt = Double.parseDouble(all_product_price) * Double.parseDouble(rty);
                                                                            list.add(new Model_SalesItemList(id, productname, productImage, rty, String.valueOf(TotalAmt), all_product_price, productid, itemistaxable,upccode, description,"false","0",rty));

                                                                            adapter_model.notifyDataSetChanged();


                                                                        }

                                                                    } while (product_c1.moveToNext());
                                                                }


                                                            }
                                                        } finally {
                                                            sqLiteController.close();
                                                        }

                                                        Calculation();

                                                    }

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if (customeristaxable.equals("true")) {
                                                                Str_CS = "true";
                                                                CS_chkSelected.setChecked(true);
                                                                if (!et_tax.getText().toString().equals("")) {
                                                                    Tax_Amt = TotalTaxSum * Double.parseDouble(et_tax.getText().toString()) / 100;
                                                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                                                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                                                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                                                }
                                                            } else {
                                                                Str_CS = "false";
                                                                CS_chkSelected.setChecked(false);
                                                                if (!et_tax.getText().toString().equals("")) {
                                                                    Tax_Amt = TotalTaxSum * Double.parseDouble("0.00") / 100;
                                                                    tv_tax.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(Tax_Amt));


                                                                    TotalPayableAmt = TotalSum + Tax_Amt + Ship_Amt;
                                                                    txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(TotalPayableAmt));

                                                                }
                                                            }

                                                            for (int i = 0; i < arCustomerID.size(); i++) {
                                                                String  SCustID = arCustomerID.get(i);
                                                                if(SCustID.contains(customerid)) {
                                                                    str_customer = arCustomerName.get(i);
                                                                    str_customerID = arCustomerID.get(i);
                                                                    tv_customerInSp.setText(str_customer);

                                                                    GetCustomerInfo(i);
                                                                }
                                                            }

                                                            sp_salesRep.setSelection(arSalesRepID.indexOf(salesrepid));
                                                            spWarehouse.setSelection(arWarehouseID.indexOf(warehouseid));
                                                            spFulfillmentType.setSelection(arFulfillID.indexOf(paymenttypeid));

                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                            Date date = null;
                                                            Date date1 = null;
                                                            try {
                                                                date = sdf.parse(submitdate);

                                                                /*if(!(datefulfilled ==null)){

                                                                    date1 = sdf.parse(datefulfilled);
                                                                    String OrderDate_str = new SimpleDateFormat("MM/dd/yyyy").format(date);
                                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                                                                    Date date2 = null;
                                                                    try {
                                                                        date2 = sdf2.parse(OrderDate_str);
                                                                        returnedDate_str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date2);
                                                                    } catch (ParseException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }*/


                                                                //String deliveryDatej = new SimpleDateFormat("MM/dd/yyyy").format(date1);


                                                                et_deliveryNotes.setText(deliverynote);
                                                                et_packShipNote.setText(shipnote);
                                                                et_terms.setText(termsconditions);
                                                                et_memo.setText(memo);
                                                                Double Dst = Double.parseDouble(discountamount)+Double.parseDouble("0.0");//freightamount
                                                                et_addCharge.setText(String.valueOf(Dst));
                                                                System.out.println("taxperecentage----"+taxperecentage);
                                                                et_tax.setText(taxperecentage);



                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                          //  Utils.getInstance().dismissDialog();

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
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        } else {
            tv_customerInSp.setText("Choose Customer");
            tv_customerInSp.setEnabled(true);
            spWarehouse.setSelection(arWarehouseID.indexOf("0"));
            et_deliveryNotes.setText("");
            et_packShipNote.setText("");
            et_terms.setText("");
            et_memo.setText("");
            et_shippingCharge.setText("0");
            et_tax.setText("0");
            TotalPayableAmt = 0.0;
            txt_payable_amount.setText(Activity_OrdersHistory.currency_symbol + Utils.truncateDecimal(0.0));
            orderNo = "0";
            OrderStatu = "New Order";
            Orderidi = "00000000-0000-0000-0000-000000000000";
            if (list.size() > 0) list.clear();
            adapter_model.notifyDataSetChanged();
            Calculation();
        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}