package com.example.arcomdriver.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.arcomdriver.driver.ActivitySignIn;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_ContactList;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Jan 2023*/
public class Activity_UpdateCustomer extends Activity_Menu {
    private CoordinatorLayout cl;
    RadioGroup radioGroup_customertype;
    RadioButton radioBusiness,radioIndividual;
    AppCompatEditText et_TaxPercent,et_BillPincode,et_DeliveryPincode,et_BillAddress,et_DeliveryAddress,et_SecondaryContact_et,et_secondaryEmail_et,et_customerName,et_website,et_primaryEmail_et,et_primaryContact_et;
    Spinner spStatus,spIndustry,spDeliveryCountry,spDeliveryState,spDeliveryCity
            ,spBillCountry,spBillState,spBillCity,spNetTerms,spCustomerTaxCode;
    CheckBox Same_add_chk,CheckTax_ap;

    LinearLayout Tax_ll;
    private String Str_netTermName="Select Net Terms",Str_netTermID,Str_TaxCode,Str_TaxID,Delivery_id_,Contact_id,Bill_id_,Bill_state="null",Bill_city="null",Delivery_state="null",Delivery_city="null",netterms="null",taxid="null",Str_StatusName,CustomerId,BFlag="2",Str_IndustryName="Select Industry",user_id,token,D_StateName="Select State",D_CityName,D_CountryName="Select Country",B_StateName="Select State",B_CityName="Select City",B_CountryName="Select Country";

    private ArrayList<String> arStateID = new ArrayList<>();
    private ArrayList<String> arStateName = new ArrayList<>();

    private ArrayList<String> arBStateID = new ArrayList<>();
    private ArrayList<String> arBStateName = new ArrayList<>();

    private ArrayList<String> arCityID = new ArrayList<>();
    private ArrayList<String> arCityName = new ArrayList<>();

    private ArrayList<String> arBCityID = new ArrayList<>();
    private ArrayList<String> arBCityName = new ArrayList<>();

    private ArrayList<String> arCountryID = new ArrayList<>();
    private ArrayList<String> arCountryName = new ArrayList<>();

    private ArrayList<String> arBCountryID = new ArrayList<>();
    private ArrayList<String> arBCountryName = new ArrayList<>();

    private ArrayList<String> arIndustryID = new ArrayList<>();
    private ArrayList<String> arIndustryName = new ArrayList<>();

    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusIDName = new ArrayList<>();
    String taxStr="0";
    ArrayAdapter<String> adapterDState;
    ArrayAdapter<String> adapterBState;

    ArrayAdapter<String> adapterDCity;
    ArrayAdapter<String> adapterBCity;
    ArrayAdapter<String> adapterDCountry;
    ArrayAdapter<String> adapterBCountry;
    private ArrayList<String> arTermsID = new ArrayList<>();
    private ArrayList<String> arTermsName = new ArrayList<>();
    private ArrayList<String> arTaxCode = new ArrayList<>();
    private ArrayList<String> arTaxID = new ArrayList<>();
    private ArrayList<String> arTaxPercent = new ArrayList<>();
    private ArrayList<String> arTaxName = new ArrayList<>();
    AppCompatButton btn_addContacts;
    String  CB_CountryName,CD_CountryName,CB_CityName,CD_CityName,CB_StateName,CD_StateName,QickCustomer ="0";
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
    LinearLayout name_ll;
    String  Primary__ ="false";
    String dl_address="";
    String dl_country="";
    String dl_state="";
    String dl_city="";
    String dl_postal="";
    String bl_address="";
    String bl_country="";
    String bl_state="";
    String bl_city="";
    String bl_postal="";
    String  settingsvalue="0";

    public AlertDialog Progress_dialog;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_updatecustomer, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Edit Customer");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateCustomer.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_UpdateCustomer.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CustomerId = extras.getString("CustomerId");

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

        name_ll = findViewById(R.id.name_ll);
        cet_firstName = findViewById(R.id.cet_firstName);
        cet_lastName = findViewById(R.id.cet_lastName);
        customer_title_tv = findViewById(R.id.customer_title_tv);

        et_customerName = findViewById(R.id.et_customerName);
        et_website = findViewById(R.id.et_website);
        spIndustry = findViewById(R.id.spIndustry);
        spStatus = findViewById(R.id.spStatus);
        btn_addContacts = findViewById(R.id.btn_UpdateContacts);
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
        et_BillAddress = findViewById(R.id.et_BillAddress);
        spBillCountry = findViewById(R.id.spBillCountry);
        spBillState = findViewById(R.id.spBillState);
        spBillCity = findViewById(R.id.spBillCity);
        et_BillPincode = findViewById(R.id.et_BillPincode);
        radioGroup_customertype = (RadioGroup) findViewById(R.id.radioGroup_customertype);
        radioBusiness = (RadioButton) findViewById(R.id.radioBusiness);
        radioIndividual = (RadioButton) findViewById(R.id.radioIndividual);

        findViewById(R.id.btn_UpdateContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAlert();
            }
        });

        CheckTax_ap = findViewById(R.id.CheckTax_ap);
        Tax_ll = findViewById(R.id.Tax_ll);
        spNetTerms = findViewById(R.id.spNetTerms);
        spCustomerTaxCode = findViewById(R.id.spCustomerTaxCode);
        et_TaxPercent = findViewById(R.id.et_TaxPercent);

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

        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UpdateCustomer.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UpdateCustomer.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UpdateCustomer.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        GetCountryD();
        GetCountryB();

        arStatusID.add("0");
        arStatusID.add("1");

        arStatusIDName.add("Active");
        arStatusIDName.add("Inactive");

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

        findViewById(R.id.btn_cancelCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        findViewById(R.id.btn_saveCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_UpdateCustomer.this);
                if (Connectivity.isConnected(Activity_UpdateCustomer.this) &&
                        Connectivity.isConnectedFast(Activity_UpdateCustomer.this)) {
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

        GetIndustry();
        GetTermsDetails();
        GetTaxCode();

        spIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_IndustryName = arIndustryName.get(position);
                System.out.println("Str_IndustryName--"+Str_IndustryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        spDeliveryState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                D_StateName = arStateName.get(position);
                String D_stateID = arStateID.get(position);

                GetCity(D_stateID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBillState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B_StateName = arBStateName.get(position);
                String B_StateID = arBStateID.get(position);

                GetBillCity(B_StateID);
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
                B_CityName = arBCityName.get(position);
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

                GetState(D_CountryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBillCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                B_CountryName = arBCountryName.get(position);
                String D_CountryId = arBCountryID.get(position);

                GetBillState(D_CountryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Same_add_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (Same_add_chk.isChecked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Bill_state="null";
                            Bill_city="null";
                            Delivery_state="null";
                            Delivery_city="null";

                            if(!D_CountryName.equals("Select Country")){
                                spBillCountry.setSelection(arBCountryName.indexOf(D_CountryName));
                            }

                            adapterBCity = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arBCityName);
                            adapterBCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            if(D_CityName!= null){
                                if(!D_CityName.equals("Select City")){
                                    int pos =adapterBCity.getPosition(D_CityName);
                                    spBillCity.setSelection(pos);

                                }
                            }

                            adapterBState = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arBStateName);
                            adapterBState.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            if(D_StateName!= null){
                                if(!D_StateName.equals("Select State")){
                                    int pos =adapterBState.getPosition(D_StateName);
                                    spBillState.setSelection(pos);
                                }
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
                            adapterBState = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arBStateName);
                            adapterBState.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spBillState.setAdapter(adapterBState);
                                    spBillState.setSelection(arBStateName.indexOf("0"));
                                }
                            });

                            adapterBCity = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arBCityName);
                            adapterBCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spBillCity.setAdapter(adapterBCity);
                                    spBillCity.setSelection(arBCityName.indexOf("0"));

                                }
                            });

                            et_BillPincode.setText("");
                            et_BillAddress.setText("");
                        }
                    });

                }
            }
        });
        spNetTerms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Str_netTermName = arTermsName.get(position);
                Str_netTermID = arTermsID.get(position);
                System.out.println("Str_netTermName--"+Str_netTermName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });spCustomerTaxCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        GetCustomerSummary(CustomerId);

    }

    private void GetCustomerSummary(String Id_) {
        Utils.getInstance().loadingDialog(Activity_UpdateCustomer.this, "Please wait.");
        try {
            App.getInstance().GetCustomerSummary(Id_,token,new Callback(){

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
                            JSONObject jsonObject = new JSONObject(res);

                            JSONObject jsdata = jsonObject.getJSONObject("data");
                            String customernumber = jsdata.getString("customernumber");
                            String id = jsdata.getString("id");
                            String customername = jsdata.getString("customername");
                            String website = jsdata.getString("website");
                            String industry = jsdata.getString("industry");
                            String status = jsdata.getString("status");
                            String istaxable = jsdata.getString("istaxable");
                            String customertypeid = jsdata.getString("customertypeid");
                            netterms = jsdata.getString("netterms");
                            taxid = jsdata.getString("taxid");

                            JSONArray jsContact1 = jsdata.getJSONArray("contactInfo");
                            for (int i=0; i<jsContact1.length(); i++) {
                                JSONObject jsContact = jsContact1.getJSONObject(i);
                                Contact_id = jsContact.getString("id");
                                String emailaddress1 = jsContact.getString("emailaddress1");
                                String telephone1 = jsContact.getString("telephone1");
                                String emailaddress2 = jsContact.getString("emailaddress2");
                                String telephone2 = jsContact.getString("telephone2");
                                String firstname = jsContact.getString("firstname");
                                String lastname = jsContact.getString("lastname");
                                String jobtitle = jsContact.getString("jobtitle");
                                String email = jsContact.getString("email");
                                String phonenumber = jsContact.getString("phonenumber");
                                String worknumber = jsContact.getString("worknumber");
                                String homenumber = jsContact.getString("homenumber");
                                String billingaddress = jsContact.getString("billingaddress");
                                String deliveryaddress = jsContact.getString("billingaddress");
                                String primarycontact = jsContact.getString("primarycontact");

                                if(primarycontact.equals("Yes")){

                                    if(firstname.equals("null")||firstname.isEmpty()){
                                        cet_firstName.setText("");
                                    }else {

                                        cet_firstName.setText(firstname);
                                    }

                                    if(lastname.equals("null")||lastname.isEmpty()){
                                        cet_lastName.setText("");
                                    }else {

                                        cet_lastName.setText(lastname);
                                    }

                                    et_primaryEmail_et.setText(emailaddress1);
                                    et_primaryContact_et.setText(telephone1);

                                    if(emailaddress2.equals("null")||emailaddress2.isEmpty()){
                                        et_secondaryEmail_et.setText("");
                                    }else {
                                        et_secondaryEmail_et.setText(emailaddress2);
                                    }

                                    if(telephone2.equals("null")||telephone2.isEmpty()){
                                        et_SecondaryContact_et.setText("");
                                    }else {
                                        et_SecondaryContact_et.setText(telephone2);
                                    }
                                }

                                if(customertypeid.equals("1")){

                                    if(!billingaddress.isEmpty()){
                                        String[] Array = billingaddress.split(",");
                                        bl_address = Array [0];
                                        bl_country = Array [1];
                                        bl_state  = Array [2];
                                        bl_city  = Array [3];
                                        bl_postal  = Array [4];
                                    }


                                    if(!deliveryaddress.isEmpty()){
                                        String[] Array1 = deliveryaddress.split(",");
                                        dl_address = Array1 [0];
                                        dl_country = Array1 [1];
                                        dl_state  = Array1 [2];
                                        dl_city  = Array1 [3];
                                        dl_postal  = Array1 [4];
                                    }

                                    if(primarycontact.equals("Yes")){
                                        ctList.add(new Model_ContactList(Contact_id,"true",firstname,lastname,jobtitle,email,phonenumber,worknumber,homenumber,
                                                dl_address,dl_country,dl_state,dl_city,dl_postal, bl_address,bl_country,bl_state,bl_city,bl_postal));

                                    }else {
                                        ctList.add(new Model_ContactList(Contact_id,"false",firstname,lastname,jobtitle,email,phonenumber,worknumber,homenumber,
                                                dl_address,dl_country,dl_state,dl_city,dl_postal, bl_address,bl_country,bl_state,bl_city,bl_postal));

                                    }

                                }

                            }

                            JSONArray options = jsdata.getJSONArray("addressInfo");
                            if (options.length() > 0) {

                                for (int i=0; i<options.length(); i++) {
                                    JSONObject js = options.getJSONObject(i);
                                    String id_ = js.getString("id");
                                    String line1 = js.getString("line2");
                                    String country = js.getString("country");
                                    String city = js.getString("city");
                                    String stateorprovince = js.getString("stateorprovince");
                                    String postalcode = js.getString("postalcode");
                                    String addresstypecode = js.getString("addresstypecode");


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(addresstypecode.equals("1")){

                                                spDeliveryCountry.setSelection(arCountryName.indexOf(country));

                                                Delivery_state=stateorprovince;
                                                Delivery_city=city;
                                                Delivery_id_=id_;

                                                et_DeliveryAddress.setText(line1);

                                                et_DeliveryPincode.setText(postalcode);


                                            }else if(addresstypecode.equals("2")){

                                                spBillCountry.setSelection(arBCountryName.indexOf(country));

                                                Bill_state=stateorprovince;
                                                Bill_city=city;
                                                Bill_id_=id_;

                                                et_BillAddress.setText(line1);
                                                et_BillPincode.setText(postalcode);

                                            }

                                        }
                                    });

                                }

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(customertypeid.equals("1")){
                                        //business
                                        radioBusiness.setChecked(true);
                                        radioIndividual.setChecked(false);

                                    }else {
                                        //indial
                                        radioBusiness.setChecked(false);
                                        radioIndividual.setChecked(true);
                                    }

                                    if(istaxable.equals("true")){
                                        CheckTax_ap.setChecked(true);
                                        //radioNonTax.setChecked(false);
                                    }else {
                                        CheckTax_ap.setChecked(false);
                                        //radioNonTax.setChecked(true);
                                    }

                                    ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arStatusIDName);
                                    adapterStatus.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            spStatus.setAdapter(adapterStatus);
                                            if(status.equals("Inactive")){
                                                spStatus.setSelection(arStatusIDName.indexOf("Inactive"));
                                            }else {
                                                spStatus.setSelection(arStatusIDName.indexOf("Active"));
                                            }
                                        }
                                    });


                                    et_customerName.setText(customername);


                                    if(website.equals("null")||website.isEmpty()){
                                        et_website.setText("");
                                    }else {
                                        et_website.setText(website);
                                    }


                                    ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arIndustryName);
                                    adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            spIndustry.setAdapter(adapterBType);
                                            System.out.println("industry---"+industry);
                                            if(!industry.equals("null")||!industry.isEmpty()){
                                                spIndustry.setSelection(arIndustryName.indexOf(industry));
                                            }

                                        }
                                    });

                                    ArrayAdapter<String> adapterBType3 = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arTermsName);
                                    adapterBType3.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            spNetTerms.setAdapter(adapterBType3);
                                            if(!netterms.equals("null")){
                                                if(!netterms.equals("00000000-0000-0000-0000-000000000000")){
                                                    spNetTerms.setSelection(arTermsID.indexOf(netterms));
                                                }

                                            }

                                        }
                                    });

                                    ArrayAdapter<String> adapterBType4 = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arTaxCode);
                                    adapterBType4.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            spCustomerTaxCode.setAdapter(adapterBType4);
                                            if(!taxid.equals("null")){
                                                if(!taxid.equals("00000000-0000-0000-0000-000000000000")){
                                                    spCustomerTaxCode.setSelection(arTaxID.indexOf(taxid));
                                                }

                                            }

                                        }
                                    });

                                }
                            });


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            Utils.getInstance().dismissDialog();

                                        }
                                    }, 5000);
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


    private void getValidation(final View v) {
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate1 = sdf.format(date1);

        if (Utils.getInstance().checkIsEmpty(et_customerName)) {
            Utils.getInstance().snackBarMessage(v,"Enter the Customer Name!");
        }else if (Str_IndustryName.equals("Select Industry")) {
            Utils.getInstance().snackBarMessage(v,"Select Industry!");
        } else if (Str_netTermName.equals("Select Net Terms")) {
            Utils.getInstance().snackBarMessage(v,"Select Net Terms!");
        }  else if (taxStr.equals("0")) {
            Utils.getInstance().snackBarMessage(v,"Select Tax Type!");
        }  else if (Utils.getInstance().checkIsEmpty(et_primaryEmail_et)) {
            Utils.getInstance().snackBarMessage(v,"Enter Primary Email!");
        } else  if (!Utils.getInstance().isEmailValid(et_primaryEmail_et.getText().toString().trim())) {
            Utils.getInstance().snackBarMessage(v,"Please enter valid Primary email address");
        } else if (Utils.getInstance().checkIsEmpty(et_primaryContact_et)) {
            Utils.getInstance().snackBarMessage(v,"Enter Primary Contact!");
        }  else if (Utils.getInstance().checkIsEmpty(et_DeliveryAddress)) {
            Utils.getInstance().snackBarMessage(v,"Enter Delivery Address !");
        }   else if (D_CountryName.equals("Select Country")) {
            Utils.getInstance().snackBarMessage(v,"Select Country!");
        } else if (D_StateName.equals("Select State")) {
            Utils.getInstance().snackBarMessage(v,"Select State!");
        }  else if (D_CityName.equals("Select City")) {
            Utils.getInstance().snackBarMessage(v,"Select City!");
        } else if (Utils.getInstance().checkIsEmpty(et_DeliveryPincode)) {
            Utils.getInstance().snackBarMessage(v,"Enter Postal Zip Code !");
        } else if (Utils.getInstance().checkIsEmpty(et_BillAddress)) {
            Utils.getInstance().snackBarMessage(v,"Enter Billing Address !");
        }  else if (B_CountryName.equals("Select Country")) {
            Utils.getInstance().snackBarMessage(v,"Select Country!");
        }   else if (B_StateName.equals("Select State")) {
            Utils.getInstance().snackBarMessage(v,"Select State!");
        }  else if (B_CityName.equals("Select City")) {
            Utils.getInstance().snackBarMessage(v,"Select City!");
        }else if (Utils.getInstance().checkIsEmpty(et_BillPincode)) {
            Utils.getInstance().snackBarMessage(v,"Enter Postal Zip Code !");
        }  else {

            if(BFlag.equals("1")) {
                if (ctList.size() > 0) {
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
            JSONObject JSON5= new JSONObject();
            JSONArray array1 =new JSONArray();
            JSONArray array3 =new JSONArray();
            JSONArray array5 =new JSONArray();
            JSON1 = new JSONObject();
            try {
                JSON1.put("id", Delivery_id_);
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
                        JSON4.put("id", ctList.get(i).getId().trim());
                        JSON4.put("firstname", ctList.get(i).getFirstNm().trim());
                        JSON4.put("lastname", ctList.get(i).getLastNm().trim());
                        JSON4.put("fullname", ctList.get(i).getFirstNm().trim()+ctList.get(i).getLastNm().trim());
                        JSON4.put("jobtitle", ctList.get(i).getJobTittle().trim());
                        JSON4.put("email", ctList.get(i).getPEmail().trim());
                        JSON4.put("phonenumber", ctList.get(i).getPContact().trim());
                        JSON4.put("worknumber", ctList.get(i).getWrkNum().trim());
                        JSON4.put("homenumber", ctList.get(i).getHNum().trim());
                        JSON4.put("homenumber", ctList.get(i).getHNum().trim());
                        if(ctList.get(i).getIsPrimary().trim().equals("true")){
                            JSON4.put("primarycontact", "Yes");
                        }else {
                            JSON4.put("primarycontact", "No");
                        }
                        JSON4.put("deliveryaddress", ctList.get(i).getDCAddress()+","+ctList.get(i).getDCCountry().trim()+","+ctList.get(i).getDCState().trim()+","+ctList.get(i).getDCCity().trim()+","+ctList.get(i).getDCPinCode().trim());
                        JSON4.put("billingaddress", ctList.get(i).getBCAddress()+","+ctList.get(i).getBCCountry().trim()+","+ctList.get(i).getBCState().trim()+","+ctList.get(i).getBCCity().trim()+","+ctList.get(i).getBCPinCode().trim());
                        JSON4.put("deliveryaddressid", "00000000-0000-0000-0000-000000000000");
                        JSON4.put("billingaddressid", "00000000-0000-0000-0000-000000000000");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array3.put(JSON4);

                }

                JSON5 = new JSONObject();
              /*  try {
                    JSON5.put("modifiedon",currentDate1);
                    JSON5.put("id",Contact_id);
                    JSON5.put("modifiedby",user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                array5.put(JSON5);



            }

            JSONObject JSON2 = new JSONObject();
            JSONArray array2 =new JSONArray();
            JSON2 = new JSONObject();
            try {
                JSON2.put("id", Bill_id_);
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
            json3.put("id",Contact_id);
            json3.put("emailaddress1",et_primaryEmail_et.getText().toString());
            json3.put("telephone1",et_primaryContact_et.getText().toString());
            json3.put("emailaddress2",et_secondaryEmail_et.getText().toString());
            json3.put("telephone2",et_SecondaryContact_et.getText().toString());
            json3.put("useforcreatingpresales",true);


            JSONObject json = new JSONObject();
            json.put("id",CustomerId);
            json.put("customertypeid",BFlag);
            json.put("customername",et_customerName.getText().toString());
            json.put("website",et_website.getText().toString());
            json.put("industry",Str_IndustryName);
            json.put("createdby",user_id);
            json.put("createdon",currentDate1);
            json.put("status",Str_StatusName);
            if(Str_StatusName.equals("Active")){
                json.put("isactive",true);
            }else {
                json.put("isactive",false);
            }
            arStatusIDName.add("");
            arStatusIDName.add("Inactive");
            if(taxStr.equals("1")) {
                json.put("netterms",Str_netTermID);
                json.put("taxid",Str_TaxID);
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
            json.put("deletedContact",array5);
            json.put("isaddresschanged", "true");
            postCustomer(v,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void postCustomer(final View v, final JSONObject json) {
        Progress_dialog.show();
        try {
            App.getInstance().PostCustomerUpdate(json.toString(),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        Utils.getInstance().GetMasterInsert(Activity_UpdateCustomer.this,"CustomerUpdate",Progress_dialog,"");

                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");

                                    if ( succeeded == true) {
                                        if (Progress_dialog != null) {
                                            if (Progress_dialog.isShowing()) {
                                                Progress_dialog.dismiss();
                                            }
                                        }

                                        showSuccess();
                                    }
                                    else {
                                        Toast.makeText(Activity_UpdateCustomer.this, "Please try again later!!", Toast.LENGTH_SHORT).show();
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

    private void showSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateCustomer.this);
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
        tittleAlert_tv.setText("Customer Updated Successful");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UpdateCustomer.this, Activity_CustomerHistory.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }



    private void GetCity(String D_stateID) {
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

                                        adapterDCity = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arCityName);
                                        adapterDCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        spDeliveryCity.setAdapter(adapterDCity);
                                        if(!Delivery_city.equals("null")){
                                            int pos =adapterDCity.getPosition(Delivery_city);
                                            spDeliveryCity.setSelection(pos);
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
    private void GetBillCity(String D_stateID) {
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
                            if (arBCityName.size() > 0) arBCityName.clear();
                            if (arBCityID.size() > 0) arBCityID.clear();
                            if (data.length() > 0) {
                                arBCityName.add("Select City");
                                arBCityID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arBCityName.add(js.getString("displayname"));
                                    arBCityID.add(js.getString("id"));
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapterBCity = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arBCityName);
                                        adapterBCity.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        spBillCity.setAdapter(adapterBCity);

                                        if(D_CityName!= null){
                                            if(!D_CityName.equals("Select City")){
                                                int pos =adapterBCity.getPosition(D_CityName);
                                                spBillCity.setSelection(pos);

                                            }
                                        }

                                        if(!Bill_city.equals("null")){
                                            int pos =adapterBCity.getPosition(Bill_city);
                                            spBillCity.setSelection(pos);
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
    private void GetState(String D_CountryId) {
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

                                        adapterDState = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arStateName);
                                        adapterDState.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        spDeliveryState.setAdapter(adapterDState);
                                        if(!Delivery_state.equals("null")){
                                            int pos =adapterDState.getPosition(Delivery_state);
                                            spDeliveryState.setSelection(pos);
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
    private void GetBillState(String D_CountryId) {
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
                            if (arBStateName.size() > 0) arBStateName.clear();
                            if (arBStateID.size() > 0) arBStateID.clear();
                            if (data.length() > 0) {
                                arBStateName.add("Select State");
                                arBStateID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arBStateName.add(js.getString("displayname"));
                                    arBStateID.add(js.getString("id"));

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        adapterBState = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, arBStateName);
                                        adapterBState.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                        spBillState.setAdapter(adapterBState);

                                        if(D_StateName!= null){
                                            if(!D_StateName.equals("Select State")){
                                                int pos =adapterBState.getPosition(D_StateName);
                                                spBillState.setSelection(pos);
                                            }
                                        }

                                        if(!Bill_state.equals("null")){
                                            int pos =adapterBState.getPosition(Bill_state);
                                            spBillState.setSelection(pos);
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
    private void GetCountryD() {
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

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);

                                    if(js.getString("displayname").equals("USA")){
                                        arCountryName.add(js.getString("displayname"));
                                        arCountryID.add(js.getString("id"));
                                    }

                                }
                                adapterDCountry = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arCountryName);
                                adapterDCountry.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spDeliveryCountry.setAdapter(adapterDCountry);

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
    private void GetCountryB() {
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
                            if (arBCountryName.size() > 0) arBCountryName.clear();
                            if (arBCountryID.size() > 0) arBCountryID.clear();
                            if (data.length() > 0) {

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);

                                    if(js.getString("displayname").equals("USA")){
                                        arBCountryName.add(js.getString("displayname"));
                                        arBCountryID.add(js.getString("id"));
                                    }

                                }
                                adapterBCountry = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arBCountryName);
                                adapterBCountry.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spBillCountry.setAdapter(adapterBCountry);
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
        try {
            App.getInstance().GetIndustry(token,new Callback(){

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
                            if (arIndustryName.size() > 0) arIndustryName.clear();
                            if (arIndustryID.size() > 0) arIndustryID.clear();
                            if (data.length() > 0) {
                                arIndustryName.add("Select Industry");
                                arIndustryID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arIndustryName.add(js.getString("displayname"));
                                    arIndustryID.add(js.getString("id"));
                                }

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

                            }else {
                                arTaxCode.add("Select Tax Code");
                                arTaxCode.add("Not Assigned");

                                arTaxID.add("00000000-0000-0000-0000-000000000000");
                                arTaxID.add("00000000-0000-0000-0000-000000000000");

                                arTaxPercent.add("0");
                                arTaxPercent.add("0");

                                arTaxName.add("0");
                                arTaxName.add("0");

                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arTaxCode);
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

    public void AddNewAlert() {

        runOnUiThread(new Runnable() {
            @SuppressLint({"Range", "MissingInflatedId"})
            @Override
            public void run() {
                CheckBox pr_check;
                AppCompatTextView txt_no_record;
                RecyclerView ry_contactlist;
                AppCompatEditText et_HomeNum,et_firstName,et_lastName,et_jobTittle,et_pEmail_et,et_pContact_et,et_workNum,et_homeNum;
                AppCompatEditText Cet_DeliveryAddress,Cet_DeliveryPincode,Cet_BillAddress,Cet_BillPincode;

                CheckBox CSame_add_chk;


                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_UpdateCustomer.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateOrder.this);
                LayoutInflater inflater = Activity_UpdateCustomer.this.getLayoutInflater();
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
                                    ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,CarStateName);
                                    adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            spBillState.setAdapter(adapterBType);
                                            spBillState.setSelection(arStateName.indexOf("0"));
                                        }
                                    });

                                    ArrayAdapter<String> adapterBType1 = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,CarCityName);
                                    adapterBType1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CspBillCity.setAdapter(adapterBType1);
                                            CspBillCity.setSelection(CarCityName.indexOf("0"));

                                        }
                                    });

                                    ArrayAdapter<String> adapterBType3 = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,CarCountryName);
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
                                        ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,CarCountryName);
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
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_UpdateCustomer.this);
                ry_contactlist.setLayoutManager(mLayoutManager);
                ry_contactlist.setItemAnimator(new DefaultItemAnimator());
                ry_contactlist.addItemDecoration(new ItemDividerDecoration(Activity_UpdateCustomer.this, LinearLayoutManager.VERTICAL));
                // set the adapter object to the Recyclerview
                ry_contactlist.setAdapter(cAdapter);
                ry_contactlist.addOnItemTouchListener(new RecyclerViewTouchListener(Activity_UpdateCustomer.this, ry_contactlist, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        view.findViewById(R.id.dots_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(Activity_UpdateCustomer.this, view, Gravity.RIGHT);
                                popupMenu.getMenuInflater().inflate(R.menu.popup_customermenu, popupMenu.getMenu());
                                Menu popMenu = popupMenu.getMenu();

                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                        switch (menuItem.getItemId()) {
                                            case R.id.primary_btn:

                                                for (int i = 0; i < ctList.size(); i++) {
                                                    ctList.get(i).setIsPrimary("false");
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
                                Toast.makeText(Activity_UpdateCustomer.this, "Contact has been deleted successfully!", Toast.LENGTH_SHORT).show();
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

                                adapterDCountry = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arCountryName);
                                adapterDCountry.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spDeliveryCountry.setAdapter(adapterDCountry);
                                        spDeliveryCountry.setSelection(arCountryName.indexOf(ctList.get(finalI).getDCCountry()));

                                    }
                                });

                                Delivery_state=ctList.get(i).getDCState();
                                Delivery_city=ctList.get(i).getDCCity();

                                et_DeliveryAddress.setText(ctList.get(i).getDCAddress());

                                et_DeliveryPincode.setText(ctList.get(i).getDCPinCode());



                                adapterBCountry = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item,arBCountryName);
                                adapterBCountry.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                int finalI1 = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spBillCountry.setAdapter(adapterBCountry);
                                        spBillCountry.setSelection(arBCountryName.indexOf(ctList.get(finalI1).getBCCountry()));
                                    }
                                });

                                Bill_state=ctList.get(i).getBCState();
                                Bill_city=ctList.get(i).getBCCity();

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
                        Utils.getInstance().hideKeyboard(Activity_UpdateCustomer.this);
                        if (Connectivity.isConnected(Activity_UpdateCustomer.this) &&
                                Connectivity.isConnectedFast(Activity_UpdateCustomer.this)) {

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
                                    ctList.add(new Model_ContactList("00000000-0000-0000-0000-000000000000",Primary__,et_firstName.getText().toString(),et_lastName.getText().toString(),et_jobTittle.getText().toString(),et_pEmail_et.getText().toString(),et_pContact_et.getText().toString(),et_workNum.getText().toString(),et_HomeNum.getText().toString(),
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
                        System.out.println("CD_stateID--"+CD_stateID);

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



                                                CadapterCity = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, CarCityName);
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

                                                CadapterBType = new ArrayAdapter<String>(Activity_UpdateCustomer.this, R.layout.spinner_item, CarStateName);
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

}

