package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class LoanType(

	@field:SerializedName("date_modified")
	val dateModified: String? = null,

	@field:SerializedName("notification_cycle")
	val notificationCycle: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("repayment_period")
	val repaymentPeriod: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("loan_type")
	val loanType: String? = null
)