package com.example.mindstone.data.remote

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://15.165.241.217:8080/" // 서버 URL

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            val responseBody = originalResponse.body

            val modifiedBody = responseBody?.let {
                val responseString = it.string() // 응답을 문자열로 변환
                val utf8ResponseString = String(responseString.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8) // UTF-8로 변환
                ResponseBody.create(it.contentType(), utf8ResponseString)
            }

            originalResponse.newBuilder()
                .body(modifiedBody)
                .build()
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
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

    // ✅ Retrofit 객체 생성 (OkHttpClient 추가)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // OkHttpClient 적용
        .addConverterFactory(GsonConverterFactory.create(gson)) // Gson 적용
        .build()

    // ✅ Retrofit Service 생성
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
    // 여기에 signupService 선언해서 썼는데 그때는 엄청난 오류가 발생했습니다...
    // 만약 하다가 안되면, 각 액티비티에서 retrofit 객체 선언하셔서 사용해보는 것도 방법일 것 같습니다

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val emotionStressService: EmotionStressService by lazy {
        retrofit.create(EmotionStressService::class.java)
    }
}
