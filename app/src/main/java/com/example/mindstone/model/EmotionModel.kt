package com.example.mindstone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmotionModel : ViewModel() {

    // 감정 이름
    private val _emotion = MutableLiveData<String>()
    val emotion: LiveData<String> get() = _emotion

    // 감정 색상 (예: R.color.happyColor)
    private val _colorResId = MutableLiveData<Int>()
    val colorResId: LiveData<Int> get() = _colorResId

    // 감정의 긍정 여부
    private val _isPositive = MutableLiveData<Boolean>()
    val isPositive: LiveData<Boolean> get() = _isPositive

    // 감정 강도
    private val _intensity = MutableLiveData<Int>().apply { value = 10 }
    val intensity: LiveData<Int> get() = _intensity

    // 감정 데이터 설정
    fun setEmotionData(emotion: String, colorResId: Int, isPositive: Boolean) {
        _emotion.value = emotion
        _colorResId.value = colorResId
        _isPositive.value = isPositive
    }

    // 감정 강도
    fun setIntensity(value: Int) {
        _intensity.value = value
    }
    // 감정 강도 증가
    fun increaseIntensity() {
        if (_intensity.value!! < 100) {
            _intensity.value = _intensity.value!! + 10
        }
    }
    // 감정 강도 감소
    fun decreaseIntensity() {
        if (_intensity.value!! > 10) {
            _intensity.value = _intensity.value!! - 10
        }
    }
    // 감정 강도 기본값(10)으로 초기화
    fun resetIntensity() {
        _intensity.value = 10
    }
}
