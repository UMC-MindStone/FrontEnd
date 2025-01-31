package com.example.mindstone.ui.auth.login

import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.TempPasswordRequest
import kotlinx.coroutines.launch

class FindPasswordViewModel : ViewModel() {

    fun sendTempPassword(email: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("FindPasswordViewModel", "비밀번호 재설정 API 요청 시작 - 이메일: $email")

                val response = RetrofitClient.authService.sendTempPassword(TempPasswordRequest(email))
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    onSuccess("입력하신 이메일로 임시 비밀번호가 전송되었습니다.")
                } else {
                    onError(response.body()?.message ?: "오류가 발생했습니다.")
                }
            } catch (e: HttpException) {
                onError("네트워크 오류가 발생했습니다.")
            } catch (e: Exception) {
                onError("알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}