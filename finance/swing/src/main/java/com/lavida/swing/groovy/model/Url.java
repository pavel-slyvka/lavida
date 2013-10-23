package com.lavida.swing.groovy.model;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class Url {
    private String title;
    private String url;

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

    @Override
    public String toString() {
        return "Url{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
