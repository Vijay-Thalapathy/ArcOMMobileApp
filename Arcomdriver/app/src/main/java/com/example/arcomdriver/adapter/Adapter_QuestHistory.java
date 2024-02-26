package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_QuestHistory;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 02 Nov 2022*/
public class Adapter_QuestHistory extends RecyclerView.Adapter<Adapter_QuestHistory.QrViewHolder> {
    private ArrayList<Model_QuestHistory> itemList_cu;
    private Context context;
    public Adapter_QuestHistory(ArrayList<Model_QuestHistory> list1) {
        this.itemList_cu = list1;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_questhistory,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, @SuppressLint("RecyclerView") final int i) {



        if(itemList_cu.get(i).getQuest_Type().equals("singleradio")){
            holder.SingleRadioQuest_tv.setText(itemList_cu.get(i).getQuest_Tittle());
            holder.SingleRadio_ll.setVisibility(View.VISIBLE);

        }else if(itemList_cu.get(i).getQuest_Type().equals("decision")){
            holder.SingleRadioQuest_tv.setText(itemList_cu.get(i).getQuest_Tittle());
            holder.SingleRadio_ll.setVisibility(View.VISIBLE);

        }else if(itemList_cu.get(i).getQuest_Type().equals("input")){
            holder.DescQuest_tv.setText(itemList_cu.get(i).getQuest_Tittle());
            holder.Description_ll.setVisibility(View.VISIBLE);
            itemList_cu.get(i).setRadio_Status("Description");

            holder.et_decribtion.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    if (!TextUtils.isEmpty(s)) {
                        itemList_cu.get(i).setDescriptionValue( holder.et_decribtion.getText().toString());
                        // notifyDataSetChanged();

                    }
                }
            });

        }else if(itemList_cu.get(i).getQuest_Type().equals("description")){
            holder.DescQuest_tv.setText(itemList_cu.get(i).getQuest_Tittle());
            holder.Description_ll.setVisibility(View.VISIBLE);
            itemList_cu.get(i).setRadio_Status("Description");

            holder.et_decribtion.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    if (!TextUtils.isEmpty(s)) {
                        itemList_cu.get(i).setDescriptionValue( holder.et_decribtion.getText().toString());
                        // notifyDataSetChanged();

                    }
                }
            });

        }


        holder.radioGroup_.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioYes) {

                    itemList_cu.get(i).setRadio_Status("Yes");

                    itemList_cu.get(i).setOptionsIsDefault("1");

                    /*if(itemList_cu.get(i).getQuest_ID().equals(itemList_cu.get(i).getOptionsQuestionId())){
                        if(itemList_cu.get(i).getOptionsIsDefault().equals("1")){
                            itemList_cu.get(i).setOptionsID(itemList_cu.get(i).getOptionsID());
                        }
                    }
*/
                }else if (checkedId == R.id.radioNo) {

                    itemList_cu.get(i).setRadio_Status("No");

                    itemList_cu.get(i).setOptionsIsDefault("0");

                   /* if(itemList_cu.get(i).getQuest_ID().equals(itemList_cu.get(i).getOptionsQuestionId())){
                        if(itemList_cu.get(i).getOptionsIsDefault().equals("0")){
                            itemList_cu.get(i).setOptionsID(itemList_cu.get(i).getOptionsID());
                        }
                    }*/

                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return itemList_cu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView SingleRadioQuest_tv,DescQuest_tv;
        LinearLayout SingleRadio_ll,Description_ll;
        RadioGroup radioGroup_;
        RadioButton radioYes,radioNo;

        AppCompatEditText et_decribtion;


        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            SingleRadioQuest_tv = itemView.findViewById(R.id.SingleRadioQuest_tv);
            SingleRadio_ll = itemView.findViewById(R.id.SingleRadio_ll);
            radioGroup_ =itemView.findViewById(R.id.radioGroup_);
            Description_ll =itemView.findViewById(R.id.Description_ll);
            DescQuest_tv =itemView.findViewById(R.id.DescQuest_tv);
            radioYes = itemView.findViewById(R.id.radioYes);
            radioNo = itemView.findViewById(R.id.radioNo);
            et_decribtion = itemView.findViewById(R.id.et_decribtion);
        }
    }




}
