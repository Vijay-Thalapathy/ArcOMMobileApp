package com.example.arcomdriver.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_CustomerHistory;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_CustomerHistory;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.products.Activity_UpdateProduct;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Jan 2023*/
public class Activity_CustomerHistory extends Activity_Menu {
    private CoordinatorLayout cl;
    Adapter_CustomerHistory adapter;
    ArrayList<Model_CustomerHistory> listInvoice = new ArrayList<>();
    private RecyclerView mRecyclerView;
    AppCompatTextView InTotal_tv,InPaid_tv,InUnPaid_tv;
    private SearchView searchBar;
    ArrayList<Model_CustomerHistory> filteredModelList = new ArrayList<>();
    private SwipeRefreshLayout srl;
    Spinner CsStatus_sp;
    String  str_ordersfill ="All";
    int InActive =0;
    int Active =0;
    String user_id,token,user_email;
    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusName = new ArrayList<>();

    public AlertDialog Progress_dialog;
    LottieAnimationView img_noCustomer;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_customerlist, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Customers");
        createOrder_img.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CustomerHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_CustomerHistory.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        SQLiteController sqLiteController1 = new SQLiteController(this);
        sqLiteController1.open();
        try {
            long count = sqLiteController1.fetchCount();
            System.out.println("count---"+count);
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController1.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    user_email = user_c.getString(user_c.getColumnIndex("Email"));

                }

            }
        } finally {
            sqLiteController1.close();
        }

        cl = findViewById(R.id.cl);
        srl = findViewById(R.id.swipe);
        searchBar = (SearchView) findViewById(R.id.search_Csview);
        mRecyclerView = findViewById(R.id.rc_CsList);
        img_noCustomer = findViewById(R.id.img_noCustomer);
        InTotal_tv = findViewById(R.id.CsTotal_tv);
        InPaid_tv = findViewById(R.id.CsActive_tv);
        InUnPaid_tv = findViewById(R.id.CsInactive_tv);
        CsStatus_sp = findViewById(R.id.CsStatus_sp);

        findViewById(R.id.createOrder_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","2");
                Intent in = new Intent(Activity_CustomerHistory.this, Activity_CreateCustomer.class);
                in.putExtras(bundle);
                startActivity(in);
                overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            }
        });

        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CustomerHistory.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CustomerHistory.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_CustomerHistory.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        adapter = new Adapter_CustomerHistory(listInvoice);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(filteredModelList.size()>0){

                    view.findViewById(R.id.CsviewImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_CustomerHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_csmenu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.edit_customer:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("CustomerId",filteredModelList.get(position).getCustomerId());
                                            Intent in = new Intent(Activity_CustomerHistory.this, Activity_UpdateCustomer.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
                                            return true;
                                            case R.id.addNote_customer:
                                                AddNoteAlert();
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();

                        }
                    });
                    view.findViewById(R.id.CsvNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("CustomerId",filteredModelList.get(position).getCustomerId());
                            Intent in = new Intent(Activity_CustomerHistory.this, ActivityCustomerDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });

                }else {
                    view.findViewById(R.id.CsviewImage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_CustomerHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_csmenu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.edit_customer:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("CustomerId",listInvoice.get(position).getCustomerId());
                                            Intent in = new Intent(Activity_CustomerHistory.this, Activity_UpdateCustomer.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
                                            return true;

                                        case R.id.addNote_customer:
                                            AddNoteAlert();
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();

                        }
                    });
                    view.findViewById(R.id.CsvNum_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("CustomerId",listInvoice.get(position).getCustomerId());
                            Intent in = new Intent(Activity_CustomerHistory.this, ActivityCustomerDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);
                        }
                    });
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                filteredModelList = filter(listInvoice, newText.toString());
                adapter.setFilter(filteredModelList);
                if(filteredModelList.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    img_noCustomer.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    img_noCustomer.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Utils.getInstance().hideKeyboard(Activity_CustomerHistory.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Progress_dialog.show();
                Utils.getInstance().GetMasterInsert(Activity_CustomerHistory.this,"CustomerRefresh",Progress_dialog,"");

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (srl.isRefreshing()) srl.setRefreshing(false);
                        GetCustomer(str_ordersfill);
                    }
                }, 6000);
            }
        });

        CsStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_ordersfill = arStatusName.get(position);

                if(str_ordersfill.equals("Active")){
                    Progress_dialog.show();
                    GetCustomer("Active");
                }else  if(str_ordersfill.equals("Inactive")){
                    Progress_dialog.show();
                    GetCustomer("Inactive");
                }else  if(str_ordersfill.equals("All")){
                    GetCustomer("All");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        GetStatus();

    }

    private void GetStatus() {
        try {
            App.getInstance().GetCustomerStatus(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("options");
                            if (arStatusName.size() > 0) arStatusName.clear();
                            if (arStatusID.size() > 0) arStatusID.clear();
                            if (data.length() > 0) {
                                arStatusName.add("All");
                                arStatusID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arStatusName.add(js.getString("displayname"));
                                    arStatusID.add(js.getString("id"));
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_CustomerHistory.this, R.layout.spinner_item,arStatusName);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CsStatus_sp.setAdapter(adapterBType);

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

    private void GetCustomer(String Sp_Status) {

        InActive =0;
        Active =0;
        if (srl.isRefreshing()) srl.setRefreshing(false);
        try {
            App.getInstance().GetCustomer(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_noCustomer.setVisibility(View.VISIBLE);
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
                            String totalCount = jsonObject.getString("totalCount");
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(data.length()>0){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(listInvoice.size()>0)listInvoice.clear();
                                        img_noCustomer.setVisibility(View.GONE);
                                    }
                                });

                                String customername ="";
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    String id = js.getString("id");
                                    String customernumber = js.getString("customernumber");

                                    String status = js.getString("status");
                                    String industry = js.getString("industry");
                                    String customertypeid = js.getString("customertypeid");

                                    customername = js.getString("businessname").equals("null") ?  js.getString("customername") : js.getString("businessname");

                                    if(status.equals("Inactive")){
                                        InActive += 1;
                                    }

                                    if(status.equals("Active")){
                                        Active += 1;
                                    }

                                    String finalCustomername = customername;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            InUnPaid_tv.setText(String.valueOf(InActive));
                                            InPaid_tv.setText(String.valueOf(Active));
                                            InTotal_tv.setText(String.valueOf(totalCount));

                                            if(Sp_Status.equals("All")){
                                                listInvoice.add(new Model_CustomerHistory(id, customernumber, finalCustomername, status, industry));

                                            }else if(Sp_Status.equals(status)){
                                                listInvoice.add(new Model_CustomerHistory(id, customernumber, finalCustomername, status, industry));

                                            }

                                            if(listInvoice.size()>0){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_noCustomer.setVisibility(View.GONE);
                                                    }
                                                });
                                            }else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_noCustomer.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }

                                        }
                                    });

                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        img_noCustomer.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
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


    private ArrayList<Model_CustomerHistory> filter(ArrayList<Model_CustomerHistory> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_CustomerHistory> filteredModelList = new ArrayList<>();

        for (Model_CustomerHistory model : models) {

            final String getOrder_name = model.getCustomername().toLowerCase();
            final String getOrder_status = model.getCustomerId().toLowerCase();
            final String getOrder_number = model.getStatus().toLowerCase();

            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);

            }else if (getOrder_status.contains(search_txt)) {
                filteredModelList.add(model);

            }else if (getOrder_number.contains(search_txt)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }

    public void AddNoteAlert() {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CustomerHistory.this);
                LayoutInflater inflater = Activity_CustomerHistory.this.getLayoutInflater();
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
                        Utils.getInstance().hideKeyboard(Activity_CustomerHistory.this);
                        if (Utils.getInstance().checkIsEmpty(edit_query_des)) {
                            Utils.getInstance().snackBarMessage(v,"Enter the Description");
                        }else {
                            dialog.dismiss();
                            if (Connectivity.isConnected(Activity_CustomerHistory.this) &&
                                    Connectivity.isConnectedFast(Activity_CustomerHistory.this)) {
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

    private void postNotesAlert(final View v, final JSONObject json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.getInstance().loadingDialog(Activity_CustomerHistory.this, "Please wait.");
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
                                        Intent intent = new Intent(Activity_CustomerHistory.this, Activity_CustomerHistory.class);
                                        startActivity(intent);
                                        Toast.makeText(Activity_CustomerHistory.this, "Notes has been added Successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Activity_CustomerHistory.this, "Error, Please try again later!!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

}
