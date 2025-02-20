package com.example.mindstone.domain.entity

import com.google.gson.annotations.SerializedName

data class EmotionReportResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: EmotionReportResponseDTO?
)

data class EmotionReportResponseDTO(
    val totalReport : String,
    val bestEmotion : String,
    val totalEmtoionStatistic : List<WeeklyRecord>,
    val totalSummary : String
)

data class WeeklyRecord(
    @SerializedName("angerFigure") val anger: Int,
    @SerializedName("depressionFigure") val depression: Int,
    @SerializedName("sadFigure") val sad: Int,
    @SerializedName("calmFigure") val calm: Int,
    @SerializedName("joyFigure") val joy: Int,
    @SerializedName("thrillFigure") val thrill: Int,
    @SerializedName("happinessFigure") val happiness: Int
)