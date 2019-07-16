package com.marafiki.android.models;

import java.util.Date;

public interface Statement {

    int getId();

    int getAccount_id();

    float getDebit();

    float getCredit();
    Date getDate();
    String getFormatDate();

    float getBalance();

    int getUser_id();

    String getAccount_name();

    String getRef_no();

    String getDesc();
}


