package com.highgreen.catalogs.core.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.listener.AnimateFirstDisplayListener;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by tihong on 16-1-29.
 */

public class ProductGridAdapter extends ArrayAdapter<ProductItem> {
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context mContext;
    private int resource;

    public ProductGridAdapter(Context context, int resource, List<ProductItem> data) {
        super(context, resource, data);
        this.mContext = context;
        this.resource = resource;


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(resource, null);
            holder.image = (ImageView) convertView.findViewById(R.id.grid_image);
            holder.title = (TextView) convertView.findViewById(R.id.product_title);
            holder.image.setLayoutParams(new LinearLayout.LayoutParams(MainApplication.screen_width / 2 - 40, MainApplication.screen_width / 2 - 40));
            holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getImageUrl(), holder.image, options);
        holder.title.setText(getItem(position).getTitle());
        return convertView;
    }


    /**
     * ViewHolder类用保存item中的控件引用
     */
    final class ViewHolder {
        ImageView image;
        TextView title;
    }
}