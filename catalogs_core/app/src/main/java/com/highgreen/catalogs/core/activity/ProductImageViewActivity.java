package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by tihong on 16-1-29.
 */
public class ProductImageViewActivity extends Activity{

    private TextView share;
    private TextView favorite;

    private PhotoViewAttacher mPhotoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_imageview_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        ProductItem productItem = (ProductItem) getIntent().getSerializableExtra("productItem");
        ImageView imageView = (ImageView) findViewById(R.id.favoriteProduct);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(productItem.getImageUrl());
        imageView.setImageBitmap(bitmap);
        mPhotoViewAttacher = new PhotoViewAttacher(imageView);
        initUI();

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
        middle_text_title.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoViewAttacher.cleanup();
    }
}
