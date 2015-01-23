package com.zyh.chuxin.app.util;

import android.content.Context;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.model.Country;
import com.zyh.chuxin.app.model.Province;

import java.util.List;

/**
 * 中国天气网中国城市数据抓取类
 * Created by zhyh on 2015/1/23.
 */
public class CityDataFetcher {

    private static final String CITY_BASE_URL = "http://www.weather.com.cn/data/list3/city";
    private static final String URL_SUFFIX = ".xml";

    private OnCityDataFetchedListener listener;
    private Context mContext;
    private ChuXinWeatherDB weatherDB;

    public CityDataFetcher(Context context, OnCityDataFetchedListener listener) {
        this.listener = listener;
        mContext = context;
        weatherDB = ChuXinWeatherDB.getInstance(context);
    }

    public void fetchCityData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchInThread();
            }
        }).start();
    }

    private void fetchInThread() {
        String fetchProvinceUrl = CITY_BASE_URL + URL_SUFFIX;
        List<Province> provinceList = Utility.parseProvinceResponse(HttpUtil.sendHttpRequest
                (fetchProvinceUrl));
        for (Province province : provinceList) {
            weatherDB.saveProvince(province);
            fetchCity(province.getCode());
        }

        if (listener != null) {
            listener.onCityDataFetched();
        }
    }

    private void fetchCity(String provinceCode) {
        String fetchCityUrl = CITY_BASE_URL + provinceCode + URL_SUFFIX;
        List<City> cityList = Utility.parseCityResponse(HttpUtil.sendHttpRequest(fetchCityUrl));
        for (City city : cityList) {
            city.setProvinceCode(provinceCode);
            weatherDB.saveCity(city);
            fetchCountry(city.getCode());
        }
    }

    private void fetchCountry(String cityCode) {
        String fetchCountryUrl = CITY_BASE_URL + cityCode + URL_SUFFIX;
        List<Country> countryList = Utility.parseCountryResponse(HttpUtil.sendHttpRequest
                (fetchCountryUrl));
        for (Country country : countryList) {
            country.setCityCode(cityCode);
            weatherDB.saveCountry(country);
        }
    }

    public interface OnCityDataFetchedListener {
        void onCityDataFetched();
    }
}
