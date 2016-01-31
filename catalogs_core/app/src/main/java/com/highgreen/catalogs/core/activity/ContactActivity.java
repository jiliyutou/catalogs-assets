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
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.adapter.PersonAdapter;
import com.highgreen.catalogs.core.bean.ContactInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tihong on 16-1-24.
 */
public class ContactActivity extends Activity {

    private final static String CONTACT_PATH = "contact/contact.json";
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
        new GetContactTask().execute(CONTACT_PATH);
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
            result = getJson(params[0]);
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
                contanct_listView.setAdapter(new PersonAdapter(ContactActivity.this, 0, contactInfo.getPerson()));
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

    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
