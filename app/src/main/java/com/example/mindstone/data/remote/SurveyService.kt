package com.example.mindstone.data.remote

import com.example.mindstone.data.local.UserData
import com.example.mindstone.network.SurveyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface SurveyService {
    @Headers("Content-Type: application/json")
    @PUT("/api/members/survey")
    fun sendUserData(
        @Body userData: UserData
    ): Call<SurveyResponse>
}