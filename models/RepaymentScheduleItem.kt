package com.marafiki.android.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class RepaymentScheduleItem {

    @field:SerializedName("date_debt_collection")
    val dateDebtCollection: Any? = null

    @field:SerializedName("loan_book_id")
    val loanBookId: Int? = null

    @field:SerializedName("agent_int_paid")
    val agentIntPaid: Float? = null

    @field:SerializedName("due_date")
    val dueDate: Date? = null

    @field:SerializedName("ref_no")
    val refNo: String? = null

    @field:SerializedName("prin_amount")
    val prinAmount: Float? = null

    @field:SerializedName("prin_amount_paid")
    val prinAmountPaid: Float? = null

    @field:SerializedName("lender_int")
    val lenderInt: Float? = null

    @field:SerializedName("date_modified")
    val dateModified: String? = null

    @field:SerializedName("agent_int")
    val agentInt: Float? = null

    @field:SerializedName("date_paid")
    val datePaid: Any? = null

    @field:SerializedName("narration")
    val narration: String? = null

    @field:SerializedName("date_rolled_over")
    val dateRolledOver: Any? = null

    @field:SerializedName("id")
    val id: Int? = null

    @field:SerializedName("lender_int_paid")
    val lenderIntPaid: Float? = null

    @field:SerializedName("punitive_charges")
    val punitiveCharges: List<PunitiveCharges?>? = null

    fun getGroupDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(dueDate)
    }
}