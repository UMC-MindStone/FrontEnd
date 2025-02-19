package com.example.mindstone.domain.entity

data class EmotionNoteStressResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: StressEmotionResult?
)

data class StressEmotionResult(
    val id: Int,
    val emotion: String,
    val emotionFigure: Int,
    val content: String
)
