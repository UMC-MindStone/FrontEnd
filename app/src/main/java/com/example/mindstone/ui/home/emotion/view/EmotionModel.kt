package com.example.mindstone.ui.home.emotion.view

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.R
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmotionData
import com.example.mindstone.domain.entity.EmotionResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


    // 감정 데이터 저장
    fun setEmotionData(emotion: String, colorResId: Int, isPositive: Boolean) {
        _emotion.value = emotion
        _colorResId.value = colorResId
        _isPositive.value = isPositive
    }


    // 커스텀뷰 이용
    // 감정 비율 (상태바 업데이트용)
    private val _emotionRatios = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val emotionRatios: LiveData<MutableMap<String, Float>> get() = _emotionRatios




    // 가장 비율 높은 감정
    private val _dominantEmotion = MutableLiveData<String>()
    val dominantEmotion: LiveData<String> get() = _dominantEmotion

    // 최근 선택한 감정 (캐릭터 상태 변경을 위해)
    private val _recentEmotion = MutableLiveData<String>()
    val recentEmotion: LiveData<String> get() = _recentEmotion

    fun selectEmotion(emotion: String, colorResId: Int, isPositive: Boolean) {
        _emotion.value = emotion
        _colorResId.value = colorResId
        _isPositive.value = isPositive

        // ✅ 최근 감정 업데이트
        _recentEmotion.value = emotion

        // 선택된 감정을 상태바 비율에 반영 (Float 변환 추가)
        addEmotion(emotion, (_intensity.value ?: 10).toFloat())
    }

    // 감정 비율 (UI 업데이트용, 100% 기준으로 변환됨)
    private val _normalizedEmotionRatios = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val normalizedEmotionRatios: LiveData<MutableMap<String, Float>> get() = _normalizedEmotionRatios

    // 🚀 실제 감정 값 저장 (값 변환 X, 추가 및 증가만)
    private val _actualEmotionValues = MutableLiveData<MutableMap<String, Float>>().apply { value = mutableMapOf() }
    val actualEmotionValues: LiveData<MutableMap<String, Float>> get() = _actualEmotionValues

    private fun fetchEmotion(data: MutableMap<String, Float>){
        _actualEmotionValues.value = data
        updateNormalizedRatios(data)
    }


    fun addEmotion(emotion: String, value: Float) {
        val actualValues = _actualEmotionValues.value?.toMutableMap() ?: mutableMapOf()

        // ✅ 기존 값이 있다면 누적, 없다면 새로 추가
        actualValues[emotion] = (actualValues[emotion] ?: 0f) + value

        // ✅ 100% 기준으로 UI 비율 변환
        updateNormalizedRatios(actualValues)

        // ✅ 디버깅 로그 출력
        Log.d("EmotionViewModel", "🟢 Added Emotion: $emotion, Value: $value")
        Log.d("EmotionViewModel", "🔵 Actual Emotion Values: $actualValues")

        _actualEmotionValues.value = actualValues

    }

    // ✅ UI 비율 변환 함수 (100% 기준으로 변환)
    private fun updateNormalizedRatios(actualValues: MutableMap<String, Float>) {
        val total = actualValues.values.sum()

        // ✅ UI에서 100% 기준으로 정규화된 감정 비율 저장
        val normalizedRatios = if (total > 0) {
            actualValues.mapValues { (_, v) -> (v / total) * 100 }.toMutableMap()
        } else {
            actualValues
        }

        // ✅ 디버깅 로그 출력
        Log.d("EmotionViewModel", "🔴 Total Emotion Sum: $total")
        Log.d("EmotionViewModel", "🟣 Normalized Emotion Ratios (for UI): $normalizedRatios")

        _normalizedEmotionRatios.value = normalizedRatios

        // ✅ UI 비율 기준으로 지배적인 감정 업데이트
        updateDominantEmotion()

        //saveEmotionState(context) // 3. 감정 비율 업데이트 시 자동 저장
    }

    // ✅ 지배적인 감정을 찾는 함수 (이제 UI에서 사용하는 비율 기반으로 계산)
    private fun updateDominantEmotion() {
        val normalizedValues = _normalizedEmotionRatios.value ?: return

        if (normalizedValues.isEmpty()) return

        // ✅ 가장 높은 비율 찾기
        val maxEmotionList = normalizedValues.entries
            .groupBy { it.value }
            .maxByOrNull { it.key }
            ?.value
            ?.map { it.key } ?: return

        // ✅ 비율이 같은 감정이 여러 개일 경우, 최근 감정을 우선 적용
        val selectedEmotion = if (maxEmotionList.size > 1) {
            _recentEmotion.value?.takeIf { it in maxEmotionList } ?: maxEmotionList.first()
        } else {
            maxEmotionList.first()
        }


        Log.d("EmotionViewModel", "🏆 최종 지배적인 감정: $selectedEmotion")
        _dominantEmotion.value = selectedEmotion
    }

//    private val _emotionData = MutableLiveData<EmotionData?>()
//    val emotionData: LiveData<EmotionData?> get() = _emotionData
//
//    fun fetchEmotionStatistics(authToken: String) {
//        val call = RetrofitClient.emotionService.getEmotionStatistics("Bearer $authToken")
//
//        call.enqueue(object : Callback<EmotionResponse> {
//            override fun onResponse(call: Call<EmotionResponse>, response: Response<EmotionResponse>) {
//                if (response.isSuccessful) {
//                    response.body()?.result?.let { emotionData ->
//                        _emotionData.value = emotionData  // 기존 로직 유지
//
//                        // ✅ API 응답을 MutableMap<String, Float> 형태로 변환 후 저장
//                        val emotionMap = mutableMapOf(
//                            "anger" to emotionData.anger.toFloat(),
//                            "depression" to emotionData.depression.toFloat(),
//                            "sad" to emotionData.sad.toFloat(),
//                            "calm" to emotionData.calm.toFloat(),
//                            "joy" to emotionData.joy.toFloat(),
//                            "thrill" to emotionData.thrill.toFloat(),
//                            "happiness" to emotionData.happiness.toFloat()
//                        )
//
//                        // ✅ _actualEmotionValues 업데이트
//                        fetchEmotion(emotionMap)
//                    }
//                } else {
//                    _emotionData.value = null  // 에러 처리
//                }
//            }
//            override fun onFailure(call: Call<EmotionResponse>, t: Throwable) {
//                _emotionData.value = null  // 네트워크 오류 처리
//            }
//        })
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