package com.example.mindstone.ui.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgument
import com.example.mindstone.R
import com.example.mindstone.data.local.UserData
import com.example.mindstone.databinding.ActivitySearch1Binding

class Search1Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySearch1Binding

    private var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivitySearch1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("Activity_Lifecycle", "Search1Activity 실행됨!") // ✅ 실행 확인용 로그 추가

        // 다음 버튼 클릭 리스너
        binding.ibSearchNext.setOnClickListener {
            validateInputs() // 입력값 검증

            userData = intent.getParcelableExtra("userData") // 이전 액티비티에서 받은 닉네임
            Log.d("UserData", "Received UserData: $userData")

            // ✅ 생년월일을 YYYY-MM-DD 형식으로 조합
            val year = binding.etYearSearch1.text.toString()
            val month = binding.spinnerMonth.selectedItem.toString().padStart(2, '0') // 1 -> "01" 변환
            val day = binding.spinnerDay.selectedItem.toString().padStart(2, '0') // 1 -> "01" 변환
            val birthDate = "$year-$month-$day"

            // ✅ 기타 선택값들 가져오기
            val job = binding.spinnerJob.selectedItem.toString()
            val mbti = binding.spinnerMbti.selectedItem.toString()


            val updatedUserData = userData?.copy(
                birthDate = birthDate,
                job = job,
                mbti = mbti
            ) ?: UserData(nickname = "Unknown")

            Log.d("UserData", "Updated UserData: $updatedUserData")

            // ✅ Intent에 값 추가해서 전송
            val intent = Intent(this, Search2Activity::class.java)
            intent.putExtra("userData", updatedUserData)

            startActivity(intent)
        }
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
            Log.d("YearInput", "Valid Year Input: $year")
        }

        // 월 선택 검증
        val month = binding.spinnerMonth.selectedItem.toString()
        if (month == "MM") {
            isValid = false
            showSpinnerError(binding.spinnerMonth)
        } else {
            clearSpinnerError(binding.spinnerMonth)
            Log.d("Validation", "Valid Month Input: $month")
        }

        // 일 선택 검증
        val day = binding.spinnerDay.selectedItem.toString()
        if (day == "DD") {
            isValid = false
            showSpinnerError(binding.spinnerDay)
        } else {
            clearSpinnerError(binding.spinnerDay)
            Log.d("Validation", "Valid Day Input: $day")
        }

        // 직업 선택 검증
        val job = binding.spinnerJob.selectedItem.toString()
        if (job == "Option") {
            isValid = false
            showSpinnerError(binding.spinnerJob)
        } else {
            clearSpinnerError(binding.spinnerJob)
            Log.d("Validation", "Valid Job Input: $job")
        }

        // MBTI 선택 검증
        val mbti = binding.spinnerMbti.selectedItem.toString()
        if (mbti == "Option") {
            isValid = false
            showSpinnerError(binding.spinnerMbti)
        } else {
            clearSpinnerError(binding.spinnerMbti)
            Log.d("Validation", "Valid MBTI Input: $mbti")
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



