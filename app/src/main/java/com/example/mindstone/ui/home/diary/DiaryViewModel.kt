package com.example.mindstone.ui.home.diary
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.MyApplication
import com.example.mindstone.R
import com.example.mindstone.data.remote.DiaryDTO
import com.example.mindstone.data.remote.DiarySaveRequest
import kotlinx.coroutines.launch

class DiaryViewModel : ViewModel() {

    // 일기 텍스트 데이터
    private val _diaryText = MutableLiveData<String>()
    val diaryText: LiveData<String> get() = _diaryText

    // 첨부된 이미지 데이터 리스트
    private val _images = MutableLiveData<List<Uri>>()
    val images: LiveData<List<Uri>> get() = _images

    private val _emotionIcon = MutableLiveData<Int>()
    val emotionIcon: LiveData<Int> get() = _emotionIcon

    private val _diaryTitle = MutableLiveData<String>()
    val diaryTitle: LiveData<String> get() = _diaryTitle

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // 텍스트 업데이트
    fun updateDiaryText(newText: String) {
        _diaryText.value = newText
    }

    fun updateEmotionIcon(newIcon: Int) {
        _emotionIcon.value = newIcon
    }

    fun updateImages(newImages: List<Uri>){
        _images.value= newImages
    }
    // 이미지 리스트 업데이트
    fun addImages(uri : Uri){
        val currentImages = _images.value?.toMutableList() ?: mutableListOf()
        currentImages.add(uri)
        _images.value = currentImages
    }

    fun fetchDiary(date: String) {
        viewModelScope.launch {
            DiaryRepository.getDiaryByDate(date,
                onSuccess = { diary ->
                    _diaryTitle.postValue(diary.title)
                    _diaryText.postValue(diary.content)
                    _emotionIcon.postValue(getEmotionIcon(diary.emotion))

                    // ✅ String (URL) 리스트 → Uri 리스트 변환
                    val uriList = diary.imagePath?.map { Uri.parse(it) } ?: emptyList()
                    _images.postValue(uriList)
                },
                onFailure = { error ->
                    _errorMessage.postValue(error)
                }
            )
        }
    }

    fun saveDiary(date: String, title: String) {
        viewModelScope.launch {
            val imageUrls = _images.value?.map { it.toString() } ?: emptyList() // ✅ Uri → String 변환

            val request = DiarySaveRequest(
                diaryDTO = DiaryDTO(
                    date = date,
                    title = title,
                    emotion = "행복",  // 예제 값
                    content = _diaryText.toString(),
                    impressiveThing = "기억에 남는 순간" // 예제 값
                ),
                image = imageUrls // ✅ 변환된 String 리스트 전송
            )

            DiaryRepository.saveDiary(request,
                onSuccess = {
                    // 성공 처리
                },
                onFailure = { error ->
                    _errorMessage.postValue(error)
                }
            )
        }
    }
    private fun getEmotionIcon(emotion: String): Int {
        return when (emotion) {
            "우울" -> R.drawable.ic_depression
            "화남" -> R.drawable.ic_angry
            "슬픔" -> R.drawable.ic_sad
            "차분" -> R.drawable.ic_calm
            "기쁨" -> R.drawable.ic_joy
            "행복" -> R.drawable.ic_happy
            "사랑" -> R.drawable.ic_romance
            else -> R.drawable.btn_nothing_normal
        }
    }
}