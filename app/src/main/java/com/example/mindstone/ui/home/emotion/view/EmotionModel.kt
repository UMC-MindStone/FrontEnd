package com.example.mindstone.ui.home.emotion.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.R

class EmotionModel : ViewModel() {


    // 최근 선택한 감정 (캐릭터 상태 변경을 위해)
    private val _recentEmotion = MutableLiveData<String>()
    val recentEmotion: LiveData<String> get() = _recentEmotion

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

    // 활동한 시간 저장 (시)
    private val _activityHour = MutableLiveData<Int>().apply { value = 0 }
    val activityHour: LiveData<Int> get() = _activityHour
    // 활동한 시간 저장 (분)
    private val _activityMinute = MutableLiveData<Int>().apply { value = 0 }
    val activityMinute: LiveData<Int> get() = _activityMinute

    // 사용자가 입력한 감정 이유
    private val _emotionReason = MutableLiveData<String>()
    val emotionReason: LiveData<String> get() = _emotionReason

    // 부정적 감정 관리 여부
    private val _manageChoice = MutableLiveData<String>()
    val manageChoice: LiveData<String> get() = _manageChoice

    // 사용자가 입력한 관리 행동
    private val _userAction = MutableLiveData<String>()
    val userAction: LiveData<String> get() = _userAction

    // 관리 행동 후, 감정 선택
    private val _afterActionEmotion = MutableLiveData<String>()
    val afterActionEmotion: LiveData<String> get() = _afterActionEmotion


    // 커스텀뷰 이용
    // 감정 비율 (상태바 업데이트용)
    private val _emotionRatios = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val emotionRatios: LiveData<MutableMap<String, Float>> get() = _emotionRatios


    // 감정 데이터 저장
    fun setEmotionData(emotion: String, colorResId: Int, isPositive: Boolean) {
        _emotion.value = emotion
        _colorResId.value = colorResId
        _isPositive.value = isPositive
    }

//    // 감정 선택 (감정이 추가될 때마다 비율 조정 - 상태바, 캐릭터 변경)
//    fun selectEmotion(emotion: String, colorResId: Int, isPositive: Boolean) {
//        _recentEmotion.value = emotion
//        _colorResId.value = colorResId
//        _isPositive.value = isPositive
//
//        updateEmotionRatios(emotion)
//    }

    fun selectEmotion(emotion: String, colorResId: Int, isPositive: Boolean) {
        _emotion.value = emotion
        _colorResId.value = colorResId
        _isPositive.value = isPositive

        // 선택된 감정을 상태바 비율에 반영 (Float 변환 추가)
        addEmotion(emotion, (_intensity.value ?: 10).toFloat())
    }

    // 감정 비율 업데이트
//    fun addEmotion(emotion: String, value: Float) {
//        val currentRatios = _emotionRatios.value ?: mutableMapOf()
//
//        // ✅ 기존 감정 값이 있으면 누적, 없으면 새로 추가
//        currentRatios[emotion] = (currentRatios[emotion] ?: 0f) + value
//
//        // ✅ 전체 합을 계산 (감정의 총합)
//        val total = currentRatios.values.sum()
//
//        // ✅ 감정 값이 계속 누적된 상태에서, 전체 합 대비 비율을 유지
//        val emotionProportions = if (total > 0) {
//            currentRatios.mapValues { it.value / total } // 전체 합 대비 비율 변환
//        } else {
//            currentRatios
//        }
//
//        _emotionRatios.value = emotionProportions.toMutableMap() // ✅ 변경된 비율 적용
//    }

    fun addEmotion(emotion: String, value: Float) {
        val currentRatios = _emotionRatios.value ?: mutableMapOf()

        // 기존 감정 값이 있으면 누적, 없으면 새로 추가
        currentRatios[emotion] = (currentRatios[emotion] ?: 0f) + value

        // 전체 합 계산 (모든 감정의 총합)
        val total = currentRatios.values.sum()

        // ✅ 정확한 비율 계산 (각 감정을 전체 합 대비 비율로 변환)
        val normalizedRatios = currentRatios.mapValues { it.value / total }

        _emotionRatios.value = normalizedRatios.toMutableMap() // ✅ 변경된 비율 적용
    }





//    // 감정 비율 업데이트
//    private fun updateEmotionRatios(newEmotion: String) {
//        val currentRatios = _emotionRatios.value ?: mutableMapOf()
//
//        // 새로운 감정을 추가하거나 기존 감정을 증가
//        currentRatios[newEmotion] = (currentRatios[newEmotion] ?: 0f) + _intensity.value!!
//
//        // 전체 합이 100이 되도록 정규화
//        val total = currentRatios.values.sum()
//        if (total > 0) {
//            currentRatios.forEach { (key, value) ->
//                currentRatios[key] = (value / total) * 100
//            }
//        }
//        _emotionRatios.value = currentRatios
//    }

    // 상태바를 위한 감정 리스트 (부정→긍정 순서)
    fun getSortedEmotionRatios(): List<Pair<String, Float>> {
        val emotionPriority = listOf("화남", "우울", "슬픔", "평온", "기쁨", "설렘", "행복")
        return _emotionRatios.value?.toList()?.sortedBy { emotionPriority.indexOf(it.first) } ?: emptyList()
    }

    // 감정에 맞는 색상 반환 함수
    fun getEmotionColor(emotion: String): Int {
        return when (emotion) {
            "행복" -> R.color.happinessColor
            "설렘" -> R.color.thrillColor
            "기쁨" -> R.color.joyColor
            "평온" -> R.color.calmColor
            "화남" -> R.color.angerColor
            "우울" -> R.color.depressionColor
            "슬픔" -> R.color.sadColor
            else -> R.color.calmColor
        }
    }

    // 최근 감정에 맞는 캐릭터
    fun getCharacterForEmotion(emotion: String): Int {
        return when (emotion) {
            "행복" -> R.drawable.ic_happy
            "설렘" -> R.drawable.ic_excited
            "기쁨" -> R.drawable.ic_joy
            "평온" -> R.drawable.ic_calm_charac
            "화남" -> R.drawable.ic_angry
            "우울" -> R.drawable.ic_depressed
            "슬픔" -> R.drawable.ic_sad
            else -> R.drawable.ic_calm_charac
        }
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

    // 감정 이유 저장
    fun setEmotionReason(reason: String) {
        _emotionReason.value = reason
    }

    // 부정적 감정일 때, 관리 행동 여부
    fun setManageChoice(choice: String) {
        _manageChoice.value = choice
    }

    // 사용자가 입력한 관리 행동 저장
    fun setUserAction(action: String) {
        _userAction.value = action
    }

    // 활동 시간 설정
    fun setActivityTime(hour: Int, minute: Int) {
        _activityHour.value = hour
        _activityMinute.value = minute
    }

    // 관리 행동 후, 사용자의 감정
    fun setAfterActionEmotion(emotion: String) {
        _afterActionEmotion.value = emotion
    }
}