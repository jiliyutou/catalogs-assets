package com.highgreen.catalogs.core.bean;

/**
 * Created by ruantihong on 1/22/16.
 */
public class CategoryItem {
    private String imageUrl;
    private String currentPath;
    private String httpHeader;
    private String title;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
