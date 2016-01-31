package com.highgreen.catalogs.core.bean;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class Person {
    private String name;
    private List<ParamKVPair> contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParamKVPair> getContacts() {
        return contacts;
    }

    public void setContacts(List<ParamKVPair> contacts) {
        this.contacts = contacts;
    }
}
