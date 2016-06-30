package com.huotu.lingyunhui.fragment;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huotu.lingyunhui.R;

import butterknife.Bind;

public class MallFragment extends BaseFragment {

    @Bind(R.id.web_mall)
    WebView web_mall;

    boolean init;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init){
            initWebView(web_mall);

            init=true;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mall;
    }

    @Override
    public void loadPage() {
        web_mall.setOnKeyListener(onKeyListener);

        web_mall.loadUrl("http://www.hao123.com");

        web_mall.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    //重写返回键
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && web_mall.canGoBack()) {
                web_mall.goBack();
                return true;
            }
            return false;
        }
    };
}
