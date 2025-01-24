package com.example.mindstone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.mindstone.databinding.ActivityMainBinding
import com.example.mindstone.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 앱이 처음 실행될 때 기본 프래그먼트(HomeFragment) 설정
//        if (savedInstanceState == null) {
//            replaceFragment(HomeFragment())
//        }

        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, HomeFragment())
            .commitAllowingStateLoss()

        // 네비게이션 바 아이템 클릭 시 프래그먼트 변경
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                //R.id.nav_emotion -> replaceFragment(EmotionFragment()) // 감정
                //R.id.nav_habit -> replaceFragment(HabitFragment()) // 습관
                R.id.nav_home -> replaceFragment(HomeFragment()) // 홈
                //R.id.nav_friends -> replaceFragment(FriendsFragment()) // 친구 목록
                //R.id.nav_my -> replaceFragment(MyFragment()) // 마이페이지
            }
            true
        }
    }

    // 프래그먼트 변경 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}