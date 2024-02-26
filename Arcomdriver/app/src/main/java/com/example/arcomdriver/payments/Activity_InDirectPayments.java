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
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_InDirectPayments extends Activity_Menu {
    private CoordinatorLayout cl;
    RadioGroup radioGroup_PayMethod,radioGroupPayMethod;
    RadioButton radioFull,radioPartial,radioCheque,radioCash;
    AppCompatTextView et_paymentDate;
    static final int DATE_PICKER1_ID = 1;
    private int mYear, mMonth, mDay;
    String user_id,token,Email;
    AppCompatEditText et_payableAmt,et_ReceivedAmt,et_dueAmt,et_reference;
    String InvoiceLastSync,paymentMethod,TotalPaymentAmt,InvoiceID,PayTypeFlag = "Full",PayCashFlag = "4E142367-5F99-4AA7-99ED-931756978EE5",paymentDate_str;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_InDirectPayments.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_InDirectPayments.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

       // toolbar = findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            InvoiceID = extras.getString("InvoiceID");
            TotalPaymentAmt = extras.getString("TotalPaymentAmt");
            paymentMethod = extras.getString("paymentMethod");

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
                    Email = user_c.getString(user_c.getColumnIndex("Email"));
                }

                //ReadMasterSync
                Cursor Csync = sqLiteController1.readTableMasterSync();
                if (Csync.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String master_sync_name = Csync.getString(Csync.getColumnIndex("master_sync_name"));
                        @SuppressLint("Range") String master_sync_time = Csync.getString(Csync.getColumnIndex("master_sync_time"));

                        System.out.println("master_sync_name----"+master_sync_name);
                        System.out.println("master_sync_time----"+master_sync_time);

                        if(master_sync_name.equals("Invoices")){
                            InvoiceLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }
                    } while (Csync.moveToNext());
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

        et_paymentDate = findViewById(R.id.et_paymentDate);

        Date date2 = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("MM/dd/yyyy");
        //sd2.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate2 = sd2.format(date2);
        et_paymentDate.setText(currentDate2);

        findViewById(R.id.et_paymentDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().hideKeyboard(Activity_InDirectPayments.this);
                showDialog(DATE_PICKER1_ID);
            }
        });

        radioGroup_PayMethod = (RadioGroup) findViewById(R.id.radioGroup_PayMethod);
        radioGroupPayMethod = (RadioGroup) findViewById(R.id.radioGroupPayMethod);
        radioFull = (RadioButton) findViewById(R.id.radioFull);
        radioPartial = (RadioButton) findViewById(R.id.radioPartial);
        radioCash = (RadioButton) findViewById(R.id.radioCash);
        radioCheque = (RadioButton) findViewById(R.id.radioCheque);
        et_payableAmt = (AppCompatEditText) findViewById(R.id.et_payableAmt);
        et_ReceivedAmt = (AppCompatEditText) findViewById(R.id.et_ReceivedAmt);
        et_dueAmt = (AppCompatEditText) findViewById(R.id.et_dueAmt);
        et_reference = (AppCompatEditText) findViewById(R.id.et_reference);
        AppCompatButton btn_proceedNxt = (AppCompatButton) findViewById(R.id.btn_proceedNxt);
        btn_proceedNxt.setText("Submit");

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
                            Toast.makeText(Activity_InDirectPayments.this, "Amount paid should be equal to payable amount.", Toast.LENGTH_SHORT).show();
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


        findViewById(R.id.btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InDirectPayments.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InDirectPayments.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InDirectPayments.this, Activity_InvoiceHistory.class));
            }
        });


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

        if(paymentMethod.equals("Cash")){
            radioCash.setChecked(true);
            radioCheque.setChecked(false);
            PayCashFlag = "4E142367-5F99-4AA7-99ED-931756978EE5";
        }else {
            radioCash.setChecked(false);
            radioCheque.setChecked(true);
            PayCashFlag = "7E1C2265-33C4-4F38-A1B3-55A80662A99C";
        }
        radioGroupPayMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioCash) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayCashFlag = "4E142367-5F99-4AA7-99ED-931756978EE5";
                        }
                    });

                }else if (checkedId == R.id.radioCheque) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayCashFlag = "7E1C2265-33C4-4F38-A1B3-55A80662A99C";
                        }
                    });

                }
            }

        });

        findViewById(R.id.btn_proceedNxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_InDirectPayments.this);
                if (Connectivity.isConnected(Activity_InDirectPayments.this) &&
                        Connectivity.isConnectedFast(Activity_InDirectPayments.this)) {
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



                            JSONObject JsonSign = new JSONObject();
                            JSONArray array = new JSONArray();
                            try {
                                JSONObject json = new JSONObject();
                                json.put("invoiceid",InvoiceID);//null
                                json.put("paymentmethodid",PayCashFlag);//null
                                json.put("paymentdate",paymentDate_str);//presale pricing Date
                                json.put("payable",et_payableAmt.getText().toString().trim());
                                json.put("paid",et_ReceivedAmt.getText().toString().trim());
                                json.put("due",et_dueAmt.getText().toString().trim());
                                json.put("checknumber","");//null
                                json.put("paymentmemo","ef");//null
                                json.put("isactive",true);
                                json.put("createdby",user_id);
                                json.put("creratedon",currentDate1);
                                json.put("modifiedon",currentDate1);
                                json.put("modifiedby",user_id);
                               // json.put("uploadRequest", array);
                               // json.put("uploadSign", JsonSign);
                                System.out.println("CreatePayment--+"+json);

                                Progress_dialog.show();
                                postPayment(json.toString().trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        //   Activity_DirectDelivery.this.finish();


                        }else {
                            Toast.makeText(Activity_InDirectPayments.this, "Amount paid should be equal to payable amount.", Toast.LENGTH_SHORT).show();
                        }



                    }
                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }
            }
        });
    }


    private void postPayment(String json) {

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
                        Utils.getInstance().GetMasterInsert(Activity_InDirectPayments.this,"DirectInvoicePayment",Progress_dialog,"");
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        JSONObject json = new JSONObject();
                                        /*    new Handler().postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                if (Progress_dialog != null) {
                                                    if (Progress_dialog.isShowing()) {
                                                        Progress_dialog.dismiss();
                                                    }
                                                }
                                                showSuccess();
                                            }
                                        }, 6000);*/
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_InDirectPayments.this);
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
        btn_AlertShareInvoice.setText("Close");
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        //tittleAlert_tv.setText("order delivered Successful \n\n To proceed payment Invoice has to be generated");
        tittleAlert_tv.setText("Payment Captured Successful");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InDirectPayments.this, Activity_InvoiceHistory.class));
                dialog.dismiss();
            }
        });
        btn_AlertShareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //invoice payment
                startActivity(new Intent(Activity_InDirectPayments.this, Activity_InvoiceHistory.class));
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