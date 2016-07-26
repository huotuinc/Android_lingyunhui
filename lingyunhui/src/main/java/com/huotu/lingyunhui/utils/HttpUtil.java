package com.huotu.lingyunhui.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.lingyunhui.config.Constants;
import com.huotu.lingyunhui.model.AccountModel;
import com.huotu.lingyunhui.model.AuthMallModel;
import com.huotu.lingyunhui.model.MDataPackageModel;
import com.huotu.lingyunhui.model.MSiteModel;
import com.huotu.lingyunhui.model.MerchantInfoModel;
import com.huotu.lingyunhui.model.MerchantPayInfo;
import com.huotu.lingyunhui.model.OrderModel;
import com.huotu.lingyunhui.model.PayModel;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.widgets.NoticePopWindow;
import com.huotu.lingyunhui.widgets.PayPopWindow;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static class Holder{
        private static final HttpUtil instance = new HttpUtil();
    }

    private HttpUtil(){
    }

    public static final HttpUtil getInstance() {
        return Holder.instance;
    }

    /**
     * 获取数据包版本信息
     * @param application
     * @param url
     */
    public void doVolleyPackage(final BaseApplication application, String url ) {
        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MDataPackageModel> jsonUtil = new JSONUtil<MDataPackageModel>();
                MDataPackageModel packageModel = new MDataPackageModel();
                packageModel = jsonUtil.toBean(response.toString (), packageModel);
                if(null != packageModel) {
                    MDataPackageModel.MDataPackageData dataPackageData = packageModel.getData ();
                    if ( 0 == dataPackageData.getUpdateData () ) {
                        //没有更新，直接执行以下步骤
                    }
                    else
                    {
                        application.writePackageVersion ( dataPackageData.getVersion ( ) );
                        //直接下载文件，并更新version
                        //下载数据
                        //new HttpDownloader().execute ( dataPackageData.getDownloadUrl () );
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        VolleyUtil.getRequestQueue().add(re);
    }

    /**
     * 根据商户编号获取商户域名
     * @param application
     * @param url
     */
    public void doVolleySite( final BaseApplication application, String url )
    {
        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MSiteModel > jsonUtil = new JSONUtil<MSiteModel>();
                MSiteModel mSite = new MSiteModel();
                mSite = jsonUtil.toBean(response.toString (), mSite);
                if(null != mSite) {
                    MSiteModel.MSiteData mSiteData = mSite.getData ();
                    if ( null != mSiteData.getMsiteUrl () ) {
                        String domain = mSiteData.getMsiteUrl ( );
                        application.writeDomain ( domain );
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }});
        VolleyUtil.getRequestQueue().add(re);
    }

    /**
     * 获取商户logo
     * @param application
     * @param url
     */
    public void doVolleyLogo( final BaseApplication application, String url)
    {
        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                JSONUtil<MerchantInfoModel> jsonUtil = new JSONUtil<MerchantInfoModel>();
                MerchantInfoModel merchantInfo = new MerchantInfoModel();
                merchantInfo = jsonUtil.toBean(response.toString (), merchantInfo);
                if(null != merchantInfo) {
                    String site = merchantInfo.getMall_site();
                    application.writeDomain(site);
                    String logo = null;
                    if ( null != merchantInfo.getMall_logo ( ) && null != merchantInfo.getMall_name () ) {
                        if(!TextUtils.isEmpty ( application.obtainMerchantUrl () ))
                        {
                            logo =  application.obtainMerchantUrl () + merchantInfo.getMall_logo ( );
                        }
                        else
                        {
                            logo = merchantInfo.getMall_logo ( );
                        }

                        String name = merchantInfo.getMall_name ( );
                        application.writeMerchantLogo ( logo );
                        application.writeMerchantName ( name );

                        //记录登录配置方式
                        application.writeLoginMethod( merchantInfo.getAccountModel() );
                        //记录服务器app最新版本信息
                        application.writeNewVersion( merchantInfo.getVersionnumber() );
                        application.writeAppUrl( merchantInfo.getApplinkurl() );
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyUtil.getRequestQueue().add( re);
    }
    /**
     * 获取支付信息
     * @param application
     * @param url
     */
    public void doVolley( final BaseApplication application, String url ){
        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                JSONUtil<MerchantPayInfo> jsonUtil = new JSONUtil<MerchantPayInfo>();
                MerchantPayInfo merchantPayInfo = new MerchantPayInfo();
                merchantPayInfo = jsonUtil.toBean(response.toString (), merchantPayInfo);
                if(null != merchantPayInfo) {
                    List<MerchantPayInfo.MerchantPayModel> merchantPays = merchantPayInfo.getData ();
                    if ( ! merchantPays.isEmpty ( ) ) {
                        for ( MerchantPayInfo.MerchantPayModel merchantPay : merchantPays) {

                            if(400 == merchantPay.getPayType ())
                            {
                                //支付宝信息
                                application.writeAlipay( merchantPay.getPartnerId (),  merchantPay.getAppKey (), merchantPay.getNotify (), merchantPay.isWebPagePay () );
                            }
                            else if(300 == merchantPay.getPayType ())
                            {
                                //微信支付
                                application.writeWx( merchantPay.getPartnerId (), merchantPay.getAppId ( ),  merchantPay.getAppKey ( ), merchantPay.getNotify ( ), merchantPay.isWebPagePay ( ) );
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        VolleyUtil.getRequestQueue().add( re);
    }

    public void doVolley(final Activity aty, final Handler mHandler, final BaseApplication application,
                         String url, Map param, final AccountModel account , final Bundle bundlePush ){
        final GsonRequest re = new GsonRequest (
                Request.Method.POST, url, AuthMallModel.class, null, param,
                new Response.Listener<AuthMallModel>(){
            @Override
            public void onResponse(AuthMallModel response) {
                if( aty ==null )return;

                AuthMallModel authMallModel = new AuthMallModel();
                authMallModel = response;
                if( authMallModel !=null && 200 == authMallModel.getCode ())
                {

                    AuthMallModel.AuthMall mall = authMallModel.getData ();
                    if(null != mall)
                    {
                        //写入userID
                        account.setAccountId(String.valueOf(mall.getUserid()));
                        account.setAccountName(mall.getNickName());
                        account.setAccountIcon(mall.getHeadImgUrl());

                        //和商城用户系统交互
                        application.writeMemberInfo (
                                account.getAccountName ( ), account.getAccountId ( ),
                                account.getAccountIcon ( ), account.getAccountToken ( ),
                                account.getAccountUnionId ( ), account.getOpenid()
                        );
                        application.writeMemberLevel(mall.getLevelName());
                        //记录登录类型(1:微信登录，2：手机登录)
                        application.writeMemberLoginType(1);
                        //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
                        application.writeMemberRelatedType( mall.getRelatedType() );

//                        //设置侧滑栏菜单
//                        List<MenuBean > menus = new ArrayList< MenuBean >(  );
//                        MenuBean menu = null;
//                        List<AuthMallModel.MenuModel > home_menus = mall.getHome_menus ();
//                        for(AuthMallModel.MenuModel home_menu:home_menus)
//                        {
//                            menu = new MenuBean ();
//                            menu.setMenuGroup ( String.valueOf ( home_menu.getMenu_group () ) );
//                            menu.setMenuIcon ( home_menu.getMenu_icon ( ) );
//                            menu.setMenuName ( home_menu.getMenu_name ( ) );
//                            menu.setMenuUrl ( home_menu.getMenu_url ( ) );
//                            menus.add ( menu );
//                        }
//                        if(null != menus && !menus.isEmpty ())
//                        {
//                            application.writeMenus ( menus );
//
//
//                            //绑定极光推送别名
//                            UIUtils.bindPush();
//
//                            Intent intent = new Intent();
//                            intent.setClass( aty , HomeActivity.class);
//                            //传递推送信息
//                            if(null != bundlePush){
//                                intent.putExtra( Constants.HUOTU_PUSH_KEY ,  bundlePush );
//                            }
//
//                            //跳转到首页
//                            ActivityUtils.getInstance ().skipActivity ( aty, intent );
//                        }
//                        else
//                        {
//                            mHandler.sendEmptyMessage ( Constants.INIT_MENU_ERROR );
//                        }
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.what = Constants.LOGIN_AUTH_ERROR;
                        msg.obj = authMallModel.getMsg ();
                        mHandler.sendMessage ( msg );
                    }

                }
                else if( authMallModel !=null && 403 == authMallModel.getCode ())
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }
                else
                {
                    //授权失败
                    Message msg = new Message();
                    msg.what = Constants.LOGIN_AUTH_ERROR;
                    msg.obj = authMallModel.getMsg ();
                    mHandler.sendMessage ( msg );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Message msg = new Message();
                msg.what = Constants.LOGIN_AUTH_ERROR;
                msg.obj = "调用授权接口失败，请确认！";
                mHandler.sendMessage ( msg );
            }


        });
        VolleyUtil.getRequestQueue().add(re);
    }

//    public void doVolleyObtainUser(final Activity aty, final BaseApplication application, String url, final View view, final WindowManager wManager, final Handler mHandler) {
//        final KJJsonObjectRequest re = new KJJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//
//            @Override
//            public void onResponse(JSONObject response) {
//                if (aty == null) return;
//
//                JSONUtil<SwitchUserModel> jsonUtil = new JSONUtil<SwitchUserModel>();
//                SwitchUserModel switchUser = new SwitchUserModel();
//                switchUser = jsonUtil.toBean(response.toString(), switchUser);
//
//                if (null != switchUser) {
//                    List<SwitchUserModel.SwitchUser> userList = switchUser.getData();
//                    if ((null != userList) && (!userList.isEmpty()) && (userList.size() > 1)) {
//                        //去重复数据
//                        List<SwitchUserModel.SwitchUser> sourceList = new ArrayList<SwitchUserModel.SwitchUser>();
//                        sourceList = clearData(userList);
//                        //关闭载入数据条
//                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
//                        //弹出切换用户面板
//                        SwitchUserPopWin userPop = new SwitchUserPopWin(aty, sourceList, application, wManager, mHandler);
//                        userPop.initView();
//                        userPop.showAtLocation(view, Gravity.CENTER, 0, 0);
//                        userPop.setOnDismissListener(new PoponDismissListener(aty));
//                    } else if ((null != userList) && (!userList.isEmpty()) && (userList.size() == 1)) {
//                        //关闭载入数据条
//                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
//
//                        NoticePopWindow noticePop = new NoticePopWindow(aty, "无其他账户，请绑定其他账户。");
//                        noticePop.showNotice();
//                        noticePop.showAtLocation(
//                                view,
//                                Gravity.CENTER, 0, 0
//                        );
//                    } else {
//                        //关闭载入数据条
//                        mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
//                        NoticePopWindow noticePop = new NoticePopWindow(aty, "无其他账户。");
//                        noticePop.showNotice();
//                        noticePop.showAtLocation(view, Gravity.CENTER, 0, 0);
//                    }
//                } else {
//                    //关闭载入数据条
//                    mHandler.sendEmptyMessage(Constants.LOAD_SWITCH_USER_OVER);
//
//                    NoticePopWindow noticePop = new NoticePopWindow(aty, "未检测到你的账户信息，请确认。");
//                    noticePop.showNotice();
//                    noticePop.showAtLocation(
//                            view,
//                            Gravity.CENTER, 0, 0
//                    );
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//
//
//        });
//        VolleyUtil.getRequestQueue().add(re);
//    }
//
//    public void doVolleyName(final BaseApplication application, String url, final TextView userType ){
//        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
//
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                if( userType ==null) return;
//
//                JSONUtil<MemberModel > jsonUtil = new JSONUtil<MemberModel>();
//                MemberModel memberIfo = new MemberModel();
//                memberIfo = jsonUtil.toBean(response.toString (), memberIfo);
//                if(null != memberIfo) {
//                    MemberModel.MemberInfo member = memberIfo.getData ();
//                    if ( null != member ) {
//                        //记录会员等级
//                        if( TextUtils.isEmpty ( member.getLevelName () ))
//                        {
//                            userType.setText ( "未设置会员等级" );
//                            application.writeMemberLevel ( "未设置会员等级" );
//                        }
//                        else
//                        {
//                            userType.setText ( member.getLevelName () );
//                            application.writeMemberLevel ( member.getLevelName () );
//                        }
//                    }
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                application.writeMemberLevel ( "普通会员" );
//            }
//
//
//        });
//        VolleyUtil.getRequestQueue().add( re);
//    }
//
    public void doVolleyPay(final Activity aty, final Handler mHandler, final BaseApplication application, String url, final PayModel payModel, final ProgressPopupWindow payProgress  ){
        final KJJsonObjectRequest re = new KJJsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                if( aty ==null  ) return;

                JSONUtil<OrderModel> jsonUtil = new JSONUtil<OrderModel>();
                OrderModel orderInfo = new OrderModel();
                orderInfo = jsonUtil.toBean(response.toString (), orderInfo);
                if(200 == orderInfo.getCode ()) {
                    if ( null != orderInfo ) {
                        OrderModel.OrderData order = orderInfo.getData ( );
                        if ( null == order)
                        {
                            //支付信息获取错误
                            payProgress.dismissView ( );
                            NoticePopWindow noticePop = new NoticePopWindow ( aty, "获取订单信息失败。");
                            noticePop.showNotice ( );
                            noticePop.showAtLocation (  aty.getWindow().getDecorView() ,  Gravity.CENTER, 0, 0
                            );
                        }
                        else
                        {
                            payModel.setAmount ( ( int ) ( 100 * format2Decimal ( order.getFinal_Amount ( ) ) ) );
                            payModel.setDetail ( order.getToStr ( ) );


                            if ( null != order ) {
                                payProgress.dismissView ( );
                                PayPopWindow payPopWindow = new PayPopWindow( aty,  mHandler, application, payModel );
                                payPopWindow.showAtLocation ( aty.getWindow().getDecorView() , Gravity.BOTTOM, 0, 0 );
                                //支付
                        /*if("1".equals ( payModel.getPaymentType () ) || "7".equals ( payModel.getPaymentType () ))
                        {
                            //添加支付宝回调路径
                            payModel.setNotifyurl ( application.obtainMerchantUrl () + application.readAlipayNotify ( ) );
                            //alipay
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty, payProgress );
                            payFunc.aliPay ( );

                        }
                        else if("2".equals ( payModel.getPaymentType () ) || "9".equals ( payModel.getPaymentType () ))
                        {
                            payModel.setAttach ( payModel.getCustomId ()+"_0" );
                            //添加微信回调路径
                            payModel.setNotifyurl ( application.obtainMerchantUrl ( ) + application.readWeixinNotify() );
                            PayFunc payFunc = new PayFunc ( context, payModel, application, mHandler, aty, payProgress );
                            payFunc.wxPay ( );

                        }*/
                            }

                        }
                    }
                    else
                    {
                        payProgress.dismissView ( );
                        NoticePopWindow noticePop = new NoticePopWindow ( aty,  "获取订单信息失败。");
                        noticePop.showNotice ();
                        noticePop.showAtLocation ( aty.getWindow().getDecorView() , Gravity.CENTER, 0, 0 );
                    }
                }
                else
                {
                    //支付信息获取错误
                    payProgress.dismissView ( );
                    NoticePopWindow noticePop = new NoticePopWindow(  aty,  "获取订单信息失败。");
                    noticePop.showNotice ( );
                    noticePop.showAtLocation ( aty.getWindow().getDecorView() , Gravity.CENTER, 0, 0 );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                payProgress.dismissView ( );
            }
        });
        VolleyUtil.getRequestQueue ().add( re);
    }

//    /**
//     * 清楚重复数据
//     * @param dataList
//     * @return
//     */
//    private List<SwitchUserModel.SwitchUser> clearData(List<SwitchUserModel.SwitchUser> dataList)
//    {
//        if(!dataList.isEmpty()){
//            for(int i=0;i<dataList.size();i++){
//                for(int j=dataList.size()-1;j>i;j--){
//                    if(dataList.get(i).getUserid () == dataList.get(j).getUserid ()){
//                        dataList.remove(j);
//                    }
//                }
//            }
//        }
//
//        return dataList;
//    }

    //保留2位小数
    private double format2Decimal(double d)
    {
        BigDecimal bg = new BigDecimal( d );
        return bg.setScale ( 2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
