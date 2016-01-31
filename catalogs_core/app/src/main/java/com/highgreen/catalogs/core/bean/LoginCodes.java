package com.highgreen.catalogs.core.bean;

import java.util.List;

/**
 * Created by tihong on 16-1-31.
 */
public class LoginCodes {
    List<LoginCodeItem> login_codes;

    public List<LoginCodeItem> getLogin_codes() {
        return login_codes;
    }

    public void setLogin_codes(List<LoginCodeItem> login_codes) {
        this.login_codes = login_codes;
    }
}
