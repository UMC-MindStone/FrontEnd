package com.example.mindstone.ui.auth.signup

import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("/api/v1/auth/signup")
    suspend fun signup(@Body signupRequest: signupRequest): signupResponse

    @POST("/api/v1/auth/send")
    suspend fun codeRequest(@Body codeRequest: codeRequest): codeResponse

    @POST("/api/v1/auth/validate")
    suspend fun codeValidate(@Body codeValidateRequest: codeValidateRequest): codeValidateResponse
}

data class signupRequest(
    val email : String,
    val password: String,
    val nickname: String,
    val mbti: String,
    val birthday: String,
    val job: String,
    val shareScope: Boolean,
    val marketingAgree: Boolean,
    val role: String
)

data class signupResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: result
)

data class result(
    val memberId: Int,
    val createdAt: String
)

data class codeValidateRequest(
    val email: String,
    val code: String
)

data class codeValidateResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: codeRequest

)

data class codeRequest(
    val email: String,
    val updatedAt: String,
)

data class codeResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: String

)