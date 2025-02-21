package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class PasswordUpdateResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Map<String, String>? // ✅ 동적 응답을 Map으로 처리
)
