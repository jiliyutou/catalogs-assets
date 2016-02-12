package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.ParamKVPair;
import com.highgreen.catalogs.core.bean.ProductDetailInfo;
import com.highgreen.catalogs.core.bean.ProductItem;

/**
 * Created by zhk on 16-2-10.
 */
public class ProductDetailActivity extends Activity {

    private static final String TAG = "ProductDetailActivity";
    private TextView mDetail;
    private String detailUrl;
    private ProductItem productItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_detail_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        productItem = (ProductItem) getIntent().getSerializableExtra("productItem");
        detailUrl = MainApplication.PRODUCT_DETAIL_PATH + productItem.getTitle() + ".json";
        mDetail = (TextView) this.findViewById(R.id.product_detail);

        initUI();
        new GetProductDetailTask().execute();
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
        left_back_title.setText(R.string.en_back);

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setText(productItem.getTitle() + "详情");
        middle_text_title.setVisibility(View.VISIBLE);
    }

    private class GetProductDetailTask extends AsyncTask<String, Void, ProductDetailInfo> {

        @Override
        protected ProductDetailInfo doInBackground(String... params) {
            ProductDetailInfo productDetailInfo = null;
            try {
                String detail = MainApplication.getUpYun().readFile(detailUrl);
                productDetailInfo = new Gson().fromJson(detail, ProductDetailInfo.class);
                Log.i(TAG, detail);
            } catch (Exception e){
                Log.i(TAG, e.toString());
            }
            return productDetailInfo;
        }

        @Override
        protected void onPostExecute(ProductDetailInfo productDetailInfo) {
            if(productDetailInfo != null) {
                String detailText = "";
                for(ParamKVPair kv : productDetailInfo.getDetails()) {
                    detailText += kv.getParamKey() + ": " + kv.getParamValue() + "\n";
                }
                mDetail.setText(detailText);
            } else {
                Toast.makeText(getApplicationContext(), "加载产品详情失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
