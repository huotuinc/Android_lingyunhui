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

    public static final String INTERFACE_URL = "http://mallapi.huobanj.cn/ArvatoConfig/";
    public static final String Message_url = "http://mallapi.huobanj.cn/ArvatoMessage/";
    public static final String URL = "http://mallapi.huobanj.cn/";
    /**
     ******************************************* 参数设置信息结束
     * ******************************************
     */

    //标准时间
    public final static String TIME_FORMAT   = "yyyy-MM-dd HH:mm:ss";
    //标准时间01
    public static final String DATE_FORMAT   = "yyyy-MM-dd";

    /**
     * 底部菜单加载页面
     */
    public static final int LOAD_PAGE_MESSAGE_TAG = 4381;
    //网页支付
    public static final int PAY_NET = 2222;
    /**
     * tile栏刷新页面
     */
    public static final int FRESHEN_PAGE_MESSAGE_TAG = 4380;


    /**
     * app系统配置
     */
    public static final String SYS_INFO    = "sysInfo";
    public static final String SYS_PACKAGE = "sysPackage";
    public static final String FIRST_OPEN  = "firstOpen";
    public static final String CUSTOMER_ID = "customerid={}";
    public static final String USER_ID     = "userid={}";
    public static final String COMMON_SHARE_LOGO = "http://1804.img.pp.sohu.com.cn/images/2013/1/14/16/2/6205e011f029437o_13cfbf362e6g85.jpg";

    // 分享成功
    public static final int SHARE_SUCCESS = 0X1000;

    // 分享取消
    public static final int SHARE_CANCEL = 0X2000;

    // 分享失败
    public static final int SHARE_ERROR = 0X3000;
    /**
     * 微信支付appID
     */
    public static final String WXPAY_ID    = "wx2f2604e380cf6be1";
    public static final String WXPAY_SECRT = "ae3a7d851f24bfc97047954fa3975cec";
    public static final String PAY_URL     = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    // 商户PID
    public static final String PARTNER      = "";
    // 商户收款账号
    public static final String SELLER       = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE  = "";
    // 支付宝公钥
    public static final String RSA_PUBLIC   = "";
    public static final int    SDK_PAY_FLAG = 1;

    public static final int SDK_CHECK_FLAG = 2;

    // API密钥，在商户平台设置(微信支付商户)
    public static final String wxpayApikey    = "0db0d4908a6ae6a09b0a7727878f0ca6";
    //微信parterKey
    public static final String wxpayParterkey = "1251040401";

    //微信支付
    public static final String WX_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 操作平台码
     */
    public static final String OPERATION_CODE = "BUYER_ANDROID_2015DC";

    public static final String APPKEY = "b73ca64567fb49ee963477263283a1bf";

    public static final String APP_ID = "7986d22352bf5acf37981b8e991edd76";

    public static final String APP_SECRET = "9389e8a5c32eefa3134340640fb4cead";

    //http请求参数
    //获取具体页面的商品类别
    public static final String INTENT_URL   = "INTENT_URL";
    public static final String HTTP_OBTAIN_CATATORY = "/goods/obtainCatagory?";
    //获取商品信息
    public static final String HTTP_OBTAIN_GOODS    = "/goods/obtainGoods?";
    //new view
    public static final String WEB_TAG_NEWFRAME     = "__newframe";
    //上传图片
    public static final String WEB_TAG_COMMITIMG    = "partnermall520://pickimage";
    //登出
    public static final String WEB_TAG_LOGOUT       = "partnermall520://logout";
    //信息保护
    public static final String WEB_TAG_INFO         = "partnermall520://togglepb";
    //关闭当前页
    public static final String WEB_TAG_FINISH       = "partnermall520://closepage";
    //share
    public static final String WEB_TAG_SHARE        = "partnermall520://shareweb";
    //弹出框
    public static final String WEB_TAG_ALERT        = "partnermall520://alert";
    //支付
    public static final String WEB_TAG_PAYMENT      = "partnermall520://payment";
    //用户信息修改
    public static final String WEB_TAG_USERINFO     = "partnermall520://userinfo?";
    //联系客服
    public static final String WEB_CONTACT          = "mqqwpa://im/chat";
    //支付
    public static final String WEB_PAY      = "/Mall/AppAlipay.aspx";
    //鉴权失效
    public static final String AUTH_FAILURE = "/UserCenter/Login.aspx";

    //切换账户
    public static final String URL_APPACCOUNTSWITCHER="/UserCenter/AppAccountSwitcher.aspx";

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
