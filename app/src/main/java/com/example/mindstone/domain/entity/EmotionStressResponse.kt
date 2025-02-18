package com.example.mindstone.domain.entity

data class EmotionStressResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)
