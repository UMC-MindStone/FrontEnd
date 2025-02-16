package com.example.mindstone.ui.search

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.ActivitySearch1Binding

class Search1Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySearch1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivitySearch1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다음 버튼 클릭 리스너
        binding.ibSearchNext.setOnClickListener {
            validateInputs()
        }

        Log.d("Activity_Lifecycle", "Search1Activity 실행됨!") // ✅ 실행 확인용 로그 추가
    }

    // 입력값 검증
    private fun validateInputs() {
        var isValid = true

        // 연도 입력값 검증
        val year = binding.etYearSearch1.text.toString()
        if (year.isEmpty()) {
            isValid = false
            showDynamicErrorMessage(
                targetView = binding.etYearSearch1,
                errorMessage = "올바른 값을 입력해주세요."
            )
        } else {
            clearDynamicErrorMessage(binding.etYearSearch1)
        }

        // 월 선택 검증
        val month = binding.spinnerMonth.selectedItem.toString()
        if (month == "MM") {
            isValid = false
            showSpinnerError(binding.spinnerMonth)
        } else {
            clearSpinnerError(binding.spinnerMonth)
        }

        // 일 선택 검증
        val day = binding.spinnerDay.selectedItem.toString()
        if (day == "DD") {
            isValid = false
            showSpinnerError(binding.spinnerDay)
        } else {
            clearSpinnerError(binding.spinnerDay)
        }

        // 직업 선택 검증
        val job = binding.spinnerJob.selectedItem.toString()
        if (job == "Option") {
            isValid = false
            showSpinnerError(binding.spinnerJob)
        } else {
            clearSpinnerError(binding.spinnerJob)
        }

        // MBTI 선택 검증
        val mbti = binding.spinnerMbti.selectedItem.toString()
        if (mbti == "Option") {
            isValid = false
            showSpinnerError(binding.spinnerMbti)
        } else {
            clearSpinnerError(binding.spinnerMbti)
        }
    }

    private fun showDynamicErrorMessage(targetView: View, errorMessage: String) {
        // 이전에 추가된 오류 메시지 삭제
        clearDynamicErrorMessage(targetView)

        // TextView 동적으로 생성
        val errorTextView = TextView(this).apply {
            text = errorMessage
            setTextColor(Color.RED)
            textSize = 12f
        }

        // 부모 레이아웃 가져오기
        val parent = targetView.parent as? LinearLayout
        if (parent != null) {
            // 연도 입력 필드에만 오류 메시지 추가 (parent.orientation == HORIZONTAL이라도 동작 보장)
            if (targetView.id == R.id.et_year_search1) {
                // 부모에서 연도 입력 필드 아래에 메시지 추가
                val index = parent.indexOfChild(targetView)
                parent.addView(errorTextView, index + 1) // 연도 입력 필드 아래에 추가
            }
        } else {
            Toast.makeText(this, "뷰 계층 구조를 확인하세요.", Toast.LENGTH_SHORT).show()
        }

        // 입력 필드 테두리 빨간색으로 변경
        targetView.setBackgroundResource(R.drawable.border_shape_error)
        targetView.tag = errorTextView // 오류 메시지를 태그로 저장
    }


    // 동적으로 추가된 오류 메시지 삭제
    private fun clearDynamicErrorMessage(targetView: View) {
        val errorTextView = targetView.tag as? TextView
        if (errorTextView != null) {
            val parent = errorTextView.parent as? LinearLayout
            parent?.removeView(errorTextView) // 오류 메시지 삭제
            targetView.setBackgroundResource(R.drawable.border_shape) // 기본 테두리 복원
            targetView.tag = null
        }
    }

    // Spinner 오류 표시
    private fun showSpinnerError(spinner: Spinner) {
        spinner.setBackgroundResource(R.drawable.border_shape_error) // 빨간 테두리
    }

    private fun clearSpinnerError(spinner: Spinner) {
        spinner.setBackgroundResource(R.drawable.border_shape) // 기본 테두리
    }
}
