package com.highgreen.catalogs.core.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.listener.AnimateFirstDisplayListener;
import com.highgreen.catalogs.core.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class CoverFlowAdapter extends ArrayAdapter<ProductItem> {
	
	private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public CoverFlowAdapter(Context context, int resource, ArrayList<ProductItem> data) {
        super(context, resource, data);
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                //.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coverflow_item,null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.text = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(getItem(position).getImageUrl(), holder.image, options);
        holder.text.setText(getItem(position).getTitle());
		return convertView;
	}


    static class ViewHolder {
        public ImageView image;
        public TextView text;
    }


}
