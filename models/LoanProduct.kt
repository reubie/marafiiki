package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class LoanProduct(

	@field:SerializedName("agent_interest_rate")
	val agentInterestRate: Double? = null,

	@field:SerializedName("loan_product_name")
	val loanProductName: String? = null,

	@field:SerializedName("notification_cycle")
	val notificationCycle: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("min_amount")
	val minAmount: Int? = null,

	@field:SerializedName("repayment_cycle")
	val repaymentCycle: Int? = null,

	@field:SerializedName("sender_id")
	val senderId: String? = null,

	@field:SerializedName("dc_rate")
	val dcRate: Double? = null,

	@field:SerializedName("grace_period")
	val gracePeriod: Int? = null,

	@field:SerializedName("rollover_no")
	val rolloverNo: Int? = null,

	@field:SerializedName("access_type_id")
	val accessTypeId: Int? = null,

	@field:SerializedName("status_id")
	val statusId: Int? = null,

	@field:SerializedName("date_modified")
	val dateModified: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("rollover_rate")
	val rolloverRate: Double? = null,

	@field:SerializedName("lender_interest_rate")
	val lenderInterestRate: Double? = null,

	@field:SerializedName("paybill")
	val paybill: String? = null,

	@field:SerializedName("crb_listing")
	val crbListing: Boolean? = null,

	@field:SerializedName("max_amount")
	val maxAmount: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("debt_collection")
	val debtCollection: Boolean? = null,

	@field:SerializedName("loan_type_id")
	val loanTypeId: Int? = null,

	@field:SerializedName("loan_type")
	val loanType: LoanType? = null
)