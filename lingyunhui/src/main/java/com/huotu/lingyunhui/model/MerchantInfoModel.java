package com.huotu.lingyunhui.model;

/**
 * 商户信息
 */
public class MerchantInfoModel {
    private String mall_description;
    private String mall_logo;
    private String mall_name;
    private String mall_site;
    private int mall_defaultlevelid;
    private int versionnumber;
    private String applinkurl;
    //0:手机+微信 1：手机 2：微信
    private int accountmodel;

    public String getMall_description() {
        return mall_description;
    }

    public void setMall_description(String mall_description) {
        this.mall_description = mall_description;
    }

    public String getMall_logo() {
        return mall_logo;
    }

    public void setMall_logo(String mall_logo) {
        this.mall_logo = mall_logo;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getMall_site() {
        return mall_site;
    }

    public void setMall_site(String mall_site) {
        this.mall_site = mall_site;
    }

    public int getMall_defaultlevelid() {
        return mall_defaultlevelid;
    }

    public void setMall_defaultlevelid(int mall_defaultlevelid) {
        this.mall_defaultlevelid = mall_defaultlevelid;
    }

    public int getVersionnumber() {
        return versionnumber;
    }

    public void setVersionnumber(int versionnumber) {
        this.versionnumber = versionnumber;
    }

    public String getApplinkurl() {
        return applinkurl;
    }

    public void setApplinkurl(String applinkurl) {
        this.applinkurl = applinkurl;
    }

    public int getAccountModel() {
        return accountmodel;
    }

    public void setAccountModel(int accountmodel) {
        this.accountmodel = accountmodel;
    }
}
