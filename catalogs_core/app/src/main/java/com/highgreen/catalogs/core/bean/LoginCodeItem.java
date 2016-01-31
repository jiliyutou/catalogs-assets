package com.highgreen.catalogs.core.bean;

/**
 * Created by tihong on 16-1-31.
 */
public class LoginCodeItem {

    private String code;
    private boolean valid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
