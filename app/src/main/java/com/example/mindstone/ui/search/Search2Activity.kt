package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.R
import com.example.mindstone.data.local.UserData
import com.example.mindstone.databinding.ActivitySearch2Binding
import com.google.gson.Gson

class Search2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySearch2Binding
    private var userData: UserData? = null

    private val selectedStressManagement = mutableListOf<String>() // ✅ 스트레스 해소 방법 리스트
    private val selectedHobbies = mutableListOf<String>() // ✅ 취미 리스트
    private val selectedSpecialSkills = mutableListOf<String>() // ✅ 특기 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ ViewBinding 초기화
        binding = ActivitySearch2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 이전 액티비티에서 UserData 받아오기
        userData = intent.getParcelableExtra("userData")
        Log.d("UserData", "Received UserData in Search2Activity: $userData")

        // ✅ 선택 가능한 TextView 리스트 설정
        val stressManagementViews = listOf( // ✅ 스트레스 해소 방법 TextView 목록
            binding.tvExerciseSearch, binding.tvReadingSearch, binding.tvSleepSearch,
            binding.tvCookSearch, binding.tvMeetSearch, binding.tvCallSearch
        )

        val hobbyViews = listOf( // ✅ 취미 TextView 목록
            binding.tvExercise2Search, binding.tvReading2Search, binding.tvSleep2Search,
            binding.tvCook2Search, binding.tvMeet2Search, binding.tvCall2Search
        )

        val specialSkillViews = listOf( // ✅ 특기 TextView 목록
            binding.tvExercise3Search, binding.tvReading3Search, binding.tvSleep3Search,
            binding.tvCook3Search, binding.tvMeet3Search, binding.tvCall3Search
        )

        // ✅ 직접 입력 가능한 EditText 리스트 설정
        val customInputs = listOf(
            binding.etEnterDirectlySearch, binding.etEnterDirectly2Search, binding.etEnterDirectly3Search
        )

        // ✅ EditText 입력 감지 및 데이터 저장
        customInputs.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) { // 포커스가 해제되었을 때
                    handleCustomInput(editText)
                }
            }
        }

        // ✅ 스트레스 해소 방법 선택
        stressManagementViews.forEach { textView ->
            textView.setOnClickListener {
                val text = textView.text.toString()
                textView.isSelected = !textView.isSelected

                if (textView.isSelected) {
                    selectedStressManagement.add(text) // ✅ 선택한 항목 추가
                } else {
                    selectedStressManagement.remove(text) // ✅ 선택 해제된 항목 제거
                }
            }
        }

        // ✅ 취미 선택
        hobbyViews.forEach { textView ->
            textView.setOnClickListener {
                val text = textView.text.toString()
                textView.isSelected = !textView.isSelected

                if (textView.isSelected) {
                    selectedHobbies.add(text)
                } else {
                    selectedHobbies.remove(text)
                }
            }
        }

        // ✅ 특기 선택
        specialSkillViews.forEach { textView ->
            textView.setOnClickListener {
                val text = textView.text.toString()
                textView.isSelected = !textView.isSelected

                if (textView.isSelected) {
                    selectedSpecialSkills.add(text)
                } else {
                    selectedSpecialSkills.remove(text)
                }
            }
        }

        // ✅ 다음 버튼 클릭 이벤트
        binding.ibSearchNext.setOnClickListener {
            saveSelectionsToUserData() // ✅ 선택한 데이터 저장
            Log.d("UserData", "Updated UserData: $userData")
            navigateToNextPage() // ✅ 다음 페이지로 이동
        }
    }

    // ✅ EditText에서 입력된 값 처리
    private fun handleCustomInput(editText: EditText) {
        val inputText = editText.text.toString().trim()

        if (inputText.isNotEmpty()) {
            editText.isSelected = true
            editText.setBackgroundResource(R.drawable.textview_background_search)
            selectedSpecialSkills.add(inputText) // ✅ 직접 입력된 값 저장
        } else {
            editText.isSelected = false
            editText.setBackgroundResource(R.drawable.textview_background_search)
            selectedSpecialSkills.remove(inputText) // ✅ 입력값이 비어 있으면 제거
        }
    }


//    private fun saveSelectionsToUserData() {
//        userData?.let {
//            it.stressManagement = selectedStressManagement.joinToString(", ") // ✅ "운동, 독서" 형식으로 변환
//            it.hobbies = selectedHobbies.joinToString(", ") // ✅ "축구, 요리" 형식으로 변환
//            it.specialSkills = selectedSpecialSkills.joinToString(", ") // ✅ "피아노, 글쓰기" 형식으로 변환
//        }
//        Log.d("UserData", "Final Updated UserData: $userData")
//    }
    private fun saveSelectionsToUserData() {
        userData?.let {
            it.stressManagement = selectedStressManagement // ✅ 리스트 그대로 사용
            it.hobbies = selectedHobbies // ✅ 리스트 그대로 사용
            it.specialSkills = selectedSpecialSkills // ✅ 리스트 그대로 사용
        }
        Log.d("UserData", "Final Updated UserData: ${Gson().toJson(userData)}") // ✅ JSON 직렬화 로그
    }



    // ✅ 다음 페이지로 이동
    private fun navigateToNextPage() {
        val intent = Intent(this, Search3Activity::class.java).apply {
            putExtra("userData", userData) // ✅ 업데이트된 UserData 전달
        }
        startActivity(intent)
        finish() // ✅ 현재 액티비티 종료
    }
}
