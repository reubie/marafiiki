package com.marafiki.android.models;

import java.util.Date;

public interface Records {

    String getId();

    int getAmount();

    String getName();

    int getType();

    String getPeriod();

    String getInterest_rate();

    Date getRepayment_date();

    Date getDate_issued();

    String getFormatDateIssued();

    String getFormatRepaymentDate();

}
