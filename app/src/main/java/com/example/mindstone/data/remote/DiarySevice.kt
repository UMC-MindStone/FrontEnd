package com.example.mindstone.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate

interface DiarySevice {
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("api/diary/create")
    suspend fun createDiary(@Body request: DiaryCreateRequest): Response<DiaryCreateResponse>

    @GET("api/diary/{date}")
    suspend fun getDiary(
        @Path("date") date: String
    ): Response<DiaryResponse>

    @POST("api/diary/save")
    suspend fun saveDiary(
        @Body request: DiarySaveRequest
    ): Response<DiarySaveResponse>

    @PATCH("api/diary")
    suspend fun updateDiary(
        @Body request: DiaryUpdateRequest
    ): Response<DiaryUpdateResponse>
}


// 일기 수정
data class DiaryUpdateRequest(
    val diaryDTO: DiaryEditDTO,
    val image: List<String>
)

data class DiaryEditDTO(
    val date : String,
    val emotion : String,
    val content: String
)

data class DiaryUpdateResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: DiaryResult
)
// 일기 자동 생성 객체들

data class DiaryCreateRequest(
    val bodyPart : String,
    val date : String
)

data class DiaryCreateResponse(
    val isSuccess: Boolean,
    val code : String,
    val message : String,
    val result : DiaryCreateResult
)

data class DiaryCreateResult(
    val content : String,
    val bodyPart : String
)


// 일기 날짜 기준 요청
data class DiaryResponse(
    val isSuccess: Boolean,
    val code : String,
    val message: String,
    val result : DiaryResult

)
data class DiaryResult(
    val id : Int,
    val date : String,
    val emotion : String,
    val title : String,
    val content : String,
    val imagePath: Array<String>
)


// 일기 저장
data class DiarySaveRequest(
    val diaryDTO: DiaryDTO,
    val image: List<String>
)

data class DiarySaveResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: DiaryResult
)

data class DiaryDTO(
    val date: String,
    val title: String,
    val emotion: String,
    val content: String,
    val impressiveThing: String
)