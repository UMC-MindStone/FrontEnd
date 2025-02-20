package com.example.mindstone.data.remote

import com.example.mindstone.ui.emotion.viewmodel.EmotionCalendarResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface EmotionCalendarService {
    @GET("/api/diary/calendar")
    fun getEmotionCalendar(
        @Header("Authorization") token: String,
        @Query("Year") year: Int,
        @Query("Month") month: Int
    ): Call<EmotionCalendarResponse>
}