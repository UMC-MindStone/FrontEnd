package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface EmotionStressService {
    @GET("/api/emotionWay/stress")
    fun getStressRecommendations(@Header("Authorization") token: String): Call<EmotionResponse>
}