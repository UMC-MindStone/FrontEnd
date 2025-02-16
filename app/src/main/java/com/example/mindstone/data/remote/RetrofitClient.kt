package com.example.mindstone.data.remote

import android.content.Context
import android.util.Log
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.SurveyService
import com.example.mindstone.domain.entity.RefreshTokenRequest
import com.example.mindstone.domain.entity.RefreshTokenResponse
import com.example.mindstone.domain.entity.RefreshTokenResult
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://15.165.241.217:8080/" // 서버 URL

    // ✅ AccessToken 자동 추가 Interceptor
//    private val authInterceptor = Interceptor { chain ->
//        val original = chain.request()
//        val requestBuilder = original.newBuilder()
//        val noAuthEndpoints = listOf("/api/auth/login", "/api/auth/signup", "/api/auth/forgot-password")
//
//        if (!noAuthEndpoints.any { original.url.encodedPath.contains(it) }) {
//            val accessToken = PreferenceManager.getAccessToken()
//            if (!accessToken.isNullOrEmpty()) {
//                requestBuilder.header("Authorization", "Bearer $accessToken")
//            }
//        }
//        val request = requestBuilder.build()
//        Log.d("API_AUTH", "✅ Final Request Header: ${request.headers}")
//        chain.proceed(request)
//    }
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        val noAuthEndpoints = listOf("/api/auth/login", "/api/auth/signup", "/api/auth/forgot-password")

        if (!noAuthEndpoints.any { original.url.encodedPath.contains(it) }) {
            val accessToken = PreferenceManager.getAccessToken()
            Log.d("API_AUTH", "Interceptor에서 가져온 AccessToken: $accessToken") // ✅ 확인용 로그

            if (!accessToken.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        // ✅ 서버가 302 Redirect를 반환하면, RefreshToken을 사용해 새로운 AccessToken을 요청
        if (response.code == 302) {
            Log.e("API_ERROR", "🚨 서버가 302 Redirect를 반환! RefreshToken을 사용해 AccessToken 갱신 시도")

            val newTokenData = refreshAccessToken()
            if (newTokenData != null) {
                Log.d("API_AUTH", "✅ AccessToken 갱신 성공: ${newTokenData.accessToken}")

                // ✅ 새로운 AccessToken을 헤더에 추가하여 요청을 다시 보냄
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer ${newTokenData.accessToken}")
                    .build()

                return@Interceptor chain.proceed(newRequest) // 새 토큰으로 요청 재시도
            } else {
                Log.e("API_ERROR", "❌ RefreshToken을 사용한 AccessToken 갱신 실패. 다시 로그인 필요.")
            }
        }

        return@Interceptor response
    }

    private fun refreshAccessToken(): RefreshTokenResult? {
        val refreshToken = PreferenceManager.getRefreshToken()
        val email = PreferenceManager.getEmail()

        if (refreshToken.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.d("API_AUTH", "$refreshToken")
            Log.d("API_AUTH", "$email")
            Log.e("API_AUTH", "❌ RefreshToken 또는 Email이 없음. 다시 로그인 필요.")
            return null // RefreshToken이 없으면 로그인 화면으로 이동해야 함
        }

        Log.d("API_AUTH", "🔄 RefreshToken을 사용하여 새로운 AccessToken 요청 중...")

        return try {
            val response = RetrofitClient.authService.refreshAccessToken(RefreshTokenRequest(refreshToken, email)).execute()

            if (response.isSuccessful && response.body() != null) {
                val refreshTokenResponse: RefreshTokenResponse = response.body()!!
                if (refreshTokenResponse.isSuccess) {
                    Log.d("API_AUTH", "✅ AccessToken 갱신 성공!")

                    // ✅ 새로운 AccessToken & RefreshToken 저장
                    PreferenceManager.saveAccessToken(refreshTokenResponse.result.accessToken)
                    PreferenceManager.saveRefreshToken(refreshTokenResponse.result.refreshToken)

                    return refreshTokenResponse.result
                } else {
                    Log.e("API_AUTH", "❌ AccessToken 갱신 실패: ${refreshTokenResponse.message}")
                    null
                }
            } else {
                Log.e("API_AUTH", "❌ RefreshToken 사용 불가. ${response.code()}: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_AUTH", "❌ AccessToken 갱신 중 오류 발생: ${e.message}")
            null
        }
    }




    // ✅ OkHttpClient 설정
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .addInterceptor(authInterceptor)
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

    val diaryService: DiarySevice by lazy {
        retrofit.create(DiarySevice::class.java)
    }
    // ✅ SurveyService 제공 (AccessToken 자동 포함)
    val surveyService: SurveyService by lazy { retrofit.create(SurveyService::class.java) }

}
