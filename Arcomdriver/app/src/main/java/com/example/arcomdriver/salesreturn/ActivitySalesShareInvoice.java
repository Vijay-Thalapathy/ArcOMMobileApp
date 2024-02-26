package com.example.arcomdriver.salesreturn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.helper.ShareInvoicePdfViewer;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.printer.ThermalPrint;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
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
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class ActivitySalesShareInvoice extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    PDFView pdfView;
    String Bill_Add,date_order_,Ship_Add;
    String FilenameName;

    public String  Invoicenumber,Invoiceid,customername,user_email,salesrep_name,ordernumber,totalamount,submitstatus,date_order;
    public String sMsg,APIInvoiceHTML ="null",InvoivePDFPreview;
    public String InvoiceEmailTemplate ="null",alert ="true";;

/*
    String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";
*/
    String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">Desc</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;text-align: center;\" align=\"center\">Qty</td>\n" +
        "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;text-align: center;\">Unit</td>\n" +
           // "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";




    public static ArrayList<Model_SummaryList> list = new ArrayList<>();

    Integer pageNumber = 0;
    public static final int PERMISSION_CODE = 42042;

    AppCompatEditText st_Subject_et,st_Content_et,st_pEmail_et,st_CcEmail_et;

    Toolbar toolbar;

    String user_id,token,Email;

    public AlertDialog Progress_dialog;

    public static JSONObject json_p = new JSONObject();
    File savedPDFFile;

    String Companylogo,SalesID;

    SharedPreferences InSharedPreferences;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareinvoice);

        Companylogo =getCompanyLogo();

        SQLiteController sqLiteControllerC = new SQLiteController(ActivitySalesShareInvoice.this);
        sqLiteControllerC.open();
        try {
            long fetchInvoiceCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchInvoiceCount > 0) {
                Cursor C_InvoiceFormat = sqLiteControllerC.readTableInvoiceFormat();
                if (C_InvoiceFormat.moveToFirst()) {
                    do {
                        String invoice_name = C_InvoiceFormat.getString(C_InvoiceFormat.getColumnIndex("invoice_name"));
                        if(invoice_name.equals("SalesReturnEmailDownloadTemplate")){
                            APIInvoiceHTML  = C_InvoiceFormat.getString(C_InvoiceFormat.getColumnIndex("invoice_value"));
                            System.out.println("APIInvoiceHTML---"+APIInvoiceHTML);
                        }
                        if(invoice_name.equals("SalesReturnEmailTemplate")){
                            InvoiceEmailTemplate  = C_InvoiceFormat.getString(C_InvoiceFormat.getColumnIndex("invoice_value"));
                            System.out.println("InvoiceEmailTemplate---"+InvoiceEmailTemplate);
                        }
                    } while (C_InvoiceFormat.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();
        }



     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }*/

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySalesShareInvoice.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivitySalesShareInvoice.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SalesID = extras.getString("SalesID");
            System.out.println("SalesID---"+SalesID);

        }

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate1 = sdf.format(date1);


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

        toolbar = findViewById(R.id.toolbar);
        AppCompatTextView tittle_tv_head = findViewById(R.id.tittle_tv_head);
        tittle_tv_head.setText("Email");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesShareInvoice.this, ActivitySalesHistory.class));
            }
        });

        pdfView = findViewById(R.id.pdfView);
        st_pEmail_et = findViewById(R.id.st_pEmail_et);
        AppCompatTextView sale_tv_pr = findViewById(R.id.sale_tv_pr);
        sale_tv_pr.setText("Sales Return Preview");
        st_CcEmail_et = findViewById(R.id.st_CcEmail_et);
        st_Subject_et = findViewById(R.id.st_Subject_et);
        st_Content_et = findViewById(R.id.st_Content_et);


        findViewById(R.id.btn_cancel_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesShareInvoice.this, ActivitySalesHistory.class));
            }
        });

        findViewById(R.id.btn_send_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(ActivitySalesShareInvoice.this);

                if (Connectivity.isConnected(ActivitySalesShareInvoice.this) &&
                        Connectivity.isConnectedFast(ActivitySalesShareInvoice.this)) {

                    if (Utils.getInstance().checkIsEmpty(st_pEmail_et)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Email!");
                    }else if (Utils.getInstance().checkIsEmpty(st_Subject_et)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Subject!");
                    }else if (Utils.getInstance().checkIsEmpty(st_Content_et)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Content!");
                    }else  if (!Utils.getInstance().isEmailValid(st_pEmail_et.getText().toString().trim())) {
                        Utils.getInstance().snackBarMessage(v,"Please enter valid Primary email address");
                    }else {
                        Double price1 =Double.valueOf(Invoicenumber);
                        DecimalFormat format1 = new DecimalFormat("0.#");
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$Customer$", customername);
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$ReturnDate$", date_order_);
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$ReturnDate$", date_order_);
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$ReturnNumber$", format1.format(price1));
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$ReturnNumber$", format1.format(price1));
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$NetAmount$", Activity_OrdersHistory.currency_symbol+" "+totalamount);
                        try {
                            JSONObject json = new JSONObject();
                            json.put("screenid","BFA5523C-3560-47DF-A8DF-1747FCFA1A11");
                            json.put("process",FilenameName);
                            json.put("toemail",st_pEmail_et.getText().toString());
                            json.put("ccemail",st_CcEmail_et.getText().toString());
                            json.put("emailsubject",st_Subject_et.getText().toString());
                            json.put("emailbody",InvoiceEmailTemplate);
                            //json.put("attachmentHTML",APIInvoiceHTML);
                            json.put("attachmentHTML",InvoivePDFPreview);
                            json.put("createdby",user_id);
                            json.put("userid",user_id);
                            json.put("createdon",currentDate1);
                            System.out.println("SharePost--+"+json);
                            postInvoive(v,json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

            }
        });


        GetInvoiceDetails();


    }

    private void GetInvoiceDetails() {
        try {
            App.getInstance().GetSalesInfo(SalesID,token,new Callback(){

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
                            final JSONObject jsData = new JSONObject(res);
                            JSONObject jsonObject = jsData.getJSONObject("data");

                            Invoicenumber = jsonObject.getString("returnnumber");
                            String invoicedate = jsonObject.getString("returneddate");
                            totalamount = jsonObject.getString("totalamount");
                            String totallineitemamount = jsonObject.getString("subtotal");
                            String taxamount = jsonObject.getString("taxamount");
                            String adddeductionamount = jsonObject.getString("adddeductionamount");
                            String discountpercentage = jsonObject.getString("discountpercentage");
                            String discountamount = jsonObject.getString("discountamount");
                            String freightamount = jsonObject.getString("shippingamount");
                            String totaltaxbase = jsonObject.getString("taxpercentage");
                            String totaltax = jsonObject.getString("totalamount");
                            salesrep_name = jsonObject.getString("salesrep");
                            ordernumber = jsonObject.getString("ordernumber");
                            String paymentmethod = jsonObject.getString("paymentmode");
                            customername = jsonObject.getString("customername");
                            String customerid = jsonObject.getString("customerid");
                            String termsandconditions = jsonObject.getString("termsandconditions");


                            GetCustomerSummary(customerid);


                            String billaddress = jsonObject.getString("billingaddress");
                            String shipaddress = jsonObject.getString("shippingaddress");

                            JSONArray jsInvoiceproduct = jsonObject.getJSONArray("salesreturnproduct");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                    Date date = null;
                                    try {
                                        date = sdf.parse(invoicedate);

                                        String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                        Bill_Add=billaddress.replace("<br/>",", ");
                                        Ship_Add=shipaddress.replace("<br/>",", ");

                                        String description ="";


                                        if(list.size()>0){
                                            list.clear();
                                        }
                                        for (int i = 0; i < jsInvoiceproduct.length(); i++) {
                                            JSONObject js = jsInvoiceproduct.getJSONObject(i);
                                           // String product_name_str = js.getString("productname");
                                            String quantity = js.getString("returnedquantity");
                                            String baseamount = js.getString("totalamount");
                                            String priceperunit = js.getString("priceperunit");
                                            String itemistaxable = js.getString("isitemtaxable");
                                            String id = js.getString("id");
                                            String productid = js.getString("productid");

                                            SQLiteController sqLiteController = new SQLiteController(getApplication());
                                            sqLiteController.open();
                                            try {
                                                long count = sqLiteController.fetchOrderCount();
                                                if (count > 0) {

                                                    //product
                                                    Cursor product_c1 = sqLiteController.readAllTableProduct();
                                                    if (product_c1 != null && product_c1.moveToFirst()) {
                                                        String productImage ="null";
                                                        do {
                                                            @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));
                                                            if(product_id.equals(productid)){
                                                                @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                                @SuppressLint("Range") String product_name_str = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));
                                                                @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));

                                                                list.add(new Model_SummaryList(product_name_str, product_name_str, "null",quantity,baseamount,baseamount,priceperunit,itemistaxable,all_upsc,all_description));
                                                            }

                                                        } while (product_c1.moveToNext());
                                                    }

                                                }
                                            } finally {
                                                sqLiteController.close();
                                            }




                                        }


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Double price1 =Double.valueOf(Invoicenumber);
                                                DecimalFormat format1 = new DecimalFormat("0.#");
                                             //   st_Subject_et.setText("Sales Return Of : "+format1.format(price1));
                                                st_Subject_et.setText("Sales Return Of : "+Invoicenumber);


                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                                Date date = null;
                                                try {
                                                    date = sdf.parse(invoicedate);

                                                    date_order_ =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                sMsg = String.valueOf(new StringBuilder().append("Hi ").append(customername)
                                                        .append("\n\n Thanks for your Business ").append(date_order_)
                                                        .append("\n\nThe Sales Return " + format1.format(price1) + " is attached with this email for your reference.")
                                                        .append("\n\n Here's an overview of the Sales Return for your reference")
                                                        .append("\n\n Sales Return Overview")
                                                        .append("\n\n Sales Return : ").append(format1.format(price1))
                                                        .append("\n\n Sales Return Date : ").append(date_order_)
                                                        .append("\n\n Total Amount : ").append(Activity_OrdersHistory.currency_symbol).append(" "+totalamount)
                                                        .append("\n\n It was great working. Looking forward to working with you again.")
                                                );

                                                System.out.println("sMsg------------"+sMsg);

                                               st_Content_et.setText(sMsg);

                                                String Outpur_Str = Utils.convertToIndianCurrency(totalamount);

                                                System.out.println("-----APIInvoiceHTML---"+APIInvoiceHTML);


                                                APIInvoiceHTML = APIInvoiceHTML.replace("$SalesReturnNumber$",Invoicenumber);


                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$ReturnedDate$", date_order_);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$DeliveryDate$", date_order_);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$referencenumber$", ordernumber);

                                                    if(Bill_Add != null){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$BillingAddress$", Bill_Add);
                                                    }

                                                    if(Ship_Add != null){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAddress$", Ship_Add);
                                                    }


                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$totalUnit$", "1");
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$UserSignature$", " ");
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$CompanyLogo$", " ");

                                                String tot = Utils.truncateDecimal(Double.parseDouble(totallineitemamount));

                                                String freightamountrf ="0";
                                                if(!freightamount.equals("null")){
                                                    freightamountrf = Utils.truncateDecimal(Double.parseDouble(freightamount));
                                                }

                                                String totaltaxbasedd = Utils.truncateDecimal(Double.parseDouble(totaltaxbase));
                                                String totaltaxdd = Utils.truncateDecimal(Double.parseDouble(taxamount));
                                                String totalamountww = Utils.truncateDecimal(Double.parseDouble(totalamount));
                                                String adddeductionamount_dd = Utils.truncateDecimal(Double.parseDouble(adddeductionamount));

                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$SubTotal$", Activity_OrdersHistory.currency_symbol+tot);
                                                   // APIInvoiceHTML = APIInvoiceHTML.replace("$DiscountAmount$", Activity_OrdersHistory.currency_symbol+discountamount);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAmount$", Activity_OrdersHistory.currency_symbol+freightamountrf);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$AddtionalDeductionAmount$", Activity_OrdersHistory.currency_symbol+adddeductionamount_dd);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxPercentage$", "("+totaltaxbasedd+" % )");
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxAmount$", Activity_OrdersHistory.currency_symbol+totaltaxdd);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamountww);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TermsandConditions$", termsandconditions);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmountInWords$", Outpur_Str);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$customerNotes$", "Thank you for your business");

                                                    System.out.println("-----OPEN_PDF---");

                                                    try {
                                                        json_p.put("InvoiceOrderNumber"," IN_"+ordernumber);
                                                        json_p.put("InvoiceDate",date_order_);
                                                        json_p.put("presaleNumber"," SO "+ordernumber);
                                                        json_p.put("paymentTerm",paymentmethod);
                                                        json_p.put("BillingAddress",Bill_Add);
                                                        json_p.put("ShippingAddress",Ship_Add);
                                                        json_p.put("SubTotal", Activity_OrdersHistory.currency_symbol+totallineitemamount);
                                                        json_p.put("DiscountAmount", Activity_OrdersHistory.currency_symbol+discountamount);
                                                        json_p.put("ShippingAmount", Activity_OrdersHistory.currency_symbol+freightamount);
                                                        json_p.put("TaxPercentage", "("+totaltaxbase+"%)");
                                                        json_p.put("discountpercentage", "("+discountpercentage+"%)");
                                                        json_p.put("TaxAmount", Activity_OrdersHistory.currency_symbol+totaltax);
                                                        json_p.put("TotalAmount", Activity_OrdersHistory.currency_symbol+totalamount);
                                                        json_p.put("TotalAmountInWords", Outpur_Str);
                                                        json_p.put("paymentTerm", "Cash");
                                                        json_p.put("CustomerName", customername);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }



                                                    CreateInvoicePDF();


                                            }
                                        });


                                    } catch (ParseException | JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


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

    private void postInvoive(final View v, final JSONObject json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.getInstance().loadingDialog(ActivitySalesShareInvoice.this, "Please wait.");
            }
        });
        try {
            App.getInstance().PostEmail(json.toString(),token, new Callback() {
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
                        Utils.getInstance().dismissDialog();
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");

                                    startActivity(new Intent(ActivitySalesShareInvoice.this, ActivitySalesHistory.class));
                                    Toast.makeText(ActivitySalesShareInvoice.this, "Email sent successfully", Toast.LENGTH_SHORT).show();


                                    if ( succeeded == true) {
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

    private void ThermalPrint_B() {
        JSONObject postedJSON = new JSONObject();
        JSONArray array =new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            postedJSON = new JSONObject();
            try {
                postedJSON.put("prodname", list.get(i).getProduct_name());
                postedJSON.put("price", list.get(i).getPrice_PerUnit());
                postedJSON.put("Qty", list.get(i).getProduct_quantity());
                postedJSON.put("amount", list.get(i).getProduct_price());
                if (Activity_OrdersHistory.tax.equals("1")){
                    if(list.get(i).getIstaxable().equals("true")){
                        postedJSON.put("Tax", "Taxable");

                    }else {
                        postedJSON.put("Tax", "Non-Taxable");

                    }
                }else {
                    postedJSON.put("Tax", "-");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(postedJSON);

        }

        try {
            json_p.put("orderitem",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("Print",json_p.toString());
        bundle.putString("Flag","1");
        Intent in = new Intent(ActivitySalesShareInvoice.this, ThermalPrint.class);
        in.putExtras(bundle);
        startActivity(in);

    }


    private void CreateInvoicePDF() {

        String localRowhtml = "";

        for (int i = 0; i < list.size(); i++) {

            localRowhtml += InvoiceRowHtml;

            if(list.get(i).getAll_upsc().equals("null")){
                localRowhtml = localRowhtml.replace("prodname", list.get(i).getProduct_name());

            }else {
                localRowhtml = localRowhtml.replace("prodname", list.get(i).getProduct_name()+"\n- "+list.get(i).getAll_upsc());

            }

            localRowhtml = localRowhtml.replace("Desc", list.get(i).getAll_dec());


            String rty = String.valueOf(new DecimalFormat("#").format(Double.parseDouble(list.get(i).getProduct_quantity())));


            localRowhtml = localRowhtml.replace("Qty", rty);

            String getPrice_PerUnit = Utils.truncateDecimal(Double.parseDouble(list.get(i).getPrice_PerUnit()));
            String getProduct_price = Utils.truncateDecimal(Double.parseDouble(list.get(i).getProduct_price()));
            localRowhtml = localRowhtml.replace("Unit", "$ "+getPrice_PerUnit);

            /*if (Activity_OrdersHistory.tax.equals("1")){
                if(list.get(i).getIstaxable().equals("true")){
                    localRowhtml = localRowhtml.replace("Tax", "Taxable");

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                }
            }else {
                localRowhtml = localRowhtml.replace("Tax", "-");

            }*/


            localRowhtml = localRowhtml.replace("amount", "$ "+getProduct_price);

        }

        APIInvoiceHTML = APIInvoiceHTML.replace("$LineItems$", localRowhtml);

        //APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$", "data:image/png;base64,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");

        APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$", Companylogo);

        FileManager.getInstance().cleanTempFolder(ActivitySalesShareInvoice.this);
        // Create Temp File to save Pdf To
        savedPDFFile = FileManager.getInstance().createTempFile(ActivitySalesShareInvoice.this, "pdf", false);
        // Generate Pdf From Html

        InvoivePDFPreview="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIInvoiceHTML+
                "</body>\n" +
                "</html>";

        System.out.println("-----OPEN NEW---");
        PDFUtil.generatePDFFromHTML(ActivitySalesShareInvoice.this, savedPDFFile, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIInvoiceHTML+
                "</body>\n" +
                "</html>", new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {

                Uri pdfUri = Uri.fromFile(savedPDFFile);

                FilenameName = getFileName(ActivitySalesShareInvoice.this, pdfUri);

                System.out.println("FilenameName---"+FilenameName);
                System.out.println("pdfUri---"+pdfUri);

                pdfView.fromUri(pdfUri)
                        .defaultPage(5)
                        .onPageChange(ActivitySalesShareInvoice.this)
                        .enableAnnotationRendering(true)
                        .onLoad(ActivitySalesShareInvoice.this)
                        .scrollHandle(new DefaultScrollHandle(ActivitySalesShareInvoice.this))
                        .spacing(10)
                        .onPageError(ActivitySalesShareInvoice.this)
                        .load();
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("TAG", "Cannot load page " + page);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("TAG", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }

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

    private void GetCustomerSummary(String Id_) {
        try {
            App.getInstance().GetCustomerSummary(Id_,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

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

                            //JSONObject jsContact= jsdata.getJSONObject("contactInfo");
                            JSONArray jsContact = jsdata.getJSONArray("contactInfo");
                            JSONObject jsContact2 = jsContact.getJSONObject(0);
                            String emailaddress = jsContact2.getString("emailaddress1");
                            String telephone1 = jsContact2.getString("telephone1");
                            String emailaddress2 = jsContact2.getString("emailaddress2");
                            String telephone2 = jsContact2.getString("telephone2");
                            String firstname = jsContact2.getString("firstname");
                            String lastname = jsContact2.getString("lastname");

                            System.out.println("emailaddress--------"+emailaddress);
                            System.out.println("emailaddress2--------"+emailaddress2);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(emailaddress.equals("null")||emailaddress.isEmpty()){
                                        st_pEmail_et.setText("");
                                    }else {
                                        st_pEmail_et.setText(emailaddress);
                                    }

                                    if(emailaddress2.equals("null")||emailaddress2.isEmpty()){
                                        st_CcEmail_et.setText("");
                                    }else {
                                        st_CcEmail_et.setText(emailaddress2);
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

    private String getCompanyLogo() {
        InSharedPreferences = getSharedPreferences(Activity_OrdersHistory.ohPref, MODE_PRIVATE);
        if (InSharedPreferences.contains(Activity_OrdersHistory.CmpLogo)) {
            return InSharedPreferences.getString(Activity_OrdersHistory.CmpLogo, null);
        }
        return " ";
    }

}