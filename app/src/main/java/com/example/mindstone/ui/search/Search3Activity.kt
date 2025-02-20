package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.data.local.UserData
import com.example.mindstone.databinding.ActivitySearch3Binding

class Search3Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySearch3Binding
    private var userData: UserData? = null
    private lateinit var viewModel: SurveyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ ViewBinding 초기화
        binding = ActivitySearch3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 시스템 바 자동 조정
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ 이전 액티비티에서 UserData 받아오기
        userData = intent.getParcelableExtra<UserData>("userData")
        Log.d("UserData", "Received UserData in Search3Activity: $userData")

        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)

        // ✅ 선택 가능한 습관 목록 (TextView)
        val selectableTextViews = listOf(
            binding.tvExerciseSearch,
            binding.tvReadingSearch,
            binding.tvSleepSearch,
            binding.tvCookSearch,
            binding.tvMedicineSearch
        )

        // ✅ EditText 입력 항목 (직접 입력 가능)
        val customInputs = listOf(binding.etEnterDirectlySearch)

        // ✅ EditText 입력 상태 관리
        customInputs.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) { // 포커스 해제 시
                    handleCustomInput(editText)
                }
            }
        }

        // ✅ TextView 클릭 이벤트 설정 (선택 시 색상 변경)
        selectableTextViews.forEach { textView ->
            textView.setOnClickListener {
                textView.isSelected = !textView.isSelected
            }
        }

        // ✅ 다음 버튼 클릭 이벤트
        binding.ibSearchNext.setOnClickListener {
            saveUserHabits() // ✅ 습관 데이터 저장
            sendUserData() // ✅ API 요청 실행 & 다음 페이지 이동
        }

        // ✅ API 응답 처리
        viewModel.surveyMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            Log.d("Survey", message)
        }
    }

    // ✅ EditText에서 입력된 값 처리
    private fun handleCustomInput(editText: EditText) {
        val inputText = editText.text.toString().trim()

        if (inputText.isNotEmpty()) {
            editText.isSelected = true // ✅ 입력된 값은 선택된 상태로 표시
            editText.setBackgroundResource(R.drawable.textview_background_search) // ✅ 선택된 배경
        } else {
            editText.isSelected = false // ✅ 입력 없으면 선택 해제
            editText.setBackgroundResource(R.drawable.textview_background_search) // ✅ 기본 배경
        }
    }

    // ✅ 습관 데이터 저장 (사용자가 선택한 것만)
    private fun saveUserHabits() {
        val selectedHabits = mutableListOf<UserData.Habit>()

        // ✅ 선택된 TextView 습관 추가
        val selectableTextViews = listOf(
            binding.tvExerciseSearch,
            binding.tvReadingSearch,
            binding.tvSleepSearch,
            binding.tvCookSearch,
            binding.tvMedicineSearch
        )
        selectableTextViews.forEach { textView ->
            if (textView.isSelected) {
                selectedHabits.add(
                    UserData.Habit(
                        title = textView.text.toString(),
                        dayOfWeek = "",  // 입력 안 하면 빈 값
                        alarmTime = "",
                        targetTime = 0,
                        isActive = false,
                        habitColor = ""
                    )
                )
            }
        }

        // ✅ 직접 입력한 습관 추가
        val customHabit = binding.etEnterDirectlySearch.text.toString().trim()
        if (customHabit.isNotEmpty()) {
            selectedHabits.add(
                UserData.Habit(
                    title = customHabit,
                    dayOfWeek = "",
                    alarmTime = "",
                    targetTime = 0,
                    isActive = false,
                    habitColor = ""
                )
            )
        }

        // ✅ 습관 정보가 없으면 null, 있으면 저장
        userData?.habits = if (selectedHabits.isEmpty()) null else selectedHabits
    }

    // ✅ 사용자 데이터 전송 (API 연동)
    private fun sendUserData() {
        userData?.let {
            viewModel.sendUserData(it)
        } ?: Log.e("Search3Activity", "UserData is null!")

        // ✅ 다음 페이지로 이동
        navigateToNextPage()
    }

    // ✅ 다음 페이지로 이동
    private fun navigateToNextPage() {
        val intent = Intent(this, Start1Activity::class.java)
        intent.putExtra("userNickname", userData?.nickname ?: "사용자")
        startActivity(intent)
        finish() // ✅ 현재 액티비티 종료
    }
}
