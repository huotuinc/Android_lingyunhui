package com.huotu.lingyunhui.ui.login;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.AccountModel;
import com.huotu.lingyunhui.model.AuthMallModel;
import com.huotu.lingyunhui.model.PhoneLoginModel;
import com.huotu.lingyunhui.model.RefreshHttpHeaderEvent;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.ui.main.MainActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.GsonRequest;
import com.huotu.lingyunhui.utils.HttpUtil;
import com.huotu.lingyunhui.utils.SystemTools;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.VolleyUtil;
import com.huotu.lingyunhui.widgets.NoticePopWindow;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    //windows类
    WindowManager wManager;
    public NoticePopWindow noticePop;
    public AssetManager am;
    AutnLogin login;
    public Handler mHandler;
    Long secure= System.currentTimeMillis();
    ShareSDKLogin shareSDKLogin;
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
        shareSDKLogin = new ShareSDKLogin(mHandler);
        am = this.getAssets();
        wManager = this.getWindowManager();
        progress = new ProgressPopupWindow ( LoginActivity.this );
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
            String url = Constants.URL + "/ArvatoUser/LoginByMobile";
            Map<String, String> map = new HashMap<>();
            map.put("mobile", edt_phone_number.getText().toString());
            map.put("password", edt_password.getText().toString());
            AuthParamUtils authParamUtils = new AuthParamUtils(application,  secure , url , LoginActivity.this );
            Map<String, String> params = authParamUtils.obtainParams(map);
            GsonRequest<PhoneLoginModel> request = new GsonRequest<>(
                    Request.Method.POST,
                    url,
                    PhoneLoginModel.class,
                    null,
                    params,
                    new LoginActivity.MyLoginListener(this),
                    new LoginActivity.MyLoginErrorListener(this)
            );
            VolleyUtil.getRequestQueue().add(request);

            if( progress ==null){
                progress=new ProgressPopupWindow( LoginActivity.this);
            }
            progress.showProgress("正在登录，请稍等...");
            progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER , 0 , 0);

        }
    }
    static class MyLoginListener implements Response.Listener<PhoneLoginModel> {
        WeakReference<LoginActivity> ref;
        public MyLoginListener(LoginActivity act) {
            ref=new WeakReference<>(act);
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel) {
            if( ref.get()==null)return;

            if( ref.get().progress !=null){
                ref.get().progress.dismissView();
            }

            if( phoneLoginModel ==null ){
                ToastUtils.showShortToast( "登录失败。" );
                return;
            }
            if( phoneLoginModel.getCode() != 200) {
                String msg = "登录失败";
                if( !TextUtils.isEmpty( phoneLoginModel.getMsg()  )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast( msg );
                return;
            }

            PhoneLoginModel.PhoneModel model = phoneLoginModel.getData();

            //写入userID
            //和商城用户系统交互
            ref.get().application.writeMemberInfo(
                    model.getUserName(), String.valueOf(model.getUserId()),
                    model.getHeadImgUrl(), String.valueOf( ref.get().secure )  , model.getAuthorizeCode() , model.getOpenId() );
            ref.get().application.writeMemberLevel(model.getLevelName());

            BaseApplication.app.writeMemberLevelId(model.getLevelId());

            //设置 用户的登级Id
            //Jlibrary.initUserLevelId( model.getLevelId() );

            ref.get().application.writePhoneLogin(model.getLoginName(), model.getRealName(), model.getRelatedType(), model.getAuthorizeCode() , String.valueOf( ref.get().secure ) );
            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType( 2 );


            Bundle bd = new Bundle();
            //String url = PreferenceHelper.readString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF);
            //bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
            //bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);
            Intent intent = new Intent();
            intent.setClass(ref.get(), MainActivity.class);
            intent.putExtras(bd);
            //传递推送信息
//            if(null !=ref.get().bundlePush){
//                intent.putExtra( Constants.PUSH_KEY, ref.get().bundlePush );
//            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            //设置调转页面
            String redirecturl = ref.get().getIntent().getExtras() ==null? "" : ref.get().getIntent().getExtras().getString("redirecturl");
            intent.putExtra("redirecturl", redirecturl);

            ActivityUtils.getInstance().skipActivity(ref.get(), intent );

            EventBus.getDefault().post(new RefreshHttpHeaderEvent());
        }
    }

    static class MyLoginErrorListener implements Response.ErrorListener {
        WeakReference<LoginActivity> ref;
        public MyLoginErrorListener(LoginActivity act) {
            ref = new WeakReference<LoginActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progress != null ){
                ref.get().progress.dismissView();
            }

            ToastUtils.showShortToast("登录失败");
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
        if( null == progress){
            progress = new ProgressPopupWindow(this);
        }
        progress.showProgress("正在调用微信登录,请稍等...");

        progress.showAtLocation(this.getWindow().getDecorView(),Gravity.CENTER , 0,0 );

        login_wechat.setEnabled(false);

        Platform platform = ShareSDK.getPlatform( Wechat.NAME);
        shareSDKLogin.authorize(platform);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if( progress!=null ){
            progress.dismissView();
            progress=null;
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
        if(progress !=null){
            progress.dismissView();
        }
        login_wechat.setClickable(true);

        if( msg.what == ShareSDKLogin.MSG_AUTH_COMPLETE ){
            AccountModel accountModel = (AccountModel) msg.obj;
            wechatToMall( accountModel );
        }else if( msg.what == ShareSDKLogin.MSG_AUTH_CANCEL){
        }else if( msg.what == ShareSDKLogin.MSG_AUTH_ERROR){
            String error = "授权失败";
            ToastUtils.showShortToast( error );
        }else if(msg.what== Constants.LOGIN_AUTH_SUCCESS){

            AuthMallModel.AuthMall data = (AuthMallModel.AuthMall) msg.obj;
            if( !data.isMobileBind()){
                //bindPhone();
            }else{
                goToHome();
            }


        }else if(msg.what==Constants.LOGIN_AUTH_ERROR){
            login_wechat.setClickable(true);
            String error = msg.obj.toString();
            ToastUtils.showShortToast(error);
        }
        return false;
    }

    protected void goToHome(){
        //跳转到首页
        Bundle bd = new Bundle();
        //String url = PreferenceHelper.readString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF);
        //bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
        //bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);

        Intent intent = new Intent();
        intent.setClass( this , MainActivity.class);
        intent.putExtras(bd);


        //设置调转页面
        String redirecturl = getIntent().getExtras() ==null ? "": getIntent().getExtras().getString("redirecturl");
        intent.putExtra("redirecturl", redirecturl);

        ActivityUtils.getInstance().skipActivity( this , intent );
        //ActivityUtils.getInstance().skipActivity(this, FragMainActivity.class, bd);

        EventBus.getDefault().post(new RefreshHttpHeaderEvent());

    }

    /**
     *
     * @param account
     */
    protected void wechatToMall(final AccountModel account ) {
        if (progress == null)
            progress = new ProgressPopupWindow(LoginActivity.this);
        progress.showProgress("正在登录，请稍等...");
        progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        String url = Constants.URL + "ArvatoUser/LoginByWeiXin";
        Map<String,String> map =new HashMap<>();
        map.put("openId",account.getOpenid());
        map.put("unionId",account.getAccountUnionId());
        AuthParamUtils paramUtils = new AuthParamUtils("");
        Map param = paramUtils.obtainParams(map);

        GsonRequest<PhoneLoginModel> request =new GsonRequest<PhoneLoginModel>(Request.Method.POST,
                url ,
                PhoneLoginModel.class,
                null,
                param,
                new WeiXinloginListener(this,account),
                new WeiXinloginErrorListener(this)
        );
                VolleyUtil.getRequestQueue().add(request);
        if( progress ==null){
            progress=new ProgressPopupWindow( LoginActivity.this);
        }
        progress.showProgress("正在登录，请稍等...");
        progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER , 0 , 0);



    }
    static class WeiXinloginListener implements Response.Listener<PhoneLoginModel> {
        WeakReference<LoginActivity> ref;
        AccountModel accountModel;
        public WeiXinloginListener(LoginActivity act,AccountModel accountModel) {
            ref=new WeakReference<>(act);
            this.accountModel= accountModel;
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel) {
            if( ref.get()==null)return;

            if( ref.get().progress !=null){
                ref.get().progress.dismissView();
            }

            if( phoneLoginModel ==null ){
                ToastUtils.showShortToast( "登录失败。" );
                return;
            }
            if (phoneLoginModel.getCode()==403){
                Bundle bundle =new Bundle();
                bundle.putSerializable("wechatdata", (Serializable) accountModel);
                ActivityUtils.getInstance().skipActivity(ref.get(),WechatRegActivity.class,bundle);

            }
            if( phoneLoginModel.getCode() != 200) {
                String msg = "登录失败";
                if( !TextUtils.isEmpty( phoneLoginModel.getMsg()  )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast( msg );
                return;
            }

            PhoneLoginModel.PhoneModel model = phoneLoginModel.getData();

            //写入userID
            //和商城用户系统交互
            ref.get().application.writeMemberInfo(
                    model.getNickName(), String.valueOf(model.getUserId()),
                    model.getHeadImgUrl(), String.valueOf( ref.get().secure )  , model.getUnionId() , model.getOpenId() );
            ref.get().application.writeMemberLevel(model.getLevelName());

            BaseApplication.app.writeMemberLevelId(model.getLevelId());

            //设置 用户的登级Id
            //Jlibrary.initUserLevelId( model.getLevelId() );

            ref.get().application.writePhoneLogin(model.getLoginName(), model.getRealName(), model.getRelatedType(), model.getAuthorizeCode() , String.valueOf( ref.get().secure ) );
            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType( 2 );



            Bundle bd = new Bundle();
            //String url = PreferenceHelper.readString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF);
            //bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
            //bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);
            Intent intent = new Intent();
            intent.setClass(ref.get(), MainActivity.class);
            intent.putExtras(bd);


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            //设置调转页面
            String redirecturl = ref.get().getIntent().getExtras() ==null? "" : ref.get().getIntent().getExtras().getString("redirecturl");
            intent.putExtra("redirecturl", redirecturl);

            ActivityUtils.getInstance().skipActivity(ref.get(), intent );

            EventBus.getDefault().post(new RefreshHttpHeaderEvent());
        }
    }

    static class WeiXinloginErrorListener implements Response.ErrorListener {
        WeakReference<LoginActivity> ref;
        public WeiXinloginErrorListener(LoginActivity act) {
            ref = new WeakReference<LoginActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progress != null ){
                ref.get().progress.dismissView();
            }

            ToastUtils.showShortToast("登录失败");
        }
    }

    protected void getinfo( String accountToken , String unionid , AuthMallModel model ){
        if (model == null || model.getCode() != 200 || model.getData() == null ) {
            Message msg = mHandler.obtainMessage(Constants.LOGIN_AUTH_ERROR);
            msg.obj = "请求失败,请重试!";
            msg.sendToTarget();
            return;
        }

        AuthMallModel.AuthMall mall = model.getData();
        //和商城用户系统交互
        application.writeMemberInfo(mall.getNickName(), String.valueOf(mall.getUserId()),
                mall.getHeadImgUrl(),  accountToken , unionid , mall.getOpenId() );
        application.writeMemberLevel(mall.getLevelName());
        //
        application.writeMemberLevelId(mall.getLevelId());





        Message msg = mHandler.obtainMessage(Constants.LOGIN_AUTH_SUCCESS);
        msg.obj = mall;
        msg.sendToTarget();
    }
    }
