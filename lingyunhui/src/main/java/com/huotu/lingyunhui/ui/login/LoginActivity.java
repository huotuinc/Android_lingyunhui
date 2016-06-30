package com.huotu.lingyunhui.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hzbc on 2016/5/16.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_phone_number)
    EditText edt_phone_number;
    @Bind(R.id.edt_password)
    TextView edt_password;

    @Override
    protected void initData() {
        //设置沉浸模式
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
    }

    @Override
    protected void initTitle() {
        titleTv.setText("登入");
        titleLeft.setImageResource(R.mipmap.back_gray);
    }

    @Override
    public int getLayoutId() {
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
        Toast.makeText(this,"微信登入",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
