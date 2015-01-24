package com.zyh.chuxin.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.zyh.chuxin.app.activity.ChooseAreaActivity;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.util.AppClientAPI;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主Activity界面
 */
public class MainActivity extends Activity implements AMapLocationListener, View.OnClickListener {

    public static final int CHOOSE_CITY = 1;

    private LocationManagerProxy locationManagerProxy;

    private NetworkBroadcastReceiver networkBroadcastReceiver;
    private ApplicationContext applicationContext;

    private TextView temperatureTextView, weatherTextView, windTextView, cityNameTextView;
    private LinearLayout changeCityLayout, zhishuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applicationContext = (ApplicationContext) getApplicationContext();

        // 注册网络状态改变广播接收器
        registerNetworkBroadcastReceiver();

        locationManagerProxy = LocationManagerProxy.getInstance(this);
        locationManagerProxy.setGpsEnable(false);
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, this);

        initView();
    }

    private void initView() {
        temperatureTextView = (TextView) findViewById(R.id.main_temperature_text);
        weatherTextView = (TextView) findViewById(R.id.main_weather_text);
        windTextView = (TextView) findViewById(R.id.main_wind_text);
        cityNameTextView = (TextView) findViewById(R.id.main_city_name_text);

        changeCityLayout = (LinearLayout) findViewById(R.id.main_change_city_layout);
        changeCityLayout.setOnClickListener(this);
        zhishuLayout = (LinearLayout) findViewById(R.id.main_zhi_shu_layout);
        zhishuLayout.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
    }

    /**
     * 注册网络状态改变广播接收器
     */
    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver = new NetworkBroadcastReceiver(new NetworkBroadcastReceiver
                .NetworkStateChangeListener() {
            @Override
            public void onNetworkStateChange(int netState) {
                if (netState == NetworkBroadcastReceiver.NETWORK_MOBILE) {
                    Toast.makeText(MainActivity.this, R.string.tip_network_mobile, Toast
                            .LENGTH_LONG).show();
                } else if (netState == NetworkBroadcastReceiver.NETWORK_NONE) {
                    Toast.makeText(MainActivity.this, R.string.tip_network_none, Toast
                            .LENGTH_LONG).show();
                }
            }
        });
        IntentFilter filter = new IntentFilter(NetworkBroadcastReceiver.NETWORK_CHANGE_ACTION);
        registerReceiver(networkBroadcastReceiver, filter);
    }

    private void queryWeatherInfo(String cityCode) {
        QueryWeatherTask task = new QueryWeatherTask(cityCode);
        task.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_change_city_layout:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                startActivityForResult(intent, CHOOSE_CITY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == CHOOSE_CITY) {
            String code = data.getStringExtra("code");
            if (!TextUtils.isEmpty(code)) {
                queryWeatherInfo(code);
            }
        }
    }

    private class QueryWeatherTask extends AsyncTask<String, Void, JSONObject> {
        private String code;

        public QueryWeatherTask(String code) {
            this.code = code;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String weatherCode = AppClientAPI.queryWeatherCode(code);
            return AppClientAPI.queryWeatherInfo(weatherCode);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                temperatureTextView.setText(jsonObject.getString("temp2"));
                weatherTextView.setText(jsonObject.getString("weather"));
                cityNameTextView.setText(jsonObject.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            String cityName = aMapLocation.getCity();
            cityName = cityName.substring(0, cityName.length() - 1);
            if (!TextUtils.isEmpty(cityName)) {
                ChuXinWeatherDB db = applicationContext.getWeatherDB();
                City city = db.loadCity(cityName);
                queryWeatherInfo(city.getCode() + "01");
            }
        } else if (aMapLocation != null) {
            Toast.makeText(this, aMapLocation.getAMapException().getErrorMessage(), Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
