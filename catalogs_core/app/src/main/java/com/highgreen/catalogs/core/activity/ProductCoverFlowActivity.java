package com.highgreen.catalogs.core.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.adapter.CoverFlowAdapter;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.database.DataBaseManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by ruantihong on 1/22/16.
 */
public class ProductCoverFlowActivity extends FragmentActivity {

    private static int CACHED_IMAGE_LIMIT = 6;

    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;

    private FeatureCoverFlow mCoverFlow;
    private ProductItem productItem;

    private ImageView detail;
    private ImageView share;
    private ImageView favorite;
    private DataBaseManager mDataBaseManager;
    private TextSwitcher mTitle;
    private int initPosition;
    private int positionCount;
    private int realPosition;

    private CoverFlowAdapter adapter;
    private ArrayList<ProductItem> productItemList = null;
    private ArrayList<ProductItem> data = new ArrayList<ProductItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_cover_flow_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        mDataBaseManager = new DataBaseManager(getApplicationContext());

        mTitle = (TextSwitcher) findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(ProductCoverFlowActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.coverflow_title_item, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        productItemList = (ArrayList<ProductItem>) getIntent().getSerializableExtra("productItemList");
        realPosition = initPosition = bundle.getInt("initPosition");
        positionCount = bundle.getInt("positionCount");
        generateData(realPosition);
        adapter = new CoverFlowAdapter(ProductCoverFlowActivity.this, 0, data);

        initUI(title);

        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(adapter);
        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                if(productItemList.size() <= CACHED_IMAGE_LIMIT) {
                    productItem = productItemList.get(position);
                    Log.i("mCoverFlow", "position in productItemList = " + position);
                } else {    //cached mode enabled
                    realPosition = (realPosition - 2 + position + positionCount) % positionCount;
                    productItem = productItemList.get(realPosition);
                    Log.i("mCoverFlow", "realPosition in productItemList = " + realPosition);
                    generateData(realPosition);
                    mCoverFlow.setAdapter(adapter);
                    mCoverFlow.clearCache();
                }
                mTitle.setText(productItem.getTitle());
                ProductItem item = mDataBaseManager.queryByUrl(productItem.getImageUrl());
                if (item != null) {
                    favorite.setImageDrawable(getResources().getDrawable(R.mipmap.favorite_btn));
                } else {
                    favorite.setImageDrawable(getResources().getDrawable(R.mipmap.unfavorite_btn));
                }
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productItem = data.get(position);
                Intent intent = new Intent(ProductCoverFlowActivity.this, ProductImageViewActivity.class);
                intent.putExtra("productItem", productItem);
                startActivity(intent);
            }
        });

        detail = (ImageView) findViewById(R.id.detail);
        detail.setVisibility(View.VISIBLE);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductCoverFlowActivity.this, ProductDetailActivity.class);
                intent.putExtra("productItem", productItem);
                startActivity(intent);
            }
        });

        share = (ImageView) findViewById(R.id.share);
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                Bitmap bitmap = imageLoader.loadImageSync(productItem.getImageUrl());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri uri = getImageUri(getApplicationContext(), bitmap);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });

        favorite = (ImageView) findViewById(R.id.favorite);
        favorite.setVisibility(View.VISIBLE);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductItem item = mDataBaseManager.queryByUrl(productItem.getImageUrl());

                if (item != null) {
                    mDataBaseManager.delete(productItem.getImageUrl());
                    favorite.setImageDrawable(getResources().getDrawable(R.mipmap.unfavorite_btn));
                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                } else {
                    mDataBaseManager.insert(productItem.getTitle(), productItem.getImageUrl());
                    favorite.setImageDrawable(getResources().getDrawable(R.mipmap.favorite_btn));
                    Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, productItem.getTitle(), null);
        return Uri.parse(path);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDataBaseManager.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseManager.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Bug fix, initPosition==0
        if(productItemList.size() <= CACHED_IMAGE_LIMIT) {
            if (initPosition == 0) {
                mCoverFlow.scrollToPosition(initPosition + positionCount);
            } else {
                mCoverFlow.scrollToPosition(initPosition);
            }
        }
    }

    private void generateData(int index) {
        if (productItemList.size() <= CACHED_IMAGE_LIMIT) {
            data = productItemList;
        } else {    //cached mode enabled
            data.clear();
            int tmp = index + productItemList.size();
            data.add(productItemList.get((tmp - 2) % productItemList.size()));
            data.add(productItemList.get((tmp - 1) % productItemList.size()));
            data.add(productItemList.get(tmp % productItemList.size()));
            data.add(productItemList.get((tmp + 1) % productItemList.size()));
            data.add(productItemList.get((tmp + 2) % productItemList.size()));
        }
    }
}
