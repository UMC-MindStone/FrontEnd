package com.example.mindstone.data.model

data class SurveyRequest(
    val nickname: String,
    val birthdate: String,  // yyyy-MM-dd 형식
    val job: String,
    val mbti: String,
    val managementMethod: String // 관리 방법
)
