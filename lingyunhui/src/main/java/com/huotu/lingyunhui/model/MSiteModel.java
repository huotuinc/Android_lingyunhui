package com.huotu.lingyunhui.model;

/**
 * 接收商家域名model
 */
public
class MSiteModel {

    private int code;
    private String msg;
    private MSiteData data;

    public
    int getCode ( ) {
        return code;
    }

    public
    void setCode ( int code ) {
        this.code = code;
    }

    public String getMsg ( ) {
        return msg;
    }

    public
    void setMsg ( String msg ) {
        this.msg = msg;
    }

    public
    MSiteData getData ( ) {
        return data;
    }

    public
    void setData ( MSiteData data ) {
        this.data = data;
    }

    public class MSiteData
    {
        private String msiteUrl;

        public String getMsiteUrl ( ) {
            return msiteUrl;
        }

        public
        void setMsiteUrl ( String msiteUrl ) {
            this.msiteUrl = msiteUrl;
        }
    }
}
