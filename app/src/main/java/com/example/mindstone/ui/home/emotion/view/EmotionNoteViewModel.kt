package com.example.mindstone.ui.home.emotion.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.data.remote.EmotionNoteService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import com.example.mindstone.domain.entity.EmotionNoteStressRequest
import com.example.mindstone.domain.entity.EmotionNoteStressResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionNoteViewModel : ViewModel() {

    private val _emotionNoteResponse = MutableLiveData<EmotionNoteResponse?>()
    val emotionNoteResponse: LiveData<EmotionNoteResponse?> get() = _emotionNoteResponse

    private val _emotionNoteStressResponse = MutableLiveData<EmotionNoteStressResponse>()
    val emotionNoteStressResponse: LiveData<EmotionNoteStressResponse> = _emotionNoteStressResponse

    private val emotionNoteService: EmotionNoteService = RetrofitClient.emotionNoteService

    // ✅ 일반 감정 기록 API
    fun postEmotionNote(token: String, emotion: String, emotionFigure: Int, content: String) {
        Log.d("EmotionNoteViewModel", "📡 EmotionNote API 호출 - 감정: $emotion, 강도: $emotionFigure, 이유: $content")

        val request = EmotionNoteRequest(emotion, emotionFigure, content)
        emotionNoteService.postEmotionNote("Bearer $token", request).enqueue(object :
            Callback<EmotionNoteResponse> {
            override fun onResponse(call: Call<EmotionNoteResponse>, response: Response<EmotionNoteResponse>) {
                Log.d("EmotionNoteViewModel", "📡 EmotionNote API 응답 수신 - 코드: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    _emotionNoteResponse.value = response.body() // UI 스레드에서 즉시 반영!
                    Log.d("EmotionNoteViewModel", "✅ 감정 데이터 저장 성공! ID: ${response.body()?.result?.id}")
                } else {
                    Log.e("EmotionNoteViewModel", "❌ EmotionNote API 호출 실패 - 응답 코드: ${response.code()}, 메시지: ${response.errorBody()?.string()}")
                    _emotionNoteResponse.value = null // postValue() 대신 setValue() 사용
                }
            }

            override fun onFailure(call: Call<EmotionNoteResponse>, t: Throwable) {
                Log.e("EmotionNoteViewModel", "❌ EmotionNote API 호출 실패 - 네트워크 오류: ${t.message}")
                _emotionNoteResponse.value = null // postValue() 대신 setValue() 사용
            }
        })

    }



    // ✅ 부정적 감정 관리 기록 API
    fun postEmotionNoteStress(
        token: String,
        emotion: String,
        emotionFigure: Int,
        content: String,
        time: String,
        stressReason_id: Int,
        recommend: Boolean
    ) {
        viewModelScope.launch {
            try {
                val request = EmotionNoteStressRequest(
                    emotion = emotion,
                    emotionFigure = emotionFigure,
                    content = content,
                    time = time,
                    steressReson_id = stressReason_id,  // 스웨거 오타 변경되면 수정 필요 (stressReson_id)
                    recommend = recommend
                )

                val response = RetrofitClient.emotionNoteService.postEmotionNoteStress(token, request)

                if (response.isSuccessful && response.body() != null) {
                    _emotionNoteStressResponse.postValue(response.body())
                } else {
                    _emotionNoteStressResponse.postValue(
                        EmotionNoteStressResponse(false, "ERROR", "API 호출 실패", null)
                    )
                }
            } catch (e: Exception) {
                _emotionNoteStressResponse.postValue(
                    EmotionNoteStressResponse(false, "EXCEPTION", e.message ?: "알 수 없는 오류", null)
                )
            }
        }
    }
}


