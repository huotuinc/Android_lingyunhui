package com.huotu.lingyunhui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.huotu.lingyunhui.R;

import butterknife.ButterKnife;

/**
 * Created by hzbc on 2016/5/24.
 */
public abstract class BaseFragment extends Fragment {

    private FrameLayout fgContainer;
    public View rootView;
    public Resources resources;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            resources = getActivity().getResources();
            rootView = inflater.inflate(R.layout.fragment_base, container, false);
            fgContainer = (FrameLayout) rootView.findViewById(R.id.fg_container);
            inflater.inflate(getLayoutRes(), fgContainer);
            ButterKnife.bind(BaseFragment.this, rootView);
        }
        return rootView;
    }

    public void initWebView(WebView webview){
        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setVerticalScrollBarEnabled(false);
        webview.setClickable(true);
        webview.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setSaveFormData(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setLoadWithOverviewMode(false);
        webview.getSettings().setSavePassword(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        loadPage();
    };

    public abstract int getLayoutRes();
    public abstract void loadPage();
}
