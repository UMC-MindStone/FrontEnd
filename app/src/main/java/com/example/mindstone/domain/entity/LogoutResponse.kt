package com.example.mindstone.domain.entity

data class LogoutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)
