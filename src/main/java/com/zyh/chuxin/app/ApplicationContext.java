package com.zyh.chuxin.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;
import com.zyh.chuxin.app.util.CityDataFetcher;

import java.io.*;

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
        try {
            initializeDatabase();
        } catch (IOException e) {
            Toast.makeText(this, "初始化失败", Toast.LENGTH_LONG).show();
            System.exit(0);
        }
    }

    public ChuXinWeatherDB getWeatherDB() {
        return weatherDB;
    }

    /**
     * 初始化数据库对象, 初次启动需要将数据库文件拷贝到目标路径
     *
     * @throws IOException
     */
    private String initDatabaseFromLocal() throws IOException {
        File dbFileDir = getDir("db_files", MODE_PRIVATE);
        File dbFile = new File(dbFileDir, ChuXinWeatherDB.DB_NAME);
        if (!dbFile.exists()) {
            InputStream is = getAssets().open("city.db");
            OutputStream os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.flush();
            os.close();
        }
        return dbFile.getAbsolutePath();
    }

    private void initializeDatabase() throws IOException {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fetchFlag = preferences.getBoolean("com.chuxin.data.fetch", false);
        weatherDB = ChuXinWeatherDB.getInstance(this, null);
        if (!fetchFlag) {
            weatherDB.clear();
            new CityDataFetcher(weatherDB, new CityDataFetcher.OnCityDataFetchedListener() {
                @Override
                public void onCityDataFetched() {
                    preferences.edit().putBoolean("com.chuxin.data.fetch", true).apply();
                }

                @Override
                public void onErrorOccur() {
                    try {
                        String dbFilePath = initDatabaseFromLocal();
                        weatherDB = ChuXinWeatherDB.getInstance(application, dbFilePath);
                    } catch (IOException ignored) {
                    }
                }
            }).fetch(true);
        }
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
