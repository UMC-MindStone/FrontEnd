package com.example.mindstone.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.databinding.ActivityCompleteBinding
import com.example.mindstone.databinding.ActivityServiceBinding
import com.example.mindstone.ui.auth.login.LoginActivity

class CompleteActivity : AppCompatActivity() {
    lateinit var binding : ActivityCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.completeLoginIv.setOnClickListener{
            onSignupSuccess()
//            val intent = Intent(this@CompleteActivity, LoginActivity::class.java)
//            startActivity(intent)
        }
    }

    fun onSignupSuccess() {
        Log.d("CompleteActivity", "✅ 회원가입 성공! 로그인 화면으로 이동")

        // 🚨 자동 로그인 설정을 false로 초기화
        PreferenceManager.setAutoLogin(false)

        PreferenceManager.setSurveyCompleted(this, false)
        Log.d("CompleteActivity", "🚨 기본 조사 완료 상태 초기화: false")

        // 로그인 화면으로 이동
        val intent = Intent(this@CompleteActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}