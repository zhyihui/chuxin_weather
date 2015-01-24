package com.zyh.chuxin.app.util;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhyh on 2015/1/24.
 */
public class AppClientAPI {

    public static final String TAG = "AppClientAPI";

    public static final String Q_WEATHER_CODE_URL = "http://www.weather.com.cn/data/list3/city{code}.xml";
    public static final String BASE_URL = "http://www.weather.com.cn/";
    public static final String WEATHER_URL = BASE_URL + "data/cityinfo/{wcode}.html";

    public static JSONObject queryWeatherInfo(String weatherCode) {
        String qurl = WEATHER_URL.replace("{wcode}", weatherCode);
        Log.d(TAG, "天气查询URL: " + qurl);
        String weatherJSON = HttpUtil.sendHttpRequest(qurl);
        try {
            JSONObject weather = new JSONObject(weatherJSON);
            return weather.getJSONObject("weatherinfo");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return new JSONObject();
        }
    }

    public static String queryWeatherCode(String countrCode) {
        String url = Q_WEATHER_CODE_URL.replace("{code}", countrCode);
        String msg = HttpUtil.sendHttpRequest(url);
        return msg.split("\\|")[1];
    }
}
