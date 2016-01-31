package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.gson.Gson;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.upyun.UpYun;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.bean.CategoryItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruantihong on 1/20/16.
 */
public class CategoryActivity extends Activity {

    private final static String TAG = "CategoryActivity";
    private final static String thumbnail_folder_name = "thumbnail";

    private List<CategoryItem> data = new ArrayList<CategoryItem>();
    private CategoryListAdapter adapter;
    private ListView listView;
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.category_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        initUI();
        listView = (ListView) findViewById(R.id.category_list);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        new GetCategoryTask().execute(thumbnail_folder_name);
    }

    private void initUI() {
        //hide language button
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

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setText(R.string.category_title);
    }

    public class CategoryListAdapter extends ArrayAdapter<CategoryItem> {

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private Context mContext;
        private int resource;

        public CategoryListAdapter(Context context, int resource, List<CategoryItem> data) {
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
                holder.image = (ImageView) convertView.findViewById(R.id.category_image);
                holder.title = (TextView) convertView.findViewById(R.id.category_title);
                holder.number = (TextView) convertView.findViewById(R.id.category_number);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage(getItem(position).getImageUrl(), holder.image, options, animateFirstListener);
            holder.title.setText(getItem(position).getTitle());
            holder.number.setText("Number of products: " + getItem(position).getNumber());
            return convertView;
        }
        /**
         * ViewHolder类用保存item中的控件引用
         */
        final class ViewHolder {
            ImageView image;
            TextView title;
            TextView number;
        }


    }

    private class GetCategoryTask extends AsyncTask<String, Void, List<CategoryItem>> {
        @Override
        protected List<CategoryItem> doInBackground(String... params) {
            List<CategoryItem> categoryItemList = new ArrayList<CategoryItem>();
            try {
                String[] paths = getAssets().list(params[0]);
                List<String> list = new ArrayList<String>(Arrays.asList(paths));
                for (String file : list){
                    CategoryItem categoryItem = new CategoryItem();
                    String url = "assets://"+params[0]+"/"+file;
                    categoryItem.setImageUrl(url);
                    String title = file.split("\\.")[0];
                    categoryItem.setTitle(title);
                    title = title.replaceAll(" ", "_");
                    Log.i(TAG, title);
                    String httpHeader = MainApplication.HTTP_PREFIX + title + File.separator;
                    String currentPath = MainApplication.ROOT_PATH + title;
                    int number = 0;
//                    List<UpYun.FolderItem> folderItemList= MainApplication.getUpYun().readDir(MainApplication.ROOT_PATH + title);
//                    if (folderItemList !=null){
//                        number = folderItemList.size();
//                    }else {
//                        Log.i(TAG,"folder: " + title + " is not exist.");
//                    }
                    categoryItem.setNumber(number);
                    categoryItem.setHttpHeader(httpHeader);
                    categoryItem.setCurrentPath(currentPath);
                    categoryItemList.add(categoryItem);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//
//            Gson gson = new Gson();
//            String json = gson.toJson(categoryItemList);
//            try {
//                File path = Environment.getExternalStorageDirectory();
//                File fileDir = new File(path,"test.json");
//                FileWriter writer = new FileWriter(fileDir);
//                writer.write(json);
//                writer.close();
//                Log.i(TAG,json);
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
            return categoryItemList;

        }

        @Override
        protected void onPostExecute(List<CategoryItem> categoryItems) {
            super.onPostExecute(categoryItems);
            data = categoryItems;
            adapter = new CategoryListAdapter(CategoryActivity.this, R.layout.category_item, data);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CategoryItem item = data.get(position);
                    Intent intent = new Intent(CategoryActivity.this, ProductGridActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("currentPath", item.getCurrentPath());
                    intent.putExtra("httpHeader", item.getHttpHeader());
                    startActivity(intent);
                }
            });
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
