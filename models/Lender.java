package com.marafiki.android.models;

import java.util.Date;

public interface Lender {

    String getLenderIds();

    String getLenderCount();

    Date getRepaymentDate();

    String getInterestRate();

    String getFormatDate();
}


