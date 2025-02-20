package com.example.mindstone.ui.home.emotion.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionData
import com.example.mindstone.domain.entity.EmotionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionViewModel : ViewModel() {

    // ✅ 감정 데이터 API 응답 저장
    private val _emotionData = MutableLiveData<EmotionData?>()
    val emotionData: LiveData<EmotionData?> get() = _emotionData

    // ✅ 실제 감정 값 저장
    private val _actualEmotionValues = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val actualEmotionValues: LiveData<MutableMap<String, Float>> get() = _actualEmotionValues

    // ✅ 선택된 감정
    private val _selectedEmotion = MutableLiveData<String>()
    val selectedEmotion: LiveData<String> get() = _selectedEmotion

    // ✅ 감정 강도
    private val _emotionIntensity = MutableLiveData<Int>()
    val emotionIntensity: LiveData<Int> get() = _emotionIntensity

    /**
     * 🚀 감정 데이터 API 호출 (서버에서 감정 통계 가져오기)
     */
    fun fetchEmotionStatistics(authToken: String) {
        Log.d("EmotionViewModel", "📡 Fetching Emotion Statistics...")

        val call = RetrofitClient.emotionService.getEmotionStatistics("Bearer $authToken")

        call.enqueue(object : Callback<EmotionResponse> {
            override fun onResponse(call: Call<EmotionResponse>, response: Response<EmotionResponse>) {
                if (response.isSuccessful) {
                    response.body()?.result?.let { emotionData ->
                        _emotionData.value = emotionData  // ✅ API 원본 데이터 저장

                        Log.d("EmotionViewModel", "✅ API Response: $emotionData")

                        // ✅ API 데이터를 MutableMap<String, Float> 형태로 변환
                        val emotionMap = mutableMapOf(
                            "anger" to emotionData.anger.toFloat(),
                            "depression" to emotionData.depression.toFloat(),
                            "sad" to emotionData.sad.toFloat(),
                            "calm" to emotionData.calm.toFloat(),
                            "joy" to emotionData.joy.toFloat(),
                            "thrill" to emotionData.thrill.toFloat(),
                            "happiness" to emotionData.happiness.toFloat()
                        )

                        // ✅ 실제 감정 값 반영
                        _actualEmotionValues.value = emotionMap
                    }
                } else {
                    Log.e("EmotionViewModel", "❌ API Error: ${response.errorBody()?.string()}")
                    _emotionData.value = null
                }
            }

            override fun onFailure(call: Call<EmotionResponse>, t: Throwable) {
                Log.e("EmotionViewModel", "❌ API Failure: ${t.message}")
                _emotionData.value = null
            }
        })
    }

    // ✅ EmotionModel에서 감정 데이터를 전달받기 위한 함수 추가
    fun setSelectedEmotion(emotion: String, intensity: Int) {
        _selectedEmotion.value = emotion
        _emotionIntensity.value = intensity
        Log.d("EmotionViewModel", "✅ setSelectedEmotion 호출됨: $emotion, Intensity: $intensity")
    }

    /**
     * 🚀 사용자가 선택한 감정을 저장
     */
    fun selectEmotion(emotion: String, intensity: Int) {
        _selectedEmotion.value = emotion
        _emotionIntensity.value = intensity

        Log.d("EmotionViewModel", "🟢 Selected Emotion: $emotion, Intensity: $intensity")

        // ✅ 선택한 감정을 _actualEmotionValues에 반영
        val actualValues = _actualEmotionValues.value?.toMutableMap() ?: mutableMapOf()

        // ✅ 기존 값이 있다면 누적, 없다면 새로 추가
        val emotionKey = convertEmotionToApiKey(emotion)
        actualValues[emotionKey] = (actualValues[emotionKey] ?: 0f) + intensity.toFloat()

        _actualEmotionValues.value = actualValues

        Log.d("EmotionViewModel", "🔵 Updated Emotion Values: $actualValues")
    }

    /**
     * 🚀 감정 이름을 API 응답 키로 변환
     */
    private fun convertEmotionToApiKey(emotion: String): String {
        return when (emotion) {
            "화남" -> "anger"
            "우울" -> "depression"
            "슬픔" -> "sad"
            "평온" -> "calm"
            "기쁨" -> "joy"
            "설렘" -> "thrill"
            "행복" -> "happiness"
            else -> "calm"
        }
    }
}
