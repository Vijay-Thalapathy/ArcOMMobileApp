package com.example.arcomdriver.driver;

import static com.example.arcomdriver.route.Activity_RoutePayment.routeActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.arcomdriver.R;
import com.example.arcomdriver.customer.ActivityCustomerPricing;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.invoice.Activity_InvoiceHistory;
import com.example.arcomdriver.order.Activity_OfflineSync;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.printer.ThermalPrint;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.reports.ActivityReportHistory;
import com.example.arcomdriver.route.Activity_RouteHistory;
import com.example.arcomdriver.salesreturn.ActivitySalesHistory;
import com.google.android.material.navigation.NavigationView;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 05 Oct 2022*/
public class Activity_Menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView txt_page;
    public static AppCompatImageView createOrder_img,Od_menu_ic,Od_edit_ic,In_print_img,In_download_ic;
    public FrameLayout parentView;
    public CoordinatorLayout coordinatorLayout;
    NavigationView navigationView;
    private SQLiteController sqLiteController;
    private AppCompatTextView activity_user_name,activity_user_id;

    String settingsvalue="1";

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentView = findViewById(R.id.layout_content);
        txt_page = findViewById(R.id.activity_title);
        createOrder_img = findViewById(R.id.createOrder_img);
        Od_menu_ic = findViewById(R.id.Od_menu_ic);
        Od_edit_ic = findViewById(R.id.Od_edit_ic);
        In_print_img = findViewById(R.id.In_print_img);
        In_download_ic = findViewById(R.id.In_download_ic);
        coordinatorLayout = findViewById(R.id.menu_parent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();


        View headerView = navigationView.getHeaderView(0);
        activity_user_name = headerView.findViewById(R.id.activity_user_name);
        activity_user_id = headerView.findViewById(R.id.activity_user_id);

        sqLiteController = new SQLiteController(this);
        sqLiteController.open();
        try {
            long count = sqLiteController.fetchCount();
            if (count > 0) {
                //user
                Cursor user_c = sqLiteController.readTableUser();
                if (user_c != null && user_c.moveToFirst()) {
                    @SuppressLint("Range") String UserName = user_c.getString(user_c.getColumnIndex("user_name"));
                    @SuppressLint("Range") String token = user_c.getString(user_c.getColumnIndex("token"));
                    @SuppressLint("Range") String Email = user_c.getString(user_c.getColumnIndex("Email"));
                    activity_user_name.setText(UserName);
                    activity_user_id.setText(Email);
                }

                Cursor C_Address = sqLiteController.readTableInvoiceFormat();
                if (C_Address.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String invoice_name = C_Address.getString(C_Address.getColumnIndex("invoice_name"));
                        if(invoice_name.equals("AddinList")){
                            @SuppressLint("Range") String settingsvalue  = C_Address.getString(C_Address.getColumnIndex("invoice_value"));
                            System.out.println("settingsvalue--"+settingsvalue);
                            if (settingsvalue != null && !settingsvalue.equals("")) {

                                if (settingsvalue.contains("Route")){
                                    //show Route true
                                    nav_Menu.findItem(R.id.nav_Routes).setVisible(true);
                                }else {
                                    //Hide Route
                                    nav_Menu.findItem(R.id.nav_Routes).setVisible(true);
                                }

                            }else {
                                nav_Menu.findItem(R.id.nav_Routes).setVisible(false);
                            }

                        }
                    } while (C_Address.moveToNext());
                }


                Cursor C_In = sqLiteController.readTableInvoiceFormat();
                if (C_In.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String invoice_name = C_In.getString(C_In.getColumnIndex("invoice_name"));
                        if(invoice_name.equals("customerpricingsetup")){

                            settingsvalue  = C_In.getString(C_In.getColumnIndex("invoice_value"));

                            if(settingsvalue.equals("1")) {
                                //show
                                nav_Menu.findItem(R.id.nav_CustomerPricing).setVisible(true);

                            }else {
                                //hide
                                nav_Menu.findItem(R.id.nav_CustomerPricing).setVisible(false);


                            }

                        }
                    } while (C_In.moveToNext());
                }


            }
        } finally {
            sqLiteController.close();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks.
        int id = item.getItemId();

        if (id == R.id.nav_presaleOrder) {
            Intent intent = new Intent(Activity_Menu.this, Activity_OrdersHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        } else if (id == R.id.nav_Invoice) {
            Intent intent = new Intent(Activity_Menu.this, Activity_InvoiceHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);

        }else if (id == R.id.nav_sales) {
            Intent intent = new Intent(Activity_Menu.this, ActivitySalesHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);

        } else if (id == R.id.nav_Product) {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         if ( routeActivity != null) {
                             routeActivity.finish();
                         }
                     } catch (IllegalStateException e) {
                         e.printStackTrace();
                     }
                 }
             });
            Intent intent = new Intent(Activity_Menu.this, Activity_ProductHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        } else if (id == R.id.nav_Routes) {
            Intent intent = new Intent(Activity_Menu.this, Activity_RouteHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        }  else if (id == R.id.nav_Timesheet) {
            Intent intent = new Intent(Activity_Menu.this, Activity_CustomerHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        } else if (id == R.id.nav_CustomerPricing) {
            Intent intent = new Intent(Activity_Menu.this, ActivityCustomerPricing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(Activity_Menu.this, ActivityReportHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        } else if (id == R.id.nav_Offline) {

           /*Intent intent = new Intent(Activity_Menu.this, Activity_OfflineSync.class);
            startActivity(intent);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);*/

            Bundle bundle = new Bundle();
            bundle.putString("Flag","0");
            Intent in = new Intent(Activity_Menu.this, Activity_OfflineSync.class);
            in.putExtras(bundle);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
            startActivity(in);

        }else if (id == R.id.nav_Printeer) {
            Bundle bundle = new Bundle();
            bundle.putString("Print","");
            bundle.putString("Flag","0");
            Intent in = new Intent(Activity_Menu.this, ThermalPrint.class);
            in.putExtras(bundle);
            startActivity(in);
            overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
        }else if (id == R.id.nav_logout) {
             userLogout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void userLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Menu.this);
        builder.setTitle("Logout?");
        builder.setMessage("Are you sure you want to logout now?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Activity_Menu.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                logOut();
            }
        });
        builder.show();
    }

    private void logOut() {
        SQLiteController sqLiteController = new SQLiteController(Activity_Menu.this);
        sqLiteController.open();
        try {
            sqLiteController.deleteTableUser();
            //sqLiteController.deleteTableOrders();
            //sqLiteController.deleteTableOrdersProducts();
            //sqLiteController.deleteTableShipment();
            sqLiteController.deleteTableWarehouse();
            sqLiteController.deleteTableCancelReason();
            //sqLiteController.deleteTableAllProducts();
            sqLiteController.deleteTableSalesRep();
            sqLiteController.deleteTableDeliverysRep();
            sqLiteController.deleteTableFulfilment();
            sqLiteController.deleteTableInvoiceFormat();
            //sqLiteController.deleteTableCustomer();
            //sqLiteController.deleteTableAddress();
            //sqLiteController.deleteTableInvoice();
            //sqLiteController.deleteTableInvoiceProducts();
            sqLiteController.deleteTableTerms();
        } finally {
            sqLiteController.close();
            Intent intent = new Intent(Activity_Menu.this, ActivitySignIn.class);
            startActivity(intent);

        }
    }
}
