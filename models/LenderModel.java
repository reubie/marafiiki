
package com.marafiki.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LenderModel implements Lender{

    @SerializedName("lender_ids")
    @Expose
    private String lenderIds;

    @SerializedName("lender_count")
    @Expose
    private String lenderCount;

    @SerializedName("repayment_date")
    @Expose
    private Date repaymentDate;

    @SerializedName("interest_rate")
    @Expose
    private String interestRate;


    public String getLenderIds() {
        return lenderIds;
    }

    public void setLenderIds(String lenderIds) {
        this.lenderIds = lenderIds;
    }

    public String getLenderCount() {
        return lenderCount;
    }

    public void setLenderCount(String lenderCount) {
        this.lenderCount = lenderCount;
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getFormatDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        return formatter.format(repaymentDate);
    }

}
