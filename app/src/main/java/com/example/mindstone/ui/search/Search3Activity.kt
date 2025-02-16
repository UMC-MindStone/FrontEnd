package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        userData = this.intent.getParcelableExtra("userData")
        // ViewBinding 초기화
        binding = ActivitySearch3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)

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
        val localUserData = userData
        binding.ibSearchNext.setOnClickListener {
            if (localUserData != null) {
                viewModel.sendUserData(localUserData) // ✅ API 요청 실행
            } else {
                Log.e("Start1Activity", "UserData is null!")
            }
            navigateToNextPage() // 선택 여부와 관계없이 다음 페이지로 이동
        }

        viewModel.surveyMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show() // ✅ 성공/실패 메시지를 토스트로 표시
            Log.d("Survey", message)
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
        val intent = Intent(this, Start1Activity::class.java)
        intent.putExtra("userData", userData?.nickname)
        // 다음 액티비티로 이동
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}
