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
import com.example.mindstone.domain.entity.HabitHistory
import com.example.mindstone.domain.entity.HabitHistoryPatch
import com.example.mindstone.domain.entity.HabitHistoryPost
import com.example.mindstone.domain.entity.HabitHistoryResponse
import com.example.mindstone.domain.entity.HabitHistoryTest
import com.example.mindstone.domain.entity.HabitReportResponse
import com.example.mindstone.domain.entity.HabitTotalResponse
import com.example.mindstone.domain.entity.NewHabitHistoryPatch
import com.example.mindstone.domain.entity.NewHabitHistoryPatchResponse
import com.example.mindstone.domain.entity.NewHabitHistoryTimePost
import com.example.mindstone.domain.entity.NewHabitHistoryTimePostResponse

class HabitCalendarViewModel : ViewModel() {

    private val token = PreferenceManager.getAccessToken() ?: ""

    private val apiService: HabitCalendarService = RetrofitClient.habitCalendarService

    // MutableLiveData로 데이터를 관찰
    private val _calendarData = MutableLiveData<HabitCalendarResponse?>()
    val calendarData: LiveData<HabitCalendarResponse?> get() = _calendarData

    private val _habitTotalData = MutableLiveData<HabitTotalResponse?>()
    val habitTotalData: LiveData<HabitTotalResponse?> get() = _habitTotalData

    private val _habitCheckData = MutableLiveData<List<HabitHistory>>()
    val habitCheckData: LiveData<List<HabitHistory>> get() = _habitCheckData

    private val _habitReport = MutableLiveData<HabitReportResponse?>()
    val habitReport: LiveData<HabitReportResponse?> = _habitReport

    private val _habitHistoryId = MutableLiveData<Long>()
    val habitHistoryId: LiveData<Long> = _habitHistoryId

    // 오류 메시지
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // 오류 메시지 (HabitTotal 관련)
    private val _errorMessageTotal = MutableLiveData<String>()
    val errorMessageTotal: LiveData<String> get() = _errorMessageTotal

    // 오류 메시지 (HabitCheck 관련)
    private val _errorMessageCheck = MutableLiveData<String>()
    val errorMessageCheck: LiveData<String> get() = _errorMessageCheck

    // 공통 오류 처리 함수
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

    // API 호출: HabitCalendar 데이터
    fun fetchHabitCalendar(year: Int, month: Int) {
        val formattedToken = "Bearer $token"
        Log.d("Token",formattedToken)
        apiService.getHabitCalendar(formattedToken, year, month)
            .enqueue(object : Callback<HabitCalendarResponse> {
                override fun onResponse(call: Call<HabitCalendarResponse>, response: Response<HabitCalendarResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        _calendarData.postValue(response.body())
                        Log.d("API_SUCCESS", "데이터 로드 성공: ${response.body()}")
                    } else {
                        handleError(response, _errorMessage, "API1_ERROR")
                    }
                }

                override fun onFailure(call: Call<HabitCalendarResponse>, t: Throwable) {
                    handleFailure(t, _errorMessage, "API1_FAILURE")
                }
            })
    }

    // API 호출: HabitTotal 데이터
    fun fetchTotalHabit() {
        val formattedToken = "Bearer $token"
        apiService.getTotalHabit(formattedToken)
            .enqueue(object : Callback<HabitTotalResponse> {
                override fun onResponse(call: Call<HabitTotalResponse>, response: Response<HabitTotalResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        _habitTotalData.postValue(response.body())
                        Log.d("API_SUCCESS", "데이터 로드 성공: ${response.body()}")
                    } else {
                        handleError(response, _errorMessageTotal, "API2_ERROR")
                    }
                }

                override fun onFailure(call: Call<HabitTotalResponse>, t: Throwable) {
                    handleFailure(t, _errorMessageTotal, "API2_FAILURE")
                }
            })
    }

    // API 호출: HabitCheck 데이터
    fun fetchCheckHabit(formattedDate: String) {
        val formattedToken = "Bearer $token"
        apiService.getHabitHistory(formattedToken, formattedDate)
            .enqueue(object : Callback<HabitHistoryResponse> {
                override fun onResponse(call: Call<HabitHistoryResponse>, response: Response<HabitHistoryResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val habitList = response.body()?.result ?: emptyList()
                        _habitCheckData.postValue(habitList)  // 🔹 List만 저장
                        Log.d("API_SUCCESS", "Habit: $habitList")
                    } else {
                        handleError(response, _errorMessageCheck, "API3_ERROR")
                    }
                }

                override fun onFailure(call: Call<HabitHistoryResponse>, t: Throwable) {
                    handleFailure(t, _errorMessageCheck, "API3_FAILURE")
                }
            })
    }

    fun postCheckHabit(habitHistory: HabitHistoryPost){
        val formattedToken = "Bearer $token"

        apiService.postHabitHistory(formattedToken, habitHistory)
            .enqueue(object : Callback<HabitHistoryTest>{
                override fun onResponse(
                    call: Call<HabitHistoryTest>,
                    response: Response<HabitHistoryTest>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _habitHistoryId.postValue(response.body()?.result)
                        Log.d("API_POST", "SUCCESS")
                    } else {
                        handleError(response, _errorMessageCheck, "API4_ERROR")
                    }
                }

                override fun onFailure(call: Call<HabitHistoryTest>, t: Throwable) {
                    handleFailure(t, _errorMessageCheck, "API4_FAILURE")
                }

            })
    }

    fun patchCheckHabit(habitHistory: NewHabitHistoryPatch) {
        val formattedToken = "Bearer $token"

        apiService.patchHabitHistory(formattedToken, habitHistory)
            .enqueue(object  : Callback<NewHabitHistoryPatchResponse>{
                override fun onResponse(
                    call: Call<NewHabitHistoryPatchResponse>,
                    response: Response<NewHabitHistoryPatchResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("API_PATCH", "SUCCESS")
                    } else {
                        handleError(response, _errorMessageCheck, "API5_ERROR")
                    }
                }

                override fun onFailure(call: Call<NewHabitHistoryPatchResponse>, t: Throwable) {
                    handleFailure(t, _errorMessageCheck, "API5_FAILURE")
                }

            })
    }

    fun getHabitReport(year: Int, month: Int){
        val formattedToken = "Bearer $token"

        apiService.getHabitReport(formattedToken, year, month)
            .enqueue(object : Callback<HabitReportResponse>{
                override fun onResponse(
                    call: Call<HabitReportResponse>,
                    response: Response<HabitReportResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _habitReport.value = response.body()
                        Log.d("API_REPORT_GET", "SUCCESS")
                    } else {
                        handleError(response, _errorMessageCheck, "API6_ERROR")
                    }
                }

                override fun onFailure(call: Call<HabitReportResponse>, t: Throwable) {
                    handleFailure(t, _errorMessageCheck, "API6_FAILURE")
                }

            })
    }

    //시간 POST
    var habitTimeResult: Long? = null
        private set

    fun postHabitTime(habitTime: NewHabitHistoryTimePost, onResult: (Long?) -> Unit) {
        val formattedToken = "Bearer $token"

        apiService.postHabitTime(formattedToken, habitTime)
            .enqueue(object : Callback<NewHabitHistoryTimePostResponse> {
                override fun onResponse(
                    call: Call<NewHabitHistoryTimePostResponse>,
                    response: Response<NewHabitHistoryTimePostResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        habitTimeResult = response.body()?.result
                        Log.d("API_POST_TIME", "SUCCESS: $habitTimeResult")
                        onResult(habitTimeResult) // 콜백 함수로 프래그먼트에 값 전달
                    } else {
                        handleError(response, _errorMessageCheck, "API_POST_ERROR_TIME")
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<NewHabitHistoryTimePostResponse>, t: Throwable) {
                    handleFailure(t, _errorMessageCheck, "API_POST_FAILURE_TIME")
                    onResult(null)
                }
            })
    }

}

