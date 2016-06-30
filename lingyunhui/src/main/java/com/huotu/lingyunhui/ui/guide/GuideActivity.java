package com.huotu.lingyunhui.ui.guide;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.adapter.ViewPagerAdapter;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.main.MainActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.SystemTools;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.vp_guide)
    ViewPager vp_guide;
    @Bind(R.id.guide_bt)
    Button btn;
    //引导图片资源
    private String[] pics;
    private ArrayList<View> views;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void initData() {
        views = new ArrayList<View>();
        initImage();
        mAdapter = new ViewPagerAdapter(views);
        vp_guide.setAdapter(mAdapter);
        vp_guide.setOnPageChangeListener(mOnPageChangeListener);
        mOnPageChangeListener.onPageSelected(0);
    }

    static Handler handler = new Handler();

    private void initImage() {
        try {
            pics = resources.getAssets().list("guide");
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            pics = resources.getStringArray(R.array.guide_pic);

            //初始化引导图片列表
            for (int i = 0; i < pics.length; i++) {
                RelativeLayout iv = (RelativeLayout) LayoutInflater.from(GuideActivity.this).inflate(R.layout.guid_item, null);
                TextView skipText = (TextView) iv.findViewById(R.id.skipText);
                iv.setLayoutParams(mParams);
                int iconId = resources.getIdentifier(pics[i], "mipmap",this.getPackageName());
                Drawable menuIconDraw = resources.getDrawable(iconId);
                SystemTools.loadBackground(iv, menuIconDraw);

                //点击跳过 ，跳转到主界面
                skipText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.post(runnable);
                    }
                });
                views.add(iv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ActivityUtils.getInstance().skipActivity(GuideActivity.this, MainActivity.class);
        }
    };

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp_guide.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacks(runnable);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == pics.length - 1) {
                btn.setVisibility(View.VISIBLE);
            } else {
                btn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick(R.id.guide_bt)
    public void click(View v) {
        goToMainUI();
    }

    private void goToMainUI() {
        ActivityUtils.getInstance().skipActivity(GuideActivity.this, MainActivity.class);
    }



}
