package com.example.mindstone.ui.emotion.viewmodel

data class EmotionCalendarResponse(
    val isSuccess: String,
    val code: String,
    val message: String,
    val result: List<EmotionCalendar>
)

data class EmotionCalendar(
    val dateIndex: Int,
    val diaryId: Int,
    val emotion: String
)
