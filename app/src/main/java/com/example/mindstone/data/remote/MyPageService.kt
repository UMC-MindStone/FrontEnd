package com.example.mindstone.data.remote

import com.example.mindstone.domain.entity.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface MyPageService {
    @GET("api/members/info")
    fun getUserInfo(): Call<UserResponse>
}