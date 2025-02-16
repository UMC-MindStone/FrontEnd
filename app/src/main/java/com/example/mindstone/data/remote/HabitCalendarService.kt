package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.HabitCalendarResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface HabitCalendarService {
    @GET("/api/habit-calendar")
    fun getHabitCalendar(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<HabitCalendarResponse>
}