package com.example.mindstone.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.RetrofitClient

class TodayFinishViewModel : ViewModel() {
    fun DiaryCreateRequest(bundle : Bundle){
        val response = RetrofitClient.diaryService
    }
}