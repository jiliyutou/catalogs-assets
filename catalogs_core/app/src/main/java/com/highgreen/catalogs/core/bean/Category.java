package com.highgreen.catalogs.core.bean;

import com.highgreen.catalogs.core.adapter.CategoryItemAdpter;

import java.util.List;

/**
 * Created by tihong on 16-1-31.
 */
public class Category {
    private List<CategoryItemAdpter> categories;

    public List<CategoryItemAdpter> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryItemAdpter> categories) {
        this.categories = categories;
    }
}
