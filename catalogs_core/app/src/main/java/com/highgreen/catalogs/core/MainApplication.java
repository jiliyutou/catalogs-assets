package com.highgreen.catalogs.core;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.highgreen.catalogs.core.bean.ContactInfo;
import com.highgreen.catalogs.core.bean.LoginCodes;
import com.highgreen.catalogs.core.upyun.UpYun;
import com.highgreen.catalogs.core.preference.UserSharedPreference;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * Created by ruantihong on 1/19/16.
 */
public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static final String LOGIN_PWD = "androidadmin";

    private final static String SERVER_NAME = "catalog-assets";
    private final static String OPERATOR_NAME = "androidadmin";
    private final static String OPERATOR_PASSWORD = "androidadmin";
    private final static String DOMAIN = ".b0.upaiyun.com";
    private final static String LOGIN_CODES = "login_codes.json";
    private final static String CONTACT = "contact/";
    private final static String NEW_PRODUCTS =  "new_products/";
    private final static String THREED_PRODUCTS = "threed_products/";

    public final static String RESOURCE_ROOT = "/004_meiya/";
    public final static String LOGIN_CODES_PATH = RESOURCE_ROOT + LOGIN_CODES;
    public final static String NEW_PRODUCTS_PATH = RESOURCE_ROOT + NEW_PRODUCTS;
    public final static String THREED_PRODUCTS_PATH = RESOURCE_ROOT + THREED_PRODUCTS;
    public final static String CONTACT_PATH = RESOURCE_ROOT + CONTACT;
    public final static String UPYUN_REQUEST_HEADER = "http://" + SERVER_NAME + DOMAIN + RESOURCE_ROOT;
    public final static String NEW_PRODUCTS_UPYUN_URL = UPYUN_REQUEST_HEADER + NEW_PRODUCTS;
    public final static String THREED_PRODUCTS_UPYUN_URL = UPYUN_REQUEST_HEADER + THREED_PRODUCTS;
    public final static String CONTACT_UPYUN_URL = UPYUN_REQUEST_HEADER + CONTACT;

    public static int screen_width;
    public static int screen_height;

    private static Context mContext = null;
    private static UpYun upYun = null;

    public static LoginCodes loginCodes;
    public static ContactInfo contactInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.i(TAG, UserSharedPreference.getPassword(mContext) + " " + UserSharedPreference.getLoginOnce(mContext));
        if (UserSharedPreference.getPassword(mContext) == null) {
            UserSharedPreference.updatePassword(mContext, LOGIN_PWD);
            UserSharedPreference.updateLoginOnce(mContext, false);
        }
        Log.i(TAG, "after " + UserSharedPreference.getPassword(mContext) + " " + UserSharedPreference.getLoginOnce(mContext));

        initImageLoader(mContext);
        initUpYun();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        screen_width = wm.getDefaultDisplay().getWidth();
        screen_height = wm.getDefaultDisplay().getHeight();

        Log.i(TAG, "screen_width : " + screen_width + ", screen_height : " + screen_height);

        new GetLoginCodesTask().execute(LOGIN_CODES_PATH);
        new GetContactTask().execute(CONTACT_PATH);

    }

    private void initUpYun() {
        upYun = new UpYun(SERVER_NAME, OPERATOR_NAME, OPERATOR_PASSWORD);
        upYun.setDebug(true);
        upYun.setTimeout(60);
        /**
         * 选择最优的接入点
         * 根据国内的网络情况，又拍云存储API目前提供了电信、联通网通、移动铁通三个接入点。可以通过setApiDomain()方法进行设置，默认将根据网络条件自动选择接入点。
         * 接入点有四个值可选：
         * UpYun.ED_AUTO    //根据网络条件自动选择接入点
         * UpYun.ED_TELECOM //电信接入点
         * UpYun.ED_CNC     //联通网通接入点
         * UpYun.ED_CTT     //移动铁通接入点
         */
        upYun.setApiDomain(UpYun.ED_AUTO);
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
                .writeDebugLogs()//Remove for release app
                .build();

        ImageLoader.getInstance().init(config);
    }

    public static UpYun getUpYun() {
        return upYun;
    }

    private class GetLoginCodesTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            try {
                String login_codes = MainApplication.getUpYun().readFile(LOGIN_CODES_PATH);
                Log.i(TAG,login_codes);
                loginCodes = new Gson().fromJson(login_codes, LoginCodes.class);
                if (login_codes == null){
                    Log.i(TAG, "loginCodes is null");
                    return null;
                }
                Log.i(TAG,loginCodes.getLogin_codes().size()+"");
            }catch (Exception e){
                return null;
            }
            return null;
        }
    }

    private class GetContactTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            try {
                String contact = MainApplication.getUpYun().readFile(CONTACT_PATH + "contact.json");
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
            return null;
        }
    }

    public static LoginCodes getLoginCodes() {
        return loginCodes;
    }

    public static ContactInfo getContactInfo() {
        return contactInfo;
    }
}
