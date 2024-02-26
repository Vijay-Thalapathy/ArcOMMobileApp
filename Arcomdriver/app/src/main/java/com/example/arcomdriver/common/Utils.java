package com.example.arcomdriver.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 30 Dec 2022*/

public class Utils {
    public static Utils instance;
    public AlertDialog Progress_dialog;

    public String token;
    public String OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync;


    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();
        return instance;
    }


    //TODO EditText EMPTY
    public boolean checkIsEmpty(EditText inputEditText) {
        return inputEditText.getText().toString().trim().equals("");
    }

    //TODO EditText EMAIL
    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void snackBarMessage(View view, String s) {
        if (view == null) return;
        final Snackbar snackbar = Snackbar.make(view, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //TODO hideKeyboard
    public void hideKeyboard(Activity ac) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) ac.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (ac.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(ac.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //TODO truncateDecimal
   /* public static String truncateDecimal(double x) {
        if (x > 0) {
            if (x > 100) {
                return String.valueOf(new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_CEILING));
            } else {
                return String.valueOf(new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_CEILING));
            }
        }
        return "0.00";
    }*/

    public static String truncateDecimal(double x) {
        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(Locale.US);
        format.setCurrency(usd);

        String s1=format.format(x);
        String replaceString=s1.replace('$',' ');
        return replaceString;
    }
    public static String CurrencyConvert(double x) {
        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(Locale.US);
        format.setCurrency(usd);

        String s1=format.format(x);
        String replaceString=s1.replace('$',' ');
        return replaceString;
    }

    public static boolean checkDates(Date d1, Date d2) {
        //System.out.println("Date "+d1+"\n"+d2);
        //SimpleDateFormat dfDate  = new SimpleDateFormat("d/mm/yyyy");
        //boolean b = false;
        //try {
        if(d2.before (d1))
        {
            return true;//If start date is before end date
        }
        else if(d1.after(d2))
        {
            return false;//If two dates are equal
        }
        else if(d1.equals(d2))
        {
            return false;//If two dates are equal //true
        }
        else
        {
            return false; //If start date is after the end date
        }
        //} catch (ParseException e) {
        // TODO Auto-generated catch block
        //e.printStackTrace();
        //}

    }


    public static String AmtFormat(double x) {
        Locale  locale = new Locale("en", "US");
        Currency currency = Currency.getInstance(locale);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        return  String.valueOf(numberFormat.format(x));
    }
    public static String convertToIndianCurrency(String num) {

        BigDecimal bd = new BigDecimal(num);
        long number = bd.longValue();
        long no = bd.longValue();
        int decimal = (int) (bd.remainder(BigDecimal.ONE).doubleValue() * 100);
        int digits_length = String.valueOf(no).length();
        int i = 0;
        String finalResult = "";

        ArrayList<String> str = new ArrayList<>();
        HashMap<Integer, String> words = new HashMap<>();
        words.put(0, "");
        words.put(1, "One");
        words.put(2, "Two");
        words.put(3, "Three");
        words.put(4, "Four");
        words.put(5, "Five");
        words.put(6, "Six");
        words.put(7, "Seven");
        words.put(8, "Eight");
        words.put(9, "Nine");
        words.put(10, "Ten");
        words.put(11, "Eleven");
        words.put(12, "Twelve");
        words.put(13, "Thirteen");
        words.put(14, "Fourteen");
        words.put(15, "Fifteen");
        words.put(16, "Sixteen");
        words.put(17, "Seventeen");
        words.put(18, "Eighteen");
        words.put(19, "Nineteen");
        words.put(20, "Twenty");
        words.put(30, "Thirty");
        words.put(40, "Forty");
        words.put(50, "Fifty");
        words.put(60, "Sixty");
        words.put(70, "Seventy");
        words.put(80, "Eighty");
        words.put(90, "Ninety");

        String digits[] = {"", "Hundred", "Thousand", "Lakh", "Crore","Arab","Kharab","Nil","Padma","Shankh"};

        while (i < digits_length) {
            int divider = (i == 2) ? 10 : 100;
            number = no % divider;
            no = no / divider;
            i += divider == 10 ? 1 : 2;
            if (number > 0) {
                int counter = str.size();
                String plural = (counter > 0 && number > 9) ? "s" : "";
                String tmp = (number < 21) ? words.get(Integer.valueOf((int) number)) + " " +
                        digits[counter] + plural :
                        words.get(Integer.valueOf((int) Math.floor(number / 10) * 10))
                                + " " + words.get(Integer.valueOf((int) (number % 10)))
                                + " " + digits[counter] + plural;
                str.add(tmp);
            } else {
                str.add("");
            }
        }

        Collections.reverse(str);
        String Rupees = null;

        Rupees = TextUtils.join(" ", str).trim();


        /*String paise = (decimal) > 0 ? " And " + words.get(
                Integer.valueOf((int) (decimal - decimal % 10))) + " " +
                words.get(Integer.valueOf((int) (decimal % 10))) : "";*/
        String paise = (decimal) > 0 ? " Point " + words.get(
                Integer.valueOf((int) (decimal - decimal % 10))) + " " +
                words.get(Integer.valueOf((int) (decimal % 10))) : "";
        // AND FORTNY NINE PAISA
        if (!paise.isEmpty()) {

            //paise = paise.concat(" Paise");
            paise = paise.concat(" ");
        }

        //finalResult = "Rupees " + Rupees + paise + " Only";
        finalResult = " " + Rupees + paise + " Only.";
        return finalResult.replace("  " ," ")
                .replace("   "," ");
    }

    //TODO loadingDialog
    public void loadingDialog(final Activity ac, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ac, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ac.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

    }

    //TODO dismissDialog
    public void dismissDialog() {
        if (Progress_dialog != null) {
            if (Progress_dialog.isShowing()) {
                Progress_dialog.dismiss();
            }
        }
    }

    @SuppressLint("Range")
/*    public void GetMasterInsert(Activity ac, String flag, AlertDialog pr) {

        SQLiteController sqLiteController1 = new SQLiteController(ac);
        sqLiteController1.open();
        try {
            long count = sqLiteController1.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController1.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    @SuppressLint("Range") String user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    System.out.println("user_id---"+user_id);
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                }

                //ReadMasterSync
                Cursor Csync = sqLiteController1.readTableMasterSync();
                if (Csync.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String master_sync_name = Csync.getString(Csync.getColumnIndex("master_sync_name"));
                        @SuppressLint("Range") String master_sync_time = Csync.getString(Csync.getColumnIndex("master_sync_time"));

                        System.out.println("master_sync_name----"+master_sync_name);
                        System.out.println("master_sync_time----"+master_sync_time);

                        if(master_sync_name.equals("Orders")){
                            OrderLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Orders_products")){
                            OrderProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Shipments")){
                            ShipmentLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Addresses")){
                            AddressLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Invoices")){
                            InvoiceLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Invoices_products")){
                            InvoiceProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Customers")){
                            CustomerLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Products")){
                            ProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }else if(master_sync_name.equals("Products_assets")){
                            ProductAssertLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                        }
                    } while (Csync.moveToNext());
                }

            }
        } finally {
            sqLiteController1.close();
        }


        try {
            App.getInstance().GetCreateOrderLogin(OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync,token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println("API_Response---"+ jsonObject.toString());
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");

                            for (int i = 0; i < Products_assets.length(); i++) {
                                JSONObject js = Products_assets.getJSONObject(i);
                                String id = js.getString("id");
                                String assetname = js.getString("assetname");
                                String productid = js.getString("productid");
                                String imageurl = js.getString("imageurl");
                                String filepath = js.getString("filepath");
                                String assettype = js.getString("assettype");
                                String isdefault = js.getString("isdefault");
                                String isactive = js.getString("isactive");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorAssertTable = sqLiteController.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_ID,id);
                                            if (CursorAssertTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Products_assets_id = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_id"));
                                                    sqLiteController.deleteProductAssertIDItems(Products_assets_id);
                                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                                } while (CursorAssertTable.moveToNext());
                                            }
                                            if(CursorAssertTable.getCount() == 0){
                                                sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < Products.length(); i++) {
                                JSONObject js = Products.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("name");
                                String description = js.getString("description");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");

                                String finalPrice = price;
                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String all_product_id = CAllOrders.getString(CAllOrders.getColumnIndex("all_product_id"));

                                                        sqLiteController.deleteAllProductIDItems(all_product_id);

                                                        sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
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
                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchCustomerCount = sqLiteController.fetchCustomerCount();
                                            if (fetchCustomerCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_CUSTOMER,DbHandler.CUSTOMER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String Id = CAllOrders.getString(CAllOrders.getColumnIndex("Id"));

                                                        sqLiteController.deleteCustomerItems(Id);

                                                        sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                                billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertCustomer(id, finalCustomername, customertypeid, status, "", "", "", "", "", "", "",
                                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject js = invoice.getJSONObject(i);

                                String orderid = js.getString("orderid");
                                String externalid = js.getString("externalid");
                                String externalstatus = js.getString("externalstatus");
                                String paymenttypeid = js.getString("paymenttypeid");
                                String paymentid = js.getString("paymentid");
                                String paymentmethod = js.getString("paymentmethod");
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String vendorid = js.getString("vendorid");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String ispricelocked = js.getString("ispricelocked");
                                String ordernumber = js.getString("ordernumber");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String invoicedate = js.getString("invoicedate");
                                String submitstatus = js.getString("submitstatus");
                                String submitstatusdescription = js.getString("submitstatusdescription");
                                String totalamount = js.getString("totalamount");
                                String totalamountbase = js.getString("totalamountbase");
                                String totalamountlessfreight = js.getString("totalamountlessfreight");
                                String totalamountlessfreightbase = js.getString("totalamountlessfreightbase");
                                String totaldiscountamount = js.getString("totaldiscountamount");
                                String totaldiscountamountbase = js.getString("totaldiscountamountbase");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totallineitemamountbase = js.getString("totallineitemamountbase");
                                String totallineitemdiscountamount = js.getString("totallineitemdiscountamount");
                                String totallineitemdiscountamountbase = js.getString("totallineitemdiscountamountbase");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String willcall = js.getString("willcall");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String referenceorder = js.getString("referenceorder");
                                String createdon = js.getString("createdon");
                                String createdby = js.getString("createdby");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String warehouseid = js.getString("warehouseid");
                                String customeristaxable = js.getString("customeristaxable");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");


                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String id = CAllOrders.getString(CAllOrders.getColumnIndex("id"));

                                                        sqLiteController.deleteInvoice(id);

                                                        if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                            sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                        } else {
                                                            sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                        }

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                    } else {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                    }
                                                }

                                                sqLiteController.deleteInvoiceProductsItems(id);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < invoiceproduct.length(); i++) {
                                JSONObject js_P = invoiceproduct.getJSONObject(i);
                                String invoiceid_IP = js_P.getString("invoiceid");
                                String transactioncurrencyid_IP = js_P.getString("transactioncurrencyid");
                                String uomid = js_P.getString("uomid");
                                String baseamount = js_P.getString("baseamount");
                                String exchangerate_Ip = js_P.getString("exchangerate");
                                String baseamountbase = js_P.getString("baseamountbase");
                                String description_Ip = js_P.getString("description");
                                String extendedamount = js_P.getString("extendedamount");
                                String extendedamountbase = js_P.getString("extendedamountbase");
                                String iscopied = js_P.getString("iscopied");
                                String ispriceoverridden = js_P.getString("ispriceoverridden");
                                String isproductoverridden = js_P.getString("isproductoverridden");
                                String lineitemnumber = js_P.getString("lineitemnumber");
                                String manualdiscountamount = js_P.getString("manualdiscountamount");
                                String manualdiscountamountbase = js_P.getString("manualdiscountamountbase");
                                String priceperunit = js_P.getString("priceperunit");
                                String priceperunitbase = js_P.getString("priceperunitbase");
                                String productdescription = js_P.getString("productdescription");
                                String productname = js_P.getString("productname");
                                String productid = js_P.getString("productid");
                                String quantity = js_P.getString("quantity");
                                String quantitybackordered = js_P.getString("quantitybackordered");
                                String quantitycancelled = js_P.getString("quantitycancelled");
                                String quantityshipped = js_P.getString("quantityshipped");
                                String quantitypicked = js_P.getString("quantitypicked");
                                String quantitypacked = js_P.getString("quantitypacked");
                                String requestdeliverybyip = js_P.getString("requestdeliveryby");
                                String salesorderispricelocked = js_P.getString("salesorderispricelocked");
                                String shiptoaddressidip = js_P.getString("shiptoaddressid");
                                String tax = js_P.getString("tax");
                                String taxbase = js_P.getString("taxbase");
                                String willcallip = js_P.getString("willcall");
                                String createdonip = js_P.getString("createdon");
                                String createdbyip = js_P.getString("createdby");
                                String modifiedbyip = js_P.getString("modifiedby");
                                String modifiedbehalfofip = js_P.getString("modifiedbehalfof");
                                String modifiedonip = js_P.getString("modifiedon");
                                String orderpid = js_P.getString("orderpid");
                                String itemistaxable = js_P.getString("itemistaxable");
                                String idip = js_P.getString("id");
                                String isactiveip = js_P.getString("isactive");
                                String domainEvents = js_P.getString("domainEvents");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE_PRODUCTS,DbHandler.IN_PRODUCTS_ID,idip);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String id = CAllOrders.getString(CAllOrders.getColumnIndex("id"));

                                                        sqLiteController.deleteInvoiceProductsALL(id);

                                                        sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }

                            for (int i = 0; i < shipments.length(); i++) {
                                JSONObject js = shipments.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String shipmenttypeid = js.getString("shipmenttypeid");
                                String warehouseid = js.getString("warehouseid");
                                String fulfillmentstatus = js.getString("fulfillmentstatus");
                                String routenum = js.getString("routenum");
                                String routedate = js.getString("routedate");
                                String truck = js.getString("truck");
                                String driverid = js.getString( "driverid");
                                String carrierid = js.getString("carrierid");
                                String shippeddate = js.getString("shippeddate");
                                String trackingnumber = js.getString( "trackingnumber");
                                String deliverydate = js.getString( "deliverydate");
                                String deliverytime = js.getString( "deliverytime");
                                String pickerid = js.getString("pickerid");
                                String packerid = js.getString( "packerid");
                                String shipperid = js.getString("shipperid");
                                String invoicerid = js.getString( "invoicerid");
                                String paymenterid = js.getString("paymenterid");
                                String uuid = js.getString( "uuid");
                                String isdisabled = js.getString( "isdisabled");
                                String disabledby = js.getString( "disabledby");
                                String disabledbehalfof = js.getString("disabledbehalfof");
                                String isdeleted = js.getString("isdeleted");
                                String deletedby = js.getString( "deletedby");
                                String deletedbehalfof = js.getString( "deletedbehalfof");
                                String deletedon = js.getString("deletedon");
                                String createdby = js.getString("createdby");
                                String createdbehalfof = js.getString("createdbehalfof");
                                String creratedon = js.getString("creratedon");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String datafromtypeid = js.getString("datafromtypeid");
                                String datafromid = js.getString("datafromid");
                                String tenantid = js.getString("tenantid");
                                String deliveryagentid = js.getString("deliveryagentid");
                                String deliveryshipmenttypeid = js.getString("deliveryshipmenttypeid");
                                String routedayid = js.getString("routedayid");
                                String routeid = js.getString("routeid");
                                String truckid = js.getString("truckid");
                                String invoiceid = js.getString("invoiceid");
                                String workorderid = js.getString("workorderid");
                                String purchaseorderid = js.getString("purchaseorderid");
                                String undeliveredreasonid = js.getString("undeliveredreasonid");
                                String undeliveredreason = js.getString("undeliveredreason");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String shipment_idd = CAllOrders.getString(CAllOrders.getColumnIndex("shipment_id"));
                                                        //@SuppressLint("Range") String ordernumberS = CAllOrders.getString(CAllOrders.getColumnIndex("ordernumber"));

                                                        sqLiteController.deleteShipItems(shipment_idd);

                                                        //Products Insert Method
                                                        sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    //Products Insert Method
                                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

                                                }
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

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ADDRESS,DbHandler.ADDRESS_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String address_id = CAllOrders.getString(CAllOrders.getColumnIndex("address_id"));

                                                        sqLiteController.deleteAddressItems(address_id);

                                                        sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < Orders.length(); i++) {
                                JSONObject js = Orders.getJSONObject(i);
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String customeridtype = js.getString("customeridtype");
                                String datefulfilled = js.getString("datefulfilled");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String poreferencenum = js.getString("poreferencenum");
                                String ispricelocked = js.getString("ispricelocked");
                                String quoteid = js.getString("quoteid");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String submitdate = js.getString("submitdate");
                                String submitstatus = js.getString("submitstatus");
                                String totalamount = js.getString("totalamount");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String createdby = js.getString("createdby");
                                String creratedon = js.getString("creratedon");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String cancellationreason = js.getString("cancellationreason");
                                String comments = js.getString("comments");
                                String lastbackofficesubmit = js.getString("lastbackofficesubmit");
                                String opportunityid = js.getString("opportunityid");
                                String ordernumber = js.getString("ordernumber");
                                String cancellationDate = js.getString("cancellationDate");
                                String confirmedby = js.getString("confirmedby");
                                String confirmeddate = js.getString("confirmeddate");
                                String notes = js.getString("notes");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String warehouseid = js.getString("warehouseid");
                                String modifiedon = js.getString("modifiedon");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");
                                String customeristaxable = js.getString("customeristaxable");

                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,id);

                                                if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String order_id = CAllOrders.getString(CAllOrders.getColumnIndex("order_id"));

                                                        sqLiteController.deleteOrder(order_id);

                                                        if (draftnumber.equals("null")) {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                        }else {
                                                            sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                        }


                                                    } while (CAllOrders.moveToNext());
                                                }

                                                if(CAllOrders.getCount() == 0){

                                                    if (draftnumber.equals("null")) {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                    }else {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                    }
                                                }

                                                sqLiteController.deleteProductItems(id);

                                                // Cursor COrdersProducts = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRODUCT_ORDERID,id);

                                               *//* if (COrdersProducts.moveToFirst()) {
                                                    do {
                                                      //  @SuppressLint("Range") String pid = COrdersProducts.getString(COrdersProducts.getColumnIndex("pid"));
                                                       // sqLiteController.deleteProductIDItems(pid);

                                                    } while (COrdersProducts.moveToNext());
                                                }*//*

                                            }
                                        } finally {
                                            sqLiteController.close();


                                        }
                                    }
                                });

                            }

                            for (int i = 0; i < OrderProducts.length(); i++) {
                                JSONObject js = OrderProducts.getJSONObject(i);
                                String productname = js.getString("productname");
                                String quantity = js.getString("quantity");
                                String baseamount = js.getString("baseamount");
                                String priceperunit = js.getString("priceperunit");
                                String productid = js.getString("productid");
                                String orderid = js.getString("orderid");
                                String pid = js.getString("id");
                                String itemistaxable = js.getString("itemistaxable");
                                String warehouseid = js.getString("warehouseid");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            long fetchOrderCount = sqLiteController.fetchOrderCount();
                                            if (fetchOrderCount > 0) {

                                                Cursor CAllOrders = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRO_ID,pid);

                                               *//* if (CAllOrders.moveToFirst()) {
                                                    do {
                                                        @SuppressLint("Range") String pid = CAllOrders.getString(CAllOrders.getColumnIndex("pid"));

                                                        sqLiteController.deleteProductIDItems(pid);

                                                        //Products Insert Method
                                                        sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);

                                                    } while (CAllOrders.moveToNext());
                                                }*//*

                                                if(CAllOrders.getCount() == 0){

                                                    //Products Insert Method
                                                    sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);

                                                }
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }

                            ac.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (pr != null) {
                                        if (pr.isShowing()) {
                                            pr.dismiss();
                                        }
                                    }

                                    if(flag.equals("OrderRefresh")){

                                    }else if(flag.equals("InvoiceRefresh")){

                                    }else if(flag.equals("ProductRefresh")){

                                    }else if(flag.equals("CustomerRefresh")){

                                    }else {
                                        ac.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(ac);
                                                LayoutInflater layoutInflater = ac.getLayoutInflater();
                                                View v = layoutInflater.inflate(R.layout.activity_successalert, null);
                                                builder.setView(v);
                                                final AlertDialog dialog = builder.create();
                                                //dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
                                                dialog.show();
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.setCancelable(false);

                                                AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
                                                AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);

                                                if(flag.equals("CreateOrder")){
                                                    tittleAlert_tv.setText("Presale Created Successful");
                                                }else if(flag.equals("UpdateOrder")){
                                                    tittleAlert_tv.setText("Presale Updated Successful");
                                                }else if(flag.equals("CreateInvoice")){
                                                    tittleAlert_tv.setText("Invoice Generated Successful");
                                                }else if(flag.equals("ProductCreate")){
                                                    tittleAlert_tv.setText("Product Added Successful");
                                                }else if(flag.equals("ProductUpdate")){
                                                    tittleAlert_tv.setText("Product Updated Successful");
                                                }



                                                btn_Okalrt.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        ac.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                Date date1 = new Date();
                                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                String currentDateTime = sdf.format(date1);

                                                                System.out.println("currentDateTime-----"+currentDateTime);
                                                                String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products"};

                                                                SQLiteController sqLiteController1 = new SQLiteController(ac);
                                                                sqLiteController1.open();
                                                                try {
                                                                    for (String s: elements) {
                                                                        System.out.println("systemOrders-----------"+s);
                                                                        sqLiteController1.UpdateMasterSync(s,currentDateTime);
                                                                    }
                                                                } finally {
                                                                    sqLiteController1.close();

                                                                }
                                                            }
                                                        });

                                                        if(flag.equals("CreateOrder")){
                                                            Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                            ac.startActivity(intent);
                                                        }else if(flag.equals("UpdateOrder")){
                                                            Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                            ac.startActivity(intent);
                                                        }else if(flag.equals("CreateInvoice")){
                                                            Intent intent = new Intent(ac, Activity_InvoiceHistory.class);
                                                            ac.startActivity(intent);
                                                        }else if(flag.equals("ProductCreate")){
                                                            Intent intent = new Intent(ac, Activity_ProductHistory.class);
                                                            ac.startActivity(intent);
                                                        }else if(flag.equals("ProductUpdate")){
                                                            Intent intent = new Intent(ac, Activity_ProductHistory.class);
                                                            ac.startActivity(intent);
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });

                                            }
                                        });
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
    }*/


    public void GetMasterInsert(Activity ac, String flag, AlertDialog pr, String  ModifiedDateTime) {

        SQLiteController sqLiteController1 = new SQLiteController(ac);
        sqLiteController1.open();
        try {
            long count = sqLiteController1.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController1.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    @SuppressLint("Range") String user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    System.out.println("user_id---"+user_id);
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));

                    System.out.println("token----"+token);

                }

                //ReadMasterSync
                Cursor Csync = sqLiteController1.readTableMasterSync();
                if (Csync.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String master_sync_name = Csync.getString(Csync.getColumnIndex("master_sync_name"));
                        @SuppressLint("Range") String master_sync_time = Csync.getString(Csync.getColumnIndex("master_sync_time"));

                        System.out.println("master_sync_name----"+master_sync_name);
                        System.out.println("master_sync_time----"+master_sync_time);
                        System.out.println("ModifiedDateTime----"+ModifiedDateTime);

                        if(master_sync_name.equals("Orders")){
                            if(flag.equals("InvoiceRefresh")){
                                OrderLastSync =ModifiedDateTime;
                            }else {
                                OrderLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Orders_products")){
                            if(flag.equals("InvoiceRefresh")){
                                OrderProductsLastSync =ModifiedDateTime;
                            }else {
                                OrderProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Shipments")){
                            if(flag.equals("InvoiceRefresh")){
                                ShipmentLastSync =ModifiedDateTime;
                            }else {
                                ShipmentLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Addresses")){
                            if(flag.equals("InvoiceRefresh")){
                                AddressLastSync =ModifiedDateTime;
                            }else {
                                AddressLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Invoices")){
                            if(flag.equals("InvoiceRefresh")){
                                InvoiceLastSync =ModifiedDateTime;
                            }else {
                                InvoiceLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Invoices_products")){
                            if(flag.equals("InvoiceRefresh")){
                                InvoiceProductsLastSync =ModifiedDateTime;
                            }else {
                                InvoiceProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                            }
                        }else if(master_sync_name.equals("Customers")){
                                if(flag.equals("InvoiceRefresh")){
                                    CustomerLastSync =ModifiedDateTime;
                                }else {
                                    CustomerLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                                }
                        }else if(master_sync_name.equals("Products")){
                                    if(flag.equals("InvoiceRefresh")){
                                        ProductsLastSync =ModifiedDateTime;
                                    }else {
                                        ProductsLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                                    }
                        }else if(master_sync_name.equals("Products_assets")){
                                        if(flag.equals("InvoiceRefresh")){
                                            ProductAssertLastSync =ModifiedDateTime;
                                        }else {
                                            ProductAssertLastSync = Csync.getString(Csync.getColumnIndex("master_sync_time"));
                                        }
                        }
                    } while (Csync.moveToNext());
                }

            }
        } finally {
            sqLiteController1.close();
        }

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                ac.runOnUiThread(new Runnable() {
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
                    System.out.println("URL------"+Const.GetCreateOrderLogin+OrderLastSync+"&orderproductlastsyncon="+OrderProductsLastSync+"&shipmentlastsyncon="+ShipmentLastSync+"&addresslastsyncon="+AddressLastSync+"&invoicelastsyncon="+InvoiceLastSync+"&invoiceproductlastsyncon="+InvoiceProductsLastSync+"&customerlastsyncon="+CustomerLastSync+"&productlastsyncon="+ProductsLastSync+"&productassetlastsyncon="+ProductAssertLastSync);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Const.GetCreateOrderLogin+OrderLastSync+"&orderproductlastsyncon="+OrderProductsLastSync+"&shipmentlastsyncon="+ShipmentLastSync+"&addresslastsyncon="+AddressLastSync+"&invoicelastsyncon="+InvoiceLastSync+"&invoiceproductlastsyncon="+InvoiceProductsLastSync+"&customerlastsyncon="+CustomerLastSync+"&productlastsyncon="+ProductsLastSync+"&productassetlastsyncon="+ProductAssertLastSync)
                            // .post(body)
                            .addHeader("Authorization","Bearer "+token)
                            .build();
                    final Response response = client1.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println("OrdersLogin---"+jsonObject);
                            Log.d("OrdersLogin----",jsonObject.toString());
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");

                            for (int i = 0; i < Orders.length(); i++) {
                                JSONObject js = Orders.getJSONObject(i);
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String customeridtype = js.getString("customeridtype");
                                String datefulfilled = js.getString("datefulfilled");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String poreferencenum = js.getString("poreferencenum");
                                String ispricelocked = js.getString("ispricelocked");
                                String quoteid = js.getString("quoteid");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String submitdate = js.getString("submitdate");
                                String submitstatus = js.getString("submitstatus");
                                String totalamount = js.getString("totalamount");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String createdby = js.getString("createdby");
                                String creratedon = js.getString("creratedon");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String cancellationreason = js.getString("cancellationreason");
                                String comments = js.getString("comments");
                                String lastbackofficesubmit = js.getString("lastbackofficesubmit");
                                String opportunityid = js.getString("opportunityid");
                                String ordernumber = js.getString("ordernumber");
                                String cancellationDate = js.getString("cancellationDate");
                                String confirmedby = js.getString("confirmedby");
                                String confirmeddate = js.getString("confirmeddate");
                                String notes = js.getString("notes");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String warehouseid = js.getString("warehouseid");
                                String modifiedon = js.getString("modifiedon");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");
                                String customeristaxable = js.getString("customeristaxable");

                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor OrderTable = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,id);
                                            if (OrderTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String order_id = OrderTable.getString(OrderTable.getColumnIndex("order_id"));
                                                    sqLiteController.deleteOrder(order_id);
                                                    if (draftnumber.equals("null")) {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                    }else {
                                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                    }
                                                } while (OrderTable.moveToNext());
                                            }

                                            if(OrderTable.getCount() == 0){
                                                if (draftnumber.equals("null")) {
                                                    sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                                }else {
                                                    sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                                }
                                            }
                                            sqLiteController.deleteProductItems(id);
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
                            for (int i = 0; i < OrderProducts.length(); i++) {
                                JSONObject js = OrderProducts.getJSONObject(i);
                                String productname = js.getString("productname");
                                String quantity = js.getString("quantity");
                                String baseamount = js.getString("baseamount");
                                String priceperunit = js.getString("priceperunit");
                                String productid = js.getString("productid");
                                String orderid = js.getString("orderid");
                                String pid = js.getString("id");
                                String itemistaxable = js.getString("itemistaxable");
                                String warehouseid = js.getString("warehouseid");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor OrderProductsTable = sqLiteController.readOrderItem(DbHandler.TABLE_ORDER_PRODUCT,DbHandler.ORDER_PRO_ID,pid);
                                            if(OrderProductsTable.getCount() == 0){
                                                sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
                            for (int i = 0; i < Products_assets.length(); i++) {
                                JSONObject js = Products_assets.getJSONObject(i);
                                String id = js.getString("id");
                                String assetname = js.getString("assetname");
                                String productid = js.getString("productid");
                                String imageurl = "";
                                //String imageurl = js.getString("imageurl");
                                String filepath = js.getString("filepath");
                                String assettype = js.getString("assettype");
                                String isdefault = js.getString("isdefault");
                                String isactive = js.getString("isactive");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorAssertTable = sqLiteController.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_ID,id);
                                            if (CursorAssertTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String Products_assets_id = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_id"));
                                                    sqLiteController.deleteProductAssertIDItems(Products_assets_id);
                                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                                } while (CursorAssertTable.moveToNext());
                                            }
                                            if(CursorAssertTable.getCount() == 0){
                                                sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
                            for (int i = 0; i < Products.length(); i++) {
                                JSONObject js = Products.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("name");
                                String description = js.getString("description");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");

                                String finalPrice = price;
                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorProductsTable = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,id);
                                            if (CursorProductsTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String all_product_id = CursorProductsTable.getString(CursorProductsTable.getColumnIndex("all_product_id"));
                                                    sqLiteController.deleteAllProductIDItems(all_product_id);
                                                    sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);
                                                } while (CursorProductsTable.moveToNext());
                                            }
                                            if(CursorProductsTable.getCount() == 0){
                                                sqLiteController.insertALlProduct(id,productnumber,productname, finalPrice,status, "null",upccode,istaxable,description);
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
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
                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
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
                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject js = invoice.getJSONObject(i);

                                String orderid = js.getString("orderid");
                                String externalid = js.getString("externalid");
                                String externalstatus = js.getString("externalstatus");
                                String paymenttypeid = js.getString("paymenttypeid");
                                String paymentid = js.getString("paymentid");
                                String paymentmethod = js.getString("paymentmethod");
                                String name = js.getString("name");
                                String billtoaddressid = js.getString("billtoaddressid");
                                String customerid = js.getString("customerid");
                                String vendorid = js.getString("vendorid");
                                String description = js.getString("description");
                                String discountamount = js.getString("discountamount");
                                String transactioncurrencyid = js.getString("transactioncurrencyid");
                                String exchangerate = js.getString("exchangerate");
                                String discountamountbase = js.getString("discountamountbase");
                                String discountpercentage = js.getString("discountpercentage");
                                String freightamount = js.getString("freightamount");
                                String freightamountbase = js.getString("freightamountbase");
                                String freighttermscode = js.getString("freighttermscode");
                                String ispricelocked = js.getString("ispricelocked");
                                String ordernumber = js.getString("ordernumber");
                                String requestdeliveryby = js.getString("requestdeliveryby");
                                String shiptoaddressid = js.getString("shiptoaddressid");
                                String invoicedate = js.getString("invoicedate");
                                String submitstatus = js.getString("submitstatus");
                                String submitstatusdescription = js.getString("submitstatusdescription");
                                String totalamount = js.getString("totalamount");
                                String totalamountbase = js.getString("totalamountbase");
                                String totalamountlessfreight = js.getString("totalamountlessfreight");
                                String totalamountlessfreightbase = js.getString("totalamountlessfreightbase");
                                String totaldiscountamount = js.getString("totaldiscountamount");
                                String totaldiscountamountbase = js.getString("totaldiscountamountbase");
                                String totallineitemamount = js.getString("totallineitemamount");
                                String totallineitemamountbase = js.getString("totallineitemamountbase");
                                String totallineitemdiscountamount = js.getString("totallineitemdiscountamount");
                                String totallineitemdiscountamountbase = js.getString("totallineitemdiscountamountbase");
                                String totaltax = js.getString("totaltax");
                                String totaltaxbase = js.getString("totaltaxbase");
                                String willcall = js.getString("willcall");
                                String salesrepid = js.getString("salesrepid");
                                String pricingdate = js.getString("pricingdate");
                                String deliverynote = js.getString("deliverynote");
                                String shipnote = js.getString("shipnote");
                                String termsconditions = js.getString("termsconditions");
                                String memo = js.getString("memo");
                                String referenceorder = js.getString("referenceorder");
                                String createdon = js.getString("createdon");
                                String createdby = js.getString("createdby");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String warehouseid = js.getString("warehouseid");
                                String customeristaxable = js.getString("customeristaxable");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");


                                String draftnumber = js.getString("draftnumber");
                                String taxid = js.getString("taxid");
                                String netterms = js.getString("netterms");
                                String duedate = js.getString("duedate");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor CursorInvoiceTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,id);
                                            if (CursorInvoiceTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = CursorInvoiceTable.getString(CursorInvoiceTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoice(id);
                                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                    } else {
                                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                    }
                                                } while (CursorInvoiceTable.moveToNext());
                                            }
                                            if(CursorInvoiceTable.getCount() == 0){
                                                if (draftnumber.equals("") || draftnumber.equals("null")) {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, "", "", "Online", "non_Sync",taxid,netterms,duedate);
                                                } else {
                                                    sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, modifiedbehalfof, modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, "", "", "", referenceorder, vendorid, externalid, externalstatus, warehouseid, customeristaxable, draftnumber, createdon, "Offline", "Sync",taxid,netterms,duedate);
                                                }
                                            }
                                            sqLiteController.deleteInvoiceProductsItems(id);
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });

                            }
                            for (int i = 0; i < invoiceproduct.length(); i++) {
                                JSONObject js_P = invoiceproduct.getJSONObject(i);
                                String invoiceid_IP = js_P.getString("invoiceid");
                                String transactioncurrencyid_IP = js_P.getString("transactioncurrencyid");
                                String uomid = js_P.getString("uomid");
                                String baseamount = js_P.getString("baseamount");
                                String exchangerate_Ip = js_P.getString("exchangerate");
                                String baseamountbase = js_P.getString("baseamountbase");
                                String description_Ip = js_P.getString("description");
                                String extendedamount = js_P.getString("extendedamount");
                                String extendedamountbase = js_P.getString("extendedamountbase");
                                String iscopied = js_P.getString("iscopied");
                                String ispriceoverridden = js_P.getString("ispriceoverridden");
                                String isproductoverridden = js_P.getString("isproductoverridden");
                                String lineitemnumber = js_P.getString("lineitemnumber");
                                String manualdiscountamount = js_P.getString("manualdiscountamount");
                                String manualdiscountamountbase = js_P.getString("manualdiscountamountbase");
                                String priceperunit = js_P.getString("priceperunit");
                                String priceperunitbase = js_P.getString("priceperunitbase");
                                String productdescription = js_P.getString("productdescription");
                                String productname = js_P.getString("productname");
                                String productid = js_P.getString("productid");
                                String quantity = js_P.getString("quantity");
                                String quantitybackordered = js_P.getString("quantitybackordered");
                                String quantitycancelled = js_P.getString("quantitycancelled");
                                String quantityshipped = js_P.getString("quantityshipped");
                                String quantitypicked = js_P.getString("quantitypicked");
                                String quantitypacked = js_P.getString("quantitypacked");
                                String requestdeliverybyip = js_P.getString("requestdeliveryby");
                                String salesorderispricelocked = js_P.getString("salesorderispricelocked");
                                String shiptoaddressidip = js_P.getString("shiptoaddressid");
                                String tax = js_P.getString("tax");
                                String taxbase = js_P.getString("taxbase");
                                String willcallip = js_P.getString("willcall");
                                String createdonip = js_P.getString("createdon");
                                String createdbyip = js_P.getString("createdby");
                                String modifiedbyip = js_P.getString("modifiedby");
                                String modifiedbehalfofip = js_P.getString("modifiedbehalfof");
                                String modifiedonip = js_P.getString("modifiedon");
                                String orderpid = js_P.getString("orderpid");
                                String itemistaxable = js_P.getString("itemistaxable");
                                String idip = js_P.getString("id");
                                String isactiveip = js_P.getString("isactive");
                                String domainEvents = js_P.getString("domainEvents");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor InvoiceProductTable = sqLiteController.readOrderItem(DbHandler.TABLE_INVOICE_PRODUCTS,DbHandler.IN_PRODUCTS_ID,idip);
                                            /*if (InvoiceProductTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String id = InvoiceProductTable.getString(InvoiceProductTable.getColumnIndex("id"));
                                                    sqLiteController.deleteInvoiceProductsALL(id);
                                                    sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                                } while (InvoiceProductTable.moveToNext());
                                            }*/
                                            if(InvoiceProductTable.getCount() == 0){
                                                sqLiteController.insertInvoiceProduct(idip, invoiceid_IP, transactioncurrencyid_IP, uomid, baseamount, exchangerate_Ip, baseamountbase, description_Ip, extendedamount, extendedamountbase, iscopied, ispriceoverridden, isproductoverridden, lineitemnumber, manualdiscountamount, manualdiscountamountbase, "", "", "", priceperunit, priceperunitbase, "", productdescription, productname, productid, quantity, quantitybackordered, quantitycancelled, quantityshipped, requestdeliverybyip, salesorderispricelocked, "", "", shiptoaddressidip, tax, taxbase, "", "", willcallip, "", "", "", "", "", "", "", "", "", "", "", createdbyip, "", createdonip, modifiedbyip, modifiedbehalfofip, modifiedonip, "", "", "", "", isactiveip, quantitypicked, quantitypacked, orderpid, "", "", itemistaxable, "", "");
                                            }
                                        } finally {
                                            sqLiteController.close();
                                        }
                                    }
                                });
                            }
                            for (int i = 0; i < shipments.length(); i++) {
                                JSONObject js = shipments.getJSONObject(i);
                                String orderid = js.getString("orderid");
                                String shipmenttypeid = js.getString("shipmenttypeid");
                                String warehouseid = js.getString("warehouseid");
                                String fulfillmentstatus = js.getString("fulfillmentstatus");
                                String routenum = js.getString("routenum");
                                String routedate = js.getString("routedate");
                                String truck = js.getString("truck");
                                String driverid = js.getString( "driverid");
                                String carrierid = js.getString("carrierid");
                                String shippeddate = js.getString("shippeddate");
                                String trackingnumber = js.getString( "trackingnumber");
                                String deliverydate = js.getString( "deliverydate");
                                String deliverytime = js.getString( "deliverytime");
                                String pickerid = js.getString("pickerid");
                                String packerid = js.getString( "packerid");
                                String shipperid = js.getString("shipperid");
                                String invoicerid = js.getString( "invoicerid");
                                String paymenterid = js.getString("paymenterid");
                                String uuid = js.getString( "uuid");
                                String isdisabled = js.getString( "isdisabled");
                                String disabledby = js.getString( "disabledby");
                                String disabledbehalfof = js.getString("disabledbehalfof");
                                String isdeleted = js.getString("isdeleted");
                                String deletedby = js.getString( "deletedby");
                                String deletedbehalfof = js.getString( "deletedbehalfof");
                                String deletedon = js.getString("deletedon");
                                String createdby = js.getString("createdby");
                                String createdbehalfof = js.getString("createdbehalfof");
                                String creratedon = js.getString("creratedon");
                                String modifiedby = js.getString("modifiedby");
                                String modifiedbehalfof = js.getString("modifiedbehalfof");
                                String modifiedon = js.getString("modifiedon");
                                String datafromtypeid = js.getString("datafromtypeid");
                                String datafromid = js.getString("datafromid");
                                String tenantid = js.getString("tenantid");
                                String deliveryagentid = js.getString("deliveryagentid");
                                String deliveryshipmenttypeid = js.getString("deliveryshipmenttypeid");
                                String routedayid = js.getString("routedayid");
                                String routeid = js.getString("routeid");
                                String truckid = js.getString("truckid");
                                String invoiceid = js.getString("invoiceid");
                                String workorderid = js.getString("workorderid");
                                String purchaseorderid = js.getString("purchaseorderid");
                                String undeliveredreasonid = js.getString("undeliveredreasonid");
                                String undeliveredreason = js.getString("undeliveredreason");
                                String id = js.getString("id");
                                String isactive = js.getString("isactive");

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
                                        sqLiteController.open();
                                        try {
                                            Cursor shipmentTable = sqLiteController.readOrderItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ID,id);
                                            if (shipmentTable.moveToFirst()) {
                                                do {
                                                    @SuppressLint("Range") String shipment_idd = shipmentTable.getString(shipmentTable.getColumnIndex("shipment_id"));
                                                    sqLiteController.deleteShipItems(shipment_idd);
                                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);
                                                } while (shipmentTable.moveToNext());
                                            }
                                            if(shipmentTable.getCount() == 0){
                                                sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);

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

                                ac.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SQLiteController sqLiteController = new SQLiteController(ac);
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
                ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Completed------");

                        ac.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(flag.equals("CreateOrder")){


                                }else {
                                    if (pr != null) {
                                        if (pr.isShowing()) {
                                            pr.dismiss();
                                        }
                                    }
                                }


                                if(flag.equals("Payment")){

                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("ModifiedDateTime-----"+ModifiedDateTime);
                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                            SQLiteController sqLiteController1 = new SQLiteController(ac);
                                            sqLiteController1.open();
                                            try {
                                                for (String s: elements) {
                                                    sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                }
                                            } finally {
                                                sqLiteController1.close();

                                            }
                                        }
                                    });


                                }else if(flag.equals("OrderMailSend")){

                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("ModifiedDateTime-----"+ModifiedDateTime);
                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                            SQLiteController sqLiteController1 = new SQLiteController(ac);
                                            sqLiteController1.open();
                                            try {
                                                for (String s: elements) {
                                                    sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                }
                                            } finally {
                                                sqLiteController1.close();

                                            }
                                        }
                                    });


                                }else if(flag.equals("OrderRefresh")){

                                }else if(flag.equals("InvoiceRefresh")){

                                }else if(flag.equals("ProductRefresh")){

                                }else if(flag.equals("CustomerRefresh")){

                                }else if(flag.equals("CustomerCreate")){

                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("ModifiedDateTime-----"+ModifiedDateTime);
                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                            SQLiteController sqLiteController1 = new SQLiteController(ac);
                                            sqLiteController1.open();
                                            try {
                                                for (String s: elements) {
                                                    sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                }
                                            } finally {
                                                sqLiteController1.close();

                                            }
                                        }
                                    });

                                }else if(flag.equals("CreateOrder")){

                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Date date1 = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                            String currentDateTime = sdf.format(date1);


                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};


                                            System.out.println("UpdateCurrentDateTime-----"+currentDateTime);

                                            SQLiteController sqLiteController1 = new SQLiteController(ac);
                                            sqLiteController1.open();
                                            try {
                                                for (String s: elements) {
                                                    sqLiteController1.UpdateMasterSync(s,currentDateTime);
                                                }
                                            } finally {
                                                sqLiteController1.close();

                                            }


                                        }
                                    });


                                }else if(flag.equals("CustomerUpdate")){

                                }else if(flag.equals("CollectPayment")){

                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("ModifiedDateTime-----"+ModifiedDateTime);
                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                                            SQLiteController sqLiteController1 = new SQLiteController(ac);
                                            sqLiteController1.open();
                                            try {
                                                for (String s: elements) {
                                                    sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                }
                                            } finally {
                                                sqLiteController1.close();

                                            }
                                        }
                                    });

                                }else {
                                    ac.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ac);
                                            LayoutInflater layoutInflater = ac.getLayoutInflater();
                                            View v = layoutInflater.inflate(R.layout.activity_successalert, null);
                                            builder.setView(v);
                                            final AlertDialog dialog = builder.create();
                                            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
                                            dialog.show();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.setCancelable(false);

                                            AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
                                            AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);

                                            if(flag.equals("CreateOrder")){
                                                tittleAlert_tv.setText("Presale Created Successful");
                                            }else if(flag.equals("UpdateOrder")){
                                                tittleAlert_tv.setText("Presale Updated Successful");
                                            }else if(flag.equals("CreateInvoice")){
                                                tittleAlert_tv.setText("Invoice Generated Successful");
                                            }else if(flag.equals("ProductCreate")){
                                                tittleAlert_tv.setText("Product Added Successful");
                                            }else if(flag.equals("ProductUpdate")){
                                                tittleAlert_tv.setText("Product Updated Successful");
                                            }else if(flag.equals("Order Deliver")){
                                                tittleAlert_tv.setText("Order Delivered Successful");
                                                btn_Okalrt.setText("Close");
                                            }else if(flag.equals("DirectInvoicePayment")){
                                                tittleAlert_tv.setText("Payment Captured Successful");
                                                btn_Okalrt.setText("Close");
                                            }else if(flag.equals("Login")){
                                                tittleAlert_tv.setText("Welcome Login Successful");
                                                btn_Okalrt.setText("Continue");
                                            }



                                            btn_Okalrt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    ac.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            Date date1 = new Date();
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                                           // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                            String currentDateTime = sdf.format(date1);


                                                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};


                                                            if(flag.equals("CreateInvoice")){

                                                                System.out.println("UpdateModifiedDateTime-----"+ModifiedDateTime);

                                                                SQLiteController sqLiteController1 = new SQLiteController(ac);
                                                                sqLiteController1.open();
                                                                try {
                                                                    for (String s: elements) {
                                                                        sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                                    }
                                                                } finally {
                                                                    sqLiteController1.close();

                                                                }
                                                            }else if(flag.equals("Order Deliver")){

                                                                System.out.println("UpdateModifiedDateTime-----"+ModifiedDateTime);

                                                                SQLiteController sqLiteController1 = new SQLiteController(ac);
                                                                sqLiteController1.open();
                                                                try {
                                                                    for (String s: elements) {
                                                                        sqLiteController1.UpdateMasterSync(s,ModifiedDateTime);
                                                                    }
                                                                } finally {
                                                                    sqLiteController1.close();

                                                                }
                                                            }else {

                                                                System.out.println("UpdateCurrentDateTime-----"+currentDateTime);

                                                                SQLiteController sqLiteController1 = new SQLiteController(ac);
                                                                sqLiteController1.open();
                                                                try {
                                                                    for (String s: elements) {
                                                                        sqLiteController1.UpdateMasterSync(s,currentDateTime);
                                                                    }
                                                                } finally {
                                                                    sqLiteController1.close();

                                                                }
                                                            }


                                                        }
                                                    });

                                                    if(flag.equals("CreateOrder")){
                                                        Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("UpdateOrder")){
                                                        Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("CreateInvoice")){
                                                        Intent intent = new Intent(ac, Activity_InvoiceHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("ProductCreate")){
                                                        Intent intent = new Intent(ac, Activity_ProductHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("ProductUpdate")){
                                                        Intent intent = new Intent(ac, Activity_ProductHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("Login")){
                                                        Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("Order Deliver")){
                                                        Intent intent = new Intent(ac, Activity_OrdersHistory.class);
                                                        ac.startActivity(intent);
                                                    }else if(flag.equals("DirectInvoicePayment")){
                                                        Intent intent = new Intent(ac, Activity_InvoiceHistory.class);
                                                        ac.startActivity(intent);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    });
                                }


                            }
                        });
                    }
                });

            }

        };
        task.execute((Void[])null);
    }




}
