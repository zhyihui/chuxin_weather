package com.zyh.chuxin.app;

import android.app.Activity;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.util.CityDataFetcher;

/**
 * 主Activity界面
 */
public class MainActivity extends Activity implements AMapLocationListener {

    private LocationManagerProxy locationManagerProxy;

    private NetworkBroadcastReceiver networkBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 注册网络状态改变广播接收器
        registerNetworkBroadcastReceiver();

        final ApplicationContext applicationContext = (ApplicationContext) getApplicationContext();
        if (!applicationContext.isCityDataReady()) {
            new CityDataFetcher(this, new CityDataFetcher.OnCityDataFetchedListener() {
                @Override
                public void onCityDataFetched() {
                    // 数据获取成功
                    applicationContext.saveCityDataState();
                    initializeLocation();
                }
            }).fetchCityData();
        } else {
            initializeLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
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

    private void initializeLocation() {
        locationManagerProxy = LocationManagerProxy.getInstance(this);
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, this);
        locationManagerProxy.setGpsEnable(false);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            TextView textView = (TextView) findViewById(R.id.show_desc);
            String cityName = aMapLocation.getCity();
            cityName = cityName.substring(0, cityName.length() - 1);
            if (!TextUtils.isEmpty(cityName)) {
                ChuXinWeatherDB weatherDB = ChuXinWeatherDB.getInstance(this);
                City city = weatherDB.loadCity(cityName);
                textView.setText(city.getName() + ":" + city.getCode());
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
