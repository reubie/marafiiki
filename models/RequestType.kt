package com.marafiki.android.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class RequestType(

        @field:SerializedName("date_modified")
        val dateModified: Date? = null,

        @field:SerializedName("date_created")
        val dateCreated: Date? = null,

        @field:SerializedName("repayment_period")
        val repaymentPeriod: Int? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("loan_type")
        val loanType: String? = null
)