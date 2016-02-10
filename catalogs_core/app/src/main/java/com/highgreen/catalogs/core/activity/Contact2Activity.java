package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.adapter.PersonAdapter;
import com.highgreen.catalogs.core.bean.ContactInfo;
import com.highgreen.catalogs.core.utils.NetworkUtils;

/**
 * Created by tihong on 16-1-24.
 */
public class Contact2Activity extends Activity {

    private final static String TAG = "Contact2Activity";
    private ListView contact_listView;
    private TextView company_website;
    private TextView company_address;
    private ContactInfo contactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.contact_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        if (NetworkUtils.isNetworkAvailable(Contact2Activity.this)) {
            new GetContactTask().execute(MainApplication.CONTACT_PATH);
        }else{
            NetworkUtils.setNetworkMethod(Contact2Activity.this);
        }
        initUI();
        contact_listView = (ListView) findViewById(R.id.contact_list);
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
        middle_text_title.setText(R.string.cn_contact_information);
    }

    private class GetContactTask extends AsyncTask<String, Void, ContactInfo> {

        @Override
        protected ContactInfo doInBackground(String... params) {
            try {
                String contact = MainApplication.getUpYun().readFile(params[0]);
                Log.i(TAG, contact);
                contactInfo = new Gson().fromJson(contact, ContactInfo.class);
                if (contactInfo == null){
                    Log.i(TAG, "contactInfo is null");
                    return null;
                }
                Log.i(TAG,contactInfo.getPerson().size()+"");
            }catch (Exception e){
                return null;
            }
            return contactInfo;
        }

        @Override
        protected void onPostExecute(ContactInfo contactInfo) {
            if (contactInfo != null) {
                company_website = (TextView) findViewById(R.id.company_website);
                company_website.setText(contactInfo.getWebsite());
                company_address = (TextView) findViewById(R.id.company_address);
                company_address.setText(contactInfo.getAddress());
                contact_listView = (ListView) findViewById(R.id.contact_list);
                contact_listView.setAdapter(new PersonAdapter(Contact2Activity.this, 0, contactInfo.getPerson()));
            } else {
                Toast.makeText(Contact2Activity.this, "Load contact.json failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // AnimateFirstDisplayListener.displayedImages.clear();
        super.onBackPressed();
    }

}
