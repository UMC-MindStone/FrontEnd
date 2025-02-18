package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EmotionNoteService {
    @POST("/api/emotionnote")
    fun postEmotionNote(
        @Header("Authorization") token: String, // 인증 토큰
        @Body requestBody: EmotionNoteRequest
    ): Call<EmotionNoteResponse>
}