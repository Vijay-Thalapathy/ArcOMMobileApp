package com.example.arcomdriver.salesreturn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_CustomerPricing;
import com.example.arcomdriver.adapter.Adapter_SalesReturn;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.ActivityCustomerPricingInfo;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.customer.Activity_UpdateCustomer;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.invoice.ActivityInvoiceDetails;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.model.Model_CustomerPricingList;
import com.example.arcomdriver.model.Model_SalesReturnList;
import com.example.arcomdriver.order.Activity_CreateOrder;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.payments.ActivityShareInvoice;
import com.example.arcomdriver.payments.Activity_OrderDelivery;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Jan 2024*/
public class ActivitySalesHistory extends Activity_Menu {

    private CoordinatorLayout cl;

    public static String  OrderId,token,user_email,user_id;
    private SQLiteController sqLiteController;

    private RecyclerView mRecyclerView;

    private LottieAnimationView txt_no_record;

    private Adapter_SalesReturn adapter_salesReturn;

    private ArrayList<Model_SalesReturnList> list_salesReturn= new ArrayList<>();

    public AlertDialog Progress_dialog;
    public AppCompatTextView newReturns_tv,yetReturns_tv,weekyetReturns_tv;

    private SwipeRefreshLayout srl;

    private ArrayList<String> arReasonID = new ArrayList<>();
    private ArrayList<String> arReasonName = new ArrayList<>();

    String str_reason="Choose Status";

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_routepayment);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_saleslist, null, false);
        parentView.addView(contentView,0);
        Od_menu_ic.setVisibility(View.GONE);
        In_download_ic.setVisibility(View.GONE);
        In_print_img.setVisibility(View.GONE);
        createOrder_img.setVisibility(View.VISIBLE);
        txt_page.setText("Sales Return");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySalesHistory.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = ActivitySalesHistory.this.getLayoutInflater();
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
                    //  Get_OrderList(token);
                }
            }
        } finally {
            sqLiteController.close();
        }

        mRecyclerView = findViewById(R.id.rcsalesReturnList);
        srl = findViewById(R.id.swipe);
        txt_no_record = findViewById(R.id.txt_no_record);
        newReturns_tv = findViewById(R.id.newReturns_tv);
        yetReturns_tv = findViewById(R.id.yetReturns_tv);
        weekyetReturns_tv = findViewById(R.id.weekyetReturns_tv);

        adapter_salesReturn = new Adapter_SalesReturn(list_salesReturn);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitySalesHistory.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDividerDecoration(ActivitySalesHistory.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter_salesReturn);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(ActivitySalesHistory.this, mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {


                /*view.findViewById(R.id.return_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("SalesID",list_salesReturn.get(position).getSrm_returnID());
                        Intent in = new Intent(ActivitySalesHistory.this, ActivitySalesDetails.class);
                        in.putExtras(bundle);
                        startActivity(in);
                    }
                });*/


                view.findViewById(R.id.viewDotImage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(ActivitySalesHistory.this, view, Gravity.RIGHT);
                        popupMenu.getMenuInflater().inflate(R.menu.add_sales_menu, popupMenu.getMenu());
                        Menu popMenu = popupMenu.getMenu();
                        if (list_salesReturn.get(position).getSrm_returnstatus().equals("Returned")){
                            popMenu.findItem(R.id.ad_status).setVisible(false);
                        }else {
                            popMenu.findItem(R.id.ad_status).setVisible(true);
                        }


                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                switch (menuItem.getItemId()) {
                                    case R.id.ad_email:
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("SalesID",list_salesReturn.get(position).getSrm_returnID());
                                        Intent in1 = new Intent(ActivitySalesHistory.this, ActivitySalesShareInvoice.class);
                                        in1.putExtras(bundle1);
                                        startActivity(in1);
                                        return true;
                                    case R.id.ad_status:
                                        CancelAlert(list_salesReturn.get(position).getSrm_returnID());
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

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        createOrder_img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("CusID","null");
                Intent in = new Intent(ActivitySalesHistory.this, Activity_CreateSalesReturn.class);
                in.putExtras(bundle);
                startActivity(in);
                overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Progress_dialog.show();
                GetSalesReturnList();
            }
        });



        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesHistory.this, ActivitySalesHistory.class));
            }
        });

        findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesHistory.this, Activity_CustomerHistory.class));
            }
        });
        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySalesHistory.this, Activity_InvoiceHistory.class));
            }
        });



        GetSalesReturnList();


        Date dateTime = new Date();
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String currentDate = sdfTime.format(dateTime);

        Calendar calE = Calendar.getInstance();
        calE.set(Calendar.DAY_OF_MONTH, calE.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date resultDate = calE.getTime();
        String  monthEnd = sdfTime.format(resultDate);


        Calendar calS = Calendar.getInstance();
        calS.set(Calendar.DAY_OF_MONTH, 1);
        Date resultDate5 = calS.getTime();
        String  monthStart= sdfTime.format(resultDate5);


        Calendar calWS = Calendar.getInstance();
        calWS.set(Calendar.DAY_OF_WEEK, calWS.getFirstDayOfWeek());
        Date resultDate2 = calWS.getTime();
        String  WeekStart = sdfTime.format(resultDate2);

        Calendar calWE = Calendar.getInstance();
        calWE.set(Calendar.DAY_OF_WEEK, -7);
        Date resultDate3 = calWE.getTime();
        String  WeekEnd = sdfTime.format(resultDate3);

        Calendar calPR= Calendar.getInstance();
        calPR.add(Calendar.DAY_OF_MONTH, -1);
        Date resultDatR = calPR.getTime();
        String  PreviousDate = sdfTime.format(resultDatR);

        try {
            JSONObject json = new JSONObject();
            json.put("screenid","BFA5523C-3560-47DF-A8DF-1747FCFA1A11");
            json.put("ownerid","d4ab35c2-4dce-454f-8bf3-77e42dc6c95d");
            json.put("todayDate",currentDate);
            json.put("thisWeekStartDate",WeekStart);
            json.put("thisWeekEndDate",WeekEnd);
            json.put("thisMonthStartDate",monthStart);
            json.put("thisMonthEndDate",monthEnd);
            System.out.println("res---"+json.toString());
            SalesFilterkpi(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void SalesFilterkpi(String json) {
        try {
            App.getInstance().SalesREturnFilterkpi(json.toString(),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String res = response.body().string();
                        System.out.println("SalesKpi---"+res);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    final JSONArray jr = new JSONArray(res);
                                    for (int i = 0; i < jr.length(); i++) {
                                        JSONObject js = jr.getJSONObject(i);
                                        String type = js.getString("type");
                                        String persistanceid = js.getString("persistanceid");
                                        String count = js.getString("count");

                                        if (type.equals("kpi-updated")){

                                            if(persistanceid.equals("1665a070-d5e1-475f-96ba-83658d685c65")){
                                                newReturns_tv.setText(count);
                                            }

                                            if(persistanceid.equals("21c7e557-1c7c-4466-b6f3-132fa97f130a")){
                                                weekyetReturns_tv.setText(count);
                                            }

                                            if(persistanceid.equals("21c7e557-1c7c-4466-b6f3-132fa97f130a")){
                                                weekyetReturns_tv.setText(count);
                                            }

                                            if(persistanceid.equals("8dd37efd-6ece-4c07-b403-ad740f7bfe73")){
                                                yetReturns_tv.setText(count);
                                            }

                                        }
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                } finally {

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


    private void GetSalesReturnList() {
        if (srl.isRefreshing()) srl.setRefreshing(false);
        try {
            App.getInstance().GetSalesReturns(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_no_record.setVisibility(View.VISIBLE);
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
                            JSONArray data = jsonObject.getJSONArray("data");

                            if(data.length()>0){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(list_salesReturn.size()>0)list_salesReturn.clear();
                                        txt_no_record.setVisibility(View.GONE);
                                    }
                                });
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    String id = js.getString("id");
                                    String returnnumber = js.getString("returnnumber");
                                    String returneddate = js.getString("returneddate");
                                    String ordernumber = js.getString("ordernumber");
                                    String invoicenumber = js.getString("invoicenumber");
                                    String orderinvnumber = js.getString("orderinvnumber");
                                    String invoiceordnumber = js.getString("invoiceordnumber");
                                    String customername = js.getString("customername");
                                    String returnstatus = js.getString("returnstatus");
                                    String totalamount = js.getString("totalamount");


                                    if(ordernumber.equals("")){
                                        ordernumber = invoicenumber;
                                    }

                                    if(orderinvnumber.equals("")){
                                        orderinvnumber = invoiceordnumber;
                                    }



                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = null;
                                    String startDate ="";
                                    try {
                                        date = sdf.parse(returneddate);
                                        startDate =new SimpleDateFormat("MM/dd/yyyy").format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String finalOrdernumber = ordernumber;
                                    String finalOrderinvnumber = orderinvnumber;
                                    String finalStartDate = startDate;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            list_salesReturn.add(new Model_SalesReturnList(id,returnnumber, finalStartDate, finalOrdernumber, finalOrderinvnumber,customername,returnstatus,totalamount));
                                        }
                                    });

                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_no_record.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter_salesReturn.notifyDataSetChanged();
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

    public void CancelAlert(String orderNo) {
        runOnUiThread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySalesHistory.this);
                LayoutInflater inflater = ActivitySalesHistory.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.activity_statussalesalert,null);
                builder.setView(v);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                AppCompatImageView close_img = v.findViewById(R.id.close_img);
                AppCompatButton btn_submit = v.findViewById(R.id.btn_submit);
                Spinner cancelReason_sp = v.findViewById(R.id.cancelReason_sp);

                if (arReasonID.size() > 0) arReasonID.clear();
                if (arReasonName.size() > 0) arReasonName.clear();

                arReasonName.add("Choose Status");
                arReasonName.add("Returned");
                arReasonID.add("0");
                arReasonID.add("1");

                ArrayAdapter<String> adapterPGroup = new ArrayAdapter<String>(ActivitySalesHistory.this, R.layout.spinner_item,arReasonName);
                adapterPGroup.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelReason_sp.setAdapter(adapterPGroup);
                    }
                });

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
                        Date date1 = new Date();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String currentDateTime = sdf1.format(date1);

                        Utils.getInstance().hideKeyboard(ActivitySalesHistory.this);
                        if (str_reason.equals("Choose Status")) {
                            Utils.getInstance().snackBarMessage(v,"Choose Status!");
                        }else {
                            dialog.dismiss();
                            if (Connectivity.isConnected(ActivitySalesHistory.this) &&
                                    Connectivity.isConnectedFast(ActivitySalesHistory.this)) {
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put("returnstatus","Returned");
                                    json.put("id",orderNo);
                                    json.put("modifiedon",currentDateTime);
                                    json.put("modifiedby",user_id);
                                    postCancelled(v,json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(ActivitySalesHistory.this, "No Network Connection! Please check internet Connection and try again later.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });


            }
        });
    }

    private void postCancelled(final View v, final JSONObject json) {
        Utils.getInstance().loadingDialog(ActivitySalesHistory.this, "Please wait.");
        try {
            App.getInstance().PostSalesReason(json.toString(),token, new Callback() {
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
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        Utils.getInstance().dismissDialog();
                                        Toast.makeText(ActivitySalesHistory.this, "Sales return status updated successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ActivitySalesHistory.this, ActivitySalesHistory.class);
                                        startActivity(intent);

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
        super.onBackPressed();
    }


}