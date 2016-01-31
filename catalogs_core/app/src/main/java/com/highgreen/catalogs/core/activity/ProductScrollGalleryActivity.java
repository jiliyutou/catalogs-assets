package com.highgreen.catalogs.core.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.utils.UpYun;
import com.meiya.simon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruantihong on 1/22/16.
 */
public class ProductScrollGalleryActivity extends FragmentActivity {

    private ScrollGalleryView scrollGalleryView;
    private DisplayImageOptions options;
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_scroll_gallery_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);



        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        Bundle bundle = getIntent().getExtras();
        ArrayList<ProductItem> productItemList =  (ArrayList<ProductItem>) getIntent().getSerializableExtra("productItemList");
        String title = bundle.getString("title");
        String currentPath = bundle.getString("currentPath");
        String httpHeader = bundle.getString("httpHeader");
        initUI(title);

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(80)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager());

        new GetProductTask().execute(currentPath,title,httpHeader);


    }

    private void initUI(String title) {
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.VISIBLE);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        left_back_title = (TextView) findViewById(R.id.left_back_title);
        left_back_title.setVisibility(View.VISIBLE);
        left_back_title.setText(title);

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.INVISIBLE);
    }


    private class GetProductTask extends AsyncTask<String,Void,List<String>>{

        @Override
        protected List<String> doInBackground(String... params) {
            System.out.println(params[0]);
            List<UpYun.FolderItem> folderItemList = MainApplication.getUpYun().readDir(params[0]);
            List<String> urls = new ArrayList<String>();
            if (folderItemList!=null&&folderItemList.size() > 1){
                for (UpYun.FolderItem item : folderItemList){
                    String name = item.name.split("\\.")[0];
                    if (!params[1].equals(name)){
                        String url = params[2]+item.name;
                        urls.add(url);
                    }
                }
            }
            return urls;
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            super.onPostExecute(urls);
            if (urls!=null&&urls.size() > 0){
                for (String url :urls){
                    new GetImageTask().execute(url);
                }
            }
        }
    }

    private class GetImageTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            return imageLoader.loadImageSync(params[0],options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            scrollGalleryView.addImage(bitmap);
        }
    }
}
