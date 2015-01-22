package com.zyh.chuxin.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.model.Country;
import com.zyh.chuxin.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类
 * Created by zhyh on 2015/1/22.
 */
public class ChuXinWeatherDB {

    // 数据库名称
    public static final String DB_NAME = "chuxin_weather";

    // 数据库版本
    public static final int DB_VERSION = 1;

    private static ChuXinWeatherDB weatherDB;

    private SQLiteDatabase db;

    private ChuXinWeatherDB(Context context) {
        ChuXinOpenHelper openHelper = new ChuXinOpenHelper(context, DB_NAME, null, DB_VERSION);
        db = openHelper.getWritableDatabase();
    }

    public static ChuXinWeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new ChuXinWeatherDB(context);
        }
        return weatherDB;
    }

    /**
     * 将Province保存到数据库
     * @param province 要保存的Province实体
     */
    public void saveProvince(Province province) {
        if (null != province) {
            ContentValues values = new ContentValues();
            values.put("name", province.getName());
            values.put("code", province.getCode());
            db.insert("province", null, values);
        }
    }

    /**
     * 加载省级行政单位
     * @return 所有省级行政单位
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<Province>();

        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }

        return provinceList;
    }

    /**
     * 保存市级行政单位
     * @param city City实体
     */
    public void saveCity(City city) {
        if (null != city) {
            ContentValues values = new ContentValues();
            values.put("name", city.getName());
            values.put("code", city.getCode());
            values.put("province_id", city.getProvinceId());
            db.insert("city", null, values);
        }
    }

    /**
     * 加载指定省级行政单位下的所有市级行政单位
     * @param provinceId 省级行政单位ID
     * @return 给定省级行政单位下的所有市级行政单位
     */
    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<City>();

        Cursor cursor = db.query("city", null, "province_id=?", new String[]{String.valueOf
                (provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("code")));
                city.setProvinceId(provinceId);
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        return cityList;
    }

    /**
     * 保存县级行政单位
     * @param country Country实体
     */
    public void saveCountry(Country country) {
        if (null != country) {
            ContentValues values = new ContentValues();
            values.put("name", country.getName());
            values.put("code", country.getCode());
            values.put("city_id", country.getCityId());
            db.insert("country", null, values);
        }
    }

    /**
     * 加载指定市级行政单位下的所有县级行政单位
     * @param cityId 市级行政单位ID
     * @return 给定市级行政单位下的所有县级行政单位
     */
    public List<Country> loadCountries(int cityId) {
        List<Country> countryList = new ArrayList<Country>();

        Cursor cursor = db.query("country", null, "city_id=?", new String[]{String.valueOf
                (cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Country country = new Country();
            country.setId(cursor.getInt(cursor.getColumnIndex("id")));
            country.setName(cursor.getString(cursor.getColumnIndex("name")));
            country.setCode(cursor.getString(cursor.getColumnIndex("code")));
            country.setCityId(cityId);
            countryList.add(country);
        }

        return countryList;
    }
}
