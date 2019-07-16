package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

class ApplyLoanModel(

        @field:SerializedName("product_name")
        var productName: String? = null,

        @field:SerializedName("first_name")
        var firstName: String? = null,

        @field:SerializedName("last_name")
        var lastName: String? = null,

        @field:SerializedName("customer_id")
        var customerID: Int? = null,

        @field:SerializedName("amount_requested")
        var amountRequested: Int? = null,

        @field:SerializedName("credit_score")
        var creditScore: Int? = null,

        @field:SerializedName("loan_type_id")
        var loanID: Int? = null,

        @field:SerializedName("loan_interest_rate")
        var loanInterestRate: Double? = null,

        @field:SerializedName("lender_loan_interest_rate")
        var lenderloanInterestRate: Double? = null,

        @field:SerializedName("returns")
        var returns: Double? = null,

        @field:SerializedName("r_id")
        var r_id: Int? = null,

        @field:SerializedName("p_id")
        var p_id: Int? = null

)
