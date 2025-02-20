package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.HabitCalendarResponse
import com.example.mindstone.domain.entity.HabitHistory
import com.example.mindstone.domain.entity.HabitHistoryResponse
import com.example.mindstone.domain.entity.HabitHistoryTest
import com.example.mindstone.domain.entity.HabitTotalResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface HabitCalendarService {
    @GET("/api/habit-calendar")
    fun getHabitCalendar(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<HabitCalendarResponse>

    @GET("/api/habits/habitHistory/{date}")
    fun getHabitHistory(
        @Header("Authorization") token: String,
        @Path("date") date: String
    ): Call<HabitHistoryResponse>

    @GET("/api/habits")
    fun getTotalHabit(
        @Header("Authorization") token: String
    ): Call<HabitTotalResponse>

    @POST("/api/habits/habitHistory")
    fun postHabitHistory(
        @Header("Authorization") token: String,
        @Body habitHistory: HabitHistory
    ): Call<HabitHistoryTest>

    @PATCH("/api/habits/habitHistory")
    fun patchHabitHistory(
        @Header("Authorization") token: String,
        @Body habitHistory: HabitHistory
    ): Call<HabitHistoryTest>
}