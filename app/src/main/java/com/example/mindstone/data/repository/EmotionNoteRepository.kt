package com.example.mindstone.data.repository

import com.example.mindstone.data.remote.EmotionNoteService
import com.example.mindstone.domain.entity.EmotionNoteRequest
import com.example.mindstone.domain.entity.EmotionNoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmotionNoteRepository(private val service: EmotionNoteService) {

    fun postEmotionNote(token: String, request: EmotionNoteRequest, callback: (EmotionNoteResponse?) -> Unit) {
        service.postEmotionNote("Bearer $token", request).enqueue(object :
            Callback<EmotionNoteResponse> {
            override fun onResponse(call: Call<EmotionNoteResponse>, response: Response<EmotionNoteResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<EmotionNoteResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}