package com.example.arcomdriver.route;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.payments.Activity_ProofDelivery;
import com.example.arcomdriver.products.Activity_CreateProduct;
import com.example.arcomdriver.products.Activity_ProductHistory;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
 * @author : SivaramYogesh*/
public class Activity_RoutePayment extends Activity_Menu {
    private CoordinatorLayout cl;
    String user_id,token,Email;


    public static Activity routeActivity;

    String StopJSON,TruckJSON,Route_ID,Route_DriverID,Route_TruckID,Route_InsID;

    Spinner sp_cash1,sp_cash2;

    private ArrayList<String> arCashlName1;

    AppCompatEditText et_CusAmt,et_MisAmt;

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private File imageFile2 =null;

    String Sign_byte,SignName,SignExtension;

    int PERMISSION_CODE = 110;

    private SharedPreferences rdSharedPreferences;


    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_routepayment);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_routepayment, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Cash");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        loadPermissionSign(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSION_CODE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            StopJSON = extras.getString("StopJSON");
            TruckJSON = extras.getString("TruckJSON");

            System.out.println("StopJSON--"+StopJSON);
            System.out.println("TruckJSON--"+TruckJSON);
        }

        Route_ID =getRouteID();
        Route_DriverID =getRouteDriverID_pref();
        Route_TruckID =getRouteTruckID_pref();
        Route_InsID =getRoute_InsID();

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

        sp_cash1 = findViewById(R.id.sp_cash1);
        sp_cash2 = findViewById(R.id.sp_cash2);
        et_CusAmt = findViewById(R.id.et_CusAmt);
        et_MisAmt = findViewById(R.id.et_MisAmt);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Activity_RoutePayment.this.finish();
              *//*  Intent intent = new Intent(Activity_RoutePayment.this, Activity_RouteHistory.class);
                startActivity(intent);*//*
            }
        });*/

        arCashlName1 = new ArrayList<String>();
        arCashlName1.add("USD");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arCashlName1);
        arrayAdapter1.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        sp_cash1.setAdapter(arrayAdapter1);
        sp_cash1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String str_cash = arCashlName1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, arCashlName1);
        arrayAdapter2.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        sp_cash2.setAdapter(arrayAdapter2);
        sp_cash2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String str_cash = arCashlName1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        findViewById(R.id.btn_Intiate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDate1 = sdf.format(date1);

                Utils.getInstance().hideKeyboard(Activity_RoutePayment.this);

                if (Connectivity.isConnected(Activity_RoutePayment.this) &&
                        Connectivity.isConnectedFast(Activity_RoutePayment.this)) {

                    if(imageFile2 == null) {
                        Utils.getInstance().snackBarMessage(v,"Please save signature!");
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                JSONObject json = new JSONObject();
                                try {
                                    json.put("routedayid", Route_ID);
                                    json.put("driverid", Route_DriverID);
                                    json.put("truckid", Route_TruckID);
                                    json.put("refundamount", et_CusAmt.getText().toString());
                                    json.put("miscamount", et_MisAmt.getText().toString());
                                    json.put("modifiedby", user_id);
                                    json.put("modifiedon", currentDate1);
                                    json.put("sodtruckinspectionid", Route_InsID);
                                    json.put("IsWarehouseModuleEnabled", true);

                                    try {
                                        JSONArray array1 = new JSONArray(StopJSON);
                                        JSONArray array2 = new JSONArray(TruckJSON);

                                        json.put("draftRouteStopDetails", array1);
                                        json.put("draftRouteLoadedOrderDetails", array2);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                System.out.println("PostPayments---" + json.toString());
                                //completed

                                PostPayments(json.toString());


                            }
                        });


                    }


                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

            }
        });
       /* findViewById(R.id.btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //  Toast.makeText(Activity_ProofDelivery.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);

        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFile2 =null;
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                Bitmap emptyBitmap = Bitmap.createBitmap(signatureBitmap.getWidth(), signatureBitmap.getHeight(), signatureBitmap.getConfig());

                if (signatureBitmap.sameAs(emptyBitmap)) {
                    // myBitmap is empty/blank
                    Toast.makeText(Activity_RoutePayment.this, "Please enter signature!", Toast.LENGTH_SHORT).show();
                } else {
                    Uri imageUri = getImageUriSign(Activity_RoutePayment.this, signatureBitmap);
                    String sImage = getPathFromURISign(imageUri);
                    imageFile2 = new File(sImage);

                    SignName = getFileName(Activity_RoutePayment.this, imageUri);
                    SignExtension = SignName.split("\\.")[1];

                    System.out.println("imageUri----"+imageUri);

                    if(imageFile2 !=null){
                        File file1 = new File(imageFile2.getAbsolutePath());

                        Log.v("LOG_TAG", "compressedImage_front" + imageFile2);

                        Log.v("LOG_TAG", "getAbsolutePath()" + imageFile2.getAbsolutePath());
                        Log.v("LOG_TAG", "file1" + file1);

                        Bitmap bm = BitmapFactory.decodeFile(String.valueOf(file1));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                        byte[] b = baos.toByteArray();

                        Sign_byte = Base64.encodeToString(b, Base64.NO_WRAP);

                        Toast.makeText(Activity_RoutePayment.this, "Signature saved successfully!", Toast.LENGTH_SHORT).show();

                        Log.e("LOG_TAG--", Sign_byte);

                    }
                }

            }
        });


    }

    private void PostPayments(String json) {
        Utils.getInstance().loadingDialog(Activity_RoutePayment.this, "Please wait.");
        try {
            App.getInstance().PostPaymentRoute(json,token, new Callback() {
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
                        System.out.println("SuccessAA---");
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.getInstance().dismissDialog();
                                    final JSONObject jsonObject = new JSONObject(res);
                                    System.out.println("Success---"+jsonObject);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                              //  Toast.makeText(Activity_RoutePayment.this, "Route initiated successfully!", Toast.LENGTH_SHORT).show();
                                                mSignaturePad.clear();
                                                showSuccess();

                                            }
                                        });
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

    private void showSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RoutePayment.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_successalert, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        tittleAlert_tv.setText("Route initiated successfully!");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_RoutePayment.this.finish();
                Intent intent = new Intent(Activity_RoutePayment.this, Activity_Route360.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }



    public Uri getImageUriSign(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        long time=System.currentTimeMillis();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(time), null);

        return Uri.parse(path);

    }

    public String getPathFromURISign(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private boolean loadPermissionSign(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission},requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission},requestCode);
            }
            return false;
        } else {
            return true;
        }

    }

    @SuppressLint("MissingSuperCall")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
                    if (! showRationale) {
                        // user also CHECKED "never ask again"
                    } else{
                        // Not checked
                    }
                }
            }
        }

    }

    @SuppressLint("Range")
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }


    private String getRouteID() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteId_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteId_pref, null);
        }
        return "0";
    }

    private String getRouteDriverID_pref() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref,MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteDriverID_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteDriverID_pref, null);
        }
        return "0";
    }

    private String getRouteTruckID_pref() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteHistory.rdPref, MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteHistory.RouteTruckID_pref)) {
            return rdSharedPreferences.getString(Activity_RouteHistory.RouteTruckID_pref, null);
        }
        return "0";
    }

    private String getRoute_InsID() {
        rdSharedPreferences = getSharedPreferences(Activity_RouteSOD.rdSPref, MODE_PRIVATE);
        if (rdSharedPreferences.contains(Activity_RouteSOD.Route_InsID)) {
            return rdSharedPreferences.getString(Activity_RouteSOD.Route_InsID, null);
        }
        return "0";
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