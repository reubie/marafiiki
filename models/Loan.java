package com.marafiki.android.models;

import java.util.Date;

public interface Loan {

    int getLoan_id();
    int getLoan_type();
    int getLoan_status_id();
    int getAccess_type_id();
    String getName(); //unavailable
    float getAmount();
    String getRisk(); //unavailable
    float getInterestRate();
    Date getDateBorrowed();
    String getFormatRepaymentDate();

}
