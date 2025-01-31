package com.example.mindstone.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.FindEmailRequest
import com.example.mindstone.domain.usecase.FindEmailUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FindEmailViewModel : ViewModel() {

    private val _emailResult = MutableLiveData<FindEmailResultState>()
    val emailResult: LiveData<FindEmailResultState> get() = _emailResult

    fun findEmail(email: String) {
        viewModelScope.launch {
            _emailResult.value = FindEmailResultState.Loading
            try {
                val response = RetrofitClient.authService.findEmail(FindEmailRequest(email))
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _emailResult.value = FindEmailResultState.Success(response.body()?.result?.email ?: "")
                } else {
                    _emailResult.value = FindEmailResultState.Failure(response.body()?.message ?: "이메일을 찾을 수 없습니다.")
                }
            } catch (e: Exception) {
                _emailResult.value = FindEmailResultState.Failure("네트워크 오류 발생")
            }
        }
    }
}

sealed class FindEmailResultState {
    object Loading : FindEmailResultState()
    data class Success(val email: String) : FindEmailResultState()
    data class Failure(val message: String) : FindEmailResultState()
}
