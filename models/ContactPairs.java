package com.marafiki.android.models;

/**
 * Created by Mbariah on 6/29/17.
 */

public class ContactPairs {

    String name;
    String phone;

    public ContactPairs(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}