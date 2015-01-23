package com.zyh.chuxin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 网络状态改变广播接收器
 * Created by zhyh on 2015/1/23.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    private NetworkStateChangeListener listener;

    public NetworkBroadcastReceiver(NetworkStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NETWORK_CHANGE_ACTION)) {
            int networkState = ApplicationContext.checkNetworkState();
            if (listener != null) {
                listener.onNetworkStateChange(networkState);
            }
        }
    }

    public interface NetworkStateChangeListener {
        void onNetworkStateChange(int netState);
    }
}
