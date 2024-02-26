package com.example.arcomdriver.salesreturn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.model.Model_SummaryList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 * Created on : 07 Feb 2024*/
public class ActivitySalesDetails extends Activity_Menu {

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
    public static  String TAG="ActivityInvoiceTab",SalesID,user_id,token,user_email;
    private SQLiteController sqLiteController;
    private static AppCompatTextView Inpayment_tv,odCustomerName_tv,odStatus_tv,odDate_tv,odDeliveryDate_tv,odAmt_tv;

    public static ArrayList<Model_SummaryList> list = new ArrayList<>();

    public static AlertDialog Progress_dialog;

    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_saledetails, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.GONE);
        In_download_ic.setVisibility(View.GONE);
        In_print_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySalesDetails.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivitySalesDetails.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        odCustomerName_tv = findViewById(R.id.InCustomerName_tv);
        Inpayment_tv = findViewById(R.id.Inpayment_tv);
        odStatus_tv = findViewById(R.id.InStatus_tv);
        odDate_tv = findViewById(R.id.InDate_tv);
        odDeliveryDate_tv = findViewById(R.id.IndueDate_tv);
        odAmt_tv = findViewById(R.id.InAmt_tv);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SalesID = extras.getString("SalesID");

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
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesDetails.this, Activity_OrdersHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesDetails.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesDetails.this, Activity_InvoiceHistory.class));
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
                                            bundle.putString("SalesID",SalesID);
                                            Intent in = new Intent(getActivity(), ActivitySalesDetails.class);
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

        private static final String TAG = "Summary : ";

        AppCompatTextView tv_saleReturn,tvsd_saleOrder,tvsd_saleRep,tvsd_saleStatus,tvsd_saleDate,tvsd_saleMode,tvsd_saleLoc;

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
            return inflater.inflate(R.layout.salesdetails_fragment_summary, container, false);
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
            tv_saleReturn = view.findViewById(R.id.tvsd_saleReturn);
            tvsd_saleOrder = view.findViewById(R.id.tvsd_saleOrder);
            tvsd_saleRep = view.findViewById(R.id.tvsd_saleRep);
            tvsd_saleStatus = view.findViewById(R.id.tvsd_saleStatus);
            tvsd_saleDate = view.findViewById(R.id.tvsd_saleDate);
            tvsd_saleMode = view.findViewById(R.id.tvsd_saleMode);
            tvsd_saleLoc = view.findViewById(R.id.tvsd_saleLoc);

            adapter = new Adapter_SummaryList(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            IdProductList.setLayoutManager(layoutManager);
            IdProductList.setItemAnimator(new DefaultItemAnimator());
            IdProductList.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            IdProductList.setAdapter(adapter);
            if (getActivity() != null) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            App.getInstance().GetSalesInfo(SalesID,token,new Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getActivity().runOnUiThread(new Runnable() {
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
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if(list.size()>0){
                                                    list.clear();
                                                }

                                                try {
                                                    final JSONObject jsonObject = new JSONObject(res);
                                                    JSONObject jsData = jsonObject.getJSONObject("data");
                                                    String returnnumber= jsData.getString("returnnumber");
                                                    String customername= jsData.getString("customername");
                                                    String returnstatus= jsData.getString("returnstatus");
                                                    String ordernumber= jsData.getString("ordernumber");
                                                    String returneddate= jsData.getString("returneddate");
                                                    String billingaddress= jsData.getString("billingaddress");
                                                    String shippingaddress= jsData.getString("shippingaddress");
                                                    String shipnotes= jsData.getString("shipnotes");
                                                    String pickpacknotes= jsData.getString("pickpacknotes");
                                                    String termsandconditions= jsData.getString("termsandconditions");
                                                    String memo= jsData.getString("memo");
                                                    String subtotal= jsData.getString("subtotal");
                                                    String taxpercentage= jsData.getString("taxpercentage");
                                                    String shippingamount= jsData.getString("shippingamount");
                                                    String taxamount= jsData.getString("taxamount");
                                                    String totalamount= jsData.getString("totalamount");
                                                    String salesrep= jsData.getString("salesrep");
                                                    String paymentmode= jsData.getString("paymentmode");
                                                    String warehousename= jsData.getString("warehousename");



                                                    JSONArray salesreturnproduct = jsData.getJSONArray("salesreturnproduct");

                                                    for (int i = 0; i < salesreturnproduct.length(); i++) {
                                                        JSONObject js = salesreturnproduct.getJSONObject(i);
                                                        String productname = js.getString("productname");
                                                        String quantity = js.getString("returnedquantity");
                                                        String baseamount = js.getString("priceperunit");
                                                        String priceperunit = js.getString("priceperunit");
                                                        String itemistaxable = js.getString("isitemtaxable");
                                                        String productid = js.getString("productid");

                                                        SQLiteController sqLiteController = new SQLiteController(getActivity());
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

                                                                        SQLiteController sqLiteController1 = new SQLiteController(getActivity());
                                                                        sqLiteController1.open();
                                                                        try {
                                                                            Cursor CursorAssertTable = sqLiteController1.readOrderItem(DbHandler.TABLE_PRODUCT_ASSERT,DbHandler.PRODUCT_ASSERT_PRODUCTSID,productid);
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

                                                            }
                                                        } finally {
                                                            sqLiteController.close();
                                                        }

                                                    }

                                                    tvId_TaxHeads.setText("Customer Tax("+taxpercentage+"%)");
                                                    Id_sub_total.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(subtotal)));
                                                    tvId_ShipmentAmt.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(shippingamount)));
                                                    tvId_Tax.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(taxamount)));
                                                    txt_IdPayable_amount.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(totalamount)));


                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                                                    Date date = null;
                                                    try {
                                                        date = sdf.parse(returneddate);

                                                        String delivery_order =new SimpleDateFormat("MM/dd/yyyy").format(date);

                                                        odAmt_tv.setText(delivery_order);
                                                        tvsd_saleDate.setText(delivery_order);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    tv_saleReturn.setText(returnnumber);

                                                    txt_page.setText(returnnumber);
                                                    tvsd_saleOrder.setText(ordernumber);
                                                    tvsd_saleRep.setText(salesrep);
                                                    tvsd_saleStatus.setText(returnstatus);

                                                    tvsd_saleMode.setText(paymentmode);
                                                    tvsd_saleLoc.setText(warehousename);

                                                    odCustomerName_tv.setText(customername);
                                                    odStatus_tv.setText(returnstatus);
                                                    odDate_tv.setText(returnnumber);
                                                    odDeliveryDate_tv.setText(ordernumber);

                                                    IdBill_tv.setText(billingaddress);
                                                    IdShip_tv.setText(shippingaddress);


                                                    if(shipnotes.equals("")&&shipnotes.isEmpty()){
                                                        idNotes_tv.setText("N/A");
                                                    }else {
                                                        if(shipnotes.equals("null")){
                                                            idNotes_tv.setText("N/A");
                                                        }else {
                                                            idNotes_tv.setText(shipnotes);
                                                        }

                                                    }

                                                    if(pickpacknotes.equals("")&&pickpacknotes.isEmpty()){
                                                        idship_tv.setText("N/A");
                                                    }else {
                                                        if(pickpacknotes.equals("null")){
                                                            idship_tv.setText("N/A");
                                                        }else {
                                                            idship_tv.setText(pickpacknotes);
                                                        }

                                                    }

                                                    if(termsandconditions.equals("")&&termsandconditions.isEmpty()){
                                                        idTerms_tv.setText("N/A");
                                                    }else {
                                                        if(termsandconditions.equals("null")){
                                                            idTerms_tv.setText("N/A");
                                                        }else {
                                                            idTerms_tv.setText(termsandconditions);
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
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (Progress_dialog != null) {
                                                    if (Progress_dialog.isShowing()) {
                                                        Progress_dialog.dismiss();
                                                    }
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


            }

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
