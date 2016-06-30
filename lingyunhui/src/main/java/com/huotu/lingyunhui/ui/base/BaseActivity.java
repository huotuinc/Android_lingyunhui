package com.huotu.lingyunhui.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huotu.lingyunhui.ui.guide.GuideActivity;
import com.huotu.lingyunhui.utils.AppManager;

import butterknife.ButterKnife;

/**
 * Created by hzbc on 2016/5/16.
 */
public abstract class BaseActivity extends Activity {

    public BaseApplication application;
    public Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = BaseApplication.getInstance();
        //禁止横屏
        BaseActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        resources = this.getResources();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        initData();
        initTitle();
    }

    public void setImmerseLayout(View view) {
        if (application.isKITKAT()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = this.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected abstract void initData();

    protected abstract void initTitle();

    public abstract int getLayoutId();

    public void closeCurrent() {
        AppManager.getInstance().removeCurrent();
    }

    public void closeSelf(Activity aty) {
        aty.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            //关闭
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
