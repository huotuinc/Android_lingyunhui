package com.huotu.lingyunhui.model;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PhoneLoginModel extends DataBase {
    private PhoneModel data;

    public PhoneModel getData() {
        return data;
    }

    public void setData(PhoneModel data) {
        this.data = data;
    }

    public class PhoneModel{
        private int userid;
        private String levelName;
        private String nickName;
        private String headImgUrl;
        private int bindUserCount;
        private int userType;
        private String loginName;
        private String realName;
        private String regTime;
        private int relatedType;//0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号
        private String authorizeCode;
        private int levelId;
        private boolean IsMobileBind;
        private String openId;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getAuthorizeCode() {
            return authorizeCode;
        }

        public void setAuthorizeCode(String authorizeCode) {
            this.authorizeCode = authorizeCode;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public int getBindUserCount() {
            return bindUserCount;
        }

        public void setBindUserCount(int bindUserCount) {
            this.bindUserCount = bindUserCount;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime = regTime;
        }

        public int getRelatedType() {
            return relatedType;
        }

        public void setRelatedType(int relatedType) {
            this.relatedType = relatedType;
        }

        public int getLevelId() {
            return levelId;
        }

        public void setLevelId(int levelId) {
            this.levelId = levelId;
        }

        public boolean isMobileBind() {
            return IsMobileBind;
        }

        public void setMobileBind(boolean mobileBind) {
            IsMobileBind = mobileBind;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }
    }
}
