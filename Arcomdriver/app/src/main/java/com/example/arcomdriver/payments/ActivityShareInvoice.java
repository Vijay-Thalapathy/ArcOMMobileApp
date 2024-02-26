package com.example.arcomdriver.payments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PDFPrint;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.ActivitySignIn;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.helper.ShareInvoicePdfViewer;
import com.example.arcomdriver.invoice.ActivityInvoiceDetails;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_AddressHistory;
import com.example.arcomdriver.model.Model_CustomerHistory;
import com.example.arcomdriver.model.Model_InvoiceProductItems;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.order.Activity_CreateOrder;
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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class ActivityShareInvoice extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    PDFView pdfView;
    String Bill_Add,date_order_,Ship_Add;
    String FilenameName;

    public String  ShareFlag="1",Invoicenumber,Invoiceid,customername,OrderId,user_email,salesrep_name,ordernumber,totalamount,submitstatus,date_order;
    public String sMsg,APIInvoiceHTML ="null",InvoivePDFPreview;
    public String InvoiceEmailTemplate ="null";

    String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
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

    String Companylogo;

    SharedPreferences InSharedPreferences;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareinvoice);

        Companylogo =getCompanyLogo();

        SQLiteController sqLiteControllerC = new SQLiteController(ActivityShareInvoice.this);
        sqLiteControllerC.open();
        try {
            long fetchInvoiceCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchInvoiceCount > 0) {
                Cursor C_InvoiceFormat = sqLiteControllerC.readTableInvoiceFormat();
                if (C_InvoiceFormat.moveToFirst()) {
                    do {
                        String invoice_name = C_InvoiceFormat.getString(C_InvoiceFormat.getColumnIndex("invoice_name"));
                        if(invoice_name.equals("InvoiceTemplatePreview")){
                            APIInvoiceHTML  = C_InvoiceFormat.getString(C_InvoiceFormat.getColumnIndex("invoice_value"));
                            System.out.println("APIInvoiceHTML---"+APIInvoiceHTML);
                        }
                        if(invoice_name.equals("InvoiceEmailTemplate")){
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

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityShareInvoice.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivityShareInvoice.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Invoiceid = extras.getString("InvoiceID");
            ShareFlag = extras.getString("ShareFlag");
            System.out.println("Invoiceid---"+Invoiceid);

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityShareInvoice.this, Activity_OrdersHistory.class));
            }
        });

        pdfView = findViewById(R.id.pdfView);
        st_pEmail_et = findViewById(R.id.st_pEmail_et);
        st_CcEmail_et = findViewById(R.id.st_CcEmail_et);
        st_Subject_et = findViewById(R.id.st_Subject_et);
        st_Content_et = findViewById(R.id.st_Content_et);


        findViewById(R.id.btn_cancel_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityShareInvoice.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.btn_send_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(ActivityShareInvoice.this);

                if (Connectivity.isConnected(ActivityShareInvoice.this) &&
                        Connectivity.isConnectedFast(ActivityShareInvoice.this)) {

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
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$InvoiceNumber$", "IN_"+format1.format(price1));
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$InvoiceNumber$", "IN_"+format1.format(price1));
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$InvoiceDate$", date_order_);
                        InvoiceEmailTemplate = InvoiceEmailTemplate.replace("$NetAmount$", Activity_OrdersHistory.currency_symbol+" "+totalamount);
                        try {
                            JSONObject json = new JSONObject();
                            json.put("screenid",null);
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


      /*  SQLiteController sqLiteController = new SQLiteController(ActivityShareInvoice.this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                //Order
                Cursor invoice_c = sqLiteController.readTableItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,Invoiceid);
                if (invoice_c != null && invoice_c.moveToFirst()) {

                    do {
                        ordernumber = invoice_c.getString(invoice_c.getColumnIndex("ordernumber"));
                        @SuppressLint("Range") String invoicedate = invoice_c.getString(invoice_c.getColumnIndex("invoicedate"));
                        totalamount = invoice_c.getString(invoice_c.getColumnIndex("totalamount"));
                        @SuppressLint("Range") String pricingdate = invoice_c.getString(invoice_c.getColumnIndex("pricingdate"));
                        @SuppressLint("Range") String customerid = invoice_c.getString(invoice_c.getColumnIndex("customerid"));
                        OrderId = invoice_c.getString(invoice_c.getColumnIndex("orderid"));
                        @SuppressLint("Range") String shiptoaddressid = invoice_c.getString(invoice_c.getColumnIndex("shiptoaddressid"));
                        @SuppressLint("Range") String billtoaddressid = invoice_c.getString(invoice_c.getColumnIndex("billtoaddressid"));
                        @SuppressLint("Range") String totallineitemamount = invoice_c.getString(invoice_c.getColumnIndex("totallineitemamount"));
                        @SuppressLint("Range") String discountpercentage = invoice_c.getString(invoice_c.getColumnIndex("discountpercentage"));
                        @SuppressLint("Range") String totaltaxbase = invoice_c.getString(invoice_c.getColumnIndex("totaltaxbase"));
                        @SuppressLint("Range") String discountamount = invoice_c.getString(invoice_c.getColumnIndex("discountamount"));
                        @SuppressLint("Range") String freightamount = invoice_c.getString(invoice_c.getColumnIndex("freightamount"));
                        @SuppressLint("Range") String totaltax = invoice_c.getString(invoice_c.getColumnIndex("totaltax"));
                        @SuppressLint("Range") String deliverynote = invoice_c.getString(invoice_c.getColumnIndex("deliverynote"));
                        @SuppressLint("Range") String shipnote = invoice_c.getString(invoice_c.getColumnIndex("shipnote"));
                        @SuppressLint("Range") String termsconditions = invoice_c.getString(invoice_c.getColumnIndex("termsconditions"));
                        @SuppressLint("Range") String memo = invoice_c.getString(invoice_c.getColumnIndex("memo"));
                        @SuppressLint("Range") String paymentmethod = invoice_c.getString(invoice_c.getColumnIndex("paymentmethod"));
                        @SuppressLint("Range") String referenceorder = invoice_c.getString(invoice_c.getColumnIndex("referenceorder"));
                        @SuppressLint("Range") String salesrepid_new = invoice_c.getString(invoice_c.getColumnIndex("salesrepid"));


                        GetCustomerSummary(customerid);

                        System.out.println("ordernumber---"+ordernumber);
                        System.out.println("customerid---"+customerid);

                        SQLiteController sqLiteController2 = new SQLiteController(ActivityShareInvoice.this);
                        sqLiteController2.open();
                        try {
                            long count1 = sqLiteController.fetchCount();
                            if (count1 > 0) {
                                //Order

                                Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER, DbHandler.ORDER_ID, OrderId);
                                if (order_c != null && order_c.moveToFirst()) {
                                    do {
                                        orderNo = order_c.getString(order_c.getColumnIndex("ordernumber"));

                                    } while (order_c.moveToNext());
                                }

                                Cursor customer_c = sqLiteController.readTableItem(DbHandler.TABLE_CUSTOMER, DbHandler.CUSTOMER_ID, customerid);
                                if (customer_c != null && customer_c.moveToFirst()) {
                                    do {
                                        String Id = customer_c.getString(customer_c.getColumnIndex("Id"));
                                        String businessname = customer_c.getString(customer_c.getColumnIndex("businessname"));
                                        String customername_s = customer_c.getString(customer_c.getColumnIndex("customername"));

                                        if(customername_s.equals("null")){
                                            customername = businessname;

                                        }else{
                                            customername = customername_s;
                                        }

                                        System.out.println("customername---"+customername);

                                    } while (customer_c.moveToNext());
                                }

                                Cursor sales_c = sqLiteController.readTableItem(DbHandler.TABLE_SALESREP, DbHandler.SALESREP_ID, salesrepid_new);
                                if (sales_c != null && sales_c.moveToFirst()) {
                                    do {
                                        String salesrep_id = sales_c.getString(sales_c.getColumnIndex("salesrep_id"));
                                        salesrep_name = sales_c.getString(sales_c.getColumnIndex("salesrep_name"));


                                    } while (sales_c.moveToNext());
                                }
                            }
                        } finally {
                            sqLiteController.close();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Double price1 =Double.valueOf(ordernumber);
                                DecimalFormat format1 = new DecimalFormat("0.#");
                                st_Subject_et.setText("Invoice details for Order ID: "+"IN_"+format1.format(price1));


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                Date date = null;
                                try {
                                    date = sdf.parse(invoicedate);

                                    date_order_ =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                sMsg = String.valueOf(new StringBuilder().append("Hi ").append(customername)
                                        .append("\n\nThe Invoice " + " IN_" + format1.format(price1) + " is attached with this email for your reference. You can choose the way out and pay online.")
                                        .append("\n\n Invoice Overview")
                                        .append("\n\n Invoice Number : ").append(" IN_" + format1.format(price1))
                                        .append("\n\n Invoice Date : ").append(date_order_)
                                        .append("\n\n Total Amount : ").append(Activity_OrdersHistory.currency_symbol).append(" "+totalamount)
                                        .append("\n\n It was great working. Looking forward to working with you again.")
                                );

                                System.out.println("sMsg------------"+sMsg);

                                st_Content_et.setText(sMsg);

                                SQLiteController sqLiteControllerC = new SQLiteController(ActivityShareInvoice.this);
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

                                                if(billtoaddressid.equals(BId)){

                                                    Bill_Add =line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country;


                                                }

                                                if(shiptoaddressid.equals(BId)){

                                                    Ship_Add =line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country;


                                                }

                                                System.out.println("Ship_Add---"+Ship_Add);

                                            } while (C_Address.moveToNext());
                                        }
                                    }
                                } finally {
                                    sqLiteControllerC.close();
                                }

                                if(list.size()>0){
                                    list.clear();
                                }
                                SQLiteController sqLiteController = new SQLiteController(ActivityShareInvoice.this);
                                sqLiteController.open();
                                try {
                                    long count = sqLiteController.fetchOrderCount();
                                    if (count > 0) {
                                        System.out.println("Invoiceid2222---"+Invoiceid);
                                        Cursor Product_c = sqLiteController.readInvoiceProductJoinTables(Invoiceid);
                                        if (Product_c != null && Product_c.moveToFirst()) {
                                            if(list.size()>0){
                                                list.clear();
                                            }
                                            do {
                                                @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                                                @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                                                @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                                                @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                                                @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                                                @SuppressLint("Range") String itemistaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));

                                                //product
                                                Cursor product_c1 = sqLiteController.readAllTableProduct();
                                                if (product_c1 != null && product_c1.moveToFirst()) {
                                                    do {

                                                        @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                        if(product_id.equals(productid)){
                                                            @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                            @SuppressLint("Range") String product_name_str = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                            @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));
                                                            @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                                            System.out.println("List---"+product_imageurl);
                                                            list.add(new Model_SummaryList(product_name_str, product_name_str, product_imageurl,quantity,baseamount,baseamount,priceperunit,itemistaxable,all_upsc,all_description));
                                                        }

                                                    } while (product_c1.moveToNext());
                                                }

                                            } while (Product_c.moveToNext());
                                        }


                                    }
                                } finally {
                                    sqLiteController.close();
                                }
                                String Outpur_Str = Utils.convertToIndianCurrency(totalamount);

                                System.out.println("-----APIInvoiceHTML---"+APIInvoiceHTML);

                                if(!APIInvoiceHTML.equals("null")){
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$COMPANYADDRESS$", " ");
                                    if(ordernumber.startsWith("DRFIN")){
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$",ordernumber);
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                    }else {
                                        Double price =Double.valueOf(ordernumber);
                                        DecimalFormat format = new DecimalFormat("0.#");

                                        if(!ordernumber.equals("null")){
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", "IN_"+format.format(price));
                                        }else {
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", "IN_"+"-");
                                        }


                                        if(!orderNo.equals("null")){
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "SO "+orderNo);
                                        }else {
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                        }


                                    }

                                    APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceDate$", date_order_);

                                    if(!paymentmethod.equals("null")){
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", paymentmethod);
                                    }else {
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", "-");
                                    }

                                    APIInvoiceHTML = APIInvoiceHTML.replace("$salesPerson$", salesrep_name);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$referencePo$", "0");
                                    if(Bill_Add != null){
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$BillingAddress$", Bill_Add);
                                    }

                                    if(Ship_Add != null){
                                        APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAddress$", Ship_Add);
                                    }


                                    APIInvoiceHTML = APIInvoiceHTML.replace("$totalUnit$", "1");

                                    APIInvoiceHTML = APIInvoiceHTML.replace("$SubTotal$", Activity_OrdersHistory.currency_symbol+totallineitemamount);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$DiscountAmount$", Activity_OrdersHistory.currency_symbol+discountamount);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAmount$", Activity_OrdersHistory.currency_symbol+freightamount);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxPercentage$", "("+totaltaxbase+" % )");
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxAmount$", Activity_OrdersHistory.currency_symbol+totaltax);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamount);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TermsandConditions$", termsconditions);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmountInWords$", Outpur_Str);
                                    APIInvoiceHTML = APIInvoiceHTML.replace("$customerNotes$", "Thank you for your business");

                                    System.out.println("-----OPEN_PDF---");

                                    CreateInvoicePDF();


                                }


                            }
                        });

                    } while (invoice_c.moveToNext());
                }

            }
        } finally {
            sqLiteController.close();

        }*/


        GetInvoiceDetails();


    }

    private void GetInvoiceDetails() {
        try {
            App.getInstance().GetInvoiceSummary(Invoiceid,token,new Callback(){

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

                            Invoicenumber = jsonObject.getString("ordernumber");
                            OrderId = jsonObject.getString("orderid");
                            String invoicedate = jsonObject.getString("invoicedate");
                            totalamount = jsonObject.getString("totalamount");
                            String pricingdate = jsonObject.getString("pricingdate");
                            String totallineitemamount = jsonObject.getString("totallineitemamount");
                            String discountpercentage = jsonObject.getString("discountpercentage");
                            String discountamount = jsonObject.getString("discountamount");
                            String freightamount = jsonObject.getString("freightamount");
                            String totaltaxbase = jsonObject.getString("totaltaxbase");
                            String totaltax = jsonObject.getString("totaltax");
                            String paymenttype = jsonObject.getString("paymenttype");
                            salesrep_name = jsonObject.getString("salesrep");
                            ordernumber = jsonObject.getString("presaleordernumber");
                            String termsconditions = jsonObject.getString("termsconditions");
                            String deliverynote = jsonObject.getString("deliverynote");
                            String shipnote = jsonObject.getString("shipnote");
                            String memo = jsonObject.getString("memo");
                            String paymentmethod = jsonObject.getString("paymenttype");
                            String referenceorder = jsonObject.getString("referenceorder");

                            JSONObject jsCus = jsonObject.getJSONObject("customers");
                            customername = jsCus.getString("customername");
                            String customerid = jsCus.getString("customerid");


                            GetCustomerSummary(customerid);


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

                                        Bill_Add=billaddress.replace("<br/>",", ");
                                        Ship_Add=shipaddress.replace("<br/>",", ");

                                        String description ="";


                                        if(list.size()>0){
                                            list.clear();
                                        }
                                        for (int i = 0; i < jsInvoiceproduct.length(); i++) {
                                            JSONObject js = jsInvoiceproduct.getJSONObject(i);
                                            String product_name_str = js.getString("productname");
                                            String quantity = js.getString("quantity");
                                            String baseamount = js.getString("baseamount");
                                            String priceperunit = js.getString("priceperunit");
                                            String itemistaxable = js.getString("itemistaxable");
                                            String upccode = js.getString("upccode");
                                            String productid = js.getString("productid");
                                            String all_description = js.getString("description");
                                            String all_upsc = js.getString("upccode");

                                            list.add(new Model_SummaryList(product_name_str, product_name_str, "null",quantity,baseamount,baseamount,priceperunit,itemistaxable,all_upsc,all_description));

                                        }


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Double price1 =Double.valueOf(Invoicenumber);
                                                DecimalFormat format1 = new DecimalFormat("0.#");
                                                st_Subject_et.setText("Invoice details for Order ID: "+"IN_"+format1.format(price1));


                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                                Date date = null;
                                                try {
                                                    date = sdf.parse(invoicedate);

                                                    date_order_ =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                sMsg = String.valueOf(new StringBuilder().append("Hi ").append(customername)
                                                        .append("\n\nThe Invoice " + " IN_" + format1.format(price1) + " is attached with this email for your reference. You can choose the way out and pay online.")
                                                        .append("\n\n Invoice Overview")
                                                        .append("\n\n Invoice Number : ").append(" IN_" + format1.format(price1))
                                                        .append("\n\n Invoice Date : ").append(date_order_)
                                                        .append("\n\n Total Amount : ").append(Activity_OrdersHistory.currency_symbol).append(" "+totalamount)
                                                        .append("\n\n It was great working. Looking forward to working with you again.")
                                                );

                                                System.out.println("sMsg------------"+sMsg);

                                                st_Content_et.setText(sMsg);

                                                String Outpur_Str = Utils.convertToIndianCurrency(totalamount);

                                                System.out.println("-----APIInvoiceHTML---"+APIInvoiceHTML);

                                                if(!APIInvoiceHTML.equals("null")){
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$COMPANYADDRESS$", " ");
                                                    if(Invoicenumber.startsWith("DRFIN")){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$",Invoicenumber);
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                                    }else {
                                                        Double price =Double.valueOf(Invoicenumber);
                                                        DecimalFormat format = new DecimalFormat("0.#");

                                                        if(!Invoicenumber.equals("null")){
                                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", "IN_"+format.format(price));
                                                        }else {
                                                            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", "IN_"+"-");
                                                        }


                                                        if(!ordernumber.equals("null")){
                                                            APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "SO "+ordernumber);
                                                        }else {
                                                            APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                                        }


                                                    }

                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceDate$", date_order_);

                                                    if(!paymentmethod.equals("null")){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", paymentmethod);
                                                    }else {
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", "-");
                                                    }

                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$salesPerson$", salesrep_name);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$referencePo$", "0");
                                                    if(Bill_Add != null){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$BillingAddress$", Bill_Add);
                                                    }

                                                    if(Ship_Add != null){
                                                        APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAddress$", Ship_Add);
                                                    }


                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$totalUnit$", "1");

                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$SubTotal$", Activity_OrdersHistory.currency_symbol+totallineitemamount);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$DiscountAmount$", Activity_OrdersHistory.currency_symbol+discountamount);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAmount$", Activity_OrdersHistory.currency_symbol+freightamount);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxPercentage$", "("+totaltaxbase+" % )");
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TaxAmount$", Activity_OrdersHistory.currency_symbol+totaltax);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamount);
                                                    APIInvoiceHTML = APIInvoiceHTML.replace("$TermsandConditions$", termsconditions);
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
                Utils.getInstance().loadingDialog(ActivityShareInvoice.this, "Please wait.");
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

                                    if(ShareFlag.equals("1")){

                                        startActivity(new Intent(ActivityShareInvoice.this, Activity_OrdersHistory.class));

                                    }else if(ShareFlag.equals("2")){

                                        System.out.println("json_p---+json_p");

                                        // Open Pdf Viewer
                                        Uri pdfUri = Uri.fromFile(savedPDFFile);

                                        Bundle bundle = new Bundle();
                                        bundle.putString("json_p",json_p.toString());
                                        Intent intentPdfViewer = new Intent(ActivityShareInvoice.this, ShareInvoicePdfViewer.class);
                                        intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);
                                        intentPdfViewer.putExtras(bundle);
                                        startActivity(intentPdfViewer);

                                       // ThermalPrint_B();
                                    }

                                    if ( succeeded == true) {
                                        Toast.makeText(ActivityShareInvoice.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
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
        Intent in = new Intent(ActivityShareInvoice.this, ThermalPrint.class);
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

            localRowhtml = localRowhtml.replace("UPC Code", list.get(i).getAll_dec());
            localRowhtml = localRowhtml.replace("price", list.get(i).getProduct_price());
            localRowhtml = localRowhtml.replace("Qty", list.get(i).getProduct_quantity());
            if (Activity_OrdersHistory.tax.equals("1")){
                if(list.get(i).getIstaxable().equals("true")){
                    localRowhtml = localRowhtml.replace("Tax", "Taxable");

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                }
            }else {
                localRowhtml = localRowhtml.replace("Tax", "-");

            }

            localRowhtml = localRowhtml.replace("amount", list.get(i).getProduct_price());


        }

        // localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

        APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceProduct$", localRowhtml);

        APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$", Companylogo);


        FileManager.getInstance().cleanTempFolder(ActivityShareInvoice.this);
        // Create Temp File to save Pdf To
        savedPDFFile = FileManager.getInstance().createTempFile(ActivityShareInvoice.this, "pdf", false);
        // Generate Pdf From Html

        InvoivePDFPreview="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIInvoiceHTML+
                "</body>\n" +
                "</html>";

        System.out.println("-----OPEN NEW---");
        PDFUtil.generatePDFFromHTML(ActivityShareInvoice.this, savedPDFFile, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIInvoiceHTML+
                "</body>\n" +
                "</html>", new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {

                Uri pdfUri = Uri.fromFile(savedPDFFile);

                FilenameName = getFileName(ActivityShareInvoice.this, pdfUri);

                System.out.println("FilenameName---"+FilenameName);
                System.out.println("pdfUri---"+pdfUri);

                pdfView.fromUri(pdfUri)
                        .defaultPage(5)
                        .onPageChange(ActivityShareInvoice.this)
                        .enableAnnotationRendering(true)
                        .onLoad(ActivityShareInvoice.this)
                        .scrollHandle(new DefaultScrollHandle(ActivityShareInvoice.this))
                        .spacing(10)
                        .onPageError(ActivityShareInvoice.this)
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