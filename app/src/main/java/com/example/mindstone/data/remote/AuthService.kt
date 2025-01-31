package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.FindEmailRequest
import com.example.mindstone.domain.entity.FindEmailResponse
import com.example.mindstone.domain.entity.TempPasswordRequest
import com.example.mindstone.domain.entity.TempPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/email")
    suspend fun findEmail(@Body request: FindEmailRequest): Response<FindEmailResponse>

    @POST("/api/auth/tempPassword")
    suspend fun sendTempPassword(@Body request: TempPasswordRequest): Response<TempPasswordResponse>
}
