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
import com.highgreen.catalogs.core.adapter.ProductGridAdapter;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.upyun.UpYun;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.utils.NetworkUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tihong on 16-1-24.
 */
public class ProductGridActivity extends Activity {


    private List<ProductItem> data = new ArrayList<ProductItem>();
    private ProductGridAdapter productGridAdapter;
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;
    private GridView gridView;

    private String title;
    private String currentPath;
    private String httpHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_grid_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        gridView = (GridView) findViewById(R.id.gridview);

        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        title = bundle.getString("title");
        currentPath = bundle.getString("currentPath");
        httpHeader = bundle.getString("httpHeader");
        initUI(title);
        System.out.println(title + " " + currentPath + " " + httpHeader);
        if (NetworkUtils.isNetworkAvailable(ProductGridActivity.this)) {
            new GetProductTask().execute(currentPath,title,httpHeader);
        }else{
            NetworkUtils.setNetworkMethod(ProductGridActivity.this);
        }

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
        left_back_title.setText(R.string.category_title);

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

            if (folderItemList != null && !folderItemList.isEmpty()) {
                for (UpYun.FolderItem item : folderItemList) {
                    System.out.println(item);
                    String name = item.name.split("\\.")[0];
                    if (!params[1].equals(name)){
                        String url = params[2]+item.name;
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
            if (productItems != null && !productItems.isEmpty()) {
                data = productItems;
                productGridAdapter = new ProductGridAdapter(ProductGridActivity.this, R.layout.product_grid_item, data);
                gridView.setAdapter(productGridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ProductItem item = data.get(position);
                        Intent intent = new Intent(ProductGridActivity.this, ProductCoverFlowActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("item_name", item.getTitle());
                        intent.putExtra("currentPath", currentPath);
                        intent.putExtra("httpHeader", httpHeader);
                        intent.putExtra("productItemList", (Serializable) data);
                        intent.putExtra("init_position", position);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "该目录下无产品", Toast.LENGTH_LONG).show();
            }
        }
    }
}
