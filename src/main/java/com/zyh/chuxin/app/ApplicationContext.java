package com.zyh.chuxin.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;

/**
 * 应用上下文环境
 * Created by zhyh on 2015/1/23.
 */
public class ApplicationContext extends Application {

    private static Application application;

    private ChuXinWeatherDB weatherDB;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        weatherDB = ChuXinWeatherDB.getInstance(this);


    }

    public boolean isCityDataReady() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("com.chuxin.data.fetch", false);
    }

    public void saveCityDataState() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
                .edit();
        editor.putBoolean("com.chuxin.data.fetch", true);
        editor.apply();
    }

    /**
     * 获取当前网络连接状态(可能结果为无连接、移动网络和Wifi网络)
     *
     * @return 当前网络连接状态
     */
    public static int checkNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifiState == State.CONNECTED || wifiState == State.CONNECTING) {
            return NetworkBroadcastReceiver.NETWORK_WIFI;
        }

        State mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (mobileState == State.CONNECTED || mobileState == State.CONNECTING) {
            return NetworkBroadcastReceiver.NETWORK_MOBILE;
        }

        return NetworkBroadcastReceiver.NETWORK_NONE;
    }
}
