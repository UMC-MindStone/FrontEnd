package com.example.mindstone.data.local

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    private fun getPrefs(): SharedPreferences {
        return prefs ?: throw IllegalStateException("PreferenceManager.init(context)를 호출하세요.")
    }

    fun saveAccessToken(token: String) {
        getPrefs().edit().putString("accessToken", token).apply()
    }

    fun getAccessToken(): String? {
        return getPrefs().getString("accessToken", null)
    }

    fun clearAccessToken() {
        getPrefs().edit().remove("accessToken").apply()
    }

    fun setAutoLogin(enable: Boolean) {
        getPrefs().edit().putBoolean("autoLogin", enable).apply()
    }

    fun getAutoLogin(): Boolean {
        return getPrefs().getBoolean("autoLogin", false)
    }

    // `autoLogin` 키가 존재하는지 확인하는 함수 추가
    fun contains(key: String): Boolean {
        return getPrefs().contains(key)
    }
}
