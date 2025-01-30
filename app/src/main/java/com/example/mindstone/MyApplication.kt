package com.example.mindstone

import android.app.Application
import com.example.mindstone.data.local.PreferenceManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 앱 실행 시 PreferenceManager 초기화 (SharedPreferences 사용 가능하도록 설정)
        PreferenceManager.init(applicationContext)
    }
}