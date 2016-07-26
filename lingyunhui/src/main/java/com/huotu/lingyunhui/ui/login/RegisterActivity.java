package com.huotu.lingyunhui.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.BaseBean;
import com.huotu.lingyunhui.model.DataBase;
import com.huotu.lingyunhui.model.InitModel;
import com.huotu.lingyunhui.model.MenuBean;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.splash.SplashActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.GsonRequest;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.VolleyUtil;
import com.huotu.lingyunhui.widgets.CountDownTimerButton;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hzbc on 2016/5/30.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.btn_code)
    Button btn_code;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_code)
    EditText  edtCode;
    @Bind(R.id.edt_phone)
    EditText edtPhone;
    ProgressPopupWindow progressPopupWindow;

    // 按钮倒计时控件
    private CountDownTimerButton countDownBtn;
    public Bundle bundle;

    @Override
    protected void initData() {
        //设置沉浸模式
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
        bundle = this.getIntent().getExtras();
    }

    @Override
    protected void initTitle() {
        if (1 == bundle.getInt("type")) {
            titleTv.setText("手机注册");
        } else if (2 == bundle.getInt("type")) {
            titleTv.setText("忘记密码");
        }
        titleLeft.setImageResource(R.mipmap.back_gray);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.rl_left, R.id.btn_save, R.id.btn_code})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_left:
                closeSelf(RegisterActivity.this);
                break;
            //提交，保存
            case R.id.btn_save:
                String url = Constants.Message_url + "VerifyCode";
                Map<String, String> map = new HashMap<>();
                map.put("mobile", edtPhone.getText().toString());
                map.put("code",edtCode.getText().toString());
                //map.put("second");
                AuthParamUtils authParamUtils = new AuthParamUtils( url);
                Map<String, String> params = authParamUtils.obtainParams(map);


                GsonRequest<DataBase> request = new GsonRequest<DataBase>(Request.Method.POST,
                        url, DataBase.class, null, params, new Response.Listener<DataBase>() {
                    @Override
                    public void onResponse(DataBase dataBase) {
                        if (progressPopupWindow != null) {
                            progressPopupWindow.dismissView();
                            ActivityUtils.getInstance().skipActivity(RegisterActivity.this,ModifyPwdActivity.class);
                        }
                        if (dataBase == null || dataBase.getCode() != 200 ) {
                           // ToastUtils.showShortToast( RegisterActivity.this,"验证码错误");
                            if (bundle.getInt("type")==1) {
                                bundle.putInt("type", 1);
                                bundle.putString("moblie",edtPhone.getText().toString());
                                ActivityUtils.getInstance().skipActivity(RegisterActivity.this, ModifyPwdActivity.class,bundle);
                            }else {
                                bundle.putInt("type", 2);
                                bundle.putString("moblie",edtPhone.getText().toString());
                                ActivityUtils.getInstance().skipActivity(RegisterActivity.this, ModifyPwdActivity.class,bundle);
                            }
                            return;
                        }

                        edtCode.requestFocus();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressPopupWindow != null) {
                            progressPopupWindow.dismissView();
                        }
                        ToastUtils.showShortToast(RegisterActivity.this,"请求失败");
                    }
                });

                VolleyUtil.getRequestQueue().add( request );

                break;
            //获取验证码
            case R.id.btn_code:
                if (isWritePhone() == true) {
                    //验证用户名是否存在
                    checkUserName();
                } else {
                    ToastUtils.showMomentToast(RegisterActivity.this, RegisterActivity.this, "请输入正确的手机号");
                }
                break;
            default:
                break;
        }
    }

    //验证用户名是否存在
    private void checkUserName() {
        countDownBtn = new CountDownTimerButton(btn_code, "%d秒重发", "获取验证码", 60000, new CountDownFinish());
        countDownBtn.start();
        String url = Constants.Message_url + "SendCode";
        Map<String, String> map = new HashMap<>();
        map.put("mobile", edtPhone.getText().toString());
        //map.put("second");
        AuthParamUtils authParamUtils = new AuthParamUtils( url);
        Map<String, String> params = authParamUtils.obtainParams(map);


        GsonRequest<DataBase> request = new GsonRequest<DataBase>(Request.Method.POST,
                url, DataBase.class, null, params, new Response.Listener<DataBase>() {
            @Override
            public void onResponse(DataBase dataBase) {
                if (progressPopupWindow != null) {
                    progressPopupWindow.dismissView();
                }
                if (dataBase == null || dataBase.getCode() != 200 ) {
                    ToastUtils.showShortToast( RegisterActivity.this,"获取验证码失败。");
                    return;
                }

                edtCode.requestFocus();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressPopupWindow != null) {
                    progressPopupWindow.dismissView();
                }
                ToastUtils.showShortToast(RegisterActivity.this,"请求失败");
            }
        });

        VolleyUtil.getRequestQueue().add( request );

    }


    //验证输入的电话号码是不是为空
    private boolean isWritePhone() {
        if (!TextUtils.isEmpty(edtPhone.getText()) && edtPhone.getText().length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (null != countDownBtn) {
            countDownBtn.Stop();
        }
    }

    /**
     * 倒计时控件 完成时，回调类
     *
     * @类名称：CountDownFinish
     * @类描述：
     * @创建人：jinxiangdong
     * @修改人：
     * @修改时间：2015年7月8日 上午9:17:06
     * @修改备注：
     * @version:
     */
    class CountDownFinish implements CountDownTimerButton.CountDownFinishListener {

        @Override
        public void finish() {
           /* if( getVCResult !=null && getVCResult.getResultData()!=null && getVCResult.getResultData().isVoiceAble()){
                // 刷新获取按钮状态，设置为可获取语音
                btn_code.setText("尝试语音获取");
                btn_code.setTag(Contant.SMS_TYPE_VOICE);
                ToastUtils.showMomentToast(MobileRegActivity.this, MobileRegActivity.this,
                        "还没收到短信，请尝试语音获取");
            }*/
        }

    }

}
