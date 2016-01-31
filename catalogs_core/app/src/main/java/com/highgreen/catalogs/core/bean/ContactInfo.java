package com.highgreen.catalogs.core.bean;

import java.util.List;

/**
 * Created by tihong on 16-1-24.
 */
public class ContactInfo {
    private String website;
    private String address;

    private List<PersonInfo> personInfos;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PersonInfo> getPersonInfos() {
        return personInfos;
    }

    public void setPersonInfos(List<PersonInfo> personInfos) {
        this.personInfos = personInfos;
    }
}
