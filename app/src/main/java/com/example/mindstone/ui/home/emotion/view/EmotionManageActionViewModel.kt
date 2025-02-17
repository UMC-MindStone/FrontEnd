package com.example.mindstone.ui.home.emotion.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionGPTResponse
import com.example.mindstone.domain.entity.EmotionStressResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionManageActionViewModel : ViewModel() {

    private val _recommendations = MutableLiveData<List<String>>()
    val recommendations: LiveData<List<String>> get() = _recommendations

    private val _aiRecommendations = MutableLiveData<List<String>>()
    val aiRecommendations: LiveData<List<String>> get() = _aiRecommendations

    private val apiService = RetrofitClient.emotionStressService


    // 사용자가 설정한 관리 행동 추천
    fun fetchStressRecommendations(token: String) {
        apiService.getStressRecommendations(token).enqueue(object : Callback<EmotionStressResponse> {
            override fun onResponse(call: Call<EmotionStressResponse>, response: Response<EmotionStressResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _recommendations.postValue(response.body()!!.result)
                }
            }
            override fun onFailure(call: Call<EmotionStressResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch recommendations", t)
            }
        })
    }


    // AI (GPT) 가 관리 행동 추천
    fun fetchAIStressRecommendations(token: String, previousRecommand: String) {
        apiService.getGPTStressRecommendations(token, previousRecommand).enqueue(object : Callback<EmotionGPTResponse> {
            override fun onResponse(call: Call<EmotionGPTResponse>, response: Response<EmotionGPTResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val recommandList = response.body()!!.result.recommand.split(",").map { it.trim() }
                    _aiRecommendations.postValue(recommandList) // ✅ AI 추천도 3개 업데이트
                } else {
                    Log.e("API_ERROR", "Failed to get AI recommendation: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<EmotionGPTResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed to connect to AI recommendation API", t)
            }
        })
    }
}
