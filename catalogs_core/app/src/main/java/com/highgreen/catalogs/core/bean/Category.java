package com.highgreen.catalogs.core.bean;

import com.highgreen.catalogs.core.adapter.CategoryItemAdapter;

import java.util.List;

/**
 * Created by tihong on 16-1-31.
 */
public class Category {
    private List<CategoryItemAdapter> categories;

    public List<CategoryItemAdapter> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryItemAdapter> categories) {
        this.categories = categories;
    }
}
