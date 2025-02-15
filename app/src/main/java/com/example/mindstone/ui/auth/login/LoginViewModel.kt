package com.example.mindstone.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.LoginService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.LoginRequest
import com.example.mindstone.domain.entity.LoginResponse
import com.example.mindstone.domain.entity.RefreshTokenRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.mindstone.data.remote.AuthService
import com.example.mindstone.domain.entity.RefreshTokenResponse

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>() // 로그인 결과 메시지
    val loginResult: LiveData<String> get() = _loginResult

    private val _accessToken = MutableLiveData<String>() // AccessToken을 LiveData로 관리
    val accessToken: LiveData<String> get() = _accessToken

    private val _refreshToken = MutableLiveData<String>() // RefreshToken을 LiveData로 관리
    val refreshToken: LiveData<String> get() = _refreshToken

    private val _refreshTokenResult = MutableLiveData<Boolean?>() // ✅ Refresh Token 결과
    val refreshTokenResult: LiveData<Boolean?> get() = _refreshTokenResult

    private val loginService: LoginService = RetrofitClient.loginService
    private val authService: AuthService = RetrofitClient.authService

    // ✅ 로그인 처리
    fun login(email: String, password: String) {

        Log.d("LOGIN_API", "로그인 요청 시작: email=$email, password=$password")

        val request = LoginRequest(email, password)

        loginService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("API_RESPONSE", "서버 응답 코드: ${response.code()}")
                Log.d("API_RESPONSE", "서버 응답 본문: ${response.body()?.toString()}") // ✅ 응답 본문 로그 추가

                if (response.isSuccessful && response.body() != null) {
                    val accessToken = response.body()?.result?.accessToken ?: return // ✅ accessToken이 null이면 함수 종료
                    val refreshToken = response.body()?.result?.refreshToken ?: ""

                    if (accessToken.isNotEmpty()) {
                        _accessToken.postValue(accessToken)
                        _refreshToken.postValue(refreshToken)

                        PreferenceManager.saveAccessToken(accessToken) // ✅ AccessToken 저장
                        PreferenceManager.saveRefreshToken(refreshToken) // ✅ RefreshToken 저장
                        PreferenceManager.saveEmail(email)

                        _loginResult.postValue("로그인 성공")
                        Log.d("API_AUTH", "로그인 성공: AccessToken, RefreshToken 저장됨 ($accessToken, $refreshToken)")
                    } else {
                        _loginResult.postValue("로그인 실패: AccessToken 또는 RefreshToken 없음")
                        Log.e("API_AUTH", "로그인 성공했지만 AccessToken 또는 RefreshToken 없음")
                    }
                } else {
                    val errorMessage = "로그인 실패: ${response.code()}"
                    _loginResult.postValue(errorMessage)
                    Log.e("API_AUTH", errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val errorMessage = "네트워크 오류 발생: ${t.message}"
                _loginResult.postValue(errorMessage)
                Log.e("API_FAILURE", errorMessage)
            }
        })
    }

    // ✅ Refresh Token을 사용하여 Access Token 갱신
    fun refreshAccessToken(callback: (Boolean) -> Unit) {
        val refreshToken = PreferenceManager.getRefreshToken()
        val email = PreferenceManager.getEmail()

        if (refreshToken.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.e("LoginViewModel", "🔴 RefreshToken 또는 Email이 없음 - 다시 로그인 필요")
            _refreshTokenResult.value = false  // 자동 로그인 실패
            callback(false) // 콜백 추가
            return
        }

        val request = RefreshTokenRequest(refreshToken, email)
        Log.d("LoginViewModel", "🔄 refreshAccessToken: RefreshToken 요청")

        authService.refreshAccessToken(request).enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        val newAccessToken = body.result?.accessToken ?: ""
                        val newRefreshToken = body.result?.refreshToken ?: ""

                        // ✅ 새롭게 발급된 AccessToken & RefreshToken 저장
                        PreferenceManager.saveAccessToken(newAccessToken)
                        PreferenceManager.saveRefreshToken(newRefreshToken)
                        _accessToken.postValue(newAccessToken) // ✅ LiveData 업데이트

                        Log.d("LoginViewModel", "✅ AccessToken 갱신 성공 - 새로운 AccessToken 저장")
                        _refreshTokenResult.postValue(true)
                        _loginResult.postValue("로그인 성공") // ✅ 자동 로그인 UI 업데이트 반영
                        callback(true)
                    } else {
                        Log.e("LoginViewModel", "🔴 AccessToken 갱신 실패: ${body?.message}")
                        _refreshTokenResult.postValue(false)
                        callback(false)
                    }
                } else {
                    Log.e("LoginViewModel", "🔴 AccessToken 갱신 실패: ${response.code()}")
                    _refreshTokenResult.postValue(false)
                    callback(false)
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.e("LoginViewModel", "🔴 AccessToken 갱신 네트워크 오류: ${t.message}")
                _refreshTokenResult.postValue(false)
                callback(false)
            }
        })
    }
}


