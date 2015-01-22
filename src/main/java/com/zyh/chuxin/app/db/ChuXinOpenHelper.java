package com.zyh.chuxin.app.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作帮助类
 * Created by zhyh on 2015/1/22.
 */
public class ChuXinOpenHelper extends SQLiteOpenHelper {

    // Province表建表语句
    public static final String CREATE_PROVINCE = "create table province (id integer primary key " +
            "autoincrement, province_name text, province_code text)";

    // city表建表语句
    public static final String CREATE_CITY = "create table city (id integer primary key " +
            "autoincrement, city_name text, city_code text, province_id integer)";

    // country表建表语句
    public static final String CREATE_COUNTRY = "create table country (id integer primary key " +
            "autoincrement, country_name text, country_code text, city_id integer)";

    public ChuXinOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    public ChuXinOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
