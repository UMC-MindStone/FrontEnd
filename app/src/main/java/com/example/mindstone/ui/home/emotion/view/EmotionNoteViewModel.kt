package com.example.mindstone.ui.home.emotion.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.EmotionNoteService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionNoteViewModel : ViewModel() {

    private val _emotionNoteResponse = MutableLiveData<EmotionNoteResponse?>()
    val emotionNoteResponse: LiveData<EmotionNoteResponse?> get() = _emotionNoteResponse

    private val emotionNoteService: EmotionNoteService = RetrofitClient.emotionNoteService

    fun postEmotionNote(token: String, emotion: String, emotionFigure: Int, content: String) {
        val request = EmotionNoteRequest(emotion, emotionFigure, content)
        emotionNoteService.postEmotionNote("Bearer $token", request).enqueue(object :
            Callback<EmotionNoteResponse> {
            override fun onResponse(call: Call<EmotionNoteResponse>, response: Response<EmotionNoteResponse>) {
                if (response.isSuccessful) {
                    _emotionNoteResponse.postValue(response.body())
                } else {
                    _emotionNoteResponse.postValue(null)
                }
            }

            override fun onFailure(call: Call<EmotionNoteResponse>, t: Throwable) {
                _emotionNoteResponse.postValue(null)
            }
        })
    }
}

