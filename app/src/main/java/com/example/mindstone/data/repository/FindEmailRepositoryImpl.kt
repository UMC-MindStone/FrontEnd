package com.example.mindstone.data.repository

import com.example.mindstone.data.remote.AuthService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.EmailResult
import com.example.mindstone.domain.entity.FindEmailRequest
import com.example.mindstone.domain.repository.FindEmailRepository
import retrofit2.HttpException

// AuthService를 사용하여 API를 호출하고 결과를 반환하는 Repository 구현체

class FindEmailRepositoryImpl : FindEmailRepository {

    private val authService = RetrofitClient.AuthService

    override suspend fun findEmail(email: String): Result<EmailResult> {
        return try {
            val response = authService.findEmail(FindEmailRequest(email))
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                Result.success(response.body()?.result!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "이메일을 찾을 수 없습니다."))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("서버 오류가 발생했습니다."))
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다."))
        }
    }
}
