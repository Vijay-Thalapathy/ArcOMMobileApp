package com.example.arcomdriver.route;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_CustomerStop;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.model.Model_CustomerStop;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;
import com.example.arcomdriver.route.deliverorders.Activity_DeliverOrders;
import com.google.android.gms.maps.model.LatLng;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Metadata;
import com.here.sdk.core.engine.SDKNativeEngine;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.mapviewlite.Camera;
import com.here.sdk.mapviewlite.MapCircle;
import com.here.sdk.mapviewlite.MapImage;
import com.here.sdk.mapviewlite.MapImageFactory;
import com.here.sdk.mapviewlite.MapMarker;
import com.here.sdk.mapviewlite.MapMarkerImageStyle;
import com.here.sdk.mapviewlite.MapPolygon;
import com.here.sdk.mapviewlite.MapPolyline;
import com.here.sdk.mapviewlite.MapPolylineStyle;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;
import com.here.sdk.mapviewlite.PixelFormat;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Maneuver;
import com.here.sdk.routing.ManeuverAction;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Section;
import com.here.sdk.routing.Waypoint;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_Route360 extends AppCompatActivity {

    private RecyclerView rc_CustomerLoc;
    Adapter_CustomerStop CusStopAdapter;
    ArrayList<Model_CustomerStop> CusListStop = new ArrayList<>();

    private CoordinatorLayout cl;
    String user_id,token,Email, RouteDay_ID,routeID;

    public AlertDialog Progress_dialog;

    private static final String TAG = Activity_Route360.class.getSimpleName();
    private MapViewLite mapView;

    Toolbar toolbar;

    private final List<MapMarker> mapMarkerList = new ArrayList<>();

    //polly
    MapScene mapScene;
    Camera mapCamera;
    MapPolyline mapPolyline;

    MapPolygon mapPolygon;

    MapCircle mapCircle;

    AppCompatTextView tr_RouteName_tv,tr_truckName_tv,tr_DriverName_tv,tr_RouteStatus_tv,rs_stopsCount_tv,rs_totalTime_tv,rs_totalDistance_tv,rs_totalDays_tv;

    private SharedPreferences rdSharedPreferences;

    ArrayList<GeoCoordinates> coordinates = new ArrayList<>();
    ArrayList<GeoCoordinates> WareHouseCoordinates = new ArrayList<>();

    ArrayList<GeoCoordinates> LastCoordinates = new ArrayList<>();

    private RoutingEngine routingEngine;
    private GeoCoordinates startGeoCoordinates;
    private GeoCoordinates destinationGeoCoordinates;

    private final List<MapPolyline> mapPolylines = new ArrayList<>();

    List<Waypoint> waypoints =new ArrayList<>();

  //  ProgressDialog progressDialog;

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false); // Prevent user from dismissing the dialog*/

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Route360.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_Route360.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();
        Progress_dialog.show();

        RouteDay_ID =getRouteID();

        System.out.println("Route_ID---"+ RouteDay_ID);

        initializeHERESDK();

        setContentView(R.layout.activity_route360);

        tr_RouteStatus_tv = findViewById(R.id.tr_RouteStatus_tv);
        tr_DriverName_tv = findViewById(R.id.tr_DriverName_tv);
        tr_truckName_tv = findViewById(R.id.tr_truckName_tv);
        rs_stopsCount_tv = findViewById(R.id.rs_stopsCount_tv);
        rs_totalTime_tv = findViewById(R.id.rs_totalTime_tv);
        rs_totalDays_tv = findViewById(R.id.rs_totalDays_tv);
        rs_totalDistance_tv = findViewById(R.id.rs_totalDistance_tv);
        tr_RouteName_tv = findViewById(R.id.tr_RouteName_tv);

        mapView = findViewById(R.id.map_VVview);
        mapView.onCreate(savedInstanceState);

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

        findViewById(R.id.ll_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Route360.this, Activity_RouteHistory.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Route360.this, Activity_ProductHistory.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Route360.this, Activity_OrdersHistory.class);
                startActivity(intent);
            }
        });

        AppCompatButton btn_Customer = findViewById(R.id.btn_Customer);
        AppCompatButton btn_Route = findViewById(R.id.btn_Route);
        btn_Customer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_Customer.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn1));
                btn_Route.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn5));

                GeoCoordinates BERLIN_GEO_COORDINATES = new GeoCoordinates(WareHouseCoordinates.get(0).latitude, WareHouseCoordinates.get(0).longitude);
                mapCamera.setTarget(BERLIN_GEO_COORDINATES);
                mapCamera.setZoomLevel(10);

                clearMap();
                loadMapScene();
                showAnchoredMapMarkers();

            }
        });

        btn_Route.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                btn_Customer.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn2));
                btn_Route.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btn7));

                GeoCoordinates BERLIN_GEO_COORDINATES = new GeoCoordinates(WareHouseCoordinates.get(0).latitude, WareHouseCoordinates.get(0).longitude);
                mapCamera.setTarget(BERLIN_GEO_COORDINATES);
                mapCamera.setZoomLevel(10);

                addRoute();

            }
        });


        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Route360.this, Activity_RouteHistory.class);
                startActivity(intent);
            }
        });

        rc_CustomerLoc = findViewById(R.id.rc_CustomerLoc);

        CusStopAdapter = new Adapter_CustomerStop(CusListStop);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rc_CustomerLoc.setLayoutManager(layoutManager);
        rc_CustomerLoc.setItemAnimator(new DefaultItemAnimator());
        rc_CustomerLoc.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rc_CustomerLoc.setAdapter(CusStopAdapter);

        rc_CustomerLoc.addOnItemTouchListener(new RecyclerViewTouchListener(this, rc_CustomerLoc, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                view.findViewById(R.id.Add_dots).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(Activity_Route360.this, view, Gravity.RIGHT);
                        popupMenu.getMenuInflater().inflate(R.menu.add_popup_menu, popupMenu.getMenu());
                        Menu popMenu = popupMenu.getMenu();

                        if(CusListStop.get(position).getSt_type().equals("customer")) {
                            popMenu.findItem(R.id.ad_deliver).setVisible(true);
                        }else {
                            popMenu.findItem(R.id.ad_deliver).setVisible(false);
                        }
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                switch (menuItem.getItemId()) {

                                    case R.id.ad_deliver:

                                        System.out.println("Cus-----------"+CusListStop.get(position).getSt_stopID());

                                        Bundle bundle = new Bundle();
                                        bundle.putString("CustomerID",CusListStop.get(position).getSt_stopID());
                                        bundle.putString("Route_ID", routeID);
                                        Intent in = new Intent(Activity_Route360.this, Activity_DeliverOrders.class);
                                        in.putExtras(bundle);
                                        startActivity(in);

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


        mapScene = mapView.getMapScene();
        mapCamera = mapView.getCamera();


        GetRouteDetails();

    }

    private void GetRouteDetails() {
        try {
            App.getInstance().GetRouteDetails(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().dismissDialog();
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
                            JSONArray jry = new JSONArray(res);
                            System.out.println("jry---"+jry);
                            if (jry.length() > 0) {
                                for (int i=0; i<jry.length(); i++) {
                                    JSONObject js = jry.getJSONObject(i);
                                    String routeDayID = js.getString("routeDayID");

                                    System.out.println("routeDayID---"+routeDayID);

                                    if(RouteDay_ID.equals(routeDayID)){
                                        routeID = js.getString("routeID");
                                        String routeName = js.getString("routeName");
                                        String routeStatus = js.getString("routeStatus");
                                        String orginaddress = js.getString("orginaddress");
                                        String wareHouseAddress = js.getString("wareHouseAddress");
                                        String totalduration = js.getString("totalduration");
                                        String totaldistance = js.getString("totaldistance");

                                        getwareHouseAddress(getApplicationContext(),wareHouseAddress);

                                        JSONObject jr_truck = js.getJSONObject("truck");
                                        String trucknum = jr_truck.getString("trucknum");

                                        JSONObject jr_driver = js.getJSONObject("driver");
                                        String driverName = jr_driver.getString("driverName");

                                        JSONArray jr_orders = js.getJSONArray("orders");
                                        for (int j=0; j<jr_orders.length(); j++) {
                                            JSONObject jsOr = jr_orders.getJSONObject(j);
                                            String orderID = jsOr.getString("orderID");
                                            String productName = jsOr.getString("productName");

                                        }

                                        JSONArray jr_stops = js.getJSONArray("stops");

                                      /*  JSONObject tot_obj = jr_stops.getJSONObject(jr_stops.length()-1);

                                        String tot_str = tot_obj.optString("address");
                                        String stopNamew = tot_obj.optString("stopName");
                                        System.out.println("tot_str---"+tot_str);
                                        System.out.println("stopNamew---"+stopNamew);*/

                                        int ShipTotal =0;
                                        final int[] StopTotal = {0};
                                        for (int k=0; k<jr_stops.length();k++) {

                                            JSONObject jsSt = jr_stops.getJSONObject(k);
                                            String id = jsSt.getString("id");
                                            String stopName = jsSt.getString("stopName");
                                            String stopID = jsSt.getString("stopID");
                                            String type = jsSt.getString("type");
                                            String sequence = jsSt.getString("sequence");
                                            String address = jsSt.getString("address");
                                            String expectedDeliveryOn = jsSt.getString("expectedDeliveryOn");
                                            String distance = jsSt.getString("distance");
                                            String duration = jsSt.getString("duration");
                                            String isCompleted = jsSt.getString("isCompleted");

                                            System.out.println("address---"+address);

                                            getLocationFromAddress(getApplicationContext(),address);

                                            ShipTotal += 1;


                                            int finalShipTotal = ShipTotal;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    double hours = Double.parseDouble(duration) / 3600;
                                                    double minutes = (Double.parseDouble(duration) % 3600) / 60;
                                                    double seconds = Double.parseDouble(duration) % 60;

                                                    String timeString = hours+" "+minutes+" "+seconds;


                                                    System.out.println("timeString-----"+timeString);

                                                    String[] arr=String.valueOf(hours).split("\\.");
                                                    int[] intArr=new int[2];
                                                    intArr[0]=Integer.parseInt(arr[0]); // 1

                                                    String[] arr2=String.valueOf(minutes).split("\\.");
                                                    int[] intArr2=new int[2];
                                                    intArr2[0]=Integer.parseInt(arr2[0]); // 1

                                                    CusListStop.add(new Model_CustomerStop(String.valueOf(finalShipTotal),stopName,address,String.valueOf(intArr[0]+":"+intArr2[0]),sequence,type,stopID));


                                                   /* for(Model_CustomerStop mod : CusListStop){
                                                        Log.i("getSt_sequence----", mod.getSt_sequence());
                                                    }*/

                                                    Collections.sort(CusListStop, new Comparator<Model_CustomerStop>() {
                                                        @Override
                                                        public int compare(Model_CustomerStop lhs, Model_CustomerStop rhs) {
                                                            return lhs.getSt_sequence().compareTo(rhs.getSt_sequence());
                                                        }
                                                    });

                                                    if(!isCompleted.equals("0")){
                                                        StopTotal[0] += 1;
                                                    }

                                                }
                                            });

                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                rs_stopsCount_tv.setText(String.valueOf(StopTotal[0] +"/"+CusListStop.size()));

                                                double hours = Double.parseDouble(totalduration) / 3600;
                                                double minutes = (Double.parseDouble(totalduration) % 3600) / 60;
                                                double seconds = Double.parseDouble(totalduration) % 60;

                                                String timeString = hours+" "+minutes+" "+seconds;

                                                String[] arr=String.valueOf(hours).split("\\.");
                                                int[] intArr=new int[2];
                                                intArr[0]=Integer.parseInt(arr[0]); // 1

                                                String[] arr2=String.valueOf(minutes).split("\\.");
                                                int[] intArr2=new int[2];
                                                intArr2[0]=Integer.parseInt(arr2[0]); // 1

                                                rs_totalTime_tv.setText(String.valueOf(intArr[0]+":"+intArr2[0]));

                                                if(routeStatus.equals("Draft")){
                                                    tr_RouteStatus_tv.setText("Planned");
                                                    tr_RouteStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                                                }else if(routeStatus.equals("Planned")){
                                                    tr_RouteStatus_tv.setText("Planned");
                                                    tr_RouteStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.ColorOrange));
                                                }else if(routeStatus.equals("In-Transit")){
                                                    tr_RouteStatus_tv.setText("Live");
                                                    tr_RouteStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                                                }else if(routeStatus.equals("Completed")){
                                                    tr_RouteStatus_tv.setText("Completed");
                                                    tr_RouteStatus_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                                                }

                                                tr_RouteName_tv.setText(routeName);
                                                tr_truckName_tv.setText(trucknum);
                                                tr_DriverName_tv.setText(driverName);

                                                double miles = Double.parseDouble(totaldistance) * 0.00062137119;

                                                String[] ar3=String.valueOf(miles).split("\\.");
                                                int[] intArr3=new int[2];
                                                intArr3[0]=Integer.parseInt(ar3[0]); // 1

                                                rs_totalDistance_tv.setText(String.valueOf(intArr3[0]));


                                                int day = (int) TimeUnit.SECONDS.toDays((long) Double.parseDouble(totalduration));
                                                rs_totalDays_tv.setText(String.valueOf(day));

                                                CusStopAdapter.notifyDataSetChanged();

                                                String last_address =CusListStop.get(CusListStop.size()-1).getSt_address();
                                                System.out.println("last_address---"+last_address);
                                                getLasteAddress(getApplicationContext(),last_address);


                                                //Collections.reverse(CusListStop);
                                                if (Progress_dialog != null) {
                                                    if (Progress_dialog.isShowing()) {
                                                        Progress_dialog.dismiss();

                                                        checkPermissions();
                                                    }
                                                }
                                            }
                                        });


                                    }

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

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;
        LatLng p2 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            coordinates.add(new GeoCoordinates(location.getLatitude(), location.getLongitude()));

            System.out.println("LAT---------------------------"+String.valueOf(p1));
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;



    }
    public LatLng getwareHouseAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;
        LatLng p2 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            coordinates.add(new GeoCoordinates(location.getLatitude(), location.getLongitude()));
            WareHouseCoordinates.add(new GeoCoordinates(location.getLatitude(), location.getLongitude()));


      /*      runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Toast.makeText(context, address1 +country, Toast.LENGTH_SHORT).show();
                }
            });*/


            System.out.println("LAT---------------------------"+String.valueOf(p1));
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;


    }

    public LatLng getLasteAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;
        LatLng p2 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            LastCoordinates.add(new GeoCoordinates(location.getLatitude(), location.getLongitude()));

            System.out.println("LAT---------------------------"+String.valueOf(p1));
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;



    }


    private void initializeHERESDK() {
        // Set your credentials for the HERE SDK.
        String accessKeyID = "H9rGqYynKQ03nAPsZsZz1w";
        String accessKeySecret = "qtW3VLqUQVvIh9P9gc3inzja_GNIV0NWyFTJC87u8yWP-XRo6UUOTXQIcbBv1VSE3seW6lS2tgpAn8xTxwpgWw";
        SDKOptions options = new SDKOptions(accessKeyID, accessKeySecret);
        try {
            Context context = this;
            SDKNativeEngine.makeSharedInstance(context, options);
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of HERE SDK failed: " + e.error.name());
        }


        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of RoutingEngine failed: " + e.error.name());
        }


    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(Activity_Route360.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                clearMap();
                loadMapScene();
                showAnchoredMapMarkers();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(Activity_Route360.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            clearMap();
            loadMapScene();
            showAnchoredMapMarkers();
        }
    }
    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {
                    mapView.getCamera().setTarget(new GeoCoordinates(WareHouseCoordinates.get(0).latitude, WareHouseCoordinates.get(0).longitude));
                    mapView.getCamera().setZoomLevel(10);
                } else {
                    Log.d(TAG, "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

    public void showAnchoredMapMarkers() {

        for (int i=0; i<coordinates.size(); i++) {
            addPOIMapMarker(new GeoCoordinates(coordinates.get(i).latitude, coordinates.get(i).longitude));
            addCircleMapMarker(new GeoCoordinates(coordinates.get(i).latitude, coordinates.get(i).longitude));
        }

    }

    private void addCircleMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(getApplicationContext().getResources(), R.drawable.circle_red);

        MapMarker mapMarker = new MapMarker(geoCoordinates);
        mapMarker.addImage(mapImage, new MapMarkerImageStyle());

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private void addPOIMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(getApplicationContext().getResources(), R.drawable.poi);

        MapMarker mapMarker = new MapMarker(geoCoordinates);

        // The bottom, middle position should point to the location.
        // By default, the anchor point is set to 0.5, 0.5.
        MapMarkerImageStyle mapMarkerImageStyle = new MapMarkerImageStyle();
        mapMarkerImageStyle.setAnchorPoint(new Anchor2D(0.5F, 1));

        mapMarker.addImage(mapImage, mapMarkerImageStyle);

        Metadata metadata = new Metadata();
        metadata.setString("key_poi", "This is a POI.");
        mapMarker.setMetadata(metadata);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private MapPolyline createPolyline() {

        GeoPolyline geoPolyline;
        try {
            geoPolyline = new GeoPolyline(coordinates);
        } catch (InstantiationErrorException e) {
            // Less than two vertices.
            return null;
        }

        MapPolylineStyle mapPolylineStyle = new MapPolylineStyle();
        mapPolylineStyle.setWidthInPixels(20);
        mapPolylineStyle.setColor(0x00908AA0, PixelFormat.RGBA_8888);
        MapPolyline mapPolyline = new MapPolyline(geoPolyline, mapPolylineStyle);

        return mapPolyline;
    }


    public void clearMap() {
        if (mapPolyline != null) {
            mapScene.removeMapPolyline(mapPolyline);
        }

        if (mapPolygon != null) {
            mapScene.removeMapPolygon(mapPolygon);
        }

        if (mapCircle != null) {
            mapScene.removeMapCircle(mapCircle);
        }

       // clearWaypointMapMarker();
        clearRoute();
    }

    private void clearWaypointMapMarker() {
        for (MapMarker mapMarker : mapMarkerList) {
            mapView.getMapScene().removeMapMarker(mapMarker);
        }
        mapMarkerList.clear();
    }

    private void clearRoute() {
        for (MapPolyline mapPolyline : mapPolylines) {
            mapView.getMapScene().removeMapPolyline(mapPolyline);
        }
        mapPolylines.clear();
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        disposeHERESDK();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mapView.onSaveInstanceState();
        super.onSaveInstanceState(outState);
    }

    private void disposeHERESDK() {
        // Free HERE SDK resources before the application shuts down.
        // Usually, this should be called only on application termination.
        // Afterwards, the HERE SDK is no longer usable unless it is initialized again.
        SDKNativeEngine sdkNativeEngine = SDKNativeEngine.getSharedInstance();
        if (sdkNativeEngine != null) {
            sdkNativeEngine.dispose();
            // For safety reasons, we explicitly set the shared instance to null to avoid situations,
            // where a disposed instance is accidentally reused.
            SDKNativeEngine.setSharedInstance(null);
        }
    }

    private String getRouteID() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteId_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteId_pref, null);
        }
        return "0";
    }
    //RoteRoad

    public void addRoute() {

        startGeoCoordinates = createStartRandomGeoCoordinatesInViewport();
        destinationGeoCoordinates = createEndtRandomGeoCoordinatesInViewport();
  /*      Waypoint startWaypoint = new Waypoint(startGeoCoordinates);
        Waypoint destinationWaypoint = new Waypoint(destinationGeoCoordinates);

        System.out.println("startWaypoint---"+startWaypoint);
        System.out.println("destinationWaypoint---"+destinationWaypoint);

        List<Waypoint> waypoints =
                new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));*/
        for (int i=0; i<coordinates.size(); i++) {

            Waypoint waypoint = new Waypoint(new GeoCoordinates(coordinates.get(i).latitude, coordinates.get(i).longitude));

            waypoints = new ArrayList<>(Arrays.asList(new Waypoint(startGeoCoordinates),
                    waypoint, new Waypoint(destinationGeoCoordinates)));

            routingEngine.calculateRoute(
                    waypoints,
                    new CarOptions(),
                    new CalculateRouteCallback() {
                        @Override
                        public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {

                            if (routingError == null) {
                                Route route = routes.get(0);
                                // showRouteDetails(route);
                                showRouteOnMap(route);

                            } else {
                                //showDialog("Error while calculating a route:", routingError.toString());
                            }
                        }
                    });
        }
    }

    private void showRouteOnMap(Route route) {
        if (mapPolyline != null) {
            mapScene.removeMapPolyline(mapPolyline);
        }

        if (mapPolygon != null) {
            mapScene.removeMapPolygon(mapPolygon);
        }

        if (mapCircle != null) {
            mapScene.removeMapCircle(mapCircle);
        }
        for (MapPolyline mapPolyline : mapPolylines) {
            mapView.getMapScene().removeMapPolyline(mapPolyline);
        }
        mapPolylines.clear();

        // Show route as polyline.
        GeoPolyline routeGeoPolyline = route.getGeometry();
        MapPolylineStyle mapPolylineStyle = new MapPolylineStyle();
        mapPolylineStyle.setColor(0x00908AA0, PixelFormat.RGBA_8888);
        mapPolylineStyle.setWidthInPixels(10);
        MapPolyline routeMapPolyline = new MapPolyline(routeGeoPolyline, mapPolylineStyle);
        mapView.getMapScene().addMapPolyline(routeMapPolyline);
        mapPolylines.add(routeMapPolyline);

        GeoCoordinates startPoint =
                route.getSections().get(0).getDeparturePlace().mapMatchedCoordinates;
        GeoCoordinates destination =
                route.getSections().get(route.getSections().size() - 1).getArrivalPlace().mapMatchedCoordinates;

        // Draw a circle to indicate starting point and destination.
       // addCircleRoadMapMarker(startPoint, R.drawable.green_dot);
        //addCircleRoadMapMarker(destination, R.drawable.green_dot);

        // Log maneuver instructions per route section.
        List<Section> sections = route.getSections();
        for (Section section : sections) {
            logManeuverInstructions(section);
        }
    }

    private void logManeuverInstructions(Section section) {
        Log.d(TAG, "Log maneuver instructions per route section:");
        List<Maneuver> maneuverInstructions = section.getManeuvers();
        for (Maneuver maneuverInstruction : maneuverInstructions) {
            ManeuverAction maneuverAction = maneuverInstruction.getAction();
            GeoCoordinates maneuverLocation = maneuverInstruction.getCoordinates();
            String maneuverInfo = maneuverInstruction.getText()
                    + ", Action: " + maneuverAction.name()
                    + ", Location: " + maneuverLocation.toString();
            Log.d(TAG, maneuverInfo);
        }
    }

    private void addCircleRoadMapMarker(GeoCoordinates geoCoordinates, int resourceId) {
        MapImage mapImage = MapImageFactory.fromResource(this.getResources(), resourceId);
        MapMarker mapMarker = new MapMarker(geoCoordinates);
        mapMarker.addImage(mapImage, new MapMarkerImageStyle());
        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private GeoCoordinates createStartRandomGeoCoordinatesInViewport() {
        GeoBox geoBox = mapView.getCamera().getBoundingBox();
        GeoCoordinates northEast = geoBox.northEastCorner;
        GeoCoordinates southWest = geoBox.southWestCorner;

        double minLat = southWest.latitude;
        double maxLat = northEast.latitude;
        double lat = WareHouseCoordinates.get(0).latitude;

        double minLon = southWest.longitude;
        double maxLon = northEast.longitude;
        double lon =  WareHouseCoordinates.get(0).longitude;

        return new GeoCoordinates(lat, lon);
    }
    private GeoCoordinates createEndtRandomGeoCoordinatesInViewport() {
        GeoBox geoBox = mapView.getCamera().getBoundingBox();
        GeoCoordinates northEast = geoBox.northEastCorner;
        GeoCoordinates southWest = geoBox.southWestCorner;

        double minLat = southWest.latitude;
        double maxLat = northEast.latitude;
        double lat = LastCoordinates.get(0).latitude;

        double minLon = southWest.longitude;
        double maxLon = northEast.longitude;
        double lon =  LastCoordinates.get(0).longitude;

        return new GeoCoordinates(lat, lon);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}