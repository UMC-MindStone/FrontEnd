package com.example.mindstone.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_AUTO_LOGIN = "autoLogin"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    // ✅ AccessToken 저장
    fun saveAccessToken(token: String) {
        prefs?.edit()?.putString(KEY_ACCESS_TOKEN, token)?.apply()
        Log.d("API_AUTH", "AccessToken 저장 완료: $token")
    }

    // ✅ AccessToken 불러오기
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

    fun getRefreshToken(): String? {
        return prefs?.getString("refreshToken", null)
    }

    fun saveRefreshToken(token: String) {
        prefs?.edit()?.putString("refreshToken", token)?.apply()
        Log.d("API_AUTH", "RefreshToken 저장 완료: $token")
    }

}
