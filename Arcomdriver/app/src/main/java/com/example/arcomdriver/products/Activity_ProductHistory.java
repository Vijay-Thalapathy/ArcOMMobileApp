package com.example.arcomdriver.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_ProductHistory;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_InvoiceHistory;
import com.example.arcomdriver.model.Model_ProductHistory;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_ProductHistory extends Activity_Menu {
    private CoordinatorLayout cl;

    Adapter_ProductHistory adapter;
    ArrayList<Model_ProductHistory> listInvoice = new ArrayList<>();
    private RecyclerView mRecyclerView;
    AppCompatTextView pd_Inactive_tv,pdc_active_tv,pd_total_tv,InTotal_tv,InPaid_tv,InUnPaid_tv;
    private SearchView searchBar;
    ArrayList<Model_ProductHistory> filteredModelList = new ArrayList<>();
    private SwipeRefreshLayout srl;
    String  str_ordersfill ="All";

    Spinner products_sp;

    int ActiveTtl =0;
    int InActiveTtl =0;

    private ArrayList<String> arStatusID = new ArrayList<>();
    private ArrayList<String> arStatusName = new ArrayList<>();
   // Toolbar toolbar;

    public AlertDialog Progress_dialog;

    LottieAnimationView img_noProducts;

    String user_id, token, Email;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_productlist);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_productlist, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Products");
        createOrder_img.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SQLiteController sqLiteController1 = new SQLiteController(this);
        sqLiteController1.open();
        try {
            long count = sqLiteController1.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController1.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    user_id = user_c.getString(user_c.getColumnIndex("user_id"));
                    token = user_c.getString(user_c.getColumnIndex("token"));
                    Email = user_c.getString(user_c.getColumnIndex("Email"));
                }


            }
        } finally {
            sqLiteController1.close();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ProductHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_ProductHistory.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();


        cl = findViewById(R.id.cl);
      //  toolbar = findViewById(R.id.toolbar);
        srl = findViewById(R.id.swipe);
        searchBar = (SearchView) findViewById(R.id.search_inview);
        mRecyclerView = findViewById(R.id.rc_productList);
        img_noProducts = findViewById(R.id.img_noProducts);
        pd_total_tv = findViewById(R.id.pd_total_tv);
        pdc_active_tv = findViewById(R.id.pdc_active_tv);
        pd_Inactive_tv = findViewById(R.id.pd_Inactive_tv);
        products_sp = findViewById(R.id.products_sp);

        findViewById(R.id.createOrder_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_ProductHistory.this, Activity_CreateProduct.class));
                overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            }
        });

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ProductHistory.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });*/
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ProductHistory.this, Activity_CustomerHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ProductHistory.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ProductHistory.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        adapter = new Adapter_ProductHistory(listInvoice);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(filteredModelList.size()>0){


                    view.findViewById(R.id.pdr_name_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("ProductId",filteredModelList.get(position).getId());
                            Intent in = new Intent(Activity_ProductHistory.this, ActivityProductDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);

                        }
                    });

                    view.findViewById(R.id.productMenu_img).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_ProductHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_productmenu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.edit_Product:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ProductId",filteredModelList.get(position).getId());
                                            Intent in = new Intent(Activity_ProductHistory.this, Activity_UpdateProduct.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
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


                }else {

                    view.findViewById(R.id.pdr_name_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("ProductId",listInvoice.get(position).getId());
                            Intent in = new Intent(Activity_ProductHistory.this, ActivityProductDetails.class);
                            in.putExtras(bundle);
                            startActivity(in);

                        }
                    });

                    view.findViewById(R.id.productMenu_img).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(Activity_ProductHistory.this, view, Gravity.RIGHT);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_productmenu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.edit_Product:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ProductId",listInvoice.get(position).getId());
                                            Intent in = new Intent(Activity_ProductHistory.this, Activity_UpdateProduct.class);
                                            in.putExtras(bundle);
                                            startActivity(in);
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
                }



            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something
                //SEARCH FILTER
                filteredModelList = filter(listInvoice, newText.toString());
                adapter.setFilter(filteredModelList);
                if(filteredModelList.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    img_noProducts.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    img_noProducts.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                Utils.getInstance().hideKeyboard(Activity_ProductHistory.this);
                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);






        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Progress_dialog.show();
                Utils.getInstance().GetMasterInsert(Activity_ProductHistory.this,"ProductRefresh",Progress_dialog,"");

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (srl.isRefreshing()) srl.setRefreshing(false);
                        GetProductsList(str_ordersfill);
                    }
                }, 6000);
            }
        });

        GetStatus();

        products_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                str_ordersfill = arStatusName.get(position);

                System.out.println("str_ordersfill---"+str_ordersfill);

                if(str_ordersfill.equals("Active")){
                    Progress_dialog.show();
                    GetProductsList("Active");
                }else  if(str_ordersfill.equals("Inactive")){
                    Progress_dialog.show();
                    GetProductsList("Inactive");
                }else  if(str_ordersfill.equals("All")){
                    Progress_dialog.show();
                    GetProductsList("All");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

    private void GetStatus() {
        //  Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetCustomerStatus(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
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
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_ProductHistory.this, R.layout.spinner_item,arStatusName);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        products_sp.setAdapter(adapterBType);
                                        //eta_spinner_state.setSelection(aState.indexOf(str_state));
                                        //sp_businessCategory.setSelection(aStateId.indexOf("1"));

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

    private void GetProductsList(String filter) {
        ActiveTtl =0;
        InActiveTtl =0;
        if (srl.isRefreshing()) srl.setRefreshing(false);
        try {
            App.getInstance().GetProductList(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_noProducts.setVisibility(View.VISIBLE);
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(listInvoice.size()>0)listInvoice.clear();
                                    img_noProducts.setVisibility(View.GONE);
                                }
                            });
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject js = data.getJSONObject(i);
                                String id = js.getString("id");
                                String productnumber = js.getString("productnumber");
                                String productname = js.getString("productname");
                                String productimage1 = js.getString("productimage");
                                String price = js.getString("price");
                                String available = js.getString("available");
                                String quantityonhand = js.getString("quantityonhand");
                                String status = js.getString("status");
                               String description = js.getString("description");
                                String upccode = js.getString("upccode");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //InUnPaid_tv.setText(String.valueOf(unpaid));

                                        if(status.equals("Active")){
                                            ActiveTtl += 1;
                                        }

                                        if(status.equals("Inactive")){
                                            InActiveTtl += 1;
                                        }

                                        if(filter.equals("All")){
                                            listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, productimage1,upccode,description));

                                        }else if(filter.equals(status)){
                                            listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, productimage1,upccode,description));

                                        }else if(filter.equals(status)){
                                            listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, productimage1,upccode,description));

                                        }

                                    }
                                });



                                /*try {
                                    JSONArray data2 = js.getJSONArray("digitalassetsinfo");
                                    String productimage = null;
                                    for (int k = 0; k < data2.length(); k++) {
                                        JSONObject js1 = data2.getJSONObject(k);
                                        String isdefault = js1.getString("isdefault");

                                        if (isdefault.equals("null")) {

                                        }else {
                                            if(isdefault.equals("true")){
                                                productimage = js1.getString("productimage");
                                                System.out.println("productimage---"+productimage);
                                            }else {
                                                productimage = null;
                                            }
                                        }


                                    }

                                    String finalProductimage = productimage;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //InUnPaid_tv.setText(String.valueOf(unpaid));

                                            if(status.equals("Active")){
                                                ActiveTtl += 1;
                                            }

                                            if(status.equals("Inactive")){
                                                InActiveTtl += 1;
                                            }

                                            if(filter.equals("All")){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }else if(filter.equals(status)){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }else if(filter.equals(status)){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }

                                        }
                                    });
                                }catch (JSONException e) {
                                    e.printStackTrace();

                                    String finalProductimage = productimage1;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //InUnPaid_tv.setText(String.valueOf(unpaid));

                                            if(status.equals("Active")){
                                                ActiveTtl += 1;
                                            }

                                            if(status.equals("Inactive")){
                                                InActiveTtl += 1;
                                            }

                                            if(filter.equals("All")){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }else if(filter.equals(status)){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }else if(filter.equals(status)){
                                                listInvoice.add(new Model_ProductHistory(id, productnumber, productname, price, available, quantityonhand,status, finalProductimage,upccode,description));

                                            }

                                        }
                                    });
                                }*/

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    // Collections.reverse(list);
                                    pd_total_tv.setText(totalCount);
                                    pdc_active_tv.setText(String.valueOf(ActiveTtl));
                                    pd_Inactive_tv.setText(String.valueOf(InActiveTtl));

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
                    }else {

                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img_noProducts.setVisibility(View.VISIBLE);


                                }
                            });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<Model_ProductHistory> filter(ArrayList<Model_ProductHistory> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model_ProductHistory> filteredModelList = new ArrayList<>();

        for (Model_ProductHistory model : models) {

            final String getOrder_name = model.getProductname().toLowerCase();


            if (getOrder_name.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }





}
