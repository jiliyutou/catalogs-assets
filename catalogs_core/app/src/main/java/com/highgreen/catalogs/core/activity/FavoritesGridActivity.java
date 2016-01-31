package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.adapter.ProductGridAdapter;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.database.DataBaseManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tihong on 16-1-24.
 */
public class FavoritesGridActivity extends Activity {
    private final static String TAG = "FavoritesGridActivity";
    private DataBaseManager mDataBaseManager;
    private List<ProductItem> productItemList;
    private GridView gridView;
    private ImageView share;
    private ProductGridAdapter mProductGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.favorite_grid_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        mDataBaseManager = new DataBaseManager(getApplicationContext());
        productItemList = mDataBaseManager.query();
        gridView = (GridView) findViewById(R.id.gridview);
        if (productItemList != null) {
            mProductGridAdapter = new ProductGridAdapter(FavoritesGridActivity.this, R.layout.product_grid_item, productItemList);
            gridView.setAdapter(mProductGridAdapter);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductItem productItem = productItemList.get(position);
                Intent intent = new Intent(FavoritesGridActivity.this, FavoritesCoverFlowActivity.class);
                intent.putExtra("productItem", productItem);
                intent.putExtra("productList", (Serializable) productItemList);
                startActivity(intent);
            }
        });
        initUI();


        if (productItemList == null || productItemList.size() == 0) {
            //
            findViewById(R.id.no_favorite).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.no_favorite).setVisibility(View.GONE);
        }

        share = (ImageView) findViewById(R.id.share);
        share.setVisibility(View.GONE);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productItemList != null) {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    ArrayList<Uri> uriList = new ArrayList<>();
                    for (ProductItem item : productItemList) {
                        uriList.add(getImageUri(getApplicationContext(),
                                        imageLoader.loadImageSync(item.getImageUrl()), item.getTitle())
                        );
                    }
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "分享到"));
                }
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
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
        middle_text_title.setText(R.string.cn_favorites);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataBaseManager = new DataBaseManager(getApplicationContext());
        List<ProductItem> list = mDataBaseManager.query();
        productItemList.clear();
        productItemList.addAll(list);

        if (productItemList == null || productItemList.size() == 0) {
            //
            findViewById(R.id.no_favorite).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.no_favorite).setVisibility(View.GONE);
        }

        if (mProductGridAdapter != null) {
            mProductGridAdapter.notifyDataSetChanged();
        } else {
            Log.i(TAG, "mProductGridAdapter is null");
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseManager.close();
    }
}
