package com.example.mindstone.data.remote

import com.example.mindstone.network.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SurveyService {
    @POST("/api/members/survey")
    fun sendNickname(
        @Header("Authorization") token: String,
        @Body userData: UserData
    ): Call<UserData>
}
