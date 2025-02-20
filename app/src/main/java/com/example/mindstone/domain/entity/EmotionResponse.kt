package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class EmotionResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: EmotionData?
)

data class EmotionData(
    val date: String,
    @SerializedName("angerFigure") val anger: Int,
    @SerializedName("depressionFigure") val depression: Int,
    @SerializedName("sadFigure") val sad: Int,
    @SerializedName("calmFigure") val calm: Int,
    @SerializedName("joyFigure") val joy: Int,
    @SerializedName("thrillFigure") val thrill: Int,
    @SerializedName("happinessFigure") val happiness: Int
)
