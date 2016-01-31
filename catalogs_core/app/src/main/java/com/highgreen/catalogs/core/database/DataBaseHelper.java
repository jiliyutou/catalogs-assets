package com.highgreen.catalogs.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruantihong on 1/28/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String SQL_CREATE_TABLE = "create table favorites_table (" +
            "_id integer primary key autoincrement, " +
            "title varchar(50),"+
            "image_url varchar(200))";
    private final static String DATABASE_NAME="favorite";
    private final static int DATABASE_VERSION = 1;

    private static DataBaseHelper mInstance = null;

    public static DataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context,DATABASE_NAME,DATABASE_VERSION);
        }
        return mInstance;
    }
    private DataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
