package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface EmotionApiService {
    @GET("/api/emotionnote/statistic")
    fun getEmotionStatistics(
        @Header("Authorization") token: String
    ): Call<EmotionResponse>
}
