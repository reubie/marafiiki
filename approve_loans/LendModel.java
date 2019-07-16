package com.marafiki.android.approve_loans;

import com.marafiki.android.models.PendingLoans;

import java.util.List;

public class LendModel {

    private String heading;

    private List<PendingLoans> singleItemModel;

    public LendModel(String heading, List<PendingLoans> singleItemModel) {
        this.heading = heading;
        this.singleItemModel = singleItemModel;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }


    public List<PendingLoans> getSingleItemArrayList() {
        return singleItemModel;
    }

    public void singleItemModel(List<PendingLoans> singleItemModel) {
        this.singleItemModel = singleItemModel;
    }
}
