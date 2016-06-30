package com.huotu.lingyunhui.model;

import java.util.List;

/**
 * Created by hzbc on 2016/5/27.
 */
public class NewsColumn {
    private String title;
    private String url;
    public List<NewsColumn> list;

    public NewsColumn(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public List<NewsColumn> getList() {
        return list;
    }

    public void setList(List<NewsColumn> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
