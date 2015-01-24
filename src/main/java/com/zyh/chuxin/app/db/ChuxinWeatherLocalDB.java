package com.zyh.chuxin.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zyh.chuxin.app.model.CityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhyh on 2015/1/24.
 */
public class ChuxinWeatherLocalDB {
    public static final String DB_NAME = "city.db";
    private SQLiteDatabase database;

    private static ChuxinWeatherLocalDB instance;

    private ChuxinWeatherLocalDB(Context context, String dbPath) {
        database = context.openOrCreateDatabase(dbPath, Context.MODE_PRIVATE, null);
    }

    public static ChuxinWeatherLocalDB getInstance(Context context, String dbPath) {
        if (instance == null) {
            instance = new ChuxinWeatherLocalDB(context, dbPath);
        }
        return instance;
    }

    public Cursor loadAllCursor() {
        return database.query("city", null, null, null, null, null, "firstpy");
    }

    public List<CityModel> loadAll() {
        List<CityModel> cityModelList = new ArrayList<CityModel>();
        Cursor cursor = database.query("city", null, null, null, null, null, "firstpy");
        if (cursor.moveToFirst()) {
            do {
                CityModel cityModel = new CityModel();
                cityModel.setName(cursor.getString(cursor.getColumnIndex("city")));
                cityModel.setProvinceName(cursor.getString(cursor.getColumnIndex("province")));
                cityModel.setCode(cursor.getString(cursor.getColumnIndex("number")));
                cityModel.setAllPinYin(cursor.getString(cursor.getColumnIndex("allpy")));
                cityModel.setAllFirstPinYin(cursor.getString(cursor.getColumnIndex("allfirstpy")));
                cityModel.setFirstPinYin(cursor.getString(cursor.getColumnIndex("firstpy")));
                cityModelList.add(cityModel);
            } while (cursor.moveToNext());
        }
        return cityModelList;
    }

    public CityModel loadCityByName(String name) {
        Cursor cursor = database.query("city", null, "city=?", new String[]{name}, null, null,
                null);
        if (cursor.moveToFirst()) {
            CityModel cityModel = new CityModel();
            cityModel.setName(cursor.getString(cursor.getColumnIndex("city")));
            cityModel.setCode(cursor.getString(cursor.getColumnIndex("number")));
            return cityModel;
        }
        return null;
    }
}
