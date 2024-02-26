package com.example.arcomdriver.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.common.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 10 Dec 2022*/
public class ImageViewAdapter extends BaseAdapter {

    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ArrayList<String> mArrayUri;
    public ImageViewAdapter(Context ctx, ArrayList<String> mArrayUri) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);

        String payment_img =mArrayUri.get(position);

        if (payment_img != null) {
            if(payment_img.startsWith("Files")){
                Picasso.with(ivGallery.getContext()).load(Const.ImageDeliverySignature+ payment_img)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .into(ivGallery);
            }else {
                Picasso.with(ivGallery.getContext()).load(Const.ImagePayments+ payment_img)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .into(ivGallery);
            }


        }else{
            ivGallery.setImageDrawable(ctx.getResources().getDrawable(R.drawable.image_placeholder));
        }



        return itemView;
    }


}