package com.example.mindstone.domain.entity

data class EmotionGPTResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: GPTRecommendation
)

data class GPTRecommendation(
    val recommand: String,  // 새로 추천받은 행동
    val previousRecommand: String  // 기존 추천된 행동
)