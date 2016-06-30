package com.huotu.lingyunhui.ui.base;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.huotu.lingyunhui.listener.LocationListener;
import com.huotu.lingyunhui.service.LocationService;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by hzbc on 2016/5/17.
 */
public class BaseApplication extends Application {
    public static BaseApplication app;

    public LocationService locationService;

    public static synchronized BaseApplication getInstance() {

        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app=this;
        ShareSDK.initSDK(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //判断是否为4.4版本。可设置沉浸模式
    public boolean isKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }


    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取当前应用程序的版本号
     */
    public String getAppVersion(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return version;
    }
}
