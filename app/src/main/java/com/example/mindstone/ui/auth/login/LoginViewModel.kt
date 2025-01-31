package com.example.mindstone.ui.auth.login

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindstone.data.remote.LoginService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.LoginRequest
import com.example.mindstone.domain.entity.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>() // 로그인 결과 메시지
    val loginResult: LiveData<String> get() = _loginResult

    private val loginService = RetrofitClient.loginService

    // 로그인 처리
    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        loginService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        _loginResult.value = "로그인 성공"
                    } else {
                        _loginResult.value = "로그인 실패: ${body?.message ?: "알 수 없는 오류"}"
                    }
                } else {
                    _loginResult.value = "로그인 실패: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResult.value = "네트워크 오류 발생: ${t.message}"
            }
        })
    }
}
