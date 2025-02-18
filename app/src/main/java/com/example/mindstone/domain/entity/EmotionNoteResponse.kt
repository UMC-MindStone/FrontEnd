package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class EmotionNoteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: EmotionResult?
)

data class EmotionResult(
    @SerializedName("id") val id: Int,
    @SerializedName("emotion") val emotion: String,
    @SerializedName("emotionFigure") val emotionFigure: Int,
    @SerializedName("content") val content: String
)