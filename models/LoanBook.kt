package com.marafiki.android.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class LoanBook {

        @field:SerializedName("amount_disbursed")
        val amountDisbursed: Float? = null

        @field:SerializedName("loan_balance")
        val loanBalance: Float? = null

        @field:SerializedName("agent_id")
        val agentId: Int? = null

        @field:SerializedName("loan_status_id")
        val loanStatusId: Int? = null

        @field:SerializedName("loan_product_id")
        val loanProductId: Int? = null

        @field:SerializedName("ref_no")
        val refNo: String? = null

        @field:SerializedName("dc_rate")
        val dcRate: Any? = null

        @field:SerializedName("lender_id")
        val lenderId: Int? = null

        @field:SerializedName("lender_int_rate")
        val lenderIntRate: Float? = null

        @field:SerializedName("date_modified")
        val dateModified: String? = null

        @field:SerializedName("user_id")
        val userId: Int? = null

        @field:SerializedName("rollover_rate")
        val rolloverRate: Any? = null

        @field:SerializedName("repayment_date")
        val repaymentDate: Date? = null

        @field:SerializedName("maturity_date")
        val maturityDate: Date? = null

        @field:SerializedName("date_disbursed")
        val dateDisbursed: Date? = null

        @field:SerializedName("repayment_schedule")
        val repaymentSchedule: List<RepaymentScheduleItem?>? = null

        @field:SerializedName("borrower")
        val borrower: Borrower? = null

        @field:SerializedName("loan_status")
        val loanStatus: LoanStatus? = null

        @field:SerializedName("loan_product")
        val loanProduct: LoanProduct? = null

        @field:SerializedName("agent_int_rate")
        val agentIntRate: Float? = null

        @field:SerializedName("id")
        val id: Int? = null

        fun getGroupDate(): String {
                val formatter = SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault())
                return formatter.format(dateDisbursed)
        }

        fun getFormatRepaymentDate(): String {
                val formatter = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
                return formatter.format(repaymentDate)
        }

        fun getFormatMaturityDate(): String {
                val formatter = SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault())
                return formatter.format(maturityDate)
        }

}