package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class PasswordUpdateRequest(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)
