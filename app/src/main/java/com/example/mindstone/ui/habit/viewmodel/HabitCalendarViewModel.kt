package com.example.mindstone.ui.habit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.HabitCalendarService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.HabitCalendarResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.mindstone.data.local.PreferenceManager

class HabitCalendarViewModel : ViewModel() {

    val token = PreferenceManager.getAccessToken() ?: ""

    private val _calendarData = MutableLiveData<HabitCalendarResponse?>()
    val calendarData: LiveData<HabitCalendarResponse?> get() = _calendarData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val apiService: HabitCalendarService = RetrofitClient.habitCalendarService

    fun fetchHabitCalendar(year: Int, month: Int) {
        val formattedToken = "Bearer $token"

        apiService.getHabitCalendar(formattedToken, year, month)
            .enqueue(object : Callback<HabitCalendarResponse> {
                override fun onResponse(call: Call<HabitCalendarResponse>, response: Response<HabitCalendarResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        _calendarData.postValue(response.body())
                        Log.d("API_SUCCESS", "데이터 로드 성공: ${response.body()}")

                    } else {
                        val errorMessage = try {
                            response.errorBody()?.source()?.buffer?.clone()?.readString(Charsets.UTF_8) ?: "알 수 없는 오류 발생"
                        } catch (e: Exception) {
                            "에러 메시지 변환 실패"
                        }

                        val error = "오류 발생: ${response.code()} | 메시지: $errorMessage"
                        _errorMessage.postValue(error)
                        Log.e("API1_ERROR", error)
                    }
                }


                override fun onFailure(call: Call<HabitCalendarResponse>, t: Throwable) {
                    val error = "네트워크 오류: ${t.message}"
                    _errorMessage.postValue(error)
                    Log.e("API1_FAILURE", error)
                }
            })
    }
}