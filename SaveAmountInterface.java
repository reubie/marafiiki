package com.marafiki.android.apply_loan;

import java.util.Map;

public interface SaveAmountInterface {

    void saveAmounttoActivity(int user_id);
    void saveLoanDatatoActivity(Map<String, Object> map);

    int returnAmounttoFragment();
}
