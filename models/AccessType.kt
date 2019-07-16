package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class AccessType(

	@field:SerializedName("date_modified")
	val dateModified: Any? = null,

	@field:SerializedName("access_type_desc")
	val accessTypeDesc: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("access_type_code")
	val accessTypeCode: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)