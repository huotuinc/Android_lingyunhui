package com.huotu.lingyunhui.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.AuthMallModel;
import com.huotu.lingyunhui.model.InitModel;
import com.huotu.lingyunhui.model.MSiteModel;
import com.huotu.lingyunhui.model.MenuBean;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.guide.GuideActivity;
import com.huotu.lingyunhui.ui.login.LoginActivity;
import com.huotu.lingyunhui.ui.main.MainActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.GsonRequest;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.VolleyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_iv)
    ImageView splashIv;

    @Override
    protected void initData() {
        //startAnimation();
        String url = Constants.INTERFACE_URL+"GetArvatoConfig";
        url += "?userid="+ application.readUserId()+"&unionId="+ application.readUserUnionId()+"&openId="+application.readOpenId();
        AuthParamUtils params = new AuthParamUtils(application, System.currentTimeMillis(),url,this);
        url= params.obtainUrl();
        GsonRequest<InitModel> gsonRequest = new GsonRequest<InitModel>(
                Request.Method.GET,
                url,
                InitModel.class,
                null,
                new Response.Listener<InitModel>() {
                    @Override
                    public void onResponse(InitModel response) {
                        InitModel base = response;
                        if (base.getCode()!=200||base==null||base.getData()==null) {
                            return;
                        }
                        List<MenuBean> menus = new ArrayList< MenuBean >(  );
                        MenuBean menu = null;
                        List<InitModel.MenuModel > home_menus = base.getData ().getBottomMenus();
                        for(InitModel.MenuModel home_menu:home_menus)
                        {
                            menu = new MenuBean ();
                            menu.setMenuIcon ( home_menu.getIcon ( ) );
                            menu.setMenuName ( home_menu.getName ( ) );
                            menu.setMenuUrl ( home_menu.getUrl ( ) );
                            menus.add ( menu );
                        }
                        if(null != menus && !menus.isEmpty ()) {
                            application.writeMenus(menus);
                        }
                        goNextUi();


                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLongToast(SplashActivity.this,"初始化失败");
//                goNextUi();
            }
        }
        );

        VolleyUtil.getRequestQueue().add( gsonRequest );

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        return R.layout.activity_splash;
    }

//    private void startAnimation() {
//        /*// 动画集合，一起播放
//        AnimationSet animationSet = new AnimationSet(true);
//
//        // 缩放动画
//        ScaleAnimation scaleAnimation = new ScaleAnimation(
//                0, 1,
//                0, 1,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setDuration(Contant.ANIMATION_DURATION);
//        animationSet.addAnimation(scaleAnimation);
//
//
//        // 透明度的动画
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//        alphaAnimation.setDuration(Contant.ANIMATION_DURATION);
//        animationSet.addAnimation(alphaAnimation);*/
//        AlphaAnimation anima = new AlphaAnimation(0.0f, 1.0f);
//        anima.setDuration(Constants.ANIMATION_DURATION);// 设置动画显示时间
//        splashIv.setAnimation(anima);
//        anima.setAnimationListener(animationListener);
//    }
//
//    static Handler handler = new Handler();
//    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
//        @Override
//        public void onAnimationStart(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            handler.postDelayed(goNextUiRunnable, 2000);
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }
//    };
//    private Runnable goNextUiRunnable = new Runnable() {
//
//        @Override
//        public void run() {
//            goNextUi();
//        }
//    };

    private void goNextUi() {
            //是否首次安装
            if (application.isFirst()) {
                ActivityUtils.getInstance().skipActivity(SplashActivity.this, GuideActivity.class);
                //写入初始化数据
                application.writeInitInfo("inited");
            } else {
                ActivityUtils.getInstance().skipActivity(SplashActivity.this, MainActivity.class);
            }

            }



    @Override
    protected void onStop() {
        super.onStop();
        // 防止再弹出主界面
       // handler.removeCallbacks(goNextUiRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
