package com.marafiki.android.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordsModel implements Records, Serializable {

    private String id;
    private String period;
    private int amount;
    private String name;
    private String interest_rate;
    private int type;
    private Date repayment_date;
    private Date date_issued;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public Date getRepayment_date() {
        return repayment_date;
    }

    public void setRepayment_date(Date repayment_date) {
        this.repayment_date = repayment_date;
    }

    public Date getDate_issued() {
        return date_issued;
    }

    public void setDate_issued(Date date_issued) {
        this.date_issued = date_issued;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFormatDateIssued() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        return formatter.format(date_issued);
    }

    public String getFormatRepaymentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        return formatter.format(repayment_date);
    }
}