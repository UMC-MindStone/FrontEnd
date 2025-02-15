package com.example.mindstone.data.remote

import android.content.Context
import android.util.Log
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.SurveyService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://15.165.241.217:8080/" // 서버 URL

    // ✅ AccessToken 자동 추가 Interceptor
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        val noAuthEndpoints = listOf("/api/auth/login", "/api/auth/signup", "/api/auth/forgot-password")

        if (!noAuthEndpoints.any { original.url.encodedPath.contains(it) }) {
            val accessToken = PreferenceManager.getAccessToken()
            if (!accessToken.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }
        }
        val request = requestBuilder.build()
        Log.d("API_AUTH", "✅ 최종 요청 헤더: ${request.headers}")
        chain.proceed(request)
    }


    // ✅ OkHttpClient 설정
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .addInterceptor(authInterceptor) // ✅ 모든 요청에 AccessToken 추가
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // ✅ 요청 & 응답 Body 전체 로깅
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    // ✅ Gson 설정 (JSON 파싱 최적화)
    private val gson = GsonBuilder()
        .setLenient() // JSON 파싱 오류 방지
        .disableHtmlEscaping() // 한글 깨짐 방지
        .serializeNulls()
        .create()

    // ✅ Retrofit 객체 생성 (공통 설정)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // ✅ 공통 OkHttpClient 적용
        .addConverterFactory(GsonConverterFactory.create(gson)) // ✅ Gson 적용
        .build()

    // ✅ Retrofit Service 생성
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    // ✅ Login 및 Auth 서비스 설정
    val loginService: LoginService by lazy { retrofit.create(LoginService::class.java) }
    val authService: AuthService by lazy { retrofit.create(AuthService::class.java) }

    // ✅ SurveyService 제공 (AccessToken 자동 포함)
    val surveyService: SurveyService by lazy { retrofit.create(SurveyService::class.java) }
}
