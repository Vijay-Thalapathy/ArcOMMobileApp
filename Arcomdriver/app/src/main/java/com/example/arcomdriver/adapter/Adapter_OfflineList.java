package com.example.arcomdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_OfflineList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 15 Oct 2022*/
public class Adapter_OfflineList extends RecyclerView.Adapter<Adapter_OfflineList.ViewHolder> {

	private List<Model_OfflineList> stList;
	private Context context;

	public Adapter_OfflineList(List<Model_OfflineList> students) {
		this.stList = students;

	}

	// Create new views
	@Override
	public Adapter_OfflineList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.offlinelist_row, null);
		context = parent.getContext();
		return new Adapter_OfflineList.ViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

		final int pos = position;

		viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

		viewHolder.chkSelected.setTag(stList.get(position));

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

				if (cb.isChecked()) {
					stList.get(pos).setSelected(cb.isChecked());

				} else if (!cb.isChecked()){
					stList.get(pos).setSelected(cb.isChecked());
				}

            }
        });

		if(stList.get(position).getOrderNum().startsWith("DRF")){
			viewHolder.Of_Id_tv.setText(stList.get(position).getDraftnumber());
			viewHolder.Of_PresaleId_tv.setText("--");
			viewHolder.Or_delete_img.setVisibility(View.VISIBLE);
		}else {

			if(stList.get(position).getFlag().equals("Presale")){
				viewHolder.Of_PresaleId_tv.setText("SO "+stList.get(position).getOrderNum());
			}else {
				Double price =Double.valueOf(stList.get(position).getOrderNum());
				DecimalFormat format = new DecimalFormat("0.#");

				String in_num =format.format(price);
				viewHolder.Of_PresaleId_tv.setText("IN_ "+in_num);
			}
			viewHolder.Of_Id_tv.setText(stList.get(position).getDraftnumber());
			viewHolder.Or_delete_img.setVisibility(View.INVISIBLE);

		}

		if(stList.get(position).getFlag().equals("Presale")){
			viewHolder.dft_txt.setText("Draft Order");
		}else {
			viewHolder.dft_txt.setText("Draft Invoice");

		}

		//viewHolder.Of_PresaleId_tv.setText("-");

		if(stList.get(position).getSubmitdate().isEmpty()) {
			viewHolder.Of_Date_tv.setText("--");
		}else {
			if(!stList.get(position).getSubmitdate().equals("null")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date date = null;
				try {
					date = sdf.parse(stList.get(position).getSubmitdate());
					String date_order =new SimpleDateFormat("hh:mm").format(date);
					viewHolder.Of_Date_tv.setText(date_order);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else {
				viewHolder.Of_Date_tv.setText("--");
			}

		}




		if(stList.get(position).getUploadStatus().equals("0")){
			viewHolder.Or_status_img.setBackgroundResource(R.drawable.ic_pending);
			viewHolder.chkSelected.setVisibility(View.VISIBLE);
		}else {
			viewHolder.Or_status_img.setBackgroundResource(R.drawable.ic_tick);
			viewHolder.chkSelected.setVisibility(View.GONE);
		}

	}

	@Override
	public int getItemCount() {
		return stList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public CheckBox chkSelected;
		AppCompatTextView Of_Id_tv,Of_PresaleId_tv,Of_Date_tv,dft_txt;
		AppCompatImageView Or_status_img,Or_delete_img;


		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);

			chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
			Of_Id_tv = itemView.findViewById(R.id.Of_Id_tv);
			Of_PresaleId_tv = itemView.findViewById(R.id.Of_PresaleId_tv);
			Of_Date_tv = itemView.findViewById(R.id.Of_Date_tv);
			Or_status_img = itemView.findViewById(R.id.Or_status_img);
			Or_delete_img = itemView.findViewById(R.id.Or_delete_img);
			dft_txt = itemView.findViewById(R.id.dft_txt);
		}

	}
	public List<Model_OfflineList> getStudentist() {
		return stList;
	}


	@SuppressLint("NotifyDataSetChanged")
	public void setFilter(ArrayList<Model_OfflineList> arrayList1) {
		stList = new ArrayList<>();
		stList.clear();
		stList.addAll(arrayList1);
		notifyDataSetChanged();
	}



}
