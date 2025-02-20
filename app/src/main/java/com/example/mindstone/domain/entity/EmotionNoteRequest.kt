package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class EmotionNoteRequest(
    @SerializedName("emotion") val emotion: String, // ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS
    @SerializedName("emotionFigure") val emotionFigure: Int,
    @SerializedName("content") val content: String
)