package com.marafiki.android.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mbariah on 7/11/18.
 */
public class StatementList implements Statement {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("account_id")
    private int account_id;

    @SerializedName("account_name")
    private String account_name;

    @SerializedName("ref_no")
    private String ref_no;

    @SerializedName("narration")
    private String desc;

    @SerializedName("transaction_date")
    private Date date;

    @SerializedName("dr")
    private float debit;

    @SerializedName("cr")
    private float credit;

    @SerializedName("balance_after")
    private float balance;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public float getDebit() {
        return debit;
    }

    public void setDebit(float debit) {
        this.debit = debit;
    }

    @Override
    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getFormatDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    public String getGroupDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault());
        return formatter.format(date);
    }


}
