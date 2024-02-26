package com.example.arcomdriver.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Oct 2022*/

public class ActivitySignIn extends AppCompatActivity {
    AppCompatEditText et_userName,et_password;
    int PERMISSION_CODE = 111;
    String[] PERMISSIONS = {Manifest.permission.READ_MEDIA_VIDEO ,Manifest.permission.READ_MEDIA_IMAGES ,android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    String  DataFlag="New";
    String OrderLastSync,OrderProductsLastSync,ShipmentLastSync,AddressLastSync,InvoiceLastSync,InvoiceProductsLastSync,CustomerLastSync,ProductsLastSync,ProductAssertLastSync;

    AppCompatTextView loading_tv;

    public AlertDialog Progress_dialog;
        @SuppressLint("Range")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder1.build());

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySignIn.this, R.style.LoadingStyle);
            builder.setCancelable(false);
            LayoutInflater layoutInflater1 = ActivitySignIn.this.getLayoutInflater();
            View v = layoutInflater1.inflate(R.layout.layout_avi, null);
            builder.setView(v);
            Progress_dialog = builder.create();

            AppCompatTextView loading_tv = v.findViewById(R.id.loading_tv);

            SQLiteController sqLiteController1 = new SQLiteController(this);
            sqLiteController1.open();
            try {
                long count = sqLiteController1.fetchSyncCount();
                System.out.println("masterCount----"+count);
                if (count > 0) {

                    DataFlag ="dataAvailable";
                    //ReadMasterSync
                    /*Cursor Csync = sqLiteController1.readTableMasterSync();
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
                    }*/
                    loading_tv.setText("Please Wait...");

                }else {
                    DataFlag="New";

                    OrderLastSync ="1990-01-01T00:00:00.000Z";
                    OrderProductsLastSync ="1990-01-01T00:00:00.000Z";
                    ShipmentLastSync ="1990-01-01T00:00:00.000Z";
                    AddressLastSync ="1990-01-01T00:00:00.000Z";
                    InvoiceLastSync ="1990-01-01T00:00:00.000Z";
                    InvoiceProductsLastSync ="1990-01-01T00:00:00.000Z";
                    CustomerLastSync ="1990-01-01T00:00:00.000Z";
                    ProductsLastSync ="1990-01-01T00:00:00.000Z";
                    ProductAssertLastSync ="1990-01-01T00:00:00.000Z";

                    loading_tv.setText("Please wait while we set up your device...");
                }
            } finally {
                sqLiteController1.close();
            }

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE);
            }

            et_userName = findViewById(R.id.et_userName);
            et_password = findViewById(R.id.et_password);

            findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.getInstance().hideKeyboard(ActivitySignIn.this);
                    if (Connectivity.isConnected(ActivitySignIn.this) &&
                            Connectivity.isConnectedFast(ActivitySignIn.this)) {
                        getValidation(v);
                    }else {
                        Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                    }
                }
            });

           /* SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
            sqLiteController.open();
            try {
                sqLiteController.deleteTableUser();
                sqLiteController.deleteTableOrders();
                sqLiteController.deleteTableProducts();
                sqLiteController.deleteTableShipment();
                sqLiteController.deleteTableWarehouse();
                sqLiteController.deleteTableCancelReason();
                sqLiteController.deleteTableAllProducts();
                sqLiteController.deleteTableSalesRep();
                sqLiteController.deleteTableDeliverysRep();
                sqLiteController.deleteTableFulfilment();
                sqLiteController.deleteTableInvoiceFormat();
                sqLiteController.deleteTableCustomer();
                sqLiteController.deleteTableAddress();
                sqLiteController.deleteTableInvoice();
                sqLiteController.deleteTableInvoiceProducts();
                sqLiteController.deleteTableTerms();
            } finally {
                sqLiteController.close();
                GetCusState("JWTEncoded");
                GetCusTax("JWTEncoded");
                GetTerms("JWTEncoded");
                GetPDFFormat("JWTEncoded");
                GetWarehouse("JWTEncoded");
                GetCancelledReason("81b2823c-d128-45bd-a7f4-1acac599065c","JWTEncoded");
                GetAllProduct("JWTEncoded");
                GetFulfilment("JWTEncoded");
                GetCurrency("JWTEncoded");
                GetSalesRep("JWTEncoded");
                GetDeliveryRep("JWTEncoded");
            }*/

              SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
            sqLiteController.open();
            try {
                sqLiteController.deleteCS_STATE();
            } finally {
                sqLiteController.close();

                GetCusState("JWTEncoded");

            }



        }

    private void getValidation(final View v) {
        if (Utils.getInstance().checkIsEmpty(et_userName)) {
            Utils.getInstance().snackBarMessage(v,"Enter the UserName!");
        } else if (Utils.getInstance().checkIsEmpty(et_password)) {
            Utils.getInstance().snackBarMessage(v,"Enter the Password");
        } else {
            //Login API
            try {
                JSONObject json = new JSONObject();
                json.put("email",et_userName.getText().toString());
                json.put("password",et_password.getText().toString());
                System.out.println("Login : "+json);
                postInstantLogin(v,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void postInstantLogin(final View v, final JSONObject json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Progress_dialog.show();
            }
        });
        try {
            App.getInstance().PostLogin(json.toString(), new Callback() {
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
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    final JSONObject js = jsonObject.getJSONObject("data");
                                    final String JWTEncoded = js.getString("token");

                                    if ( succeeded == true) {
                                        String[] split = JWTEncoded.split("\\.");
                                        //Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
                                        //Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
                                        JSONObject jsonUser = new JSONObject(getJson(split[1]).toString());
                                        String Email = jsonUser.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
                                        String UserID = jsonUser.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier");
                                        String fullName = jsonUser.getString("fullName");

                                        SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
                                        sqLiteController.open();
                                        try {
                                            sqLiteController.deleteTableUser();
                                            //sqLiteController.deleteTableOrders();
                                            //sqLiteController.deleteTableOrdersProducts();
                                            //sqLiteController.deleteTableShipment();
                                            sqLiteController.deleteTableWarehouse();
                                            sqLiteController.deleteTableCancelReason();
                                            //sqLiteController.deleteTableAllProducts();
                                            sqLiteController.deleteTableSalesRep();
                                            sqLiteController.deleteTableDeliverysRep();
                                            sqLiteController.deleteTableFulfilment();
                                            sqLiteController.deleteTableInvoiceFormat();
                                            //sqLiteController.deleteTableCustomer();
                                            //sqLiteController.deleteTableAddress();
                                            //sqLiteController.deleteTableInvoice();
                                            //sqLiteController.deleteTableInvoiceProducts();
                                            sqLiteController.deleteTableTerms();
                                        } finally {
                                            sqLiteController.close();
                                           // GetAllProduct(JWTEncoded);

                                            GetCusTax(JWTEncoded);
                                            GetTerms(JWTEncoded);
                                            GetPDFFormat(JWTEncoded);
                                            GetWarehouse(JWTEncoded);
                                            GetCancelledReason("81b2823c-d128-45bd-a7f4-1acac599065c",JWTEncoded);
                                            GetFulfilment(JWTEncoded);
                                            GetCurrency(JWTEncoded);
                                            GetSalesRep(JWTEncoded);
                                            GetDeliveryRep(JWTEncoded);
                                            GetUserDetails(UserID,JWTEncoded);
                                        }

                                    }
                                    else {
                                        Toast.makeText(ActivitySignIn.this, "login Error, Please try again later!!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException | UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Progress_dialog != null) {
                                    if (Progress_dialog.isShowing()) {
                                        Progress_dialog.dismiss();
                                    }
                                }
                                try {
                                    String res = response.body().string();
                                    JSONObject jsonObject = new JSONObject(res);
                                    String exception = jsonObject.getString("exception");
                                    String errorCode = jsonObject.getString("errorCode");
                                    Toast.makeText(ActivitySignIn.this, exception, Toast.LENGTH_SHORT).show();
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
    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    private void Get_OrderList(String JWTEncoded) {

        try {
            App.getInstance().GetOrderLogin(JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        int OrderCount_ =0;
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            System.out.println(jsonObject);
                            JSONArray Orders = jsonObject.getJSONArray("Orders");
                            JSONArray shipments = jsonObject.getJSONArray("Shipments");
                            JSONArray address = jsonObject.getJSONArray("Addresses");
                            JSONArray OrderProducts = jsonObject.getJSONArray("Orders_products");
                            JSONArray invoice = jsonObject.getJSONArray("Invoices");
                            JSONArray invoiceproduct = jsonObject.getJSONArray("Invoices_products");
                            JSONArray Customers = jsonObject.getJSONArray("Customers");
                            JSONArray Products = jsonObject.getJSONArray("Products");
                            JSONArray Products_assets = jsonObject.getJSONArray("Products_assets");

                            Date date1 = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String currentDateTime = sdf.format(date1);

                            String[] elements = {"Orders", "Orders_products", "Invoices", "Invoices_products","Shipments","Addresses","Customers","Products","Products_assets"};

                            SQLiteController sqLiteController_sync = new SQLiteController(ActivitySignIn.this);
                            sqLiteController_sync.open();
                            try {
                                for (String s: elements) {
                                    OrderCount_ += 1;
                                    sqLiteController_sync.insertMasterSync(String.valueOf(OrderCount_),s,currentDateTime);
                                }
                            } finally {
                                Log.d("MasterSyncDate","Create");
                                sqLiteController_sync.close();
                            }

                            SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
                            sqLiteController.open();
                            try {
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
                                    sqLiteController.insertProductsAssert(id,assetname,productid,imageurl,filepath,assettype,isdefault,isactive);
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
                                    sqLiteController.insertALlProduct(id,productnumber,productname, price,status, "null",upccode,istaxable,description);
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
                                    sqLiteController.insertCustomer(id, customername, customertypeid, status, "", "", "", "", "", "", "",
                                            billingaddressid, shippingaddressid, deliveryaddressid, isactive, "", "", "", "", "", "", "", "", createdby, "", createdon, modifiedby, "", modifiedon, "", "", "", "", "", industry, "", "", customernumber, externalid, externalupdated,istaxable,taxid,netterms);
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
                                    if (draftnumber.equals("null")) {
                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Online","non_Sync","","","",taxid,netterms,duedate);
                                    }else {
                                        sqLiteController.insertOrders(id, name,billtoaddressid,customerid,customeridtype, datefulfilled, description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, lastbackofficesubmit, opportunityid, ordernumber, "", "", "", "", "", quoteid, requestdeliveryby, "", "", shiptoaddressid, "", submitdate, submitstatus, "", totalamount, "", "", "", "", "", totallineitemamount, "", "", "", totaltax, totaltaxbase, "", "", "", "", "", "", "", "", createdby, "", creratedon, "", "", modifiedon, "", salesrepid, pricingdate, "", "", "", isactive, deliverynote, shipnote, termsconditions, memo, cancellationreason, comments, cancellationDate, poreferencenum, confirmedby, confirmeddate, notes, warehouseid,customeristaxable,"Offline","Sync",submitdate,draftnumber,"",taxid,netterms,duedate);
                                    }
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
                                    sqLiteController.insertShipments( id, orderid, shipmenttypeid, warehouseid, routenum, routedate, truck, driverid, carrierid, shippeddate, trackingnumber, deliverydate, deliverytime, isactive, uuid, isdisabled, disabledby, disabledbehalfof, isdeleted, deletedby, deletedbehalfof, deletedon, createdby, createdbehalfof, creratedon, modifiedby, modifiedbehalfof, modifiedon, datafromtypeid, datafromid, tenantid, fulfillmentstatus, pickerid, packerid, shipperid, invoicerid, paymenterid, deliveryagentid, deliveryshipmenttypeid, routedayid, routeid, truckid, invoiceid, workorderid, purchaseorderid, undeliveredreasonid, undeliveredreason);
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
                                    sqLiteController.insertAddress(id,line1,line2,line3,stateorprovince,addresstypecode,country,city,postalcode,postofficebox,name,isprimaryaddress,isactive,domainEvents);
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
                                    sqLiteController.insertProducts(pid,orderid,productid,priceperunit,baseamount,quantity,productname,itemistaxable,warehouseid);
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

                                    if (draftnumber.equals("") || draftnumber.equals("null")) {
                                        sqLiteController.insertInvoice(id, orderid, paymenttypeid, paymentid, paymentmethod, name, billtoaddressid, customerid, "", "", description, discountamount, "", transactioncurrencyid, exchangerate, discountamountbase, discountpercentage, freightamount, freightamountbase, freighttermscode, ispricelocked, "", "", ordernumber, "", "", "", "", "", "", requestdeliveryby, "", "", shiptoaddressid, "", "", invoicedate, submitstatus, submitstatusdescription, totalamount, totalamountbase, totalamountlessfreight, totalamountlessfreightbase, totaldiscountamount, totaldiscountamountbase, totallineitemamount, totallineitemamountbase, totallineitemdiscountamount, totallineitemdiscountamountbase, totaltax, totaltaxbase, willcall, "", "", "", "", "", "", "", createdby, "",
                                                createdon,
                                                modifiedby,
                                                modifiedbehalfof,
                                                modifiedon,
                                                "",
                                                salesrepid,
                                                pricingdate,
                                                "",
                                                "",
                                                "",
                                                isactive,
                                                deliverynote,
                                                shipnote,
                                                termsconditions,
                                                memo,
                                                "",
                                                "",
                                                "",
                                                referenceorder,
                                                vendorid,
                                                externalid,
                                                externalstatus,
                                                warehouseid,
                                                customeristaxable,
                                                "",
                                                "",
                                                "Online",
                                                "non_Sync",taxid,netterms,duedate);

                                    }
                                    else {
                                        sqLiteController.insertInvoice(
                                                id,
                                                orderid,
                                                paymenttypeid,
                                                paymentid,
                                                paymentmethod,
                                                name,
                                                billtoaddressid,
                                                customerid,
                                                "",
                                                "",
                                                description,
                                                discountamount,
                                                "",
                                                transactioncurrencyid,
                                                exchangerate,
                                                discountamountbase,
                                                discountpercentage,
                                                freightamount,
                                                freightamountbase,
                                                freighttermscode,
                                                ispricelocked,
                                                "",
                                                "",
                                                ordernumber,
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                requestdeliveryby,
                                                "",
                                                "",
                                                shiptoaddressid,
                                                "",
                                                "",
                                                invoicedate,
                                                submitstatus,
                                                submitstatusdescription,
                                                totalamount,
                                                totalamountbase,
                                                totalamountlessfreight,
                                                totalamountlessfreightbase,
                                                totaldiscountamount,
                                                totaldiscountamountbase,
                                                totallineitemamount,
                                                totallineitemamountbase,
                                                totallineitemdiscountamount,
                                                totallineitemdiscountamountbase,
                                                totaltax,
                                                totaltaxbase,
                                                willcall,
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                createdby,
                                                "",
                                                createdon,
                                                modifiedby,
                                                modifiedbehalfof,
                                                modifiedon,
                                                "",
                                                salesrepid,
                                                pricingdate,
                                                "",
                                                "",
                                                "",
                                                isactive,
                                                deliverynote,
                                                shipnote,
                                                termsconditions,
                                                memo,
                                                "",
                                                "",
                                                "",
                                                referenceorder,
                                                vendorid,
                                                externalid,
                                                externalstatus,
                                                warehouseid,
                                                customeristaxable,
                                                draftnumber,
                                                createdon,
                                                "Offline",
                                                "Sync",taxid,netterms,duedate);
                                    }

                                }
                                for (int i = 0; i < invoiceproduct.length(); i++) {
                                    JSONObject js = invoiceproduct.getJSONObject(i);
                                    String invoiceid = js.getString("invoiceid");
                                    String transactioncurrencyid = js.getString("transactioncurrencyid");
                                    String uomid = js.getString( "uomid");
                                    String baseamount = js.getString("baseamount");
                                    String exchangerate = js.getString( "exchangerate");
                                    String baseamountbase = js.getString("baseamountbase");
                                    String description = js.getString("description");
                                    String extendedamount = js.getString("extendedamount");
                                    String extendedamountbase = js.getString("extendedamountbase");
                                    String iscopied = js.getString("iscopied");
                                    String ispriceoverridden = js.getString("ispriceoverridden");
                                    String isproductoverridden = js.getString("isproductoverridden");
                                    String lineitemnumber = js.getString("lineitemnumber");
                                    String manualdiscountamount = js.getString("manualdiscountamount");
                                    String manualdiscountamountbase = js.getString("manualdiscountamountbase");
                                    String priceperunit = js.getString("priceperunit");
                                    String priceperunitbase = js.getString("priceperunitbase");
                                    String productdescription = js.getString("productdescription");
                                    String productname = js.getString("productname");
                                    String productid = js.getString("productid");
                                    String quantity = js.getString("quantity");
                                    String quantitybackordered = js.getString("quantitybackordered");
                                    String quantitycancelled = js.getString("quantitycancelled");
                                    String quantityshipped = js.getString("quantityshipped");
                                    String quantitypicked = js.getString("quantitypicked");
                                    String quantitypacked = js.getString("quantitypacked");
                                    String requestdeliveryby = js.getString("requestdeliveryby");
                                    String salesorderispricelocked = js.getString("salesorderispricelocked");
                                    String shiptoaddressid = js.getString("shiptoaddressid");
                                    String tax = js.getString("tax");
                                    String taxbase = js.getString("taxbase");
                                    String willcall = js.getString("willcall");
                                    String createdon = js.getString("createdon");
                                    String createdby = js.getString("createdby");
                                    String modifiedby = js.getString("modifiedby");
                                    String modifiedbehalfof = js.getString("modifiedbehalfof");
                                    String modifiedon = js.getString("modifiedon");
                                    String orderpid = js.getString("orderpid");
                                    String itemistaxable = js.getString("itemistaxable");
                                    String id = js.getString("id");
                                    String isactive = js.getString("isactive");

                                    sqLiteController.insertInvoiceProduct(
                                             id,
                                             invoiceid,
                                             transactioncurrencyid,
                                             uomid,
                                             baseamount,
                                             exchangerate,
                                             baseamountbase,
                                             description,
                                             extendedamount,
                                             extendedamountbase,
                                             iscopied,
                                             ispriceoverridden,
                                             isproductoverridden,
                                             lineitemnumber,
                                             manualdiscountamount,
                                             manualdiscountamountbase,
                                             "",
                                             "",
                                             "",
                                             priceperunit,
                                             priceperunitbase,
                                             "",
                                             productdescription,
                                             productname,
                                             productid,
                                             quantity,
                                             quantitybackordered,
                                             quantitycancelled,
                                             quantityshipped,
                                             requestdeliveryby,
                                             salesorderispricelocked,
                                             "",
                                             "",
                                             shiptoaddressid,
                                             tax,
                                             taxbase,
                                             "",
                                             "",
                                             willcall,
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             "",
                                             createdby,
                                             "",
                                             createdon,
                                             modifiedby,
                                             modifiedbehalfof,
                                             modifiedon,
                                             "",
                                             "",
                                             "",
                                             "",
                                             isactive,
                                             quantitypicked,
                                             quantitypacked,
                                             orderpid,
                                             "",
                                             "",
                                             itemistaxable,"","");

                                }
                            } finally {
                                Log.d("MasterAllTable","Create");
                                sqLiteController.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Progress_dialog != null) {
                                            if (Progress_dialog.isShowing()) {
                                                Progress_dialog.dismiss();
                                            }
                                        }
                                        showSuccess();
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
    private void showSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySignIn.this);
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
        tittleAlert_tv.setText("Welcome Login Successful");
        btn_Okalrt.setText("Continue");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySignIn.this, Activity_OrdersHistory.class));
                dialog.dismiss();
            }
        });
    }
    private void GetCancelledReason(String Id_,String JWTEncoded) {
        try {
            App.getInstance().GetCancelReason(Id_,JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Failure
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
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray options = jsonObject.getJSONArray("options");
                                    if (options.length() > 0) {
                                        for (int i=0; i<options.length(); i++) {
                                            JSONObject js = options.getJSONObject(i);
                                            SQLiteController sqLiteController_cancel = new SQLiteController(ActivitySignIn.this);
                                            sqLiteController_cancel.open();
                                            try {
                                                sqLiteController_cancel.insertCancelledReason(js.getString("id"),js.getString("displayname"));
                                            } finally {
                                                sqLiteController_cancel.close();

                                            }
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
    private void GetUserDetails(String Id_,String JWTEncoded) {
        try {
            App.getInstance().GetUserDetails(Id_,JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Failure
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONArray jr = new JSONArray(res);
                                    final JSONObject js = jr.getJSONObject(0);
                                    boolean isactive = js.getBoolean("isactive");
                                    final String userid = js.getString("userid");
                                    final String firstname = js.getString("firstname");
                                    final String lastname = js.getString("lastname");
                                    final String emailid = js.getString("emailid");
                                    final String userimage = js.getString("userimage");
                                    if ( isactive == true) {
                                        SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
                                        sqLiteController.open();
                                        try {
                                            sqLiteController.insertUser(userid,firstname+lastname,JWTEncoded,emailid);
                                        } finally {
                                            sqLiteController.close();

                                            System.out.println("DataFlag---"+ DataFlag);

                                            if(DataFlag.equals("New")){
                                                Get_OrderList(JWTEncoded);
                                            }else {
                                                Utils.getInstance().GetMasterInsert(ActivitySignIn.this,"Login",Progress_dialog,"");

                                               // GetOldOrderInsert(JWTEncoded);
                                            }

                                        }
                                    }
                                    else {
                                        Toast.makeText(ActivitySignIn.this, "The username and password is incorrect. Please try again!", Toast.LENGTH_SHORT).show();
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
    private void GetSalesRep(String JWTEncoded) {
        try {
            App.getInstance().GetSalesRep(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(res);
                                    if (jsonArray.length() > 0) {
                                        for (int i=0; i<jsonArray.length(); i++) {
                                            JSONObject js = jsonArray.getJSONObject(i);
                                            String id= js.getString("id");
                                            String salesrep= js.getString("salesrep");
                                            SQLiteController sqlsales_v = new SQLiteController(ActivitySignIn.this);
                                            sqlsales_v.open();
                                            try {
                                                sqlsales_v.insertSalesRep(id,salesrep);
                                            } finally {
                                                sqlsales_v.close();
                                            }
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
    private void GetDeliveryRep(String JWTEncoded) {
        try {
            App.getInstance().GetDeliveryRep(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    if (data.length() > 0) {
                                        for (int i=0; i<data.length(); i++) {
                                            JSONObject js = data.getJSONObject(i);
                                            String firstName= js.getString("firstName");
                                            String lastName= js.getString("lastName");
                                            String id= js.getString("id");
                                            String email= js.getString("email");
                                            SQLiteController sqlsales_dd = new SQLiteController(ActivitySignIn.this);
                                            sqlsales_dd.open();
                                            try {
                                                sqlsales_dd.insertDeliveryRep(id,firstName+" "+lastName,email);
                                            } finally {
                                                sqlsales_dd.close();
                                            }
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
    private void GetFulfilment(String JWTEncoded) {
        try {
            App.getInstance().GetFulfilment(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Failure
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray options = jsonObject.getJSONArray("options");
                            if (options.length() > 0) {
                                for (int i=0; i<options.length(); i++) {
                                    JSONObject js = options.getJSONObject(i);
                                    String id= js.getString("id");
                                    String displayname= js.getString("displayname");
                                    String FullIsDefault= js.getString("isdefault");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            SQLiteController sqlfulfill = new SQLiteController(ActivitySignIn.this);
                                            sqlfulfill.open();
                                            try {
                                                sqlfulfill.insertFulfillment(id,displayname,FullIsDefault);
                                            } finally {
                                                sqlfulfill.close();
                                            }
                                        }
                                    });
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
    private void GetWarehouse(String JWTEncoded) {
        try {
            App.getInstance().GetWarehouse(JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Failure
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
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray options = jsonObject.getJSONArray("data");
                                    if (options.length() > 0) {
                                        for (int i=0; i<options.length(); i++) {
                                            JSONObject js = options.getJSONObject(i);
                                            String id= js.getString("id");
                                            String warehousenumber= js.getString("warehousenumber");
                                            String warehousename= js.getString("warehousename");
                                            SQLiteController sqlWarehouse = new SQLiteController(ActivitySignIn.this);
                                            sqlWarehouse.open();
                                            try {
                                                sqlWarehouse.insertWarehouse(id,warehousenumber+"-"+warehousename);
                                            } finally {
                                                sqlWarehouse.close();
                                            }
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
    private void GetAllProduct(String JWTEncoded) {
        try {
            App.getInstance().GetProductList(JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Failure
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("data");
                            System.out.println("ProductsALl"+data);

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject js = data.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("productname");
                                String description = js.getString("description");
                                String productimage1 = js.getString("productimage");
                                String price ="0";
                                if (!js.getString("price").equals("null")) {
                                    price = js.getString("price");
                                }else {
                                    price = "0";
                                }
                                String status = js.getString("status");
                                String upccode = js.getString("upccode");
                                String istaxable = js.getString("istaxable");
                                try {
                                    JSONArray data2 = js.getJSONArray("digitalassetsinfo");
                                    for (int k = 0; k < data2.length(); k++) {
                                        JSONObject js1 = data2.getJSONObject(k);
                                        String isdefault = js1.getString("isdefault");
                                        if (!isdefault.equals("null")) {
                                            if(isdefault.equals("true")){
                                                String productimage = js1.getString("productimage");
                                                String finalPrice = price;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        SQLiteController sqlproduct = new SQLiteController(ActivitySignIn.this);
                                                        sqlproduct.open();
                                                        try {
                                                            sqlproduct.insertALlProduct(id,productnumber,productname, finalPrice,status, productimage,upccode,istaxable,description);
                                                        } finally {
                                                            sqlproduct.close();
                                                        }
                                                    }
                                                });
                                            }
                                        }else {
                                            String finalPrice1 = price;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SQLiteController sqlproduct = new SQLiteController(ActivitySignIn.this);
                                                    sqlproduct.open();
                                                    try {
                                                        sqlproduct.insertALlProduct(id,productnumber,productname, finalPrice1,status, "null",upccode,istaxable,description);
                                                    } finally {
                                                        sqlproduct.close();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                    String finalPrice2 = price;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SQLiteController sqlproduct = new SQLiteController(ActivitySignIn.this);
                                            sqlproduct.open();
                                            try {
                                                sqlproduct.insertALlProduct(id,productnumber,productname, finalPrice2,status, productimage1,upccode,istaxable,description);
                                            } finally {
                                                sqlproduct.close();
                                            }
                                        }
                                    });
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

    private void GetPDFFormat(String JWTEncoded) {
        try {
            App.getInstance().GetPDFFormat(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONArray jr = new JSONArray(res);
                            SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
                            sqLiteController.open();
                            try {
                                for (int i = 0; i < jr.length(); i++) {
                                    JSONObject jb1 = jr.getJSONObject(i);
                                    String id = jb1.getString("id");
                                    String settingsname = jb1.getString("settingsname");
                                    String settingsvalue = jb1.getString("settingsvalue");
                                    sqLiteController.insertInvoiceFormat(id,settingsname,settingsvalue);
                                }
                            } finally {
                                sqLiteController.close();
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
    private void GetCurrency(String JWTEncoded) {
        try {
            App.getInstance().GetCurrency(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jr = new JSONObject(res);
                            SQLiteController sqLiteController = new SQLiteController(ActivitySignIn.this);
                            sqLiteController.open();
                            try {
                                String id = jr.getString("id");
                                String name = jr.getString("name");
                                String currencysymbol = jr.getString("currencysymbol");
                                String isocurrencycode = jr.getString("isocurrencycode");
                                sqLiteController.insertCurrency(id,name,currencysymbol,isocurrencycode);
                            } finally {
                                sqLiteController.close();
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
    private void GetCusState(String JWTEncoded) {
        try {
            App.getInstance().GetCustomerState(JWTEncoded,new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String resw = response.body().string();
                        System.out.println("GetCustomerState---"+resw);
                        try {
                            JSONArray jsonArray = new JSONArray(resw);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    String Cs_id = js.getString("id");
                                    String shippstateid = js.getString("shippstateid");
                                    String istaxable = js.getString("istaxable");
                                    String taxid = js.getString("taxid");
                                    String nettermsid = js.getString("nettermsid");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SQLiteController sqlStateCus= new SQLiteController(ActivitySignIn.this);
                                            sqlStateCus.open();
                                            try {
                                                sqlStateCus.insertCusState(Cs_id,shippstateid,istaxable,taxid,nettermsid);
                                            } finally {
                                                sqlStateCus.close();
                                            }
                                        }
                                    });

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
    private void GetCusTax(String JWTEncoded) {
        try {
            App.getInstance().GetCustomerTaxall(JWTEncoded,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(res);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    String id = js.getString("id");
                                    String taxpercentage = js.getString("taxpercentage");
                                    String stateid = js.getString("stateid");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SQLiteController sqltax= new SQLiteController(ActivitySignIn.this);
                                            sqltax.open();
                                            try {
                                                sqltax.insertCusTax(id,taxpercentage,stateid);
                                            } finally {
                                                sqltax.close();
                                            }
                                        }
                                    });
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
    private void GetTerms(String JWTEncoded) {
        try {
            App.getInstance().GetTermsDetails(JWTEncoded,new Callback(){

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
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    String id = js.getString("id");
                                    String termname = js.getString("termname");
                                    String netduedays = js.getString("netduedays");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SQLiteController sqlTerms= new SQLiteController(ActivitySignIn.this);
                                            sqlTerms.open();
                                            try {
                                                sqlTerms.insertTerms(id,termname,netduedays);
                                            } finally {
                                                sqlTerms.close();
                                            }
                                        }
                                    });
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
}

