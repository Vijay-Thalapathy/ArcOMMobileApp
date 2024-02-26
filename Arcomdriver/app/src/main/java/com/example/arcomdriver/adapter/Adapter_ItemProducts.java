package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.example.arcomdriver.common.Utils;
import com.example.arcomdriver.model.Model_ItemProducts;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 27 Sep 2022*/
public class Adapter_ItemProducts extends RecyclerView.Adapter<Adapter_ItemProducts.ViewHolder> {

	private List<Model_ItemProducts> stList;
	private Context context;

	public Adapter_ItemProducts(List<Model_ItemProducts> students) {
		this.stList = students;

	}

	// Create new views
	@Override
	public Adapter_ItemProducts.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.cardview_row, null);
		context = parent.getContext();
		return new Adapter_ItemProducts.ViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

		final int pos = position;

		viewHolder.MtQty_et.setText(stList.get(position).getProduct_quantity());

		if(stList.get(position).getUpsc_code().equals("null")){
			viewHolder.mt_UpscCode_tv.setText("N/A");
		}else {
			viewHolder.mt_UpscCode_tv.setText(stList.get(position).getUpsc_code());
		}

		if(stList.get(position).getUpsc_code().equals("null")){
			viewHolder.mt_UpscCode_tv.setText("N/A");
		}else {
			viewHolder.mt_UpscCode_tv.setText(stList.get(position).getUpsc_code());
		}



		viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

		viewHolder.chkSelected.setTag(stList.get(position));

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

				if (cb.isChecked()) {
					stList.get(pos).setSelected(cb.isChecked());

					if(Double.valueOf(viewHolder.MtQty_et.getText().toString())==0){

						stList.get(pos).setProduct_quantity("1");
						viewHolder.MtQty_et.setText("1");

					}else {
						stList.get(pos).setProduct_quantity(viewHolder.MtQty_et.getText().toString());
						viewHolder.MtQty_et.setText(viewHolder.MtQty_et.getText().toString());

					}


					double TotalAmt = Double.parseDouble(viewHolder.MtQty_et.getText().toString()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());
					viewHolder.mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(TotalAmt)));

				} else if (!cb.isChecked()){
					stList.get(pos).setSelected(cb.isChecked());

					stList.get(pos).setProduct_quantity("0");
					viewHolder.MtQty_et.setText("0");

					double TotalAmt = Double.parseDouble("0") * Double.parseDouble(stList.get(position).getPrice_PerUnit());
					viewHolder.mtAmount_tv.setText(Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(TotalAmt)));

				}

            }
        });

		System.out.println("Image---"+Const.ImageProducts + stList.get(position).getProduct_imageurl());

		if (stList.get(position).getProduct_imageurl() != null) {
			Picasso.with(viewHolder.p1.getContext()).load(Const.ImageProducts + stList.get(position).getProduct_imageurl())
					.placeholder(R.drawable.image_placeholder)
					.error(R.drawable.image_placeholder)
					.into(viewHolder.p1);
		}else{
			viewHolder.p1.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
		}

		/*if(stList.get(position).getProduct_imageurl().equals("null")){
			viewHolder.p1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
		}else {

			String imageUri = Const.ImageHostName+stList.get(position).getProduct_imageurl();
			Picasso.with(context).load(imageUri).fit().centerInside()
					.placeholder(R.drawable.image_placeholder)
					.error(R.drawable.image_placeholder)
					.into(viewHolder.p1);

		}*/

		viewHolder.pricePer_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				/*if (!TextUtils.isEmpty(s)) {
					if(Double.parseDouble(viewHolder.pricePer_et.getText().toString())>0){
						stList.get(pos).setPrice_PerUnit(viewHolder.pricePer_et.getText().toString());
						//notifyDataSetChanged();
					}

				}*/


			}
		});

		System.out.println("QTY---------"+stList.get(position).getProduct_quantity());
		System.out.println("PRicePER---------"+stList.get(position).getPrice_PerUnit());



		double TotalAmt = Double.parseDouble(stList.get(position).getProduct_quantity()) * Double.parseDouble(stList.get(position).getPrice_PerUnit());

		//viewHolder.pricePer_et.setText(Utils.truncateDecimal(Double.valueOf(stList.get(position).getPrice_PerUnit())));
		viewHolder.pricePer_et.setText(String.valueOf(Double.valueOf(stList.get(position).getPrice_PerUnit())));

		viewHolder.mtAmount_tv.setText("-"+Activity_OrdersHistory.currency_symbol+ Utils.truncateDecimal(Double.valueOf(TotalAmt)));


		if (stList.get(position).getDescription().equals("")||stList.get(position).getDescription().equals("null")){
			viewHolder.p2_tv.setText(stList.get(position).getProduct_name());
		}else {
			viewHolder.p2_tv.setText(stList.get(position).getProduct_name()+" - "+ stList.get(position).getDescription());
		}

		if(stList.get(position).getIstaxable().equals("true")){
			viewHolder.AddchkSelected.setChecked(true);
		}else {
			viewHolder.AddchkSelected.setChecked(false);
		}


		viewHolder.AddchkSelected.setTag(stList.get(position));

		if (Activity_OrdersHistory.PricingSetup.equals("1")){
			viewHolder.pricePer_et.setFocusable(true);
			viewHolder.pricePer_et.setFocusableInTouchMode(true);
			viewHolder.pricePer_et.setClickable(true);
		}else {
			viewHolder.pricePer_et.setFocusable(false);
			viewHolder.pricePer_et.setFocusableInTouchMode(false);
			viewHolder.pricePer_et.setClickable(false);
		}


		if (Activity_OrdersHistory.tax.equals("1")){
			viewHolder.tax_ll.setVisibility(View.VISIBLE);
		}else {
			viewHolder.tax_ll.setVisibility(View.GONE);
		}


	}

	@Override
	public int getItemCount() {
		return stList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public CheckBox chkSelected;
		AppCompatTextView p2_tv,mtAmount_tv,mt_UpscCode_tv;
		AppCompatEditText MtQty_et;
		AppCompatImageView p1;
		AppCompatEditText pricePer_et;
		AppCompatCheckBox AddchkSelected;
		LinearLayout tax_ll;


		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);

			chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
			MtQty_et = itemLayoutView.findViewById(R.id.MtQty_et);
			p1 = itemLayoutView.findViewById(R.id.p1);
			p2_tv = itemLayoutView.findViewById(R.id.p2_tv);
			pricePer_et = itemLayoutView.findViewById(R.id.p4_tv);
			mtAmount_tv = itemLayoutView.findViewById(R.id.mtAmount_tv);
			AddchkSelected = itemView.findViewById(R.id.AddchkSelected);
			mt_UpscCode_tv = itemView.findViewById(R.id.mt_UpscCode_tv);
			tax_ll = itemView.findViewById(R.id.tax_ll);
		}

	}
	public List<Model_ItemProducts> getStudentist() {
		return stList;
	}


	@SuppressLint("NotifyDataSetChanged")
	public void setFilter(ArrayList<Model_ItemProducts> arrayList1) {
		stList = new ArrayList<>();
		stList.clear();
		stList.addAll(arrayList1);
		notifyDataSetChanged();
	}


}
