package com.huotu.lingyunhui.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.huotu.lingyunhui.model.PayModel;
import com.huotu.lingyunhui.ui.base.BaseApplication;
import com.huotu.lingyunhui.utils.WXPayAsyncTask;
import com.huotu.lingyunhui.widgets.ProgressPopupWindow;


/**
 * 支付总览
 */
public
class PayFunc {

    private PayModel payModel;
    private
    BaseApplication application;
    //private FMPrepareBuy    prepareBuy;
    private
    Handler handler;
    private
    Context context;
    private
    Activity aty;
    private ProgressPopupWindow progress;

    public
    PayFunc (Context context, PayModel payModel, BaseApplication application, Handler handler,
             Activity aty, ProgressPopupWindow progress ) {
        this.payModel = payModel;
        this.application = application;
        this.handler = handler;
        this.context = context;
        this.aty = aty;
        this.progress = progress;
    }

    public
    void wxPay ( ) {
        //根据订单号获取支付信息
        String body        = payModel.getDetail ( );
        String price       = String.valueOf ( payModel.getAmount ( ) );
        int    productType = 0;
        long   productId   = 0;
        //prepareBuy = new FMPrepareBuy ();
        progress.dismissView ();
        //调用微信支付模块
        new WXPayAsyncTask(handler, body, price, productType, productId, context, application, payModel.getNotifyurl (), payModel.getAttach (), payModel.getTradeNo ()).execute();
    }

    public void aliPay()
    {
//        AliPayUtil aliPay = new AliPayUtil(aty, handler, application);
//        //根据订单号获取订单信息
//        String body = payModel.getDetail ( );
//        String price = String.valueOf ( payModel.getAmount () );
//        String subject = payModel.getDetail ();
//        int productType= 0;
//        long productId= 0;
//        //prepareBuy = new FMPrepareBuy ();
//        progress.dismissView ();
//        aliPay.pay(subject, body, price, payModel.getNotifyurl (), productType, productId);
    }
}

