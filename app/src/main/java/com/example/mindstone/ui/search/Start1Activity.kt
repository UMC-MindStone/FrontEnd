package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.MainActivity
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.databinding.ActivityStart1Binding

class Start1Activity : AppCompatActivity() {
    // ViewBinding 변수 선언
    private lateinit var binding: ActivityStart1Binding
    private lateinit var viewModel: SurveyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)

        // ViewBinding 초기화
        binding = ActivityStart1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mock 데이터로 사용자 이름 가져오기
        val userName = getUserName()

        // ViewBinding을 사용해 TextView에 텍스트 설정
        binding.start1Tv.text = "${userName}님. 마인드 스톤에 오신 걸 환영합니다."

        binding.btnStart1ColoredIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // 기본조사 화면 중복 방지
        binding.btnStart1ColoredIb.setOnClickListener {
            PreferenceManager.setSurveyCompleted(this, true) // 기본 조사 완료 저장
            val intent = Intent(this, MainActivity::class.java) // 홈 화면으로 이동
            startActivity(intent)
            finish()
        }
    }

    // Mock 데이터로 사용자 이름 반환
    private fun getUserName(): String {
        val nickname = intent.getStringExtra("userData").toString()
        return nickname
    }
}
