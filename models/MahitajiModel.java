package com.marafiki.android.models;

public class MahitajiModel implements Mahitaji {

    private String interest_rate;

    private int lender_count;

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getLender_count() {
        return lender_count;
    }

    public void setLender_count(int lender_count) {
        this.lender_count = lender_count;
    }
}