package com.example.arcomdriver.products;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
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
import com.example.arcomdriver.adapter.Adapter_ProductWarehoseHistory;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_AddressHistory;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.model.Model_ProductWarehouseHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

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
 * @author : SivaramYogesh*/
public class ActivityProductDetails extends Activity_Menu {

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
    public static  String TAG="ActivityCustomerTab";
    public static String token,user_email,user_id,salesrepid,ProductId,ordernumber,totalamount,submitstatus;
    private SQLiteController sqLiteController;
    private static AppCompatTextView cdvEmail_tv,CdvMob_tv,CsStatus_tv,CsEmail_tv;
    //Toolbar toolbar;
    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_productdetailstab);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_productdetailstab, null, false);
        parentView.addView(contentView,0);
        Od_edit_ic.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //CsName_tv = findViewById(R.id.CsName_tv);
       // toolbar = findViewById(R.id.toolbar);
        CsEmail_tv = findViewById(R.id.CsEmail_tv);
        CsStatus_tv = findViewById(R.id.CsStatus_tv);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ProductId = extras.getString("ProductId");

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
                    //  Get_OrderList(token);

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
        mSectionsPagerAdapter.addFragment(new PRicingStockFragment());
        //Set adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);



        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductDetails.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });*/


        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductDetails.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductDetails.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProductDetails.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.Od_edit_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ProductId",ProductId);
                Intent in = new Intent(ActivityProductDetails.this, Activity_UpdateProduct.class);
                in.putExtras(bundle);
                startActivity(in);
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

                    }/*else {
                        Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                    }*/
                }
            });

            if (Connectivity.isConnected(getActivity()) &&
                    Connectivity.isConnectedFast(getActivity())) {
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(list_notes.size()>0)list_notes.clear();
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
            if (getActivity() != null) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
                    }
                });
            }

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
                            if (getActivity() != null) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final JSONObject jsonObject = new JSONObject(res);
                                            boolean succeeded = jsonObject.getBoolean("succeeded");

                                            if ( succeeded == true) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("ProductId",ProductId);
                                                Intent in = new Intent(getActivity(), ActivityProductDetails.class);
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

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @SuppressLint("ValidFragment")
    public static class SummaryFragment extends Fragment {

        private ArrayList<Model_AddressHistory> list_address = new ArrayList<>();
        private AppCompatTextView pds_Dec_tv,pds_ups_tv,pds_type,pds_class_tv,pds_brand,pds_vendor_tv,cd_primaryEmail_tv;

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
            return inflater.inflate(R.layout.pd_fragment_summary, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            pds_Dec_tv = view.findViewById(R.id.pds_Dec_tv);
            pds_ups_tv = view.findViewById(R.id.pds_ups_tv);
            pds_type = view.findViewById(R.id.pds_type);
            pds_class_tv = view.findViewById(R.id.pds_class_tv);
            pds_brand = view.findViewById(R.id.pds_brand);
            pds_vendor_tv = view.findViewById(R.id.pds_vendor_tv);

            if (getActivity() != null) {
                GetProductsSummary(ProductId);
            }

        }

        private void GetProductsSummary(String Id_) {
            Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetProductsSummary(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
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

                            if (getActivity() != null) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.getInstance().dismissDialog();
                                    }
                                });
                            }


                            try {
                                JSONObject jsonObject = new JSONObject(res);

                                JSONObject jsdata = jsonObject.getJSONObject("data");
                                String id = jsdata.getString("id");
                                String productnumber = jsdata.getString("productnumber");
                                String productname = jsdata.getString("productname");
                                String producttypeid = jsdata.getString("producttypeid");
                                String description = jsdata.getString("description");
                                String skucode = jsdata.getString("skucode");
                                String typename = jsdata.getString("typename");
                                String brandname = jsdata.getString("brandname");
                                String upccode = jsdata.getString("upccode");
                                String price = jsdata.getString("price");
                                String productimage = jsdata.getString("productimage");
                                String quantityonhand = jsdata.getString("quantityonhand");
                                String status = jsdata.getString("status");
                                String vendorname = jsdata.getString("vendorname");
                                String classname = jsdata.getString("classname");
                                String committed = jsdata.getString("committed");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CsStatus_tv  .setText(status);
                                        txt_page  .setText(productname);
                                        CsEmail_tv  .setText(productnumber);



                                        if(upccode.equals("null")){
                                            pds_ups_tv.setText("N/A");
                                        }else {
                                            pds_ups_tv  .setText(upccode);
                                        }



                                        if(classname.equals("null")){
                                            pds_class_tv.setText("N/A");
                                        }else {
                                            pds_class_tv  .setText(classname);
                                        }


                                        if(vendorname.equals("null")){
                                            pds_vendor_tv.setText("N/A");
                                        }else {
                                            pds_vendor_tv .setText(vendorname);
                                        }

                                        if(typename.equals("null")){
                                            pds_type.setText("N/A");
                                        }else {
                                            pds_type  .setText(typename);
                                        }

                                        if(description.equals("")){
                                            pds_Dec_tv.setText("N/A");
                                        }else {
                                            pds_Dec_tv  .setText(description);
                                        }

                                        if(brandname.equals("null")){
                                            pds_brand.setText("N/A");
                                        }else {
                                            pds_brand  .setText(brandname);
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




    }


    @SuppressLint("ValidFragment")
    public static class PRicingStockFragment extends Fragment {


        private CoordinatorLayout cl;
        AppCompatImageView pds_image;
        AppCompatTextView pds_itemCost_tv;

        private RecyclerView mRecyclerView;
        private AppCompatTextView txt_no_record;

        LinearLayout ware_ll;

        Adapter_ProductWarehoseHistory adapter_warehouse;
        ArrayList<Model_ProductWarehouseHistory> list_Warehouse = new ArrayList<>();


        public PRicingStockFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.pd_fragment_pricing, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            pds_image = view.findViewById(R.id.pds_image);
            pds_itemCost_tv = view.findViewById(R.id.pds_itemCost_tv);

            mRecyclerView = view.findViewById(R.id.rc_WarehouseList);
            txt_no_record = view.findViewById(R.id.txt_no_record);
            ware_ll = view.findViewById(R.id.ware_ll);

            if (Activity_OrdersHistory.WarehouseFlag.equals("false")){
                //Hide Warehouse
                ware_ll.setVisibility(View.GONE);
            }else {
                //Show Warehouse
                ware_ll.setVisibility(View.VISIBLE);

            }

            adapter_warehouse = new Adapter_ProductWarehoseHistory(list_Warehouse);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(adapter_warehouse);



            if (getActivity() != null) {
                GetProductsPrice(ProductId);
            }
        }

        private void GetProductsPrice(String Id_) {
           // Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetProductsSummary(Id_,token,new Callback(){

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //  Utils.getInstance().dismissDialog();
                            }
                        });

                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String res = response.body().string();

                            try {
                                JSONObject jsonObject = new JSONObject(res);

                                JSONObject jsdata = jsonObject.getJSONObject("data");
                                String id = jsdata.getString("id");
                                String productnumber = jsdata.getString("productnumber");
                                String productname = jsdata.getString("productname");
                                String producttypeid = jsdata.getString("producttypeid");
                                String description = jsdata.getString("description");
                                String skucode = jsdata.getString("skucode");
                                String typename = jsdata.getString("typename");
                                String brandname = jsdata.getString("brandname");
                                String upccode = jsdata.getString("upccode");
                                String price = jsdata.getString("price");
                              //  String productimage = jsdata.getString("productimage");
                                String quantityonhand = jsdata.getString("quantityonhand");
                                String status = jsdata.getString("status");
                                String vendorname = jsdata.getString("vendorname");
                                String classname = jsdata.getString("classname");
                                String committed = jsdata.getString("committed");


                                JSONArray options = jsdata.getJSONArray("warehouseproductstock");
                                if(list_Warehouse.size()>0)list_Warehouse.clear();
                                if (options.length() > 0) {

                                    for (int i=0; i<options.length(); i++) {
                                        JSONObject js = options.getJSONObject(i);
                                        String idw = js.getString("id");
                                        String productid = js.getString("productid");
                                        String warehouseid = js.getString("warehouseid");
                                        String onhand = js.getString("onhand");
                                        String committed_w = js.getString("committed");
                                        String available = js.getString("available");
                                        String locationtypeid = js.getString("locationtypeid");
                                        String warehousenumber = js.getString("warehousenumber");

                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void run() {
                                                    txt_no_record.setVisibility(View.GONE);
                                                    list_Warehouse.add(new Model_ProductWarehouseHistory(locationtypeid,warehousenumber,onhand,committed_w,available,""));
                                                    adapter_warehouse.notifyDataSetChanged();
                                                    //  Utils.getInstance().dismissDialog();
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


                                if (getActivity() != null) {
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            JSONArray data2 = null;
                                            try {
                                                data2 = jsdata.getJSONArray("digitalassetsinfo");
                                                String productimage33 = null;
                                                for (int k = 0; k < data2.length(); k++) {
                                                    JSONObject js1 = data2.getJSONObject(k);
                                                    String isdefault = js1.getString("isdefault");

                                                    if (isdefault.equals("null")) {

                                                    }else {
                                                        if(isdefault.equals("true")){
                                                            productimage33 = js1.getString("productimage");
                                                            System.out.println("productimage---"+productimage33);
                                                        }
                                                    }


                                                }

                                                pds_itemCost_tv  .setText("$ "+ Utils.truncateDecimal(Double.valueOf(price)));

                                                String imageUri = Const.ImageProducts + productimage33;
                                                System.out.println("I----"+imageUri);
                                                Picasso.with(getActivity()).load(imageUri).fit().centerInside()
                                                        .placeholder(R.drawable.image_placeholder)
                                                        .error(R.drawable.image_placeholder)
                                                        .into(pds_image);

                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }





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
