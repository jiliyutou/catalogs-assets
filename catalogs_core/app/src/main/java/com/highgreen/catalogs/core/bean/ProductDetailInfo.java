package com.highgreen.catalogs.core.bean;

import java.util.List;

/**
 * Created by zhk on 16-2-10.
 */
public class ProductDetailInfo {
    private String product;
    private List<ParamKVPair> details;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<ParamKVPair> getDetails() {
        return details;
    }

    public void setDetails(List<ParamKVPair> details) {
        this.details = details;
    }
}
