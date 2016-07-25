package com.huotu.lingyunhui.model;

/**
 * Created by Administrator on 2015/10/28.
 */
public
class MDataPackageModel {

    private int code;
    private String msg;
    private MDataPackageData data;

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
    MDataPackageData getData ( ) {
        return data;
    }

    public
    void setData ( MDataPackageData data ) {
        this.data = data;
    }

    public class MDataPackageData
    {
        private int updateData;
        private String version;
        private String downloadUrl;

        public
        int getUpdateData ( ) {
            return updateData;
        }

        public
        void setUpdateData ( int updateData ) {
            this.updateData = updateData;
        }

        public String getVersion ( ) {
            return version;
        }

        public
        void setVersion ( String version ) {
            this.version = version;
        }

        public String getDownloadUrl ( ) {
            return downloadUrl;
        }

        public
        void setDownloadUrl ( String downloadUrl ) {
            this.downloadUrl = downloadUrl;
        }
    }
}
