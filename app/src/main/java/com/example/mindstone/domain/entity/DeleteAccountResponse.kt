package com.example.mindstone.domain.entity

data class DeleteAccountResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)
