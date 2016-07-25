package com.huotu.lingyunhui.model;

/**
 * 底部菜单实体
 */
public class MenuBean extends BaseBean {

    //菜单名称
    private String menuName;
    //菜单图标
    private String menuIcon;
    //菜单标识
    private String menuTag;
    //菜单url
    private String menuUrl;
    //菜单组
    private String menuGroup;


    public String getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }

    public String getMenuTag() {
        return menuTag;
    }

    public void setMenuTag(String menuTag) {
        this.menuTag = menuTag;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
}
