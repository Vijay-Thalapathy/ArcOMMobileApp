package com.example.arcomdriver.route;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.adapter.Adapter_QuestHistory;
import com.example.arcomdriver.adapter.Adapter_RouteHistory;
import com.example.arcomdriver.adapter.GalleryAdapter;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.customer.Activity_CreateCustomer;
import com.example.arcomdriver.customer.Activity_CustomerHistory;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.driver.Activity_Menu;
import com.example.arcomdriver.model.Model_ItemList;
import com.example.arcomdriver.model.Model_ItemProducts;
import com.example.arcomdriver.model.Model_QuestHistory;
import com.example.arcomdriver.model.Model_RouteHistory;
import com.example.arcomdriver.products.Activity_CreateProduct;
import com.example.arcomdriver.recyclerhelper.ItemDividerDecoration;
import com.example.arcomdriver.recyclerhelper.RecyclerViewClickListener;
import com.example.arcomdriver.recyclerhelper.RecyclerViewTouchListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh*/
public class Activity_RouteSOD extends Activity_Menu {
    private CoordinatorLayout cl;
    int PERMISSION_CODE = 110;
    Toolbar toolbar;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded,TruckInsID;
    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    AppCompatTextView item_count_tv;


    String user_id,token,Email;

    public AlertDialog Progress_dialog;

    RecyclerView rt_recycler_ques;

    Adapter_QuestHistory adapterQues;
    ArrayList<Model_QuestHistory> listQuest = new ArrayList<>();
    Spinner spQuestion;


    private ArrayList<String> arTruckInsID = new ArrayList<>();
    private ArrayList<String> arTruckInsTittle = new ArrayList<>();

    private ArrayList<String> arTruckInsQuesID = new ArrayList<>();
    private ArrayList<String> arTruckInspectionID = new ArrayList<>();


    private ArrayList<String> arTruckQuesID = new ArrayList<>();
    private ArrayList<String> arTruckQuesTittle = new ArrayList<>();
    private ArrayList<String> arTruckQuesType = new ArrayList<>();



    private ArrayList<String> arOptionsID = new ArrayList<>();
    private ArrayList<String> arOptionsTitle = new ArrayList<>();
    private ArrayList<String> arOptionsQuestionId = new ArrayList<>();
    private ArrayList<String> arOptionsIsDefault = new ArrayList<>();


    private SharedPreferences rdSharedPreferences;
    public static final String rdSPref = "rdSODPreferences";
    public static final String Route_InsID = "Route_InsID";


    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_routesod);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_routesod, null, false);
        parentView.addView(contentView,0);
        txt_page.setText("Route Planning (SOD)");
        createOrder_img.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_RouteSOD.this, R.style.LoadingStyle);
        builder.setCancelable(false);
        LayoutInflater layoutInflater1 = Activity_RouteSOD.this.getLayoutInflater();
        View v = layoutInflater1.inflate(R.layout.layout_avi, null);
        builder.setView(v);
        Progress_dialog = builder.create();


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


       // toolbar = findViewById(R.id.toolbar);
        rt_recycler_ques = findViewById(R.id.rt_recycler_ques);
        spQuestion = findViewById(R.id.spQuestion);

        item_count_tv = findViewById(R.id.item_count_tv);
        gvGallery = (GridView)findViewById(R.id.gv);

        adapterQues = new Adapter_QuestHistory(listQuest);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rt_recycler_ques.setLayoutManager(layoutManager);
        rt_recycler_ques.setItemAnimator(new DefaultItemAnimator());
        rt_recycler_ques.addItemDecoration(new ItemDividerDecoration(this, LinearLayoutManager.VERTICAL));
        rt_recycler_ques.setAdapter(adapterQues);

        rt_recycler_ques.addOnItemTouchListener(new RecyclerViewTouchListener(this, rt_recycler_ques, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

/*
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
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
*/

        loadPermissionSign(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSION_CODE);


       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExsits =false;
                if(listQuest.size()>0){

                    JSONObject postedJSON = new JSONObject();
                    JSONArray array =new JSONArray();

                    for (int j = 0; j < listQuest.size(); j++) {

                        Model_QuestHistory List = listQuest.get(j);

                        String Quest_ID = "";
                        String Radio_Status = "";

                        if (!List.getRadio_Status().equals("Empty")) {
                            isExsits= true;

                            Quest_ID = List.getQuest_ID();
                            Radio_Status = List.getRadio_Status();

                            postedJSON = new JSONObject();
                            try {
                              //  postedJSON.put("inspectionid", List.getInspection_ID());
                                postedJSON.put("inspectionid", TruckInsID);
                                postedJSON.put("questionid", List.getQuest_ID());

                                if(listQuest.get(j).getOptionsIsDefault().equals("1")){
                                    postedJSON.put("optionid",  List.getYesOptionsID());
                                }else {
                                    postedJSON.put("optionid",  List.getNoOptionsID());
                                }
                                postedJSON.put("multipleoptionids", "");
                                postedJSON.put("value", List.getDescriptionValue());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            array.put(postedJSON);

                        }else{
                            isExsits= false;
                        }

                    }


                    if(isExsits){
                        JSONObject json = new JSONObject();
                        try {
                            String uniqueId = UUID.randomUUID().toString();
                            json.put("id",uniqueId);
                            json.put("answeredInspectionId",TruckInsID);
                            json.put("ansdQuestions",array);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println("SOD---"+json.toString());

                        postCreateSOD(json.toString());


                    }else {
                        Toast.makeText(Activity_RouteSOD.this, "Select mandatory fields!", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    isExsits =true;
                }


            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        ArrayAdapter<String> adapterQ = new ArrayAdapter<String>(Activity_RouteSOD.this, R.layout.spinner_item,arTruckInsTittle);
        adapterQ.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spQuestion.setAdapter(adapterQ);
            }
        });
        spQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TruckInsID = arTruckInsID.get(position);
                System.out.println("TruckInsID----"+TruckInsID);


                if (listQuest.size() > 0){
                    listQuest.clear();
                }

                for (int i=0; i<arTruckInspectionID.size(); i++) {

                    String TruckInspectionID = arTruckInspectionID.get(i);

                    if(TruckInspectionID.equals(TruckInsID)){

                        String questionID = arTruckInsQuesID.get(i);

                        System.out.println("questionID----"+questionID);

                        for (int j=0; j<arTruckQuesID.size(); j++) {

                            String TruckQuesID = arTruckQuesID.get(j);

                            if(questionID.equals(TruckQuesID)){

                                String TruckQuesType = arTruckQuesType.get(j);
                                String TruckQuesTittle = arTruckQuesTittle.get(j);
                                String TruckQues_ID = arTruckQuesID.get(j);


                                for (int k=0; k<arOptionsQuestionId.size(); k++) {

                                    String QuesID = arOptionsQuestionId.get(k);

                                    if (QuesID.equals(TruckQues_ID)) {

                                        String OptionsID = arOptionsID.get(k);
                                        String OptionsIsDefault = arOptionsIsDefault.get(k);
                                        String OptionsTitle = arOptionsTitle.get(k);
                                        String OptionsQuestionId = arOptionsQuestionId.get(k);

                                        System.out.println("OptionsID----"+OptionsID);
                                        System.out.println("OptionsIsDefault----"+OptionsIsDefault);
                                        System.out.println("OptionsTitle----"+OptionsTitle);

                                        if(listQuest.size()>0){
                                            if(listQuest.contains(OptionsQuestionId)){
                                                //Update
                                                if(OptionsIsDefault.equals("1")) {
                                                    //Yes
                                                    listQuest.get(k).setYesOptionsID(OptionsID);
                                                }else if(OptionsIsDefault.equals("0")){
                                                    //No
                                                    listQuest.get(k).setNoOptionsID(OptionsID);
                                                }

                                            }else {
                                                //Add
                                                if(OptionsIsDefault.equals("1")) {
                                                    //Yes
                                                    listQuest.add(new Model_QuestHistory(TruckQues_ID,TruckQuesTittle,TruckQuesType,"Yes",TruckInsID,"",OptionsID,OptionsIsDefault,QuesID,""));
                                                    adapterQues.notifyDataSetChanged();
                                                    break;

                                                }else if(OptionsIsDefault.equals("0")){
                                                    //No
                                                    listQuest.add(new Model_QuestHistory(TruckQues_ID,TruckQuesTittle,TruckQuesType,"Yes",TruckInsID,"","",OptionsIsDefault,QuesID,OptionsID));
                                                    adapterQues.notifyDataSetChanged();
                                                    break;
                                                }


                                            }

                                        }else {
                                            if(OptionsIsDefault.equals("1")) {
                                                //Yes
                                                listQuest.add(new Model_QuestHistory(TruckQues_ID,TruckQuesTittle,TruckQuesType,"Yes",TruckInsID,"",OptionsID,OptionsIsDefault,QuesID,""));
                                                adapterQues.notifyDataSetChanged();
                                                break;
                                            }else if(OptionsIsDefault.equals("0")){
                                                //No
                                                listQuest.add(new Model_QuestHistory(TruckQues_ID,TruckQuesTittle,TruckQuesType,"Yes",TruckInsID,"","",OptionsIsDefault,QuesID,OptionsID));
                                                adapterQues.notifyDataSetChanged();

                                                break;
                                            }
                                        }

                                    }
                                }

                            }

                        }

                    }


                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        GetInspectionDetails();
    }

    private void GetInspectionDetails() {
          Utils.getInstance().loadingDialog(this, "Please wait.");
        try {
            App.getInstance().GetInspectionDetails(token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
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
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray data = jsonObject.getJSONArray("truckInspection");
                            JSONArray data1 = jsonObject.getJSONArray("truckInspectionQuestion");
                            JSONArray data2 = jsonObject.getJSONArray("truckQuestion");
                            JSONArray data3 = jsonObject.getJSONArray("truckQuestionOptions");
                            if (arTruckInsTittle.size() > 0) arTruckInsTittle.clear();
                            if (arTruckInsID.size() > 0) arTruckInsID.clear();

                            if (arTruckInsQuesID.size() > 0) arTruckInsQuesID.clear();
                            if (arTruckInspectionID.size() > 0) arTruckInspectionID.clear();

                            if (data.length() > 0) {
                               // arTruckInsTittle.add("Select Truck Inspection");
                               // arTruckInsID.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js = data.getJSONObject(i);
                                    arTruckInsTittle.add(js.getString("title"));
                                    arTruckInsID.add(js.getString("id"));
                                }
                                ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(Activity_RouteSOD.this, R.layout.spinner_item,arTruckInsTittle);
                                adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spQuestion.setAdapter(adapterBType);
                                        Utils.getInstance().dismissDialog();

                                    }
                                });


                                for (int i=0; i<data1.length(); i++) {
                                    JSONObject js = data1.getJSONObject(i);
                                    arTruckInspectionID.add(js.getString("inspectionid"));
                                    arTruckInsQuesID.add(js.getString("questionid"));
                                }

                                for (int i=0; i<data2.length(); i++) {
                                    JSONObject js = data2.getJSONObject(i);
                                    arTruckQuesID.add(js.getString("id"));
                                    arTruckQuesTittle.add(js.getString("title"));
                                    arTruckQuesType.add(js.getString("type"));
                                }

                                for (int i=0; i<data3.length(); i++) {
                                    JSONObject js = data3.getJSONObject(i);
                                    arOptionsID.add(js.getString("id"));
                                    arOptionsTitle.add(js.getString("title"));
                                    arOptionsQuestionId.add(js.getString("questionid"));
                                    arOptionsIsDefault.add(js.getString("isdefault"));
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

    private void postCreateSOD(String json) {
        Utils.getInstance().loadingDialog(Activity_RouteSOD.this, "Please wait.");
        try {
            App.getInstance().PostInspection(json,token, new Callback() {
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
                                    String data = jsonObject.getString("data");
                                    boolean succeeded = jsonObject.getBoolean("succeeded");
                                    if ( succeeded == true) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                rdSharedPreferences = getSharedPreferences(rdSPref,MODE_PRIVATE);
                                                SharedPreferences.Editor editor = rdSharedPreferences.edit();
                                                editor.putString(Route_InsID,  String.valueOf(data));
                                                editor.apply();


                                                Toast.makeText(Activity_RouteSOD.this, "Inspection Added Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Activity_RouteSOD.this, Activity_RoutePlanning.class);
                                                startActivity(intent);
                                                Utils.getInstance().hideKeyboard(Activity_RouteSOD.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}