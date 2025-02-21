package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import com.example.mindstone.domain.entity.EmotionReportResponse
import com.example.mindstone.ui.emotion.viewmodel.EmotionCalendarResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface EmotionCalendarService {
    @GET("/api/diary/calendar")
    fun getEmotionCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<EmotionCalendarResponse>

    @GET("/api/emotion-report")
    suspend fun getEmotionReport(
        @Header("Authorization") token: String, // 인증 토큰
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<EmotionReportResponse>
}