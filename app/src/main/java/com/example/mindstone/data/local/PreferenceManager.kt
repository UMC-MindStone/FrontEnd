package com.example.mindstone.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_AUTO_LOGIN = "autoLogin"
    private const val KEY_IS_SURVEY_COMPLETED = "is_survey_completed"

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
        prefs?.edit()?.putString(KEY_ACCESS_TOKEN, token)?.apply()
        Log.d("API_AUTH", "AccessToken 저장 완료: $token")
    }

    fun getAccessToken(): String? {
        val token = prefs?.getString(KEY_ACCESS_TOKEN, null)
        Log.d("API_AUTH", "SharedPreferences에서 불러온 AccessToken: $token")
        return token
    }

    // ✅ AccessToken 삭제 (로그아웃 시 사용)
    fun clearAccessToken() {
        prefs?.edit()?.remove(KEY_ACCESS_TOKEN)?.apply()
        Log.d("API_AUTH", "AccessToken 삭제 완료")
    }

    // ✅ 자동 로그인 설정 저장
    fun setAutoLogin(enable: Boolean) {
        prefs?.edit()?.putBoolean(KEY_AUTO_LOGIN, enable)?.apply()
        Log.d("API_AUTH", "자동 로그인 설정: $enable")
    }

    // ✅ 자동 로그인 여부 확인
    fun getAutoLogin(): Boolean {
        return prefs?.getBoolean(KEY_AUTO_LOGIN, false) ?: false
    }

    // ✅ Refresh Token 저장 & 불러오기
    fun saveRefreshToken(token: String) {
        prefs?.edit()?.putString("refreshToken", token)?.apply()
        Log.d("API_AUTH", "RefreshToken 저장 완료: $token")
    }

    fun getRefreshToken(): String? {
        return getPrefs().getString("refreshToken", null)
    }

    fun clearRefreshToken() {
        getPrefs().edit().remove("refreshToken").apply()
    }

    // ✅ 이메일 저장 & 불러오기
    fun saveEmail(email: String) {
        if (email.isEmpty()) {
            Log.e("API_AUTH", "❌ 이메일이 빈 문자열이므로 저장하지 않음!")
            return // ✅ 빈 값이면 저장하지 않도록 방어 코드 추가
        }
        getPrefs().edit().putString("email", email).apply()
        Log.d("API_save", "email : $email")
    }

    fun getEmail(): String? {
        return getPrefs().getString("email", null)
    }

    fun clearEmail() {
        getPrefs().edit().remove("email").apply()
    }


    // ✅ 특정 키 존재 여부 확인
    fun contains(key: String): Boolean {
        return getPrefs().contains(key)
    }


    // 기본 조사 화면 중복 방지
    fun setSurveyCompleted(context: Context, isCompleted: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_SURVEY_COMPLETED, isCompleted).apply()
    }

    fun isSurveyCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_SURVEY_COMPLETED, false) // 기본값은 false
    }
}
