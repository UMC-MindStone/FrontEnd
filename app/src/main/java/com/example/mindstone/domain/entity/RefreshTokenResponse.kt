package com.example.mindstone.domain.entity

data class RefreshTokenResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: RefreshTokenResult
)

data class RefreshTokenResult(
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val createdAt: String
)