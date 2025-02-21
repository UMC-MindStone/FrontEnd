package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class NicknameUpdateResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Map<String, String>? // ✅ additionalProp1,2,3이 동적일 가능성이 있어 Map으로 처리
)
