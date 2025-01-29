package com.example.mindstone.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.mindstone.R
import com.example.mindstone.databinding.ActivitySignupPasswordBinding

class SignupPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupPasswordBinding
    private val signupViewModel = SignupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 설정
        initClicker()

        // 입력 변경 시 동적 상태 업데이트
        setupTextWatchers()
    }

    private fun validatePassword(input: String): Boolean {
        return when {
            input.isEmpty() -> {
                binding.signupPasswordTil.apply {
                    boxStrokeColor = getColor(R.color.error)
                    hintTextColor = getColorStateList(R.color.error)
                    helperText = "영문, 숫자, 특수문자 포함 8자 이상."
                }
                false
            }
            containsCharacter(input) && containsNumber(input) && containsSpecialCharacter(input) && input.length >= 8 -> {
                binding.signupPasswordTil.apply {
                    boxStrokeColor = getColor(R.color.black)
                    hintTextColor = getColorStateList(R.color.black)
                    helperText = null
                }
                true
            }
            else -> {
                binding.signupPasswordTil.apply {
                    boxStrokeColor = getColor(R.color.error)
                    hintTextColor = getColorStateList(R.color.error)
                    helperText = "영문, 숫자, 특수문자 포함 8자 이상."
                }
                false
            }
        }
    }

    private fun containsNumber(input: String): Boolean {
        val numberRegex = ".*\\d.*".toRegex() // 숫자를 포함한 정규식
        return numberRegex.matches(input)
    }

    private fun containsSpecialCharacter(input: String): Boolean {
        val specialCharRegex = ".*[!@#\$%^&*()_+\\-=\\[\\]{};':/?].*".toRegex()
        return specialCharRegex.matches(input)
    }

    private fun containsCharacter(input: String): Boolean {
        val characterRegex = ".*[a-zA-Z].*".toRegex()
        return characterRegex.matches(input)
    }

    private fun initClicker() {
        binding.signupPasswordNextBtn.setOnClickListener {
            val input = binding.signupPasswordTextTie.text.toString()
            val check = binding.signupPasswordCheckTextTie.text.toString()

            // 비밀번호 유효성 및 일치 여부 확인
            val isPasswordValid = validatePassword(input)
            if (isPasswordValid) {
                updateButtonState(input, check)
            }
        }
    }

    private fun updateButtonState(input: String, check: String) {
        if (input == check) {

            binding.signupPasswordNextBtn.apply {
                isEnabled = true
                background = getDrawable(R.drawable.btn_pink_background) ?: return
                setOnClickListener {
                    signupViewModel.password.value = input
                }

            }
            binding.signupPasswordCheckTil.apply {
                boxStrokeColor = getColor(R.color.black)
                hintTextColor = getColorStateList(R.color.black)
                helperText = null
            }
        } else {
            binding.signupPasswordNextBtn.apply {
                isEnabled = false
                background = getDrawable(R.drawable.btn_disabled) ?: return
            }
            binding.signupPasswordCheckTil.apply {
                hintTextColor = getColorStateList(R.color.error)
                boxStrokeColor = getColor(R.color.error)
                helperText = "비밀번호가 일치하지 않습니다."
            }
        }
    }

    private fun setupTextWatchers() {
        // 비밀번호 입력 및 확인 입력 변화 감지
        binding.signupPasswordTextTie.addTextChangedListener {
            val input = it.toString()
            val check = binding.signupPasswordCheckTextTie.text.toString()
            validatePassword(input) // 비밀번호 유효성 검사
            updateButtonState(input, check) // 상태 업데이트
        }

        binding.signupPasswordCheckTextTie.addTextChangedListener {
            val input = binding.signupPasswordTextTie.text.toString()
            val check = it.toString()
            updateButtonState(input, check) // 상태 업데이트
        }
    }
}
