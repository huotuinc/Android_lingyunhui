package com.huotu.lingyunhui.ui.main;



import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.listener.PoponDismissListener;
import com.huotu.lingyunhui.model.AccountModel;
import com.huotu.lingyunhui.model.PayModel;
import com.huotu.lingyunhui.model.RefreshHttpHeaderEvent;
import com.huotu.lingyunhui.model.ShareModel;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.ui.login.AutnLogin;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.HttpUtil;
import com.huotu.lingyunhui.utils.SystemTools;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.UIUtils;
import com.huotu.lingyunhui.utils.UrlFilterUtils;
import com.huotu.lingyunhui.utils.WindowUtils;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;
import com.huotu.lingyunhui.widgets.SharePopupWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class MallActivity extends BaseActivity implements Handler.Callback {
//获取资源文件对象
private Resources resources;
private long exitTime = 0l;
//handler对象
public Handler mHandler;
//windows类
private WindowManager wManager;
private SharePopupWindow share;
public ProgressPopupWindow progress;
public AssetManager am;
public static ValueCallback<Uri> mUploadMessage;
public static final int FILECHOOSER_RESULTCODE = 1;
public static final int BINDPHONE_REQUESTCODE = 1001;
private AutnLogin autnLogin;

//标题栏布局对象
@Bind(R.id.titleLayout)
RelativeLayout homeTitle;
//标题栏左侧图标
@Bind(R.id.titleLeftImage)
ImageView titleLeftImage;
//标题栏标题文字
@Bind(R.id.titleText)
TextView titleText;
//标题栏右侧图标
@Bind(R.id.titleRightImage)
ImageView titleRightImage;
//web视图
public WebView pageWeb;
//单独加载菜单
@Bind(R.id.menuPage)
WebView menuView;
ImageView loginSetting;
//主菜单容器
@Bind(R.id.mainMenuLayout)
LinearLayout mainMenuLayout;
//已授权界面

@Bind(R.id.viewPage)
PullToRefreshWebView refreshWebView;

@Bind(R.id.main_pgbar)
ProgressBar pgBar;



@Override
protected void onResume() {
        super.onResume();
        }


@Override
protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        initRedrectUrl(intent);
        }


/***
 * 判断 登录以后，是否需要调转
 */
protected void initRedrectUrl( Intent intent ){
        String redirecturl="";
        if( intent.hasExtra("redirecturl")){
        redirecturl = intent.getStringExtra("redirecturl");
        }
        if( TextUtils.isEmpty(redirecturl) ) return;

        pageWeb.loadUrl( redirecturl );
        }



@Override
protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if( progress !=null ){
        progress.dismissView();
        progress=null;
        }
        if( share !=null){
        share.dismiss();
        share=null;
        }
        if( pageWeb !=null ){
        pageWeb.setVisibility(View.GONE);
        }
        if(menuView !=null){
        menuView.setVisibility(View.GONE);
        }

        UnRegister();

        if(mHandler!=null){
        mHandler.removeCallbacksAndMessages(null);
        }
        }


protected void initView ( ) {

        //动态加载侧滑菜单
        UIUtils ui = new UIUtils(application, MallActivity.this, resources, mainMenuLayout, mHandler);
        ui.loadMenus();
        //监听web控件
        pageWeb = refreshWebView.getRefreshableView();
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
@Override
public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
        //刷新界面
        pageWeb.reload();
        }
        });


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
        //ToastUtils.showLongToast(HomeActivity.this, throwable.getMessage());
        mHandler.sendMessage(msg);
        }

@Override
public void onCancel(Platform platform, int i) {
        Message msg = Message.obtain();
        msg.what = Constants.SHARE_CANCEL;
        msg.obj = platform;
        mHandler.sendMessage(msg);
        }
        }
        );
        share.showShareWindow();
        share.setOnDismissListener(new PoponDismissListener(this));

        loadPage();
        loadMainMenu();
        }







private void loadMainMenu() {
        menuView.getSettings().setJavaScriptEnabled(true);
        menuView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //
//        String userAgent = menuView.getSettings().getUserAgentString();
//        if( TextUtils.isEmpty(userAgent) ) {
//            userAgent = "mobile";
//        }else{
//            userAgent +=";mobile";
//        }
//        menuView.getSettings().setUserAgentString( userAgent );
        signHeader( menuView );

        //首页默认为商户站点 + index
        String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        menuView.loadUrl(menuUrl);

        menuView.setWebViewClient(
        new WebViewClient() {
//重写此方法，浏览器内部跳转
public boolean shouldOverrideUrlLoading( WebView view, String url ) {
        if( pageWeb ==null ) return true;

        AuthParamUtils paramUtils = new AuthParamUtils(BaseApplication.app, System.currentTimeMillis(), url, MallActivity.this);
        String urlStr = paramUtils.obtainUrl();

        pageWeb.loadUrl(urlStr);
        return true;
        }

@Override
public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        }

@Override
public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        }
        }
        );
        }

private void signHeader( WebView webView ){
        String userid= application.readMemberId();
        String unionid = application.readUserUnionId();
        String openId = BaseApplication.app.readOpenId();
        String sign = AuthParamUtils.SignHeaderString(userid, unionid , openId );
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
        webView.getSettings().setUserAgentString( userAgent );
        }

private void loadPage(){
        pageWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pageWeb.setVerticalScrollBarEnabled(false);
        pageWeb.setClickable(true);
        //设置app标志
//        String userAgent = pageWeb.getSettings().getUserAgentString();
//        if( TextUtils.isEmpty(userAgent) ) {
//            userAgent = "mobile";
//        }else{
//            userAgent +=";mobile";
//        }
//        pageWeb.getSettings().setUserAgentString(userAgent);
        signHeader( pageWeb );
        //
        pageWeb.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作
        pageWeb.getSettings().setSupportZoom(true);
        pageWeb.getSettings().setBuiltInZoomControls(true);
        pageWeb.getSettings().setJavaScriptEnabled(true);
        pageWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        pageWeb.getSettings().setSaveFormData(true);
        pageWeb.getSettings().setAllowFileAccess(true);
        pageWeb.getSettings().setLoadWithOverviewMode(false);
        pageWeb.getSettings().setSavePassword(true);
        pageWeb.getSettings().setLoadsImagesAutomatically(true);
        pageWeb.getSettings().setDomStorageEnabled(true);
        pageWeb.getSettings().setAppCacheEnabled(true);
        pageWeb.getSettings().setDatabaseEnabled(true);
     String dir = BaseApplication.app.getDir("database", Context.MODE_PRIVATE).getPath();
        pageWeb.getSettings().setGeolocationDatabasePath(dir);
        pageWeb.getSettings().setGeolocationEnabled(true);

        pageWeb.addJavascriptInterface( MallActivity.this ,"android");
        //首页鉴权
        AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), MallActivity.this );
        String url = paramUtils.obtainUrl ();
        //首页默认为商户站点 + index
        pageWeb.loadUrl(url);

        pageWeb.setWebViewClient(
        new WebViewClient() {
//重写此方法，浏览器内部跳转
public boolean shouldOverrideUrlLoading( WebView view, String url ) {

        UrlFilterUtils filter = new UrlFilterUtils( MallActivity.this, mHandler, application  );
        return filter.shouldOverrideUrlBySFriend(pageWeb, url);
        }

@Override
public void onPageStarted(WebView view, String url, Bitmap favicon) {

//                        if( titleRightImage !=null){
//                            titleRightImage.setVisibility(View.GONE);
//                        }

        super.onPageStarted(view, url, favicon);
        }

@Override
public void onPageFinished(WebView view, String url) {
        //页面加载完成后,读取菜单项
        super.onPageFinished(view, url);
        if( titleText ==null || pageWeb ==null ) return;

        titleText.setText(view.getTitle());

        //boolean r = url.startsWith( application.obtainMerchantUrl() );



        }

@Override
public void onReceivedError( WebView view, int errorCode, String description,String failingUrl ){
        super.onReceivedError(view, errorCode, description, failingUrl);
        if( refreshWebView ==null) return;
        refreshWebView.onRefreshComplete();

        if( pgBar == null) return;
        pgBar.setVisibility(View.GONE);
        }
        }
        );

        pageWeb.setWebChromeClient(new WebChromeClient()
        {
@Override
public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if( titleText ==null) return;
        titleText.setText(title);
        }

@Override
public void onProgressChanged(WebView view, int newProgress) {
        if( refreshWebView ==null || pgBar ==null ) return;

        if(100 == newProgress) {
        refreshWebView.onRefreshComplete();

        pgBar.setVisibility(View.GONE);
        }else {
        if (pgBar.getVisibility() == View.GONE) pgBar.setVisibility(View.VISIBLE);
        pgBar.setProgress(newProgress);
        }

        super.onProgressChanged(view, newProgress);
        }

public void openFileChooser(ValueCallback<Uri> uploadMsg) {
    MallActivity.mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        MallActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MallActivity.FILECHOOSER_RESULTCODE);
        }

public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
        openFileChooser(uploadMsg);
        }

//For Android 4.1
public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){

        openFileChooser(uploadMsg);

        }

@Override
public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke( origin, true, false );
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        });

        }

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
        if (null == mUploadMessage)
        return;
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
        }else if( requestCode == BINDPHONE_REQUESTCODE && resultCode == RESULT_OK){
        UIUtils ui = new UIUtils(application, MallActivity.this, resources, mainMenuLayout, mHandler);
        ui.loadMenus();
        }
        }

@Override
public boolean dispatchKeyEvent(KeyEvent event){
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
        if(pageWeb.canGoBack ()){
        titleRightImage.setVisibility(View.GONE);
        pageWeb.goBack ( );
        }
        else{
        finish();
        }





        return true;
        }
        return super.dispatchKeyEvent ( event );
        }

@OnClick(R.id.titleLeftImage)
void doBackOrMenuClick() {

    if (pageWeb.canGoBack()) {
        pageWeb.goBack();
    } else {
        finish();
    }
}




/**
 *
 */
protected void getShareContentByJS(){
        pageWeb.loadUrl("javascript:__getShareStr();");
        }

@OnClick(R.id.titleRightImage)
void doShare_old(){
        String sourceUrl = pageWeb.getUrl();
        if( !TextUtils.isEmpty( sourceUrl )){
        try {
        URI u = URI.create(sourceUrl);
        String path = u.getPath().toLowerCase().trim();

        int idx = path.lastIndexOf("/");
        String fileName = path.substring( idx + 1);

        if (fileName.equals("view.aspx")) {
        progress.showProgress("请稍等...");
        progress.showAtLocation(titleLeftImage, Gravity.CENTER, 0, 0);
        getShareContentByJS();
        return;
        }else if( fileName.equals("inviteOpenShop".toLowerCase())){
        progress.showProgress("请稍等...");
        progress.showAtLocation(titleLeftImage, Gravity.CENTER, 0, 0);
        getShareContentByJS();
        return;
        }
        }catch (Exception ex){
        }
        }

        String text = application.obtainMerchantName ()+"分享";
        String imageurl = application.obtainMerchantLogo ();
        if(!imageurl.contains ( "http://" ))
        {
        //加上域名
        imageurl = application.obtainMerchantUrl () + imageurl;
        }
        else if(TextUtils.isEmpty ( imageurl ))
        {
        imageurl = Constants.COMMON_SHARE_LOGO;
        }
        String title = application.obtainMerchantName ()+"分享";
        String url = null;
        url = pageWeb.getUrl();
        if( url == null ){
        url = application.obtainMerchantUrl();
        }
        url = SystemTools.shareUrl(application, url);
        ShareModel msgModel = new ShareModel();
        msgModel.setImageUrl(imageurl);
        msgModel.setText(text);
        msgModel.setTitle(title);
        msgModel.setUrl(url);
        share.initShareParams(msgModel);
        WindowUtils.backgroundAlpha( MallActivity.this , 0.4f );
        share.showAtLocation(titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

@OnClick(R.id.titleRightImage)
void doShare(){
        progress.showProgress("请稍等...");
        progress.showAtLocation(titleLeftImage, Gravity.CENTER, 0, 0);
        getShareContentByJS();
        }


@Override
public boolean handleMessage ( Message msg ) {
        switch ( msg.what )
        {
        //加载页面
        case Constants.LOAD_PAGE_MESSAGE_TAG:
        {//加载菜单页面

        String url = msg.obj.toString ();

        pageWeb.loadUrl(url);

        }
        break;
        case Constants.FRESHEN_PAGE_MESSAGE_TAG:
        {
        //刷新界面
        String url = msg.obj.toString ();
        pageWeb.loadUrl(url);
        }
        break;
        //分享
        case Constants.SHARE_SUCCESS:
        {
        //分享成功
        Platform platform = ( Platform ) msg.obj;
        if("WechatMoments".equals ( platform.getName () )) {
        ToastUtils.showShortToast ( MallActivity.this, "微信朋友圈分享成功" );
        }
        else if("Wechat".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "微信分享成功" );
        }
        else if("QZone".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "QQ空间分享成功" );
        }
        else if( platform.getName ().equals(SinaWeibo.NAME))
        {
        ToastUtils.showShortToast ( MallActivity.this, "新浪微博分享成功" );
        }
        }
        break;
        case Constants.SHARE_ERROR:
        {
        //分享失败
        Platform platform = ( Platform ) msg.obj;
        if("WechatMoments".equals ( platform.getName () )) {
        ToastUtils.showShortToast ( MallActivity.this, "微信朋友圈分享失败" );
        }
        else if("Wechat".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "微信分享失败" );
        }
        else if("QZone".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "QQ空间分享失败" );
        }
        else if(  platform.getName ().equals( SinaWeibo.NAME ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "新浪微博分享失败" );
        }
        }
        break;
        case Constants.SHARE_CANCEL:
        {
        //分享取消
        Platform platform = ( Platform ) msg.obj;
        if("WechatMoments".equals ( platform.getName () )) {
        ToastUtils.showShortToast ( MallActivity.this, "微信朋友圈分享取消" );
        }
        else if("Wechat".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "微信分享取消" );
        }
        else if("QZone".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "QQ空间分享取消" );
        }
        else if("SinaWeibo".equals ( platform.getName () ))
        {
        ToastUtils.showShortToast ( MallActivity.this, "新浪微博分享取消" );
        }
        }
        break;
        case Constants.MSG_AUTH_COMPLETE:
        {//提示授权成功
        Platform plat = ( Platform ) msg.obj;
        autnLogin.authorize(plat);
        }
        break;
        case Constants.LOGIN_AUTH_ERROR:
        {//授权登录 失败
        titleLeftImage.setClickable ( true );
        progress.dismissView();
        ToastUtils.showShortToast(this, "授权失败");
        }
        break;
        case Constants.MSG_AUTH_ERROR:
        {//
        titleLeftImage.setClickable ( true );
        progress.dismissView ();
        Throwable throwable = ( Throwable ) msg.obj;
        //if("cn.sharesdk.wechat.utils.WechatClientNotExistException".equals ( throwable.toString () ))
        if( throwable instanceof cn.sharesdk.wechat.utils.WechatClientNotExistException ){
        ToastUtils.showShortToast(this,"请安装微信客户端，在进行绑定操作");
        }else{
        ToastUtils.showShortToast(this,"微信绑定失败");
        }
        }
        break;
        case Constants.MSG_AUTH_CANCEL:
        {
        if( progress!=null ){
        progress.dismissView();
        }
        ToastUtils.showShortToast(this,"你已经取消微信授权，绑定操作失败");
        }
        break;
        case Constants.MSG_USERID_FOUND:
        {
        progress.showProgress ( "已经获取微信的用户信息" );
        progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
        break;
        case Constants.MSG_LOGIN:
        {
        progress.dismissView();
        AccountModel account = (AccountModel) msg.obj;
        }
        break;
        case Constants.PAY_NET:
        {
        PayModel payModel = ( PayModel ) msg.obj;
        //调用JS
        pageWeb.loadUrl("javascript:utils.Go2Payment(" + payModel.getCustomId() + "," + payModel.getTradeNo() + "," + payModel.getPaymentType() + ", " + "false);\n");
        }
default:
        break;
        }
        return false;
        }

protected void dealUserid(){
        pageWeb.clearHistory();
        pageWeb.clearCache(true);
        AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), MallActivity.this );
        String url = paramUtils.obtainUrl ();
        //首页默认为商户站点 + index
        pageWeb.loadUrl(url);


        return;
        }










    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link,final String img_url){
        if(  this==null ) return;
        if( this.share ==null ) return;

        //ToastUtils.showShortToast( ref.get() , title+desc+link+img_url);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( MallActivity.this ==null ) return;

                if( MallActivity.this.progress !=null ){
                    MallActivity.this.progress.dismissView();
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
                sLink = SystemTools.shareUrl(application, sLink);
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData( BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ) );
                share.initShareParams(msgModel);
                //share.showShareWindow();
                WindowUtils.backgroundAlpha( MallActivity.this , 0.4f);
                share.showAtLocation( MallActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

    @JavascriptInterface
    public void sendSisShare(final String title, final String desc, final String link,final String img_url){
        if(  this==null ) return;
        if( this.share ==null ) return;

        //ToastUtils.showShortToast( ref.get() , title+desc+link+img_url);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( MallActivity.this ==null ) return;

                if( MallActivity.this.progress !=null ){
                    MallActivity.this.progress.dismissView();
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
                sLink = SystemTools.shareUrl(application, sLink);
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData( BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ) );
                share.initShareParams(msgModel);
                //share.showShareWindow();
                WindowUtils.backgroundAlpha( MallActivity.this , 0.4f);
                share.showAtLocation( MallActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }



    @Override
    protected void initData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        resources = MallActivity.this.getResources();
        mHandler = new Handler ( this );
        share = new SharePopupWindow ( MallActivity.this );
        wManager = this.getWindowManager();
        am = this.getAssets();

        ButterKnife.bind(this);

        Register();
            setImmerseLayout1(homeTitle);
            initView();


            initRedrectUrl(getIntent());

            progress = new ProgressPopupWindow ( MallActivity.this );

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
       return R.layout.activity_mall;
    }
}
