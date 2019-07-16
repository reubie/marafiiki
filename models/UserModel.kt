package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("total_loans")
	val loans: Int? = null
)