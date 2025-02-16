package com.example.mindstone.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.local.UserData
import com.example.mindstone.data.repository.SurveyRepository

class SurveyViewModel : ViewModel() {

    private val _surveyMessage = MutableLiveData<String>()  // ✅ 응답 메시지를 저장할 LiveData
    val surveyMessage: LiveData<String> get() = _surveyMessage

    fun sendUserData(userData: UserData) {
        SurveyRepository.sendUserData( userData,
            onSuccess = { message ->
                _surveyMessage.postValue(message)  // ✅ 성공 메시지 업데이트
            },
            onFailure = { error ->
                Log.e("SurveyViewModel", "Survey request failed: $error")
                _surveyMessage.postValue("실패: $error")  // ✅ 에러 메시지 업데이트
            }
        )
    }
}
