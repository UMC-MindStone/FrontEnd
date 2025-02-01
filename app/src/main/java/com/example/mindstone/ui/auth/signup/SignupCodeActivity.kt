package com.example.mindstone.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mindstone.MyApplication
import com.example.mindstone.R
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.databinding.ActivitySignupCodeBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

class SignupCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupCodeBinding
    private lateinit var signupViewModel: SignupViewModel
    private val retrofitService = RetrofitClient.create(SignupService::class.java)

    private var debounceJob: Job? = null // 디바운싱 작업을 관리하기 위한 Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCodeBinding.inflate(layoutInflater)
        signupViewModel = (application as MyApplication).signupViewModel

        setContentView(binding.root)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("code activity", "${signupViewModel.email.value}")


        initClicker()
        requestCode()

        signupViewModel.email.observe(this){ email ->
            Log.d("email", "${email}")
            binding.signupCodeMentTv.text = "${email}으로 보내드린 인증번호를 입력하세요"
        }

    }

    private fun initClicker() {
        // 입력 필드 리스트 생성
        val inputs = arrayListOf(
            binding.signupCode1Til.editText as TextInputEditText,
            binding.signupCode2Til.editText as TextInputEditText,
            binding.signupCode3Til.editText as TextInputEditText,
            binding.signupCode4Til.editText as TextInputEditText
        )

        // 입력 포커스 이동 설정
        moveTextFocus(inputs)

        // 텍스트 변경 감지 및 서버 요청 디바운싱 적용
        inputs.forEach { input ->
            input.addTextChangedListener(
                onTextChanged = { _, _, _, _ ->
                    debounceJob?.cancel() // 기존 요청 취소
                    debounceJob = lifecycleScope.launch {
                        delay(300)
                        updateButtonState(inputs) // 버튼 상태 업데이트
                    }
                }
            )
        }

        // 버튼 클릭 이벤트
        binding.signupCodeNextAbleBtn.setOnClickListener {
            if (binding.signupCodeNextAbleBtn.isEnabled) {
                val intent = Intent(this, SignupPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 버튼 상태 업데이트 함수
    private suspend fun updateButtonState(inputs: List<TextInputEditText>) {
        val inputCode = inputs.joinToString("") { it.text.toString() }

        try {
            // 서버에 코드 검증 요청
            val response = retrofitService.codeValidate(
                codeValidateRequest(
                    signupViewModel.email.value.orEmpty(),
                    inputCode
                )
            )

            if (response.isSuccess) {
                // 성공 처리: 버튼 활성화 및 에러 메시지 숨김
                binding.signupCodeNextBtn.apply {
                    visibility = View.GONE
                }
                binding.signupCodeNextAbleBtn.apply{
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                binding.signupCode1Til.boxStrokeColor = getColor(R.color.black)
                binding.signupCode2Til.boxStrokeColor = getColor(R.color.black)
                binding.signupCode3Til.boxStrokeColor = getColor(R.color.black)
                binding.signupCode4Til.boxStrokeColor = getColor(R.color.black)
                binding.signupResendTv.visibility = View.GONE
            } else {
                // 실패 처리: 버튼 비활성화 및 에러 메시지 표시
                binding.signupCodeNextBtn.apply {
                    visibility = View.GONE
                }
                binding.signupResendTv.apply{
                    visibility = View.VISIBLE
                    setOnClickListener {
                        requestCode()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    // 입력 포커스 자동 이동 함수
    private fun moveTextFocus(inputs: List<TextInputEditText>) {
        for (i in inputs.indices) {
            inputs[i].addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                    if (text?.length == 1 && i < inputs.size - 1) {
                        inputs[i + 1].requestFocus()
                    }
                }
            )
        }
    }

    private fun requestCode() {
        val email = signupViewModel.email.value
        val updatedAt = getCurrentTimestamp()

        lifecycleScope.launch {
            try {
                val response = retrofitService.codeRequest(
                    codeRequest(
                        email.orEmpty(),
                        updatedAt
                    )
                )
                if (response.isSuccess) {
                    Log.d("SignupCodeActivity", "인증번호 전송 성공")
                    Log.d("code response", "${response}")
                } else {
                    Log.d("SignupCodeActivity", "인증번호 전송 실패")
                    Log.d("code response", "${response}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Code Error", "Error: ${e.message}")
            }
        }
    }

    fun getCurrentTimestamp(): String {
        return Instant.now().toString() // 현재 UTC 시간을 ISO 8601 형식으로 반환
    }
}

