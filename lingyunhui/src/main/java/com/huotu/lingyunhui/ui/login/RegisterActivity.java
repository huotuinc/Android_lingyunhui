package com.huotu.lingyunhui.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.widgets.CountDownTimerButton;

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
    @Bind(R.id.edt_phone)
    EditText edtPhone;

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
