package com.highgreen.catalogs.core.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruantihong on 1/19/16.
 */
public class UserSharedPreference {
    private static final String USER_PREFERENCE = "user_preference";
    private static final String PASSWORD = "password";
    private static final String LOGINONCE = "login_once";

    public static void updatePassword(Context context, String password) {
        SharedPreferences userPreferences = context.getSharedPreferences(USER_PREFERENCE, 0);
        if (password != null) {
            userPreferences.edit().putString(PASSWORD, password).commit();
        }
    }

    public static String getPassword(Context context) {
        SharedPreferences userPreferences = context.getSharedPreferences(USER_PREFERENCE, 0);
        return userPreferences.getString(PASSWORD, null);

    }

    public static void updateLoginOnce(Context context, boolean flag) {
        SharedPreferences userPreferences = context.getSharedPreferences(USER_PREFERENCE, 0);
        userPreferences.edit().putBoolean(LOGINONCE, flag).commit();
    }

    public static boolean getLoginOnce(Context context) {
        SharedPreferences userPreferences = context.getSharedPreferences(USER_PREFERENCE, 0);
        return userPreferences.getBoolean(LOGINONCE, false);
    }

}
