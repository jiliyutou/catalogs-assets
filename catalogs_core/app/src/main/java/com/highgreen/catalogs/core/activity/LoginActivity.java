package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.meiya.simon.R;
import com.highgreen.catalogs.core.preference.UserSharedPreference;

/**
 * Created by ruantihong on 1/19/16.
 */
public class LoginActivity extends Activity {

    private TextView loginTextView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        mContext = this;

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
                String password = passwordEditText.getText().toString().trim();
                if (!UserSharedPreference.getPassword(mContext).equals(password)) {
                    findViewById(R.id.errorTextView).setVisibility(View.VISIBLE);
                }else{
                    finish();
                }
            }
        });
    }
}