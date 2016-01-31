package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.highgreen.catalogs.core.MainApplication;
import com.meiya.simon.R;
import com.highgreen.catalogs.core.adapter.PersonAdapter;
import com.highgreen.catalogs.core.bean.ContactInfo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Created by tihong on 16-1-24.
 */
public class ContactActivity extends Activity {

    private final static String CONTACT_PATH = "Contact/contact.json";
    private ImageView back_arrow;
    private TextView middle_text_title;
    private TextView left_back_title;
    private ListView contanct_listView;
    private TextView company_website;
    private TextView company_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.contact_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        initUI();
        contanct_listView = (ListView) findViewById(R.id.contact_list);
        String contact_path = MainApplication.ROOTPATH + CONTACT_PATH;
        new GetContactTask().execute(contact_path);
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
        middle_text_title.setText(R.string.cn_contact_information);
    }

    private class GetContactTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
//            result = MainApplication.getUpYun().readFile(params[0]);
//            Gson gson = new Gson();
//            UpYunHttpResponse response = gson.fromJson(result, UpYunHttpResponse.class);
//            if (response != null && response.getCode() == 40400001) {
//                return null;
//            }

            OkHttpClient client = new OkHttpClient();

            String url = "http://catalog-assets.b0.upaiyun.com/006_test/Contact/contact.json";
            Request request = new Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String json) {
            Gson gson = new Gson();
            ContactInfo contactInfo = gson.fromJson(json, ContactInfo.class);
            if (contactInfo != null) {
                company_website = (TextView) findViewById(R.id.company_website);
                company_website.setText(contactInfo.getWebsite());
                company_address = (TextView) findViewById(R.id.company_address);
                company_address.setText(contactInfo.getAddress());
                contanct_listView = (ListView) findViewById(R.id.contact_list);
                contanct_listView.setAdapter(new PersonAdapter(ContactActivity.this, 0, contactInfo.getPersonInfos()));
            } else {
                Toast.makeText(ContactActivity.this, "Load contact.json failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // AnimateFirstDisplayListener.displayedImages.clear();
        super.onBackPressed();
    }

}
