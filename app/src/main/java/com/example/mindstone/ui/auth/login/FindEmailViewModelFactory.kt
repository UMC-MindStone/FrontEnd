package com.example.mindstone.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.domain.usecase.FindEmailUseCase

class FindEmailViewModelFactory(private val useCase: FindEmailUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindEmailViewModel::class.java)) {
            return FindEmailViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
