package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.preference.UserSharedPreference;

public class CatalogsActivity extends Activity {

    private Button mLanguageButton;
    private Context mContext;
    private TextView mProductListTextView;
    private TextView mNewProductListTextView;
    private TextView m3DProductListTextView;
    private TextView mCompanyProfileTextView;
    private TextView mContactInformationTextView;
    private TextView mFavoritesTextView;
    private TextView mProductCertificateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.catalogs_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        mContext = this;
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserSharedPreference.getLoginOnce(mContext)) {
            Intent intent = new Intent(CatalogsActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initUI() {

        mProductListTextView = (TextView) findViewById(R.id.product_list);
        mNewProductListTextView = (TextView) findViewById(R.id.new_product_list);
        m3DProductListTextView = (TextView) findViewById(R.id.threed_product_list);
        mCompanyProfileTextView = (TextView) findViewById(R.id.company_profile);
        mContactInformationTextView = (TextView) findViewById(R.id.contact_information);
        mFavoritesTextView = (TextView) findViewById(R.id.favorites);
        mProductCertificateTextView = (TextView) findViewById(R.id.product_certificate);

        mProductListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent category = new Intent(CatalogsActivity.this, CategoryActivity.class);
                startActivity(category);
            }
        });

        mNewProductListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newProduct = new Intent(CatalogsActivity.this, NewProductGridActivity.class);
                startActivity(newProduct);
            }
        });

        m3DProductListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"not supported",Toast.LENGTH_SHORT).show();
                //Intent threeDProduct = new Intent(CatalogsActivity.this, ThreeDProductGridActivity.class);
                //startActivity(threeDProduct);
            }
        });

        mCompanyProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent companyProfile = new Intent(CatalogsActivity.this, CompanyProfileActivity.class);
                startActivity(companyProfile);
            }
        });

        mContactInformationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact = new Intent(CatalogsActivity.this, Contact2Activity.class);
                startActivity(contact);
            }
        });

        mFavoritesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorites = new Intent(CatalogsActivity.this, FavoritesGridActivity.class);
                startActivity(favorites);

            }
        });

        mProductCertificateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent certificate = new Intent(CatalogsActivity.this, CertificateActivity.class);
                startActivity(certificate);
            }
        });

        mLanguageButton = (Button) findViewById(R.id.languageButton);
        mLanguageButton.setVisibility(View.VISIBLE);
        mLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) mLanguageButton.getText();
                if (getResources().getString(R.string.cn_language).trim().equals(text.trim())) {
                    mLanguageButton.setText(R.string.en_language);
                    mProductListTextView.setText(R.string.en_product_list);
                    mNewProductListTextView.setText(R.string.en_new_product_list);
                    m3DProductListTextView.setText(R.string.en_threed_product_list);
                    mCompanyProfileTextView.setText(R.string.en_company_profile);
                    mContactInformationTextView.setText(R.string.en_contact_information);
                    mFavoritesTextView.setText(R.string.en_favorites);
                    mProductCertificateTextView.setText(R.string.en_product_certificate);
                } else {
                    mLanguageButton.setText(R.string.cn_language);
                    mProductListTextView.setText(R.string.cn_product_list);
                    mNewProductListTextView.setText(R.string.cn_new_product_list);
                    m3DProductListTextView.setText(R.string.cn_threed_product_list);
                    mCompanyProfileTextView.setText(R.string.cn_company_profile);
                    mContactInformationTextView.setText(R.string.cn_contact_information);
                    mFavoritesTextView.setText(R.string.cn_favorites);
                    mProductCertificateTextView.setText(R.string.cn_product_certificate);
                }
            }
        });
    }
}
