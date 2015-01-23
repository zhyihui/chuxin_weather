package com.zyh.chuxin.app.util;

import android.text.TextUtils;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.model.Country;
import com.zyh.chuxin.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhyh on 2015/1/22.
 */
public class Utility {

    /**
     * 解析省级行政单位响应数据
     *
     * @param resp HTTP请求响应数据
     * @return 是否解析并入库成功
     */
    public synchronized static List<Province> parseProvinceResponse(String resp) {
        if (TextUtils.isEmpty(resp)) return null;

        String[] allProvinces = resp.split(",");
        if (allProvinces.length < 1) return null;

        List<Province> provinceList = new ArrayList<Province>();
        for (String provinceStr : allProvinces) {
            String[] pArray = provinceStr.split("\\|");
            Province province = new Province();
            province.setCode(pArray[0]);
            province.setName(pArray[1]);
            provinceList.add(province);
        }

        return provinceList;
    }

    /**
     * 解析市级行政单位HTTP请求响应数据
     *
     * @param resp HTTP请求响应数据
     * @return 是否解析并入库成功
     */
    public synchronized static List<City> parseCityResponse(String resp) {
        if (TextUtils.isEmpty(resp)) return null;

        String[] allCities = resp.split(",");
        if (allCities.length < 1) return null;

        List<City> cityList = new ArrayList<City>();
        for (String cityStr : allCities) {
            String[] cArray = cityStr.split("\\|");
            City city = new City();
            city.setCode(cArray[0]);
            city.setName(cArray[1]);
            cityList.add(city);
        }

        return cityList;
    }

    /**
     * 解析县级行政单位HTTP请求响应数据
     *
     * @param resp HTTP请求响应数据
     * @return 是否解析并入库成功
     */
    public synchronized static List<Country> parseCountryResponse(String resp) {
        if (TextUtils.isEmpty(resp)) return null;

        String[] allCountries = resp.split(",");
        if (allCountries.length < 1) return null;

        List<Country> countryList = new ArrayList<Country>();
        for (String countryStr : allCountries) {
            String[] cArray = countryStr.split("\\|");
            Country country = new Country();
            country.setCode(cArray[0]);
            country.setName(cArray[1]);
            countryList.add(country);
        }

        return countryList;
    }
}
