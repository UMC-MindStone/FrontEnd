package com.example.mindstone.domain.entity

data class LoginResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: LoginResult?
)

data class LoginResult(
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val createdAt: String
)