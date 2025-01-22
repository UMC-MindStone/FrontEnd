package com.example.mindstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.databinding.ActivityStart1Binding

class Start1Activity : AppCompatActivity() {
    // ViewBinding 변수 선언
    private lateinit var binding: ActivityStart1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityStart1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mock 데이터로 사용자 이름 가져오기
        val userName = getMockUserName()

        // ViewBinding을 사용해 TextView에 텍스트 설정
        binding.start1Tv.text = "${userName}님. 마인드 스톤에 오신 걸 환영합니다."
    }

    // Mock 데이터로 사용자 이름 반환
    private fun getMockUserName(): String {
        return "밍돌" // 백엔드 연동 전까지는 임의의 값 사용
    }
}
