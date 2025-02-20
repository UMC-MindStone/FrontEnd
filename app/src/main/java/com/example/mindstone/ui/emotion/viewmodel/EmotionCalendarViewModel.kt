package com.example.mindstone.ui.emotion.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.EmotionCalendarService
import com.example.mindstone.data.remote.HabitCalendarService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.HabitTotalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionCalendarViewModel: ViewModel() {

    private val token = PreferenceManager.getAccessToken() ?: ""
    private val apiService: EmotionCalendarService = RetrofitClient.emotionCalendarService

    //에러 메시지
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    private fun handleError(response: Response<*>, errorMessageLiveData: MutableLiveData<String>, tag: String) {
        val errorMessage = try {
            response.errorBody()?.source()?.buffer?.clone()?.readString(Charsets.UTF_8)
                ?: "알 수 없는 오류 발생"
        } catch (e: Exception) {
            "에러 메시지 변환 실패"
        }

        val error = "오류 발생: ${response.code()} | 메시지: $errorMessage"
        errorMessageLiveData.postValue(error)
        Log.e(tag, error)
    }

    // 공통 네트워크 실패 처리 함수
    private fun handleFailure(t: Throwable, errorMessageLiveData: MutableLiveData<String>, tag: String) {
        val error = "네트워크 오류: ${t.message}"
        errorMessageLiveData.postValue(error)
        Log.e(tag, error)
    }

    //emotionCalendar
    val _emotionCalendarData = MutableLiveData<EmotionCalendarResponse?>()
    val habitTotalData: LiveData<EmotionCalendarResponse?> get() = _emotionCalendarData

    fun getEmotionCalendar(year: Int, month: Int){

        val formattedToken = "Bearer $token"
        apiService.getEmotionCalendar(formattedToken, year, month)
            .enqueue(object : Callback<EmotionCalendarResponse>{
                override fun onResponse(
                    call: Call<EmotionCalendarResponse>,
                    response: Response<EmotionCalendarResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _emotionCalendarData.postValue(response.body())
                        Log.d("API_EMOTION_CALENDAR_SUCCESS", "데이터 로드 성공: ${response.body()}")
                    } else {
                        handleError(response, _errorMessage, "API_EMOTION_CALENDAR_ERROR")
                    }
                }

                override fun onFailure(call: Call<EmotionCalendarResponse>, t: Throwable) {
                    handleFailure(t, _errorMessage, "API_EMOTION_CALENDAR_FAILURE")
                }

            })
    }
}