package com.example.mindstone.ui.home.diary

import android.util.Log
import com.example.mindstone.data.remote.DiaryResult
import com.example.mindstone.data.remote.DiaryUpdateRequest
import com.example.mindstone.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

object DiaryRepository {
    private val diaryService = RetrofitClient.diaryService

    fun getDiaryByDate(date: String, onSuccess: (DiaryResult) -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = diaryService.getDiary(date)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        onSuccess(body.result)
                    } else {
                        onFailure(body?.message ?: "Unknown error")
                    }
                } else {
                    onFailure("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: HttpException) {
                onFailure("HttpException: ${e.message}")
            } catch (e: Exception) {
                onFailure("Exception: ${e.message}")
            }
        }
    }

    fun getDiaryById(id : Int, onSuccess: (DiaryResult) -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = diaryService.getDiaryById(id)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null && body.isSuccess){
                        onSuccess(body.result)
                    } else {
                        onFailure(body?.message ?: "Unknown error")}
                } else {
                    onFailure("Error: ${response.code()} ${response.message()}")
                }

            } catch (e: HttpException){
                onFailure("HttpException: ${e.message}")
            } catch (e: Exception){
                onFailure("Exception: ${e.message}")
            }
        }
    }
    fun saveDiary(diaryRequestBody: RequestBody,
                  imageParts: List<MultipartBody.Part>,
                  onSuccess: () -> Unit,
                  onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("UploadDebug", "🛠 saveDiary() 실행됨")

            try {
                Log.d("UploadDebug", "🚀 Retrofit 요청 시작!")
                val response = diaryService.saveDiary(diaryRequestBody, imageParts)

                Log.d("UploadDebug", "✅ Retrofit 응답 받음!")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        onSuccess() // 성공 처리
                    } else {
                        onFailure(body?.message ?: "Unknown error")
                    }
                } else {
                    onFailure("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UploadDebug", "❌ saveDiary() 내부 오류: ${e.message}")
                onFailure("Exception: ${e.message}")
            }
        }
    }
    fun updateDiary(diaryRequest: RequestBody,
                    imageParts: List<MultipartBody.Part>,
                    onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = diaryService.updateDiary(diaryRequest, imageParts)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        onSuccess()
                    } else {
                        onFailure(body?.message ?: "Unknown error")
                    }
                } else {
                    onFailure("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                onFailure("Exception: ${e.message}")
            }
        }
    }

}
