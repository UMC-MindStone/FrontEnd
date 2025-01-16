package com.example.mindstone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.mindstone.databinding.ActivitySignupCodeBinding
import com.google.android.material.textfield.TextInputEditText

class SignupCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupCodeBinding
    private val correctCode = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClicker()

        val email = intent.getStringExtra("email")
        binding.signupCodeMentTv.text = "${email}으로 보내드린 인증번호를 입력하세요"
    }

    private fun initClicker() {
        // 입력 필드 리스트 생성
        val inputs = arrayListOf<TextInputEditText>(
            binding.signupCode1Til.editText as TextInputEditText,
            binding.signupCode2Til.editText as TextInputEditText,
            binding.signupCode3Til.editText as TextInputEditText,
            binding.signupCode4Til.editText as TextInputEditText
        )

        // 입력 포커스 이동 설정
        moveTextFocus(inputs)

        // 텍스트 변경 감지 및 버튼 상태 업데이트
        inputs.forEach { input ->
            input.addTextChangedListener(
                onTextChanged = { _, _, _, _ ->
                    updateButtonState(inputs) // 텍스트 변경 시 버튼 상태 업데이트
                }
            )
        }

        // 버튼 클릭 이벤트
        binding.signupCodeNextBtn.setOnClickListener {
            if (binding.signupCodeNextBtn.isEnabled) {
                val intent = Intent(this, SignupPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 버튼 상태 업데이트 함수
    private fun updateButtonState(inputs: List<TextInputEditText>) {
        val inputCode = inputs.joinToString("") { it.text.toString() }

        if (inputCode == correctCode) {
            binding.signupCodeNextBtn.apply {
                isEnabled = true
                background = getDrawable(R.drawable.btn_pink_background)
            }
            binding.signupResendTv.visibility = View.GONE // 에러 메시지 숨기기
        } else {
            binding.signupCodeNextBtn.apply {
                isEnabled = false
                background = getDrawable(R.drawable.btn_disabled)
            }
            binding.signupResendTv.visibility = View.VISIBLE // 에러 메시지 표시
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
}
