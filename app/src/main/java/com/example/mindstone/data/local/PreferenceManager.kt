package com.example.mindstone.data.local

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private var prefs: SharedPreferences? = null

    // ✅ SharedPreferences 초기화
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        }
    }

    // ✅ SharedPreferences 가져오기
    private fun getPrefs(): SharedPreferences {
        return prefs ?: throw IllegalStateException("PreferenceManager.init(context)를 호출하세요.")
    }

    // ✅ Access Token 저장 & 불러오기
    fun saveAccessToken(token: String) {
        getPrefs().edit().putString("accessToken", token).apply()
    }

    fun getAccessToken(): String? {
        return getPrefs().getString("accessToken", null)
    }

    fun clearAccessToken() {
        getPrefs().edit().remove("accessToken").apply()
    }

    // ✅ Refresh Token 저장 & 불러오기
    fun saveRefreshToken(token: String) {
        getPrefs().edit().putString("refreshToken", token).apply()
    }

    fun getRefreshToken(): String? {
        return getPrefs().getString("refreshToken", null)
    }

    fun clearRefreshToken() {
        getPrefs().edit().remove("refreshToken").apply()
    }

    // ✅ 이메일 저장 & 불러오기
    fun saveEmail(email: String) {
        getPrefs().edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return getPrefs().getString("email", null)
    }

    fun clearEmail() {
        getPrefs().edit().remove("email").apply()
    }

    // ✅ 자동 로그인 설정
    fun setAutoLogin(enable: Boolean) {
        getPrefs().edit().putBoolean("autoLogin", enable).apply()
    }

    fun getAutoLogin(): Boolean {
        return getPrefs().getBoolean("autoLogin", false)
    }

    // ✅ 특정 키 존재 여부 확인
    fun contains(key: String): Boolean {
        return getPrefs().contains(key)
    }

    // ✅ 로그아웃 시 모든 저장 데이터 삭제
    fun clearAll() {
        getPrefs().edit().clear().apply()
    }
}
