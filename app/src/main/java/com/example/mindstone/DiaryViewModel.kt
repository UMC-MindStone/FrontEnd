package com.example.mindstone
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiaryViewModel : ViewModel() {

    // 일기 텍스트 데이터
    private val _diaryText = MutableLiveData<String>()
    val diaryText: LiveData<String> get() = _diaryText

    // 첨부된 이미지 데이터 리스트
    private val _images = MutableLiveData<List<Uri>>()
    val images: LiveData<List<Uri>> get() = _images

    // 텍스트 업데이트
    fun updateDiaryText(newText: String) {
        _diaryText.value = newText
    }

    // 이미지 리스트 업데이트
    fun addImages(uri : Uri){
        val currentImages = _images.value?.toMutableList() ?: mutableListOf()
        currentImages.add(uri)
        _images.value = currentImages
    }
}