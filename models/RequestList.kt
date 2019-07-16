package com.marafiki.android.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class RequestList {

    @field:SerializedName("id")
    var id: Int? = null

    @field:SerializedName("user_id")
    var userId: Int? = null

    @field:SerializedName("date_requested")
    var dateRequested: Date? = null

    @field:SerializedName("access_type")
    var accessType: AccessType? = null

    @field:SerializedName("ref_no")
    var refNo: String? = null

    @field:SerializedName("loan_status_id")
    var loanStatusId: Int? = null

    @field:SerializedName("date_expired")
    var dateExpired: String? = null

    @field:SerializedName("access_type_id")
    var accessTypeId: Int? = null

    @field:SerializedName("lender_id")
    var lenderId: Any? = null

    @field:SerializedName("expired")
    var expired: Boolean? = null

    @field:SerializedName("date_modified")
    var dateModified: String? = null

    @field:SerializedName("amount_requested")
    var amountRequested: Float? = null
    
    @field:SerializedName("loan_type_id")
    var loanTypeId: Int? = null

    @field:SerializedName("proposed_interest_rate")
    var proposedInterestRate: Float? = null

    @field:SerializedName("loan_type")
    var loanType: RequestType? = null

    fun getGroupDate(): String {
        val formatter = SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault())
        return formatter.format(dateRequested)
    }

    fun getRequestDate(): String {
        val formatter = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        return formatter.format(dateRequested)
    }


}