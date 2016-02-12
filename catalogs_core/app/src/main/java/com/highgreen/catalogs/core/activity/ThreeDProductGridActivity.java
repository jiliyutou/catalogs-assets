package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.adapter.ProductGridAdapter;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.upyun.UpYun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhk on 16-2-8.
 */
public class ThreeDProductGridActivity extends Activity {

    private List<ProductItem> data = new ArrayList<ProductItem>();
    private ProductGridAdapter productGridAdapter;
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;
    private GridView gridView;

    private String currentPath;
    private String httpHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_grid_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        this.gridView = (GridView) findViewById(R.id.gridview);
        this.currentPath = MainApplication.THREED_PRODUCTS_PATH;
        this.httpHeader = MainApplication.THREED_PRODUCTS_UPYUN_URL;

        initUI();
        new GetThreeDProductTask().execute(currentPath, httpHeader);
    }

    private void initUI() {
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
        left_back_title.setText(R.string.app_name);

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.VISIBLE);
        middle_text_title.setText(R.string.threeD_product_title);
    }

    @Override
    public void onBackPressed() {
        // AnimateFirstDisplayListener.displayedImages.clear();
        super.onBackPressed();
    }

    private class GetThreeDProductTask extends AsyncTask<String, Void, List<ProductItem>> {
        @Override
        protected List<ProductItem> doInBackground(String... params) {
            String currentPath = params[0];
            String httpHeader = params[1];

            List<UpYun.FolderItem> folderItemList = MainApplication.getUpYun().readDir(currentPath);
            List<ProductItem> productItemList = new ArrayList<ProductItem>();

            if (folderItemList != null && !folderItemList.isEmpty()) {
                for (UpYun.FolderItem item : folderItemList) {
                    System.out.println(item);
                    if (!item.name.split("\\.")[1].equals("html")){
                        ProductItem productItem = new ProductItem();
                        productItem.setImageUrl(httpHeader +item.name);
                        productItem.setTitle(item.name.split("\\.")[0]);
                        productItemList.add(productItem);
                    }
                }
            }
            return productItemList;
        }

        @Override
        protected void onPostExecute(List<ProductItem> productItems) {
            super.onPostExecute(productItems);
            if (productItems != null && !productItems.isEmpty()) {
                data = productItems;
                productGridAdapter = new ProductGridAdapter(ThreeDProductGridActivity.this, R.layout.product_grid_item, data);
                gridView.setAdapter(productGridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ProductItem item = data.get(position);
                        Intent intent = new Intent(ThreeDProductGridActivity.this, ThreeDProductWebViewActivity.class);
                        intent.putExtra("url", httpHeader + item.getTitle() + ".html");
                        intent.putExtra("title", item.getTitle());
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "目前无3D图片产品展示", Toast.LENGTH_LONG).show();
            }
        }
    }
}
