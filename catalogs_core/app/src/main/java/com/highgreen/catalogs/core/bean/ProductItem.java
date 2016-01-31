package com.highgreen.catalogs.core.bean;

import java.io.Serializable;

/**
 * Created by tihong on 16-1-24.
 */
public class ProductItem implements Serializable{
    private String imageUrl;
    private String title;

    public ProductItem() {
    }

    public ProductItem(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
