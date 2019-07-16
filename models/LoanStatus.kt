package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class LoanStatus(

	@field:SerializedName("date_earned")
	val dateEarned: String? = null,

	@field:SerializedName("loan_status")
	val loanStatus: String? = null,

	@field:SerializedName("date_modified")
	val dateModified: String? = null,

	@field:SerializedName("loan_status_desc")
	val loanStatusDesc: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)