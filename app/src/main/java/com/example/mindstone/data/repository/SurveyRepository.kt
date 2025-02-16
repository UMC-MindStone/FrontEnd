package com.example.mindstone.data.repository

import com.example.mindstone.data.local.UserData
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.data.remote.SurveyService
import com.example.mindstone.network.SurveyResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SurveyRepository {


    fun sendUserData(
        userData: UserData,
        onSuccess: (String) -> Unit,  // ✅ 성공 시 메시지 반환
        onFailure: (String) -> Unit   // ✅ 실패 시 메시지 반환
    ) {

        RetrofitClient.surveyService.sendUserData(userData).enqueue(object : Callback<SurveyResponse> {
            override fun onResponse(call: Call<SurveyResponse>, response: Response<SurveyResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val message = response.body()!!.message
                    onSuccess(message) // ✅ 성공 시 메시지 반환
                } else {
                    onFailure("Error: ${response.code()} ${response.message()}") // ✅ 실패 시 에러 메시지 반환
                }
            }

            override fun onFailure(call: Call<SurveyResponse>, t: Throwable) {
                onFailure("Failure: ${t.message}") // ✅ 네트워크 오류 처리
            }
        })
    }
}
