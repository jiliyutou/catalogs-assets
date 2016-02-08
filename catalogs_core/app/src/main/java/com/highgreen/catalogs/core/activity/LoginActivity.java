package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.LoginCodeItem;
import com.highgreen.catalogs.core.preference.UserSharedPreference;

import java.util.List;

/**
 * Created by ruantihong on 1/19/16.
 */
public class LoginActivity extends Activity {
    private final static String TAG = "LoginActivity";

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
                if (!(UserSharedPreference.getPassword(mContext).equals(password)||isCodeValid(password))) {
                    Toast.makeText(getApplicationContext(),R.string.login_error_hint,Toast.LENGTH_SHORT).show();
                }else{
                    UserSharedPreference.updateLoginOnce(mContext,true);
                    finish();
                }
            }
        });
    }

    private boolean isCodeValid(String password) {
        List<LoginCodeItem> loginCodeItemList = MainApplication.getLoginCodes().getLogin_codes();
        for (LoginCodeItem code : loginCodeItemList){
            if (code.getCode().equals(password.trim())){
                code.setValid(false);
                new ValidLoginCodeTask().execute();
                return true;
            }
        }
        return false;
    }

    private class ValidLoginCodeTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            String json = gson.toJson(MainApplication.getLoginCodes());
            Log.i(TAG,json);
            MainApplication.getUpYun().writeFile(MainApplication.LOGIN_CODES_PATH,json.getBytes());
            Log.i(TAG, "ValidLoginCodeTask is succeed");
            return null;
        }
    }
}