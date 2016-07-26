package com.huotu.lingyunhui.ui.login;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.AccountModel;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.HttpUtil;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.widgets.NoticePopWindow;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * Created by hzbc on 2016/5/16.
 */
public class LoginActivity extends BaseActivity implements Handler.Callback {

    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_phone_number)
    EditText edt_phone_number;
    @Bind(R.id.edt_password)
    TextView edt_password;
    @Bind(R.id.login_wechat)
    RelativeLayout login_wechat;
    public ProgressPopupWindow progress;
    public ProgressPopupWindow successProgress;
    //windows类
    WindowManager wManager;
    public NoticePopWindow noticePop;
    public AssetManager am;
    AutnLogin login;
    public Handler mHandler;

    @Override
    protected void initData() {
        //设置沉浸模式
        progress = new ProgressPopupWindow ( LoginActivity.this );
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
    }

    @Override
    protected void initTitle() {
        titleTv.setText("登入");
        titleLeft.setImageResource(R.mipmap.back_gray);
    }

    @Override
    public int getLayoutId() {
        mHandler = new Handler ( this );
        am = this.getAssets();
        wManager = this.getWindowManager();
        progress = new ProgressPopupWindow ( LoginActivity.this );
        successProgress = new ProgressPopupWindow (  LoginActivity.this );
        return R.layout.activity_login;
    }

    @OnClick({R.id.rl_left, R.id.btn_register, R.id.tv_password, R.id.btn_login, R.id.login_wechat})
    public void OnClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.rl_left:
                closeSelf(LoginActivity.this);
                break;
            //注册
            case R.id.btn_register:
                bundle=new Bundle();
                bundle.putInt("type",1);
                ActivityUtils.getInstance().showActivity(LoginActivity.this, RegisterActivity.class,bundle);
                break;
            //忘记密码
            case R.id.tv_password:
                bundle=new Bundle();
                bundle.putInt("type",2);
                ActivityUtils.getInstance().showActivity(LoginActivity.this, RegisterActivity.class,bundle);
                break;
            case R.id.btn_login:
                //立即登入
                goLogin();
                break;
            case R.id.login_wechat:
                //微信登入
                goWechatLogin();
                break;
            default:
                break;
        }
    }

    //立即登入
    private void goLogin() {
        if (TextUtils.isEmpty(edt_phone_number.getText()) || edt_phone_number.getText().length() != 11) {
            ToastUtils.showMomentToast(LoginActivity.this, LoginActivity.this, "请输入正确的手机号");
            return;
        } else if (TextUtils.isEmpty(edt_password.getText())) {
            ToastUtils.showMomentToast(LoginActivity.this, LoginActivity.this, "请输入密码");
            return;
        } else {


        }
    }

    //微信登入
    private void goWechatLogin() {
        progress.showProgress(null);
        progress.showAtLocation ( login_wechat, Gravity.CENTER, 0, 0 );
        //微信授权登录
        login = new AutnLogin (  mHandler );
        //login.authorize(new Wechat(LoginActivity.this));
        login.authorize(ShareSDK.getPlatform( Wechat.NAME ));
        login_wechat.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if( progress!=null ){
            progress.dismissView();
            progress=null;
        }
        if( successProgress !=null) {
            successProgress.dismissView();
            successProgress=null;
        }
    }
    @Override
    protected void onResume ( ) {
        super.onResume();

        if(null != progress){
            progress.dismissView ( );
        }

        login_wechat.setClickable(true);
    }

    @Override
    public boolean handleMessage(Message msg) {
        login_wechat.setClickable(true);

        switch ( msg.what )
        {
            //授权登录
            case Constants.MSG_AUTH_COMPLETE:
            {
                //提示授权成功
                Platform plat = ( Platform ) msg.obj;
                login.authorize ( plat );
            }
            break;
            //授权登录 失败
            case Constants.LOGIN_AUTH_ERROR:
            {
                login_wechat.setClickable ( true );
                progress.dismissView ( );
                successProgress.dismissView ();
                //提示授权失败
                String notice = ( String ) msg.obj;
                noticePop = new NoticePopWindow( LoginActivity.this,  notice);
                noticePop.showNotice ();
                noticePop.showAtLocation ( login_wechat , Gravity.CENTER, 0, 0 );
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {
                login_wechat.setClickable ( true );
                progress.dismissView ( );
                Throwable throwable = ( Throwable ) msg.obj;
                //if("cn.sharesdk.wechat.utils.WechatClientNotExistException".equals ( throwable.toString () ))
                if( throwable instanceof cn.sharesdk.wechat.utils.WechatClientNotExistException ){
                    //手机没有安装微信客户端
                    //phoneLogin("您的设备没有安装微信客户端,是否通过手机登录?");
                }
                else {
                    login_wechat.setClickable ( true );
                    progress.dismissView ();
                    //提示授权失败
                    noticePop = new NoticePopWindow ( LoginActivity.this, "微信授权失败");
                    noticePop.showNotice ();
                    noticePop.showAtLocation ( login_wechat, Gravity.CENTER, 0, 0 );
                }
            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                //phoneLogin("你已经取消微信授权,是否通过手机登录?");
            }
            break;
            case Constants.MSG_USERID_FOUND:
            {
                successProgress.showProgress ( "已经获取用户信息" );
                successProgress.showAtLocation ( login_wechat , Gravity.CENTER, 0, 0);
            }
            break;
            case Constants.MSG_LOGIN:
            {
                progress.dismissView ( );
                //登录后更新界面
                AccountModel account = ( AccountModel ) msg.obj;
                //和商家授权
                AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), "", LoginActivity.this );
                final Map param = paramUtils.obtainParams ( account );
                String authUrl = Constants.INTERFACE_URL + "weixin/LoginAuthorize";
                HttpUtil.getInstance ().doVolley ( LoginActivity.this, mHandler, application, authUrl, param, account ,null );
            }
            break;
            case Constants.MSG_USERID_NO_FOUND:
            {
                progress.dismissView ();
                //提示授权成功
                noticePop = new NoticePopWindow (  LoginActivity.this, "获取用户信息失败");
                noticePop.showNotice ();
                noticePop.showAtLocation (login_wechat , Gravity.CENTER, 0, 0);
            }
            break;
//            case Constants.INIT_MENU_ERROR:
//            {
//                progress.dismissView ();
//                //提示初始化菜单失败
//                noticePop = new NoticePopWindow (  LoginActivity.this, "初始化菜单失败");
//                noticePop.showNotice ();
//                noticePop.showAtLocation (login_wechat , Gravity.CENTER, 0, 0 );
//            }
//            break;
        }
        return false;
    }
}
