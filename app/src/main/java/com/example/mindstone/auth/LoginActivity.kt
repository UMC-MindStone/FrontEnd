package com.example.mindstone.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mindstone.R
import com.example.mindstone.SignupEmailActivity
import com.example.mindstone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // "이메일 찾기" 클릭 시 -> LoginActivity2로 이동
        binding.loginFindEmailTv.setOnClickListener {
            val intent = Intent(this, LoginActivity2::class.java)
            startActivity(intent)
        }

        // "비밀번호 재설정" 클릭 시 -> LoginActivity2로 이동 (FindPasswordFragment를 시작 Fragment로 설정)
        binding.loginFindPWTv.setOnClickListener {
            val intent = Intent(this, LoginActivity2::class.java)
            intent.putExtra("startFragment", "FindPasswordFragment")
            startActivity(intent)
        }

        // "회원가입" 클릭 시 -> SignupEmailActivity로 이동
        binding.loginJoinTv.setOnClickListener {
            val intent = Intent(this, SignupEmailActivity::class.java)
            startActivity(intent)
        }

        // 자동 로그인 체크박스 클릭 이벤트
        binding.loginChecked2Iv.setOnClickListener {
            toggleAutoLogin()
        }
        binding.loginCheckedIv.setOnClickListener {
            toggleAutoLogin()
        }

        // 로그인 버튼 클릭 시 유효성 검사 실행
        binding.loginLoginIv.setOnClickListener {
            validateInputs()
        }
    }

    private fun validateInputs() {
        val email = binding.loginEmailEt.text.toString().trim()
        val password = binding.loginPasswordEt.text.toString().trim()
        var isValid = true

        // 이메일 유효성 검사
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginEmailTil.error = "example@example.com"
            binding.loginEmailErrorTv.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.loginEmailTil.error = null
            binding.loginEmailErrorTv.visibility = View.GONE
        }

        // 비밀번호 유효성 검사 (8자 이상 + 영문, 숫자, 특수문자 포함)
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
        if (password.isEmpty() || !password.matches(passwordPattern)) {
            binding.loginPasswordTil.error = "영문, 숫자, 특수문자 포함 8자 이상 입력하세요."
            binding.loginPasswordErrorTv.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.loginPasswordTil.error = null
            binding.loginPasswordErrorTv.visibility = View.GONE
        }

        if (isValid) {
            // 로그인 진행 로직 (ex. Firebase 인증, API 호출 등)
        }
    }

    private fun toggleAutoLogin() {
        val isChecked = binding.loginChecked2Iv.visibility == View.VISIBLE

        if (isChecked) {
            // 체크 해제 상태로 변경
            binding.loginChecked2Iv.visibility = View.GONE
            binding.loginCheckedIv.visibility = View.VISIBLE
        } else {
            // 체크된 상태로 변경
            binding.loginChecked2Iv.visibility = View.VISIBLE
            binding.loginCheckedIv.visibility = View.GONE
        }
    }
}