package com.example.mindstone.domain.entity

data class EmotionNoteStressRequest(
    val emotion: String,       // "ANGER", "DEPRESSION", "SAD"
    val emotionFigure: Int,
    val content: String,
    val time: String,
    val stressReasonId: Int
)
