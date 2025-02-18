package com.example.mindstone.ui.home.emotion.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.repository.EmotionNoteRepository
import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse

class EmotionNoteViewModel(private val repository: EmotionNoteRepository) : ViewModel() {

    private val _emotionNoteResponse = MutableLiveData<EmotionNoteResponse?>()
    val emotionNoteResponse: LiveData<EmotionNoteResponse?> get() = _emotionNoteResponse

    fun postEmotionNote(token: String, emotion: String, emotionFigure: Int, content: String) {
        val request = EmotionNoteRequest(emotion, emotionFigure, content)
        repository.postEmotionNote(token, request) { response ->
            _emotionNoteResponse.postValue(response)
        }
    }
}