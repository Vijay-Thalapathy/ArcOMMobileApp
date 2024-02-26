package com.example.arcomdriver.payments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Activity_CollectPayments extends Activity_Menu {
    private CoordinatorLayout cl;
    RadioGroup radioGroup_PayMethod,radioGroupPayMethod;
    RadioButton radioFull,radioPartial,radioCheque,radioCash;
    AppCompatTextView et_paymentDate;
    static final int DATE_PICKER1_ID = 1;
    private int mYear, mMonth, mDay;
    String user_id,token,Email;
    //Toolbar toolbar;
    AppCompatEditText et_payableAmt,et_ReceivedAmt,et_dueAmt,et_reference;
    String Newinvoiceid,deliverywarehouseid,deliveryshipmenttypeid,OrderID,TotalPaymentAmt,PayTypeFlag = "Full",PayCashFlag = "4E142367-5F99-4AA7-99ED-931756978EE5",PayCash="Cash",paymentDate_str;

    public AlertDialog Progress_dialog;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_deliver);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deliver, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Add Payment");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       // toolbar = findViewById(R.id.toolbar);



        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CollectPayments.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_CollectPayments.this.getLayoutInflater();
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
                    Email = user_c.getString(user_c.getColumnIndex("Email"));
                }

            }
        } finally {
            sqLiteController1.close();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {;
            Newinvoiceid = extras.getString("Newinvoiceid");
            TotalPaymentAmt = extras.getString("TotalPaymentAmt");
            OrderID = extras.getString("OrderID");
            deliveryshipmenttypeid = extras.getString("deliveryshipmenttypeid");
            deliverywarehouseid = extras.getString("deliverywarehouseid");

        }

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


      //  txt_ImgName = findViewById(R.id.txt_ImgName);
        et_paymentDate = findViewById(R.id.et_paymentDate);
        AppCompatButton btn_proceedNxt = findViewById(R.id.btn_proceedNxt);
        btn_proceedNxt.setText("Submit");

        Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
        //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate2 = sd2.format(date2);
        et_paymentDate.setText(currentDate2);

        findViewById(R.id.et_paymentDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_CollectPayments.this);
                showDialog(DATE_PICKER1_ID);
            }
        });

        radioGroup_PayMethod = (RadioGroup) findViewById(R.id.radioGroup_PayMethod);
        radioGroupPayMethod = (RadioGroup) findViewById(R.id.radioGroupPayMethod);
        radioFull = (RadioButton) findViewById(R.id.radioFull);
        radioPartial = (RadioButton) findViewById(R.id.radioPartial);
        radioCash = (RadioButton) findViewById(R.id.radioCash);
        radioCheque = (RadioButton) findViewById(R.id.radioCheque);
       // radioCredit = (RadioButton) findViewById(R.id.radioCredit);
        et_payableAmt = (AppCompatEditText) findViewById(R.id.et_payableAmt);
        et_ReceivedAmt = (AppCompatEditText) findViewById(R.id.et_ReceivedAmt);
        et_dueAmt = (AppCompatEditText) findViewById(R.id.et_dueAmt);
        et_reference = (AppCompatEditText) findViewById(R.id.et_reference);
        //et_payMemo = (AppCompatEditText) findViewById(R.id.et_payMemo);

        et_ReceivedAmt.addTextChangedListener(new TextWatcher() {

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
                    if (!TextUtils.isEmpty(String.valueOf(et_payableAmt.getText().toString()))) {
                        if(Double.parseDouble(et_payableAmt.getText().toString()) ==Double.parseDouble(et_ReceivedAmt.getText().toString())){
                          //  double x = Math.abs(Double.parseDouble(et_payableAmt.getText().toString())-Double.parseDouble(et_ReceivedAmt.getText().toString()));
                            et_dueAmt .setText("0.00");
                        }else {
                            Toast.makeText(Activity_CollectPayments.this, "Amount paid should be equal to payable amount.", Toast.LENGTH_SHORT).show();
                            double x = Math.abs(Double.parseDouble(et_payableAmt.getText().toString())-Double.parseDouble(et_ReceivedAmt.getText().toString()));
                            et_dueAmt .setText(Utils.truncateDecimal(x));
                        }
                    }


                }else {
                    et_ReceivedAmt .setText("0");
                }


            }
        });

        if(TotalPaymentAmt!=null){
            et_payableAmt.setText(TotalPaymentAmt);
            et_ReceivedAmt.setText(TotalPaymentAmt);
        }



       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        findViewById(R.id.btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  onBackPressed();
                startActivity(new Intent(Activity_CollectPayments.this, Activity_OrdersHistory.class));

            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CollectPayments.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CollectPayments.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CollectPayments.this, Activity_InvoiceHistory.class));
            }
        });

/*
        findViewById(R.id.select_Img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_Deliver.this);
                image_layout = 1;
                select_image();

            }
        });
*/


        radioGroup_PayMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioFull) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayTypeFlag = "Full";
                        }
                    });

                }else if (checkedId == R.id.radioPartial) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayTypeFlag = "Partial";
                        }
                    });

                }
            }

        });
        radioGroupPayMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioCash) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayCashFlag = "4E142367-5F99-4AA7-99ED-931756978EE5";
                            PayCash = "Cash";
                        }
                    });

                }else if (checkedId == R.id.radioCheque) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayCashFlag = "7E1C2265-33C4-4F38-A1B3-55A80662A99C";
                            PayCash = "Check";
                        }
                    });

                }
            }

        });


        findViewById(R.id.btn_proceedNxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_CollectPayments.this);
                if (Connectivity.isConnected(Activity_CollectPayments.this) &&
                        Connectivity.isConnectedFast(Activity_CollectPayments.this)) {
                    if (et_paymentDate.getText().toString().equals("MM/DD/YYYY")) {
                        Utils.getInstance().snackBarMessage(v,"Choose Payment Date!");
                    } else if (Utils.getInstance().checkIsEmpty(et_payableAmt)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Payable Amount!");
                    } else if (Utils.getInstance().checkIsEmpty(et_ReceivedAmt)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Received Amount!");
                    } else if (Utils.getInstance().checkIsEmpty(et_dueAmt)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Due Amount!");
                    }else {
                        if(Double.parseDouble(et_payableAmt.getText().toString()) ==Double.parseDouble(et_ReceivedAmt.getText().toString())){

                            //   startActivity(new Intent(Activity_Deliver.this, Activity_ProofDelivery.class));
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                            Date date = null;
                            try {
                                date = sdf.parse(et_paymentDate.getText().toString());
                                paymentDate_str =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date date1 = new Date();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String currentDate1 = sdf1.format(date1);

                            try {
                                JSONObject json = new JSONObject();

                                json.put("invoiceid",Newinvoiceid);//null
                                json.put("paymentmethodid",PayCashFlag);//null
                                json.put("paymentdate",paymentDate_str);//presale pricing Date
                                json.put("payable",et_payableAmt.getText().toString().trim());
                                json.put("paid",et_ReceivedAmt.getText().toString().trim());
                                json.put("due",et_dueAmt.getText().toString().trim());
                                json.put("checknumber","");//null
                                json.put("paymentmemo","");//null
                                json.put("isactive",true);
                                json.put("createdby",user_id);
                                json.put("creratedon",currentDate1);
                                json.put("modifiedon",currentDate1);
                                json.put("modifiedby",user_id);
                               // json.put("uploadRequest", array);
                                //json.put("uploadSign", JsonSign);
                                System.out.println("CollectPayment--+"+json);

                                Progress_dialog.show();
                                postPayment(json.toString().trim(),currentDate1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }else {
                            Toast.makeText(Activity_CollectPayments.this, "Amount paid should be equal to payable amount.", Toast.LENGTH_SHORT).show();
                        }



                    }
                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }
            }
        });
    }



    private void postPayment(String json,String currentDate1) {

        try {
            App.getInstance().PostPayment(json,token, new Callback() {
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
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {

                                        JSONObject json = new JSONObject();
                                        try {
                                            if(deliveryshipmenttypeid==null){
                                            }else {
                                                if(!deliveryshipmenttypeid.equals("null")){
                                                    json.put("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                                }
                                            }
                                            //json.put("fulfillmentstatus","Paid");
                                            json.put("Id",OrderID);
                                            json.put("paymenterid",user_id);

                                            if(deliverywarehouseid.equals("null")){

                                            }else {
                                                json.put("warehouseid",deliverywarehouseid);
                                            }

                                            System.out.println("UpdateStatus----"+json.toString());
                                            postUpdateFull(json.toString(),currentDate1);
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

    private void postUpdateFull(String json,String currentDate1) {
        try {
            App.getInstance().PostWarehouseSummaryFul(json,token, new Callback() {
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
                                    System.out.println("Fulfillment Success---"+jsonObject);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        JSONObject json = new JSONObject();
                                        try {
                                            json.put("submitstatus","Fulfilled");
                                            json.put("id",OrderID);
                                            json.put("modifiedon",currentDate1);
                                            json.put("modifiedby",user_id);

                                            System.out.println("FulfilledFulPayment----"+json.toString());
                                            PostStatusUpdate(json.toString(),currentDate1);
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

    private void PostStatusUpdate(String json,String currentDate1) {
        try {
            App.getInstance().PostStatusUpdate(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        Utils.getInstance().GetMasterInsert(Activity_CollectPayments.this,"Payment",Progress_dialog,currentDate1);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CollectPayments.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_successalert_payment, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
        btn_Okalrt.setVisibility(View.GONE);
        AppCompatButton btn_AlertShareInvoice = v.findViewById(R.id.btn_AlertInvoice);
        //btn_AlertShareInvoice.setText("Continue");
        btn_AlertShareInvoice.setText("Close");
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        tittleAlert_tv.setText("Payment Captured Successful");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CollectPayments.this, Activity_OrdersHistory.class));
                dialog.dismiss();
            }
        });
        btn_AlertShareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invoice payment
               /* Bundle bundle1 = new Bundle();
                bundle1.putString("InvoiceID",Newinvoiceid);
                bundle1.putString("ShareFlag","1");
                Intent in1 = new Intent(Activity_CollectPayments.this, ActivityShareInvoice.class);
                in1.putExtras(bundle1);
                startActivity(in1);
                dialog.dismiss();*/

                startActivity(new Intent(Activity_CollectPayments.this, Activity_OrdersHistory.class));
                dialog.dismiss();
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
        }
        return null;
    }



    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et_paymentDate.setText(new StringBuilder()
                    .append(selectedMonth+1)
                    .append("/")
                    .append(selectedDay)
                    .append("/")
                    .append(selectedYear).toString());
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}