package org.unimelb.itime.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.messageevent.MessageNetworkStatus;

/**
 * Created by Qiushuo Huang on 2017/3/12.
 */

public class NetworkUtil {
    private static NetworkUtil instance;
    private Context context;
    private boolean available;
    private boolean wifi;
    private boolean mobile;

    private NetworkUtil(Context context){
        this.context = context;
    }

    public static NetworkUtil getInstance(Context context){
        if(instance==null){
            instance = new NetworkUtil(context.getApplicationContext());
        }
        return instance;
    }

    public boolean isMobile() {
        return mobile;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        checkNetwork();
        if(available!=this.available) {
            EventBus.getDefault().post(new MessageNetworkStatus(available, wifi, mobile));
        }
        this.available = available;
    }

    public boolean isWifi() {
        return wifi;
    }

    private void checkNetwork() {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            mobile = dataNetworkInfo == null ? false : dataNetworkInfo.isConnected();
            wifi = wifiNetworkInfo == null ? false : wifiNetworkInfo.isConnected();
    }
}
