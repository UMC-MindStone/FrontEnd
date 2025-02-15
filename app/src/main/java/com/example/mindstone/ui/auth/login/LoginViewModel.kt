package com.example.mindstone.ui.auth.login

import com.example.mindstone.data.local.PreferenceManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.LoginService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.LoginRequest
import com.example.mindstone.domain.entity.LoginResponse
import com.example.mindstone.domain.entity.RefreshTokenRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String?>() // 🔹 일반 로그인 결과 메시지
    val loginResult: LiveData<String?> get() = _loginResult

    private val _refreshTokenResult = MutableLiveData<Boolean?>() // 🔹 Refresh Token 결과
    val refreshTokenResult: LiveData<Boolean?> get() = _refreshTokenResult

    private val loginService = RetrofitClient.loginService

    // 로그인 처리
    fun login(email: String, password: String) {

        val request = LoginRequest(email, password)
        Log.d("LoginViewModel", "login: 로그인 요청 - 이메일: $email")
        Log.d("LoginViewModel", "로그인 요청: $request")

        loginService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        val accessToken = body.result?.accessToken ?: ""
                        val refreshToken = body.result?.refreshToken ?: ""

                        // ✅ AccessToken & RefreshToken 저장
                        PreferenceManager.saveAccessToken(accessToken)
                        PreferenceManager.saveRefreshToken(refreshToken)
                        PreferenceManager.saveEmail(email)

                        Log.d("LoginViewModel", "로그인 성공 - AccessToken 저장 완료")
                        _loginResult.value = "로그인 성공"
                    } else {
                        Log.e("LoginViewModel", "로그인 실패: ${body?.message}")
                        _loginResult.value = "로그인 실패: ${body?.message ?: "알 수 없는 오류"}"
                    }
                } else {
                    Log.e("LoginViewModel", "로그인 실패: ${response.code()}")
                    _loginResult.value = "로그인 실패: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginViewModel", "네트워크 오류 발생: ${t.message}")
                _loginResult.value = "네트워크 오류 발생: ${t.message}"
            }
        })
    }

    // Refresh Token을 사용하여 Access Token 갱신
    fun refreshAccessToken(callback: (Boolean) -> Unit) {
        val refreshToken = PreferenceManager.getRefreshToken()
        val email = PreferenceManager.getEmail()

        if (refreshToken.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.e("LoginViewModel", "RefreshToken 또는 Email이 없음 - 다시 로그인 필요")
            _refreshTokenResult.value = false  // 자동 로그인 실패
            return
        }

        val request = RefreshTokenRequest(refreshToken, email)
        Log.d("LoginViewModel", "refreshAccessToken: RefreshToken 요청")

        loginService.refreshAccessToken(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        val newAccessToken = body.result?.accessToken ?: ""
                        val newRefreshToken = body.result?.refreshToken ?: ""

                        // ✅ 새롭게 발급된 AccessToken & RefreshToken 저장
                        PreferenceManager.saveAccessToken(newAccessToken)
                        PreferenceManager.saveRefreshToken(newRefreshToken)

                        Log.d("LoginViewModel", "AccessToken 갱신 성공 - 새로운 AccessToken 저장")
                        _refreshTokenResult.value = true
                    } else {
                        Log.e("LoginViewModel", "AccessToken 갱신 실패: ${body?.message}")
                        _refreshTokenResult.value = false
                    }
                } else {
                    Log.e("LoginViewModel", "AccessToken 갱신 실패: ${response.code()}")
                    _refreshTokenResult.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginViewModel", "AccessToken 갱신 네트워크 오류: ${t.message}")
                _refreshTokenResult.value = false
            }
        })
    }
}

