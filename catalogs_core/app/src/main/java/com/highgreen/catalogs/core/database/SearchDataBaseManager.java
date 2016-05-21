package com.highgreen.catalogs.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.highgreen.catalogs.core.bean.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruantihong on 1/28/16.
 */
public class SearchDataBaseManager {
    private SQLiteDatabase mSQLiteDatabase;

    final String SQL_INSERT = "insert into search_table values(null,?,?)";
    final String SQL_QUERY_ALL = "select * from search_table";

    public SearchDataBaseManager(Context context) {
        mSQLiteDatabase = SearchDataBaseHelper.getInstance(context).getWritableDatabase();
    }

    public void insert(String title, String image_url) {
        mSQLiteDatabase.beginTransaction();
        try {
            mSQLiteDatabase.execSQL(SQL_INSERT, new String[]{title, image_url});
            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    public void delete(String image_url) {
        mSQLiteDatabase.delete("search_table", "image_url = ?", new String[]{image_url});
    }

    public HashMap<String, String> query() {
        HashMap<String, String> productItems = new HashMap<String, String>();
        Cursor c = mSQLiteDatabase.rawQuery(SQL_QUERY_ALL, null);
        while (c.moveToNext()) {
            productItems.put(c.getString(c.getColumnIndex("image_url")),
                    c.getString(c.getColumnIndex("title")));
        }
        c.close();
        return productItems;

    }

    public List<ProductItem> queryByTitle(String title) {
        ArrayList<ProductItem> productItems = new ArrayList<ProductItem>();
        Cursor c = mSQLiteDatabase.query("search_table", new String[]{"title", "image_url"}, "title like ?",
                new String[]{title+"%"}, null, null, null);
        while (c.moveToNext()) {
            ProductItem productItem = new ProductItem();
            productItem.setTitle(c.getString(c.getColumnIndex("title")));
            productItem.setImageUrl(c.getString(c.getColumnIndex("image_url")));
            productItems.add(productItem);
        }
        c.close();
        return productItems;
    }

    public void close() {
        mSQLiteDatabase.close();
    }

}
