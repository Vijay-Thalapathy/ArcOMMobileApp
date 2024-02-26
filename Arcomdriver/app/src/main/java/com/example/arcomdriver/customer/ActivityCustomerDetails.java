package com.example.arcomdriver.customer;

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
import com.example.arcomdriver.adapter.Adapter_AddressHistory;
import com.example.arcomdriver.adapter.Adapter_NotesList;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_AddressHistory;
import com.example.arcomdriver.model.Model_NotesList;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
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
 * Created on : 15 Jan 2023*/
public class ActivityCustomerDetails extends Activity_Menu {

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
    public static String token,user_email,user_id,salesrepid,CustomerId,ordernumber,totalamount,submitstatus;
    private SQLiteController sqLiteController;
    private static AppCompatTextView cdvEmail_tv,CdvMob_tv,CsStatus_tv,CsEmail_tv;
    @SuppressLint({"CheckResult", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_customerdetailstab, null, false);
        parentView.addView(contentView,0);
        Od_edit_ic.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CsEmail_tv = findViewById(R.id.CsEmail_tv);
        CsStatus_tv = findViewById(R.id.CsStatus_tv);
        cdvEmail_tv = findViewById(R.id.cdvEmail_tv);
        CdvMob_tv = findViewById(R.id.CdvMob_tv);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CustomerId = extras.getString("CustomerId");

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

        findViewById(R.id.Od_edit_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CustomerId",CustomerId);
                Intent in = new Intent(ActivityCustomerDetails.this, Activity_UpdateCustomer.class);
                in.putExtras(bundle);
                startActivity(in);
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCustomerDetails.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCustomerDetails.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCustomerDetails.this, Activity_OrdersHistory.class);
                startActivity(intent);
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
                                            bundle.putString("CustomerId",CustomerId);
                                            Intent in = new Intent(getActivity(), ActivityCustomerDetails.class);
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

        private AppCompatTextView txt_no_record;
        private Adapter_AddressHistory adapter_address;
        private ArrayList<Model_AddressHistory> list_address = new ArrayList<>();

        private CoordinatorLayout cl;
        private RecyclerView rc_CsAddressList;
        private AppCompatTextView cd_lastName1_tv,cd_firstName1_tv,cd_taxcode_tv,cd_taxApp_tv,cd_secContact_tv,cd_secEmail_tv,cd_primaryContact_tv,cd_name_tv,cd_industry_tv,cd_website_tv,cd_primaryEmail_tv;

        private static final String TAG = "Summary : ";

        LinearLayout firstname_ll,lastname_ll;

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
            return inflater.inflate(R.layout.cs_fragment_summary, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            //txtNoRecord1 = view.findViewById(R.id.txt_no_record);
            cd_name_tv = view.findViewById(R.id.cd_name_tv);
            cd_firstName1_tv = view.findViewById(R.id.cd_firstName1_tv);
            cd_lastName1_tv = view.findViewById(R.id.cd_lastName1_tv);
            lastname_ll = view.findViewById(R.id.lastname_ll);
            firstname_ll = view.findViewById(R.id.firstname_ll);
            cd_industry_tv = view.findViewById(R.id.cd_industry_tv);
            cd_website_tv = view.findViewById(R.id.cd_website_tv);
            cd_primaryEmail_tv = view.findViewById(R.id.cd_primaryEmail_tv);
            cd_primaryContact_tv = view.findViewById(R.id.cd_primaryContact_tv);
            cd_secEmail_tv = view.findViewById(R.id.cd_secEmail_tv);
            cd_secContact_tv = view.findViewById(R.id.cd_secContact_tv);
            cd_taxApp_tv = view.findViewById(R.id.cd_taxApp_tv);
            cd_taxcode_tv = view.findViewById(R.id.cd_taxcode_tv);

            rc_CsAddressList = view.findViewById(R.id.rc_CsAddressList);

            adapter_address = new Adapter_AddressHistory(list_address);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rc_CsAddressList.setLayoutManager(layoutManager);
            rc_CsAddressList.setItemAnimator(new DefaultItemAnimator());
            rc_CsAddressList.addItemDecoration(new ItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            rc_CsAddressList.setAdapter(adapter_address);

            if (getActivity() != null) {
                GetCustomerSummary(CustomerId);
            }

        }

        private void GetCustomerSummary(String Id_) {
            Utils.getInstance().loadingDialog(getActivity(), "Please wait.");
            try {
                App.getInstance().GetCustomerSummary(Id_,token,new Callback(){

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
                                String customernumber = jsdata.getString("customernumber");
                                String id = jsdata.getString("id");
                                String customername = jsdata.getString("customername");
                                String website = jsdata.getString("website");
                                String industry = jsdata.getString("industry");
                                String status = jsdata.getString("status");
                                String istaxable = jsdata.getString("istaxable");
                                String taxcode = jsdata.getString("taxcode");
                                String customertypeid = jsdata.getString("customertypeid");

                               // JSONObject jsContact= jsdata.getJSONObject("contactInfo");
                                JSONArray jsContact = jsdata.getJSONArray("contactInfo");
                                JSONObject jsContact2 = jsContact.getJSONObject(0);
                                String emailaddress1 = jsContact2.getString("emailaddress1");
                                String telephone1 = jsContact2.getString("telephone1");
                                String emailaddress2 = jsContact2.getString("emailaddress2");
                                String telephone2 = jsContact2.getString("telephone2");
                                String firstname = jsContact2.getString("firstname");
                                String lastname = jsContact2.getString("lastname");

                                JSONArray options = jsdata.getJSONArray("addressInfo");
                                if(list_address.size()>0)list_address.clear();
                                if (options.length() > 0) {
                                    for (int i=0; i<options.length(); i++) {
                                        JSONObject js = options.getJSONObject(i);
                                        String id_address = js.getString("id");
                                        String line2 = js.getString("line2");
                                        String country = js.getString("country");
                                        String city = js.getString("city");
                                        String stateorprovince = js.getString("stateorprovince");
                                        String postalcode = js.getString("postalcode");
                                        String addresstypecode = js.getString("addresstypecode");
                                        String isprimaryaddress = js.getString("isprimaryaddress");
                                        if (getActivity() != null) {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void run() {
                                                    if(isprimaryaddress.equals("true")){
                                                        list_address.add(new Model_AddressHistory(id_address,line2, country,city,stateorprovince,postalcode,addresstypecode));
                                                        adapter_address.notifyDataSetChanged();
                                                    }
                                                    //  Utils.getInstance().dismissDialog();
                                                }
                                            });

                                        }


                                    }

                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(customertypeid.equals("1")) {
                                            //business
                                            firstname_ll.setVisibility(View.VISIBLE);
                                            lastname_ll.setVisibility(View.VISIBLE);

                                            if(firstname.equals("null")||firstname.isEmpty()){
                                                cd_firstName1_tv.setText("N/A");
                                            }else {

                                                cd_firstName1_tv.setText(firstname);
                                            }

                                            if(lastname.equals("null")||lastname.isEmpty()){
                                                cd_lastName1_tv.setText("N/A");
                                            }else {

                                                cd_lastName1_tv.setText(lastname);
                                            }


                                        }else {
                                            firstname_ll.setVisibility(View.GONE);
                                            lastname_ll.setVisibility(View.GONE);
                                        }


                                        if(industry.equals("null")||industry.isEmpty()){
                                            cd_industry_tv.setText("N/A");
                                        }else {
                                            cd_industry_tv.setText(industry);
                                        }

                                        if(website.equals("null")||website.isEmpty()){
                                            cd_website_tv.setText("N/A");
                                        }else {
                                            cd_website_tv .setText(website);
                                        }

                                        if(emailaddress2.equals("null")||emailaddress2.isEmpty()){
                                            cd_secEmail_tv.setText("N/A");
                                        }else {
                                            cd_secEmail_tv .setText(emailaddress2);
                                        }

                                        if(telephone2.equals("null")||telephone2.isEmpty()){
                                            cd_secContact_tv.setText("N/A");
                                        }else {
                                            cd_secContact_tv .setText(telephone2);
                                        }

                                        if(emailaddress1.equals("null")||emailaddress1.isEmpty()){
                                            cd_primaryEmail_tv.setText("N/A");
                                        }else {
                                            cd_primaryEmail_tv.setText(emailaddress1);
                                        }

                                        if(telephone1.equals("null")||telephone1.isEmpty()){
                                            cd_primaryContact_tv.setText("N/A");
                                        }else {
                                            cd_primaryContact_tv.setText(telephone1);
                                        }


                                        cd_name_tv.setText(customername);

                                        if(emailaddress1.equals("null")||emailaddress1.isEmpty()){
                                            cdvEmail_tv.setText("N/A");
                                        }else {
                                            cdvEmail_tv .setText(emailaddress1);
                                        }



                                        CdvMob_tv .setText(telephone1);

                                        txt_page .setText(customernumber);

                                        CsStatus_tv  .setText(status);


                                        if(emailaddress1.equals("null")||emailaddress1.isEmpty()){
                                            CsEmail_tv.setText("N/A");
                                        }else {
                                            CsEmail_tv .setText(emailaddress1);
                                        }


                                        if(istaxable.equals("true")){
                                            cd_taxApp_tv  .setText("Yes");
                                        }else {
                                            cd_taxApp_tv  .setText("No");
                                        }




                                        if(taxcode.equals("null")||taxcode.isEmpty()){
                                            cd_taxcode_tv.setText("N/A");
                                        }else {
                                            cd_taxcode_tv  .setText(taxcode);
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
