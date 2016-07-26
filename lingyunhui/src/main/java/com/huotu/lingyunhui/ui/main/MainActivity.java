package com.huotu.lingyunhui.ui.main;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.fragment.AgentFragment;
import com.huotu.lingyunhui.fragment.HomeFragment;
import com.huotu.lingyunhui.fragment.LYHFragment;
import com.huotu.lingyunhui.fragment.MallFragment;
import com.huotu.lingyunhui.listener.LocationListener;
import com.huotu.lingyunhui.model.AccountModel;
import com.huotu.lingyunhui.model.MenuBean;
import com.huotu.lingyunhui.model.PayModel;
import com.huotu.lingyunhui.model.PhoneLoginModel;
import com.huotu.lingyunhui.service.LocationService;
import com.huotu.lingyunhui.ui.base.BaseActivity;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.ui.login.AutnLogin;
import com.huotu.lingyunhui.ui.login.LoginActivity;
import com.huotu.lingyunhui.utils.ActivityUtils;
import com.huotu.lingyunhui.utils.AuthParamUtils;
import com.huotu.lingyunhui.utils.GsonRequest;
import com.huotu.lingyunhui.utils.KJLoger;
import com.huotu.lingyunhui.utils.SystemTools;
import com.huotu.lingyunhui.utils.ToastUtils;
import com.huotu.lingyunhui.utils.UIUtils;
import com.huotu.lingyunhui.utils.UrlFilterUtils;
import com.huotu.lingyunhui.utils.VolleyUtil;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;
import com.huotu.lingyunhui.widgets.SharePopupWindow;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class MainActivity extends BaseActivity implements Handler.Callback {


    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    //标题栏布局对象
    @Bind(R.id.titleLayoutL)
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
    @Bind(R.id.viewPage)
    PullToRefreshWebView refreshWebView;
    @Bind(R.id.mainMenuLayout)
    LinearLayout mainMenuLayout;
    public static final int FILECHOOSER_RESULTCODE = 1;
    //web视图
    public WebView pageWeb;
    private LocationService locationService;
    private LocationListener locationListener;
    private long exitTime = 0l;
    private Handler mHandler;
    public static ValueCallback< Uri > mUploadMessage;
    private AutnLogin autnLogin;
    private SharePopupWindow share;
    public ProgressPopupWindow progress;

    @Override
    protected void initData() {
        UIUtils ui = new UIUtils(application, MainActivity.this, resources, mainMenuLayout, mHandler);
        ui.loadMenus();

        mainMenuLayout.getChildAt(0).performClick();

        pageWeb = refreshWebView.getRefreshableView();
        progress = new ProgressPopupWindow ( MainActivity.this );
        share = new SharePopupWindow ( MainActivity.this );
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                //刷新界面
                pageWeb.reload();
            }
        });
        loadPage();
        //设置沉浸模式
        setImmerseLayout(this.findViewById(R.id.titleLayoutL));
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        mHandler = new Handler ( this );
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


    @SuppressLint("JavascriptInterface")
    private void loadPage(){
        pageWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pageWeb.setVerticalScrollBarEnabled(false);
        pageWeb.setClickable(true);
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

        pageWeb.addJavascriptInterface( MainActivity.this ,"android");
        //首页鉴权
//        AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), MainActivity.this );
//        String url = ;
        //首页默认为商户站点 + index
//        pageWeb.loadUrl(url);

        pageWeb.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading( WebView view, String url ) {

                        UrlFilterUtils filter = new UrlFilterUtils( MainActivity.this, mHandler, application  );
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

//                        if(url.contains ( "&back" ) || url.contains ( "?back" )){
//                            mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
//                        }
//                        else {
//                            if ( pageWeb.canGoBack ( ) ) {
//                                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
//                            }
//                            else {
//                                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
//                            }
//                        }
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
                MainActivity.mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);
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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case Constants.LOAD_PAGE_MESSAGE_TAG:
            {//加载菜单页面

                String url = msg.obj.toString ();
//                if( url.toLowerCase().contains("http://www.bindweixin.com") ){
//                    //绑定微信
//                    callWeixin();
//                }
//                else if( url.toLowerCase().trim().contains("http://www.bindphone.com") ){
//                    //绑定手机
//                    callPhone();
//                }
//                else if( url.toLowerCase().contains("http://www.dzd.com") ){
//                    openSis();
//                }else {
                    pageWeb.loadUrl(url);
//                }
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
                    ToastUtils.showShortToast ( MainActivity.this, "微信朋友圈分享成功" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "微信分享成功" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "QQ空间分享成功" );
                }
                else if( platform.getName ().equals(SinaWeibo.NAME))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "新浪微博分享成功" );
                }
            }
            break;
            case Constants.SHARE_ERROR:
            {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( MainActivity.this, "微信朋友圈分享失败" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "微信分享失败" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "QQ空间分享失败" );
                }
                else if(  platform.getName ().equals( SinaWeibo.NAME ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "新浪微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL:
            {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( MainActivity.this, "微信朋友圈分享取消" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "微信分享取消" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "QQ空间分享取消" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( MainActivity.this, "新浪微博分享取消" );
                }
            }
            break;
//            case Constants.LEFT_IMG_SIDE:
//            {
//                //设置左侧图标
//                Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_sideslip );
//                SystemTools.loadBackground ( titleLeftImage, leftDraw );
//                application.isLeftImg = true;
//            }
//            break;
//            case Constants.LEFT_IMG_BACK:
//            {
//                //设置左侧图标
//                Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_back );
//                SystemTools.loadBackground ( titleLeftImage, leftDraw );
//                application.isLeftImg = false;
//            }
//            break;
//            case Constants.SWITCH_USER_NOTIFY:
//            {
//                SwitchUserModel.SwitchUser user = ( SwitchUserModel.SwitchUser ) msg.obj;
//                //更新userId
//                application.writeMemberId(String.valueOf(user.getUserid()));
//                //更新昵称
//                application.writeUserName(user.getWxNickName());
//                application.writeUserIcon(user.getWxHeadImg());
//                application.writeUserUnionId( user.getWxUnionId() );
//                application.writeMemberLevel(user.getLevelName());
//
//                //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
//                application.writeMemberRelatedType(user.getRelatedType());
//
//                //更新界面
//                userName.setText(user.getWxNickName());
//                userType.setText(user.getLevelName());
//                new LoadLogoImageAyscTask ( resources, userLogo, user.getWxHeadImg ( ), R.drawable.ic_login_username ).execute ( );
//
//                //切换用户，需要清空 店中店的 缓存数据
//                SisConstant.SHOPINFO = null;
//                SisConstant.CATEGORY = null;
//
//                //动态加载侧滑菜单
//                UIUtils ui = new UIUtils ( application, MainActivity.this, resources, mainMenuLayout, mHandler );
//                ui.loadMenus();
//
//                dealUserid();
//            }
//            break;
//            case Constants.LOAD_SWITCH_USER_OVER:
//            {
//                progress.dismissView();
//            }
//            break;
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
                bindWeiXin(account);
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

    private  void bindWeiXin(AccountModel model ){
        String url = Constants.INTERFACE_URL + "Account/bindWeixin";
        Map map = new HashMap();
        map.put("userid",  application.readMemberId() );
        map.put("customerid", application.readMerchantId());
        map.put("sex", model.getSex());
        map.put("nickname", model.getNickname());
        map.put("openid", model.getOpenid());
        map.put("city", model.getCity());
        map.put("country",model.getCountry());
        map.put("province",model.getProvince());
        map.put("headimgurl",model.getAccountIcon());
        map.put("unionid",model.getAccountUnionId());
        map.put("refreshToken",model.getAccountToken());
        AuthParamUtils authParamUtils =new AuthParamUtils(application, System.currentTimeMillis(), url , MainActivity.this);
        Map<String,String> params = authParamUtils.obtainParams(map);

        GsonRequest<PhoneLoginModel> request =new GsonRequest<PhoneLoginModel>(Request.Method.POST,
                url ,
                PhoneLoginModel.class,
                null,
                params,
                new MyBindWeiXinListener(this , model ),
                new MyBindWeiXinErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);

        progress.showProgress("正在绑定微信，请稍等...");
        progress.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
    static class MyBindWeiXinListener implements Response.Listener<PhoneLoginModel>{
        WeakReference<MainActivity> ref;
        AccountModel weixinModel;
        MyBindWeiXinListener(MainActivity act , AccountModel model ){
            ref=new WeakReference<>(act);
            weixinModel=model;
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel ) {
            if( ref.get()==null) return;
            if( ref.get().progress!=null){
                ref.get().progress.dismissView();
            }
            if( phoneLoginModel ==null){
                ToastUtils.showShortToast(ref.get(), "绑定微信操作失败");
                return;
            }
            if( phoneLoginModel.getCode() != 200){
                String msg ="绑定微信操作失败";
                if( !TextUtils.isEmpty(phoneLoginModel.getMsg() )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg);
                return;
            }
            if( phoneLoginModel.getData() ==null ){
                ToastUtils.showShortToast(ref.get(),"绑定微信操作失败");
                return;
            }

            ToastUtils.showShortToast(ref.get(), "绑定操作成功");

            ref.get().application.writeMemberInfo (
                    phoneLoginModel.getData().getNickName() , String.valueOf( phoneLoginModel.getData().getUserid() ),
                    phoneLoginModel.getData().getHeadImgUrl() , weixinModel.getAccountToken (),
                    weixinModel.getAccountUnionId () , weixinModel.getOpenid()
            );
            ref.get().application.writeMemberLevel( phoneLoginModel.getData().getLevelName());
            //记录登录类型(1:微信登录，2：手机登录)
            ref.get().application.writeMemberLoginType( 1 );
            ref.get().application.writeMemberRelatedType( phoneLoginModel.getData().getRelatedType() );//重写 关联类型=2 已经绑定
            //动态加载侧滑菜单
            UIUtils ui = new UIUtils (  ref.get().application,  ref.get() ,  ref.get().resources,  ref.get().mainMenuLayout,  ref.get().mHandler );
            ui.loadMenus();

            //ref.get().initUserInfo();
        }
    }

    static class MyBindWeiXinErrorListener implements Response.ErrorListener{
        WeakReference<MainActivity> ref;
        public MyBindWeiXinErrorListener(MainActivity act){
            ref=new WeakReference<MainActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if(ref.get()==null)return;
            if(ref.get().progress!=null ){
                ref.get().progress.dismissView();
            }
            ToastUtils.showShortToast(ref.get(),"绑定微信操作失败。");
        }
    }
}
