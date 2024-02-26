package com.example.arcomdriver.invoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.print.PDFPrint;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_InvoiceHistory;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.model.Model_InvoiceHistory;
import com.example.arcomdriver.model.Model_InvoiceProductItems;
import com.example.arcomdriver.model.Model_OrderList;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
 * @author : SivaramYogesh
 * Created on : 30 Jan 2023*/
public class Activity_InvoiceHistory extends Activity_Menu {
    private CoordinatorLayout cl;
    Adapter_InvoiceHistory adapter;
    ArrayList<Model_InvoiceHistory> listInvoice = new ArrayList<>();
    private RecyclerView mRecyclerView;
    AppCompatTextView InTotal_tv,InPaid_tv,InUnPaid_tv;

    LottieAnimationView img_noInvoice;
    private SearchView searchBar;
    ArrayList<Model_InvoiceHistory> filteredModelList = new ArrayList<>();
    private SwipeRefreshLayout srl;

    int unpaid =0;
    int paid =0;
    String token;
    String  str_Status ="All";

    public static String APIInvoiceHTML ="null";

    String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";

    ArrayList<Model_InvoiceProductItems> listPDfInvoice = new ArrayList<>();

    public AlertDialog Progress_dialog;

     String TotalInvoice_s,PaidInvoice_s,UnPaidInvoice_s;

    private SharedPreferences InSharedPreferences;

    String Companylogo;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_invoiceviewlist, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Invoice");
        createOrder_img.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Companylogo =getCompanyLogo();

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_InvoiceHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_InvoiceHistory.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        InTotal_tv = findViewById(R.id.InTotal_tv);
        InPaid_tv = findViewById(R.id.InPaid_tv);
        InUnPaid_tv = findViewById(R.id.InUnPaid_tv);

        SQLiteController sqLiteControllerHome = new SQLiteController(this);
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
                }


            }
        } finally {
            sqLiteControllerHome.close();

        }

        Date dateTime = new Date();
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String currentDate = sdfTime.format(dateTime);

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

        try {
            JSONObject json = new JSONObject();
            json.put("screenid","e5328976-ebcc-4a7f-ab95-c0e4bc6f787f");
            json.put("ownerid","d4ab35c2-4dce-454f-8bf3-77e42dc6c95d");
            json.put("todayDate",currentDate);
            json.put("thisWeekStartDate",WeekStart);
            json.put("thisWeekEndDate",WeekEnd);
            json.put("thisMonthStartDate",monthStart);
            json.put("thisMonthEndDate",monthEnd);
            System.out.println("res---"+json.toString());
            InvoiceFilterkpi(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TotalInvoice_s =Activity_OrdersHistory.TotalInvoice_s;
        PaidInvoice_s =Activity_OrdersHistory.PaidInvoice_s;
        UnPaidInvoice_s =Activity_OrdersHistory.UnPaidInvoice_s;

        InTotal_tv.setText(TotalInvoice_s);
        InPaid_tv.setText(PaidInvoice_s);
        InUnPaid_tv.setText(UnPaidInvoice_s);

        cl = findViewById(R.id.cl);
        srl = findViewById(R.id.swipe);
        searchBar = (SearchView) findViewById(R.id.search_inview);
        mRecyclerView = findViewById(R.id.rc_InvoiceList);
        img_noInvoice = findViewById(R.id.img_noInvoice);


        AppCompatButton btn_All = findViewById(R.id.btn_All);
        AppCompatButton btn_paid = findViewById(R.id.btn_paid);
        AppCompatButton btn_Unpaid = findViewById(R.id.btn_Unpaid);
        btn_All.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                Progress_dialog.show();

                str_Status ="All";

                btn_All.setBackgroundResource(R.drawable.shape_btn1);
                btn_paid.setBackgroundResource(R.drawable.shape_btn5);
                btn_Unpaid.setBackgroundResource(R.drawable.shape_btn4);

                GetInvoiceListSql("All");

            }
        });
        btn_paid.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                Progress_dialog.show();

                str_Status ="Paid";

                btn_paid.setBackgroundResource(R.drawable.shape_btn7);
                btn_All.setBackgroundResource(R.drawable.shape_btn2);
                btn_Unpaid.setBackgroundResource(R.drawable.shape_btn4);

                GetInvoiceListSql("Paid");
            }
        });
        btn_Unpaid.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                Progress_dialog.show();

                str_Status ="Payment pending";

                btn_Unpaid.setBackgroundResource(R.drawable.shape_btn3);
                btn_All.setBackgroundResource(R.drawable.shape_btn2);
                btn_paid.setBackgroundResource(R.drawable.shape_btn5);

                GetInvoiceListSql("Payment pending");
            }
        });

        findViewById(R.id.createOrder_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("In_Flag","0");
                bundle.putString("In_OrderID","0");
                Intent in = new Intent(Activity_InvoiceHistory.this, Activity_CreateInvoice.class);
                in.putExtras(bundle);
                startActivity(in);
                overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InvoiceHistory.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InvoiceHistory.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_InvoiceHistory.this, Activity_InvoiceHistory.class));
            }
        });

        adapter = new Adapter_InvoiceHistory(listInvoice);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                APIInvoiceHTML ="null";

                if(filteredModelList.size()>0){


                    view.findViewById(R.id.InvNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("Invoiceid",filteredModelList.get(position).getInvoiceid());
                            bundle.putString("CustomerName",filteredModelList.get(position).getCustomername());
                            Intent in = new Intent(Activity_InvoiceHistory.this, ActivityInvoiceDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });
                    view.findViewById(R.id.viewDownloadImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetInvoiceSummary(filteredModelList.get(position).getInvoiceid());

                        }
                    });

                }else {

                    view.findViewById(R.id.InvNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("Invoiceid",listInvoice.get(position).getInvoiceid());
                            bundle.putString("CustomerName",listInvoice.get(position).getCustomername());
                            Intent in = new Intent(Activity_InvoiceHistory.this, ActivityInvoiceDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });

                    view.findViewById(R.id.viewDownloadImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetInvoiceSummary(listInvoice.get(position).getInvoiceid());

                        }
                    });

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something
                //SEARCH FILTER
                filteredModelList = filter(listInvoice, newText.toString());
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
                Utils.getInstance().hideKeyboard(Activity_InvoiceHistory.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                Utils.getInstance().GetMasterInsert(Activity_InvoiceHistory.this,"InvoiceRefresh",Progress_dialog,PreviousDate);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (srl.isRefreshing()) srl.setRefreshing(false);
                        GetInvoiceListSql(str_Status);
                    }
                }, 6000);

                str_Status ="All";

                btn_All.setBackgroundResource(R.drawable.shape_btn1);
                btn_paid.setBackgroundResource(R.drawable.shape_btn4);
                btn_Unpaid.setBackgroundResource(R.drawable.shape_btn4);

            }
        });

        GetInvoiceListSql(str_Status);
    }

    private void CreateInvoicePDF() {

        String localRowhtml = "";

        for (int i = 0; i < listPDfInvoice.size(); i++) {

            localRowhtml += InvoiceRowHtml;

            if(listPDfInvoice.get(i).getAll_upsc().equals("null")){
                localRowhtml = localRowhtml.replace("prodname", listPDfInvoice.get(i).getProduct_name());

            }else {
                localRowhtml = localRowhtml.replace("prodname", listPDfInvoice.get(i).getProduct_name()+"\n- "+listPDfInvoice.get(i).getAll_upsc());

            }

            localRowhtml = localRowhtml.replace("UPC Code", listPDfInvoice.get(i).getAll_description());
            localRowhtml = localRowhtml.replace("price", listPDfInvoice.get(i).getProduct_price());
            localRowhtml = localRowhtml.replace("Qty", listPDfInvoice.get(i).getProduct_quantity());


            if (Activity_OrdersHistory.tax.equals("1")){
                if(listPDfInvoice.get(i).getItemistaxable().equals("true")){
                    localRowhtml = localRowhtml.replace("Tax", "Taxable");

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                }
            }else {
                localRowhtml = localRowhtml.replace("Tax", "-");

            }

            localRowhtml = localRowhtml.replace("amount", listPDfInvoice.get(i).getProduct_price());

        }

        APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceProduct$", localRowhtml);


        APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$", Companylogo);

        FileManager.getInstance().cleanTempFolder(Activity_InvoiceHistory.this);
        final File savedPDFFile = FileManager.getInstance().createTempFile(Activity_InvoiceHistory.this, "pdf", false);

        PDFUtil.generatePDFFromHTML(Activity_InvoiceHistory.this, savedPDFFile, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIInvoiceHTML+
                "</body>\n" +
                "</html>", new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                // Open Pdf Viewer
                Uri pdfUri = Uri.fromFile(savedPDFFile);

                Bundle bundle = new Bundle();
                bundle.putString("Order_flag","0");
                Intent intentPdfViewer = new Intent(Activity_InvoiceHistory.this, PdfViewerActivity.class);
                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);
                intentPdfViewer.putExtras(bundle);
                startActivity(intentPdfViewer);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @SuppressLint("Range")
    private void GetInvoiceListSql(String str_Status) {

        unpaid =0;
        paid =0;

        SQLiteController sqLiteController = new SQLiteController(this);

        sqLiteController.open();
        try {
            long count = sqLiteController.fetchInvoiceListCount();

            System.out.println("InvoiceCount--"+count);

            if (count > 0) {
                if (listInvoice.size() > 0){
                    listInvoice.clear();
                }
                //Order
                Cursor invoice_c = sqLiteController.readJointTableInvoice();
                if (invoice_c != null && invoice_c.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String invoiceNum = invoice_c.getString(invoice_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String orderid = invoice_c.getString(invoice_c.getColumnIndex("orderid"));
                        @SuppressLint("Range") String invoicedate = invoice_c.getString(invoice_c.getColumnIndex("invoicedate"));
                        @SuppressLint("Range") String customername = invoice_c.getString(invoice_c.getColumnIndex("customername"));
                        @SuppressLint("Range") String totalamount = invoice_c.getString(invoice_c.getColumnIndex("totalamount"));
                        @SuppressLint("Range") String invoiceid = invoice_c.getString(invoice_c.getColumnIndex("id"));
                        @SuppressLint("Range") String status = invoice_c.getString(invoice_c.getColumnIndex("Insubmitstatus"));

                        /*String in_num;
                        if(invoiceNum.startsWith("DRFIN")){
                            in_num =invoiceNum;
                        }else {
                            Double price =Double.valueOf(invoiceNum);
                            DecimalFormat format = new DecimalFormat("0.#");

                            //in_num =format.format(price);
                            in_num =invoiceNum;

                        }*/

                      /*  if(invoiceNum.startsWith("DRFIN")){
                            status ="Payment pending";
                        }*/

                        if(status.equals("Payment pending")){
                            unpaid += 1;
                        }

                        if(status.equals("Paid")){
                            paid += 1;
                        }

                        if(str_Status.equals("All")){

                            if(status.equals("Payment pending")){
                                listInvoice.add(new Model_InvoiceHistory(invoiceid, invoiceNum, invoicedate, customername, totalamount, status));
                            }

                            if(status.equals("Paid")){
                                listInvoice.add(new Model_InvoiceHistory(invoiceid, invoiceNum, invoicedate, customername, totalamount, status));
                            }

                        }else if(str_Status.equals("Paid")){


                            if(status.equals("Paid")){
                                listInvoice.add(new Model_InvoiceHistory(invoiceid, invoiceNum, invoicedate, customername, totalamount, status));

                            }

                        }else if(str_Status.equals("Payment pending")){

                            if(status.equals("Payment pending")){
                                listInvoice.add(new Model_InvoiceHistory(invoiceid, invoiceNum, invoicedate, customername, totalamount, status));
                            }

                        }



                    } while (invoice_c.moveToNext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_noInvoice.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            // Collections.reverse(listInvoice);
                            if (Progress_dialog != null) {
                                if (Progress_dialog.isShowing()) {
                                    Progress_dialog.dismiss();
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                if (!Connectivity.isConnected(Activity_InvoiceHistory.this) &&
                                        !Connectivity.isConnectedFast(Activity_InvoiceHistory.this)) {
                                    InUnPaid_tv.setText(String.valueOf(unpaid));
                                    InPaid_tv.setText(String.valueOf(paid));
                                    InTotal_tv.setText(String.valueOf(listInvoice.size()));

                                }


                                }
                            });


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

    private ArrayList<Model_InvoiceHistory> filter(ArrayList<Model_InvoiceHistory> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_InvoiceHistory> filteredModelList = new ArrayList<>();

        for (Model_InvoiceHistory model : models) {

            final String getOrder_name = model.getCustomername().toLowerCase();
            final String getOrder_status = model.getInvoiceNum().toLowerCase();
            final String getOrder_number = model.getStatus().toLowerCase();

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

    @SuppressLint("Range")
    private void GetInvoiceSummary(String Id_) {
        Utils.getInstance().loadingDialog(Activity_InvoiceHistory.this, "Please wait.");
        //OrdersSpinner
        SQLiteController sqLiteControllerC = new SQLiteController(Activity_InvoiceHistory.this);
        sqLiteControllerC.open();
        try {
            long fetchAddressCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchAddressCount > 0) {
                Cursor C_Address = sqLiteControllerC.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("InvoiceTemplatePreview")){

                            APIInvoiceHTML  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));

                        }

                    } while (C_Address.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();
        }

        try {
            App.getInstance().GetInvoiceSummary(Id_,token,new Callback(){

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.getInstance().dismissDialog();
                                if(listPDfInvoice.size()>0)listPDfInvoice.clear();
                            }
                        });

                        try {
                            JSONObject jsonObject = new JSONObject(res);

                            String ordernumber = jsonObject.getString("ordernumber");
                            String invoicedate = jsonObject.getString("invoicedate");
                            String totalamount = jsonObject.getString("totalamount");
                            String totallineitemamount = jsonObject.getString("totallineitemamount");
                            String discountpercentage = jsonObject.getString("discountpercentage");
                            String discountamount = jsonObject.getString("discountamount");
                            String freightamount = jsonObject.getString("freightamount");
                            String totaltaxbase = jsonObject.getString("totaltaxbase");
                            String totaltax = jsonObject.getString("totaltax");
                            String paymenttype = jsonObject.getString("paymenttype");
                            String salesrep = jsonObject.getString("salesrep");
                            String presaleordernumber = jsonObject.getString("presaleordernumber");
                            String termsconditions = jsonObject.getString("termsconditions");

                            String billaddress = jsonObject.getString("billaddress");
                            String shipaddress = jsonObject.getString("shipaddress");

                            JSONArray jsInvoiceproduct = jsonObject.getJSONArray("invoiceproduct");

                       runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                    Date date = null;
                                    try {
                                        date = sdf.parse(invoicedate);

                                        String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                        String b=billaddress.replace("<br/>",", ");
                                        String s=shipaddress.replace("<br/>",", ");

                                        String description ="";

                                        for (int i = 0; i < jsInvoiceproduct.length(); i++) {
                                            JSONObject js = jsInvoiceproduct.getJSONObject(i);
                                            String productname = js.getString("productname");
                                            String quantity = js.getString("quantity");
                                            String baseamount = js.getString("baseamount");
                                            String priceperunit = js.getString("priceperunit");
                                            String itemistaxable = js.getString("itemistaxable");
                                            String upccode = js.getString("upccode");
                                            String productid = js.getString("productid");

                                            SQLiteController sqLiteController = new SQLiteController(Activity_InvoiceHistory.this);
                                            sqLiteController.open();
                                            try {
                                                long count = sqLiteController.fetchOrderCount();
                                                if (count > 0) {
                                                    Cursor product_c1 = sqLiteController.readAllTableProduct();
                                                    if (product_c1 != null && product_c1.moveToFirst()) {
                                                        do {

                                                            @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                            if(product_id.equals(productid)){
                                                                @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                                @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                                                description = product_c1.getString(product_c1.getColumnIndex("all_description"));


                                                            }

                                                        } while (product_c1.moveToNext());
                                                    }


                                                }
                                            } finally {
                                                sqLiteController.close();
                                            }

                                            listPDfInvoice.add(new Model_InvoiceProductItems(productname, quantity, priceperunit, baseamount,itemistaxable,upccode,description));
                                        }


                                        String Outpur_Str = Utils.convertToIndianCurrency(totalamount);

                                        System.out.println("APIInvoiceHTML--"+APIInvoiceHTML);


                                        if(!APIInvoiceHTML.equals("null")){
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$COMPANYADDRESS$", " ");
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", " IN_"+ordernumber);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceDate$", date_order);
                                            if(!presaleordernumber.equals("null")){
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", " SO "+presaleordernumber);
                                            }else {
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                            }

                                            if(!paymenttype.equals("null")){
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", paymenttype);
                                            }else {
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", "-");
                                            }


                                            APIInvoiceHTML = APIInvoiceHTML.replace("$salesPerson$", salesrep);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$referencePo$", "0");
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$BillingAddress$", b);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAddress$", s);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$totalUnit$", "1");

                                            APIInvoiceHTML = APIInvoiceHTML.replace("$SubTotal$", Activity_OrdersHistory.currency_symbol+totallineitemamount);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$DiscountAmount$", Activity_OrdersHistory.currency_symbol+discountamount);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$DiscountPercentage$", "("+discountpercentage+" % )");
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAmount$", Activity_OrdersHistory.currency_symbol+freightamount);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$TaxPercentage$", "("+totaltaxbase+" % )");
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$TaxAmount$", Activity_OrdersHistory.currency_symbol+totaltax);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamount);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$TermsandConditions$", termsconditions);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmountInWords$", Outpur_Str);
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$customerNotes$", "Thank you for your business");
                                            CreateInvoicePDF();
                                        }


                                    } catch (ParseException | JSONException e) {
                                        e.printStackTrace();
                                    }

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
    private void InvoiceFilterkpi(String json) {
        try {
            App.getInstance().InvoiceFilterkpi(json.toString(),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        System.out.println("OrderINres---"+res);

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
                                                InTotal_tv.setText(count);
                                            }else if(persistanceid.equals("d7ef00e1-d912-42d0-861c-b4535a25cde0")){
                                                //Paid
                                                InPaid_tv.setText(count);
                                            }else if(persistanceid.equals("f49b1eef-8d01-4356-af8a-08daca1c29a5")){
                                                //UnPaid
                                                InUnPaid_tv.setText(count);
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


    private String getCompanyLogo() {
        InSharedPreferences = getSharedPreferences(Activity_OrdersHistory.ohPref, MODE_PRIVATE);
        if (InSharedPreferences.contains(Activity_OrdersHistory.CmpLogo)) {
            return InSharedPreferences.getString(Activity_OrdersHistory.CmpLogo, null);
        }
        return " ";
    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

}
