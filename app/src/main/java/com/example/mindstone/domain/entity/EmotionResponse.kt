package com.example.mindstone.domain.entity

data class EmotionResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<String>  // API에서 추천 리스트를 받음
)
