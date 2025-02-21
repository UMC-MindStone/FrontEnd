package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class NicknameUpdateRequest(
    @SerializedName("nickname") val nickname: String
)
