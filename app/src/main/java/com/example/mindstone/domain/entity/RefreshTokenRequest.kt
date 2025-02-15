package com.example.mindstone.domain.entity

data class RefreshTokenRequest(
    val refreshToken: String,
    val email: String
)