package com.example.arcomdriver.invoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
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
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_NotesList;
import com.example.arcomdriver.adapter.Adapter_SummaryList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.helper.PdfViewerActivity;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.payments.Activity_Deliver;
import com.example.arcomdriver.payments.Activity_InDirectPayments;
import com.example.arcomdriver.printer.ThermalPrint;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.google.android.material.tabs.TabLayout;
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
 * @author : SivaramYogesh
 * Created on : 05 Feb 2023*/
public class ActivityInvoiceDetails extends Activity_Menu {

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
    public static  String TAG="ActivityInvoiceTab";
    public static String  paymenttypeid,paymentmethod,Invoicesubmitstatus,OrderId,TotalPaymentAmt,tr_fulfillmentstatus="N/A",tr_packed,tr_picked,tr_status,tr_submitdate,token,str_reason,user_email,user_id,salesrep_name,salesrepid,pricing_date,delivery_date,Invoiceid,CustomerName,ordernumber,totalamount,submitstatus,date_order;
    private SQLiteController sqLiteController;
    private static AppCompatTextView Inpayment_tv,odCustomerName_tv,odStatus_tv,odDate_tv,odDeliveryDate_tv,odAmt_tv;

    public static String APIInvoiceHTML ="null",deliveryshipmenttypeid,deliverywarehouseid;

    String InvoiceRowHtml = "<tr><td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">prodname</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">UPC Code</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\">price</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Qty</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"center\">Tax</td>\n" +
            "<td style=\"border-bottom:1px solid #dadada;font-family:Arial,Verdana,Helvetica,sans-serif;font-size:9px;padding:5px;\" align=\"right\">amount</td></tr>";

    public static ArrayList<Model_SummaryList> list = new ArrayList<>();

    public static JSONObject json_p = new JSONObject();

    String Companylogo;

    SharedPreferences InSharedPreferences;

    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_invoicedetailstab, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.GONE);
        In_download_ic.setVisibility(View.VISIBLE);
        In_print_img.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Companylogo =getCompanyLogo();

        odCustomerName_tv = findViewById(R.id.InCustomerName_tv);
        Inpayment_tv = findViewById(R.id.Inpayment_tv);
        odStatus_tv = findViewById(R.id.InStatus_tv);
        odDate_tv = findViewById(R.id.InDate_tv);
        odDeliveryDate_tv = findViewById(R.id.IndueDate_tv);
        odAmt_tv = findViewById(R.id.InAmt_tv);

        //OrdersSpinner
        SQLiteController sqLiteControllerC = new SQLiteController(ActivityInvoiceDetails.this);
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
                        }
                    } while (C_InvoiceFormat.moveToNext());
                }
            }
        } finally {
            sqLiteControllerC.close();
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Invoiceid = extras.getString("Invoiceid");
            CustomerName = extras.getString("CustomerName");

            SQLiteController sqLiteController = new SQLiteController(this);
            sqLiteController.open();
            try {
                long count = sqLiteController.fetchCount();
                if (count > 0) {
                    //Order
                    Cursor invoice_c = sqLiteController.readTableItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,Invoiceid);
                    if (invoice_c != null && invoice_c.moveToFirst()) {
                        do {
                            String ordernumber = invoice_c.getString(invoice_c.getColumnIndex("ordernumber"));
                            String invoicedate = invoice_c.getString(invoice_c.getColumnIndex("invoicedate"));
                            String pricingdate = invoice_c.getString(invoice_c.getColumnIndex("pricingdate"));
                            String in_due = invoice_c.getString(invoice_c.getColumnIndex("in_due"));
                            TotalPaymentAmt = invoice_c.getString(invoice_c.getColumnIndex("totalamount"));
                            String totalamount = invoice_c.getString(invoice_c.getColumnIndex("totalamount"));
                            Invoicesubmitstatus = invoice_c.getString(invoice_c.getColumnIndex("Insubmitstatus"));
                            OrderId = invoice_c.getString(invoice_c.getColumnIndex("orderid"));
                            paymenttypeid = invoice_c.getString(invoice_c.getColumnIndex("paymenttypeid"));
                            paymentmethod = invoice_c.getString(invoice_c.getColumnIndex("paymentmethod"));
                            System.out.println("paymenttypeid---"+paymenttypeid);
                            System.out.println("paymentmethod---"+paymentmethod);
                            System.out.println("OrderId44---"+OrderId);
                            System.out.println("Invoicesubmitstatus---"+Invoicesubmitstatus);
                            if (OrderId.equals("00000000-0000-0000-0000-000000000000")) {

                                if(Invoicesubmitstatus.equals("Paid")){
                                    Inpayment_tv.setVisibility(View.GONE);
                                }else if(Invoicesubmitstatus.equals("Payment pending")){
                                    Inpayment_tv.setVisibility(View.VISIBLE);
                                }else {
                                    Inpayment_tv.setVisibility(View.GONE);
                                }


                            } else {
                                Inpayment_tv.setVisibility(View.GONE);

                                 /* if(Invoicesubmitstatus.equals("Payment pending")){
                                    Inpayment_tv.setVisibility(View.VISIBLE);
                                }else {
                                    Inpayment_tv.setVisibility(View.GONE);
                                }*/
                            }


                            GetInvoiceSummary(OrderId);

                           runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(ordernumber.startsWith("DRFIN")){
                                        Invoicesubmitstatus ="Payment pending";
                                    }

                                    odStatus_tv.setText(Invoicesubmitstatus);

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                    Date date = null;
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        if(invoicedate.equals("null")){
                                            odDate_tv.setText("N/A");
                                        }else {
                                            date = sdf.parse(invoicedate);
                                            String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                            odDate_tv.setText(date_order);
                                        }

                                        if(in_due.equals("null")){
                                            odDeliveryDate_tv.setText("N/A");
                                        }else {
                                            date2 = sdf.parse(in_due);
                                            String due_str =new SimpleDateFormat("MM/dd/yyyy").format(date2);

                                            odDeliveryDate_tv.setText(due_str);
                                        }

                                        odCustomerName_tv.setText(CustomerName);

                                        if(ordernumber.startsWith("DRFIN")){
                                            txt_page.setText("IN_"+ordernumber);
                                        }else {
                                            Double price =Double.valueOf(ordernumber);
                                            DecimalFormat format = new DecimalFormat("0.#");
                                            txt_page.setText("IN_"+format.format(price));
                                        }

                                        odAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } while (invoice_c.moveToNext());
                    }

                }
            } finally {
                sqLiteController.close();
            }

        }

        sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    user_email = user_c.getString(user_c.getColumnIndex("Email"));

                }
            }
        } finally {
            sqLiteController.close();
        }

        mViewPager = findViewById(R.id.fragment_container);
        tabLayout = findViewById(R.id.tabs);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabLayout.setVisibility(View.VISIBLE);
        mSectionsPagerAdapter.addFragment(new NotesFragment());
        mSectionsPagerAdapter.addFragment(new SummaryFragment());
        mSectionsPagerAdapter.addFragment(new PaymentFragment());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        findViewById(R.id.In_download_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateInvoicePDF();

            }
        });
        findViewById(R.id.Inpayment_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("InvoiceID",Invoiceid);
                bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                bundle.putString("paymentMethod",paymentmethod);
                System.out.println("bundle----"+bundle.toString());

                Intent in = new Intent(ActivityInvoiceDetails.this, Activity_InDirectPayments.class);
                in.putExtras(bundle);
                startActivity(in);

            }
        });
        findViewById(R.id.In_print_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Intent in = new Intent(ActivityInvoiceDetails.this, ThermalPrint.class);
                in.putExtras(bundle);
                startActivity(in);

            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityInvoiceDetails.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityInvoiceDetails.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityInvoiceDetails.this, Activity_InvoiceHistory.class));
            }
        });

        findViewById(R.id.Od_menu_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ActivityInvoiceDetails.this, v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.popup_invoiceetailsmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (menuItem.getItemId()) {
                            case R.id.OrderDeliver:
                                if(!Invoiceid.equals("null")){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("InvoiceID",Invoiceid);
                                    bundle.putString("TotalPaymentAmt",TotalPaymentAmt);
                                    bundle.putString("OrderID",OrderId);
                                    bundle.putString("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                    bundle.putString("deliverywarehouseid",deliverywarehouseid);
                                    Intent in = new Intent(ActivityInvoiceDetails.this, Activity_Deliver.class);
                                    in.putExtras(bundle);
                                    startActivity(in);
                                }else{
                                    Toast.makeText(ActivityInvoiceDetails.this, "Invoice not generated!", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            default:
                                return false;
                        }
                    }

                });

                popupMenu.show();
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
                                postNotes(v,json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }
            });

            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {
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
                                                                        txt_no_record.setVisibility(View.GONE);
                                                                        //  Utils.getInstance().dismissDialog();
                                                                    }
                                                                });
                                                            }
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }else {
                                                    requireActivity().runOnUiThread(new Runnable() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        @Override
                                                        public void run() {
                                                            txt_no_record.setVisibility(View.VISIBLE);
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
                                            bundle.putString("Invoiceid",Invoiceid);
                                            Intent in = new Intent(getActivity(), ActivityInvoiceDetails.class);
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
    public static class SummaryFragment extends Fragment {

        private CoordinatorLayout cl;
        private RecyclerView IdProductList;
        LinearLayout InvoiceTax_ll;
        private AppCompatTextView idMemo_tv,idTerms_tv,idship_tv,idNotes_tv,txt_IdPayable_amount,tvId_Tax,tvId_TaxHeads,id_RefPo_tv,id_createdby_tv,IdBill_tv,IdShip_tv,Id_sub_total,tvId_discountTittle,tvId_discount,tvId_ShipmentAmt;
        private Adapter_SummaryList adapter;

        String Bill_Add,date_order_,Ship_Add;

        String orderNo="null";

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
            return inflater.inflate(R.layout.in_fragment_summary, container, false);
        }

        @SuppressLint("Range")
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            id_RefPo_tv = view.findViewById(R.id.id_RefPo_tv);
            InvoiceTax_ll = view.findViewById(R.id.InvoiceTax_ll);

            if (Activity_OrdersHistory.tax.equals("1")){
                InvoiceTax_ll.setVisibility(View.VISIBLE);
            }else {
                InvoiceTax_ll.setVisibility(View.GONE);
            }

            id_createdby_tv = view.findViewById(R.id.id_createdby_tv);
            IdBill_tv = view.findViewById(R.id.IdBill_tv);
            IdShip_tv = view.findViewById(R.id.IdShip_tv);
            IdProductList = view.findViewById(R.id.IdProductList);
            Id_sub_total = view.findViewById(R.id.Id_sub_total);
            tvId_discountTittle = view.findViewById(R.id.tvId_discountTittle);
            tvId_discount = view.findViewById(R.id.tvId_discount);
            tvId_ShipmentAmt = view.findViewById(R.id.tvId_ShipmentAmt);
            tvId_TaxHeads = view.findViewById(R.id.tvId_TaxHeads);
            tvId_Tax = view.findViewById(R.id.tvId_Tax);
            txt_IdPayable_amount = view.findViewById(R.id.txt_IdPayable_amount);
            idNotes_tv = view.findViewById(R.id.idNotes_tv);
            idship_tv = view.findViewById(R.id.idship_tv);
            idTerms_tv = view.findViewById(R.id.idTerms_tv);
            idMemo_tv = view.findViewById(R.id.idMemo_tv);

            adapter = new Adapter_SummaryList(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            IdProductList.setLayoutManager(layoutManager);
            IdProductList.setItemAnimator(new DefaultItemAnimator());
            IdProductList.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            IdProductList.setAdapter(adapter);
            if(list.size()>0){
                list.clear();
            }


            if (getActivity() != null) {

                SQLiteController sqLiteController = new SQLiteController(getActivity());
                sqLiteController.open();
                try {
                    long count = sqLiteController.fetchCount();
                    if (count > 0) {
                        //Order
                        Cursor invoice_c = sqLiteController.readTableItem(DbHandler.TABLE_INVOICE,DbHandler.INVOICES_DEFAULT_ID,Invoiceid);
                        if (invoice_c != null && invoice_c.moveToFirst()) {

                            do {
                                @SuppressLint("Range") String ordernumber = invoice_c.getString(invoice_c.getColumnIndex("ordernumber"));
                                @SuppressLint("Range") String invoicedate = invoice_c.getString(invoice_c.getColumnIndex("invoicedate"));
                                @SuppressLint("Range") String pricingdate = invoice_c.getString(invoice_c.getColumnIndex("pricingdate"));
                                @SuppressLint("Range") String totalamount = invoice_c.getString(invoice_c.getColumnIndex("totalamount"));
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
                                @SuppressLint("Range") String referenceorder = invoice_c.getString(invoice_c.getColumnIndex("referenceorder"));


                                SQLiteController sqLiteController2 = new SQLiteController(getActivity());
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
                                    }
                                } finally {
                                    sqLiteController.close();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                        Date date = null;
                                        Date date1 = null;
                                        try {
                                            date = sdf.parse(invoicedate);
                                            date1 = sdf.parse(pricingdate);

                                            date_order_ =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                            String delivery_order =new SimpleDateFormat("MM/dd/yyyy").format(date1);


                                            if(ordernumber.startsWith("DRFIN")){
                                                txt_page.setText("IN_"+ordernumber);
                                            }else {
                                                Double price =Double.valueOf(ordernumber);
                                                DecimalFormat format = new DecimalFormat("0.#");
                                                txt_page.setText("IN_"+format.format(price));
                                            }


                                            odAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

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

                                                            Bill_Add =line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country;

                                                            IdBill_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                                        }

                                                        if(shiptoaddressid.equals(BId)){

                                                            Ship_Add =line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country;

                                                            IdShip_tv.setText(line2+", "+stateorprovince+", \n"+city+", "+postalcode+", "+country);

                                                        }



                                                    } while (C_Address.moveToNext());
                                                }
                                            }
                                        } finally {
                                            sqLiteControllerC.close();
                                        }

                                        tvId_discountTittle.setText("Discount("+discountpercentage+"%)");
                                        tvId_TaxHeads.setText("Customer Tax("+totaltaxbase+"%)");
                                        if(ordernumber.startsWith("DRFIN")){
                                            txt_page.setText(ordernumber);
                                        }else {
                                            Double price =Double.valueOf(ordernumber);
                                            DecimalFormat format = new DecimalFormat("0.#");
                                            txt_page.setText("IN_"+format.format(price));
                                        }


                                        if(referenceorder.equals("null")){
                                            id_RefPo_tv.setText("N/A");
                                        }else {
                                            id_RefPo_tv.setText(referenceorder);
                                        }
                                        odAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));
                                        Id_sub_total.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totallineitemamount)));
                                        tvId_discount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(discountamount)));
                                        tvId_ShipmentAmt.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(freightamount)));
                                        tvId_Tax.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totaltax)));
                                        txt_IdPayable_amount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));

                                        if(list.size()>0){
                                            list.clear();
                                        }
                                        SQLiteController sqLiteController = new SQLiteController(getActivity());
                                        sqLiteController.open();
                                        try {
                                            long count = sqLiteController.fetchOrderCount();
                                            if (count > 0) {
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
                                                            String productImage ="null";
                                                            do {
                                                                @SuppressLint("Range") String product_id = product_c1.getString(product_c1.getColumnIndex("all_product_id"));

                                                                SQLiteController sqLiteController1 = new SQLiteController(getActivity());
                                                                sqLiteController1.open();
                                                                try {
                                                                    Cursor CursorAssertTable = sqLiteController1.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,product_id);
                                                                    if (CursorAssertTable.moveToFirst()) {
                                                                        do {
                                                                            String Products_assets_default = CursorAssertTable.getString(CursorAssertTable.getColumnIndex("Products_assets_default"));

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
                                                                    @SuppressLint("Range") String product_imageurl = product_c1.getString(product_c1.getColumnIndex("all_product_img"));
                                                                    @SuppressLint("Range") String product_name_str = product_c1.getString(product_c1.getColumnIndex("all_product_name"));
                                                                    @SuppressLint("Range") String all_description = product_c1.getString(product_c1.getColumnIndex("all_description"));
                                                                    @SuppressLint("Range") String all_upsc = product_c1.getString(product_c1.getColumnIndex("all_upsc"));

                                                                    list.add(new Model_SummaryList(product_name_str, product_name_str, productImage,quantity,baseamount,baseamount,priceperunit,itemistaxable,all_upsc,all_description));

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

                                        String Outpur_Str = Utils.convertToIndianCurrency(totalamount);

                                        if(!APIInvoiceHTML.equals("null")){
                                            APIInvoiceHTML = APIInvoiceHTML.replace("$COMPANYADDRESS$", " ");
                                            if(ordernumber.startsWith("DRFIN")){
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceOrderNumber$",ordernumber);
                                                APIInvoiceHTML = APIInvoiceHTML.replace("$presaleNumber$", "-");
                                            }else {
                                                Double price =Double.valueOf(ordernumber);
                                                DecimalFormat format = new DecimalFormat("0.#");
                                                txt_page.setText("IN_"+format.format(price));

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

                                            APIInvoiceHTML = APIInvoiceHTML.replace("$salesPerson$", "-");
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
                                                json_p.put("CustomerName", CustomerName);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }


                                    }
                                });

                            } while (invoice_c.moveToNext());
                        }

                    }
                } finally {
                    sqLiteController.close();
                }


            }

        }

    }

    @SuppressLint("ValidFragment")
    public static class PaymentFragment extends Fragment {

        private CoordinatorLayout cl;
        AppCompatTextView payUser_tv,paydate_tv,payMethod_tv,payOrderAmt_tv,payPaidAmt_tv,payDueAmt_tv,payTotalAmt_tv;
        LinearLayout InPayment_ll;
        AppCompatButton btn_paymentsubmit;

        LottieAnimationView txt_noInvoice;

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
            return inflater.inflate(R.layout.in_fragment_payment, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            txt_noInvoice = view.findViewById(R.id.txt_noInvoice);
            InPayment_ll = view.findViewById(R.id.InPayment_ll);
            payOrderAmt_tv = view.findViewById(R.id.payOrderAmt_tv);
            payPaidAmt_tv = view.findViewById(R.id.payPaidAmt_tv);
            payDueAmt_tv = view.findViewById(R.id.payDueAmt_tv);
            payTotalAmt_tv = view.findViewById(R.id.payTotalAmt_tv);
            paydate_tv = view.findViewById(R.id.paydate_tv);
            payMethod_tv = view.findViewById(R.id.payMethod_tv);
            payUser_tv = view.findViewById(R.id.payUser_tv);

            if (getActivity() != null) {
                if(!Invoiceid.equals("null")){
                    GetPaySummary(Invoiceid);
                }
            }
        }


        private void GetPaySummary(String Id_) {
            try {
                App.getInstance().GetPaymentsView(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                                    String paymentcollectedbyname = jsonObject.getString("paymentcollectedbyname");
                                    TotalPaymentAmt= jsonObject.getString("payable");

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                            Date date = null;
                                            try {
                                                date = sdf.parse(paymentdate);
                                                String date_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                payUser_tv.setText(paymentcollectedbyname);
                                                payMethod_tv.setText(paymentmethod);
                                                paydate_tv.setText(date_order);
                                                payOrderAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(payable)));
                                                payPaidAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(paid)));
                                                payDueAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(due)));
                                                payTotalAmt_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(payable)));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
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

        APIInvoiceHTML = APIInvoiceHTML.replace("$InvoiceProduct$", localRowhtml);

        APIInvoiceHTML=APIInvoiceHTML.replace("$CompanyLogo$",Companylogo);

        FileManager.getInstance().cleanTempFolder(ActivityInvoiceDetails.this);
        // Create Temp File to save Pdf To
        final File savedPDFFile = FileManager.getInstance().createTempFile(ActivityInvoiceDetails.this, "pdf", false);
        // Generate Pdf From Html

        PDFUtil.generatePDFFromHTML(ActivityInvoiceDetails.this, savedPDFFile, "<!DOCTYPE html>\n" +
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
                Intent intentPdfViewer = new Intent(ActivityInvoiceDetails.this, PdfViewerActivity.class);
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


    private void GetInvoiceSummary(String Id_) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    App.getInstance().PostPresaleOrder(Id_,"",token,new Callback() {
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
                                            JSONObject jsData = jsonObject.getJSONObject("data");
                                            String submitdate= jsData.getString("submitdate");
                                            if (jsData.has("shipments") && !jsData.isNull("shipments")) {
                                                JSONObject jsShipments = jsData.getJSONObject("shipments");
                                                deliveryshipmenttypeid= jsShipments.getString("deliveryshipmenttypeid");
                                                deliverywarehouseid= jsShipments.getString("warehouseid");
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

    private String getCompanyLogo() {
        InSharedPreferences = getSharedPreferences(Activity_OrdersHistory.ohPref, MODE_PRIVATE);
        if (InSharedPreferences.contains(Activity_OrdersHistory.CmpLogo)) {
            return InSharedPreferences.getString(Activity_OrdersHistory.CmpLogo, null);
        }
        return " ";
    }

}
