package com.example.arcomdriver.printer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class ThermalPrint extends Activity_Menu implements Runnable {

    protected static final String TAG = "ThermalPrint";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint;


    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    TextView stat,device_name_tv;

    LinearLayout layout;

    String Print,rVn,Flag;

    /* Get time and date */
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
    final String formattedDate = df.format(c.getTime());

    String APIOrderHTML="0";


   // Toolbar toolbar;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_thermal);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_thermal, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Printer Select");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SQLiteController sqLiteControllerC = new SQLiteController(ThermalPrint.this);
        sqLiteControllerC.open();
        try {
            long fetchAddressCount = sqLiteControllerC.fetchInvoiceCount();
            if (fetchAddressCount > 0) {
                Cursor C_Address = sqLiteControllerC.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));

                        if(invoice_name.equals("InvoiceTemplatePreview")){

                            APIOrderHTML  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));
                            System.out.println("APIOrderHTML----"+APIOrderHTML);

                        }



                    } while (C_Address.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Print = extras.getString("Print");
            Flag = extras.getString("Flag");


        }

        stat = findViewById(R.id.bpstatus);
        //toolbar = findViewById(R.id.toolbar);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        layout = findViewById(R.id.layout);

        device_name_tv = findViewById(R.id.device_name_tv);

        mScan = findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (mScan.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(ThermalPrint.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(ThermalPrint.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {
                            ListPairedDevices();
                            Intent connectIntent = new Intent(ThermalPrint.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                } else if (mScan.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                }
            }
        });


        mPrint = findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                if(Flag.equals("0")){
                    //Test
                    printLN();
                }else {
                    //Invoice
                    printV();
                }

                //p1();
                /* 5000 ms (5 Seconds) */
        /*        int TIME = 10000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        *//* print second copy *//*
                        p2();

                        printstat = 1;
                    }
                }, TIME);*/

            }
        });


    }

    private void printV() {

        try {
            JSONObject jsonObject = new JSONObject(Print);

            String InvoiceOrderNumber = jsonObject.getString("InvoiceOrderNumber");
            String InvoiceDate = jsonObject.getString("InvoiceDate");
            String presaleNumber = jsonObject.getString("presaleNumber");
            String SubTotal = jsonObject.getString("SubTotal");
            String DiscountAmount = jsonObject.getString("DiscountAmount");
            String ShippingAmount = jsonObject.getString("ShippingAmount");
            String TaxPercentage = jsonObject.getString("TaxPercentage");
            String discountpercentage = jsonObject.getString("discountpercentage");
            String TaxAmount = jsonObject.getString("TaxAmount");
            String TotalAmount = jsonObject.getString("TotalAmount");
            String BillingAddress = jsonObject.getString("BillingAddress");
            String ShippingAddress = jsonObject.getString("ShippingAddress");
            String TotalAmountInWords = jsonObject.getString("TotalAmountInWords");
            String paymentTerm = jsonObject.getString("paymentTerm");
            String CustomerName = jsonObject.getString("CustomerName");
            JSONArray jsInvoiceproduct = jsonObject.getJSONArray("orderitem");

            Thread t = new Thread() {
                public void run() {
                    int OrderCount_ =0;
                    try {
                        OutputStream os = mBluetoothSocket
                                .getOutputStream();
                        String header = "";
                        String he = "";
                        String blank = "";
                        String header2 = "";
                        String BILL = "";
                        String vio = "";
                        //String header3 = "";
                        String header7 = "";
                        String header6 = "";
                        //String mvdtail = "";
                        String header4 = "";
                        String header5 = "";
                        String offname = "";
                        String offname2 = "";
                        String time = "";
                        String checktop_status = "";
                        String ItemView = "";
                        String Products = "";
                        String Bottomv = "";
                        String Paytail = "";
                        String Csname = "";

                        blank = "\n\n";
                        he = "                Invoice Details\n";
                        he = he + "********************************************\n\n";

                        //header = "Invoice #\n";
                        header = "Invoice #"+"                    Invoice Date\n";
                        //BILL = InvoiceOrderNumber + "\n";
                        BILL = InvoiceOrderNumber+"                       "+InvoiceDate+"\n";
                        BILL = BILL
                                + "----------------------------------------------\n";
                        header2 = "Presale Order #\n";
                        vio = presaleNumber + "\n";
                        vio = vio
                                + "----------------------------------------------\n";
                      /*  header3 = "Invoice Date\n";
                        mvdtail = InvoiceDate + "\n";
                        mvdtail = mvdtail
                                + "--------------------------------\n";*/
                        header7 = "Customer Name\n";
                        Csname = CustomerName + "\n";
                        Csname = Csname
                                + "----------------------------------------------\n";

                        header6 = "Payment Term\n";
                        Paytail = paymentTerm + "\n";
                        Paytail = Paytail
                                + "----------------------------------------------\n";

                        header4 = "Billed To\n";
                        offname = BillingAddress + "\n";
                        offname = offname
                                + "----------------------------------------------\n";

                        header5 = "Ship To\n";
                        offname2 = ShippingAddress + "\n";
                        offname2 = offname2
                                + "----------------------------------------------\n";
                        ItemView = ItemView + "\n\n ";
                      //  ItemView = ItemView + String.format("%1$-9s %2$7s %3$8s %4$7s", "Item", "Qty", "Cost", "Amount");
                        ItemView = ItemView + String.format("%1$-10s %2$8s %3$9s %4$8s", "Item", "Qty", "Cost", "Amount");
                        ItemView = ItemView + "\n";
                        ItemView = ItemView
                                + "-----------------------------------------------";
                        Bottomv = Bottomv
                                + "----------------------------------------------\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "             Subtotal          :"+ SubTotal + "\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "             Discount"+discountpercentage+"    :"+ DiscountAmount+ "\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "             Shipping Charges  :"+ ShippingAmount+ "\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "             Customer Tax"+TaxPercentage+":"+ TaxAmount+ "\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "             Total             :"+ TotalAmount + "\n";

                        Bottomv = Bottomv
                                + "-----------------------------------------------\n";
                        Bottomv = Bottomv + "\n\n ";
                        Bottomv = Bottomv + "        Thank you for your business! " + "\n";
                        Bottomv = Bottomv + "\n\n ";
                        time = formattedDate + "\n\n";
                        //copy = "-Customer's Copy\n\n\n\n\n\n\n\n\n";
                        os.write(blank.getBytes());
                        os.write(he.getBytes());
                       // os.write(View.getBytes());
                        os.write(header.getBytes());
                        os.write(BILL.getBytes());
                        os.write(header2.getBytes());
                        os.write(vio.getBytes());
                        //os.write(header3.getBytes());
                       // os.write(mvdtail.getBytes());
                        os.write(header7.getBytes());
                        os.write(Csname.getBytes());
                        os.write(header6.getBytes());
                        os.write(Paytail.getBytes());
                        os.write(header4.getBytes());
                        os.write(offname.getBytes());
                        os.write(header5.getBytes());
                        os.write(offname2.getBytes());
                        os.write(checktop_status.getBytes());
                        os.write(ItemView.getBytes());

                        for (int i = 0; i < jsInvoiceproduct.length(); i++) {
                            try {
                                JSONObject js = jsInvoiceproduct.getJSONObject(i);
                                String prodname = js.getString("prodname");
                                String price = js.getString("price");
                                String Qty = js.getString("Qty");
                                String amount = js.getString("amount");
                                String Tax = js.getString("Tax");

                                OrderCount_ += 1;

                                rVn =String.format("%1$-10s %2$8s %3$9s %4$8s", "item-00"+OrderCount_, Qty, price, amount);
                                //rVn =String.format("%1$-10s %2$8s %3$9s %4$8s",String.valueOf(addLinebreaks(prodname,4)), Qty, price, amount);
                                Products = Products + "\n " + rVn + "\n ";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        os.write(Products.getBytes());
                        os.write(Bottomv.getBytes());
                        os.write(time.getBytes());

                        // Setting height
                        int gs = 29;
                        os.write(intToByteArray(gs));
                        int h = 150;
                        os.write(intToByteArray(h));
                        int n = 170;
                        os.write(intToByteArray(n));

                        // Setting Width
                        int gs_width = 29;
                        os.write(intToByteArray(gs_width));
                        int w = 119;
                        os.write(intToByteArray(w));
                        int n_width = 2;
                        os.write(intToByteArray(n_width));

                    } catch (Exception e) {
                        Log.e("PrintActivity", "Exe ", e);
                    }
                }
            };
            t.start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String addLinebreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > maxLineLength) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }

    public void p1() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";
                    String vio = "";
                    String header3 = "";
                    String mvdtail = "";
                    String header4 = "";
                    String offname = "";
                    String time = "";
                    String copy = "";
                    String checktop_status = "";
                    String BILLView = "";
                    String View = "";

                    blank = "\n\n";
                    he = "      Isc Global Solutions Private Limited\n";
                    he = he + "********************************************\n\n";

                    View = "                Isc Global\n"
                            + "             Unipunch prestige,\n " +
                            "        Ambattur Industrial Estate,\n" +
                            "        Chennai,Tamil Nadu 600058. \n";
                    View = View
                            + "-----------------------------------------------\n";

                    header = "FULL NAME:\n";
                    BILL = "SivaramYogesh" + "\n";
                    BILL = BILL
                            + "--------------------------------\n";
                    header2 = "COMPANY'S NAME:\n";
                    vio = "ISC GLOBAL" + "\n";
                    vio = vio
                            + "--------------------------------\n";
                    header3 = "ORDER NUMBER:\n";
                    mvdtail = "3000" + "\n";
                    mvdtail = mvdtail
                            + "--------------------------------\n";

                    header4 = "DETAILS:\n";
                    offname = "Bmobile" + "\n";
                    offname = offname
                            + "--------------------------------\n";

                    BILLView = BILLView + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Totel");
                    BILLView = BILLView + "\n";
                    BILLView = BILLView
                            + "-----------------------------------------------";
                    BILLView = BILLView + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00" + "\n ");
                    BILLView = BILLView + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00" + "\n ");
                    BILLView = BILLView + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00" + "\n ");
                    BILLView = BILLView + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00" + "\n ");

                    BILLView = BILLView
                            + "\n-----------------------------------------------";
                    BILLView = BILLView + "\n\n ";

                    BILLView = BILLView + "                   Total Qty:" + "      " + "85" + "\n";
                    BILLView = BILLView + "                   Total Value:" + "     " + "700.00" + "\n";

                    BILLView = BILLView
                            + "-----------------------------------------------\n";
                    BILLView = BILLView + "\n\n ";
                    BILLView = BILLView + "        Thank you for your business! " + "\n";
                    BILLView = BILLView + "\n\n ";
                    time = formattedDate + "\n\n";
                    copy = "-Customer's Copy\n\n\n\n\n\n\n\n\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(View.getBytes());
                    os.write(header.getBytes());
                    os.write(BILL.getBytes());
                    os.write(header2.getBytes());
                    os.write(vio.getBytes());
                    os.write(header3.getBytes());
                    os.write(mvdtail.getBytes());
                    os.write(header4.getBytes());
                    os.write(offname.getBytes());
                    os.write(checktop_status.getBytes());
                    os.write(BILLView.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public void printLN() {

        Thread t = new Thread() {
            public void run() {

                int OrderCount_ =0;
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";
                    String vio = "";
                    //String header3 = "";
                    String header7 = "";
                    String header6 = "";
                    //String mvdtail = "";
                    String header4 = "";
                    String header5 = "";
                    String offname = "";
                    String offname2 = "";
                    String time = "";
                    String checktop_status = "";
                    String ItemView = "";
                    String Products = "";
                    String Bottomv = "";
                    String Paytail = "";
                    String Csname = "";

                    blank = "\n\n";
                    he = "                Invoice Details\n";
                    he = he + "********************************************\n\n";

                    //header = "Invoice #\n";
                    header = "Invoice #"+"                    Invoice Date\n";
                    //BILL = InvoiceOrderNumber + "\n";
                    BILL = "N/A"+"                            "+"N/A"+"\n";
                    BILL = BILL
                            + "----------------------------------------------\n";
                    header2 = "Presale Order #\n";
                    vio = "N/A" + "\n";
                    vio = vio
                            + "----------------------------------------------\n";
                      /*  header3 = "Invoice Date\n";
                        mvdtail = InvoiceDate + "\n";
                        mvdtail = mvdtail
                                + "--------------------------------\n";*/

                    header7 = "Customer Name\n";
                    Csname = "N/A" + "\n";
                    Csname = Csname
                            + "----------------------------------------------\n";

                    header6 = "Payment Term\n";
                    Paytail = "N/A" + "\n";
                    Paytail = Paytail
                            + "----------------------------------------------\n";

                    header4 = "Billed To\n";
                    offname = "N/A" + "\n";
                    offname = offname
                            + "----------------------------------------------\n";

                    header5 = "Ship To\n";
                    offname2 = "N/A" + "\n";
                    offname2 = offname2
                            + "----------------------------------------------\n";
                    ItemView = ItemView + "\n\n ";
                    //  ItemView = ItemView + String.format("%1$-9s %2$7s %3$8s %4$7s", "Item", "Qty", "Cost", "Amount");
                    ItemView = ItemView + String.format("%1$-10s %2$8s %3$9s %4$8s", "Item", "Qty", "Cost", "Amount");
                    ItemView = ItemView + "\n";
                    ItemView = ItemView
                            + "-----------------------------------------------";
                    rVn =String.format("%1$-10s %2$8s %3$9s %4$8s", "item-00", "0", "0.00", "0.00");
                    //rVn =String.format("%1$-10s %2$8s %3$9s %4$8s",String.valueOf(addLinebreaks(prodname,4)), Qty, price, amount);
                    Products = Products + "\n " + rVn + "\n ";
                    Bottomv = Bottomv
                            + "----------------------------------------------\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "             Subtotal          :"+ "$0.00" + "\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "             Discount"+"0.00%"+"   :"+ "$0.00"+ "\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "             Shipping Charges  :"+ "$0.00"+ "\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "             Customer Tax"+"0.00%"+":"+ "$0.00"+ "\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "             Total             :"+ "$0.00" + "\n";

                    Bottomv = Bottomv
                            + "-----------------------------------------------\n";
                    Bottomv = Bottomv + "\n\n ";
                    Bottomv = Bottomv + "        Thank you for your business! " + "\n";
                    Bottomv = Bottomv + "\n\n ";
                    time = formattedDate + "\n\n";
                    //copy = "-Customer's Copy\n\n\n\n\n\n\n\n\n";
                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    // os.write(View.getBytes());
                    os.write(header.getBytes());
                    os.write(BILL.getBytes());
                    os.write(header2.getBytes());
                    os.write(vio.getBytes());
                    //os.write(header3.getBytes());
                    // os.write(mvdtail.getBytes());
                    os.write(header7.getBytes());
                    os.write(Csname.getBytes());
                    os.write(header6.getBytes());
                    os.write(Paytail.getBytes());
                    os.write(header4.getBytes());
                    os.write(offname.getBytes());
                    os.write(header5.getBytes());
                    os.write(offname2.getBytes());
                    os.write(checktop_status.getBytes());
                    os.write(ItemView.getBytes());
                    os.write(Products.getBytes());
                    os.write(Bottomv.getBytes());
                    os.write(time.getBytes());

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));

                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };

        t.start();
    }


   /* public void p2() {

        Thread tt = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";
                    String vio = "";
                    String header3 = "";
                    String mvdtail = "";
                    String header4 = "";
                    String offname = "";
                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n\n";
                    he = "      EFULLTECH\n";
                    he = he + "********************************\n\n";

                    header = "FULL NAME:\n";
                    BILL = fullName.getText().toString() + "\n";
                    BILL = BILL
                            + "================================\n";
                    header2 = "COMPANY'S NAME:\n";
                    vio = companyName.getText().toString() + "\n";
                    vio = vio
                            + "================================\n";
                    header3 = "AGE:\n";
                    mvdtail = age.getText().toString() + "\n";
                    mvdtail = mvdtail
                            + "================================\n";

                    header4 = "AGENT DETAILS:\n";
                    offname = agent_detail.getText().toString() + "\n";
                    offname = offname
                            + "--------------------------------\n";
                    time = formattedDate + "\n\n";
                    copy = "-Agents's Copy\n\n\n\n\n\n\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(header.getBytes());
                    os.write(BILL.getBytes());
                    os.write(header2.getBytes());
                    os.write(vio.getBytes());
                    os.write(header3.getBytes());
                    os.write(mvdtail.getBytes());
                    os.write(header4.getBytes());
                    os.write(offname.getBytes());
                    os.write(checktop_status.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        tt.start();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* Terminate bluetooth connection and close all sockets opened */
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    String mDeviceInfo = mExtra.getString("mDeviceInfo");
                    device_name_tv.setText(mDeviceInfo+ "\n" + mDeviceAddress);
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ThermalPrint.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ThermalPrint.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();

            stat.setText("");
            stat.setText("Connected");
            stat.setTextColor(Color.rgb(97, 170, 74));
            mPrint.setEnabled(true);
            mScan.setText("Disconnect");


        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }


}
