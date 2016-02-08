package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.Category;
import com.highgreen.catalogs.core.bean.CategoryItem;
import com.highgreen.catalogs.core.adapter.CategoryItemAdpter;
import com.highgreen.catalogs.core.json.JsonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruantihong on 1/20/16.
 */
public class CategoryActivity extends Activity {

    private final static String TAG = "CategoryActivity";
    private final static String thumbnail_folder_name = "thumbnail";
    private final static String CONTACT_PATH = "json/categories.json";

    private List<CategoryItem> data = new ArrayList<CategoryItem>();
    private CategoryListAdapter adapter;
    private ListView listView;
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
        new GetCategoryTask().execute(thumbnail_folder_name, CONTACT_PATH);
    }

    private void initUI() {
        //hide language button
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

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
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

            List<CategoryItemAdpter> categoryItemAdpterList = new ArrayList<CategoryItemAdpter>();

            String categoryString = JsonUtils.getJson(getApplicationContext(), params[1]);

            if (categoryString != null) {
                Gson gson = new Gson();
                Category category = gson.fromJson(categoryString, Category.class);
                categoryItemAdpterList = category.getCategories();
            } else {
                Toast.makeText(getApplicationContext(),"categories.json is not exist or parse error ",Toast.LENGTH_SHORT);
            }

            List<CategoryItem> categoryItemList = new ArrayList<CategoryItem>();

            for (CategoryItemAdpter item : categoryItemAdpterList) {
                CategoryItem categoryItem = new CategoryItem();
                String url = "assets://" + params[0] + "/" + item.getThumbnail();
                categoryItem.setImageUrl(url);
                categoryItem.setTitle(item.getName());

                String httpHeader = MainApplication.UPYUN_REQUEST_HEADER + item.getDirectory() + File.separator;
                String currentPath = MainApplication.RESOURCE_ROOT + item.getDirectory();

                categoryItem.setHttpHeader(httpHeader);
                categoryItem.setCurrentPath(currentPath);
                categoryItemList.add(categoryItem);
            }
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
