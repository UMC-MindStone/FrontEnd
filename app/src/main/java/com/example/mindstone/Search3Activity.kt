package com.example.mindstone

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.databinding.ActivitySearch3Binding

class Search3Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySearch3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivitySearch3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 리스트 설정 (선택 가능한 항목들)
        val selectableTextViews = listOf(
            binding.tvExerciseSearch,
            binding.tvReadingSearch,
            binding.tvSleepSearch,
            binding.tvCookSearch,
            binding.tvMedicineSearch
        )

        // EditText 리스트 설정 (직접 입력 항목들)
        val customInputs = listOf(
            binding.etEnterDirectlySearch
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
            navigateToNextPage() // 선택 여부와 관계없이 다음 페이지로 이동
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

    // 다음 페이지로 이동
    private fun navigateToNextPage() {
        val intent = Intent(this, Start1Activity::class.java) // 다음 액티비티로 이동
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}
