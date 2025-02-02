package com.example.mindstone.repository

import android.util.Log
import com.example.mindstone.data.model.SurveyRequest
import com.example.mindstone.data.model.SurveyResponse
import com.example.mindstone.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyRepository {
    fun submitSurvey(request: SurveyRequest, onResult: (Boolean, String) -> Unit) {
        RetrofitClient.apiService.submitSurvey(request).enqueue(object : Callback<SurveyResponse> {
            override fun onResponse(call: Call<SurveyResponse>, response: Response<SurveyResponse>) {
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "설문 제출 성공")
                } else {
                    onResult(false, "서버 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SurveyResponse>, t: Throwable) {
                Log.e("SurveyRepository", "❌ 네트워크 오류 발생: ${t.message}")
                onResult(false, "네트워크 오류 발생: ${t.message}")
            }
        })
    }
}
