package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class FindEmailRequest(
    @SerializedName("email") val email: String
)
