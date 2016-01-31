package com.highgreen.catalogs.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.highgreen.catalogs.core.bean.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruantihong on 1/28/16.
 */
public class DataBaseManager {
    private SQLiteDatabase mSQLiteDatabase;

    final String SQL_INSERT = "insert into favorites_table values(null,?,?)";
    final String SQL_QUERY_ALL = "select * from favorites_table";

    public DataBaseManager(Context context){
        mSQLiteDatabase = DataBaseHelper.getInstance(context).getWritableDatabase();
    }

    public void insert(String title, String image_url){
        mSQLiteDatabase.beginTransaction();
        try{
            mSQLiteDatabase.execSQL(SQL_INSERT,new String[]{title,image_url});
            mSQLiteDatabase.setTransactionSuccessful();
        }finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    public void delete(String image_url){
        mSQLiteDatabase.delete("favorites_table", "image_url = ?", new String[]{image_url});
    }

    public List<ProductItem> query(){
        ArrayList<ProductItem> productItems = new ArrayList<ProductItem>();
        Cursor c = mSQLiteDatabase.rawQuery(SQL_QUERY_ALL, null);
        while (c.moveToNext()) {
            ProductItem productItem = new ProductItem();
            productItem.setTitle(c.getString(c.getColumnIndex("title")));
            productItem.setImageUrl(c.getString(c.getColumnIndex("image_url")));
            productItems.add(productItem);
        }
        c.close();
        return productItems;

    }

    public ProductItem queryByUrl(String image_url){
        Cursor c = mSQLiteDatabase.query("favorites_table", new String[]{"title","image_url"},"image_url=?",
                new String[]{image_url},null,null,null);
        while (c.moveToNext()) {
           return new ProductItem(c.getString(0),c.getString(1));
        }
        return null;
    }

    public void close(){
        mSQLiteDatabase.close();
    }

}
