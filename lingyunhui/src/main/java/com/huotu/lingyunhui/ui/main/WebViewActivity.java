package com.huotu.lingyunhui.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.listener.PoponDismissListener;
import com.huotu.lingyunhui.model.PayModel;
import com.huotu.lingyunhui.model.ShareModel;
import com.huotu.lingyunhui.receiver.MyBroadcastReceiver;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.SystemTools;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.UrlFilterUtils;
import com.huotu.lingyunhui.utils.WindowUtils;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;
import com.huotu.lingyunhui.widgets.SharePopupWindow;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 单张展示web页面
 */
public class WebViewActivity extends BaseActivity implements Handler.Callback, MyBroadcastReceiver.BroadcastListener {
    //获取资源文件对象
    private Resources resources;
    private Handler mHandler;
    //web视图
    private WebView viewPage;
    private String url;
    private SharePopupWindow share;
    private MyBroadcastReceiver myBroadcastReceiver;
    //tilte组件
    @Bind(R.id.newtitleLayout)
    RelativeLayout newtitleLayout;
    //标题栏左侧图标
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    //标题栏标题文字
    @Bind(R.id.titleText)
    TextView titleText;
    //标题栏右侧图标
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;
    @Bind(R.id.viewPage)
    PullToRefreshWebView refreshWebView;

    ProgressPopupWindow progress;

    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    @Bind(R.id.statuslayout)
    RelativeLayout statuslayout;


    @Override
    protected void initData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        resources = this.getResources ( );
        ButterKnife.bind(this);
        setImmerseLayout1(newtitleLayout);
        mHandler = new Handler( this );
        progress = new ProgressPopupWindow ( WebViewActivity.this );
        share = new SharePopupWindow ( WebViewActivity.this );
        myBroadcastReceiver = new MyBroadcastReceiver(WebViewActivity.this,this, MyBroadcastReceiver.ACTION_PAY_SUCCESS);
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString ( Constants.INTENT_URL );
        initView();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.new_load_page;
    }


    private void initView() {
        //设置title背景

        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.mipmap.main_title_left_back );
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.mipmap.home_title_right_share );
        SystemTools.loadBackground(titleRightImage, rightDraw);
        titleRightImage.setVisibility(View.GONE);

        viewPage = refreshWebView.getRefreshableView();
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                if (viewPage == null) return;
                viewPage.reload();
            }
        });
        loadPage();

        share.showShareWindow();
        share.setPlatformActionListener(
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_SUCCESS;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_ERROR;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_CANCEL;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }
                });
        share.setOnDismissListener(new PoponDismissListener(WebViewActivity.this));
    }

    private void signHeader( WebView webView ){
        String userid= application.readMemberId();
        String unionid = application.readUserUnionId();
        String openId = BaseApplication.app.readOpenId();
        String sign = AuthParamUtils.SignHeaderString(userid, unionid , openId);
        String userAgent = webView.getSettings().getUserAgentString();
        if( TextUtils.isEmpty(userAgent) ) {
            userAgent = "mobile;"+sign;
        }else{
            int idx = userAgent.lastIndexOf(";mobile;hottec:");
            if(idx>=0){
                userAgent = userAgent.substring(0,idx);
            }
            userAgent +=";mobile;"+sign;
        }
        webView.getSettings().setUserAgentString(userAgent);
    }

    private void loadPage(){
        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        viewPage.setVerticalScrollBarEnabled(false);
        viewPage.setClickable(true);
        viewPage.getSettings().setUseWideViewPort(true);


        signHeader( viewPage );

        //是否需要避免页面放大缩小操作
        viewPage.getSettings().setSupportZoom(true);
        viewPage.getSettings().setBuiltInZoomControls(true);
        viewPage.getSettings().setJavaScriptEnabled(true);
        viewPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        viewPage.getSettings().setSaveFormData(true);
        viewPage.getSettings().setAllowFileAccess(true);
        viewPage.getSettings().setLoadWithOverviewMode(false);
        viewPage.getSettings().setSavePassword(true);
        viewPage.getSettings().setLoadsImagesAutomatically(true);
        viewPage.getSettings().setDomStorageEnabled(true);
        viewPage.getSettings().setAppCacheEnabled(true);
        viewPage.getSettings().setDatabaseEnabled(true);
        String dir = BaseApplication.app.getDir("database", Context.MODE_PRIVATE).getPath();
        viewPage.getSettings().setGeolocationDatabasePath(dir);
        viewPage.getSettings().setGeolocationEnabled(true);
        viewPage.addJavascriptInterface(this, "android");
        viewPage.loadUrl(url);

        viewPage.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (titleText == null) return false;


                        UrlFilterUtils filter = new UrlFilterUtils(WebViewActivity.this, mHandler, application);
                        return filter.shouldOverrideUrlBySFriend(viewPage, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        if( titleRightImage !=null  ) {
                            titleRightImage.setVisibility(View.GONE);
                        }

                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (titleText == null) return;
                        titleText.setText(view.getTitle());
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        if (refreshWebView == null) return;
                        refreshWebView.onRefreshComplete();

                        if (pgBar == null) return;
                        pgBar.setVisibility(View.GONE);

                        if (progress == null) return;
                        progress.dismissView();
                    }
                }
        );

        viewPage.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                if (titleText == null) {
                    return;
                }
                if (title == null) {
                    return;
                }

                titleText.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (refreshWebView == null || pgBar == null) return;
                if (100 == newProgress) {
                    refreshWebView.onRefreshComplete();
                    pgBar.setVisibility(View.GONE);
                } else {
                    if (pgBar.getVisibility() == View.GONE) pgBar.setVisibility(View.VISIBLE);
                    pgBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                MainActivity.mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                openFileChooser(uploadMsg);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke( origin , true ,false );
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
    }

    @OnClick(R.id.titleLeftImage)
    void doBack(){
        WebViewActivity.this.finish();
    }

    /**
     * 通过调用javascript代码获得 分享的相关内容
     */
    protected void getShareContentByJS(){
        viewPage.loadUrl("javascript:__getShareStr();");
    }

    @OnClick(R.id.titleRightImage)
    void doShare_old(){
        String sourceUrl = viewPage.getUrl();
        if( !TextUtils.isEmpty( sourceUrl )) {
            Uri u = Uri.parse( sourceUrl );
            String path = u.getPath().toLowerCase().trim();
            int idx = path.lastIndexOf("/");
            String fileName = path.substring(idx + 1);

            if( fileName.equals("view.aspx") ) {//商品详细界面
                progress.showProgress("请稍等...");
                progress.showAtLocation( getWindow().getDecorView() , Gravity.CENTER, 0, 0);
                getShareContentByJS();
                return;
            }else if( fileName.equals("inviteOpenShop".toLowerCase())){
                progress.showProgress("请稍等...");
                progress.showAtLocation(getWindow().getDecorView() , Gravity.CENTER, 0, 0);
                getShareContentByJS();
                return;
            }
        }

        String text = application.obtainMerchantName ()+"分享";
        String imageurl = application.obtainMerchantLogo ();
        if(!imageurl.contains ( "http://" ))
        {
            //加上域名
            imageurl = application.obtainMerchantUrl () + imageurl;
        }
        else if( TextUtils.isEmpty ( imageurl ))
        {
            imageurl = Constants.COMMON_SHARE_LOGO;
        }
        String title = application.obtainMerchantName ()+"分享";
        String url = null;
        url = viewPage.getUrl();
        ShareModel msgModel = new ShareModel ();
        msgModel.setImageUrl(imageurl);
        msgModel.setText(text);
        msgModel.setTitle(title);
        msgModel.setUrl(url);
        share.initShareParams(msgModel);
        share.showAtLocation(titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @OnClick(R.id.titleRightImage)
    void doShare(){
        progress.showProgress("请稍等...");
        progress.showAtLocation( getWindow().getDecorView() , Gravity.CENTER, 0, 0);
        getShareContentByJS();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            closeSelf(WebViewActivity.this);
            return true;
        }
        return super.dispatchKeyEvent ( event );
    }

    @Override
    public boolean handleMessage ( Message msg ) {
        switch (msg.what) {
            //分享
            case Constants.SHARE_SUCCESS: {
                //分享成功
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享成功");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享成功");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享成功");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享成功");
                }
            }
            break;
            case Constants.SHARE_ERROR: {
                //分享失败
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享失败");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享失败");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享失败");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享失败");
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享取消");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享取消");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享取消");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享取消");
                }
            }
            break;
//            case AliPayUtil.SDK_PAY_FLAG: {
//                PayGoodBean payGoodBean = ( PayGoodBean ) msg.obj;
//                String tag = payGoodBean.getTag ( );
//                String[] tags = tag.split ( ";" );
//                for ( String str:tags )
//                {
//                    if(str.contains ( "resultStatus" ))
//                    {
//                        String code = str.substring ( str.indexOf ( "{" )+1, str.indexOf ( "}" ) );
//                        if(!"9000".equals ( code ))
//                        {
//                            //支付宝支付信息提示
//                            ToastUtils.showShortToast ( WebViewActivity.this, "支付宝支付失败，code:"+code );
//                        }
//                    }
//                }
//            }
//            break;
            case Constants.PAY_NET: {
                PayModel payModel = (PayModel) msg.obj;
                //调用JS
                viewPage.loadUrl("javascript:utils.Go2Payment(" + payModel.getCustomId() + "," + payModel.getTradeNo() + "," + payModel.getPaymentType() + ", "
                        + "false);\n");
            }
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );
        ButterKnife.unbind(this);
        if( null != myBroadcastReceiver){
            myBroadcastReceiver.unregisterReceiver();
        }
        if( viewPage !=null ){
            viewPage.setVisibility(View.GONE);
        }
        if( progress !=null){
            progress.dismissView();
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFinishReceiver ( MyBroadcastReceiver.ReceiverType type, Object msg ) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess){
            viewPage.goBack();
        }
    }

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( WebViewActivity.this ==null ) return;
                if( progress!=null ){
                    progress.dismissView();
                }

                String sTitle = title;
                if( TextUtils.isEmpty( sTitle ) ){
                    sTitle = application.obtainMerchantName ()+"分享";
                }
                String sDesc = desc;
                if( TextUtils.isEmpty( sDesc ) ){
                    sDesc = sTitle;
                }
                String imageUrl = img_url; //application.obtainMerchantLogo ();
                if(TextUtils.isEmpty ( imageUrl )) {
                    imageUrl = Constants.COMMON_SHARE_LOGO;
                }

                String sLink = link;
                if( TextUtils.isEmpty( sLink ) ){
                    sLink = application.obtainMerchantUrl();
                }
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                share.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( WebViewActivity.this , 0.4f);
                share.showAtLocation( WebViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

    @JavascriptInterface
    public void sendSisShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        //ToastUtils.showLongToast( getApplicationContext() , "title:"+title +", desc:"+ desc );

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( WebViewActivity.this ==null ) return;
                if( progress!=null ){
                    progress.dismissView();
                }

                String sTitle = title;
                if( TextUtils.isEmpty( sTitle ) ){
                    sTitle = application.obtainMerchantName ()+"分享";
                }
                String sDesc = desc;
                if( TextUtils.isEmpty( sDesc ) ){
                    sDesc = sTitle;
                }
                String imageUrl = img_url; //application.obtainMerchantLogo ();
                if(TextUtils.isEmpty ( imageUrl )) {
                    imageUrl = Constants.COMMON_SHARE_LOGO;
                }

                String sLink = link;
                if( TextUtils.isEmpty( sLink ) ){
                    sLink = application.obtainMerchantUrl();
                }
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                share.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( WebViewActivity.this , 0.4f);
                share.showAtLocation( WebViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

//    @JavascriptInterface
//    public void enableShare(){
//        titleRightImage.setVisibility(View.VISIBLE);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventClose(CloseEvent event){
//        this.finish();
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventRefreshHttpHeader(RefreshHttpHeaderEvent event){
//        if( viewPage==null) return;
//        signHeader(viewPage);
//    }
}
