package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.MainApplication;
import com.meiya.simon.R;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.utils.UpYun;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tihong on 16-1-24.
 */
public class ProductGalleryAcitivty extends Activity {

    private DisplayImageOptions options;
    private List<ProductItem> data = new ArrayList<ProductItem>();
    private ProductGalleryAdapter productGalleryAdapter;
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;
    private Gallery gallery;

    private String title;
    private String currentPath;
    private String httpHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_gallery_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        gallery = (Gallery) findViewById(R.id.gallery);



        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        currentPath = bundle.getString("currentPath");
        httpHeader = bundle.getString("httpHeader");
        initUI(title);

        new GetProductTask().execute(currentPath, title, httpHeader);
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
        left_back_title.setText("Categories");

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.VISIBLE);
        middle_text_title.setText(title);
    }


    public class ProductGalleryAdapter extends ArrayAdapter<ProductItem> {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private Context mContext;
        private int resource;

        public ProductGalleryAdapter(Context context, int resource, List<ProductItem> data) {
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
                holder.image = (ImageView) convertView.findViewById(R.id.gallery_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).getImageUrl(), holder.image, options);
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

    private class GetProductTask extends AsyncTask<String, Void, List<ProductItem>> {
        @Override
        protected List<ProductItem> doInBackground(String... params) {

            List<UpYun.FolderItem> folderItemList = MainApplication.getUpYun().readDir(params[0]);
            List<ProductItem> productItemList = new ArrayList<ProductItem>();

            if (folderItemList != null && folderItemList.size() > 1) {
                for (UpYun.FolderItem item : folderItemList) {
                    System.out.println(item);
                    String name = item.name.split("\\.")[0];
                    if (!params[1].equals(name)) {
                        String url = params[2] + item.name;
                        System.out.println(url);
                        ProductItem productItem = new ProductItem();
                        productItem.setImageUrl(url);
                        productItem.setTitle(name);
                        productItemList.add(productItem);
                    }
                }
            }
            return productItemList;
        }

        @Override
        protected void onPostExecute(List<ProductItem> productItems) {
            super.onPostExecute(productItems);
            data = productItems;
            productGalleryAdapter = new ProductGalleryAdapter(ProductGalleryAcitivty.this, R.layout.product_gallery_item, data);
            if (productGalleryAdapter == null) {
                System.out.println("productGalleryAdapter is null");
            }
            gallery.setAdapter(productGalleryAdapter);

            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    ProductItem item = data.get(position);
//                    Intent intent = new Intent(ProductGridActivity.this, ProductScrollGalleryActivity.class);
//                    intent.putExtra("title", title);
//                    intent.putExtra("item_name",item.getTitle());
//                    intent.putExtra("currentPath", currentPath);
//                    intent.putExtra("httpHeader",httpHeader);
//                    startActivity(intent);
                }
            });
        }
    }
}
