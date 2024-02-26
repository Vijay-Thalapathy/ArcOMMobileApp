package com.example.arcomdriver.payments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.GalleryAdapter;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Connectivity;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.database.DbHandler;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.invoice.Activity_CreateInvoice;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.order.ActivityOrdersDetails;
import com.example.arcomdriver.order.Activity_OrdersHistory;
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
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_OrderDelivery extends AppCompatActivity {
    private CoordinatorLayout cl;
    AppCompatEditText et_Notes;
    int PERMISSION_CODE = 110;
    Toolbar toolbar;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded,Sign_byte,SignName,SignExtension;

    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    AppCompatTextView item_count_tv;

    String TotalPaymentAmt,deliverywarehouseid,deliveryshipmenttypeid,user_id,token,Email,OrderID;

    public AlertDialog Progress_dialog;


    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private File imageFile2 =null;

    String currentDateTime;

  //  AppCompatButton btn_shareInvoice;

    String pageFlag ="0";


    String Newinvoiceid,Incustomeristaxable,Induedate,Innetterms,Intaxid,Inwarehouseid,Inmemo,Intermsconditions,Inshipnote,Indeliverynote,Indatefulfilled,Insalesrepid,Intaxperecentage,Intax,Inbilltoaddressid,Incustomerid,Indiscountamount,Indiscountpercentage,Inshiptoaddressid,Intotalamount,Intotallineitemamount,Infreightamount;
    private ArrayList<Model_ItemList> Collectlist = new ArrayList<>();

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdeliver);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OrderDelivery.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_OrderDelivery.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();

        cl = findViewById(R.id.cl);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        AppCompatTextView tittleMemo_tv = (AppCompatTextView) findViewById(R.id.tittleMemo_tv);
        tittleMemo_tv.setText("Delivery Memo*");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            TotalPaymentAmt = extras.getString("TotalPaymentAmt");
            OrderID = extras.getString("OrderID");
            deliveryshipmenttypeid = extras.getString("deliveryshipmenttypeid");
            deliverywarehouseid = extras.getString("deliverywarehouseid");

            System.out.println("OrderID---"+OrderID);



        }

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDateTime = sdf.format(date1);


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

            getOrderDetails(OrderID);
        }


        toolbar = findViewById(R.id.toolbar);
        et_Notes = findViewById(R.id.et_Notes);
       // btn_shareInvoice = findViewById(R.id.btn_shareInvoice);
       // btn_shareInvoice.setText("Deliver");

        item_count_tv = findViewById(R.id.item_count_tv);
        gvGallery = (GridView)findViewById(R.id.gv);

        findViewById(R.id.btn_floating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_count_tv.setText("Upload Proof(0)*");
                mArrayUri.clear();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
/*
        findViewById(R.id.btn_cancel_pd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_OrderDelivery.this, Activity_OrdersHistory.class));
            }
        });
*/
        findViewById(R.id.viewImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Activity_OrderDelivery.this, v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.popup_orderdelivermenu, popupMenu.getMenu());
                Menu popMenu = popupMenu.getMenu();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(Activity_OrdersHistory.this, "Selected Item: " +menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (menuItem.getItemId()) {
                            case R.id.OrderDeliver_ll:

                                pageFlag ="0";
                                DeliverOrderMethod();

                                return true;
                            case R.id.Deliver_email_ll:

                                pageFlag ="1";
                                DeliverOrderMethod();

                                return true;
                            case R.id.OrderPrint_ll:
                                pageFlag ="2";
                                DeliverOrderMethod();

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

        loadPermissionSign(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSION_CODE);

        et_Notes = (AppCompatEditText) findViewById(R.id.et_Notes);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       /* findViewById(R.id.btn_shareInvoice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.getInstance().hideKeyboard(Activity_OrderDelivery.this);

                if (Connectivity.isConnected(Activity_OrderDelivery.this) &&
                        Connectivity.isConnectedFast(Activity_OrderDelivery.this)) {

                    if (Utils.getInstance().checkIsEmpty(et_Notes)) {
                        Utils.getInstance().snackBarMessage(v,"Enter the Memo!");
                    }else if (mArrayUri.size()==0) {
                        Utils.getInstance().snackBarMessage(v,"Upload Proof");
                    }else if(imageFile2 == null) {
                        Utils.getInstance().snackBarMessage(v,"Please save signature!");
                    }else {
                        Progress_dialog.show();
                        DeliveryOrder();

                    }


                }else {
                    Utils.getInstance().snackBarMessage(v,"No Network Connection! Please check internet Connection and try again later.");
                }

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
                    Toast.makeText(Activity_OrderDelivery.this, "Please enter signature!", Toast.LENGTH_SHORT).show();
                } else {
                    Uri imageUri = getImageUriSign(Activity_OrderDelivery.this, signatureBitmap);
                    String sImage = getPathFromURISign(imageUri);
                    imageFile2 = new File(sImage);

                    SignName = getFileName(Activity_OrderDelivery.this, imageUri);
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

                        Toast.makeText(Activity_OrderDelivery.this, "Signature saved successfully!", Toast.LENGTH_SHORT).show();

                        Log.e("LOG_TAG--", Sign_byte);

                    }
                }

            }
        });

    }

    private void DeliverOrderMethod() {

        Utils.getInstance().hideKeyboard(Activity_OrderDelivery.this);

        if (Connectivity.isConnected(Activity_OrderDelivery.this) &&
                Connectivity.isConnectedFast(Activity_OrderDelivery.this)) {

            if (Utils.getInstance().checkIsEmpty(et_Notes)) {
                Utils.getInstance().snackBarMessage(cl,"Enter the Memo!");
            }else if (mArrayUri.size()==0) {
                Utils.getInstance().snackBarMessage(cl,"Upload Proof");
            }else if(imageFile2 == null) {
                Utils.getInstance().snackBarMessage(cl,"Please save signature!");
            }else {
                Progress_dialog.show();
                DeliveryOrder();

            }


        }else {
            Utils.getInstance().snackBarMessage(cl,"No Network Connection! Please check internet Connection and try again later.");
        }

    }

    private void DeliveryOrder() {

        JSONObject json_d = new JSONObject();
        try {
            json_d.put("isactive",true);
            json_d.put("fulfillmentstatus","Shipped");
            json_d.put("requestdeliveryby",user_id);
            json_d.put("attempt","1");
            json_d.put("creratedon",currentDateTime);
            json_d.put("orderid",OrderID);
            if(deliveryshipmenttypeid.equals("474bece4-7bc2-43a0-a7c4-e74b1de134f1")){
                json_d.put("deliverytype","customerpickup");
                json_d.put("isInsert",true);
            }else if(deliveryshipmenttypeid.equals("28d68737-5d45-47f4-907c-4f66e6b01600")){
                json_d.put("deliveryagentid",user_id);
                json_d.put("isInsert",false);
            }
            json_d.put("deliveryshipmenttypeid",deliveryshipmenttypeid);
            json_d.put("deliverytime",currentDateTime);
            json_d.put("deliverydate",currentDateTime);
            json_d.put("modifiedon",currentDateTime);

            System.out.println("delivery----"+json_d.toString());

            try {
                App.getInstance().PostInvoiceSummaryFul(String.valueOf(json_d),token, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

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
                                           ProofPayments();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ProofPayments() {
        JSONObject postedJSON = new JSONObject();
        JSONObject JsonSign = new JSONObject();
        JSONArray array = new JSONArray();

        for (int i = 0; i < mArrayUri.size(); i++) {

            Uri irl = mArrayUri.get(i);
            System.out.println("mArray----"+irl);

            String fileName = getFileName(Activity_OrderDelivery.this, irl);
            String extensionRemoved = fileName.split("\\.")[1];


            //GetFile
            File compressedImage_front = null;
            try {
                compressedImage_front = FileUtil.from(Activity_OrderDelivery.this, irl);

                final File file1 = new File(compressedImage_front.getAbsolutePath());

                Log.v("LOG_TAG", "compressedImage_front" + compressedImage_front);

                Log.v("LOG_TAG", "getAbsolutePath()" + compressedImage_front.getAbsolutePath());
                Log.v("LOG_TAG", "file1" + file1);

                Bitmap bm = BitmapFactory.decodeFile(String.valueOf(file1));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);

                Log.e("LOG_TAG--", encodedImage);

                postedJSON = new JSONObject();
                try {
                    postedJSON.put("fileName", fileName);
                    postedJSON.put("extension", extensionRemoved);
                    postedJSON.put("type","Payments");
                    postedJSON.put("data","data:image/jpeg;base64,"+encodedImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(postedJSON);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        JsonSign = new JSONObject();
        try {
            JsonSign.put("fileName", SignName);
            JsonSign.put("extension", SignExtension);
            JsonSign.put("assettypename","Deliverysignature");//Delivery Signature
            JsonSign.put("data","data:image/jpeg;base64,"+Sign_byte);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject();
            //json.put("invoiceid",InvoiceID);//null
            //json.put("paymentmethodid",PayCashFlag);//null
            //json.put("paymentdate",paymentDate_str);//presale pricing Date
            //json.put("payable",payable);
            //json.put("paid",paid);
            //json.put("due",due);
            //json.put("checknumber","");//null
            json.put("paymentmemo",et_Notes.getText().toString());//null
            json.put("isactive",true);
            json.put("createdby",user_id);
            json.put("creratedon",currentDateTime);
          //json.put("uploadRequest", array);
          //json.put("uploadSign", JsonSign);
            json.put("modifiedon",currentDateTime);
            json.put("modifiedby",user_id);

            System.out.println("CreatePayment--+"+json);

            postPayment(json.toString().trim(),array,JsonSign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();

                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    System.out.println("mImageUri--"+mImageUri);
                    cursor.close();

                    mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setNumColumns(mArrayUri.size());
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    item_count_tv.setText("Upload Proof("+String.valueOf(mArrayUri.size())+")*");

                } else {

                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setNumColumns(mArrayUri.size());
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        item_count_tv.setText("Upload Proof("+String.valueOf(mArrayUri.size())+")*");
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    private void postPayment(String json,JSONArray array,JSONObject JsonSign) {

        try {
            App.getInstance().PostPayment(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                        JSONObject json = new JSONObject();
                                        try {
                                            json.put("submitstatus","Confirmed");
                                            json.put("id",OrderID);
                                            json.put("modifiedon",currentDateTime);
                                            json.put("modifiedby",user_id);

                                            System.out.println("FulfilledFulPayment----"+json.toString());
                                            PostStatusUpdate(json.toString(),array,JsonSign);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


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

    private void PostStatusUpdate(String json,JSONArray array,JSONObject JsonSign) {
        try {
            App.getInstance().PostStatusUpdate(json,token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                    //    Utils.getInstance().GetMasterInsert(Activity_OrderDelivery.this,"Order Deliver",Progress_dialog,"");
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);

                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {

                                     //   showSuccess();

                                        JSONObject json = new JSONObject();
                                        try {

                                            json.put("isactive",true);
                                            json.put("fulfillmentstatus","Shipped");
                                            json.put("requestdeliveryby",user_id);
                                            json.put("attempt","1");
                                            json.put("creratedon",currentDateTime);
                                            json.put("orderid",OrderID);
                                            if(deliveryshipmenttypeid.equals("474bece4-7bc2-43a0-a7c4-e74b1de134f1")){
                                                json.put("deliverytype","customerpickup");
                                                json.put("isInsert",true);
                                            }else if(deliveryshipmenttypeid.equals("28d68737-5d45-47f4-907c-4f66e6b01600")){
                                                json.put("deliveryagentid",user_id);
                                                json.put("isInsert",false);
                                            }
                                            json.put("deliveryshipmenttypeid",deliveryshipmenttypeid);
                                            json.put("deliverytime",currentDateTime);
                                            json.put("deliverydate",currentDateTime);
                                            json.put("modifiedon",currentDateTime);
                                            json.put("uploadRequest", array);
                                            json.put("uploadSign", JsonSign);

                                            /*    json.put("isInsert",true);
                                            json.put("isactive",true);
                                            json.put("requestdeliveryby",user_id);
                                            json.put("attempt","1");
                                            json.put("creratedon",currentDateTime);
                                            json.put("orderid",OrderID);
                                            json.put("uploadRequest", array);
                                            json.put("uploadSign", JsonSign);*/

                                            System.out.println("ProofImageUpload----"+json.toString());

                                            ProofImageUpload(json.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

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

    private void ProofImageUpload(String json) {
        try {
            App.getInstance().PostInvoiceSummaryFul(String.valueOf(json),token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

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
                                    System.out.println("UploadResponse---"+jsonObject.toString());
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {

                                        CreateInvoice();

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

    private void getOrderDetails(String OrderID) {
        Progress_dialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    App.getInstance().PostPresaleOrder(OrderID, "",token, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("------Order Getting Success----");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                final String res = response.body().string();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("------Order Getting Success----");
                                        if (Collectlist.size() > 0) Collectlist.clear();
                                        try {
                                            final JSONObject jsonObject = new JSONObject(res);
                                            JSONObject jsData = jsonObject.getJSONObject("data");
                                            Incustomerid = jsData.getString("customerid");
                                            Indiscountamount = jsData.getString("discountamount");
                                            Indiscountpercentage = jsData.getString("discountpercentage");
                                            Infreightamount = jsData.getString("freightamount");
                                            Intotalamount = jsData.getString("totalamount");
                                            Intotallineitemamount = jsData.getString("totallineitemamount");
                                            Intax = jsData.getString("tax");
                                            Intaxperecentage = jsData.getString("taxperecentage");
                                            Insalesrepid = jsData.getString("salesrepid");
                                            Indatefulfilled = jsData.getString("datefulfilled");
                                            Indeliverynote = jsData.getString("deliverynote");
                                            Inshipnote = jsData.getString("shipnote");
                                            Intermsconditions = jsData.getString("termsconditions");
                                            Inmemo = jsData.getString("memo");
                                            Inwarehouseid = jsData.getString("warehouseid");
                                            Intaxid = jsData.getString("taxid");
                                            Innetterms = jsData.getString("netterms");
                                            Induedate = jsData.getString("duedate");
                                            Incustomeristaxable = jsData.getString("customeristaxable");
                                            Inbilltoaddressid = jsData.getString("billtoaddressid");
                                            Inshiptoaddressid = jsData.getString("shiptoaddressid");


                                            JSONArray OrderProducts = jsData.getJSONArray("orderProducts");
                                            for (int i = 0; i < OrderProducts.length(); i++) {
                                                JSONObject js = OrderProducts.getJSONObject(i);
                                                String productname = js.getString("productname");
                                                String quantity = js.getString("quantity");
                                                String baseamount = js.getString("baseamount");
                                                String priceperunit = js.getString("priceperunit");
                                                String productid = js.getString("productid");
                                                String orderid = js.getString("orderid");
                                                String id = js.getString("id");
                                                String itemistaxable = js.getString("itemistaxable");
                                                String upccode = js.getString("upccode");

                                                Collectlist.add(new Model_ItemList(id, productname, "", quantity, String.valueOf(baseamount), priceperunit, productid, itemistaxable,upccode, "","false"));

                                            }

                                            if (Progress_dialog != null) {
                                                if (Progress_dialog.isShowing()) {
                                                    Progress_dialog.dismiss();
                                                }
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
        });

    }

    private void CreateInvoice() {

        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateTime = sdf1.format(date1);

        JSONObject postedJSON = new JSONObject();
        JSONArray array = new JSONArray();


        for (int i = 0; i < Collectlist.size(); i++) {

            double TotalBaseAmt = Double.parseDouble(Collectlist.get(i).getPrice_PerUnit()) * Double.parseDouble(Collectlist.get(i).getProduct_quantity());

            postedJSON = new JSONObject();
            try {
                //TODO getProduct_id = getOrder_productid
                //TODO getOrder_productid = getProduct_id

                if (OrderID.equals("0")) {
                    //TODO OrderProductID
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    } else {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    }
                } else {
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("id", "00000000-0000-0000-0000-000000000000");
                    } else {
                        //TODO OrderProductID
                        postedJSON.put("id", Collectlist.get(i).getProduct_id().trim());
                    }
                }

                postedJSON.put("isactive", true);

                if (OrderID.equals("0")) {
                    //   postedJSON.put("orderid","00000000-0000-0000-0000-000000000000");
                } else {
                    postedJSON.put("orderid", OrderID);
                }

                if (OrderID.equals("0")) {
                } else {
                    if (Collectlist.get(i).getProduct_id().equals("null")) {
                        postedJSON.put("orderpid", "00000000-0000-0000-0000-000000000000");
                    } else {
                        //TODO OrderProductID
                        postedJSON.put("orderpid", Collectlist.get(i).getProduct_id().trim());
                    }

                }

                //TODO ProductID
                postedJSON.put("productid", Collectlist.get(i).getOrder_productid().trim());
                postedJSON.put("name", Collectlist.get(i).getProduct_name().trim());
                postedJSON.put("vendorid", "00000000-0000-0000-0000-000000000000");//null object
                postedJSON.put("quantityonhand", "0");
                postedJSON.put("baseamount", TotalBaseAmt);//produvt amount
                postedJSON.put("priceperunit", Collectlist.get(i).getPrice_PerUnit().trim());
                postedJSON.put("uomid", "80b14ad0-9369-4f3f-9d81-f647c27c6157");//each ID
                postedJSON.put("delete", "null");
                postedJSON.put("warehouseid", Inwarehouseid);//warehouseid
                postedJSON.put("modifiedby", user_id);
                postedJSON.put("modifiedon", currentDateTime);
                postedJSON.put("isHigh", true);
                postedJSON.put("quantity", Collectlist.get(i).getProduct_quantity().trim());//quantity
                postedJSON.put("available", "0");
                postedJSON.put("committed", "0");
                if (Collectlist.get(i).getIstaxable().equals("true")) {
                    postedJSON.put("itemistaxable", true);
                } else {
                    postedJSON.put("itemistaxable", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(postedJSON);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("orderid", OrderID);
            json.put("paymenttypeid", "4e142367-5f99-4aa7-99ed-931756978ee5");
            json.put("paymentid", "00000000-0000-0000-0000-000000000000");
            json.put("paymentmethod", "Cash");
            json.put("name", "");
            json.put("billtoaddressid", Inbilltoaddressid);
            json.put("customerid", Incustomerid);
            json.put("vendorid", "00000000-0000-0000-0000-000000000000");
            json.put("datefulfilled", "00000000-0000-0000-0000-000000000000");
            json.put("description", "");
            json.put("discountamount", Indiscountamount);
            json.put("timestamp", currentDateTime);
            json.put("transactioncurrencyid", "00000000-0000-0000-0000-000000000000");
            json.put("exchangerate", "0");
            json.put("discountamountbase", "0");
            json.put("discountpercentage", Indiscountpercentage);
            json.put("freightamount", Infreightamount);
            json.put("freightamountbase", "0");
            json.put("freighttermscode", "0");
            json.put("ispricelocked", true);
            json.put("lastbackofficesubmit", currentDateTime);
            json.put("ordernumber", "0");
            json.put("quoteid", "00000000-0000-0000-0000-000000000000");
            json.put("requestdeliveryby", "00000000-0000-0000-0000-000000000000");
            json.put("shiptoaddressid", Inshiptoaddressid);
            json.put("invoicedate", currentDateTime);
            json.put("submitstatus", "Payment pending");
            json.put("submitstatusdescription", "");
            json.put("totalamount", Intotalamount);
            json.put("totalamountbase", "0");
            json.put("totalamountlessfreight", "0");
            json.put("totalamountlessfreightbase", "0");
            json.put("totaldiscountamount", "0");
            json.put("totaldiscountamountbase", "0");
            json.put("totallineitemamount", Intotallineitemamount);
            json.put("totallineitemamountbase", "0");
            json.put("totallineitemdiscountamount", "0");
            json.put("totallineitemdiscountamountbase", "0");
            json.put("totaltax", Intax);
            json.put("totaltaxbase", Intaxperecentage);
            json.put("willcall", true);
            json.put("salesrepid", Insalesrepid);
            json.put("pricingdate", Indatefulfilled);
            json.put("deliverynote", Indeliverynote);
            json.put("shipnote", Inshipnote);
            json.put("termsconditions", Intermsconditions);
            json.put("memo", Inmemo);
            json.put("referenceorder", "0");
            json.put("createdby", user_id);
            json.put("createdon", currentDateTime);
            json.put("modifiedby", user_id);
            json.put("modifiedon", currentDateTime);
            json.put("isactive", true);
            json.put("isPresale", true);
            //json.put("deletedRows", "");
            json.put("warehouseid", Inwarehouseid);
            json.put("draftnumber", "");
            json.put("lastsyncon", "");

            if(Intaxid.equals("null")){
                json.put("taxid",null);
            }else{
                json.put("taxid",Intaxid);
            }

            if(Innetterms.equals("null")){
                json.put("netterms",null);
            }else{
                json.put("netterms",Innetterms);
            }

            json.put("duedate",Induedate);
            if (Incustomeristaxable.equals("true")) {
                json.put("customeristaxable", true);
            } else {
                json.put("customeristaxable", false);
            }
            json.put("invoiceproduct", array);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("InvoiceJSON ----"+json.toString());

        postCreateInvoice(json.toString(), OrderID,currentDateTime);

    }

    private void postCreateInvoice(String json, String OrderID, String ModifiedDateTime) {

        try {
            App.getInstance().PostInvoiceCreate(json, token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                    Newinvoiceid = jsonObject.getString("data");
                                    if (succeeded == true) {

                                        JSONObject json = new JSONObject();
                                        try {
                                            if (OrderID.equals("0")) {
                                                json.put("id", "00000000-0000-0000-0000-000000000000");
                                            } else {
                                                json.put("id", OrderID);
                                            }

                                            //json.put("fulfillmentstatus", "Payment pending");
                                            json.put("invoiceid", Newinvoiceid);
                                            json.put("invoicerid", user_id);
                                            json.put("modifiedon",ModifiedDateTime);
                                            json.put("modifiedby",user_id);

                                            System.out.println("StatusUpdate---"+json.toString());
                                            System.out.println("modifiedon---"+ModifiedDateTime);

                                            postCreateFull(json.toString(),ModifiedDateTime);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

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

    private void postCreateFull(String json,String ModifiedDateTime) {
        try {
            App.getInstance().PostWarehouseSummaryFul(json, token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if(pageFlag.equals("0")){
                            Utils.getInstance().GetMasterInsert(Activity_OrderDelivery.this,"Order Deliver",Progress_dialog,ModifiedDateTime);
                        }else if(pageFlag.equals("1")){
                            Utils.getInstance().GetMasterInsert(Activity_OrderDelivery.this,"OrderMailSend",Progress_dialog,ModifiedDateTime);
                        }else if(pageFlag.equals("2")){
                            Utils.getInstance().GetMasterInsert(Activity_OrderDelivery.this,"OrderMailSend",Progress_dialog,ModifiedDateTime);
                        }
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final JSONObject jsonObject = new JSONObject(res);
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if (succeeded == true) {

                                        if(pageFlag.equals("1")){
                                            showSuccess();
                                        }else if(pageFlag.equals("2")){
                                            showSuccess();
                                        }
                                        Toast.makeText(Activity_OrderDelivery.this, "Invoice Generated Successful", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OrderDelivery.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.activity_successalert_payment, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnimateDialog_In;
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        AppCompatButton btn_Okalrt = v.findViewById(R.id.btn_Okalrt);
        btn_Okalrt.setVisibility(View.GONE);
        AppCompatButton btn_AlertShareInvoice = v.findViewById(R.id.btn_AlertInvoice);
        btn_AlertShareInvoice.setText("Continue");
        AppCompatTextView tittleAlert_tv = v.findViewById(R.id.tittleAlert_tv);
        tittleAlert_tv.setText("Order Delivered Successful");

        btn_Okalrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_OrderDelivery.this, Activity_OrdersHistory.class));
                dialog.dismiss();
            }
        });
        btn_AlertShareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pageFlag.equals("1")){
                    //deliver and send email
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("InvoiceID",Newinvoiceid);
                    bundle1.putString("ShareFlag","1");
                    Intent in1 = new Intent(Activity_OrderDelivery.this, ActivityShareInvoice.class);
                    in1.putExtras(bundle1);
                    startActivity(in1);

                }else if(pageFlag.equals("2")){
                    //deliver and print
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("InvoiceID",Newinvoiceid);
                    bundle1.putString("ShareFlag","2");
                    Intent in1 = new Intent(Activity_OrderDelivery.this, ActivityShareInvoice.class);
                    in1.putExtras(bundle1);
                    startActivity(in1);

                }
               dialog.dismiss();
            }
        });
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }



}