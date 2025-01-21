package com.example.mindstone

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.databinding.ActivitySearch2Binding

class Search2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySearch2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivitySearch2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 리스트 설정 (선택 가능한 항목들)
        val selectableTextViews = listOf(
            binding.tvExerciseSearch,
            binding.tvReadingSearch,
            binding.tvSleepSearch,
            binding.tvCookSearch,
            binding.tvMeetSearch,
            binding.tvCallSearch,
            binding.tvExercise2Search,
            binding.tvReading2Search,
            binding.tvSleep2Search,
            binding.tvCook2Search,
            binding.tvMeet2Search,
            binding.tvCall2Search,
            binding.tvExercise3Search,
            binding.tvReading3Search,
            binding.tvSleep3Search,
            binding.tvCook3Search,
            binding.tvMeet3Search,
            binding.tvCall3Search
        )

        // EditText 리스트 설정 (직접 입력 항목들)
        val customInputs = listOf(
            binding.etEnterDirectlySearch,
            binding.etEnterDirectly2Search,
            binding.etEnterDirectly3Search
        )

        // EditText 입력 상태 관리
        customInputs.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) { // 포커스가 해제되었을 때
                    handleCustomInput(editText)
                }
            }
        }

        // 클릭 이벤트 설정 (셀렉터를 통해 색상 전환)
        selectableTextViews.forEach { textView ->
            textView.setOnClickListener {
                textView.isSelected = !textView.isSelected
            }
        }

        // 다음 버튼 클릭 이벤트
        binding.ibSearchNext.setOnClickListener {
            if (isAllFieldsSelected(selectableTextViews, customInputs)) {
                navigateToNextPage() // 조건 만족 시 다음 페이지로 이동
            }
        }
    }

    // EditText에서 입력된 값 처리
    private fun handleCustomInput(editText: EditText) {
        val inputText = editText.text.toString().trim()

        if (inputText.isNotEmpty()) {
            editText.isSelected = true // 입력된 값은 선택된 상태로 표시
            editText.setBackgroundResource(R.drawable.textview_background_search) // 선택된 배경
        } else {
            editText.isSelected = false // 입력이 없으면 선택 해제
            editText.setBackgroundResource(R.drawable.textview_background_search) // 기본 배경
        }
    }

    // 모든 필드가 선택 또는 입력되었는지 확인
    private fun isAllFieldsSelected(
        textViews: List<TextView>,
        editTexts: List<EditText>
    ): Boolean {
        // 모든 TextView가 선택되었는지 확인
        val areAllTextViewsSelected = textViews.all { it.isSelected }

        // 모든 EditText가 비어 있지 않은지 확인
        val areAllEditTextsFilled = editTexts.all { it.isSelected }

        return areAllTextViewsSelected && areAllEditTextsFilled
    }

    // 다음 페이지로 이동
    private fun navigateToNextPage() {
        val intent = Intent(this, Search3Activity::class.java) // NextActivity로 이동
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}
