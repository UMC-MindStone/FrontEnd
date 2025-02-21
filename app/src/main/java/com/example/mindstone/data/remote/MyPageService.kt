package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.NicknameUpdateRequest
import com.example.mindstone.domain.entity.NicknameUpdateResponse
import com.example.mindstone.domain.entity.PasswordUpdateRequest
import com.example.mindstone.domain.entity.PasswordUpdateResponse
import com.example.mindstone.domain.entity.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH

interface MyPageService {

    @GET("api/members/info")
    fun getUserInfo(): Call<UserResponse>

    @PATCH("/api/members/nickname")
    @Headers("Content-Type: application/json") // ✅ Content-Type만 명시
    fun updateNickname(
        @Body request: NicknameUpdateRequest
    ): Call<NicknameUpdateResponse>

    @PATCH("/api/members/password")
    @Headers("Content-Type: application/json") // ✅ Content-Type 명시
    fun updatePassword(
        @Body request: PasswordUpdateRequest
    ): Call<PasswordUpdateResponse>
}