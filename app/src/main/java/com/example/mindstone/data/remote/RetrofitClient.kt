package com.example.mindstone.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://15.165.241.217:8080/" // 나중에 백엔드 API URL로 변경

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    fun <T> create(service: Class<T>): T {
//        return retrofit.create(service)
//    }

    val signupService: SignupService by lazy {
        retrofit.create(SignupService::class.java)
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
}