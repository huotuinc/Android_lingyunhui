package com.huotu.lingyunhui.fragment;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huotu.lingyunhui.R;

import butterknife.Bind;

public class LYHFragment extends BaseFragment {

    @Bind(R.id.web_lyh)
    WebView web_lyh;

    boolean init;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init){

             initWebView(web_lyh);
            init=true;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_lyh;
    }

    @Override
    public void loadPage() {
        web_lyh.setOnKeyListener(onKeyListener);

        web_lyh.loadUrl("http://www.baidu.com");

        web_lyh.setWebViewClient(new WebViewClient() {
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
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && web_lyh.canGoBack()) {
                web_lyh.goBack();
                return true;
            }
            return false;
        }
    };
}
