package com.example.mindstone.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.LoginService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.LoginRequest
import com.example.mindstone.domain.entity.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>() // 로그인 결과 메시지
    val loginResult: LiveData<String> get() = _loginResult

    private val _accessToken = MutableLiveData<String>() // ✅ AccessToken을 LiveData로 관리
    val accessToken: LiveData<String> get() = _accessToken

    private val loginService: LoginService = RetrofitClient.loginService

    // ✅ 로그인 처리
    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        loginService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("API_RESPONSE", "서버 응답 코드: ${response.code()}")
                Log.d("API_RESPONSE", "서버 응답 본문: ${response.body()?.toString()}") // ✅ 응답 본문 로그 추가

                if (response.isSuccessful && response.body() != null) {
                    val accessToken = response.body()?.result?.accessToken ?: return // ✅ accessToken이 null이면 함수 종료
                    val refreshToken = response.body()?.result?.refreshToken ?: ""

                    if (accessToken.isNotEmpty()) {
                        _accessToken.postValue(accessToken) // ✅ LiveData 업데이트
                        PreferenceManager.saveAccessToken(accessToken) // ✅ AccessToken 저장
                        _loginResult.postValue("로그인 성공")
                        Log.d("API_AUTH", "로그인 성공: AccessToken 저장됨 ($accessToken)")
                    } else {
                        _loginResult.postValue("로그인 실패: AccessToken이 없습니다.")
                        Log.e("API_AUTH", "로그인 성공했지만 AccessToken 없음!")
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
}
