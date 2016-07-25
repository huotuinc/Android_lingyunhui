package com.huotu.lingyunhui.ui.login;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.AccountModel;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

/**
 * 微信授权实现类
 */
public class AutnLogin {
    private Handler mHandler;

    public AutnLogin(Handler mHandler ) {
        this.mHandler = mHandler;
    }

    public void authorize ( Platform plat ) {
        if (plat.isValid()) {
            //application.plat = plat;
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                mHandler.sendEmptyMessage(Constants.MSG_USERID_FOUND);
                login(plat);
                return;
            } else {
                plat.removeAccount();
                mHandler.sendEmptyMessage(Constants.MSG_USERID_NO_FOUND);
                return;
            }
        }
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {

                //view.setClickable ( true );
                if (action == Platform.ACTION_USER_INFOR) {
                    //unionid
                    //String unionId = platform.getDb().get("unionid");
                    //openId
                    //String openId = platform.getDb().get("openid");
                    Message msg = new Message();
                    msg.what = Constants.MSG_AUTH_COMPLETE;
                    msg.obj = platform;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Platform platform, int action, Throwable throwable) {
                //view.setClickable ( true );
                Message msg = new Message();
                msg.what = Constants.MSG_AUTH_ERROR;
                msg.obj = throwable;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onCancel(Platform platform, int action) {
                //view.setClickable ( true );
                if (action == Platform.ACTION_USER_INFOR) {
                    mHandler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL);
                }
            }
        });
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private void login(Platform plat) {
        Message msg = new Message();
        msg.what = Constants.MSG_LOGIN;

        PlatformDb accountDb = plat.getDb ();
        AccountModel account = new AccountModel ();
        account.setAccountId ( accountDb.getUserId ( ) );
        account.setAccountName ( accountDb.getUserName ( ) );
        account.setAccountIcon ( accountDb.getUserIcon ( ) );
        account.setAccountToken ( accountDb.getToken ( ) );
        account.setAccountUnionId ( accountDb.get ( "unionid" ) );
        account.setCity ( accountDb.get ( "city" ) );
        String sex = accountDb.getUserGender ( );
        if("f".equals ( sex ))
        {
            account.setSex ( 2 );
        }
        else if("m".equals ( sex ))
        {
            account.setSex ( 1 );
        }
        else if(null == sex)
        {
            account.setSex ( 0 );
        }

        account.setOpenid(accountDb.get("openid"));
        account.setCountry(accountDb.get("country"));
        account.setProvince(accountDb.get("province"));
        account.setNickname ( accountDb.get ( "nickname" ) );

        msg.obj = account;
        mHandler.sendMessage ( msg );
    }
}
