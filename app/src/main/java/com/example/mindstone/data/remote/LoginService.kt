package com.example.mindstone.data.remote
import com.example.mindstone.domain.entity.LoginRequest
import com.example.mindstone.domain.entity.LoginResponse
import com.example.mindstone.domain.entity.RefreshTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST("/api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}