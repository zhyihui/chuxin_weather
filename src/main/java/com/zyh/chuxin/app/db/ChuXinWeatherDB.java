package com.zyh.chuxin.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
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
    public static final String DB_NAME = "city.db";

    // 数据库版本
    public static final int DB_VERSION = 1;

    private static ChuXinWeatherDB weatherDB;

    private String dbFilePath;
    private SQLiteDatabase db;

    private ChuXinWeatherDB(Context context, String dbFilePath) {
        if (TextUtils.isEmpty(dbFilePath)) {
            ChuXinOpenHelper dbHelper = new ChuXinOpenHelper(context, DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
        } else {
            db = context.openOrCreateDatabase(dbFilePath, Context.MODE_PRIVATE, null);
        }
    }

    public static ChuXinWeatherDB getInstance(Context context, String dbFilePath) {
        if (weatherDB == null || (dbFilePath != null && !dbFilePath.equals(weatherDB.dbFilePath))) {
            weatherDB = new ChuXinWeatherDB(context, dbFilePath);
            weatherDB.dbFilePath = dbFilePath;
        }
        return weatherDB;
    }

    public void clear() {
        db.execSQL("delete from province;");
        db.execSQL("delete from city;");
        db.execSQL("delete from country;");
    }

    /**
     * 将Province保存到数据库
     *
     * @param province 要保存的Province实体
     */
    public void saveProvince(Province province) {
        if (null != province) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getName());
            values.put("province_code", province.getCode());
            db.insert("province", null, values);
        }
    }

    /**
     * 加载省级行政单位
     *
     * @return 所有省级行政单位
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<Province>();

        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }

        return provinceList;
    }

    /**
     * 保存市级行政单位
     *
     * @param city City实体
     */
    public void saveCity(City city) {
        if (null != city) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getName());
            values.put("city_code", city.getCode());
            values.put("province_code", city.getProvinceCode());
            db.insert("city", null, values);
        }
    }

    /**
     * 加载指定省级行政单位下的所有市级行政单位
     *
     * @return 给定省级行政单位下的所有市级行政单位
     */
    public List<City> loadCities(String provinceCode) {
        List<City> cityList = new ArrayList<City>();

        Cursor cursor = db.query("city", null, "province_code=?", new String[]{provinceCode},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceCode(provinceCode);
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        return cityList;
    }

    /**
     * 保存县级行政单位
     *
     * @param country Country实体
     */
    public void saveCountry(Country country) {
        if (null != country) {
            ContentValues values = new ContentValues();
            values.put("country_name", country.getName());
            values.put("country_code", country.getCode());
            values.put("city_code", country.getCityCode());
            db.insert("country", null, values);
        }
    }

    /**
     * 加载指定市级行政单位下的所有县级行政单位
     *
     * @return 给定市级行政单位下的所有县级行政单位
     */
    public List<Country> loadCountries(String cityCode) {
        List<Country> countryList = new ArrayList<Country>();

        Cursor cursor = db.query("country", null, "city_code=?", new String[]{cityCode}, null,
                null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityCode(cityCode);
                countryList.add(country);
            } while (cursor.moveToNext());
        }

        return countryList;
    }

    public City loadCity(String cityName) {
        Cursor cursor = db.query("city", null, "city_name=?", new String[]{cityName}, null, null,
                null);
        if (cursor.moveToFirst()) {
            City city = new City();
            city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            return city;
        }
        return null;
    }
}
