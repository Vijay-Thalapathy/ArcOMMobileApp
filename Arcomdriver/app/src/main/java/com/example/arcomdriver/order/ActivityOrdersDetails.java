package com.example.arcomdriver.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.payments.Activity_CollectPayments;
import com.example.arcomdriver.payments.Activity_Deliver;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_InvoiceList;
import com.example.arcomdriver.adapter.Adapter_NotesList;
import com.example.arcomdriver.adapter.Adapter_SummaryList;
import com.example.arcomdriver.adapter.ImageViewAdapter;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_InvoiceList;
import com.example.arcomdriver.model.Model_InvoiceProductItems;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.payments.Activity_OrderDelivery;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
public class ActivityOrdersDetails extends Activity_Menu {

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentStatePagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public static  String TAG="ActivityOrdersTab";
    public static String poreferencenum,ShipmentStatus,memo,termsconditions,shipnote,deliverynote,billtoaddressid,shiptoaddressid,totaltax,totaltaxbase,freightamount,discountamount,discountpercentage,totallineitemamount,TotalPaymentAmt,invoiceid="null",tr_fulfillmentstatus="N/A",deliveryshipmenttypeid,deliverywarehouseid,tr_packed,tr_picked,tr_status,tr_submitdate,token,str_reason,user_email,user_id,salesrep_name,salesrepid,pricing_date,delivery_date,OrderId,CustomerName,ordernumber,totalamount,submitstatus,date_order,Invoicesubmitstatus,datefulfilled,status;
    private SQLiteController sqLiteController;
    private AppCompatTextView odCustomerName_tv,odStatus_tv,odDate_tv,odDeliveryDate_tv,odAmt_tv;
    private AppCompatSpinner cancelReason_sp;
    private ArrayList<String> arReasonID;
    private ArrayList<String> arReasonName;

    ArrayList<Model_InvoiceProductItems> listInvoice = new ArrayList<>();

    public static String APIInvoiceHTML ="null";
    public static String APIOrderHTML ="null";

    String invoiceFalg ="NotAvailable";

    //Toolbar toolbar;
    String RowOrderHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";

    String Newinvoiceid,Incustomeristaxable,Induedate,Innetterms,Intaxid,Inwarehouseid,Inmemo,Intermsconditions,Inshipnote,Indeliverynote,Indatefulfilled,Insalesrepid,Intaxperecentage,Intax,Inbilltoaddressid,Incustomerid,Indiscountamount,Indiscountpercentage,Inshiptoaddressid,Intotalamount,Intotallineitemamount,Infreightamount;
    private ArrayList<Model_ItemList> Collectlist = new ArrayList<>();

    public static AlertDialog Progress_dialog;

    public static String Companylogo;
    SharedPreferences InSharedPreferences;

    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_orderstab);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_orderstab, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Companylogo =getCompanyLogo();

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrdersDetails.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivityOrdersDetails.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

        //odId_tv = findViewById(R.id.odId_tv);
        //toolbar = findViewById(R.id.toolbar);
        odCustomerName_tv = findViewById(R.id.odCustomerName_tv);
        odStatus_tv = findViewById(R.id.odStatus_tv);
        odDate_tv = findViewById(R.id.odDate_tv);
        odDeliveryDate_tv = findViewById(R.id.odDeliveryDate_tv);
        odAmt_tv = findViewById(R.id.odAmt_tv);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            OrderId = extras.getString("OrderId");
            System.out.println("OrderId--"+OrderId);
            CustomerName = extras.getString("CustomerName");

            sqLiteController = new SQLiteController(this);
            sqLiteController.open();
            try {
                long count = sqLiteController.fetchCount();
                if (count > 0) {
                    //Order
                    Cursor order_c = sqLiteController.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderId);
                    if (order_c != null && order_c.moveToFirst()) {
                        do {
                            ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                            totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                            String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                            submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                            String datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                            String pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                            salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                            totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                            discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                            discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                            freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                            totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                            totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                            shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                            billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                            deliverynote = order_c.getString(order_c.getColumnIndex("deliverynote"));
                            shipnote = order_c.getString(order_c.getColumnIndex("shipnote"));
                            termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));
                            memo = order_c.getString(order_c.getColumnIndex("memo"));
                            poreferencenum = order_c.getString(order_c.getColumnIndex("poreferencenum"));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(submitstatus.equals("New Order")){
                                        Od_edit_ic.setVisibility(View.VISIBLE);
                                    }else if(submitstatus.equals("Confirmed")){
                                        Od_edit_ic.setVisibility(View.GONE);
                                    }else{
                                        Od_edit_ic.setVisibility(View.GONE);
                                    }

                                    if(ordernumber.startsWith("DRFOM")){
                                        txt_page.setText(ordernumber);
                                    }else {
                                        txt_page.setText("SO "+ordernumber);
                                    }

                                    odCustomerName_tv.setText(CustomerName);
                                    odStatus_tv.setText(submitstatus);
                                    odAmt_tv.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Double.valueOf(totalamount)));

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date = sdf.parse(submitdate);
                                        date1 = sdf.parse(datefulfilled);
                                        date2 = sdf.parse(pricingdate);
                                        date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                        delivery_date =new SimpleDateFormat("MM/dd/yyyy").format(date1);
                                        pricing_date =new SimpleDateFormat("MM/dd/yyyy").format(date2);
                                        odDate_tv.setText(date_order);
                                        odDeliveryDate_tv.setText(delivery_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });





                        } while (order_c.moveToNext());
                    }

                    Cursor sales_c = sqLiteController.readTableSalesRep();
                    if (sales_c != null && sales_c.moveToFirst()) {
                        do {
                            String salesrep_id = sales_c.getString(sales_c.getColumnIndex("salesrep_id"));

                            if(salesrep_id.equals(salesrepid)){
                                salesrep_name = sales_c.getString(sales_c.getColumnIndex("salesrep_name"));

                            }

                        } while (sales_c.moveToNext());
                    }

                    //user
                    Cursor user_c = sqLiteController.readTableUser();
                    if (user_c != null && user_c.moveToFirst()) {
                        @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                         user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                         token = user_c.getString(user_c.getColumnIndex("token"));
                         user_email = user_c.getString(user_c.getColumnIndex("Email"));

                    }

                    //CancelledReason
                    SQLiteController sqLiteController = new SQLiteController(ActivityOrdersDetails.this);
                    sqLiteController.open();
                    try {
                        long fetchCancelReasonCount = sqLiteController.fetchCancelReasonCount();
                        if (fetchCancelReasonCount > 0) {
                            arReasonName = new ArrayList<String>();
                            arReasonID = new ArrayList<String>();

                            Cursor CancelledReason_c = sqLiteController.readTableCancelledReason();
                            if (CancelledReason_c.moveToFirst()) {
                                do {
                                    arReasonName.add(CancelledReason_c.getString(CancelledReason_c.getColumnIndex("cancelreason_name")));
                                    arReasonID.add(CancelledReason_c.getString(CancelledReason_c.getColumnIndex("cancelreason_id")));
                                } while (CancelledReason_c.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteController.close();
                    }

                }
            } finally {
                sqLiteController.close();

             //   getOrderDetails(OrderId);
            }

        }

        //OrdersSpinner
        SQLiteController sqLiteControllerC = new SQLiteController(ActivityOrdersDetails.this);
        sqLiteControllerC.open();
        try {
            long fetchAddressCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchAddressCount > 0) {
                Cursor C_Address = sqLiteControllerC.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("InvoiceTemplatePreview")){

                            APIInvoiceHTML  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));

                        }

                        if(invoice_name.equals("PresaleOrderTemplate")){

                            APIOrderHTML  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));

                        }



                    } while (C_Address.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();
        }


        mViewPager = findViewById(R.id.fragment_container);
        tabLayout = findViewById(R.id.tabs);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabLayout.setVisibility(View.VISIBLE);
        mSectionsPagerAdapter.addFragment(new NotesFragment());
        mSectionsPagerAdapter.addFragment(new TrackOrderFragment());
        mSectionsPagerAdapter.addFragment(new SummaryFragment());
       // mSectionsPagerAdapter.addFragment(new FulfillmentFragment());
        mSectionsPagerAdapter.addFragment(new InvoiceFragment());
        mSectionsPagerAdapter.addFragment(new PaymentFragment());
        mSectionsPagerAdapter.addFragment(new DeliveryFragment());
      //  mSectionsPagerAdapter.addFragment(new AssociateOrderFragment());
        //Set adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);



        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityOrdersDetails.this, com.example.arcomdriver.order.Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });*/

        SQLiteController sqLiteController_ship = new SQLiteController(ActivityOrdersDetails.this);
        sqLiteController_ship.open();
        try {
            long count = sqLiteController_ship.fetchOrderCount();
            if (count > 0) {
                Cursor order_c = sqLiteController_ship.readShipTableItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ORDER_ID,OrderId);
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        ShipmentStatus = order_c.getString(order_c.getColumnIndex("fulfillmentstatus"));

                        System.out.println("ShipmentStatus-----"+ShipmentStatus);

                    } while (order_c.moveToNext());
                }

                if(order_c.getCount() == 0){

                }
            }

        } finally {
            sqLiteController_ship.close();
        }



        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityOrdersDetails.this, Activity_OrdersHistory.class));
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityOrdersDetails.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityOrdersDetails.this, Activity_InvoiceHistory.class));
            }
        });


        findViewById(R.id.Od_menu_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ActivityOrdersDetails.this, v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.popup_ordersetailsmenu, popupMenu.getMenu());
                Menu popMenu = popupMenu.getMenu();
                if(ordernumber.startsWith("DRFOM")){

                    popMenu.findItem(R.id.cancel_orderDetails).setVisible(false);
                    popMenu.findItem(R.id.OrderDeliver).setVisible(false);
                    popMenu.findItem(R.id.generate_orders).setVisible(false);
                    popMenu.findItem(R.id.collectPayments).setVisible(false);

                }else {

                    if (submitstatus.equals("Cancelled")||submitstatus.equals("Fulfilled")){
                        popMenu.findItem(R.id.cancel_orderDetails).setVisible(false);
                        popMenu.findItem(R.id.OrderDeliver).setVisible(false);
                        popMenu.findItem(R.id.generate_orders).setVisible(false);
                        popMenu.findItem(R.id.collectPayments).setVisible(false);
                    }else {

                        SQLiteController sqLiteController2 = new SQLiteController(ActivityOrdersDetails.this);
                        sqLiteController2.open();
                        try {
                            long count = sqLiteController2.fetchOrderCount();
                            if (count > 0) {
                                Cursor order_c = sqLiteController2.readOrderItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_ORDER_ID,OrderId);
                                //Cursor order_c = sqLiteController2.readShipTableItem(DbHandler.TABLE_SHIPMENT,DbHandler.SHIP_ORDER_ID,OrderId);
                                if (order_c != null && order_c.moveToFirst()) {
                                    do {
                                        invoiceFalg ="Available";

                                        Invoicesubmitstatus = order_c.getString(order_c.getColumnIndex("Insubmitstatus"));
                                        //invoiceid = order_c.getString(order_c.getColumnIndex("id"));
                                        //String Innumber = order_c.getString(order_c.getColumnIndex("ordernumber"));

                                        if(Invoicesubmitstatus.equals("Payment pending")){
                                            popMenu.findItem(R.id.OrderDeliver).setVisible(true);
                                            popMenu.findItem(R.id.generate_orders).setVisible(false);
                                            popMenu.findItem(R.id.collectPayments).setVisible(false);
                                        }else if(Invoicesubmitstatus.equals("Paid")){
                                            popMenu.findItem(R.id.OrderDeliver).setVisible(false);
                                            popMenu.findItem(R.id.generate_orders).setVisible(false);
                                            popMenu.findItem(R.id.collectPayments).setVisible(false);
                                        }

                                    } while (order_c.moveToNext());
                                }

                                if(order_c.getCount() == 0){
                                    invoiceFalg ="NotAvailable";
                                    popMenu.findItem(R.id.generate_orders).setVisible(true);
                                    popMenu.findItem(R.id.collectPayments).setVisible(false);
                                }
                            }

                        } finally {
                            sqLiteController2.close();
                        }


                        if(!(ShipmentStatus ==null)){
                            if(ShipmentStatus.equals("Shipped")){
                                popMenu.findItem(R.id.OrderDeliver).setVisible(false);
                                popMenu.findItem(R.id.generate_orders).setVisible(false);
                                popMenu.findItem(R.id.collectPayments).setVisible(true);
                            }

                        }


                    }

                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (menuItem.getItemId()) {
                            case R.id.cancel_orderDetails:
                                CancelAlert(OrderId);
                                return true;

                            case R.id.generate_orders:
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("In_Flag","1");
                                bundle1.putString("In_OrderID",OrderId);
                                Intent in1 = new Intent(ActivityOrdersDetails.this, Activity_CreateInvoice.class);
                                in1.putExtras(bundle1);
                                startActivity(in1);
                                return true;
                            case R.id.Add_note:
                                AddNoteAlert(OrderId);
                                return true;

                                case R.id.new_pdf:

                                    CreateOrderPDF();

                                return true;

                            case R.id.collectPayments:

                                System.out.println("invoiceid---"+invoiceid);
                                System.out.println("OrderId---"+OrderId);
                                System.out.println("invoiceFalg---> "+invoiceFalg);

                                if(invoiceid.equals("null")) {
                                    showAlert();
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Newinvoiceid",invoiceid);
                                    bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                                    bundle.putString("OrderID",OrderId);
                                    bundle.putString("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                    bundle.putString("deliverywarehouseid",deliverywarehouseid);
                                    Intent in = new Intent(ActivityOrdersDetails.this, Activity_CollectPayments.class);
                                    in.putExtras(bundle);
                                    startActivity(in);
                                }

                                return true;

                            case R.id.OrderDeliver:

                                System.out.println("invoiceid---"+invoiceid);
                                System.out.println("OrderId---"+OrderId);
                                System.out.println("invoiceFalg---> "+invoiceFalg);


                                if(invoiceFalg.equals("NotAvailable")){
                                    Bundle bundlee = new Bundle();
                                    bundlee.putString("TotalPaymentAmt",TotalPaymentAmt);
                                    bundlee.putString("OrderID",OrderId);
                                    bundlee.putString("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                    bundlee.putString("deliverywarehouseid",deliverywarehouseid);
                                    Intent ine = new Intent(ActivityOrdersDetails.this, Activity_OrderDelivery.class);
                                    ine.putExtras(bundlee);
                                    startActivity(ine);

                                }else {
                                    Bundle bundlee = new Bundle();
                                    bundlee.putString("InvoiceID",invoiceid);
                                    bundlee.putString("TotalPaymentAmt",TotalPaymentAmt);
                                    bundlee.putString("OrderID",OrderId);
                                    bundlee.putString("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                    bundlee.putString("deliverywarehouseid",deliverywarehouseid);
                                    bundlee.putString("fulfillmentstatus",ShipmentStatus);
                                    Intent ine = new Intent(ActivityOrdersDetails.this, Activity_Deliver.class);
                                    ine.putExtras(bundlee);
                                    startActivity(ine);
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
        findViewById(R.id.Od_edit_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","1");
                bundle.putString("OrderId",OrderId);
                bundle.putString("CustomerName",CustomerName);
                Intent in = new Intent(ActivityOrdersDetails.this, Activity_UpdateOrder.class);
                in.putExtras(bundle);
                startActivity(in);
            }
        });



      RepaceString();
    }

    @SuppressLint("Range")
    private void RepaceString() {
        SQLiteController sqLiteController_h = new SQLiteController(this);
        sqLiteController_h.open();
        try {
            long count = sqLiteController_h.fetchCount();
            if (count > 0) {
                System.out.println("OrderId--"+OrderId);
                //Order
                Cursor order_c = sqLiteController_h.readTableItem(DbHandler.TABLE_ORDER,DbHandler.ORDER_ID,OrderId);
                if (order_c != null && order_c.moveToFirst()) {
                    do {
                        String ordernumber = order_c.getString(order_c.getColumnIndex("ordernumber"));
                        String totalamount = order_c.getString(order_c.getColumnIndex("totalamount"));
                        String submitdate = order_c.getString(order_c.getColumnIndex("submitdate"));
                        String submitstatus = order_c.getString(order_c.getColumnIndex("submitstatus"));
                        String datefulfilled = order_c.getString(order_c.getColumnIndex("datefulfilled"));
                        String pricingdate = order_c.getString(order_c.getColumnIndex("pricingdate"));
                        String  salesrepid = order_c.getString(order_c.getColumnIndex("salesrepid"));
                        String id = order_c.getString(order_c.getColumnIndex("order_id"));
                        String billtoaddressid = order_c.getString(order_c.getColumnIndex("billtoaddressid"));
                        String shiptoaddressid = order_c.getString(order_c.getColumnIndex("shiptoaddressid"));
                        String totallineitemamount = order_c.getString(order_c.getColumnIndex("totallineitemamount"));
                        String freightamount = order_c.getString(order_c.getColumnIndex("freightamount"));
                        String totaltaxbase = order_c.getString(order_c.getColumnIndex("totaltaxbase"));
                        String totaltax = order_c.getString(order_c.getColumnIndex("totaltax"));
                        String discountpercentage = order_c.getString(order_c.getColumnIndex("discountpercentage"));
                        String discountamountbase = order_c.getString(order_c.getColumnIndex("discountamountbase"));
                        String totalamount1 = order_c.getString(order_c.getColumnIndex("totalamount"));
                        String freightamountbase = order_c.getString(order_c.getColumnIndex("freightamountbase"));
                        String discountamount = order_c.getString(order_c.getColumnIndex("discountamount"));
                        String termsconditions = order_c.getString(order_c.getColumnIndex("termsconditions"));

                        String Outpur_Str = Utils.convertToIndianCurrency(totalamount);
                        Log.i("Outpur_Str", Outpur_Str);
                        APIOrderHTML = APIOrderHTML.replace("$totalUnit$", "1");
                        APIOrderHTML = APIOrderHTML.replace("$SubTotal$", Activity_OrdersHistory.currency_symbol+totallineitemamount);
                        APIOrderHTML = APIOrderHTML.replace("$DiscountPercentage$", "("+discountpercentage+" % )");
                        APIOrderHTML = APIOrderHTML.replace("$DiscountAmount$", Activity_OrdersHistory.currency_symbol+discountamount);
                        APIOrderHTML = APIOrderHTML.replace("$ShippingAmount$", Activity_OrdersHistory.currency_symbol+freightamount);
                        APIOrderHTML = APIOrderHTML.replace("$TaxPercentage$", "("+totaltaxbase+" % )");
                        APIOrderHTML = APIOrderHTML.replace("$TaxAmount$", Activity_OrdersHistory.currency_symbol+totaltax);
                        APIOrderHTML = APIOrderHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamount1);
                        APIOrderHTML = APIOrderHTML.replace("$TermsandConditions$", termsconditions);
                        APIOrderHTML = APIOrderHTML.replace("$TotalAmountInWords$", Outpur_Str);

                        Cursor C_Address = sqLiteController_h.readTableAddress();
                        if (C_Address.moveToFirst()) {
                            do {
                                String BId = C_Address.getString(C_Address.getColumnIndex("address_id"));
                                String line2 = C_Address.getString(C_Address.getColumnIndex("line2"));
                                String stateorprovince = C_Address.getString(C_Address.getColumnIndex("stateorprovince"));
                                String city = C_Address.getString(C_Address.getColumnIndex("city"));
                                String postalcode = C_Address.getString(C_Address.getColumnIndex("postalcode"));
                                String country = C_Address.getString(C_Address.getColumnIndex("country"));




                                if(billtoaddressid.equals(BId)){

                                    APIOrderHTML = APIOrderHTML.replace("$BillingAddress$", line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                }

                                if(shiptoaddressid.equals(BId)){
                                    APIOrderHTML = APIOrderHTML.replace("$ShippingAddress$", line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                }



                            } while (C_Address.moveToNext());
                        }

                        APIOrderHTML = APIOrderHTML.replace("$PresaleOrderNumber$", " SO "+ordernumber);
                        APIOrderHTML = APIOrderHTML.replace("$TotalAmount$", Activity_OrdersHistory.currency_symbol+totalamount);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date date = null;
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date = sdf.parse(submitdate);
                                    date1 = sdf.parse(datefulfilled);
                                    date2 = sdf.parse(pricingdate);
                                    String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                    String delivery_date =new SimpleDateFormat("MM/dd/yyyy").format(date1);
                                    String pricing_date =new SimpleDateFormat("MM/dd/yyyy").format(date2);

                                    APIOrderHTML = APIOrderHTML.replace("$PresaleOrderNumber$", ordernumber);
                                    APIOrderHTML = APIOrderHTML.replace("$OrderDate$", date_order);
                                    APIOrderHTML = APIOrderHTML.replace("$DeliveryDate$", delivery_date);
                                    APIOrderHTML = APIOrderHTML.replace("$ReferencePONumber$", "0");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }






                        });



                    } while (order_c.moveToNext());
                }


                Cursor Product_c = sqLiteController_h.readProductJoinTables(OrderId);

                if (Product_c != null && Product_c.moveToFirst()) {
                    if (listInvoice.size() > 0) {
                        listInvoice.clear();
                    }
                    do {
                        @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                       // @SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                        @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                        @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                        @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                        @SuppressLint("Range") String itemistaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));

                       // Cursor product_c1 = sqLiteController_h.readAllTableProduct();
                        Cursor product_c1 = sqLiteController_h.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);

                        if (product_c1 != null && product_c1.moveToFirst()) {
                            do {

                                @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                if(product_id.equals(productid)){
                                   // @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                    @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                    @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                    @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                    System.out.println("all_upsc"+all_upsc);

                                    listInvoice.add(new Model_InvoiceProductItems(product_name__, quantity, priceperunit, baseamount,itemistaxable,all_upsc,all_description));


                                }

                            } while (product_c1.moveToNext());
                        }




                    } while (Product_c.moveToNext());
                }
            }
        } finally {
            sqLiteController_h.close();
        }

    }

    private void CreateOrderPDF() {
        System.out.println("CreateOrderPDF---");

        String localRowhtml = "";

        for (int i = 0; i < listInvoice.size(); i++) {

            localRowhtml += RowOrderHtml;

            if(listInvoice.get(i).getAll_upsc().equals("null")){
                localRowhtml = localRowhtml.replace("prodname", listInvoice.get(i).getProduct_name());

            }else {
                localRowhtml = localRowhtml.replace("prodname", listInvoice.get(i).getProduct_name()+"\n- "+listInvoice.get(i).getAll_upsc());

            }


            localRowhtml = localRowhtml.replace("UPC Code", listInvoice.get(i).getAll_description());
            localRowhtml = localRowhtml.replace("price", listInvoice.get(i).getProduct_price());
            localRowhtml = localRowhtml.replace("Qty", listInvoice.get(i).getProduct_quantity());

            if (Activity_OrdersHistory.tax.equals("1")){
                if(listInvoice.get(i).getItemistaxable().equals("true")){
                    localRowhtml = localRowhtml.replace("Tax", "Taxable");

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                }
            }else {
                localRowhtml = localRowhtml.replace("Tax", "-");

            }


            localRowhtml = localRowhtml.replace("amount", listInvoice.get(i).getPrice_PerUnit());


        }

       // localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

        APIOrderHTML = APIOrderHTML.replace("$OrderProduct$", localRowhtml);

        APIOrderHTML=APIOrderHTML.replace("$CompanyLogo$",Companylogo);

        FileManager.getInstance().cleanTempFolder(getApplicationContext());
        // Create Temp File to save Pdf To
        final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
        // Generate Pdf From Html


        PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +APIOrderHTML+
                "</body>\n" +
                "</html>", new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                // Open Pdf Viewer
                Uri pdfUri = Uri.fromFile(savedPDFFile);

                Bundle bundle = new Bundle();
                bundle.putString("Order_flag","1");
                Intent intentPdfViewer = new Intent(ActivityOrdersDetails.this, PdfViewerActivity.class);
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

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @SuppressLint("ValidFragment")
    public static class NotesFragment extends Fragment {

        private CoordinatorLayout cl;
        private RecyclerView mRecyclerView;
        private LottieAnimationView txt_no_record;
        private Adapter_NotesList adapter_notes;
        private AppCompatEditText et_comments;
        private ArrayList<Model_NotesList> list_notes = new ArrayList<>();

        private static final String TAG = "Notes : ";

        public NotesFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_notes, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            et_comments = view.findViewById(R.id.et_comments);
            mRecyclerView = view.findViewById(R.id.rc_commentsList);
            txt_no_record = view.findViewById(R.id.txt_no_record);

            adapter_notes = new Adapter_NotesList(list_notes);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(adapter_notes);


            view.findViewById(R.id.notes_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.getInstance().hideKeyboard(getActivity());
                    if (Connectivity.isConnected(getActivity()) &&
                            Connectivity.isConnectedFast(getActivity())) {
                        if (Utils.getInstance().checkIsEmpty(et_comments)) {
                            Utils.getInstance().snackBarMessage(v,"Enter the Notes!!");
                        } else {
                            try {
                                JSONObject json = new JSONObject();
                                json.put("email",user_email);
                                json.put("userId",user_id);
                                json.put("messageType","Notes");
                                json.put("data",et_comments.getText().toString());
                                json.put("eventDescription","Notes added");
                                json.put("aggregateId",user_id);
                                json.put("screenId","073664B1-CD21-46A5-8514-6F4BBFBCFEF1");

                                System.out.println("Notes----"+json.toString());
                                postNotes(v,json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }else {
                        Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                    }
                }
            });

            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if(ordernumber.startsWith("DRFOM")){

                }else {
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    App.getInstance().GetNotesList(user_id,token,new Callback(){

                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            if (response.isSuccessful()) {
                                                if (getActivity() != null) {
                                                    requireActivity().runOnUiThread(new Runnable() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        @Override
                                                        public void run() {
                                                            txt_no_record.setVisibility(View.GONE);
                                                        }
                                                    });

                                                }

                                                String res = response.body().string();

                                                try {
                                                    JSONObject jsonObject = new JSONObject(res);
                                                    JSONArray options = jsonObject.getJSONArray("data");
                                                    if(list_notes.size()>0)list_notes.clear();
                                                    if (options.length() > 0) {
                                                        for (int i=0; i<options.length(); i++) {
                                                            JSONObject js = options.getJSONObject(i);
                                                            String id = js.getString("id");
                                                            String aggregateId = js.getString("aggregateId");
                                                            String screenId = js.getString("screenId");
                                                            String data = js.getString("data");
                                                            String displayname = js.getString("displayname");
                                                            String timestamp = js.getString("timestamp");

                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                                            Date date = null;
                                                            try {
                                                                date = sdf.parse(timestamp);
                                                                String date_order =new SimpleDateFormat("MM/dd/yyyy HH:mm a").format(date);

                                                                if (getActivity() != null) {
                                                                    requireActivity().runOnUiThread(new Runnable() {
                                                                        @SuppressLint("NotifyDataSetChanged")
                                                                        @Override
                                                                        public void run() {
                                                                            list_notes.add(new Model_NotesList(id,displayname+" "+date_order, data,displayname));
                                                                            adapter_notes.notifyDataSetChanged();
                                                                            //  Utils.getInstance().dismissDialog();
                                                                        }
                                                                    });

                                                                }


                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }


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
                        });
                    }
                }

            }else {
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            txt_no_record.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }



        }

        private void postNotes(final View v, final JSONObject json) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
                }
            });
            try {
                App.getInstance().PostNotes(json.toString(),token, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        requireActivity().runOnUiThread(new Runnable() {
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
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(res);
                                        boolean succeeded = jsonObject.getBoolean("succeeded");

                                        if ( succeeded == true) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("OrderId",OrderId);
                                            bundle.putString("CustomerName",CustomerName);
                                            Intent in = new Intent(getActivity(), ActivityOrdersDetails.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
                                          Toast.makeText(getActivity(), "Notes has been added Successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                             Toast.makeText(getActivity(), "Error, Please try again later!!", Toast.LENGTH_SHORT).show();
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


    }

    @SuppressLint("ValidFragment")
    public static class TrackOrderFragment extends Fragment {

        private CoordinatorLayout cl;
        private  AppCompatImageView img_packed,img_picked,img_invoice,img_payment,img_fulfillStatus;
        private AppCompatTextView Tr_OrderInvoice_tv,Tr_OrderPayment_tv,Tr_OrderFill_tv,Tr_date_tv,Tr_OrderStatus_tv,Tr_OrderPacked_tv,Tr_OrderPicked_tv;

        private static final String TAG = "TrackOrder : ";

        LinearLayout picked_ll,packed_ll;

        public TrackOrderFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_trackorder, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            Tr_date_tv = view.findViewById(R.id.Tr_date_tv);
            Tr_OrderStatus_tv = view.findViewById(R.id.Tr_OrderStatus_tv);
            Tr_OrderPacked_tv = view.findViewById(R.id.Tr_OrderPacked_tv);
            Tr_OrderPicked_tv = view.findViewById(R.id.Tr_OrderPicked_tv);
            Tr_OrderFill_tv = view.findViewById(R.id.Tr_OrderFill_tv);
            Tr_OrderPayment_tv = view.findViewById(R.id.Tr_OrderPayment_tv);
            Tr_OrderInvoice_tv = view.findViewById(R.id.Tr_OrderInvoice_tv);
            img_invoice = view.findViewById(R.id.img_invoice);
            img_payment = view.findViewById(R.id.img_payment);
            img_fulfillStatus = view.findViewById(R.id.img_fulfillStatus);
            img_picked = view.findViewById(R.id.img_picked);
            img_packed = view.findViewById(R.id.img_packed);
            picked_ll = view.findViewById(R.id.picked_ll);
            packed_ll = view.findViewById(R.id.packed_ll);

            if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                //Hide Warehouse
                picked_ll.setVisibility(View.GONE);
                packed_ll.setVisibility(View.GONE);
            }else {
                //Show Warehouse
                picked_ll.setVisibility(View.VISIBLE);
                packed_ll.setVisibility(View.VISIBLE);
            }


            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if(ordernumber.startsWith("DRFOM")){

                }else {
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
                                try {
                                    App.getInstance().PostPresaleOrder(OrderId,"",token,new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.isSuccessful()) {
                                                Utils.getInstance().dismissDialog();
                                                final String res = response.body().string();
                                                if (getActivity() != null) {
                                                    requireActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                final JSONObject jsonObject = new JSONObject(res);
                                                                JSONObject jsData = jsonObject.getJSONObject("data");
                                                                String submitdate= jsData.getString("submitdate");
                                                                tr_status= jsData.getString("status");
                                                                tr_picked= jsData.getString("picked");
                                                                tr_packed= jsData.getString("packed");
                                                                invoiceid= jsData.getString("invoiceid");
                                                                TotalPaymentAmt= jsData.getString("totalamount");
                                                                datefulfilled= jsData.getString("datefulfilled");
                                                                status= jsData.getString("status");
                                                                System.out.println("Trackinvoiceid---"+invoiceid);
                                                                tr_fulfillmentstatus= jsData.getString("fulfillmentstatus");

                                                                if (jsData.has("shipments") && !jsData.isNull("shipments")) {
                                                                    JSONObject jsShipments = jsData.getJSONObject("shipments");
                                                                    tr_fulfillmentstatus= jsShipments.getString("fulfillmentstatus");
                                                                    deliveryshipmenttypeid= jsShipments.getString("deliveryshipmenttypeid");
                                                                    deliverywarehouseid= jsShipments.getString("warehouseid");
                                                                }

                                                                if (getActivity() != null) {

                                                                    requireActivity().runOnUiThread(new Runnable() {
                                                                        @SuppressLint("UseCompatLoadingForDrawables")
                                                                        @Override
                                                                        public void run() {

                                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                                            Date date = null;

                                                                            try {
                                                                                date = sdf.parse(submitdate);
                                                                                tr_submitdate =new SimpleDateFormat("EEE MMM d, yyyy hh:mm a").format(date);
                                                                            } catch (ParseException e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            Tr_date_tv.setText("Ordered "+tr_submitdate);
                                                                            Tr_OrderStatus_tv.setText("Status :  "+tr_status);
                                                                            Tr_OrderFill_tv.setText(tr_fulfillmentstatus);

                                                                            Tr_OrderPicked_tv.setText("Picked :  ("+tr_picked+")");
                                                                            Tr_OrderPacked_tv.setText("packed :  ("+tr_packed+")");

                                                                            if(tr_picked.equals("6/6")){
                                                                                img_picked.setBackgroundResource(R.drawable.ic_tick);
                                                                            }else {
                                                                                img_picked.setBackgroundResource(R.drawable.ic_pending);
                                                                            }

                                                                            if(tr_packed.equals("6/6")){
                                                                                img_picked.setBackgroundResource(R.drawable.ic_tick);
                                                                            }else {
                                                                                img_packed.setBackgroundResource(R.drawable.ic_pending);
                                                                            }


                                                                            if(tr_fulfillmentstatus.equals("Payment pending")){

                                                                                img_invoice.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_payment.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_fulfillStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));

                                                                              /*  img_invoice.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_payment.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_fulfillStatus.setBackgroundResource(R.drawable.ic_pending);*/


                                                                                Tr_OrderInvoice_tv.setText("Invoice : "+"Not Generated");
                                                                                Tr_OrderPayment_tv.setText("Payment : "+"Unpaid");

                                                                                Tr_OrderFill_tv.setText(tr_fulfillmentstatus);

                                                                            }else if(tr_fulfillmentstatus.equals("Shipped")){

                                                                              /*  img_invoice.setBackgroundResource(R.drawable.ic_tick);
                                                                                img_payment.setBackgroundResource(R.drawable.ic_tick);
                                                                                img_fulfillStatus.setBackgroundResource(R.drawable.ic_tick);*/

                                                                                img_invoice.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));
                                                                                img_payment.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));
                                                                                img_fulfillStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));


                                                                                Tr_OrderInvoice_tv.setText("Invoice : "+"Generated");
                                                                                Tr_OrderPayment_tv.setText("Payment : "+"Paid");

                                                                                Tr_OrderFill_tv.setText(tr_fulfillmentstatus);

                                                                            }else if(tr_fulfillmentstatus.equals("Paid")){

                                                                              /*  img_invoice.setBackgroundResource(R.drawable.ic_tick);
                                                                                img_payment.setBackgroundResource(R.drawable.ic_tick);
                                                                                img_fulfillStatus.setBackgroundResource(R.drawable.ic_tick);*/

                                                                                img_invoice.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));
                                                                                img_payment.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));
                                                                                img_fulfillStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));


                                                                                Tr_OrderInvoice_tv.setText("Invoice : "+"Generated");
                                                                                Tr_OrderPayment_tv.setText("Payment : "+"Paid");

                                                                                Tr_OrderFill_tv.setText(tr_fulfillmentstatus);

                                                                            }else if(tr_fulfillmentstatus.equals("Ready to deliver / ship")){

                                                                               /* img_invoice.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_payment.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_fulfillStatus.setBackgroundResource(R.drawable.ic_tick);*/

                                                                                img_invoice.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_payment.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_fulfillStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tick));

                                                                                Tr_OrderInvoice_tv.setText("Invoice : "+"Not Generated");
                                                                                Tr_OrderPayment_tv.setText("Payment : "+"Unpaid");

                                                                                Tr_OrderFill_tv.setText("Yet to deliver");

                                                                            }else{

                                                                                img_invoice.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_payment.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));
                                                                                img_fulfillStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pending));

                                                                              /*  img_invoice.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_payment.setBackgroundResource(R.drawable.ic_pending);
                                                                                img_fulfillStatus.setBackgroundResource(R.drawable.ic_pending);*/

                                                                                Tr_OrderInvoice_tv.setText("Invoice : "+"Not Generated");
                                                                                Tr_OrderPayment_tv.setText("Payment : "+"Unpaid");

                                                                                Tr_OrderFill_tv.setText("Ordered Status : "+"N/A");

                                                                            }


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

                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }




            }





        }


    }

    @SuppressLint("ValidFragment")
    public static class SummaryFragment extends Fragment {

        private CoordinatorLayout cl;
        private RecyclerView mRecyclerView;
        private AppCompatTextView idMemo_tv,idTerms_tv,idship_tv,idNotes_tv,IdShip_tv,IdBill_tv,OdPayable_amount,Od__Tax,Od_TaxHeads,Od_ShipmentAmt,Od_discount,Od_discountTittle,Od_sub_total,txt_no_record,smOrderDate_tv,smDeliveryDate_tv,smOrderNum_tv,smOrderStatus_tv,smPricing_tv,smSaleRep_tv,smRef_tv;
        private Adapter_SummaryList adapter;
        private ArrayList<Model_SummaryList> list = new ArrayList<>();
        LinearLayout CustomerSTax_ll;

        private static final String TAG = "Summary : ";

        public SummaryFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_summary, container, false);
        }

        @SuppressLint("Range")
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            //txtNoRecord1 = view.findViewById(R.id.txt_no_record);
            mRecyclerView = view.findViewById(R.id.rc_summaryList);
            CustomerSTax_ll = view.findViewById(R.id.CustomerSTax_ll);
            txt_no_record = view.findViewById(R.id.txt_no_record);
            smOrderDate_tv = view.findViewById(R.id.smOrderDate_tv);
            smDeliveryDate_tv = view.findViewById(R.id.smDeliveryDate_tv);
            smOrderNum_tv = view.findViewById(R.id.smOrderNum_tv);
            smOrderStatus_tv = view.findViewById(R.id.smOrderStatus_tv);
            smPricing_tv = view.findViewById(R.id.smPricing_tv);
            smSaleRep_tv = view.findViewById(R.id.smSaleRep_tv);
            smRef_tv = view.findViewById(R.id.smRef_tv);

            if (Activity_OrdersHistory.tax.equals("1")){
                CustomerSTax_ll.setVisibility(View.VISIBLE);
            }else {
                CustomerSTax_ll.setVisibility(View.GONE);
            }


            Od_sub_total = view.findViewById(R.id.Od_sub_total);
            Od_discountTittle = view.findViewById(R.id.Od_discountTittle);
            Od_discount = view.findViewById(R.id.Od_discount);
            Od_ShipmentAmt = view.findViewById(R.id.Od_ShipmentAmt);
            Od_TaxHeads = view.findViewById(R.id.Od_TaxHeads);
            Od__Tax = view.findViewById(R.id.Od__Tax);
            OdPayable_amount = view.findViewById(R.id.OdPayable_amount);
            IdBill_tv = view.findViewById(R.id.IdBill_tv);
            IdShip_tv = view.findViewById(R.id.IdShip_tv);
            idNotes_tv = view.findViewById(R.id.idNotes_tv);
            idship_tv = view.findViewById(R.id.idship_tv);
            idTerms_tv = view.findViewById(R.id.idTerms_tv);
            idMemo_tv = view.findViewById(R.id.idMemo_tv);

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //OrdersSpinner
                    SQLiteController sqLiteControllerC = new SQLiteController(getActivity());
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

                                        if (country.isEmpty()) {
                                            IdBill_tv.setText("No address found!");
                                        }else {
                                            IdBill_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);
                                        }



                                    }

                                    if(shiptoaddressid.equals(BId)){

                                        if (country.isEmpty()) {
                                            IdShip_tv.setText("No address found!");
                                        }else {
                                            IdShip_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);
                                        }



                                    }



                                } while (C_Address.moveToNext());
                            }
                        }
                    } finally {
                        sqLiteControllerC.close();
                    }
                }
            });

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(ordernumber.startsWith("DRFOM")){
                        smOrderNum_tv.setText(ordernumber);
                    }else {
                        smOrderNum_tv.setText("SO "+ordernumber);
                    }

                    smOrderStatus_tv.setText(submitstatus);
                    smOrderDate_tv.setText(date_order);
                    smDeliveryDate_tv.setText(delivery_date);
                    smPricing_tv.setText(pricing_date);
                    smSaleRep_tv.setText(salesrep_name);
                    smRef_tv.setText(poreferencenum);

                    Od_sub_total.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totallineitemamount)));
                    Od_discountTittle.setText("Discount("+discountpercentage+"%)");
                    Od_TaxHeads.setText("Customer Tax("+totaltaxbase+"%)");
                    OdPayable_amount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));
                    Od_discount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(discountamount)));
                    Od_ShipmentAmt.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(freightamount)));
                    Od__Tax.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totaltax)));


                    if(deliverynote.equals("")&&deliverynote.isEmpty()){
                        idNotes_tv.setText("N/A");
                    }else {
                        if(deliverynote.equals("null")){
                            idNotes_tv.setText("N/A");
                        }else {
                            idNotes_tv.setText(deliverynote);
                        }

                    }

                    if(shipnote.equals("")&&shipnote.isEmpty()){
                        idship_tv.setText("N/A");
                    }else {
                        if(shipnote.equals("null")){
                            idship_tv.setText("N/A");
                        }else {
                            idship_tv.setText(shipnote);
                        }
                    }

                    if(termsconditions.equals("")&&termsconditions.isEmpty()){
                        idTerms_tv.setText("N/A");
                    }else {
                        if(termsconditions.equals("null")){
                            idTerms_tv.setText("N/A");
                        }else {
                            idTerms_tv.setText(termsconditions);
                        }
                    }

                    if(memo.equals("")&&memo.isEmpty()){
                        idMemo_tv.setText("N/A");
                    }else {
                        if(memo.equals("null")){
                            idMemo_tv.setText("N/A");
                        }else {
                            idMemo_tv.setText(memo);
                        }

                    }


                }
            });


            adapter = new Adapter_SummaryList(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(adapter);
            if(list.size()>0){
                list.clear();
            }
           /* SQLiteController sqLiteController = new SQLiteController(getActivity());
            sqLiteController.open();
            try {
                long count = sqLiteController.fetchOrderCount();
                if (count > 0) {
                    System.out.println("Product_c---"+OrderId);
                    Cursor Product_c = sqLiteController.readProductJoinTables(OrderId);
                    if (Product_c != null && Product_c.moveToFirst()) {
                        if(list.size()>0){
                           list.clear();
                        }
                        do {
                            @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                            //@SuppressLint("Range") String productname = Product_c.getString(Product_c.getColumnIndex("productname"));
                            @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                            @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                            @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                            @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                            @SuppressLint("Range") String orderid = Product_c.getString(Product_c.getColumnIndex("orderid"));
                            System.out.println("productid"+productid);
                            //product
                            Cursor product_c1 = sqLiteController.readAllTableProduct();
                            if (product_c1 != null && product_c1.moveToFirst()) {
                                String productImage ="null";
                                do {

                                    @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));
                                    SQLiteController sqLiteController1 = new SQLiteController(getActivity());
                                    sqLiteController1.open();
                                    try {
                                        Cursor CursorAssertTable = sqLiteController1.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
                                        if (CursorAssertTable.moveToFirst()) {
                                            do {
                                                @SuppressLint("Range") String Products_assets_default = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_default"));

                                                if(Products_assets_default.equals("true")){
                                                    productImage = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_file"));
                                                }


                                            } while (CursorAssertTable.moveToNext());
                                        }
                                        if(CursorAssertTable.getCount() == 0){
                                            productImage ="null";
                                        }
                                    } finally {
                                        sqLiteController1.close();
                                    }

                                    if(product_id.equals(productid)){
                                        //@SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                        @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                        @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                        @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                        System.out.println("product_name__---"+product_name__);

                                        list.add(new Model_SummaryList(productid, product_name__, productImage,quantity,baseamount,baseamount,priceperunit,istaxable,all_upsc,all_description));

                                        adapter.notifyDataSetChanged();
                                    }

                                } while (product_c1.moveToNext());
                            }

                        } while (Product_c.moveToNext());
                    }


                }
            } finally {
                sqLiteController.close();
            }*/
            SQLiteController sqLiteController = new SQLiteController(getActivity());
            sqLiteController.open();
            try {
                long count = sqLiteController.fetchOrderCount();
                if (count > 0) {
                    Cursor Product_c = sqLiteController.readProductJoinTables(OrderId);
                    if (Product_c != null && Product_c.moveToFirst()) {
                        if(list.size()>0){
                            list.clear();
                        }
                        do {
                            @SuppressLint("Range") String productid = Product_c.getString(Product_c.getColumnIndex("productid"));
                            @SuppressLint("Range") String quantity = Product_c.getString(Product_c.getColumnIndex("quantity"));
                            @SuppressLint("Range") String priceperunit = Product_c.getString(Product_c.getColumnIndex("priceperunit"));
                            @SuppressLint("Range") String baseamount = Product_c.getString(Product_c.getColumnIndex("baseamount"));
                            @SuppressLint("Range") String istaxable = Product_c.getString(Product_c.getColumnIndex("itemistaxable"));
                            @SuppressLint("Range") String orderid = Product_c.getString(Product_c.getColumnIndex("orderid"));
                            System.out.println("productid"+productid);
                            //product
                            Cursor product_c1 = sqLiteController.readOrderItem(DbHandler.TABLE_All_PRODUCT,DbHandler.All_PRODUCT_ID,productid);
                            if (product_c1 != null && product_c1.moveToFirst()) {
                                String productImage ="null";
                                do {
                                    @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));
                                    SQLiteController sqLiteController1 = new SQLiteController(getActivity());
                                    sqLiteController1.open();
                                    try {
                                        Cursor CursorAssertTable = sqLiteController1.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
                                        if (CursorAssertTable.moveToFirst()) {
                                            do {
                                                @SuppressLint("Range") String Products_assets_default = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_default"));
                                                if(Products_assets_default.equals("true")){
                                                    productImage = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_file"));
                                                }
                                            } while (CursorAssertTable.moveToNext());
                                        }
                                        if(CursorAssertTable.getCount() == 0){
                                            productImage ="null";
                                        }
                                    } finally {
                                        sqLiteController1.close();
                                    }
                                    if(product_id.equals(productid)){
                                        @SuppressLint("Range") String product_name__ = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                        @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));
                                        @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));

                                        list.add(new Model_SummaryList(productid, product_name__, productImage,quantity,baseamount,baseamount,priceperunit,istaxable,all_upsc,all_description));
                                        adapter.notifyDataSetChanged();
                                    }
                                } while (product_c1.moveToNext());
                            }
                        } while (Product_c.moveToNext());
                    }


                }
            } finally {
                sqLiteController.close();
            }

        }


    }

    @SuppressLint("ValidFragment")
    public static class FulfillmentFragment extends Fragment {

        private CoordinatorLayout cl;
      //  private RecyclerView mRecyclerView;
        private AppCompatTextView txt_no_record;
       // private Adapter_FulfillList adapter;
       // private ArrayList<Model_FulfillList> list = new ArrayList<>();


        public FulfillmentFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_fulfillment, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            //  txtNoRecord2 = view.findViewById(R.id.txt_no_record);
         /*   mRecyclerView = view.findViewById(R.id.rc_FulfillList);
            txt_no_record = view.findViewById(R.id.txt_no_record);

            adapter = new Adapter_FulfillList(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(adapter);*/


/*

            list.add(new Model_FulfillList("PKG-0001 | S0-0003","04/01/2023", "Invoiced"));
            list.add(new Model_FulfillList("PKG-0001 | S0-0003","04/01/2023", "Packed"));
            list.add(new Model_FulfillList("PKG-0001 | S0-0003","04/01/2023", "Shipped"));

            adapter.notifyDataSetChanged();

*/



        }

    }

    @SuppressLint("ValidFragment")
    public static class InvoiceFragment extends Fragment {


        private CoordinatorLayout cl;
        private RecyclerView mRecyclerView;
        private AppCompatTextView invoiceRefPo_tv,invoiceRep_tv,invoicePayType_tv,invoicePresale_tv,InShip_tv,InBill_tv,txt_InPayable_amount,tvIn_Tax,tvIn_TaxHeads,tvIn_ShipmentAmt,tvIn_discountTittle,tvIn_discount,In_sub_total,invoiceStatus_tv, invoiceID_tv,invoiceDate_tv,invoiceCustomer_tv,invoiceAmt_tv;
        private Adapter_InvoiceList adapter;

        LottieAnimationView txt_noInvoice;
        private ArrayList<Model_InvoiceList> InvoiceProductlist = new ArrayList<>();
        private LinearLayout Invoice_ll,CustomerITax_ll;
        private AppCompatTextView invoiceDeliveryNotes_tv,invoicePackship_tv,invoiceTerms_tv,invoiceMemo_tv;

     String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
                "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
                "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
                "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
                "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";


        public InvoiceFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_invoice, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            invoiceID_tv = view.findViewById(R.id.invoiceID_tv);
            CustomerITax_ll = view.findViewById(R.id.CustomerITax_ll);


            if (Activity_OrdersHistory.tax.equals("1")){
                CustomerITax_ll.setVisibility(View.VISIBLE);
            }else {
                CustomerITax_ll.setVisibility(View.GONE);
            }

            txt_noInvoice = view.findViewById(R.id.txt_noInvoice);
            Invoice_ll = view.findViewById(R.id.Invoice_ll);
            invoiceDate_tv = view.findViewById(R.id.invoiceDate_tv);
            invoiceCustomer_tv = view.findViewById(R.id.invoiceCustomer_tv);
            invoiceAmt_tv = view.findViewById(R.id.invoiceAmt_tv);
            invoiceRep_tv = view.findViewById(R.id.invoiceRep_tv);
            invoiceRefPo_tv = view.findViewById(R.id.invoiceRefPo_tv);
            invoiceStatus_tv = view.findViewById(R.id.invoiceStatus_tv);
            invoicePresale_tv = view.findViewById(R.id.invoicePresale_tv);
            invoicePayType_tv = view.findViewById(R.id.invoicePayType_tv);
            In_sub_total = view.findViewById(R.id.In_sub_total);
            tvIn_discount = view.findViewById(R.id.tvIn_discount);
            tvIn_discountTittle = view.findViewById(R.id.tvIn_discountTittle);
            tvIn_ShipmentAmt = view.findViewById(R.id.tvIn_ShipmentAmt);
            tvIn_TaxHeads = view.findViewById(R.id.tvIn_TaxHeads);
            tvIn_Tax = view.findViewById(R.id.tvIn_Tax);
            txt_InPayable_amount = view.findViewById(R.id.txt_InPayable_amount);
            InBill_tv = view.findViewById(R.id.InBill_tv);
            InShip_tv = view.findViewById(R.id.InShip_tv);
            invoiceDeliveryNotes_tv = view.findViewById(R.id.invoiceDeliveryNotes_tv);
            invoicePackship_tv = view.findViewById(R.id.invoicePackship_tv);
            invoiceTerms_tv = view.findViewById(R.id.invoiceTerms_tv);
            invoiceMemo_tv = view.findViewById(R.id.invoiceMemo_tv);

            mRecyclerView = view.findViewById(R.id.InvoiceProductList);
            //txt_no_record = view.findViewById(R.id.txt_no_record);

            adapter = new Adapter_InvoiceList(InvoiceProductlist);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(adapter);

            view.findViewById(R.id.InvoiceviewImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v, Gravity.RIGHT);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_invoicemenu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                            switch (menuItem.getItemId()) {
                                case R.id.In_download:
                                     CreateInvoicePDF();
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

            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if(ordernumber.startsWith("DRFOM")){

                }else {
                    if (getActivity() != null) {
                        GetInvoiceList(OrderId);
                    }

                }


            }





        }

        private void GetInvoiceList(String Id_) {
            //Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetInvoiceList(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Utils.getInstance().dismissDialog();
                                Invoice_ll.setVisibility(View.GONE);
                                txt_noInvoice.setVisibility(View.VISIBLE);
                            }
                        });

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String res = response.body().string();

                            if (getActivity() != null) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(InvoiceProductlist.size()>0)InvoiceProductlist.clear();
                                        txt_noInvoice.setVisibility(View.GONE);
                                        Invoice_ll.setVisibility(View.VISIBLE);
                                        //Utils.getInstance().dismissDialog();
                                    }
                                });
                            }


                            try {
                                JSONObject jsonObject = new JSONObject(res);

                                String ordernumber = jsonObject.getString("ordernumber");
                                invoiceid = jsonObject.getString("id");
                                System.out.println("invoiceidIn---"+invoiceid);
                                String invoicedate = jsonObject.getString("invoicedate");
                                TotalPaymentAmt= jsonObject.getString("totalamount");
                                String totalamount = jsonObject.getString("totalamount");
                                String totallineitemamount = jsonObject.getString("totallineitemamount");
                                String discountpercentage = jsonObject.getString("discountpercentage");
                                String discountamount = jsonObject.getString("discountamount");
                                String freightamount = jsonObject.getString("freightamount");
                                String totaltaxbase = jsonObject.getString("totaltaxbase");
                                String totaltax = jsonObject.getString("totaltax");
                                String paymenttype = jsonObject.getString("paymenttype");
                                String salesrep = jsonObject.getString("salesrep");
                                String referenceorder = jsonObject.getString("referenceorder");
                                String presaleordernumber = jsonObject.getString("presaleordernumber");
                                String deliverynote = jsonObject.getString("deliverynote");
                                String shipnote = jsonObject.getString("shipnote");
                                String memo = jsonObject.getString("memo");
                                String termsconditions = jsonObject.getString("termsconditions");

                                String billaddress = jsonObject.getString("billaddress");
                                String shipaddress = jsonObject.getString("shipaddress");

                                JSONObject jsCustomers = jsonObject.getJSONObject("customers");
                                String customername = jsCustomers.getString("customername");

                                JSONArray jsInvoiceproduct = jsonObject.getJSONArray("invoiceproduct");
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @SuppressLint("Range")
                                        @Override
                                        public void run() {

                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                            Date date = null;
                                            try {
                                                date = sdf.parse(invoicedate);
                                                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                invoiceDate_tv.setText(date_order);
                                                invoiceCustomer_tv.setText(customername);
                                                if (billaddress.isEmpty()) {
                                                    InBill_tv.setText("No address found!");
                                                }else {
                                                    String b=billaddress.replace("<br/>",", ");
                                                    InBill_tv.setText(b);
                                                }

                                                if (shipaddress.isEmpty()) {
                                                    InShip_tv.setText("No address found!");
                                                }else {
                                                    String s=shipaddress.replace("<br/>",", ");
                                                    InShip_tv.setText(s);
                                                }



                                                invoiceStatus_tv.setText(submitstatus);
                                                invoicePresale_tv.setText(presaleordernumber);
                                                if(paymenttype.equals("null")){
                                                    invoicePayType_tv.setText("N/A");
                                                }else {
                                                    invoicePayType_tv.setText(paymenttype);
                                                }

                                                if(salesrep.equals("null")){
                                                    invoiceRep_tv.setText("N/A");
                                                }else {
                                                    invoiceRep_tv.setText(salesrep);
                                                }

                                                if(referenceorder.equals("null")){
                                                    invoiceRefPo_tv.setText("N/A");
                                                }else {
                                                    invoiceRefPo_tv.setText(referenceorder);
                                                }



                                                tvIn_discountTittle.setText("Discount("+discountpercentage+"%)");
                                                tvIn_TaxHeads.setText("Customer Tax("+totaltaxbase+"%)");
                                                invoiceID_tv.setText("IN_"+ordernumber);
                                                invoiceAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));
                                                In_sub_total.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totallineitemamount)));
                                                tvIn_discount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(discountamount)));
                                                tvIn_ShipmentAmt.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(freightamount)));
                                                tvIn_Tax.setText(Activity_OrdersHistory.currency_symbol+Utils.truncateDecimal(Double.valueOf(totaltax)));
                                                txt_InPayable_amount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));

                                                String description = "";
                                                for (int i = 0; i < jsInvoiceproduct.length(); i++) {
                                                    JSONObject js = jsInvoiceproduct.getJSONObject(i);
                                                    String productname = js.getString("productname");
                                                    String quantity = js.getString("quantity");
                                                    String baseamount = js.getString("baseamount");
                                                 //   String productimage = js.getString("productimage");
                                                    String priceperunit = js.getString("priceperunit");
                                                    String itemistaxable = js.getString("itemistaxable");
                                                    String upccode = js.getString("upccode");
                                                    String productid = js.getString("productid");
                                                    //String imageurl = js.getString("imageurl");

                                                    SQLiteController sqLiteController = new SQLiteController(getActivity());
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

                                                    JSONArray data2 = js.getJSONArray("digitalassetsinfo");
                                                    if(data2.length()>0){
                                                        for (int k = 0; k < data2.length(); k++) {

                                                            JSONObject js1 = data2.getJSONObject(k);
                                                            String isdefault = js1.getString("isdefault");

                                                            if (!isdefault.equals("null")) {

                                                                if(isdefault.equals("true")){

                                                                    String productimage = js1.getString("productimage");
                                                                    String finalDescription = description;
                                                                    getActivity().runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            InvoiceProductlist.add(new Model_InvoiceList(productname,quantity, baseamount,productimage,priceperunit,itemistaxable,upccode, finalDescription));

                                                                        }
                                                                    });
                                                                }
                                                            }else {
                                                                String finalDescription1 = description;
                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        InvoiceProductlist.add(new Model_InvoiceList(productname,quantity, baseamount,"null",priceperunit,itemistaxable,upccode, finalDescription1));

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }else {
                                                        String finalDescription1 = description;
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                InvoiceProductlist.add(new Model_InvoiceList(productname,quantity, baseamount,"null",priceperunit,itemistaxable,upccode, finalDescription1));

                                                            }
                                                        });
                                                    }



                                                    adapter.notifyDataSetChanged();
                                                }


                                                if(deliverynote.isEmpty()||deliverynote.equals("null")){
                                                    invoiceDeliveryNotes_tv.setText("N/A");
                                                }else {
                                                    invoiceDeliveryNotes_tv.setText(deliverynote);
                                                } if(shipnote.isEmpty()||shipnote.equals("null")){
                                                    invoicePackship_tv.setText("N/A");
                                                }else {
                                                    invoicePackship_tv.setText(shipnote);
                                                } if(termsconditions.isEmpty()||termsconditions.equals("null")){
                                                    invoiceTerms_tv.setText("N/A");
                                                }else {
                                                    invoiceTerms_tv.setText(termsconditions);
                                                } if(memo.isEmpty()||memo.equals("null")){
                                                    invoiceMemo_tv.setText("N/A");
                                                }else {
                                                    invoiceMemo_tv.setText(memo);
                                                }

                                            } catch (ParseException | JSONException e) {
                                                e.printStackTrace();
                                            }



                                            String Outpur_Str = Utils.convertToIndianCurrency(totalamount);
                                            Log.i("Outpur_Str", Outpur_Str);

                                            String b=billaddress.replace("<br/>",", ");
                                            String s=shipaddress.replace("<br/>",", ");


                                            if(!APIInvoiceHTML.equals("null")){
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$COMPANYADDRESS$", " ");
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$", " IN_"+ordernumber);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceDate$", date_order);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", " SO "+presaleordernumber);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$paymentTerm$", paymenttype);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$salesPerson$", salesrep);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$referencePo$", "0");
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$BillingAddress$", b);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$ShippingAddress$", s);
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

                                            }




                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            if (getActivity() != null) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Invoice_ll.setVisibility(View.GONE);
                                        txt_noInvoice.setVisibility(View.VISIBLE);
                                        Utils.getInstance().dismissDialog();
                                    }
                                });
                            }

                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void CreateInvoicePDF() {
            System.out.println("CreateINVOicePDF---");

            String localRowhtml = "";

            for (int i = 0; i < InvoiceProductlist.size(); i++) {

                localRowhtml += InvoiceRowHtml;

                localRowhtml = localRowhtml.replace("prodname", InvoiceProductlist.get(i).getInvoice_productname()+InvoiceProductlist.get(i).getInvoice_description());
                localRowhtml = localRowhtml.replace("price", InvoiceProductlist.get(i).getInvoice_priceperunit());
                localRowhtml = localRowhtml.replace("Qty", InvoiceProductlist.get(i).getInvoice_quantity());
                if (Activity_OrdersHistory.tax.equals("1")){
                    if(InvoiceProductlist.get(i).getItemistaxable().equals("true")){
                        localRowhtml = localRowhtml.replace("Tax", "Taxable");

                    }else {
                        localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

                    }

                }else {
                    localRowhtml = localRowhtml.replace("Tax", "-");
                }
                localRowhtml = localRowhtml.replace("amount", InvoiceProductlist.get(i).getInvoice_baseamount());


            }

          //  localRowhtml = localRowhtml.replace("Tax", "Non-Taxable");

            APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceProduct$", localRowhtml);

            APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$", Companylogo);


            FileManager.getInstance().cleanTempFolder(getActivity());
            // Create Temp File to save Pdf To
            final File savedPDFFile = FileManager.getInstance().createTempFile(getActivity(), "pdf", false);
            // Generate Pdf From Html


            PDFUtil.generatePDFFromHTML(getActivity(), savedPDFFile, "<!DOCTYPE html>\n" +
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
                    Intent intentPdfViewer = new Intent(getActivity(), PdfViewerActivity.class);
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



    }


    @SuppressLint("ValidFragment")
    public static class PaymentFragment extends Fragment {

        private CoordinatorLayout cl;
        AppCompatTextView payTittled_tv,payUser_tv,paydate_tv,payMethod_tv,payOrderAmt_tv,payPaidAmt_tv,payDueAmt_tv,payTotalAmt_tv;
        LinearLayout InPayment_ll;
        AppCompatButton btn_paymentsubmit;
        AppCompatImageView payment_view_img;

        AppCompatTextView dl_amt_tv,dl_date_tv,dl_no_tv;

        ArrayList<String> mArrayUri = new ArrayList<String>();

        private GridView gvGallery;
        private ImageViewAdapter galleryAdapter;

        LottieAnimationView txt_noInvoice;

        AppCompatImageView payment_sign_img;


        public PaymentFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_payment, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

             txt_noInvoice = view.findViewById(R.id.txt_noInvoice);
            payTittled_tv = view.findViewById(R.id.payTittled_tv);
            payment_sign_img = view.findViewById(R.id.payment_sign_img);
            InPayment_ll = view.findViewById(R.id.InPayment_ll);
            gvGallery = (GridView) view.findViewById(R.id.gv_payment);
            payOrderAmt_tv = view.findViewById(R.id.payOrderAmt_tv);
            payPaidAmt_tv = view.findViewById(R.id.payPaidAmt_tv);
            payDueAmt_tv = view.findViewById(R.id.payDueAmt_tv);
            payTotalAmt_tv = view.findViewById(R.id.payTotalAmt_tv);
            paydate_tv = view.findViewById(R.id.paydate_tv);
            payMethod_tv = view.findViewById(R.id.payMethod_tv);
            payUser_tv = view.findViewById(R.id.payUser_tv);
            dl_amt_tv = view.findViewById(R.id.dl_amt_tv);
            dl_date_tv = view.findViewById(R.id.dl_date_tv);
            dl_no_tv = view.findViewById(R.id.dl_no_tv);

          /*  btn_paymentsubmit = view.findViewById(R.id.btn_paymentsubmit);
            btn_paymentsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("InvoiceID--"+invoiceid);
                    if(!invoiceid.equals("null")){
                        Bundle bundle = new Bundle();
                        bundle.putString("InvoiceID",invoiceid);
                        bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                        Intent in = new Intent(getActivity(), Activity_Deliver.class);
                        in.putExtras(bundle);
                        startActivity(in);
                    }else{
                        Toast.makeText(getActivity(), "Invoice not generated!", Toast.LENGTH_SHORT).show();
                    }

                }
            });


*/
            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if(ordernumber.startsWith("DRFOM")){

                }else {
                    if (getActivity() != null) {

                        if(!invoiceid.equals("null")){
                            GetPaySummary(invoiceid);
                        }

                    }
                }


            }





        }


        private void GetPaySummary(String Id_) {
            //Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetPaymentsView(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Utils.getInstance().dismissDialog();
                                InPayment_ll.setVisibility(View.GONE);
                                txt_noInvoice.setVisibility(View.VISIBLE);
                            }
                        });

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String res = response.body().string();


                            try {
                                JSONArray jsonAr = new JSONArray(res);

                                if(jsonAr.length()>0){

                                    if (getActivity() != null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(mArrayUri.size()>0)mArrayUri.clear();
                                                txt_noInvoice.setVisibility(View.GONE);
                                                InPayment_ll.setVisibility(View.VISIBLE);
                                                //Utils.getInstance().dismissDialog();
                                            }
                                        });
                                    }

                                    JSONObject jsonObject = jsonAr.getJSONObject(0);

                                    String payable = jsonObject.getString("payable");
                                    String paid = jsonObject.getString("paid");
                                    String due = jsonObject.getString("due");
                                    String paymentdate = jsonObject.getString("paymentdate");
                                    String paymentmethod = jsonObject.getString("paymentmethod");
                                    String ordernumber = jsonObject.getString("ordernumber");
                                    String paymentcollectedbyname = jsonObject.getString("paymentcollectedbyname");

                                    JSONArray jsry = jsonObject.getJSONArray("paymentimg");
                                    for (int i = 0; i < jsry.length(); i++) {
                                        JSONObject jsb = jsry.getJSONObject(i);
                                        String payment_img = jsb.getString("productimage");
                                        String assettypename = jsb.getString("assettypename");

                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(assettypename.equals("null")){
                                                        mArrayUri.add(String.valueOf(payment_img));
                                                        galleryAdapter = new ImageViewAdapter(getActivity(),mArrayUri);
                                                        gvGallery.setNumColumns(jsry.length());
                                                        gvGallery.setAdapter(galleryAdapter);
                                                        gvGallery.setHorizontalSpacing(gvGallery.getHorizontalSpacing());
                                                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                                                .getLayoutParams();
                                                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                                                    }

                                                }
                                            });
                                        }

                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(assettypename.equals("Paymentsignature")){

                                                        payTittled_tv.setVisibility(View.VISIBLE);

                                                            Picasso.with(payment_sign_img.getContext()).load(Const.ImagePaymentsSignature+payment_img)
                                                                    .placeholder(R.drawable.image_placeholder)
                                                                    .error(R.drawable.image_placeholder)
                                                                    .into(payment_sign_img);
                                                    }



                                                }
                                            });
                                        }



                                    }



                                    if (getActivity() != null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                Date date = null;
                                                try {
                                                    date = sdf.parse(paymentdate);
                                                    String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                    payUser_tv.setText(paymentcollectedbyname);

                                                    paydate_tv.setText(date_order);
                                                    dl_date_tv.setText(date_order);
                                                    payOrderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(payable)));
                                                    payPaidAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(paid)));
                                                    payDueAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(due)));
                                                    payTotalAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(payable)));
                                                    dl_amt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(payable)));
                                                    dl_no_tv.setText("SO "+ordernumber);

                                                    if(paymentmethod.isEmpty()||paymentmethod.equals("null")){
                                                        payMethod_tv.setText("N/A");
                                                    }else {
                                                        payMethod_tv.setText(paymentmethod);
                                                    }

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }


                                }else {
                                    if (getActivity() != null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                InPayment_ll.setVisibility(View.GONE);
                                                txt_noInvoice.setVisibility(View.VISIBLE);


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



    }

    @SuppressLint("ValidFragment")
    public static class DeliveryFragment extends Fragment {

        private CoordinatorLayout cl;
        AppCompatTextView payMethod_tv;
        LinearLayout InPayment_ll;
        AppCompatButton btn_paymentsubmit;
        AppCompatImageView payment_view_img;

        AppCompatTextView dl_amt_tv,dl_date_tv,dl_no_tv,dl_status_tv;

        ArrayList<String> mArrayUri = new ArrayList<String>();

        private GridView gvGallery;
        private ImageViewAdapter galleryAdapter;

        LottieAnimationView txt_noInvoice;

        AppCompatImageView payment_sign_img;


        public DeliveryFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_delivery, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            txt_noInvoice = view.findViewById(R.id.txt_noInvoice);
            payment_sign_img = view.findViewById(R.id.payment_sign_img);
            InPayment_ll = view.findViewById(R.id.InPayment_ll);
            gvGallery = (GridView) view.findViewById(R.id.gv_payment);

            dl_date_tv = view.findViewById(R.id.dl_date_tv);
            dl_no_tv = view.findViewById(R.id.dl_no_tv);
            dl_status_tv = view.findViewById(R.id.dl_status_tv);
            payMethod_tv = view.findViewById(R.id.payMethod_tv);
            dl_amt_tv = view.findViewById(R.id.dl_amt_tv);

          /*  btn_paymentsubmit = view.findViewById(R.id.btn_paymentsubmit);
            btn_paymentsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("InvoiceID--"+invoiceid);
                    if(!invoiceid.equals("null")){
                        Bundle bundle = new Bundle();
                        bundle.putString("InvoiceID",invoiceid);
                        bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                        Intent in = new Intent(getActivity(), Activity_Deliver.class);
                        in.putExtras(bundle);
                        startActivity(in);
                    }else{
                        Toast.makeText(getActivity(), "Invoice not generated!", Toast.LENGTH_SHORT).show();
                    }

                }
            });


*/

            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if(ordernumber.startsWith("DRFOM")){

                }else {

                    if (getActivity() != null) {
                        Progress_dialog.show();
                        GetDeliveryImage(OrderId);

                    }

                }

            }

        }

        private void GetDeliveryImage(String Id_) {
            //Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetDeliveryView(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Utils.getInstance().dismissDialog();
                                InPayment_ll.setVisibility(View.GONE);
                                txt_noInvoice.setVisibility(View.VISIBLE);
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
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (Progress_dialog != null) {
                                        if (Progress_dialog.isShowing()) {
                                            Progress_dialog.dismiss();
                                        }
                                    }
                                }
                            });


                            String res = response.body().string();

                            try {
                                JSONObject jsOb = new JSONObject(res);
                                System.out.println("jsonObjectDelivery---"+jsOb.toString());

                                JSONArray jsry = jsOb.getJSONArray("fileproperties");

                                if(jsry.length()>0){
                                    if (getActivity() != null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                  if(mArrayUri.size()>0)mArrayUri.clear();
                                                txt_noInvoice.setVisibility(View.GONE);
                                                InPayment_ll.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }

                                    for (int i = 0; i < jsry.length(); i++) {
                                        JSONObject jsb = jsry.getJSONObject(i);
                                        //String payment_img = jsb.getString("productimage");
                                        String payment_img = jsb.getString("filepath");
                                        String assettypename = jsb.getString("assettypename");

                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(assettypename.equals("null")){
                                                        System.out.println("ImageUpload----"+payment_img);
                                                        mArrayUri.add(String.valueOf(payment_img));
                                                        galleryAdapter = new ImageViewAdapter(getActivity(),mArrayUri);
                                                        gvGallery.setNumColumns(jsry.length());
                                                        gvGallery.setAdapter(galleryAdapter);
                                                        gvGallery.setHorizontalSpacing(gvGallery.getHorizontalSpacing());
                                                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                                                .getLayoutParams();
                                                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                                                    }

                                                }
                                            });
                                        }

                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(assettypename.equals("Deliverysignature")){

                                                        Picasso.with(payment_sign_img.getContext()).load(Const.ImageDeliverySignature+payment_img)
                                                                .placeholder(R.drawable.image_placeholder)
                                                                .error(R.drawable.image_placeholder)
                                                                .into(payment_sign_img);
                                                    }



                                                }
                                            });
                                        }



                                    }

                                    if (getActivity() != null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                Date date = null;
                                                try {
                                                    date = sdf.parse(datefulfilled);
                                                    String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                                    dl_date_tv.setText(date_order);
                                                    dl_amt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(TotalPaymentAmt)));
                                                    dl_no_tv.setText("SO "+ordernumber);
                                                    dl_status_tv.setText(status);



                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                }else {
                                    InPayment_ll.setVisibility(View.GONE);
                                    txt_noInvoice.setVisibility(View.VISIBLE);
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



    }

    @SuppressLint("ValidFragment")
    public static class AssociateOrderFragment extends Fragment {


        private CoordinatorLayout cl;


        public AssociateOrderFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_associateorders, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            //  txtNoRecord4 = view.findViewById(R.id.txt_no_record);


        }


    }

    public void AddNoteAlert(String OrderId) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrdersDetails.this);
                LayoutInflater inflater = ActivityOrdersDetails.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_notealert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_submit = v.findViewById(R.id.btn_Add);
                AppCompatEditText edit_query_des = v.findViewById(R.id.edit_notequery_des);


                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.getInstance().hideKeyboard(ActivityOrdersDetails.this);
                        if (Utils.getInstance().checkIsEmpty(edit_query_des)) {
                            Utils.getInstance().snackBarMessage(v,"Enter the Cancellation Reason");
                        }else {
                            dialog.dismiss();
                            if (Connectivity.isConnected(ActivityOrdersDetails.this) &&
                                    Connectivity.isConnectedFast(ActivityOrdersDetails.this)) {
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put("email",user_email);
                                    json.put("userId",user_id);
                                    json.put("messageType","Notes");
                                    json.put("data",edit_query_des.getText().toString());
                                    json.put("eventDescription","Notes added");
                                    json.put("aggregateId",user_id);
                                    json.put("screenId","073664B1-CD21-46A5-8514-6F4BBFBCFEF1");
                                    postNotesAlert(v,json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{

                                Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                            }

                        }

                    }
                });


            }
        });
    }
    public void CancelAlert(String OrderId) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrdersDetails.this);
                LayoutInflater inflater = ActivityOrdersDetails.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_cancelalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_submit = v.findViewById(R.id.btn_submit);
                cancelReason_sp = v.findViewById(R.id.cancelReason_sp);
                // AppCompatTextView order_number = v.findViewById(R.id.order_number);
                AppCompatEditText edit_query_des = v.findViewById(R.id.edit_query_des);
                //  order_number.setText(orderNo+" | "+OrderName);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arReasonName);
                arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                cancelReason_sp.setAdapter(arrayAdapter);
                cancelReason_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                        str_reason = arReasonName.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });

                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.getInstance().hideKeyboard(ActivityOrdersDetails.this);
                        if (str_reason.equals("")) {
                            Utils.getInstance().snackBarMessage(v,"Cancellation Reason!");
                        } else {
                            dialog.dismiss();
                            if (Connectivity.isConnected(ActivityOrdersDetails.this) &&
                                    Connectivity.isConnectedFast(ActivityOrdersDetails.this)) {
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put("id",OrderId);
                                    json.put("cancellationreason",str_reason);
                                    json.put("comments",edit_query_des.getText().toString());
                                    json.put("submitstatus","Cancelled");
                                    postCancelled(v,json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                sqLiteController = new SQLiteController(ActivityOrdersDetails.this);
                                sqLiteController.open();
                                try {
                                    sqLiteController.updatePresaleOrder(OrderId,"Cancelled",str_reason,edit_query_des.getText().toString());
                                } finally {
                                    sqLiteController.close();
                                    // Toast.makeText(Activity_OrdersHistory.this, "Order has cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ActivityOrdersDetails.this, com.example.arcomdriver.order.Activity_OrdersHistory.class);
                                    startActivity(intent);
                                }
                                //Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                            }

                        }

                    }
                });


            }
        });
    }

    private void postCancelled(final View v, final JSONObject json) {
        Utils.getInstance().loadingDialog(ActivityOrdersDetails.this, "Please wait.");
        try {
            App.getInstance().PostCancelPresale(json.toString(),token, new Callback() {
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
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.getInstance().dismissDialog();
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);

                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        startActivity(new Intent(ActivityOrdersDetails.this, Activity_OrdersHistory.class));
                                        Toast.makeText(ActivityOrdersDetails.this, "Presale Order cancelled Successfully", Toast.LENGTH_SHORT).show();
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

    private void postNotesAlert(final View v, final JSONObject json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.getInstance().loadingDialog(ActivityOrdersDetails.this, "Please wait.");
            }
        });
        try {
            App.getInstance().PostNotes(json.toString(),token, new Callback() {
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

                                    if ( succeeded == true) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("OrderId",OrderId);
                                        bundle.putString("CustomerName",CustomerName);
                                        Intent in = new Intent(ActivityOrdersDetails.this, ActivityOrdersDetails.class);
                                        in.putExtras(bundle);
                                        startActivity(in);
                                        Toast.makeText(ActivityOrdersDetails.this, "Notes has been added Successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(ActivityOrdersDetails.this, "Error, Please try again later!!", Toast.LENGTH_SHORT).show();
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

    private void getOrderDetails(String OrderID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("OrderId--"+OrderId);
                try {
                    App.getInstance().PostPresaleOrder(OrderID, "",token, new Callback() {
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
                                        System.out.println("------Order Getting Success----");
                                          if (Collectlist.size() > 0) Collectlist.clear();
                                        try {
                                            final JSONObject jsonObject = new JSONObject(res);
                                            JSONObject jsData = jsonObject.getJSONObject("data");
                                            Incustomerid = jsData.getString("customerid");
                                            Indiscountamount = jsData.getString("discountamount");
                                            Indiscountpercentage = jsData.getString("discountpercentage");
                                            Infreightamount = jsData.getString("freightamount");
                                            Intotalamount = jsData.getString("totalamount");
                                            Intotallineitemamount = jsData.getString("totallineitemamount");
                                            Intax = jsData.getString("tax");
                                            Intaxperecentage = jsData.getString("taxperecentage");
                                            Insalesrepid = jsData.getString("salesrepid");
                                            Indatefulfilled = jsData.getString("datefulfilled");
                                            Indeliverynote = jsData.getString("deliverynote");
                                            Inshipnote = jsData.getString("shipnote");
                                            Intermsconditions = jsData.getString("termsconditions");
                                            Inmemo = jsData.getString("memo");
                                            Inwarehouseid = jsData.getString("warehouseid");
                                            Intaxid = jsData.getString("taxid");
                                            Innetterms = jsData.getString("netterms");
                                            Induedate = jsData.getString("duedate");
                                            Incustomeristaxable = jsData.getString("customeristaxable");
                                            Inbilltoaddressid = jsData.getString("billtoaddressid");
                                            Inshiptoaddressid = jsData.getString("shiptoaddressid");


                                            JSONArray OrderProducts = jsData.getJSONArray("orderProducts");
                                            for (int i = 0; i < OrderProducts.length(); i++) {
                                                JSONObject js = OrderProducts.getJSONObject(i);
                                                String productname = js.getString("productname");
                                                String quantity = js.getString("quantity");
                                                String baseamount = js.getString("baseamount");
                                                String priceperunit = js.getString("priceperunit");
                                                String productid = js.getString("productid");
                                                String orderid = js.getString("orderid");
                                                String id = js.getString("id");
                                                String itemistaxable = js.getString("itemistaxable");
                                                String upccode = js.getString("upccode");

                                                Collectlist.add(new Model_ItemList(id, productname, "", quantity, String.valueOf(baseamount), priceperunit, productid, itemistaxable,upccode, "","false"));

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
        });

    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrdersDetails.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_successalert_payment, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
        AppCompatButton btn_AlertShareInvoice = v.findViewById(R.id.btn_AlertInvoice);
        btn_AlertShareInvoice.setText("Continue");
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        tittleAlert_tv.setText("To proceed payment invoice will be generated .");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btn_AlertShareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Progress_dialog.show();
               CreateInvoice();
                dialog.dismiss();
            }
        });
    }

    private void CreateInvoice() {

        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateTime = sdf1.format(date1);

        JSONObject postedJSON = new JSONObject();
        JSONArray array = new JSONArray();


        for (int i = 0; i < Collectlist.size(); i++) {

            double TotalBaseAmt = Double.parseDouble(Collectlist.get(i).getPrice_PerUnit()) * Double.parseDouble(Collectlist.get(i).getProduct_quantity());

            postedJSON = new JSONObject();
            try {
                //TODO getProduct_id = getOrder_productid
                //TODO getOrder_productid = getProduct_id

                if (OrderId.equals("0")) {
                    //TODO OrderProductID
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    } else {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    }
                } else {
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    } else {
                        //TODO OrderProductID
                        postedJSON.put("id", Collectlist.get(i).getProduct_id().trim());
                    }
                }

                postedJSON.put("isactive", true);

                if (OrderId.equals("0")) {
                    //   postedJSON.put("orderid","00000000-0000-0000-0000-000000000000");
                } else {
                    postedJSON.put("orderid", OrderId);
                }

                if (OrderId.equals("0")) {
                } else {
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("orderpid", "00000000-0000-0000-0000-000000000000");
                    } else {
                        //TODO OrderProductID
                        postedJSON.put("orderpid", Collectlist.get(i).getProduct_id().trim());
                    }

                }

                //TODO ProductID
                postedJSON.put("productid", Collectlist.get(i).getOrder_productid().trim());
                postedJSON.put("name", Collectlist.get(i).getProduct_name().trim());
                postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                postedJSON.put("quantityonhand", "0");
                postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                postedJSON.put("priceperunit", Collectlist.get(i).getPrice_PerUnit().trim());
                postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                postedJSON.put("delete", "null");
                postedJSON.put("warehouseid", Inwarehouseid);//warehouseid
                postedJSON.put("modifiedby", user_id);
                postedJSON.put("modifiedon", currentDateTime);
                postedJSON.put("isHigh", true);
                postedJSON.put("quantity", Collectlist.get(i).getProduct_quantity().trim());//quantity
                postedJSON.put("available", "0");
                postedJSON.put("committed", "0");
                if (Collectlist.get(i).getIstaxable().equals("true")) {
                    postedJSON.put("itemistaxable", true);
                } else {
                    postedJSON.put("itemistaxable", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(postedJSON);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("orderid", OrderId);
            json.put("paymenttypeid", "4e142367-5f99-4aa7-99ed-931756978ee5");
            json.put("paymentid", "00000000-0000-0000-0000-000000000000");
            json.put("paymentmethod", "Cash");
            json.put("name", "");
            json.put("billtoaddressid", Inbilltoaddressid);
            json.put("customerid", Incustomerid);
            json.put("vendorid", "00000000-0000-0000-0000-000000000000");
            json.put("datefulfilled", "00000000-0000-0000-0000-000000000000");
            json.put("description", "");
            json.put("discountamount", Indiscountamount);
            json.put("timestamp", currentDateTime);
            json.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
            json.put("exchangerate", "0");
            json.put("discountamountbase", "0");
            json.put("discountpercentage", Indiscountpercentage);
            json.put("freightamount", Infreightamount);
            json.put("freightamountbase", "0");
            json.put("freighttermscode", "0");
            json.put("ispricelocked", true);
            json.put("lastbackofficesubmit", currentDateTime);
            json.put("ordernumber", "0");
            json.put("quoteid", "00000000-0000-0000-0000-000000000000");
            json.put("requestdeliveryby", "00000000-0000-0000-0000-000000000000");
            json.put("shiptoaddressid", Inshiptoaddressid);
            json.put("invoicedate", currentDateTime);
            json.put("submitstatus", "Payment pending");
            json.put("submitstatusdescription", "");
            json.put("totalamount", Intotalamount);
            json.put("totalamountbase", "0");
            json.put("totalamountlessfreight", "0");
            json.put("totalamountlessfreightbase", "0");
            json.put("totaldiscountamount", "0");
            json.put("totaldiscountamountbase", "0");
            json.put("totallineitemamount", Intotallineitemamount);
            json.put("totallineitemamountbase", "0");
            json.put("totallineitemdiscountamount", "0");
            json.put("totallineitemdiscountamountbase", "0");
            json.put("totaltax", Intax);
            json.put("totaltaxbase", Intaxperecentage);
            json.put("willcall", true);
            json.put("salesrepid", Insalesrepid);
            json.put("pricingdate", Indatefulfilled);
            json.put("deliverynote", Indeliverynote);
            json.put("shipnote", Inshipnote);
            json.put("termsconditions", Intermsconditions);
            json.put("memo", Inmemo);
            json.put("referenceorder", "0");
            json.put("createdby", user_id);
            json.put("createdon", currentDateTime);
            json.put("modifiedby", user_id);
            json.put("modifiedon", currentDateTime);
            json.put("isactive", true);
            json.put("isPresale", true);
            //json.put("deletedRows", "");
            json.put("warehouseid", Inwarehouseid);
            json.put("draftnumber", "");
            json.put("lastsyncon", "");

            if(Intaxid.equals("null")){
                json.put("taxid",null);
            }else{
                json.put("taxid",Intaxid);
            }

            if(Innetterms.equals("null")){
                json.put("netterms",null);
            }else{
                json.put("netterms",Innetterms);
            }

            json.put("duedate",Induedate);
            if (Incustomeristaxable.equals("true")) {
                json.put("customeristaxable", true);
            } else {
                json.put("customeristaxable", false);
            }
            json.put("invoiceproduct", array);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("InvoiceJSON ----"+json.toString());

        postCreateInvoice(json.toString(), OrderId,currentDateTime);

    }

    private void postCreateInvoice(String json, String OrderID, String ModifiedDateTime) {

        try {
            App.getInstance().PostInvoiceCreate(json, token, new Callback() {
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
                                    Newinvoiceid = jsonObject.getString("data");
                                    if (succeeded == true) {

                                        JSONObject json = new JSONObject();
                                        try {
                                            if (OrderID.equals("0")) {
                                                json.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                json.put("id", OrderID);
                                            }

                                            //json.put("fulfillmentstatus", "Payment pending");
                                            json.put("invoiceid", Newinvoiceid);
                                            json.put("invoicerid", user_id);
                                            json.put("modifiedon",ModifiedDateTime);
                                            json.put("modifiedby",user_id);

                                            System.out.println("StatusUpdate---"+json.toString());
                                            System.out.println("modifiedon---"+ModifiedDateTime);

                                            postCreateFull(json.toString(),ModifiedDateTime);
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

    private void postCreateFull(String json,String ModifiedDateTime) {
        try {
            App.getInstance().PostWarehouseSummaryFul(json, token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (Progress_dialog != null) {
                            if (Progress_dialog.isShowing()) {
                                Progress_dialog.dismiss();
                            }
                        }
                        Utils.getInstance().GetMasterInsert(ActivityOrdersDetails.this,"CollectPayment",Progress_dialog,ModifiedDateTime);

                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if (succeeded == true) {
                                        Toast.makeText(ActivityOrdersDetails.this, "Invoice Generated Successful", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Newinvoiceid",Newinvoiceid);
                                        bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                                        bundle.putString("OrderID",OrderId);
                                        bundle.putString("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                        bundle.putString("deliverywarehouseid",deliverywarehouseid);
                                        Intent in = new Intent(ActivityOrdersDetails.this, Activity_CollectPayments.class);
                                        in.putExtras(bundle);
                                        startActivity(in);
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

    private String getCompanyLogo() {
        InSharedPreferences = getSharedPreferences(Activity_OrdersHistory.ohPref, MODE_PRIVATE);
        if (InSharedPreferences.contains(Activity_OrdersHistory.CmpLogo)) {
            return InSharedPreferences.getString(Activity_OrdersHistory.CmpLogo, null);
        }
        return " ";
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
