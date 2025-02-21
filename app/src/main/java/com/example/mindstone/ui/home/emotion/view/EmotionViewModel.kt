package com.example.mindstone.ui.home.emotion.view

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionData
import com.example.mindstone.domain.entity.EmotionResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionViewModel(application: Application) : AndroidViewModel(application) {

    private val _actualEmotionValues = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val actualEmotionValues: LiveData<MutableMap<String, Float>> get() = _actualEmotionValues

    private val _normalizedEmotionRatios = MutableLiveData<Map<String, Float>>()
    val normalizedEmotionRatios: LiveData<Map<String, Float>> get() = _normalizedEmotionRatios

    private val _dominantEmotion = MutableLiveData<String>()
    val dominantEmotion: LiveData<String> get() = _dominantEmotion

    private val sharedPreferences = application.getSharedPreferences("EmotionData", Context.MODE_PRIVATE)

    init {
        // ✅ 앱 실행 시 저장된 데이터 불러오기
        loadEmotionData()
    }

    fun fetchEmotionStatistics(authToken: String, emotionModel: EmotionModel) {
        Log.d("EmotionViewModel", "📡 Fetching Emotion Statistics...")

        val call = RetrofitClient.emotionService.getEmotionStatistics("Bearer $authToken")

        call.enqueue(object : Callback<EmotionResponse> {
            override fun onResponse(call: Call<EmotionResponse>, response: Response<EmotionResponse>) {
                if (response.isSuccessful) {
                    response.body()?.result?.let { emotionData ->
                        Log.d("EmotionViewModel", "✅ API Response: $emotionData")

                        val emotionMap = mutableMapOf(
                            "화남" to emotionData.angerFigure.toFloat(),
                            "우울" to emotionData.depressionFigure.toFloat(),
                            "슬픔" to emotionData.sadFigure.toFloat(),
                            "평온" to emotionData.calmFigure.toFloat(),
                            "기쁨" to emotionData.joyFigure.toFloat(),
                            "설렘" to emotionData.thrillFigure.toFloat(),
                            "행복" to emotionData.happinessFigure.toFloat()
                        )

                        _actualEmotionValues.value = emotionMap

                        // ✅ EmotionModel에 API 데이터를 전달하여 상태 업데이트
                        emotionModel.fetchEmotion(emotionMap)

                        Log.d("EmotionViewModel", "🔵 Synced API data with EmotionModel: $emotionMap")

                        // ✅ 감정 데이터를 SharedPreferences에 저장 (앱 재실행 시 유지)
                        saveEmotionData(emotionMap)
                    }
                } else {
                    Log.e("EmotionViewModel", "❌ API Error: ${response.errorBody()?.string()}")
                    _actualEmotionValues.value = mutableMapOf()
                }
            }

            override fun onFailure(call: Call<EmotionResponse>, t: Throwable) {
                Log.e("EmotionViewModel", "❌ API Failure: ${t.message}")
                _actualEmotionValues.value = mutableMapOf()
            }
        })
    }


    private fun saveEmotionData(emotionMap: Map<String, Float>) {
        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(emotionMap) // Map을 JSON 문자열로 변환하여 저장
        editor.putString("emotionRatios", jsonString)
        editor.apply()
    }

    // ✅ 앱 실행 시 저장된 감정 데이터를 불러오기
    private fun loadEmotionData() {
        val jsonString = sharedPreferences.getString("emotionRatios", null)
        if (jsonString != null) {
            val type = object : TypeToken<Map<String, Float>>() {}.type
            val ratios: Map<String, Float> = Gson().fromJson(jsonString, type)

            // ✅ Map을 MutableMap으로 변환하여 해결
            _actualEmotionValues.postValue(ratios.toMutableMap())
        }
    }

}


