package com.example.arcomdriver.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ContactList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_ContactList;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.order.Activity_UpdateOrder;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.example.arcomdriver.salesreturn.Activity_CreateSalesReturn;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 02 Jan 2023*/
public class Activity_CreateCustomer extends Activity_Menu {
    private CoordinatorLayout cl;
    RadioGroup radioGroup_customertype;
    RadioButton radioBusiness,radioIndividual;
    AppCompatEditText et_TaxPercent,et_BillPincode,et_DeliveryPincode,et_BillAddress,et_DeliveryAddress,et_SecondaryContact_et,et_secondaryEmail_et,et_customerName,et_website,et_primaryEmail_et,et_primaryContact_et;
    Spinner spStatus,spIndustry,spDeliveryCountry,spDeliveryState,spDeliveryCity
            ,spBillCountry,spBillState,spBillCity,spNetTerms,spCustomerTaxCode;
    CheckBox Same_add_chk,CheckTax_ap;
    String  Primary__ ="false";
    LinearLayout Tax_ll;
    private String BFlag="2",Str_IndustryName="Not Assigned",Str_netTermName,Str_netTermID,Str_TaxCode,Str_TaxID,user_id,token,D_StateName="Select State",D_CityName="Select City",D_CountryName="Select Country",B_StateName="Select State",B_CityName="Select City",B_CountryName="Select Country";
    String Str_StatusName="Active",taxStr="2";
    private ArrayList<String> arStateID = new ArrayList<>();
    private ArrayList<String> arStateName = new ArrayList<>();
    private ArrayList<String> arCityID = new ArrayList<>();
    private ArrayList<String> arCityName = new ArrayList<>();
    private ArrayList<String> arCountryID = new ArrayList<>();
    private ArrayList<String> arCountryName = new ArrayList<>();
    private ArrayList<String> arIndustryID = new ArrayList<>();
    private ArrayList<String> arIndustryName = new ArrayList<>();
    private ArrayList<String> arTermsID = new ArrayList<>();
    private ArrayList<String> arTermsName = new ArrayList<>();
    private ArrayList<String> arTaxCode = new ArrayList<>();
    private ArrayList<String> arTaxID = new ArrayList<>();
    private ArrayList<String> arTaxPercent = new ArrayList<>();
    private ArrayList<String> arTaxName = new ArrayList<>();
    ArrayAdapter<String> adapterBType;
    ArrayAdapter<String> adapterCity;
    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusIDName = new ArrayList<>();
    String CusID,OrderId,CustomerName;
    AppCompatTextView qc0_tv,qc1_tv,qc3_tv,qc4_tv;
    RelativeLayout cs_er_ll,cs_er_ll2;
    LinearLayout email_ll3;
    TextInputLayout et_website_tl;
    AppCompatButton btn_addContacts;
    String  settingsvalue="0",CB_CountryName,CD_CountryName,CB_CityName,CD_CityName,CB_StateName,CD_StateName,QickCustomer ="0";
    ArrayList<String> CarStateID = new ArrayList<>();
    ArrayList<String> CarStateName = new ArrayList<>();
    ArrayList<String> CarCityID = new ArrayList<>();
    ArrayList<String> CarCityName = new ArrayList<>();
    ArrayList<String> CarCountryID = new ArrayList<>();
    ArrayList<String> CarCountryName = new ArrayList<>();
    ArrayAdapter<String> CadapterCity;
    ArrayAdapter<String> CadapterBType;
    Spinner CspDeliveryCountry,CspDeliveryState,CspDeliveryCity,CspBillCountry,CspBillState,CspBillCity;
    ArrayList<Model_ContactList> ctList = new ArrayList<Model_ContactList>();
    Adapter_ContactList cAdapter;
    AppCompatTextView customer_title_tv;
    AppCompatEditText cet_firstName,cet_lastName;
    LinearLayout name_ll,PContact_ll;

    public AlertDialog Progress_dialog;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_createcustomer, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Add Customer");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateCustomer.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_CreateCustomer.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CusID = extras.getString("CusID");
            System.out.println("Customer Create--"+CusID);
            if(CusID.equals("1")){
                OrderId = extras.getString("OrderId");
                CustomerName = extras.getString("CustomerName");

            }
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
        PContact_ll = findViewById(R.id.PContact_ll);
        customer_title_tv = findViewById(R.id.customer_title_tv);
        cet_firstName = findViewById(R.id.cet_firstName);
        cet_lastName = findViewById(R.id.cet_lastName);
        name_ll = findViewById(R.id.name_ll);
        btn_addContacts = findViewById(R.id.btn_addContacts);
        qc0_tv = findViewById(R.id.qc0_tv);
        qc1_tv = findViewById(R.id.qc1_tv);
        qc3_tv = findViewById(R.id.qc3_tv);
        qc4_tv = findViewById(R.id.qc4_tv);
        cs_er_ll = findViewById(R.id.cs_er_ll);
        cs_er_ll2 = findViewById(R.id.cs_er_ll2);
        email_ll3 = findViewById(R.id.email_ll3);
        et_customerName = findViewById(R.id.et_customerName);
        et_website = findViewById(R.id.et_website);
        et_website_tl = findViewById(R.id.et_website_tl);
        spStatus = findViewById(R.id.spCStatus);
        spIndustry = findViewById(R.id.spIndustry);
        et_primaryEmail_et = findViewById(R.id.et_primaryEmail_et);
        et_primaryContact_et = findViewById(R.id.et_primaryContact_et);
        et_secondaryEmail_et = findViewById(R.id.et_secondaryEmail_et);
        et_SecondaryContact_et = findViewById(R.id.et_SecondaryContact_et);
        et_DeliveryAddress = findViewById(R.id.et_DeliveryAddress);
        spDeliveryCountry = findViewById(R.id.spDeliveryCountry);
        spDeliveryState = findViewById(R.id.spDeliveryState);
        spDeliveryCity = findViewById(R.id.spDeliveryCity);
        et_DeliveryPincode = findViewById(R.id.et_DeliveryPincode);
        Same_add_chk = findViewById(R.id.Same_add_chk);
        CheckTax_ap = findViewById(R.id.CheckTax_ap);
        Tax_ll = findViewById(R.id.Tax_ll);
        et_BillAddress = findViewById(R.id.et_BillAddress);
        spBillCountry = findViewById(R.id.spBillCountry);
        spBillState = findViewById(R.id.spBillState);
        spBillCity = findViewById(R.id.spBillCity);
        spNetTerms = findViewById(R.id.spNetTerms);
        spCustomerTaxCode = findViewById(R.id.spCustomerTaxCode);
        et_BillPincode = findViewById(R.id.et_BillPincode);
        et_TaxPercent = findViewById(R.id.et_TaxPercent);
        radioGroup_customertype = (RadioGroup) findViewById(R.id.radioGroup_customertype);
        radioBusiness = (RadioButton) findViewById(R.id.radioBusiness);
        radioIndividual = (RadioButton) findViewById(R.id.radioIndividual);
        LinearLayout Chxtax_ll = (LinearLayout) findViewById(R.id.Chxtax_ll);

        if(settingsvalue.equals("1")) {
            //show
            Chxtax_ll.setVisibility(View.VISIBLE);

        }else {
            //hide
            Chxtax_ll.setVisibility(View.GONE);
            Tax_ll.setVisibility(View.GONE);
            taxStr="2";


        }


        if(CusID.equals("0")|| CusID.equals("3")){
            radioBusiness.setVisibility(View.GONE);
        }else {
            radioBusiness.setVisibility(View.VISIBLE);
        }

        if(CusID.equals("0") || CusID.equals("1")|| CusID.equals("3")) {

            //quick customer
            qc0_tv.setVisibility(View.VISIBLE);
            qc1_tv.setVisibility(View.GONE);
            et_website_tl.setVisibility(View.GONE);
            qc3_tv.setVisibility(View.GONE);
            cs_er_ll.setVisibility(View.GONE);
            qc4_tv.setVisibility(View.GONE);
            cs_er_ll2.setVisibility(View.GONE);
            email_ll3.setVisibility(View.GONE);
            PContact_ll.setVisibility(View.GONE);

        }else if(CusID.equals("2")){

            qc0_tv.setVisibility(View.GONE);
            qc1_tv.setVisibility(View.VISIBLE);
            et_website_tl.setVisibility(View.VISIBLE);
            qc3_tv.setVisibility(View.VISIBLE);
            cs_er_ll.setVisibility(View.VISIBLE);
            qc4_tv.setVisibility(View.VISIBLE);
            cs_er_ll2.setVisibility(View.VISIBLE);
            PContact_ll.setVisibility(View.VISIBLE);
            email_ll3.setVisibility(View.VISIBLE);

            arStatusID.add("0");
            arStatusID.add("1");

            arStatusIDName.add("Active");
            arStatusIDName.add("Inactive");

            spIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Str_IndustryName = arIndustryName.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arStatusIDName);
            adapterStatus.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spStatus.setAdapter(adapterStatus);
                    spStatus.setSelection(arStatusIDName.indexOf("Active"));
                }
            });
            spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Str_StatusName = arStatusIDName.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            GetIndustry();

        }

        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateCustomer.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateCustomer.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CreateCustomer.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_addContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAlert();
            }
        });

        findViewById(R.id.btn_cancelCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CusID.equals("2")){
                    Intent intent = new Intent(Activity_CreateCustomer.this, Activity_CustomerHistory.class);
                    startActivity(intent);
                }else if(CusID.equals("0")){
                    onBackPressed();
                }else if(CusID.equals("1")){
                    onBackPressed();
                }else if(CusID.equals("3")){
                    onBackPressed();
                }
            }
        });
        findViewById(R.id.btn_saveCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_CreateCustomer.this);
                if (Connectivity.isConnected(Activity_CreateCustomer.this) &&
                        Connectivity.isConnectedFast(Activity_CreateCustomer.this)) {
                    getValidation(v);
                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

            }
        });
        radioGroup_customertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioBusiness) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                              BFlag = "1";
                            btn_addContacts.setVisibility(View.VISIBLE);
                            email_ll3.setVisibility(View.GONE);
                            name_ll.setVisibility(View.VISIBLE);
                            customer_title_tv.setText("Business Name *");
                            et_customerName.setHint("Enter the Business Name ");
                            et_primaryEmail_et.setEnabled(false);
                            et_primaryContact_et.setEnabled(false);
                        }
                    });
                }else if (checkedId == R.id.radioIndividual) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BFlag = "2";

                            btn_addContacts.setVisibility(View.GONE);
                            email_ll3.setVisibility(View.VISIBLE);
                            name_ll.setVisibility(View.GONE);
                            customer_title_tv.setText("Customer Name *");
                            et_customerName.setHint("Enter the Customer Name ");
                            et_primaryEmail_et.setEnabled(true);
                            et_primaryContact_et.setEnabled(true);
                        }
                    });

                }
            }

        });
        spDeliveryState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                D_StateName = arStateName.get(position);
                String D_stateID = arStateID.get(position);
                GetCity(D_stateID,"1");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spBillState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B_StateName = arStateName.get(position);
                String B_StateID = arStateID.get(position);
                GetCity(B_StateID,"2");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDeliveryCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                D_CityName = arCityName.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBillCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B_CityName = arCityName.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDeliveryCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                D_CountryName = arCountryName.get(position);
                String D_CountryId = arCountryID.get(position);

                GetState(D_CountryId,"1");

                if(D_CountryId.equals("0")){
                    arStateName.add("Select State");
                    arStateID.add("0");
                    adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, arStateName);
                    adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                    spDeliveryState.setAdapter(adapterBType);
                    spBillState.setAdapter(adapterBType);

                    arCityName.add("Select City");
                    arCityID.add("0");
                    adapterCity = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, arCityName);
                    adapterCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                    spDeliveryCity.setAdapter(adapterCity);
                    spBillCity.setAdapter(adapterCity);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBillCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B_CountryName = arCountryName.get(position);
                String D_CountryId = arCountryID.get(position);

                GetState(D_CountryId,"2");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spNetTerms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_netTermName = arTermsName.get(position);
                Str_netTermID = arTermsID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCustomerTaxCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_TaxCode= arTaxCode.get(position);
                Str_TaxID= arTaxID.get(position);
                String arTaxPercent_str = arTaxPercent.get(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_TaxPercent.setText(String.valueOf(Utils.truncateDecimal(Double.parseDouble(arTaxPercent_str))));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetCountry();
        GetTermsDetails();
        GetTaxCode();

        Same_add_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (Same_add_chk.isChecked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(!D_CountryName.equals("Select Country")){
                                spBillCountry.setSelection(arCountryName.indexOf(D_CountryName));
                            }
                            if(!et_DeliveryPincode.getText().toString().equals("")){
                                et_BillPincode.setText(et_DeliveryPincode.getText().toString());
                            }

                            if(!et_DeliveryAddress.getText().toString().equals("")){
                                et_BillAddress.setText(et_DeliveryAddress.getText().toString());
                            }
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arStateName);
                            adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spBillState.setAdapter(adapterBType);
                                    spBillState.setSelection(arStateName.indexOf("0"));
                                }
                            });

                            ArrayAdapter<String> adapterBType1 = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arCityName);
                            adapterBType1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spBillCity.setAdapter(adapterBType1);
                                    spBillCity.setSelection(arCityName.indexOf("0"));

                                }
                            });

                            ArrayAdapter<String> adapterBType3 = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arCountryName);
                            adapterBType3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spBillCountry.setAdapter(adapterBType3);
                                    spBillCountry.setSelection(arCountryName.indexOf("0"));
                                }
                            });
                            et_BillPincode.setText("");
                            et_BillAddress.setText("");
                        }
                    });

                }
            }
        });

        CheckTax_ap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (CheckTax_ap.isChecked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tax_ll.setVisibility(View.VISIBLE);
                            taxStr="1";

                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tax_ll.setVisibility(View.GONE);
                            taxStr="2";

                        }
                    });

                }
            }
        });
    }
    private void getValidation(final View v) {
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate1 = sdf.format(date1);

        if(CusID.equals("0") || CusID.equals("1")|| CusID.equals("3")) {
            Str_IndustryName ="";
        }

        if (Utils.getInstance().checkIsEmpty(et_customerName)) {
            Utils.getInstance().snackBarMessage(v,"Enter the Customer Name!");
        } else if (Str_netTermName.equals("Select Net Terms")) {
            Utils.getInstance().snackBarMessage(v,"Select Net Terms!");
        } else if (Utils.getInstance().checkIsEmpty(et_primaryEmail_et)) {
            Utils.getInstance().snackBarMessage(v,"Enter Primary Email!");
        }else  if (!Utils.getInstance().isEmailValid(et_primaryEmail_et.getText().toString().trim())) {
            Utils.getInstance().snackBarMessage(v,"Please enter valid Primary email address");
        }else if (Utils.getInstance().checkIsEmpty(et_DeliveryAddress)) {
            Utils.getInstance().snackBarMessage(v,"Enter Delivery Address !");
        }  else if (D_CountryName.equals("Select Country")) {
            Utils.getInstance().snackBarMessage(v,"Select Country!");
        } else if (D_StateName.equals("Select State")) {
            Utils.getInstance().snackBarMessage(v,"Select State!");
        }  else if (D_CityName.equals("Select City")) {
            Utils.getInstance().snackBarMessage(v,"Select City!");
        }  else if (Utils.getInstance().checkIsEmpty(et_DeliveryPincode)) {
            Utils.getInstance().snackBarMessage(v,"Enter Postal Zip Code !");
        } else if (Utils.getInstance().checkIsEmpty(et_BillAddress)) {
            Utils.getInstance().snackBarMessage(v,"Enter Billing Address !");
        } else if (B_CountryName.equals("Select Country")) {
            Utils.getInstance().snackBarMessage(v,"Select Country!");
        }   else if (B_StateName.equals("Select State")) {
            Utils.getInstance().snackBarMessage(v,"Select State!");
        }  else if (B_CityName.equals("Select City")) {
            Utils.getInstance().snackBarMessage(v,"Select City!");
        } else if (Utils.getInstance().checkIsEmpty(et_BillPincode)) {
            Utils.getInstance().snackBarMessage(v,"Enter Postal Zip Code !");
        }  else {
            if(BFlag.equals("1")){
                if( ctList.size()>0){
                    if(et_secondaryEmail_et.getText().toString().equals("")){

                        if (taxStr.equals("1")) {

                            if (Str_TaxCode.equals("Select Tax Code")) {
                                Utils.getInstance().snackBarMessage(v,"Select Tax Code!");
                            } else{
                                postCus(taxStr,v,currentDate1);
                            }

                        }else if (taxStr.equals("2")) {
                            postCus(taxStr,v,currentDate1);
                        }

                    }else {
                        if (!Utils.getInstance().isEmailValid(et_secondaryEmail_et.getText().toString().trim())) {
                            Utils.getInstance().snackBarMessage(v,"Please enter valid Secondary email address");
                        }else {

                            if (taxStr.equals("1")) {

                                if (Str_TaxCode.equals("Select Tax Code")) {
                                    Utils.getInstance().snackBarMessage(v,"Select Tax Code!");
                                } else{
                                    postCus(taxStr,v,currentDate1);
                                }

                            }else if (taxStr.equals("2")) {
                                postCus(taxStr,v,currentDate1);
                            }

                        }
                    }
                }else {
                    Utils.getInstance().snackBarMessage(v,"Please add customer contact!");
                }
            }else {
                if(et_secondaryEmail_et.getText().toString().equals("")){

                    if (taxStr.equals("1")) {

                        if (Str_TaxCode.equals("Select Tax Code")) {
                            Utils.getInstance().snackBarMessage(v,"Select Tax Code!");
                        } else{
                            postCus(taxStr,v,currentDate1);
                        }

                    }else if (taxStr.equals("2")) {
                        postCus(taxStr,v,currentDate1);
                    }

                }else {
                    if (!Utils.getInstance().isEmailValid(et_secondaryEmail_et.getText().toString().trim())) {
                        Utils.getInstance().snackBarMessage(v,"Please enter valid Secondary email address");
                    }else {

                        if (taxStr.equals("1")) {

                            if (Str_TaxCode.equals("Select Tax Code")) {
                                Utils.getInstance().snackBarMessage(v,"Select Tax Code!");
                            } else{
                                postCus(taxStr,v,currentDate1);
                            }

                        }else if (taxStr.equals("2")) {
                            postCus(taxStr,v,currentDate1);
                        }

                    }
                }
            }
        }
    }

    private void postCus(String taxStr,final View v,String currentDate1 ) {

        try {
            JSONObject JSON1 = new JSONObject();
            JSONObject JSON4 = new JSONObject();
            JSONArray array1 =new JSONArray();
            JSONArray array3 =new JSONArray();
            JSON1 = new JSONObject();
            try {
                JSON1.put("id", "00000000-0000-0000-0000-000000000000");
                JSON1.put("line2", et_DeliveryAddress.getText().toString());
                JSON1.put("country", D_CountryName);
                JSON1.put("city", D_CityName);
                JSON1.put("stateorprovince", D_StateName);
                JSON1.put("postalcode", et_DeliveryPincode.getText().toString());
                JSON1.put("isprimaryaddress", true);
                JSON1.put("addresstypecode", "1");
                JSON1.put("mode", "update");
                JSON1.put("isdeleted", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array1.put(JSON1);

            if(BFlag.equals("1")){

                for (int i = 0; i < ctList.size(); i++ ){

                    JSON4 = new JSONObject();
                    try {
                        JSON4.put("id", "00000000-0000-0000-0000-000000000000");
                        JSON4.put("firstname", ctList.get(i).getFirstNm().trim());
                        JSON4.put("lastname", ctList.get(i).getLastNm().trim());
                        JSON4.put("fullname", ctList.get(i).getFirstNm().trim()+ctList.get(i).getLastNm().trim());
                        JSON4.put("jobtitle", ctList.get(i).getJobTittle().trim());
                        JSON4.put("email", ctList.get(i).getPEmail().trim());
                        JSON4.put("phonenumber", ctList.get(i).getPContact().trim());
                        JSON4.put("worknumber", ctList.get(i).getWrkNum().trim());
                        JSON4.put("homenumber", ctList.get(i).getHNum().trim());
                        if(ctList.get(i).getIsPrimary().trim().equals("true")){
                            JSON4.put("primarycontact", "Yes");
                        }else {
                            JSON4.put("primarycontact", "No");
                        }
                        JSON4.put("deliveryaddress", ctList.get(i).getDCAddress()+","+ctList.get(i).getDCCountry().trim()+","+ctList.get(i).getDCState().trim()+","+ctList.get(i).getDCCity().trim()+","+ctList.get(i).getDCPinCode().trim());
                        JSON4.put("billingaddress", ctList.get(i).getBCAddress()+","+ctList.get(i).getBCCountry().trim()+","+ctList.get(i).getBCState().trim()+","+ctList.get(i).getBCCity().trim()+","+ctList.get(i).getBCPinCode().trim());
                        JSON4.put("createdon",currentDate1);
                        JSON4.put("createdby", user_id);
                        JSON4.put("deliveryaddressid", "00000000-0000-0000-0000-000000000000");
                        JSON4.put("billingaddressid", "00000000-0000-0000-0000-000000000000");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array3.put(JSON4);

                }

            }

            JSONObject JSON2 = new JSONObject();
            JSONArray array2 =new JSONArray();
            JSON2 = new JSONObject();
            try {
                JSON2.put("id", "00000000-0000-0000-0000-000000000000");
                JSON2.put("line2", et_BillAddress.getText().toString());
                JSON2.put("country", B_CountryName);
                JSON2.put("city", B_CityName);
                JSON2.put("stateorprovince", B_StateName);
                JSON2.put("postalcode", et_BillPincode.getText().toString());
                JSON2.put("isprimaryaddress", true);
                JSON2.put("addresstypecode", "2");
                JSON2.put("mode", "update");
                JSON2.put("isdeleted", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array2.put(JSON2);

            JSONObject json3 = new JSONObject();
            if(BFlag.equals("1")){
                json3.put("businessfirstname",cet_firstName.getText().toString());
                json3.put("businesslastname",cet_lastName.getText().toString());
            }

            json3.put("id","00000000-0000-0000-0000-000000000000");
            json3.put("emailaddress1",et_primaryEmail_et.getText().toString());
            json3.put("telephone1",et_primaryContact_et.getText().toString());
            json3.put("emailaddress2",et_secondaryEmail_et.getText().toString());
            json3.put("telephone2",et_SecondaryContact_et.getText().toString());
            json3.put("useforcreatingpresales",true);

            JSONObject json = new JSONObject();
            json.put("id","00000000-0000-0000-0000-000000000000");
            json.put("customertypeid",BFlag);
            json.put("customername",et_customerName.getText().toString());
            json.put("website",et_website.getText().toString());
            json.put("industry",Str_IndustryName);
            json.put("createdby",user_id);
            json.put("createdon",currentDate1);
            json.put("status","Active");
            json.put("isactive",true);
            if(taxStr.equals("1")) {
                json.put("netterms", Str_netTermID);
                json.put("taxid", Str_TaxID);
            }else if(taxStr.equals("2")){
                json.put("netterms","00000000-0000-0000-0000-000000000000");
                json.put("taxid","00000000-0000-0000-0000-000000000000");
            }

            if(taxStr.equals("1")){
                json.put("istaxable",true);
            }else {
                json.put("istaxable",false);
            }

            json.put("deliveryAddress",array1);
            json.put("billingAddress",array2);
            json.put("contactList",json3);
            json.put("multiplecontacts",array3);


            System.out.println("currentDate1---"+currentDate1);
            System.out.println("Customer post---"+json);

            postCustomer(v,json,currentDate1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postCustomer(final View v, final JSONObject json,String currentDate1 ) {
        Progress_dialog.show();
        try {
            App.getInstance().PostCustomerCreate(json.toString(),token,new Callback() {
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
                        //Utils.getInstance().GetMasterInsert(Activity_CreateCustomer.this,"CustomerCreate",Progress_dialog,currentDate1);
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    String data = jsonObject.getString("data");

                                    if (succeeded == true) {
                                        GetMasterCustomer(currentDate1,data);

                                    }
                                    else {
                                        Toast.makeText(Activity_CreateCustomer.this, "Please try again later!!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.getInstance().dismissDialog();
                                try {
                                    String res = response.body().string();
                                    JSONObject jsonObject = new JSONObject(res);
                                    String exception = jsonObject.getString("exception");
                                    String errorCode = jsonObject.getString("errorCode");
                                    Toast.makeText(Activity_CreateCustomer.this, exception, Toast.LENGTH_SHORT).show();

                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }
                                } catch (IOException | JSONException e) {
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

    private void showSuccess(String CusID,String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateCustomer.this);
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
        tittleAlert_tv.setText("Customer Created Successful");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CusID.equals("2")){
                    Intent intent = new Intent(Activity_CreateCustomer.this, Activity_CustomerHistory.class);
                    startActivity(intent);
                }else if(CusID.equals("0")){
                    Bundle bundle = new Bundle();
                    bundle.putString("CusID",data);
                    Intent in = new Intent(Activity_CreateCustomer.this, Activity_CreateOrder.class);
                    in.putExtras(bundle);
                    startActivity(in);
                }else if(CusID.equals("3")){
                    Bundle bundle = new Bundle();
                    bundle.putString("CusID",data);
                    Intent in = new Intent(Activity_CreateCustomer.this, Activity_CreateSalesReturn.class);
                    in.putExtras(bundle);
                    startActivity(in);
                }else if(CusID.equals("1")){
                    Bundle bundle = new Bundle();
                    bundle.putString("CusID",data);
                    bundle.putString("OrderId",OrderId);
                    bundle.putString("CustomerName",CustomerName);
                    Intent in = new Intent(Activity_CreateCustomer.this, Activity_UpdateOrder.class);
                    in.putExtras(bundle);
                    startActivity(in);
                }
                dialog.dismiss();
            }
        });
    }
    private void GetState(String D_CountryId,String flag) {
        try {
            App.getInstance().GetState(D_CountryId,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("options");
                            if (arStateName.size() > 0) arStateName.clear();
                            if (arStateID.size() > 0) arStateID.clear();
                            if (data.length() > 0) {
                                arStateName.add("Select State");
                                arStateID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arStateName.add(js.getString("displayname"));
                                    arStateID.add(js.getString("id"));

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, arStateName);
                                        adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        if(flag.equals("1")){
                                            spDeliveryState.setAdapter(adapterBType);

                                            if(QickCustomer.equals("1")){
                                                if(CD_StateName!= null){
                                                    if(!CD_StateName.equals("Select State")){
                                                        int pos =adapterBType.getPosition(CD_StateName);
                                                        spDeliveryState.setSelection(pos);
                                                    }
                                                }
                                            }

                                        }else {
                                            spBillState.setAdapter(adapterBType);

                                            if(QickCustomer.equals("1")){
                                                if(CB_StateName!= null){
                                                    if(!CB_StateName.equals("Select State")){
                                                        System.out.println("CB_StateName---"+CB_StateName);
                                                        int pos =adapterBType.getPosition(CB_StateName);
                                                        spBillState.setSelection(pos);
                                                    }
                                                }
                                            }

                                            if(D_StateName!= null){
                                                if(!D_StateName.equals("Select State")){
                                                    int pos =adapterBType.getPosition(D_StateName);
                                                    spBillState.setSelection(pos);
                                                }
                                            }
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
    private void GetCity(String D_stateID,String flag) {
        try {
            App.getInstance().GetCity(D_stateID,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("options");
                            if (arCityName.size() > 0) arCityName.clear();
                            if (arCityID.size() > 0) arCityID.clear();
                            if (data.length() > 0) {
                                arCityName.add("Select City");
                                arCityID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arCityName.add(js.getString("displayname"));
                                    arCityID.add(js.getString("id"));
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapterCity = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, arCityName);
                                        adapterCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        if(flag.equals("1")){
                                            spDeliveryCity.setAdapter(adapterCity);

                                            if(QickCustomer.equals("1")){
                                                if(CD_CityName!= null){
                                                    if(!CD_CityName.equals("Select City")){
                                                        int pos =adapterCity.getPosition(CD_CityName);
                                                        spDeliveryCity.setSelection(pos);
                                                    }
                                                }

                                            }

                                        }else {
                                            spBillCity.setAdapter(adapterCity);

                                            if(QickCustomer.equals("1")){
                                                if(CB_CityName!= null){
                                                    if(!CB_CityName.equals("Select City")){
                                                        int pos =adapterCity.getPosition(CB_CityName);
                                                        spBillCity.setSelection(pos);

                                                    }
                                                }
                                            }

                                            if(D_CityName!= null){
                                                if(!D_CityName.equals("Select City")){
                                                    int pos =adapterCity.getPosition(D_CityName);
                                                    spBillCity.setSelection(pos);

                                                }
                                            }
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
    private void GetCountry() {
        try {
            App.getInstance().GetCountry(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("options");
                            if (arCountryName.size() > 0) arCountryName.clear();
                            if (arCountryID.size() > 0) arCountryID.clear();
                            if (data.length() > 0) {
                                arCountryName.add("Select Country");
                                arCountryID.add("0");


                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    if(js.getString("displayname").equals("USA")){
                                        arCountryName.add(js.getString("displayname"));
                                        arCountryID.add(js.getString("id"));
                                    }
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arCountryName);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spDeliveryCountry.setAdapter(adapterBType);

                                    }
                                });

                                ArrayAdapter<String> adapterBType5 = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arCountryName);
                                adapterBType5.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spBillCountry.setAdapter(adapterBType5);

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
    private void GetTermsDetails() {
        try {
            App.getInstance().GetTermsDetails(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (arTermsName.size() > 0) arTermsName.clear();
                            if (arTermsID.size() > 0) arTermsID.clear();
                            if (data.length() > 0) {
                                arTermsName.add("Select Net Terms");
                                arTermsID.add("0");


                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arTermsName.add(js.getString("termname"));
                                    arTermsID.add(js.getString("id"));
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arTermsName);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spNetTerms.setAdapter(adapterBType);

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

    private void GetTaxCode() {
        try {
            App.getInstance().GetTaxCode(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (arTaxCode.size() > 0) arTaxCode.clear();
                            if (arTaxID.size() > 0) arTaxID.clear();
                            if (arTaxPercent.size() > 0) arTaxPercent.clear();
                            if (arTaxName.size() > 0) arTaxName.clear();
                            if (data.length() > 0) {
                                arTaxCode.add("Select Tax Code");
                                arTaxID.add("0");
                                arTaxPercent.add("0");
                                arTaxName.add("0");


                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arTaxCode.add(js.getString("code"));
                                    arTaxID.add(js.getString("id"));
                                    arTaxPercent.add(js.getString("taxpercentage"));
                                    arTaxName.add(js.getString("name"));
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arTaxCode);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spCustomerTaxCode.setAdapter(adapterBType);

                                    }
                                });
                            }else {
                                arTaxCode.add("Select Tax Code");
                                arTaxCode.add("Not Assigned");

                                arTaxID.add("00000000-0000-0000-0000-000000000000");
                                arTaxID.add("00000000-0000-0000-0000-000000000000");

                                arTaxPercent.add("0");
                                arTaxPercent.add("0");

                                arTaxName.add("0");
                                arTaxName.add("0");

                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arTaxCode);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spCustomerTaxCode.setAdapter(adapterBType);

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
    private void GetIndustry() {
        Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetIndustry(token,new Callback(){

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
                        Utils.getInstance().dismissDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("options");
                            if (arIndustryName.size() > 0) arIndustryName.clear();
                            if (arIndustryID.size() > 0) arIndustryID.clear();
                            if (data.length() > 0) {

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arIndustryName.add(js.getString("displayname"));
                                    arIndustryID.add(js.getString("id"));
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,arIndustryName);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spIndustry.setAdapter(adapterBType);
                                        spIndustry.setSelection(arIndustryName.indexOf("Not Assigned"));

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

    public void AddNewAlert() {
        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {
                Primary__ ="false";
                CheckBox pr_check;
                AppCompatTextView txt_no_record;
                RecyclerView ry_contactlist;
                AppCompatEditText et_HomeNum,et_firstName,et_lastName,et_jobTittle,et_pEmail_et,et_pContact_et,et_workNum,et_homeNum;
                AppCompatEditText Cet_DeliveryAddress,Cet_DeliveryPincode,Cet_BillAddress,Cet_BillPincode;

                CheckBox CSame_add_chk;

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateCustomer.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateOrder.this);
                LayoutInflater inflater = Activity_CreateCustomer.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_addnewcus,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();


                pr_check = (CheckBox) v.findViewById(R.id.pr_check);
                pr_check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                        if (cb.isChecked()) {

                            Primary__ ="true";

                        } else if (!cb.isChecked()){

                            Primary__ ="false";

                        }

                    }
                });
                txt_no_record = (AppCompatTextView) v.findViewById(R.id.txt_no_record2);
                et_firstName = (AppCompatEditText) v.findViewById(R.id.et_firstName);
                et_lastName = (AppCompatEditText) v.findViewById(R.id.et_lastName);
                et_jobTittle = (AppCompatEditText) v.findViewById(R.id.et_jobTittle);
                et_pEmail_et = (AppCompatEditText) v.findViewById(R.id.et_pEmail_et);
                et_pContact_et = (AppCompatEditText) v.findViewById(R.id.et_pContact_et);
                et_workNum = (AppCompatEditText) v.findViewById(R.id.et_workNum);
                et_HomeNum = (AppCompatEditText) v.findViewById(R.id.et_HomeNum);
                Cet_DeliveryAddress =  v.findViewById(R.id.et_DeliveryAddress);
                CspDeliveryCountry =  v.findViewById(R.id.CspDeliveryCountry);
                CspDeliveryState =  v.findViewById(R.id.CspDeliveryState);
                CspDeliveryCity =  v.findViewById(R.id.CspDeliveryCity);
                Cet_DeliveryPincode =  v.findViewById(R.id.et_DeliveryPincode);
                CSame_add_chk =  v.findViewById(R.id.Same_add_chk);
                Cet_BillAddress =  v.findViewById(R.id.et_BillAddress);
                CspBillCountry =  v.findViewById(R.id.spBillCountry);
                CspBillState =  v.findViewById(R.id.spBillState);
                CspBillCity =  v.findViewById(R.id.spBillCity);
                Cet_BillPincode =  v.findViewById(R.id.et_BillPincode);
                ry_contactlist = (RecyclerView) v.findViewById(R.id.ry_contactlist);

                CSame_add_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        if (CSame_add_chk.isChecked()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(!CD_CountryName.equals("Select Country")){
                                        CspBillCountry.setSelection(CarCountryName.indexOf(CD_CountryName));
                                    }
                                    if(!Cet_DeliveryPincode.getText().toString().equals("")){
                                        Cet_BillPincode.setText(Cet_DeliveryPincode.getText().toString());
                                    }

                                    if(!Cet_DeliveryAddress.getText().toString().equals("")){
                                        Cet_BillAddress.setText(Cet_DeliveryAddress.getText().toString());
                                    }


                                }
                            });


                        }else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,CarStateName);
                                    adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CspBillState.setAdapter(adapterBType);
                                            CspBillState.setSelection(CarStateName.indexOf("0"));
                                        }
                                    });

                                    ArrayAdapter<String> adapterBType1 = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,CarCityName);
                                    adapterBType1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CspBillCity.setAdapter(adapterBType1);
                                            CspBillCity.setSelection(CarCityName.indexOf("0"));

                                        }
                                    });

                                    ArrayAdapter<String> adapterBType3 = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,CarCountryName);
                                    adapterBType3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CspBillCountry.setAdapter(adapterBType3);
                                            CspBillCountry.setSelection(CarCountryName.indexOf("0"));
                                        }
                                    });

                                    Cet_BillPincode.setText("");
                                    Cet_BillAddress.setText("");
                                }
                            });

                        }
                    }
                });

                try {
                    App.getInstance().GetCountry(token,new Callback(){

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
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray data = jsonObject.getJSONArray("options");
                                    if (CarCountryName.size() > 0) CarCountryName.clear();
                                    if (CarCountryID.size() > 0) CarCountryID.clear();
                                    if (data.length() > 0) {
                                        CarCountryName.add("Select Country");
                                        CarCountryID.add("0");


                                        for (int i=0; i<data.length(); i++) {
                                            JSONObject js = data.getJSONObject(i);
                                            if(js.getString("displayname").equals("USA")){
                                                CarCountryName.add(js.getString("displayname"));
                                                CarCountryID.add(js.getString("id"));
                                            }
                                        }
                                        ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item,CarCountryName);
                                        adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CspDeliveryCountry.setAdapter(adapterBType);
                                                CspBillCountry.setAdapter(adapterBType);

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

                cAdapter = new Adapter_ContactList(ctList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_CreateCustomer.this);
                ry_contactlist.setLayoutManager(mLayoutManager);
                ry_contactlist.setItemAnimator(new DefaultItemAnimator());
                ry_contactlist.addItemDecoration(new ItemDividerDecoration(Activity_CreateCustomer.this, LinearLayoutManager.VERTICAL));
                // set the adapter object to the Recyclerview
                ry_contactlist.setAdapter(cAdapter);
                ry_contactlist.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_CreateCustomer.this, ry_contactlist, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        view.findViewById(R.id.dots_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(Activity_CreateCustomer.this, view, Gravity.RIGHT);
                                popupMenu.getMenuInflater().inflate(R.menu.popup_customermenu, popupMenu.getMenu());
                                Menu popMenu = popupMenu.getMenu();

                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                        switch (menuItem.getItemId()) {
                                            case R.id.primary_btn:

                                                if( ctList.size()>0){
                                                    for (int i = 0; i < ctList.size(); i++) {
                                                        ctList.get(i).setIsPrimary("false");
                                                        ctList.get(position).setIsPrimary("true");
                                                        cAdapter.notifyDataSetChanged();
                                                    }
                                                }else {
                                                    ctList.get(position).setIsPrimary("true");
                                                    cAdapter.notifyDataSetChanged();
                                                }

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
                        view.findViewById(R.id.ct_delete_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ctList.remove(position);
                                cAdapter.notifyDataSetChanged();
                                Toast.makeText(Activity_CreateCustomer.this, "Contact has been deleted successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));


                v.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_ct_clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_ct_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(ctList.size()>0){
                            for (int i = 0; i < ctList.size(); i++ ){
                                Model_ContactList primary_is = ctList.get(i);

                                    QickCustomer ="1";

                                    spDeliveryCountry.setSelection(arCountryName.indexOf(ctList.get(i).getDCCountry()));

                                    CD_StateName=ctList.get(i).getDCState();
                                    CD_CityName=ctList.get(i).getDCCity();

                                    et_DeliveryAddress.setText(ctList.get(i).getDCAddress());

                                    et_DeliveryPincode.setText(ctList.get(i).getDCPinCode());

                                    spBillCountry.setSelection(arCountryName.indexOf(ctList.get(i).getBCCountry()));

                                    CB_StateName=ctList.get(i).getBCState();
                                    CB_CityName=ctList.get(i).getBCCity();

                                    et_BillAddress.setText(ctList.get(i).getBCAddress());
                                    et_BillPincode.setText(ctList.get(i).getBCPinCode());

                                    if(primary_is.getIsPrimary().equals("true")){
                                        et_primaryEmail_et .setText(ctList.get(i).getPEmail());
                                        et_primaryEmail_et.setEnabled(false);
                                        et_primaryContact_et .setText(ctList.get(i).getPContact());
                                        et_primaryContact_et.setEnabled(false);

                                        cet_firstName.setText(ctList.get(i).getFirstNm());
                                        cet_lastName.setText(ctList.get(i).getLastNm());

                                    }

                                    dialog.dismiss();

                            }
                        }else {
                            Utils.getInstance().snackBarMessage(v,"Add Contact information!");
                        }




                    }
                });
                v.findViewById(R.id.btn_ContactSave_).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        boolean isExsits =false;
                        Utils.getInstance().hideKeyboard(Activity_CreateCustomer.this);
                        if (Connectivity.isConnected(Activity_CreateCustomer.this) &&
                                Connectivity.isConnectedFast(Activity_CreateCustomer.this)) {

                            if (Utils.getInstance().checkIsEmpty(et_firstName)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the First Name!");
                            }else if (Utils.getInstance().checkIsEmpty(et_lastName)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Last Name!");
                            } else if (Utils.getInstance().checkIsEmpty(et_jobTittle)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Job Tittle!");
                            } else if (Utils.getInstance().checkIsEmpty(et_pEmail_et)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Email!");
                            }else if (!Utils.getInstance().isEmailValid(et_pEmail_et.getText().toString().trim())) {
                                Utils.getInstance().snackBarMessage(v,"Please enter valid Primary email address");
                            }  else if (Utils.getInstance().checkIsEmpty(et_pContact_et)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Contact Number!");
                            }else if (Utils.getInstance().checkIsEmpty(et_workNum)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Work Number!");
                            }else if (Utils.getInstance().checkIsEmpty(et_HomeNum)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the Home Number!");
                            } else if (Utils.getInstance().checkIsEmpty(Cet_DeliveryAddress)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the DeliveryAddress!");
                            }else if (Utils.getInstance().checkIsEmpty(Cet_BillAddress)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the BillAddress!");
                            }else if (Utils.getInstance().checkIsEmpty(Cet_DeliveryPincode)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the PinCode!");
                            }else if (Utils.getInstance().checkIsEmpty(Cet_BillPincode)) {
                                Utils.getInstance().snackBarMessage(v,"Enter the PinCode!");
                            }else if (CD_CountryName.equals("Select Country")) {
                                Utils.getInstance().snackBarMessage(v,"Select Country!");
                            }else if (CD_StateName.equals("Select State")) {
                                Utils.getInstance().snackBarMessage(v,"Select State!");
                            }else if (CD_CityName.equals("Select City")) {
                                Utils.getInstance().snackBarMessage(v,"Select City!");
                            }else if (CB_CountryName.equals("Select Country")) {
                                Utils.getInstance().snackBarMessage(v,"Select Country!");
                            }else if (CB_StateName.equals("Select State")) {
                                Utils.getInstance().snackBarMessage(v,"Select State!");
                            }else if (CB_CityName.equals("Select City")) {
                                Utils.getInstance().snackBarMessage(v,"Select City!");
                            }else {
                                if(ctList.size()>0){

                                    for (int k = 0; k < ctList.size(); k++) {

                                        if(ctList.get(k).getIsPrimary().equals(Primary__)){
                                            isExsits= false;

                                        }else {
                                            isExsits =true;
                                        }
                                    }
                                }else {
                                    isExsits =true;
                                }
                                if(isExsits){
                                    ctList.add(new Model_ContactList("",Primary__,et_firstName.getText().toString(),et_lastName.getText().toString(),et_jobTittle.getText().toString(),et_pEmail_et.getText().toString(),et_pContact_et.getText().toString(),et_workNum.getText().toString(),et_HomeNum.getText().toString(),
                                            Cet_DeliveryAddress.getText().toString(),CD_CountryName,CD_StateName,CD_CityName,   Cet_DeliveryPincode.getText().toString(),   Cet_BillAddress.getText().toString(),CB_CountryName,CB_StateName,CB_CityName,   Cet_BillPincode.getText().toString()));
                                    cAdapter.notifyDataSetChanged();
                                }else {
                                    Utils.getInstance().snackBarMessage(v,"Primary customer already added!");
                                }

                            }
                           // dialog.dismiss();
                        }else {
                            Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                        }
                    }
                });

                CspDeliveryState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CD_StateName = CarStateName.get(position);
                        String CD_stateID = CarStateID.get(position);

                        GetCityC(CD_stateID,"1");

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                CspBillState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CB_StateName = CarStateName.get(position);
                        String CB_StateID = CarStateID.get(position);

                        GetCityC(CB_StateID,"2");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                CspDeliveryCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CD_CityName = CarCityName.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                CspBillCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CB_CityName = CarCityName.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                CspDeliveryCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CD_CountryName = CarCountryName.get(position);
                        String CD_CountryId = CarCountryID.get(position);

                        GetStateC(CD_CountryId,"1");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                CspBillCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CB_CountryName = CarCountryName.get(position);
                        String CD_CountryId = CarCountryID.get(position);

                        GetStateC(CD_CountryId,"2");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
            private void GetCityC(String D_stateID,String flag) {
                try {
                    App.getInstance().GetCity(D_stateID,token,new Callback(){

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String res = response.body().string();

                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray data = jsonObject.getJSONArray("options");
                                    if (CarCityName.size() > 0) CarCityName.clear();
                                    if (CarCityID.size() > 0) CarCityID.clear();
                                    if (data.length() > 0) {
                                        CarCityName.add("Select City");
                                        CarCityID.add("0");

                                        for (int i=0; i<data.length(); i++) {
                                            JSONObject js = data.getJSONObject(i);
                                            CarCityName.add(js.getString("displayname"));
                                            CarCityID.add(js.getString("id"));
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CadapterCity = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, CarCityName);
                                                CadapterCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                                if(flag.equals("1")){
                                                    CspDeliveryCity.setAdapter(CadapterCity);
                                                }else {
                                                    CspBillCity.setAdapter(CadapterCity);
                                                    if(CD_CityName!= null){
                                                        if(!CD_CityName.equals("Select City")){
                                                            int pos =CadapterCity.getPosition(CD_CityName);
                                                            CspBillCity.setSelection(pos);

                                                        }
                                                    }
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

            private void GetStateC(String D_CountryId,String flag) {
                try {
                    App.getInstance().GetState(D_CountryId,token,new Callback(){

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String res = response.body().string();

                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray data = jsonObject.getJSONArray("options");
                                    if (CarStateName.size() > 0) CarStateName.clear();
                                    if (CarStateID.size() > 0) CarStateID.clear();
                                    if (data.length() > 0) {
                                        CarStateName.add("Select State");
                                        CarStateID.add("0");

                                        for (int i=0; i<data.length(); i++) {
                                            JSONObject js = data.getJSONObject(i);
                                            CarStateName.add(js.getString("displayname"));
                                            CarStateID.add(js.getString("id"));

                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                CadapterBType = new ArrayAdapter<String>(Activity_CreateCustomer.this, R.layout.spinner_item, CarStateName);
                                                CadapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                                if(flag.equals("1")){
                                                    CspDeliveryState.setAdapter(CadapterBType);
                                                }else {
                                                    CspBillState.setAdapter(CadapterBType);
                                                    if(CD_StateName!= null){
                                                        if(!CD_StateName.equals("Select State")){
                                                            int pos =CadapterBType.getPosition(CD_StateName);
                                                            CspBillState.setSelection(pos);
                                                        }
                                                    }
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
        });
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }



    public void GetMasterCustomer(String  ModifiedDateTime,String  data) {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Started------");
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
                    System.out.println("URL------"+ Const.GetCreateOrderLogin+ModifiedDateTime+"&orderproductlastsyncon="+ModifiedDateTime+"&shipmentlastsyncon="+ModifiedDateTime+"&addresslastsyncon="+ModifiedDateTime+"&invoicelastsyncon="+ModifiedDateTime+"&invoiceproductlastsyncon="+ModifiedDateTime+"&customerlastsyncon="+ModifiedDateTime+"&productlastsyncon="+ModifiedDateTime+"&productassetlastsyncon="+ModifiedDateTime);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.GetCreateOrderLogin+ModifiedDateTime+"&orderproductlastsyncon="+ModifiedDateTime+"&shipmentlastsyncon="+ModifiedDateTime+"&addresslastsyncon="+ModifiedDateTime+"&invoicelastsyncon="+ModifiedDateTime+"&invoiceproductlastsyncon="+ModifiedDateTime+"&customerlastsyncon="+ModifiedDateTime+"&productlastsyncon="+ModifiedDateTime+"&productassetlastsyncon="+ModifiedDateTime)
                            // .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();
                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println("OrdersLogin---"+jsonObject);
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");

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
                                        SQLiteController sqLiteController = new SQLiteController(Activity_CreateCustomer.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorCustomerTable = sqLiteController.readOrderItem(DbHandler.TABLE_CUSTOMER,DbHandler.CUSTOMER_ID,id);
                                            if (CursorCustomerTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Id = CursorCustomerTable.getString(CursorCustomerTable.getColumnIndex("Id"));
                                                    sqLiteController.deleteCustomerItems(Id);
                                                    sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                } while (CursorCustomerTable.moveToNext());
                                            }
                                            if(CursorCustomerTable.getCount() == 0){
                                                sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                        billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
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
                                        SQLiteController sqLiteController = new SQLiteController(Activity_CreateCustomer.this);
                                        sqLiteController.open();
                                        try {
                                            Cursor AddressTable = sqLiteController.readOrderItem(DbHandler.TABLE_ADDRESS,DbHandler.ADDRESS_ID,id);
                                            if (AddressTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String address_id = AddressTable.getString(AddressTable.getColumnIndex("address_id"));
                                                    sqLiteController.deleteAddressItems(address_id);
                                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);
                                                } while (AddressTable.moveToNext());
                                            }
                                            if(AddressTable.getCount() == 0){
                                                sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);
                                            }
                                        } finally {
                                            sqLiteController.close();
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
                        System.out.println("Completed------");
                        if (Progress_dialog != null) {
                            if (Progress_dialog.isShowing()) {
                                Progress_dialog.dismiss();
                            }
                        }
                        showSuccess(CusID,data);
                    }
                });

            }

        };
        task.execute((Void[])null);
    }
}

