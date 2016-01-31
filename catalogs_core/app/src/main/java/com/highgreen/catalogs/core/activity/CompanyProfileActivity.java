package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreen.catalogs.core.R;

/**
 * Created by tihong on 16-1-24.
 */
public class CompanyProfileActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.company_profile_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

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
        left_back_title.setText(R.string.app_name);

        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setVisibility(View.VISIBLE);
        middle_text_title.setText(R.string.cn_company_profile);
    }
}
