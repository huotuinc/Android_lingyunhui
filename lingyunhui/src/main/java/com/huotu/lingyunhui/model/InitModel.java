package com.huotu.lingyunhui.model;

import java.util.List;

public
class InitModel {

    private int code;
    private String msg;
    private AuthMall data;

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
    AuthMall getData ( ) {
        return data;
    }

    public
    void setData ( AuthMall data ) {
        this.data = data;
    }

    public class AuthMall
    {

        private List<MenuModel> bottomMenus;
        private UserModel userInfo;
        private AppConfigModel appConfig;

        public List<MenuModel> getBottomMenus() {
            return bottomMenus;
        }

        public void setBottomMenus(List<MenuModel> bottomMenus) {
            this.bottomMenus = bottomMenus;
        }

        public UserModel getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserModel userInfo) {
            this.userInfo = userInfo;
        }

        public AppConfigModel getAppConfig() {
            return appConfig;
        }

        public void setAppConfig(AppConfigModel appConfig) {
            this.appConfig = appConfig;
        }








    }

    public class UserModel
    {
        private int userId;
        private String userName;
        private int unionId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUnionId() {
            return unionId;
        }

        public void setUnionId(int unionId) {
            this.unionId = unionId;
        }

        public int getOpenId() {
            return openId;
        }

        public void setOpenId(int openId) {
            this.openId = openId;
        }

        private int openId;
    }

    public class MenuModel
    {
        private String name;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }
    public class AppConfigModel
    {
        private String version;
        private String mallBottomUrl;
        private String mallUrl;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getMallBottomUrl() {
            return mallBottomUrl;
        }

        public void setMallBottomUrl(String mallBottomUrl) {
            this.mallBottomUrl = mallBottomUrl;
        }

        public String getMallUrl() {
            return mallUrl;
        }

        public void setMallUrl(String mallUrl) {
            this.mallUrl = mallUrl;
        }
    }
}
