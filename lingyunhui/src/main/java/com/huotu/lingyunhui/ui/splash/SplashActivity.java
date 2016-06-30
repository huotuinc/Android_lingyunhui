package com.huotu.lingyunhui.ui.splash;

import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Contant;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.guide.GuideActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_iv)
    ImageView splashIv;

    @Override
    protected void initData() {
        startAnimation();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void startAnimation() {
        /*// 动画集合，一起播放
        AnimationSet animationSet = new AnimationSet(true);

        // 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(Contant.ANIMATION_DURATION);
        animationSet.addAnimation(scaleAnimation);


        // 透明度的动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(Contant.ANIMATION_DURATION);
        animationSet.addAnimation(alphaAnimation);*/
        AlphaAnimation anima = new AlphaAnimation(0.0f, 1.0f);
        anima.setDuration(Contant.ANIMATION_DURATION);// 设置动画显示时间
        splashIv.setAnimation(anima);
        anima.setAnimationListener(animationListener);
    }

    static Handler handler = new Handler();
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            handler.postDelayed(goNextUiRunnable, 2000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    private Runnable goNextUiRunnable = new Runnable() {

        @Override
        public void run() {
            goNextUi();
        }
    };

    private void goNextUi() {
        ActivityUtils.getInstance().skipActivity(this, GuideActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 防止再弹出主界面
        handler.removeCallbacks(goNextUiRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
