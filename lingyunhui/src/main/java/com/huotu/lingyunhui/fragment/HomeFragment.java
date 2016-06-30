package com.huotu.lingyunhui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.model.NewsColumn;
import com.huotu.lingyunhui.widgets.EditDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hzbc on 2016/5/23.
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;
    @Bind(R.id.iv_select)
    ImageView ivSelect;
    @Bind(R.id.tv_position)
    TextView tvPosition;
    @Bind(R.id.web_home)
    WebView web_home;

    boolean init;
    private List<NewsColumn> titles;
    private List<NewsColumn> lists;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init) {
            initView();
            initData();
            initTab();
            initWebView(web_home);
            init = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        titles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NewsColumn title = new NewsColumn("总部的新闻类别", "www.baidu.com");
            titles.add(title);
            List<NewsColumn> list = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                NewsColumn news = new NewsColumn("全国活动", "www.baidu.com");
                list.add(news);
            }
            title.setList(list);
        }

        lists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NewsColumn title = new NewsColumn("最新" + i, "www.baidu.com");
            lists.add(title);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    private void initView() {
    }

    private void initTab() {
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tabLayout.newTab().setText(lists.get(i).getTitle());
            tabLayout.addTab(tab);
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);
    }

   /* private void initWebView() {
        loadPage();
    }*/

    /*private void loadPage() {
        web_home.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        web_home.setVerticalScrollBarEnabled(false);
        web_home.setClickable(true);
        web_home.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作
        web_home.getSettings().setSupportZoom(true);
        web_home.getSettings().setBuiltInZoomControls(true);
        web_home.getSettings().setJavaScriptEnabled(true);
        web_home.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web_home.getSettings().setSaveFormData(true);
        web_home.getSettings().setAllowFileAccess(true);
        web_home.getSettings().setLoadWithOverviewMode(false);
        web_home.getSettings().setSavePassword(true);
        web_home.getSettings().setLoadsImagesAutomatically(true);
        web_home.getSettings().setDomStorageEnabled(true);

        web_home.setOnKeyListener(onKeyListener);

        web_home.loadUrl("http://www.sina.com");

        web_home.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }*/
    @Override
    public void loadPage() {
        web_home.setOnKeyListener(onKeyListener);

        web_home.loadUrl("http://www.sina.com");

        web_home.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (tvPosition != null)
                tvPosition.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_selector)
    public void select(View view) {
        final EditDialog dialog = new EditDialog(getActivity());

        ivSelect.setBackgroundResource(R.mipmap.pull);
        dialog.showNewsColumn(titles);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ivSelect.setBackgroundResource(R.mipmap.down);
            }
        });
        dialog.show();
    }

    @OnClick(R.id.ll_search)
    public void search(View view) {
        Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Toast.makeText(getActivity(), "" + tab.getText(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    //重写返回键
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && web_home.canGoBack()) {
                web_home.goBack();
                return true;
            }
            return false;
        }
    };

    //不同界面之间的数据传递
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(String cityCode) {
        logMsg(cityCode);
    }
}
