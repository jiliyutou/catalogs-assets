package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.upyun.UpYun;
import com.highgreen.catalogs.core.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tihong on 16-1-24.
 */
public class CertificateActivity extends Activity{

    private final static String TAG = "CertificateActivity";
    private final static String certificate_folder_name = "certificate";

    private ListView certificate_listView;
    private DisplayImageOptions options;
    private List<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.certificate_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        initUI();
        certificate_listView = (ListView) findViewById(R.id.certificate_list);
        new GetCertificateTask().execute(certificate_folder_name);
    }

    private void initUI() {

        ImageView back_arrow = (ImageView) findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.VISIBLE);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView left_back_title = (TextView) findViewById(R.id.left_back_title);
        left_back_title.setVisibility(View.VISIBLE);
        left_back_title.setText(R.string.app_name);

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.VISIBLE);
        middle_text_title.setText(R.string.cn_product_certificate);
    }

    private class GetCertificateTask extends AsyncTask<String, Void, List<String>>{

        @Override
        protected List<String> doInBackground(String... params) {

            List<String> urls = new ArrayList<String>();
            try {
                String[] paths = getAssets().list(params[0]);
                List<String> list = new ArrayList<String>(Arrays.asList(paths));
                for (String file : list){
                    String url = "assets://"+params[0]+"/"+file;
                    urls.add(url);
                    Log.i(TAG, file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return urls;
        }

        @Override
        protected void onPostExecute(final List<String> urls) {
            urlList = urls;
            certificate_listView.setAdapter(new ImageListViewAdapter(CertificateActivity.this, R.layout.certificate_item, urls));
            certificate_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProductItem item = new ProductItem();
                    item.setImageUrl(urls.get(position));
                    Intent intent = new Intent(CertificateActivity.this, ProductImageViewActivity.class);
                    intent.putExtra("productItem", item);
                    startActivity(intent);
                }
            });

            if (urls == null || urls.size() == 0) {
                Toast.makeText(getApplicationContext(),"没有证书", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class ImageListViewAdapter extends ArrayAdapter<String> {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private Context mContext;
        private int resource;

        public ImageListViewAdapter(Context context, int resource, List<String> data) {
            super(context, resource, data);
            this.mContext = context;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(resource, null);
                holder.image = (ImageView) convertView.findViewById(R.id.certificate_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position), holder.image, options);
            return convertView;
        }

        /**
         * ViewHolder类用保存item中的控件引用
         */
        final class ViewHolder {
            ImageView image;
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // AnimateFirstDisplayListener.displayedImages.clear();
        super.onBackPressed();
    }

}
