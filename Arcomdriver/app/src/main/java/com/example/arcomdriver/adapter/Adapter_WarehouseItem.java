package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.App;
import com.example.arcomdriver.model.Model_WarehouseItem;
import com.example.arcomdriver.order.Activity_OrdersHistory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 25 Nov 2022*/
public class Adapter_WarehouseItem extends RecyclerView.Adapter<Adapter_WarehouseItem.QrViewHolder> {
    private ArrayList<Model_WarehouseItem> itemList;
    private Context context;
    private Activity activity;
    private ArrayList<String> arLtypeId = new ArrayList<>();
    private ArrayList<String> arLtypeName = new ArrayList<>();

    private ArrayList<String> arLocationId = new ArrayList<>();
    private ArrayList<String> arLocationName = new ArrayList<>();
    public Adapter_WarehouseItem(Activity activity,ArrayList<Model_WarehouseItem> list) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_warehouseitem,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {


       // arLocationName.add("Select Location *");
      //  arLocationId.add("0");

        if(itemList.get(i).getOnhand().equals("null")){
            holder.et_OnHand.setText("0");
        }else {
            holder.et_OnHand.setText(itemList.get(i).getOnhand());
        }

        if(itemList.get(i).getAvailable().equals("null")){
            holder.pd_available_tv.setText("0");
        }else {
            holder.pd_available_tv.setText(itemList.get(i).getAvailable());
        }


        try {
            App.getInstance().GetWareHouseType(Activity_OrdersHistory.token,new Callback(){

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();

                        try {
                            JSONObject js = new JSONObject(res);
                            JSONArray data = js.getJSONArray("options");

                            if (arLtypeId.size() > 0) arLtypeId.clear();
                            if (arLtypeName.size() > 0) arLtypeName.clear();


                            if (data.length() > 0) {
                               arLtypeName.add("Select Location Type *");
                               arLtypeId.add("0");

                                for (int i=0; i<data.length(); i++) {
                                    JSONObject js0 = data.getJSONObject(i);

                                    arLtypeName.add(js0.getString("displayname"));
                                    arLtypeId.add(js0.getString("id"));

                                }
                                ArrayAdapter<String> adapterPGroup = new ArrayAdapter<String>(context, R.layout.spinner_item,arLtypeName);
                                adapterPGroup.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.Wh_locationType_sp.setAdapter(adapterPGroup);
                                        holder.Wh_locationType_sp.setSelection(arLtypeName.indexOf(itemList.get(i).getLocationType()));
                                        // adapterPGroup.notifyDataSetChanged();
                                       // notifyDataSetChanged();
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

        holder.Wh_locationType_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String LtypeS= arLtypeId.get(position);
                String LName= arLtypeName.get(position);

                itemList.get(i).setLocationType(LName);
                itemList.get(i).setLocationTypeID(LtypeS);


                if(!LtypeS.equals("0")){
                    try {
                        App.getInstance().GetWareHouseLocation(LName,Activity_OrdersHistory.token,new Callback(){

                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String res = response.body().string();

                                    try {
                                        JSONObject jsonObject = new JSONObject(res);
                                        JSONArray data = jsonObject.getJSONArray("data");
                                        if (arLocationName.size() > 0) arLocationName.clear();
                                        if (arLocationId.size() > 0) arLocationId.clear();
                                        if (data.length() > 0) {


                                            for (int i=0; i<data.length(); i++) {
                                                JSONObject js = data.getJSONObject(i);
                                                arLocationName.add(js.getString("warehousenumber")+" - "+js.getString("warehousename"));
                                                arLocationId.add(js.getString("id"));
                                            }
                                            ArrayAdapter<String> adapterBType = new ArrayAdapter<String>(activity, R.layout.spinner_item,arLocationName);
                                            adapterBType.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    holder.Wh_location_sp.setAdapter(adapterBType);
                                                    holder.Wh_location_sp.setSelection(arLocationName.indexOf(itemList.get(i).getLocation()));

                                                    //  adapterBType.notifyDataSetChanged();
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.Wh_location_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String LtypeS= arLocationId.get(position);
                String LName= arLocationName.get(position);
                itemList.get(i).setLocation(LName);
                System.out.println("LName--"+LName);

                itemList.get(i).setWarehouseID(LtypeS);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.et_OnHand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              if (holder.et_OnHand.getText().toString().startsWith(" ")) {
                  holder.et_OnHand.setText("");
                }else  if (holder.et_OnHand.getText().toString().startsWith(".")) {
                  holder.et_OnHand.setText("");
                }else {
                    itemList.get(i).setOnhand(holder.et_OnHand.getText().toString().trim());
                    holder.pd_available_tv.setText(holder.et_OnHand.getText().toString().trim());
                    itemList.get(i).setAvailable(holder.pd_available_tv.getText().toString().trim());


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatSpinner Wh_locationType_sp;
        AppCompatSpinner Wh_location_sp;
        AppCompatEditText et_OnHand;
        AppCompatTextView pd_available_tv;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            Wh_locationType_sp = itemView.findViewById(R.id.Wh_locationType_sp);
            Wh_location_sp = itemView.findViewById(R.id.Wh_location_sp);
            et_OnHand = itemView.findViewById(R.id.et_OnHand);
            pd_available_tv = itemView.findViewById(R.id.pd_available_tv);
            }

    }
    // method to access in activity after updating selection
    public List<Model_WarehouseItem> getStudentist() {
        return itemList;
    }




}
