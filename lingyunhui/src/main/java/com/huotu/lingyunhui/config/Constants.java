package com.huotu.lingyunhui.config;

/**
 * Created by hzbc on 2016/5/17.
 */
public class Constants {

    public static final int ANIMATION_DURATION = 2000;

    // 屏幕高度
    public static int SCREEN_HEIGHT = 800;
    // 屏幕宽度
    public static int SCREEN_WIDTH = 480;

    // 屏幕密度
    public static float SCREEN_DENSITY = 1.5f;
    //鉴权失效
    public static final int LOGIN_AUTH_ERROR = 2131;
    //微信登录:用户存在
    public static final int MSG_USERID_FOUND    = 1;
    //微信登录：用户不存在
    public static final int MSG_USERID_NO_FOUND = 0;
    public static final int MSG_LOGIN           = 2;
    public static final int MSG_AUTH_CANCEL     = 3;
    public static final int MSG_AUTH_ERROR      = 4;
    public static final int MSG_AUTH_COMPLETE   = 5;

    public static final String INTERFACE_URL = "http://mallapi.huobanj.cn/";
    /**
     * app系统配置
     */
    public static final String SYS_INFO    = "sysInfo";
    public static final String SYS_PACKAGE = "sysPackage";
    public static final String FIRST_OPEN  = "firstOpen";

    /**
     * 操作平台码
     */
    public static final String OPERATION_CODE = "BUYER_ANDROID_2015DC";

    public static final String APPKEY = "b73ca64567fb49ee963477263283a1bf";

    public static final String APP_ID = "7986d22352bf5acf37981b8e991edd76";

    public static final String APP_SECRET = "9389e8a5c32eefa3134340640fb4cead";

    /**
     * ************************************商户信息xml节点***********************
     */
    //app信息
    //public static final String APP_INFO    = "appinfo";
    //app版本号
    public static final String APP_VERSION = "app_version";
    //app名称
    public static final String APP_NAME    = "app_name";
    //app_build
    public static final String APP_BUILD   = "app_build";


    //商户信息
//	public static final String MERCHANT    = "MERCHANT";
    //商户ID
    public static final String MERCHANT_ID = "app_merchant_id";

    //微信支付信息
//	public static final String WEIXIN_PAY         = "weixinpay";
    //微信商家编号
    public static final String WEIXIN_MERCHANT_ID = "weixin_merchant_id";
    //商家微信编号
    public static final String MERCHANT_WEIXIN_ID = "merchant_weixin_id";
    //商户微信支付KEY信息
    public static final String WEIXIN_KEY         = "weixin_key";
    //支付宝商家编号
    public static final String ALIPAY_MERCHANT_ID = "alipay_merchant_id";
    //商家支付宝编号
    public static final String MERCHANT_ALIPAY_ID = "merchant_alipay_id";
    //商户支付宝KEY信息
    public static final String ALIPAY_KEY         = "alipay_key";
    //商户logo
    public static final String MERCHANT_LOGO      = "merchant_logo";
    //商户名称
    public static final String MERCHANT_NAME      = "merchant_name";

    //UMENG统计信息
    public static final String U_MENG            = "umeng";
    //UMENG app key
    public static final String U_MENG_KEY        = "umeng_appkey";
    //umeng_channel
    public static final String U_MENG_CHANNEL    = "umeng_channel";
    //umeng_message_secret
    public static final String U_MENG_SECRET     = "umeng_message_secret";
    //网络请求
    public static final String HTTP_PREFIX       = "httpprefix";
    //网络请求前缀
    public static final String PREFIX            = "prefix";
    //分享
    public static final String SHARE_INFO        = "shareinfo";
    //share KEY
    public static final String SHARE_KEY         = "share_key";
    //tencent_key
    public static final String TENCENT_KEY       = "tencent_key";
    //tencent_secret
    public static final String TENCENT_SECRET    = "tencent_secret";
    //sina_key
    public static final String SINA_KEY          = "sina_key";
    //sina_secret
    public static final String SINA_SECRET       = "sina_secret";
    //sina_redirect_uri
    public static final String SINA_REDIRECT_URI = "sina_redirect_uri";
    //weixin SHARE key
    public static final String WEIXIN_SHARE_key  = "weixin_share_key";
    //推送信息
    public static final String PUSH_INFO         = "push_info";
    //推送key
    public static final String PUSH_KEY          = "push_key";

    //定位key
    public static final String LOCATION_KEY        = "location_key";
    //微信分享加密
    public static final String WEIXIN_SHARE_SECRET = "weixin_share_secret";


    /**
     * *******************preference参数设置
     */
    //商户信息
    public static final String MERCHANT_INFO            = "merchant_info";
    //首页url
    public static final String HOME_URL                 = "home_url";
    //会员信息
    public static final String MEMBER_INFO              = "member_info";
    //会员等级
    public static final String MEMBER_level             = "member_level";
    //会员名称
    public static final String MEMBER_NAME              = "member_name";
    //会员ID
    public static final String MEMBER_ID                = "member_id";
    //会员等级id
    public static final String MEMBER_LEVELID			="member_levelid";
    //会员token
    public static final String MEMBER_UNIONID           = "member_unionid";
    //会员token
    public static final String MEMBER_TOKEN             = "member_token";
    //会员icon
    public static final String MEMBER_ICON              = "member_icon";
    //会员类型
    public static final String MEMBER_USERTYPE          ="member_usertype";
    //OpenId
    public static final String MEMBER_OPENID ="member_openid";
    //手机用户 登录名
    public static final String MEMBER_LOGINNAME         ="loginname";
    //手机用户 管理类型
    public static final String MEMBER_RELATEDTYPE       ="relatedType";
    //手机用户 授权码
    public static final String MEMBER_AUTHORIZECODE     ="authorizeCode";
    //手机用户 姓名
    public static final String MEMBER_REALNAME ="realName";
    //手机用户 安全码
    public static final String MEMBER_SECURE = "secure";
    //用户登录类型（1：微信登录，2：手机登录）
    public static final String MEMBER_LOGINTYPE="loginType";

    //登录方式
    public static final String MERCHANT_INFO_LOGINMETHOD = "login_method";


    //商户ID
    public static final String MERCHANT_INFO_ID         = "merchant_id";
    //商户支付宝key信息
    public static final String MERCHANT_INFO_ALIPAY_KEY = "merchant_alipay_key";
    //商户微信支付KEY信息
    public static final String MERCHANT_INFO_WEIXIN_KEY = "merchant_weixin_key";
    //商户菜单
    public static final String MERCHANT_INFO_MENUS      = "main_menus";
    //商户分类菜单
    public static final String MERCHANT_INFO_CATAGORY   = "catagory_menus";
    //app最新版本
    public static final String NEW_APP_VERSION = "new_app_version";
    //app 升级地址
    public static final String APP_UPDATE_URL = "app_update_url";
    //数据包版本号
    public static final String DATA_INIT            = "data_init";
    //会员信息
    public static final String PACKAGE_VERSION              = "package_version";
    public static final String ALIPAY_NOTIFY = "alipay_notify";
    public static final String WEIXIN_NOTIFY = "weixin_notify";
    public static final String IS_WEB_WEIXINPAY = "is_web_weixinpay";
    public static final String IS_WEB_ALIPAY = "is_web_alipay";
}
