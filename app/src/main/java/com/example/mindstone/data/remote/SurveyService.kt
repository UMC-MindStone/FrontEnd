package com.example.mindstone.data.remote

import com.example.mindstone.data.model.SurveyRequest
import com.example.mindstone.data.model.SurveyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SurveyService {
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/api/members/survey")  // ✅ POST 요청 엔드포인트
    fun submitSurvey(@Body request: SurveyRequest): Call<SurveyResponse>
}
