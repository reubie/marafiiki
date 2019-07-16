package com.marafiki.android.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


class PendingLoans {

    @field:SerializedName("date_requested")
    var dateRequested: Date? = null

    @field:SerializedName("loan_product_name")
    var loanProductName: String? = null

    @field:SerializedName("amount_requested")
    var amountRequested: Int? = null

    @field:SerializedName("last_name")
    var lastName: String? = null

    @field:SerializedName("ref_no")
    var refNo: String? = null

    @field:SerializedName("rating")
    var rating: String? = null

    @field:SerializedName("loan_type_id")
    var loanTypeId: Int? = null

    @field:SerializedName("customer_id")
    var customerID: Int? = null

    @field:SerializedName("credit_score")
    var creditScore: Int? = null

    @field:SerializedName("r_id")
    var rId: Int? = null

    @field:SerializedName("child")
    var childData: List<ApplyLoanModel>? = null

    @field:SerializedName("proposed_interest_rate")
    var proposedInterestRate: Double? = null

    @field:SerializedName("lender_interest_rate")
    var lenderInterestRate: Double? = null

    @field:SerializedName("first_name")
    var firstName: String? = null

    @field:SerializedName("agent_name")
    var agentName: String? = null

    @field:SerializedName("p_id")
    var pId: Int? = null

    fun getRequestDate(): String {
        val formatter = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        return formatter.format(dateRequested)
    }
}
