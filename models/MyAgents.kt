package com.marafiki.android.models

import com.google.gson.annotations.SerializedName

data class MyAgents(

        @field:SerializedName("lender_id")
        val lenderId: Int? = null,

        @field:SerializedName("agent")
        val agent: Customer? = null,

        @field:SerializedName("agent_id")
        val agentId: Int? = null
)