package com.huotu.lingyunhui.ui.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;

import com.google.gson.Gson;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.MenuBean;
import com.huotu.lingyunhui.service.LocationService;
import com.huotu.lingyunhui.utils.PreferenceHelper;
import com.huotu.lingyunhui.utils.VolleyUtil;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by hzbc on 2016/5/17.
 */
public class BaseApplication extends Application {
    public static BaseApplication app;

    public LocationService locationService;
    //是否有网络连接
    public boolean isConn = false;

    public static synchronized BaseApplication getInstance() {

        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app=this;
        ShareSDK.initSDK(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
        VolleyUtil.init(getApplicationContext());
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
    //是否首次安装
    public boolean isFirst() {
        String initInfo = PreferenceHelper.readString(getApplicationContext(), Constants.SYS_INFO, Constants.FIRST_OPEN);
        if (TextUtils.isEmpty(initInfo)) {
            return true;
        } else {
            return false;
        }
    }
    //登出
    public void logout() {
//        if( plat ==null ) {
//            plat = ShareSDK.getPlatform(Wechat.NAME);
//        }

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        //取消授权
        if (null != platform) {
            platform.removeAccount();
        }

        PreferenceHelper.clean(getApplicationContext(), Constants.MEMBER_INFO);

        clearAllCookies();
    }

    public String readWxpayAppId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_WEIXIN_ID);
    }
    public String readWeixinNotify() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_NOTIFY);
    }
    //获取用户名称
    public String getUserName() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_NAME);
    }
    public String readWxpayParentId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID);
    }

    public String readWxpayAppKey() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.WEIXIN_KEY);
    }
    public boolean scanWx() {
        String parentId = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID);
        String appid = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_WEIXIN_ID);
        String appKey = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_KEY);
        String notify = PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_NOTIFY);

        if (!TextUtils.isEmpty(parentId) && !TextUtils.isEmpty(appid) && !TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(notify)) {
            return true;
        } else {
            return false;
        }
    }
    public void clearAllCookies(){
        CookieManager.getInstance().removeAllCookie();
    }
    public void writeInitInfo(String initStr) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.SYS_INFO, Constants.FIRST_OPEN, initStr);
    }
    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    //写入数据包版本号
    public void writePackageVersion(String packageVersion) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.DATA_INIT, Constants.PACKAGE_VERSION, packageVersion);
    }

    public static void writeNewVersion(int versionid){
        PreferenceHelper.writeInt( app , Constants.MERCHANT_INFO , Constants.NEW_APP_VERSION , versionid );
    }

    public static int readNewAppVersion(){
        return  PreferenceHelper.readInt( app , Constants.MERCHANT_INFO , Constants.NEW_APP_VERSION , 0 );
    }

    public static void writeAppUrl(String appurl){
        PreferenceHelper.writeString( app , Constants.MERCHANT_INFO , Constants.APP_UPDATE_URL , appurl);
    }
    public static String readAppUlr(){
        return PreferenceHelper.readString( app , Constants.MERCHANT_INFO , Constants.APP_UPDATE_URL, "");
    }
    //读取数据包版本号
    public String readPackageVersion() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.DATA_INIT, Constants.PACKAGE_VERSION);
    }
    public void writeLoginMethod(int loginMethod){
        PreferenceHelper.writeInt( getApplicationContext() , Constants.MERCHANT_INFO , Constants.MERCHANT_INFO_LOGINMETHOD , loginMethod );
    }
    public int readLoginMethod(){
        return  PreferenceHelper.readInt( getApplicationContext(),Constants.MERCHANT_INFO,Constants.MERCHANT_INFO_LOGINMETHOD , 0 );
    }
    public void writehomeurl(String homeurl ){
        PreferenceHelper.writeString(getApplicationContext(),Constants.MERCHANT_INFO,Constants.HOME_URL,homeurl);
    }
    public String readhomeurl(){
       return PreferenceHelper.readString(getApplicationContext(),Constants.MERCHANT_INFO,Constants.HOME_URL);
    }
    //写入底部导航
    public void writeMenus(List<MenuBean> menus) {
        Gson gson = new Gson();
        String menuStr = gson.toJson(menus);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS, menuStr);
    }
    /**
     * 获取商户的底部菜单信息
     *
     * @return
     */
    public String readMenus() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_MENUS);
    }

    //获取商家的访问渠道
    public String obtainMerchantLogo() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_LOGO);
    }

    //获取商家的访问渠道
    public void writeMerchantLogo(String logo) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_LOGO, logo);
    }

    //获取商家的访问渠道
    public String obtainMerchantName() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_NAME);
    }

    //获取商家的访问渠道
    public void writeMerchantName(String name) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_NAME, name);
    }

    public void writeAlipay(String parentId, String appKey, String notify, boolean isWebPay) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.ALIPAY_KEY, appKey);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID, parentId);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.ALIPAY_NOTIFY, notify);
        PreferenceHelper.writeBoolean(getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.IS_WEB_ALIPAY, isWebPay);
    }

    public String readAlipayAppKey() {
        return PreferenceHelper.readString(
                getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.ALIPAY_KEY
        );
    }
    public String readMemberId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID);
    }

    public void writeMemberId(String userId) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId);
    }
    public String readAlipayParentId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.ALIPAY_MERCHANT_ID);
    }

    public void writeWx(String parentId, String appId, String appKey, String notify, boolean isWebPay) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.WEIXIN_MERCHANT_ID, parentId);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_WEIXIN_ID, appId);
        PreferenceHelper.writeString(
                getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.WEIXIN_KEY, appKey
        );
        PreferenceHelper.writeString(
                getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.WEIXIN_NOTIFY, notify
        );
        PreferenceHelper.writeBoolean(
                getApplicationContext(), Constants.MERCHANT_INFO,
                Constants.IS_WEB_WEIXINPAY, isWebPay
        );
    }
    /**
     * 记录域名
     *
     * @param domain
     */
    public void writeDomain(String domain) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.PREFIX, domain);
    }

    public void writeMemberInfo(String userName, String userId, String userIcon, String userToken, String unionid , String openid ) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_NAME, userName);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ICON, userIcon);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_TOKEN, userToken);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_UNIONID, unionid);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_OPENID , openid );
    }
    public void writePhoneLogin(String loginName, String realName, int relatedType, String authorizeCode, String secure) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LOGINNAME, loginName);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_REALNAME, realName);
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_RELATEDTYPE, relatedType);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_AUTHORIZECODE, authorizeCode);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_SECURE, secure);
    }
    public void writeMemberLevelId(int levelid) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LEVELID, levelid);
    }
    public void writeMemberRelatedType(int relatedType) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_RELATEDTYPE, relatedType);
    }
    //记录会员等级
    public void writeMemberLevel(String level) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_level, level);
    }

    public String readMemberLevel() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_level);
    }
    public void writeMemberLoginType(int loginType) {
        PreferenceHelper.writeInt(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_LOGINTYPE, loginType);
    }
    //获取商户ID
    public String readMerchantId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.MERCHANT_INFO_ID);
    }

    //获取商家的访问渠道
    public String obtainMerchantUrl() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MERCHANT_INFO, Constants.PREFIX);
    }
    //获取用户unionId
    public String readUserUnionId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_UNIONID,"");
    }

    //获取用户编号
    public String readUserId() {
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID,"");
    }
    public String readOpenId(){
        return PreferenceHelper.readString(getApplicationContext(),Constants.MEMBER_INFO, Constants.MEMBER_OPENID,"");
    }

    /**
     * 获取当前应用程序的版本号
     */
    public String getAppVersion(Context context) {
        String version = "0";
        try {
            version = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(BaseApplication.class.getName(), e.getMessage());
        }
        return version;
    }

    public void writeConfiginfo(String mallBottomUrl, String mallUrl, String version) {
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.member_mallBottomUrl, mallBottomUrl);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.member_mallUrl, mallUrl);
        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.member_version, version);
    }
    public String readmallBottomUrl(){
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.member_mallBottomUrl, "");
    }
    public String readversion(){
        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.member_version, "");
    }
}
