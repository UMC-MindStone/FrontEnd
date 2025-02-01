package com.example.mindstone.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.FindEmailRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

class FindEmailViewModel : ViewModel() {

    private val _emailResult = MutableStateFlow<FindEmailResultState>(FindEmailResultState.Idle)
    val emailResult: StateFlow<FindEmailResultState> = _emailResult

    fun findEmail(email: String) {
        viewModelScope.launch {
            _emailResult.value = FindEmailResultState.Loading
            try {
                Log.d("FindEmailViewModel", "이메일 찾기 요청 보냄: $email")

                val response = RetrofitClient.authService.findEmail(FindEmailRequest(email))
                if (response.isSuccessful && response.body()?.isSuccess == true) {

                    Log.d("FindEmailViewModel", "이메일 찾기 성공: ${response.body()?.result?.email}")

                    _emailResult.value = FindEmailResultState.Success(response.body()?.result?.email ?: "")
                } else {

                    Log.e("FindEmailViewModel", "이메일 찾기 실패 - 서버 응답 오류: ${response.body()?.message}")

                    _emailResult.value = FindEmailResultState.Failure(response.body()?.message ?: "이메일을 찾을 수 없습니다.")
                }
            } catch (e: HttpException) {
                _emailResult.value = FindEmailResultState.Failure("서버 오류가 발생했습니다.")
            } catch (e: Exception) {
                _emailResult.value = FindEmailResultState.Failure("네트워크 오류가 발생했습니다.")
            }
        }
    }
}

sealed class FindEmailResultState {
    object Idle : FindEmailResultState()
    object Loading : FindEmailResultState()
    data class Success(val email: String) : FindEmailResultState()
    data class Failure(val message: String) : FindEmailResultState()
}
