package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class Customer(

	@field:SerializedName("band_id")
	val bandId: Int? = null,

	@field:SerializedName("id_number")
	val idNumber: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("language_id")
	val languageId: Int? = null,

	@field:SerializedName("middle_name")
	val middleName: String? = null,

	@field:SerializedName("physical_location")
	val physicalLocation: Any? = null,

	@field:SerializedName("access_type_id")
	val accessTypeId: Int? = null,

	@field:SerializedName("status_id")
	val statusId: Int? = null,

	@field:SerializedName("user_type_id")
	val userTypeId: Int? = null,

	@field:SerializedName("is_agent")
	val isAgent: Boolean? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)