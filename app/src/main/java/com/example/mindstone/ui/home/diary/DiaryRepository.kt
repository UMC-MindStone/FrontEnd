package com.example.mindstone.ui.home.diary

import android.util.Log
import com.example.mindstone.data.remote.DiaryResult
import com.example.mindstone.data.remote.DiarySaveRequest
import com.example.mindstone.data.remote.DiaryUpdateRequest
import com.example.mindstone.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    fun saveDiary(request: DiarySaveRequest, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = diaryService.saveDiary(request)

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
                onFailure("Exception: ${e.message}")
            }
        }
    }
    fun updateDiary(request: DiaryUpdateRequest, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = diaryService.updateDiary(request)

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
