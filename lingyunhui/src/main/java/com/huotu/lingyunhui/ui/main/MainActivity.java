package com.huotu.lingyunhui.ui.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.fragment.AgentFragment;
import com.huotu.lingyunhui.fragment.HomeFragment;
import com.huotu.lingyunhui.fragment.LYHFragment;
import com.huotu.lingyunhui.fragment.MallFragment;
import com.huotu.lingyunhui.listener.LocationListener;
import com.huotu.lingyunhui.service.LocationService;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.login.LoginActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.SystemTools;
import com.huotu.lingyunhui.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.iv_lyh)
    ImageView ivLyh;
    @Bind(R.id.tv_lyh)
    TextView tvLyh;
    @Bind(R.id.iv_shangcheng)
    ImageView ivShangcheng;
    @Bind(R.id.tv_shangcheng)
    TextView tvShangcheng;
    @Bind(R.id.iv_dlr)
    ImageView ivDlr;
    @Bind(R.id.tv_dlr)
    TextView tvDlr;
    private FragmentTransaction transaction;
    private LocationService locationService;
    private LocationListener locationListener;
    private long exitTime = 0l;

    @Override
    protected void initData() {
        setSelect(0);
        //设置沉浸模式
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }




    @Override
    protected void onStart() {
        super.onStart();
        locationService = application.locationService;
        locationListener=new LocationListener();
        locationService.registerListener(locationListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(locationListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    HomeFragment homeFragment = null;
    LYHFragment lyhFragment = null;
    MallFragment mallFragment = null;
    AgentFragment agentFragment = null;

    @OnClick({R.id.ll_home, R.id.ll_lyh, R.id.ll_shangcheng, R.id.ll_dlr})
    public void changeFrag(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                setSelect(0);
                break;
            case R.id.ll_lyh:
                setSelect(1);
                break;
            case R.id.ll_shangcheng:
                setSelect(2);
                break;
            case R.id.ll_dlr:
                if (true){
                    ActivityUtils.getInstance().showActivity(this, LoginActivity.class);
                }else{
                    setSelect(3);
                }
                break;
        }
    }
int currentIndex;
    private void setSelect(int i) {
        FragmentManager fm = getFragmentManager();
        transaction = fm.beginTransaction();
        hideFragments();
        resetImgs();
        switch (i) {
            case 0:
                if (null == homeFragment) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fg_container, homeFragment);
                }
                transaction.show(homeFragment);
                ivHome.setImageResource(R.mipmap.shouye_press);
                tvHome.setTextColor(getApplication().getResources().getColor(R.color.title_bg));
                currentIndex=0;
                break;
            case 1:
                if (null == lyhFragment) {
                    lyhFragment = new LYHFragment();
                    transaction.add(R.id.fg_container, lyhFragment);
                }
                transaction.show(lyhFragment);
                ivLyh.setImageResource(R.mipmap.lingyuehui_press);
                tvLyh.setTextColor(getApplication().getResources().getColor(R.color.title_bg));
                currentIndex=1;
                break;
            case 2:
                if (null == mallFragment) {
                    mallFragment = new MallFragment();
                    transaction.add(R.id.fg_container, mallFragment);
                }
                transaction.show(mallFragment);
                ivShangcheng.setImageResource(R.mipmap.shangcheng_press);
                tvShangcheng.setTextColor(getApplication().getResources().getColor(R.color.title_bg));
                currentIndex=2;
                break;
            case 3:
                if (null == agentFragment) {
                    agentFragment = new AgentFragment();
                    transaction.add(R.id.fg_container, agentFragment);
                }
                transaction.show(agentFragment);
                ivDlr.setImageResource(R.mipmap.dailiren_press);
                tvDlr.setTextColor(getApplication().getResources().getColor(R.color.title_bg));
                currentIndex=3;
                break;
        }
        transaction.commit();
    }

    private void resetImgs() {
        ivHome.setImageResource(R.mipmap.shouye_normal);
        tvHome.setTextColor(getApplication().getResources().getColor(R.color.text_black));
        ivLyh.setImageResource(R.mipmap.lingyuehui_normal);
        tvLyh.setTextColor(getApplication().getResources().getColor(R.color.text_black));
        ivShangcheng.setImageResource(R.mipmap.shangcheng_normal);
        tvShangcheng.setTextColor(getApplication().getResources().getColor(R.color.text_black));
        ivDlr.setImageResource(R.mipmap.dailiren_normal);
        tvDlr.setTextColor(getApplication().getResources().getColor(R.color.text_black));
    }

    private void hideFragments() {
        if (null != homeFragment) {
            transaction.hide(homeFragment);
        }
        if (null != lyhFragment) {
            transaction.hide(lyhFragment);
        }
        if (null != mallFragment) {
            transaction.hide(mallFragment);
        }
        if (null != agentFragment) {
            transaction.hide(agentFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 2秒以内按两次推出程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtils.showLongToast(getApplicationContext(), "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                    // 切出菜单界面
                    // layDrag.openDrawer(Gravity.LEFT);
                } else {
                    closeSelf(MainActivity.this);
//                int currentVersion = android.os.Build.VERSION.SDK_INT;
                    SystemTools.killAppDestory(MainActivity.this);
                }

                return true;
            }
        return super.onKeyDown(keyCode, event);
    }
}
