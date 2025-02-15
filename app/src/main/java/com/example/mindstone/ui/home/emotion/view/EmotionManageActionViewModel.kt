package com.example.mindstone.ui.home.emotion.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.EmotionStressService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionManageActionViewModel : ViewModel() {

    private val _recommendations = MutableLiveData<List<String>>()
    val recommendations: LiveData<List<String>> get() = _recommendations

    private val apiService = RetrofitClient.emotionStressService

    fun fetchStressRecommendations(token: String) {
        apiService.getStressRecommendations(token).enqueue(object : Callback<EmotionResponse> {
            override fun onResponse(call: Call<EmotionResponse>, response: Response<EmotionResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _recommendations.value = response.body()!!.result
                }
            }

            override fun onFailure(call: Call<EmotionResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch recommendations", t)
            }
        })
    }
}
