package com.marafiki.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoanModel implements Loan {

    @SerializedName("id")
    @Expose
    private int loan_id;

    @SerializedName("loan_type_id")
    @Expose
    private int loan_type;

    @SerializedName("loan_status_id")
    @Expose
    private int loan_status_id;

    @SerializedName("access_type_id")
    @Expose
    private int access_type_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("amount_requested")
    @Expose
    private float amount;

    @SerializedName("risk")
    @Expose
    private String risk;

    @SerializedName("proposed_interest_rate")
    @Expose
    private float interestRate;

    @SerializedName("date_requested")
    @Expose
    private Date dateBorrowed;

    @SerializedName("loan_types")
    @Expose
    private int loan_types;


    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(int loan_type) {
        this.loan_type = loan_type;
    }

    public int getLoan_status_id() {
        return loan_status_id;
    }

    public void setLoan_status_id(int loan_status_id) {
        this.loan_status_id = loan_status_id;
    }

    public int getAccess_type_id() {
        return access_type_id;
    }

    public void setAccess_type_id(int access_type_id) {
        this.access_type_id = access_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "First Name";
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = "High";
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public int getLoan_types() {
        return loan_types;
    }

    @Override
    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getFormatRepaymentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd ''yy", Locale.getDefault());
        return formatter.format(dateBorrowed);
    }

}
