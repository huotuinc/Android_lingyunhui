package com.huotu.lingyunhui.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.android.library.libedittext.EditText;
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
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.VolleyUtil;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class WechatRegActivity extends BaseActivity {

   @Bind(R.id.edt_phone_number)
    EditText edt_phone;
    @Bind(R.id.edt_password)
    EditText edt_password;
    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_tv)
    TextView titleTv;
    ProgressPopupWindow progress;
    public Bundle bundle;
    Long secure= System.currentTimeMillis();
    @Override
    protected void initData() {
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
        bundle = this.getIntent().getExtras();
    }

    @Override
    protected void initTitle() {

            titleTv.setText("微信注册");
        titleLeft.setImageResource(R.mipmap.back_gray);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wechat_reg;
    }
    @OnClick({R.id.rl_left, R.id.btn_save})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_left:
                closeSelf(WechatRegActivity.this);
                break;
            //提交，保存
            case R.id.btn_save:
                if (progress == null)
                    progress = new ProgressPopupWindow(WechatRegActivity.this);
                progress.showProgress("正在登录，请稍等...");
                progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

                String url = Constants.URL + "ArvatoUser/RegisterByWeiXin";
                Map<String,String> map =new HashMap<>();
                AccountModel account=(AccountModel) bundle.getSerializable("wechatdata");
                map.put("openId",account.getOpenid());
                map.put("sex",String.valueOf(account.getSex()));
                map.put("nickname",account.getNickname());
                map.put("wxHead",account.getAccountIcon());
                map.put("city",account.getCity());
                map.put("country",account.getCountry());
                map.put("province",account.getProvince());
                map.put("openId",account.getOpenid());
                map.put("openId",account.getOpenid());
                map.put("unionId",account.getAccountUnionId());
                map.put("mobile",edt_phone.getText().toString());
                map.put("password",edt_password.getText().toString());
                AuthParamUtils paramUtils = new AuthParamUtils("");
                Map param = paramUtils.obtainParams(map);

                GsonRequest<PhoneLoginModel> request = new GsonRequest<>(
                        Request.Method.POST,
                        url,
                        PhoneLoginModel.class,
                        null,
                        param,
                        new WechatRegActivity.MyLoginListener(this),
                        new WechatRegActivity.MyLoginErrorListener(this)
                );
                VolleyUtil.getRequestQueue().add(request);

                if( progress ==null){
                    progress=new ProgressPopupWindow( WechatRegActivity.this);
                }
                progress.showProgress("正在登录，请稍等...");
                progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER , 0 , 0);
                break;
            default:
                break;
        }
    }
    static class MyLoginListener implements Response.Listener<PhoneLoginModel> {
        WeakReference<WechatRegActivity> ref;
        public MyLoginListener(WechatRegActivity act) {
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
        WeakReference<WechatRegActivity> ref;
        public MyLoginErrorListener(WechatRegActivity act) {
            ref = new WeakReference<WechatRegActivity>(act);
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
}
