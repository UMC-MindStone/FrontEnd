package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class FindEmailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: EmailResult?
)

data class EmailResult(
    @SerializedName("email") val email: String,
    @SerializedName("updatedAt") val updatedAt: String
)
