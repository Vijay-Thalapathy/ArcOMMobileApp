package com.example.arcomdriver.products;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_WarehouseItem;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.ActivitySignIn;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_WarehouseItem;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.google.android.gms.vision.text.Line;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class Activity_CreateProduct extends Activity_Menu {
    private CoordinatorLayout cl;
    private String user_id,token,sImage="null",fileName_s,extension_S,Str_StatusName="Active";
    private RecyclerView mRecyclerView;
    AppCompatTextView txt_ImgName;
    private Adapter_WarehouseItem adapter;
    private ArrayList<Model_WarehouseItem> list = new ArrayList<>();

    private ArrayList<String> arProductTypeID = new ArrayList<>();
    private ArrayList<String> arProductTypeName = new ArrayList<>();

    private ArrayList<String> arBrandID = new ArrayList<>();
    private ArrayList<String> arBrandName = new ArrayList<>();

    private ArrayList<String> arClassID = new ArrayList<>();
    private ArrayList<String> arClassName = new ArrayList<>();

    private ArrayList<String> arVendorID = new ArrayList<>();
    private ArrayList<String> arVendorName = new ArrayList<>();

    private ArrayList<String> arGroupId = new ArrayList<>();
    private ArrayList<String> arGroupName = new ArrayList<>();

    private ArrayList<String> arPmethodId = new ArrayList<>();
    private ArrayList<String> arPmethodName = new ArrayList<>();


    private ArrayList<String> arPEachId = new ArrayList<>();
    private ArrayList<String> arPEachName = new ArrayList<>();

    private ArrayList<String> arLoc = new ArrayList<>();

    String strVendorId="00000000-0000-0000-0000-000000000000",strClassId="00000000-0000-0000-0000-000000000000",strBrandId="00000000-0000-0000-0000-000000000000",strProductTypeId="00000000-0000-0000-0000-000000000000",strProductGroupId,strProductMethodId="0",Filename_="Browse Files";
    String taxStr="0", settingsvalue="0";

    double Profit=0.0,Margin=0.0;


    //Toolbar toolbar;

    //IMAGE
    private static int IMG_RESULT = 1;
    private static int CAMERA_RESULT = 2;
    int image_layout;
    int PERMISSION_CODE = 111;
    public long fileSizeInMB;
    File compressedImage_front;
    AppCompatEditText et_pdName,et_pdUpc,et_pdSku,et_decribtion,et_pdItemCost,et_pdHand,et_pdVendorCost;
    AppCompatTextView pd_committed_tv,pd_available_tv;
    Spinner spStatus,sp_productType,sp_brand,sp_productClass,sp_vendor,sp_productGroup,spPricingMethod,spEach;


    String[] PERMISSIONS = {Manifest.permission.READ_MEDIA_VIDEO ,Manifest.permission.READ_MEDIA_IMAGES ,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    RadioGroup radioGroup_Tax,radioGroup_retur;
    RadioButton radioTax,radioNonTax,radioRetur,radioNonSales;
    AppCompatImageView view_img;

    AppCompatTextView ProductPercent_tv;

    LinearLayout warehouse_lll,vendor_ll;

    boolean wrBln =false;

    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusIDName = new ArrayList<>();

    public AlertDialog Progress_dialog;

  //  CheckBox returnCheck,SellCheck;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_createproduct);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_createproduct, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Add Product");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateProduct.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_CreateProduct.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE);
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


         cl = findViewById(R.id.cl);
        warehouse_lll = findViewById(R.id.warehouse_lll);
        vendor_ll = findViewById(R.id.vendor_ll);
        if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
            //Hide Warehouse
            warehouse_lll.setVisibility(View.GONE);
            vendor_ll.setVisibility(View.GONE);
        }else {
            //Show Warehouse
            warehouse_lll.setVisibility(View.VISIBLE);
            vendor_ll.setVisibility(View.VISIBLE);
        }
        //toolbar = findViewById(R.id.toolbar);
        view_img = findViewById(R.id.view_img);
        spStatus = findViewById(R.id.spPStatus);
        spEach = findViewById(R.id.spEach);

        radioGroup_Tax = (RadioGroup) findViewById(R.id.radioGroup_Tax);
        radioTax = (RadioButton) findViewById(R.id.radioTax);
        radioNonTax = (RadioButton) findViewById(R.id.radioNonTax);

        radioGroup_retur = (RadioGroup) findViewById(R.id.radioGroup_retur);
        radioRetur = (RadioButton) findViewById(R.id.radioRetur);
        radioNonSales = (RadioButton) findViewById(R.id.radioNonSales);

        /*   returnCheck = findViewById(R.id.returnCheck);
        SellCheck = findViewById(R.id.SellCheck);*/
        et_pdName = findViewById(R.id.et_pdName);
        et_pdUpc = findViewById(R.id.et_pdUpc);
        et_pdSku = findViewById(R.id.et_pdSku);
        et_decribtion = findViewById(R.id.et_decribtion);
        et_pdItemCost = findViewById(R.id.et_pdItemCost);
        et_pdVendorCost = findViewById(R.id.et_pdVendorCost);
        et_pdHand = findViewById(R.id.et_pdHand);
        pd_committed_tv = findViewById(R.id.pd_committed_tv);
        pd_available_tv = findViewById(R.id.pd_available_tv);
        ProductPercent_tv = findViewById(R.id.ProductPercent_tv);
        LinearLayout ll_taxview = findViewById(R.id.ll_taxview);

        if(settingsvalue.equals("1")) {
            //show
            ll_taxview.setVisibility(View.VISIBLE);
        }else {
            //hide
            ll_taxview.setVisibility(View.GONE);
            taxStr="2";
        }


        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateProduct.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateProduct.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateProduct.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

     /*   returnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (returnCheck.isChecked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ReturnCheckClicked");
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ReturnUNCheckClicked");
                        }
                    });

                }
            }
        });
        SellCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (SellCheck.isChecked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("SellCheckClicked");
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("SEllUnCheckClicked");
                        }
                    });

                }
            }
        });*/


        et_pdHand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {

                } else if (!TextUtils.isEmpty(s)) {
                    if (et_pdHand.getText().toString().startsWith(" ")) {
                        et_pdHand.setText("");
                    }else  if (et_pdHand.getText().toString().startsWith(".")) {
                        et_pdHand.setText("");
                    }else {
                        pd_available_tv.setText(et_pdHand.getText().toString().trim());

                    }



                }
            }
        });
        et_pdItemCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    //et_pdItemCost.setText("0");
                    Profit=0.0;
                    Margin=0.0;
                }else if (!TextUtils.isEmpty(s)) {
                    if (et_pdItemCost.getText().toString().startsWith(" ")) {
                        et_pdItemCost.setText("");
                    }else  if (et_pdItemCost.getText().toString().startsWith(".")) {
                        et_pdItemCost.setText("");
                    }else {

                        if(!et_pdVendorCost.getText().toString().isEmpty() && !et_pdItemCost.getText().toString().isEmpty()){
                            if(Double.parseDouble(et_pdVendorCost.getText().toString())>0 && Double.parseDouble(et_pdItemCost.getText().toString())>0){

                                Profit  = Double.parseDouble(et_pdItemCost.getText().toString()) - Double.parseDouble(et_pdVendorCost.getText().toString());

                                double m =Profit/Double.parseDouble(et_pdItemCost.getText().toString());

                                Margin =m*100;

                                ProductPercent_tv.setText("Margin : "+String.valueOf(Utils.truncateDecimal(Margin))+"% | Profit : $"+String.valueOf(Utils.truncateDecimal(Profit))+"");

                            }else {
                                ProductPercent_tv.setText("Margin : 0.00% | Profit : $0.00");
                            }


                        }



                    }
                }
            }
        });

        et_pdVendorCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                   // et_pdVendorCost.setText("0");
                    Profit=0.0;
                    Margin=0.0;
                } else if (!TextUtils.isEmpty(s)) {
                    if (et_pdVendorCost.getText().toString().startsWith(" ")) {
                        et_pdVendorCost.setText("");
                    }else  if (et_pdVendorCost.getText().toString().startsWith(".")) {
                        et_pdVendorCost.setText("");
                    }else {

                        if(!et_pdVendorCost.getText().toString().isEmpty() && !et_pdItemCost.getText().toString().isEmpty()){
                            if(Double.parseDouble(et_pdVendorCost.getText().toString())>0 && Double.parseDouble(et_pdItemCost.getText().toString())>0){

                                Profit  = Double.parseDouble(et_pdItemCost.getText().toString()) - Double.parseDouble(et_pdVendorCost.getText().toString());

                                double m =Profit/Double.parseDouble(et_pdItemCost.getText().toString());

                                Margin =m*100;

                                ProductPercent_tv.setText("Margin : "+String.valueOf(Utils.truncateDecimal(Margin))+"% | Profit : $"+String.valueOf(Utils.truncateDecimal(Profit))+"");

                            }else {
                                ProductPercent_tv.setText("Margin : 0.00% | Profit : $0.00");
                            }
                        }



                    }
                }
            }
        });


        txt_ImgName = findViewById(R.id.txt_ImgName);
        sp_productType = findViewById(R.id.sp_productType);
        sp_brand = findViewById(R.id.sp_brand);
        sp_productClass = findViewById(R.id.sp_productClass);
        sp_vendor = findViewById(R.id.sp_vendor);
        sp_productGroup = findViewById(R.id.sp_productGroup);
        spPricingMethod = findViewById(R.id.spPricingMethod);

        mRecyclerView = findViewById(R.id.rc_WarehouseItems);

        adapter = new Adapter_WarehouseItem(Activity_CreateProduct.this,list);
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

        radioGroup_Tax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioTax) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taxStr="1";
                          //  PayTypeFlag = "Full";
                        }
                    });

                }else if (checkedId == R.id.radioNonTax) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taxStr="2";
                           // PayTypeFlag = "Partial";
                        }
                    });

                }
            }

        });

        radioGroup_retur.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioRetur) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }else if (checkedId == R.id.radioNonSales) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }

        });


        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/


        findViewById(R.id.btn_cancelCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateProduct.this, Activity_CustomerHistory.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.select_Img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_CreateProduct.this);
                image_layout = 1;
                select_image();

            }
        });
        findViewById(R.id.fabWarehouse_adds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(new Model_WarehouseItem("00000000-0000-0000-0000-000000000000","LocationType", "Location", "0","0","0","0","0","0"));
                adapter.notifyDataSetChanged();

            }
        });

        findViewById(R.id.btn_saveCustomer).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                System.out.println("compressedImage_front---"+compressedImage_front);
                System.out.println("Filename_---"+Filename_);

                Utils.getInstance().hideKeyboard(Activity_CreateProduct.this);
                if (Connectivity.isConnected(Activity_CreateProduct.this) &&
                        Connectivity.isConnectedFast(Activity_CreateProduct.this)) {
                    if (Utils.getInstance().checkIsEmpty(et_pdName)) {
                        Utils.getInstance().snackBarMessage(v,"Enter Product Name!");
                    } else if (Utils.getInstance().checkIsEmpty(et_pdUpc)) {
                        Utils.getInstance().snackBarMessage(v,"Enter UPC Code!");
                    } else if (Utils.getInstance().checkIsEmpty(et_pdItemCost)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the selling price!");
                    }else if (Utils.getInstance().checkIsEmpty(et_pdVendorCost)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the vendor cost!");
                    }else if (strProductGroupId.equals("0")) {
                        Utils.getInstance().snackBarMessage(v,"Select Product Group!");
                    }else if (strProductMethodId.equals("0")) {
                        Utils.getInstance().snackBarMessage(v,"Select Pricing Method!");
                    }else if (taxStr.equals("0")) {
                        Utils.getInstance().snackBarMessage(v,"Select Tax Type!");
                    }else if (Double.parseDouble(et_pdItemCost.getText().toString()) < Double.parseDouble(et_pdVendorCost.getText().toString())){
                        Utils.getInstance().snackBarMessage(v,"Selling price greater than Vendor Cost!");
                    }else if (Utils.getInstance().checkIsEmpty(et_pdHand)) {
                        Utils.getInstance().snackBarMessage(v,"Enter On hand!");
                    }/*else if (Double.parseDouble(et_pdItemCost.getText().toString()) == 0){
                        Utils.getInstance().snackBarMessage(v,"Selling price greater than zero!");
                    } */else {

                        /*if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                            //Hide
                            wrBln =true;
                        }else {
                            //Show
                            if (strVendorId.equals("00000000-0000-0000-0000-000000000000")) {
                                Utils.getInstance().snackBarMessage(v,"Select Vendors!");
                                wrBln =false;
                            }else {
                                wrBln =true;
                            }
                        }*/

                        /*
                        if(wrBln){
                            getValidation(v);
                        }*/

                        getValidation(v);

                    }

                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

            }
        });

        GetProductMaster();
        GetProductGroup();
        GetProductMethod();

        list.add(new Model_WarehouseItem("00000000-0000-0000-0000-000000000000","LocationType", "Location", "0","0","0","0","0","0"));
        adapter.notifyDataSetChanged();


        sp_vendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strVendorId = arVendorID.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        sp_productClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strClassId = arClassID.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        sp_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strBrandId = arBrandID.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        sp_productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strProductTypeId = arProductTypeID.get(position);
                System.out.println("strProductTypeId--"+strProductTypeId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        sp_productGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strProductGroupId = arGroupId.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        spPricingMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                strProductMethodId = arPmethodId.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        arStatusID.add("0");
        arStatusID.add("1");

        arStatusIDName.add("Active");
        arStatusIDName.add("Inactive");


        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_StatusName = arStatusIDName.get(position);
                System.out.println("Str_StatusName--"+Str_StatusName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arStatusIDName);
        adapterStatus.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spStatus.setAdapter(adapterStatus);
            }
        });


        arPEachName.add("Each");
        arPEachId.add("0");



        ArrayAdapter<String> adapterEach = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arPEachName);
        adapterEach.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spEach.setAdapter(adapterEach);
            }
        });

        spEach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Str_StatusName = arPEachName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getValidation(final View v) {
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate1 = sdf.format(date1);


        if(compressedImage_front !=null){
            File file1 = new File(compressedImage_front.getAbsolutePath());

            Log.v("LOG_TAG", "compressedImage_front" + compressedImage_front);

            Log.v("LOG_TAG", "getAbsolutePath()" + compressedImage_front.getAbsolutePath());
            Log.v("LOG_TAG", "file1" + file1);

            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(file1));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
            byte[] b = baos.toByteArray();

            sImage = Base64.encodeToString(b, Base64.NO_WRAP);

            Log.e("LOG_TAG--", sImage);

        }

        if(list.size()>0) {


            ArrayList<String> stringArrayList = new ArrayList<String>();

            JSONObject postedJSON = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            boolean ware_b = true;
            boolean Loc_b = true;

            if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                //Hide Warehouse
            }else {
                //Show Warehouse
                for (int i = 0; i < list.size(); i++) {

                    String irl = list.get(i).getLocationType();

                    stringArrayList.add(list.get(i).getWarehouseID());

                    if (irl.equals("Select Location Type *")) {
                        ware_b = false;
                    }


                    postedJSON = new JSONObject();
                    try {
                        //  postedJSON.put("id","null");
                        postedJSON.put("locationtypeid", list.get(i).getLocationType().trim());
                        postedJSON.put("quantityonhand", list.get(i).getOnhand().trim());
                        postedJSON.put("available", list.get(i).getAvailable().trim());
                        postedJSON.put("warehouseid", list.get(i).getWarehouseID().trim());
                        postedJSON.put("onhand", list.get(i).getOnhand().trim());
                        postedJSON.put("committed", "0");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(postedJSON);
                }
            }



            String [] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);

            for (int k = 0; k < stringArray.length; k++) {
                for (int j = k + 1; j < stringArray.length; j++) {
                    if (stringArray[k].equals(stringArray[j])) {
                        Loc_b = false;
                    }
                }
            }

            if(ware_b==true){

                if(Loc_b==true) {

                    JSONObject json2 = new JSONObject();
                    try {
                        json2.put("fileName", fileName_s);
                        json2.put("extension", extension_S);
                        json2.put("type", "Product");
                        json2.put("isdefault", true);
                        if(sImage.equals("null")){
                            json2.put("data","null");
                        }else {
                            json2.put("data","data:image/jpeg;base64,"+sImage);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    array2.put(json2);

                    /*String mr= String.valueOf(Margin).trim();
                    String pr= String.valueOf(Profit).trim();
                    String pattern ="-?\\d+";

                    String mrS ="0";
                    if(mr.matches(pattern)){
                        String[] Array1 = mr.split("-");
                        mrS = Array1 [1];
                    }else {
                        mrS = mr;
                    }

                    String PrS ="0";
                    if(pr.matches(pattern)){
                        String[] Array1 = pr.split("-");
                        PrS = Array1 [1];
                    }else {
                        PrS = pr;
                    }

                    System.out.println("Margin---"+mrS);
                    System.out.println("ProNew---"+PrS);*/

                    String mr= Utils.truncateDecimal(Margin).trim();
                    String pr= Utils.truncateDecimal(Profit).trim();

                    String st1 = mr.replaceAll("\\s+","");
                    String st2 = pr.replaceAll("\\s+","");

                    JSONObject jsonp = new JSONObject();
                    try {
                        jsonp.put("pricemethodid", strProductMethodId);
                        jsonp.put("vendorcost",  et_pdVendorCost.getText().toString());
                        jsonp.put("price",  et_pdItemCost.getText().toString());
                        jsonp.put("uomid", "80B14AD0-9369-4F3F-9D81-F647C27C6157");
                        jsonp.put("id", "00000000-0000-0000-0000-000000000000");
                        jsonp.put("margin", String.valueOf(st1).trim());
                        jsonp.put("profit", String.valueOf(st2).trim());
                        jsonp.put("createdby", user_id);
                        jsonp.put("modifiedby", user_id);
                        jsonp.put("modifiedon", currentDate1);
                        jsonp.put("createdon", currentDate1);
                        jsonp.put("isactive", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println("MaginProduct---" + jsonp.toString());

                    JSONObject json = new JSONObject();
                    try {
                        json.put("id", "00000000-0000-0000-0000-000000000000");
                        json.put("productname", et_pdName.getText().toString());
                        json.put("vendorid", strVendorId);
                        json.put("classid", strClassId);
                        json.put("brandid", strBrandId);
                        json.put("producttypeid", strProductTypeId);
                        json.put("description", et_decribtion.getText().toString());
                        json.put("skucode", et_pdSku.getText().toString());
                        json.put("upccode", et_pdUpc.getText().toString());
                        json.put("price", et_pdItemCost.getText().toString());
                        json.put("quantityonhand", et_pdHand.getText().toString());
                        json.put("committed", pd_committed_tv.getText().toString());
                        json.put("available", pd_available_tv.getText().toString());
                        json.put("createdby", user_id);
                        json.put("createdon", currentDate1);
                        if(Str_StatusName.equals("Inactive")){
                            json.put("status","Inactive");
                            json.put("isactive",false);
                        }else {
                            json.put("status","Active");
                            json.put("isactive",true);
                        }
                        if (taxStr.equals("1")) {
                            json.put("istaxable", true);
                        } else {
                            json.put("istaxable", false);
                        }
                        json.put("productgroupid", strProductGroupId);
                        if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                            //Hide Warehouse
                        }else {
                            //Show Warehouse
                            json.put("warehouseproductstock", array);
                        }

                        json.put("uploadRequest", array2);
                        json.put("ppc", jsonp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println("PostProduct---" + json.toString());

                   PostProduct(json.toString());
                }else {
                    Utils.getInstance().snackBarMessage(v,"Location already selected!");

                }

            }else {
                    Utils.getInstance().snackBarMessage(v,"Please Select Location Type!");
            }
        }else {
            Utils.getInstance().snackBarMessage(v,"Please select Warehouse stock info!!");
        }



    }

    private void PostProduct(String json) {
        Progress_dialog.show();
        try {
            App.getInstance().PostProduct(json,token, new Callback() {
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

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utils.getInstance().GetMasterInsert(Activity_CreateProduct.this,"ProductCreate",Progress_dialog,"");

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


    // TODO Image upload method
    private void select_image() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    boolean granted = loadPermission(Manifest.permission.CAMERA,2);
                    if (granted) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,CAMERA_RESULT);
                    }

                } else if (items[item].equals("Choose from Library")) {
                   /* boolean gra = loadPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,1);
                    if (gra) {

                    }*/
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, IMG_RESULT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;

        if (resultCode == RESULT_OK) {

            if (requestCode == IMG_RESULT) {

                try {

                    if (image_layout == 1) {

                        Uri selectedImageUri = data.getData();

                        fileName_s = getFileName(Activity_CreateProduct.this, selectedImageUri);
                        extension_S = fileName_s.split("\\.")[1];
                        //GetFile
                        compressedImage_front = FileUtil.from(this, selectedImageUri);
                        //GetFileName
                        String fileName1 = getPathFromURI(selectedImageUri);
                        File f = new File(fileName1);
                        Filename_ = f.getName();
                        //Text name
                        txt_ImgName.setText(Filename_);
                        //GET IMAGE SIZE
                        long fileSizeInBytes = f.length();
                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        fileSizeInMB = fileSizeInKB / 1024;
                        // postMultiPartRequest();

                        //ViewImage
                        view_img.setVisibility(View.VISIBLE);

                        System.out.println("selectedImageUri---"+selectedImageUri);

                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                        // initialize byte stream
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        // compress Bitmap
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        // Initialize byte array
                        byte[] bytes=stream.toByteArray();
                        // get base64 encoded string
                        sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

                        view_img.setImageBitmap(bitmap);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (requestCode == CAMERA_RESULT) {

                try {

                    if (image_layout == 1) {


                        //GetFile
                        Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                        String sImage1 = savedImage(bitmap1);
                        compressedImage_front = new File(sImage1);

                        if (data.getData() == null) {
                            imageUri = getImageUri(getApplicationContext(), (Bitmap) data.getExtras().get("data"));
                        } else {
                            imageUri = data.getData();
                        }

                        fileName_s = getFileName(Activity_CreateProduct.this, imageUri);
                        extension_S = fileName_s.split("\\.")[1];

                        //compressedImage_front = FileUtil.from(this, imageUri);
                        //GetFileName
                        String fileName1 = getPathFromURI(imageUri);
                        File f = new File(fileName1);
                        Filename_ = f.getName();
                        //Text name
                         txt_ImgName.setText(Filename_);
                        //GET IMAGE SIZE
                        long fileSizeInBytes = f.length();
                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        fileSizeInMB = fileSizeInKB / 1024;

                        //  postMultiPartRequest();


                        //ViewImage
                        view_img.setVisibility(View.VISIBLE);

                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        // initialize byte stream
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        // compress Bitmap
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        // Initialize byte array
                        byte[] bytes=stream.toByteArray();
                        // get base64 encoded string
                        sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

                        view_img.setImageBitmap(bitmap);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }/*else if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
                switch (requestCode) {
                    case REQUEST_CHECK_SETTINGS_GPS:
                        switch (resultCode) {
                            case Activity.RESULT_OK:
                                getMyLocation();
                                break;
                            case Activity.RESULT_CANCELED:
                                finish();
                                break;
                        }
                        break;
                }

            }*/


        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);

    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    private boolean loadPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(Activity_CreateProduct.this,permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_CreateProduct.this,permission)) {
                ActivityCompat.requestPermissions(Activity_CreateProduct.this, new String[]{permission},requestCode);
            } else {
                ActivityCompat.requestPermissions(Activity_CreateProduct.this, new String[]{permission},requestCode);
            }
            return false;
        } else {
            return true;
        }

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

    private String savedImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File fileDirectory = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS) + "/DriverApp");
        try {
            fileDirectory.mkdirs();
            File f = new File(fileDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    private void GetProductMaster() {
          Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetProductMaster(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              Utils.getInstance().dismissDialog();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject js = new JSONObject(res);
                            JSONObject jsonObject =js.getJSONObject("data");
                            JSONArray data = jsonObject.getJSONArray("masters");

                            if (arProductTypeName.size() > 0) arProductTypeName.clear();
                            if (arProductTypeID.size() > 0) arProductTypeID.clear();

                            if (arBrandName.size() > 0) arBrandName.clear();
                            if (arBrandID.size() > 0) arBrandID.clear();

                            if (arClassName.size() > 0) arClassName.clear();
                            if (arClassID.size() > 0) arClassID.clear();

                            if (arVendorName.size() > 0) arVendorName.clear();
                            if (arVendorID.size() > 0) arVendorID.clear();

                            if (data.length() > 0) {
                              /*  arProductTypeName.add("Select Product Type *");
                                arProductTypeID.add("0");*/

                                //arBrandName.add("Select Brand Type *");
                                //arBrandID.add("0");

                                //arClassName.add("Select Product Class *");
                                //arClassID.add("0");

                                arVendorName.add("Select Vendor");
                                arVendorID.add("00000000-0000-0000-0000-000000000000");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js0 = data.getJSONObject(i);

                                    String Value_ =js0.getString("keyname");

                                    if(Value_.equals("producttype")){
                                        arProductTypeName.add(js0.getString("keytext"));
                                        arProductTypeID.add(js0.getString("keyvalue"));
                                    }else if(Value_.equals("productbrand")){
                                        arBrandName.add(js0.getString("keytext"));
                                        arBrandID.add(js0.getString("keyvalue"));
                                    }else if(Value_.equals("productclass")){
                                        arClassName.add(js0.getString("keytext"));
                                        arClassID.add(js0.getString("keyvalue"));
                                    }else if(Value_.equals("productvendor")){
                                        arVendorName.add(js0.getString("keytext"));
                                        arVendorID.add(js0.getString("keyvalue"));
                                    }

                                }
                                ArrayAdapter<String> adapterPType = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arProductTypeName);
                                adapterPType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sp_productType.setAdapter(adapterPType);
                                        sp_productType.setSelection(arProductTypeName.indexOf("Not Assigned"));
                                    }
                                });


                                ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arBrandName);
                                adapterBrand.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sp_brand.setAdapter(adapterBrand);
                                        sp_brand.setSelection(arBrandName.indexOf("Not Assigned"));
                                    }
                                });

                                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arClassName);
                                adapterClass.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sp_productClass.setAdapter(adapterClass);
                                        sp_productClass.setSelection(arClassName.indexOf("Not Assigned"));
                                    }
                                });

                                ArrayAdapter<String> adapterVendor = new ArrayAdapter<String>(Activity_CreateProduct.this,R.layout.spinner_item,arVendorName);
                                adapterVendor.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sp_vendor.setAdapter(adapterVendor);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.getInstance().dismissDialog();
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
    private void GetProductGroup() {
        //  Utils.getInstance().loadingDialog(this, "Please wait.");

        try {
            App.getInstance().GetProductGroup(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              //Utils.getInstance().dismissDialog();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject js = new JSONObject(res);
                            JSONArray data = js.getJSONArray("options");

                            if (arGroupName.size() > 0) arGroupName.clear();
                            if (arGroupId.size() > 0) arGroupId.clear();


                            if (data.length() > 0) {
                                arGroupName.add("Select Product Group *");
                                arGroupId.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js0 = data.getJSONObject(i);

                                    arGroupName.add(js0.getString("displayname"));
                                    arGroupId.add(js0.getString("id"));

                                }
                                ArrayAdapter<String> adapterPGroup = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arGroupName);
                                adapterPGroup.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sp_productGroup.setAdapter(adapterPGroup);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       // Utils.getInstance().dismissDialog();
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
    private void GetProductMethod() {
        //  Utils.getInstance().loadingDialog(this, "Please wait.");

        try {
            App.getInstance().GetPriceMethod(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              //Utils.getInstance().dismissDialog();
                        }
                    });

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject js = new JSONObject(res);
                            JSONArray data = js.getJSONArray("options");

                            if (arPmethodName.size() > 0) arPmethodName.clear();
                            if (arPmethodId.size() > 0) arPmethodId.clear();


                            if (data.length() > 0) {
                                arPmethodName.add("Select Pricing Method");
                                arPmethodId.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js0 = data.getJSONObject(i);

                                    arPmethodName.add(js0.getString("displayname"));
                                    arPmethodId.add(js0.getString("id"));

                                }
                                ArrayAdapter<String> adapterPGroup = new ArrayAdapter<String>(Activity_CreateProduct.this, R.layout.spinner_item,arPmethodName);
                                adapterPGroup.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spPricingMethod.setAdapter(adapterPGroup);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       // Utils.getInstance().dismissDialog();
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

    @SuppressLint("Range")
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }






}

