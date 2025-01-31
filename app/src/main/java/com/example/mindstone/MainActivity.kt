package com.example.mindstone

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.ActivityMainBinding
import com.example.mindstone.ui.emotion.EmotionCalendarFragment
import com.example.mindstone.ui.habit.HabitCalendarFragment
import com.example.mindstone.ui.home.HomeFragment
import com.example.mindstone.ui.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔹 ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 시스템 바 인셋 적용 (네비게이션 바 패딩 설정)
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 앱 실행 시 기본 프래그먼트(HomeFragment) 설정
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // 🔹 네비게이션 바 아이템 클릭 시 프래그먼트 변경
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            Log.d("BottomNav", "Clicked item: ${item.itemId}") // ← 로그 추가
            when (item.itemId) {
                R.id.nav_emotion -> replaceFragment(EmotionCalendarFragment()) // 감정
                R.id.nav_habit -> replaceFragment(HabitCalendarFragment()) // 습관
                R.id.nav_home -> replaceFragment(HomeFragment()) // 홈
                R.id.nav_my -> replaceFragment(MyPageFragment()) // 마이페이지
            }
            true
        }
    }

    // 🔹 프래그먼트 변경 함수 (ViewBinding 적용)
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, fragment)
            .addToBackStack(null)
            .commit()
    }
}
