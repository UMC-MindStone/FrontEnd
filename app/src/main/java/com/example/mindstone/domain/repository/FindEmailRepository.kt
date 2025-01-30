package com.example.mindstone.domain.repository

import com.example.mindstone.domain.entity.EmailResult

interface FindEmailRepository {     // 이메일 찾기 기능을 위한 Repository 인터페이스 (UseCase에서 사용)
    suspend fun findEmail(email: String): Result<EmailResult>
}