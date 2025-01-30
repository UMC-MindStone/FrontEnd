package com.example.mindstone.domain.usecase

import com.example.mindstone.domain.entity.EmailResult
import com.example.mindstone.domain.repository.FindEmailRepository

// 이메일 찾기 기능을 수행하는 UseCase

class FindEmailUseCase(private val repository: FindEmailRepository) {
    suspend fun execute(email: String): Result<EmailResult> {
        return repository.findEmail(email)
    }
}
