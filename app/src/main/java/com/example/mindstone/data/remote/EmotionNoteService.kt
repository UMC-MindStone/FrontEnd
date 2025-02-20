package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import com.example.mindstone.domain.entity.EmotionNoteStressRequest
import com.example.mindstone.domain.entity.EmotionNoteStressResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EmotionNoteService {

    @POST("/api/emotionnote")
    suspend fun postEmotionNote(
        @Header("Authorization") token: String, // 인증 토큰
        @Body requestBody: EmotionNoteRequest
    ): Response<EmotionNoteResponse>


    @POST("/api/emotionnote/stress")
    suspend fun postEmotionNoteStress(
        @Header("Authorization") token: String,
        @Body request: EmotionNoteStressRequest
    ): Response<EmotionNoteStressResponse>
}