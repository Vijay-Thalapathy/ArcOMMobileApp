package com.example.arcomdriver.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.arcomdriver.adapter.Adapter_CustomerInv;
import com.example.arcomdriver.adapter.Adapter_CustomerPricing;
import com.example.arcomdriver.adapter.Adapter_InventoryProducts;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_CustInv;
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.model.Model_InventoryProductList;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created on : 24 Jan 2024*/
public class ActivityCustomerPricingInfo extends Activity_Menu {

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

    public static  String TAG="ActivityCustomerInfoTab";

    public static String token,user_email,user_id,PriceID;


    private SQLiteController sqLiteController;

    public AlertDialog Progress_dialog;


    public static RecyclerView rcFrInventoryList,rcCustomerList;
    public static LottieAnimationView txt_Inventory_no_record,txt_cust_no_record;

    public static Adapter_InventoryProducts adapter_inventoryProducts;

    public static ArrayList<Model_InventoryProductList> list_InventoryProducts = new ArrayList<>();


    public static Adapter_CustomerInv adapter_CustomerInv;

    public static ArrayList<Model_CustInv> listCustomerInv = new ArrayList<>();

    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_customerpricinginfostab, null, false);
        parentView.addView(contentView,0);
        Od_edit_ic.setVisibility(View.GONE);
        txt_page.setText("Customer Pricing Info");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PriceID = extras.getString("PriceID");

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCustomerPricingInfo.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivityCustomerPricingInfo.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();


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
        mSectionsPagerAdapter.addFragment(new GeneralFragment());
        mSectionsPagerAdapter.addFragment(new InventoryFragment());
        mSectionsPagerAdapter.addFragment(new CustomerFragment());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        if (Connectivity.isConnected(ActivityCustomerPricingInfo.this) &&
                Connectivity.isConnectedFast(ActivityCustomerPricingInfo.this)) {

            if (getApplicationContext() != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            App.getInstance().GetCustomerPricingInfo(PriceID,token,new Callback(){

                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    if (response.isSuccessful()) {

                                        String res = response.body().string();
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            JSONObject options = jsonObject.getJSONObject("data");
                                            JSONArray JrProductsCount = options.getJSONArray("orderProductitems");
                                            JSONArray JrCustomerCount = options.getJSONArray("orderCustomeritems");
                                            String pricename = options.getString("pricename");
                                            String startdatetime = options.getString("startdatetime");
                                            String enddatetime = options.getString("enddatetime");
                                            String isactive = options.getString("isactive");
                                            String description = options.getString("description");

                                            if(list_InventoryProducts.size()>0)list_InventoryProducts.clear();
                                            if(listCustomerInv.size()>0)listCustomerInv.clear();

                                            if (JrProductsCount.length() > 0) {
                                                for (int i=0; i<JrProductsCount.length(); i++) {
                                                    JSONObject js = JrProductsCount.getJSONObject(i);
                                                    String productname = js.getString("productname");
                                                    String productimage = js.getString("productimage");
                                                    String unitmeasure = js.getString("unitmeasure");
                                                    String price = js.getString("price");
                                                    String margin = js.getString("margin");
                                                    String profit = js.getString("profit");
                                                    String customerprice = js.getString("customerprice");
                                                    String vendorcost = js.getString("vendorcost");
                                                    String Smargin = js.getString("sellingpricemargin");
                                                    String Sprofit = js.getString("sellingpriceprofit");

                                                    list_InventoryProducts.add(new Model_InventoryProductList(productname,productimage, unitmeasure, price,margin,profit,customerprice,vendorcost,Smargin,Sprofit));

                                                }

                                            }else {
                                                txt_Inventory_no_record.setVisibility(View.VISIBLE);
                                            }

                                            if (JrCustomerCount.length() > 0) {
                                                String customername ="";

                                                for (int i=0; i<JrCustomerCount.length(); i++) {
                                                    JSONObject js = JrCustomerCount.getJSONObject(i);
                                                    String groupid = js.getString("groupname");
                                                    String zoneid = js.getString("zonename");
                                                    String Cstartdatetime = js.getString("startdatetime");
                                                    String Cenddatetime = js.getString("enddatetime");

                                                    customername = js.getString("businessname").equals("null") ?  js.getString("customername") : js.getString("businessname");


                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                    Date date = null;
                                                    Date date1 = null;
                                                    String startDate ="";
                                                    String endDate ="";
                                                    try {
                                                        date = sdf.parse(Cstartdatetime);
                                                        date1 = sdf.parse(Cenddatetime);
                                                        startDate =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date);
                                                        endDate  =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date1);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    listCustomerInv.add(new Model_CustInv(customername,startDate,endDate,groupid,zoneid));

                                                }

                                            }else {
                                                txt_cust_no_record.setVisibility(View.VISIBLE);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void run() {

                                                    adapter_inventoryProducts.notifyDataSetChanged();
                                                    adapter_CustomerInv.notifyDataSetChanged();

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
                });
            }

        }

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
    public static class GeneralFragment extends Fragment {

        private CoordinatorLayout cl;
        private RecyclerView mRecyclerView;
        private LottieAnimationView txt_no_record;

        private static final String TAG = "Notes : ";

         AppCompatTextView cpd_name,cpd_noProducts,cpd_noCustomer,cpd_noStatus,cpd_noDescription,cpd_startDate,cpd_endDate;


        public GeneralFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_general, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            cpd_name = view.findViewById(R.id.cpd_name);
            cpd_noProducts = view.findViewById(R.id.cpd_noProducts);
            cpd_noCustomer = view.findViewById(R.id.cpd_noCustomer);
            cpd_noStatus = view.findViewById(R.id.cpd_noStatus);
            cpd_noDescription = view.findViewById(R.id.cpd_noDescription);
            cpd_startDate = view.findViewById(R.id.cpd_startDate);
            cpd_endDate = view.findViewById(R.id.cpd_endDate);





            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                App.getInstance().GetCustomerPricingInfo(PriceID,token,new Callback(){

                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        if (response.isSuccessful()) {

                                            String res = response.body().string();
                                            try {
                                                JSONObject jsonObject = new JSONObject(res);
                                                JSONObject options = jsonObject.getJSONObject("data");
                                                JSONArray JrProductsCount = options.getJSONArray("orderProductitems");
                                                JSONArray JrCustomerCount = options.getJSONArray("orderCustomeritems");
                                                String pricename = options.getString("pricename");
                                                String startdatetime = options.getString("startdatetime");
                                                String enddatetime = options.getString("enddatetime");
                                                String isactive = options.getString("isactive");
                                                String description = options.getString("description");

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void run() {

                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                        Date date = null;
                                                        Date date1 = null;
                                                        try {
                                                            date = sdf.parse(startdatetime);
                                                            date1 = sdf.parse(enddatetime);
                                                            String startDate =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date);
                                                            String endDate  =new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(date1);

                                                            cpd_startDate.setText(startDate);
                                                            cpd_endDate.setText(endDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }


                                                        cpd_name.setText(pricename);
                                                        cpd_noProducts.setText(String.valueOf(JrProductsCount.length()));
                                                        cpd_noCustomer.setText(String.valueOf(JrCustomerCount.length()));


                                                        if(isactive.equals("true")){
                                                            cpd_noStatus.setText("Active");
                                                            cpd_noStatus.setBackgroundResource(R.drawable.shape_btngreen_bg);
                                                        }else {
                                                            cpd_noStatus.setText("InActive");
                                                            cpd_noStatus.setBackgroundResource(R.drawable.shape_redbg);
                                                        }


                                                        if(description.equals("")&&description.isEmpty()){
                                                            cpd_noDescription.setText("N/A");
                                                        }else {
                                                            if(description.equals("null")){
                                                                cpd_noDescription.setText("N/A");
                                                            }else {
                                                                cpd_noDescription.setText(description);
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
                    });
                }

            }

        }

    }

    @SuppressLint("ValidFragment")
    public static class InventoryFragment extends Fragment {


        private CoordinatorLayout cl;

        private static final String TAG = "Summary : ";

        public InventoryFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_inventory, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            rcFrInventoryList = view.findViewById(R.id.rcFrInventoryList);
            txt_Inventory_no_record = view.findViewById(R.id.txt_Inventory_no_record);

            adapter_inventoryProducts = new Adapter_InventoryProducts(list_InventoryProducts);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rcFrInventoryList.setLayoutManager(layoutManager);
            rcFrInventoryList.setItemAnimator(new DefaultItemAnimator());
            rcFrInventoryList.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            rcFrInventoryList.setAdapter(adapter_inventoryProducts);

            rcFrInventoryList.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rcFrInventoryList, new RecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                }

                @Override
                public void onLongClick(View view, int position) {

                }

            }));


        }

    }


    @SuppressLint("ValidFragment")
    public static class CustomerFragment extends Fragment {


        private CoordinatorLayout cl;

        private static final String TAG = "Summary : ";

        public CustomerFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cpcustomer, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            rcCustomerList = view.findViewById(R.id.rcCustomerList);
            txt_cust_no_record = view.findViewById(R.id.txt_cust_no_record);

            adapter_CustomerInv = new Adapter_CustomerInv(listCustomerInv);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rcCustomerList.setLayoutManager(layoutManager);
            rcCustomerList.setItemAnimator(new DefaultItemAnimator());
            rcCustomerList.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            rcCustomerList.setAdapter(adapter_CustomerInv);

            rcCustomerList.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rcCustomerList, new RecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                }

                @Override
                public void onLongClick(View view, int position) {

                }

            }));



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
