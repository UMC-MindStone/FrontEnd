package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionGPTResponse
import com.example.mindstone.domain.entity.EmotionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface EmotionStressService {
    @GET("/api/emotionWay/stress")
    fun getStressRecommendations(@Header("Authorization") token: String): Call<EmotionResponse>

    @GET("/api/emotionWay/stress/gpt")
    fun getGPTStressRecommendations(
        @Header("Authorization") token: String,
        @Query("previousRecommand") previousRecommand: String = ""
    ): Call<EmotionGPTResponse>
}