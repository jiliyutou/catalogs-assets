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
import com.highgreen.catalogs.core.utils.NetworkUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by ruantihong on 1/22/16.
 */
public class FavoritesCoverFlowActivity extends FragmentActivity {

    private FeatureCoverFlow mCoverFlow;
    private ProductItem productItem;
    private ArrayList<ProductItem> productItemList;

    private ImageView detail;
    private ImageView share;
    private ImageView favorite;
    private DataBaseManager mDataBaseManager;
    private TextSwitcher mTitle;
    private int initPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_cover_flow_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        mDataBaseManager = new DataBaseManager(getApplicationContext());

        mTitle = (TextSwitcher)findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(FavoritesCoverFlowActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.coverflow_title_item, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        productItem = (ProductItem) getIntent().getSerializableExtra("productItem");
        productItemList = (ArrayList<ProductItem>) getIntent().getSerializableExtra("productList");
        initPosition = getIntent().getExtras().getInt("initPosition");

        initUI();
        mCoverFlow.setAdapter(new CoverFlowAdapter(FavoritesCoverFlowActivity.this, 0, productItemList));

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                productItem = productItemList.get(position);
                mTitle.setText(productItem.getTitle());
                Log.i("FavoritesCover", "position = " + position);
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
                productItem = productItemList.get(position);
                Intent intent = new Intent(FavoritesCoverFlowActivity.this, ProductImageViewActivity.class);
                intent.putExtra("productItem", productItem);
                startActivity(intent);
            }
        });

        detail = (ImageView) findViewById(R.id.detail);
        detail.setVisibility(View.VISIBLE);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesCoverFlowActivity.this, ProductDetailActivity.class);
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

        if (!NetworkUtils.isNetworkAvailable(FavoritesCoverFlowActivity.this)) {
            NetworkUtils.setNetworkMethod(FavoritesCoverFlowActivity.this);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, productItem.getTitle(), null);
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
        left_back_title.setText("Favorites");

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Bug fix, initPosition==0
        mCoverFlow.scrollToPosition(initPosition);
//        int index = 0;
//        for (ProductItem item : productItemList) {
//            if (item.getImageUrl().equals(productItem.getImageUrl())) {
//                Log.i("FavoritesCover", " index = " + index);
//                mCoverFlow.setSelection(index);
//                break;
//            } else {
//                index++;
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mDataBaseManager.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseManager.close();
    }
}
