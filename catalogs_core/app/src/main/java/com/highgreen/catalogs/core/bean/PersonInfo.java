package com.highgreen.catalogs.core.bean;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class PersonInfo {
    private String name;
    private List<ContactItem> contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactItem> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactItem> contacts) {
        this.contacts = contacts;
    }
}
