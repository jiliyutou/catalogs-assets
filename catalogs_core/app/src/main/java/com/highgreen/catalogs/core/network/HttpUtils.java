package com.highgreen.catalogs.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tihong on 16-2-7.
 */
public class HttpUtils {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null){
            return false;
        }else{
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null){
                return false;
            }
            else{
                if (info.isAvailable()){
                    return true;
                }
            }
        }
        return false;
    }
}
