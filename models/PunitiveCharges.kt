package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class PunitiveCharges(

	@field:SerializedName("amount")
	val amount: Float? = null,

	@field:SerializedName("amount_paid")
	val amountPaid: Float? = null,

	@field:SerializedName("repayment_schedule_id")
	val repaymentScheduleId: Int? = null,

	@field:SerializedName("account_id")
	val accountId: Int? = null,

	@field:SerializedName("date_updated")
	val dateUpdated: Any? = null,

	@field:SerializedName("date_modified")
	val dateModified: Any? = null,

	@field:SerializedName("date_paid")
	val datePaid: Any? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("narration")
	val narration: String? = null,

	@field:SerializedName("interest_rate")
	val interestRate: Float? = null,

	@field:SerializedName("ref_no")
	val refNo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)