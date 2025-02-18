package com.example.mindstone.ui.home.diary
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.MyApplication
import com.example.mindstone.R
import com.example.mindstone.data.remote.DiaryDTO
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DiaryViewModel : ViewModel() {

    // 일기 텍스트 데이터
    private val _diaryText = MutableLiveData<String>()
    val diaryText: LiveData<String> get() = _diaryText

    private val _diaryStatus = MutableLiveData<Int>()  // 0: 수정, 1: 저장
    val diaryStatus: LiveData<Int> get() = _diaryStatus

    private val _diaryExists = MutableLiveData<Boolean>()
    val diaryExists : LiveData<Boolean> get() = _diaryExists


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

                    // ✅ 기존 일기가 있으면 수정 (0), 없으면 저장 (1)
                    _diaryStatus.postValue(0)
                    // CalendarToDiaryFragment로 바로 이동
                    _diaryExists.postValue(true)
                },
                onFailure = { error ->
                    _errorMessage.postValue(error)
                    _diaryTitle.postValue("") // 제목 초기화
                    _diaryText.postValue("")  // 내용 초기화
                    _emotionIcon.postValue(R.drawable.btn_nothing_normal) // 기본 감정 아이콘 설정
                    _images.postValue(emptyList())

                    // ✅ 일기 불러오기 실패 → 새로운 일기 작성 (1)
                    _diaryStatus.postValue(1)
                    //
                    _diaryExists.postValue(false)
                }
            )
        }
    }
    fun fetchDiaryById(id: Int) {
        viewModelScope.launch {
            DiaryRepository.getDiaryById(id,
                onSuccess = { diary ->
                    _diaryTitle.postValue(diary.title)
                    _diaryText.postValue(diary.content)
                    _emotionIcon.postValue(getEmotionIcon(diary.emotion))

                    // ✅ String (URL) 리스트 → Uri 리스트 변환
                    val uriList = diary.imagePath?.map { Uri.parse(it) } ?: emptyList()
                    _images.postValue(uriList)

                    // ✅ 기존 일기가 있으면 수정 (0), 없으면 저장 (1)
                    _diaryStatus.postValue(0)
                    // ✅ 다이어리가 존재하는 상태로 설정
                    _diaryExists.postValue(true)

                    Log.d("DiaryViewModel", "✅ 다이어리 불러오기 성공: $diary")
                },
                onFailure = { error ->
                    _errorMessage.postValue(error)

                    // ✅ 다이어리가 없으면 초기 상태로 설정
                    _diaryTitle.postValue("")  // 제목 초기화
                    _diaryText.postValue("")   // 내용 초기화
                    _emotionIcon.postValue(R.drawable.btn_nothing_normal) // 기본 감정 아이콘
                    _images.postValue(emptyList())

                    // ✅ 다이어리 없음 → 새로운 일기 작성 상태로 변경
                    _diaryStatus.postValue(1)
                    _diaryExists.postValue(false)

                    Log.e("DiaryViewModel", "❌ 다이어리 불러오기 실패: $error")
                }
            )
        }
    }



    fun saveOrUpdateDiary(context:Context, date: String, title: String) {
        Log.d("Upload Debug", "✅ saveOrUpdateDiary() 함수 호출됨")
        viewModelScope.launch {
            Log.d("UploadDebug", "✅ viewModelScope.launch 실행됨")
            val emotion = getEmotionString(emotionIcon.value ?: R.drawable.btn_nothing_normal)

            if (_diaryStatus.value == 1) {
                // ✅ 새로운 일기 저장 (POST)
                val diaryJson = Gson().toJson(
                    DiaryDTO(
                        date = date,
                        title = title,
                        emotion = emotion,
                        content = _diaryText.value.orEmpty(),
                        impressiveThing = "기억에 남는 순간(임시)"
                    )
                )
                val diaryRequestBody= diaryJson.toRequestBody("application/json".toMediaTypeOrNull())

                val imageParts = _images.value?.map{ prepareFilePart(context, it, "images")} ?: emptyList()

                DiaryRepository.saveDiary(diaryRequestBody, imageParts,
                    onSuccess = { _diaryStatus.postValue(0)
                        Log.d("UploadDebug", "✅ 서버 요청 성공!")},
                    onFailure = { error -> _errorMessage.postValue(error)
                        Log.d("UploadDebug", "서버 요청 실패")}
                )
            } else {
                // ✅ 기존 일기 수정 (PATCH)
                val diaryJson = Gson().toJson(
                    DiaryDTO(
                        date = date,
                        title = title,
                        emotion = emotion,
                        content = _diaryText.value.orEmpty(),
                        impressiveThing = "기억에 남는 순간"
                    )
                )

                val diaryRequestBody= diaryJson.toRequestBody("application/json".toMediaTypeOrNull())

                val imageParts = _images.value?.map{ prepareFilePart(context, it, "images")} ?: emptyList()

                DiaryRepository.updateDiary(diaryRequestBody, imageParts,
                    onSuccess = { /* 성공 처리 */ },
                    onFailure = { error -> _errorMessage.postValue(error) }
                )
            }
        }
    }

    private fun getEmotionIcon(emotion: String): Int {
        return when (emotion) {
            "DEPRESSION" -> R.drawable.ic_depression
            "ANGER" -> R.drawable.ic_angry
            "SAD" -> R.drawable.ic_sad
            "CALM" -> R.drawable.ic_calm
            "JOY" -> R.drawable.ic_joy
            "HAPPINESS" -> R.drawable.ic_happy
            "THRILL" -> R.drawable.ic_romance
            else -> R.drawable.btn_nothing_normal
        }
    }

    private fun getEmotionString(emotion : Int) : String {
        return when (emotion) {
            R.drawable.ic_depression -> "DEPRESSION"
            R.drawable.ic_angry -> "ANGER"
            R.drawable.ic_sad -> "SAD"
            R.drawable.ic_calm -> "CALM"
            R.drawable.ic_joy -> "JOY"
            R.drawable.ic_happy -> "HAPPINESS"
            R.drawable.ic_romance -> "THRILL"
            else -> "NEURAL"
        }

    }
    fun prepareFilePart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return MultipartBody.Part.createFormData(partName, "")

        val file = File(context.cacheDir, "upload_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output -> inputStream.copyTo(output) }

        Log.d("UploadDebug", "파일 경로: ${file.absolutePath}, 크기: ${file.length()} bytes")

        if (file.length() == 0L) {
            Log.e("UploadDebug", "파일이 비어있음!")
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

}