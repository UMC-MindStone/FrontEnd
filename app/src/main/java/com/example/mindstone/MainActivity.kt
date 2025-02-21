package com.example.mindstone

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.databinding.ActivityMainBinding
import com.example.mindstone.ui.emotion.EmotionCalendarFragment
import com.example.mindstone.ui.habit.HabitCalendarFragment
import com.example.mindstone.ui.home.HomeFragment
import com.example.mindstone.ui.home.TodayFinishFragment
import com.example.mindstone.ui.home.TodayFinishViewModel
import com.example.mindstone.ui.home.emotion.view.EmotionModel
import com.example.mindstone.ui.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todayViewModel: TodayFinishViewModel
    private lateinit var emotionModel: EmotionModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 🔹 ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ✅ 감정 상태 복원
        //emotionModel.loadEmotionState()

        // ✅ 마지막 화면 복원
        //restoreLastFragment()


        // 특정 시간에 띄우기
        todayViewModel = ViewModelProvider(this).get(TodayFinishViewModel::class.java)
        todayViewModel.scheduleFragmentAtSpecificTime(this, 20, 52) //

         //🔹 시스템 바 인셋 적용 (네비게이션 바 패딩 설정)
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 앱 실행 시 기본 프래그먼트(HomeFragment) 설정
        if (savedInstanceState == null) {

            replaceFragment(EmotionCalendarFragment())
            binding.bottomNavigationView.selectedItemId = R.id.nav_home
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

    // 마지막 화면 저장
    private fun saveLastFragment(fragmentTag: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("last_fragment", fragmentTag).apply()
    }

    // 마지막 화면 복원
    private fun restoreLastFragment() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lastFragment = sharedPreferences.getString("last_fragment", "HomeFragment")

        val fragment = when (lastFragment) {
            //"EmotionFinalFragment" -> EmotionFinalFragment()
            //"EmotionManageChoiceFragment" -> EmotionManageChoiceFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }


    fun showTargetFragment() {
        val fragment = TodayFinishFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
        Log.d("FragmentScheduler", "Fragment changed successfully!")
    }

    // 🔹 프래그먼트 변경 함수 (ViewBinding 적용)
    fun replaceFragment(fragment: Fragment, bundle: Bundle? =null) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, fragment)
            .addToBackStack(null)
            .commit()
    }
}
