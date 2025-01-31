package com.example.mindstone.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstone.domain.usecase.FindEmailUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FindEmailViewModel(private val useCase: FindEmailUseCase) : ViewModel() {

    private val _emailResult = MutableStateFlow<FindEmailResultState>(FindEmailResultState.Idle)

    val emailResult: StateFlow<FindEmailResultState> = _emailResult

    fun findEmail(email: String) {
        viewModelScope.launch {
            _emailResult.value = FindEmailResultState.Loading
            val result = useCase.execute(email)
            _emailResult.value = if (result.isSuccess) {
                FindEmailResultState.Success(result.getOrNull()?.email ?: "")
            } else {
                FindEmailResultState.Failure(result.exceptionOrNull()?.message ?: "이메일을 찾을 수 없습니다.")
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
