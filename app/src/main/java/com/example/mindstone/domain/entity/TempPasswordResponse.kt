package com.example.mindstone.domain.entity

data class TempPasswordResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)
