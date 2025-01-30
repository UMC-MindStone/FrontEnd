package com.example.mindstone.ui.auth.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.databinding.ActivityLoginBinding
import com.example.mindstone.ui.auth.signup.SignupEmailActivity
import com.example.mindstone.ui.search.AfterLoginActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels() // ViewModel 초기화

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

        // 입력 필드 이벤트 리스너 설정
        setupInputListeners()

        // ✅ 자동 로그인 여부 확인 (앱 실행 시)
        checkAutoLogin()

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

        // 로그인 버튼 클릭
        binding.loginLoginIv.setOnClickListener {
            validateInputs()
        }

        // 자동 로그인 체크박스 클릭 이벤트
        binding.loginChecked2Iv.setOnClickListener { toggleAutoLogin(false) }
        binding.loginCheckedIv.setOnClickListener { toggleAutoLogin(true) }


        // 로그인 결과 옵저빙
        viewModel.loginResult.observe(this, Observer { result ->
            if (result == "로그인 성공") {
                navigateToAfterLogin()
            } else {
                binding.loginEmailTil.isErrorEnabled = true
                binding.loginPasswordTil.isErrorEnabled = true
                binding.loginPasswordTil.helperText = "이메일 또는 비밀번호가 일치하지 않습니다."
            }
        })

    }

    // 자동 로그인 여부 확인 (앱 실행 시)
    private fun checkAutoLogin() {
        var isAutoLogin = PreferenceManager.getAutoLogin()
        // 앱 최초 실행 시 자동 로그인을 기본값으로 활성화 (true)
        if (!PreferenceManager.contains("autoLogin")) {
            PreferenceManager.setAutoLogin(true)
            isAutoLogin = true
        }
        if (isAutoLogin && PreferenceManager.getAccessToken() != null) {
            navigateToAfterLogin()
        }
        updateAutoLoginUI(isAutoLogin)
    }


    // 자동 로그인 체크박스 상태 변경 및 저장
    private fun toggleAutoLogin(enable: Boolean) {
        PreferenceManager.setAutoLogin(enable)
        updateAutoLoginUI(enable)
    }

    // UI 업데이트
    private fun updateAutoLoginUI(isChecked: Boolean) {
        if (isChecked) {
            binding.loginChecked2Iv.visibility = View.VISIBLE
            binding.loginCheckedIv.visibility = View.GONE
        } else {
            binding.loginChecked2Iv.visibility = View.GONE
            binding.loginCheckedIv.visibility = View.VISIBLE
        }
    }


    private fun setupInputListeners() {
        // 이메일 입력 필드 포커스 이벤트
        binding.loginEmailEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginEmailTil.helperText = "example@example.com"
                binding.loginEmailTil.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.error)))
            }
        }

        binding.loginEmailEt.addTextChangedListener {
            val email = it.toString().trim()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.loginEmailTil.isErrorEnabled = false
                binding.loginEmailTil.helperText = null
            } else {
                binding.loginEmailTil.isErrorEnabled = true
                binding.loginEmailTil.helperText = "example@example.com"
            }
        }

        // 비밀번호 입력 필드 포커스 이벤트
        binding.loginPasswordEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginPasswordTil.helperText = "영문, 숫자, 특수문자 포함 8자 이상"
                binding.loginPasswordTil.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.error)))
            }
        }

        binding.loginPasswordEt.addTextChangedListener {
            val password = it.toString().trim()
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
            if (password.matches(passwordPattern)) {
                binding.loginPasswordTil.isErrorEnabled = false
                binding.loginPasswordTil.helperText = null
            } else {
                binding.loginPasswordTil.isErrorEnabled = true
                binding.loginPasswordTil.helperText = "영문, 숫자, 특수문자 포함 8자 이상"
            }
        }
    }

    // 로그인 버튼 클릭 시
    private fun validateInputs() {
        val email = binding.loginEmailEt.text.toString().trim()
        val password = binding.loginPasswordEt.text.toString().trim()
        var isValid = true

        // 이메일 유효성 검사
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginEmailTil.isErrorEnabled = true
            binding.loginEmailTil.helperText = "example@example.com"
            isValid = false
        }

        // 비밀번호 유효성 검사
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
        if (!password.matches(passwordPattern)) {
            binding.loginPasswordTil.isErrorEnabled = true
            binding.loginPasswordTil.helperText = "영문, 숫자, 특수문자 포함 8자 이상"
            isValid = false
        }

        // 유효성 검사 결과에 따른 로그인 진행 여부
        if (isValid) {
            viewModel.login(email, password)
        } else {
            binding.loginEmailTil.isErrorEnabled = true
            binding.loginPasswordTil.isErrorEnabled = true
            binding.loginPasswordTil.helperText = "이메일 또는 비밀번호가 일치하지 않습니다."
        }
    }


    private fun navigateToAfterLogin() {
        val intent = Intent(this, AfterLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}