package com.example.mindstone

import android.app.Application
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.ui.auth.signup.SignupViewModel
import com.example.mindstone.ui.home.diary.DiaryViewModel

class MyApplication : Application() {

    val signupViewModel: SignupViewModel by lazy { SignupViewModel(this) }
    val diaryViewModel: DiaryViewModel by lazy { DiaryViewModel(this) }

    override fun onCreate() {

        super.onCreate()

        // 앱 실행 시 PreferenceManager 초기화 (SharedPreferences 사용 가능하도록 설정)
        PreferenceManager.init(applicationContext)

    }
}