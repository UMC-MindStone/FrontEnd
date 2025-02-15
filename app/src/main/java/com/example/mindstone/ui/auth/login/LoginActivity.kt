package com.example.mindstone.ui.auth.login

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.databinding.ActivityLoginBinding
import com.example.mindstone.domain.entity.LoginResponse
import com.example.mindstone.ui.auth.signup.SignupEmailActivity
import com.example.mindstone.ui.search.AfterLoginActivity
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels() // ViewModel 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 이메일 입력 필드
        binding.loginEmailEt.addTextChangedListener { text ->
            validateEmail(text.toString())
        }
        // 비밀번호 입력 필드
        binding.loginPasswordEt.addTextChangedListener { text ->
            validatePassword(text.toString())
        }

        // 자동 로그인 여부 확인 (앱 실행 시)
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


        // ✅ 로그인 결과 옵저버 (로그인 성공 시 AccessToken 저장)
        viewModel.loginResult.observe(this, Observer { result ->
            if (result == "로그인 성공") {
                val accessToken = viewModel.accessToken.value ?: ""
                if (accessToken.isNotEmpty()) {
                    PreferenceManager.saveAccessToken(accessToken) // ✅ AccessToken 저장
                    navigateToAfterLogin()
                } else {
                    Log.e("API_AUTH", "로그인 성공했지만 AccessToken 없음!")
                }
            } else {
                binding.loginEmailTil.isErrorEnabled = true
                binding.loginPasswordTil.isErrorEnabled = true
                binding.loginPasswordTil.helperText = "이메일 또는 비밀번호가 일치하지 않습니다."
            }
        })
    }


    private fun validateEmail(input:String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com"

        if(input.isEmpty()){
            binding.loginEmailTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)
            }
        } else if(!input.matches(emailPattern.toRegex())){
            binding.loginEmailTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)

            }
        }
        else {
            binding.loginEmailTil.apply{
                helperText = null
                errorIconDrawable = null
                boxStrokeColor = getColor(R.color.black)
                //signupViewModel.email.value = input
            }
        }
    }

    private fun validatePassword(input:String) {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"

        if(input.isEmpty()){
            binding.loginPasswordTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "영문, 숫자, 특수문자 포함 8자 이상"
                errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)
            }
        } else if(!input.matches(passwordPattern.toRegex())){
            binding.loginPasswordTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "영문, 숫자, 특수문자 포함 8자 이상"
                errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)
            }
        }
        else {
            binding.loginPasswordTil.apply{
                helperText = null
                errorIconDrawable = null
                boxStrokeColor = getColor(R.color.black)
                //signupViewModel.email.value = input
            }
        }
    }

    // ✅ 자동 로그인 여부 확인 (앱 실행 시)
    private fun checkAutoLogin() {
        val token = PreferenceManager.getAccessToken()
        val isAutoLogin = PreferenceManager.getAutoLogin()

        Log.d("API_AUTH", "앱 실행 시 저장된 AccessToken 확인: $token") // ✅ 로그 추가

        if (!token.isNullOrEmpty() && isAutoLogin) {
            navigateToAfterLogin() // ✅ 토큰이 있고 자동 로그인이 활성화되었으면 자동 로그인
        }
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


    // 로그인 버튼 클릭 시
    private fun validateInputs() {
        val email = binding.loginEmailEt.text.toString().trim()
        val password = binding.loginPasswordEt.text.toString().trim()

        // 바로 로그인 시도
        viewModel.login(email, password)

        // 로그인 결과에 따른 UI 업데이트
        viewModel.loginResult.observe(this, Observer { result ->
            if (result == "로그인 성공") {
                navigateToAfterLogin()
            } else {
                binding.loginEmailTil.apply {
                    isErrorEnabled = false
                    isErrorEnabled = true
                    error = " " // 강제 에러 상태 적용
                    errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity, R.drawable.ic_emailerror)
                    boxStrokeColor = getColor(R.color.error)
                }
                binding.loginPasswordTil.apply {
                    isErrorEnabled = false
                    isErrorEnabled = true
                    error = " "
                    helperText = "이메일 또는 비밀번호가 일치하지 않습니다."
                    errorIconDrawable = ContextCompat.getDrawable(this@LoginActivity, R.drawable.ic_emailerror)
                    boxStrokeColor = getColor(R.color.error)
                    hintTextColor = getColorStateList(R.color.error)

                }
            }
        })
    }



    private fun navigateToAfterLogin() {
        val intent = Intent(this, AfterLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun saveAuthToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", token)
        editor.apply()

        Log.d("API_AUTH", "저장된 AccessToken: $token") // ✅ 저장된 값 확인 로그 추가
    }
}