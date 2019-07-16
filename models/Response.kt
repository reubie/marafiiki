package com.marafiki.android.models


import com.google.gson.annotations.SerializedName
data class Response(

	@field:SerializedName("wallets")
	val wallets: Wallets? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)