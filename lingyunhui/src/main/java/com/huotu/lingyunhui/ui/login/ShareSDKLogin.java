package com.huotu.lingyunhui.ui.login;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.huotu.lingyunhui.model.AccountModel;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ShareSDKLogin implements PlatformActionListener {

    public static final int MSG_AUTH_CANCEL = 1;
    public static final int MSG_AUTH_ERROR= 2;
    public static final int MSG_AUTH_COMPLETE = 3;
    private Handler handler;

    public ShareSDKLogin(Handler handler){
        this.handler = handler;
    }

    public void authorize(Platform platform){
        if (platform.isAuthValid()) {
            if(TextUtils.isEmpty( platform.getDb().getUserId() ) ){
                platform.removeAccount(true);
            }else {
                getInfo(platform);
                return;
            }
        }

        platform.setPlatformActionListener(this);
        platform.SSOSetting(false);
        platform.showUser(null);
    }

    protected void getInfo(Platform platform ){
        if(platform.getName().equals(Wechat.NAME)){
            getWechat(platform);
        }else if(platform.getName().equals(QQ.NAME)){
            getQQ(platform);
        }
    }

    protected void getWechat(Platform platform){
        PlatformDb platformDb = platform.getDb();
        AccountModel account = new AccountModel();
        account.setAccountId(platformDb.getUserId());
        account.setAccountName(platformDb.getUserName());
        account.setAccountIcon(platformDb.getUserIcon());
        account.setAccountToken(platformDb.getToken());
        account.setAccountUnionId(platformDb.get("unionid"));
        account.setCity(platformDb.get("city"));
        String sex = platformDb.getUserGender();
        if ("f".equals(sex)) {
            account.setSex(2);
        } else if ("m".equals(sex)) {
            account.setSex(1);
        } else if (null == sex) {
            account.setSex(0);
        }

        account.setOpenid(platformDb.get("openid"));
        account.setCountry(platformDb.get("country"));
        account.setProvince(platformDb.get("province"));
        account.setNickname(platformDb.get("nickname"));

        Message msg= handler.obtainMessage( MSG_AUTH_COMPLETE);
        msg.obj = account;
        msg.sendToTarget();
    }

    protected void getQQ(Platform platform){
        PlatformDb platformDb = platform.getDb();

        AccountModel account = new AccountModel();
        account.setAccountId(platformDb.getUserId());
        account.setAccountName(platformDb.getUserName());
        account.setAccountIcon(platformDb.getUserIcon());
        account.setAccountToken(platformDb.getToken());
        //account.setAccountUnionId(platformDb.get("unionid"));
        account.setCity(platformDb.get("city"));
        String sex = platformDb.getUserGender();
        if ("f".equals(sex)) {
            account.setSex(2);
        } else if ("m".equals(sex)) {
            account.setSex(1);
        } else if (null == sex) {
            account.setSex(0);
        }

        //account.setOpenid(platformDb.get("openid"));
        account.setCountry(platformDb.get("country"));
        account.setProvince(platformDb.get("province"));
        account.setNickname(platformDb.get("nickname"));

        Message msg= handler.obtainMessage( MSG_AUTH_COMPLETE);
        msg.obj = account;
        msg.sendToTarget();
    }

    @Override
    public void onComplete(Platform platform, int action , HashMap<String, Object> hashMap) {
        if( action == Platform.ACTION_USER_INFOR){
            getInfo(platform);
            platform.setPlatformActionListener(null);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        //if(action == Platform.ACTION_USER_INFOR){
            Message msg = handler.obtainMessage(MSG_AUTH_ERROR);
            msg.obj = throwable;
            msg.sendToTarget();
        //移除监听
        platform.setPlatformActionListener(null);
        //}
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if(action== Platform.ACTION_USER_INFOR){
            Message msg = handler.obtainMessage(MSG_AUTH_CANCEL);
            msg.sendToTarget();
        }

        platform.setPlatformActionListener(null);
    }


    public void removeListener(){

    }
}
