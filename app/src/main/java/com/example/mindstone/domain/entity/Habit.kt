package com.example.mindstone.domain.entity

data class Habit(
    val title: String,        // 제목
    val duration: String,     // 지속 시간
    val alarmDays: String,    // 알림 요일
    val alarmTimes: String,   // 알림 시간
    var isEnabled: Boolean     // 스위치 상태
)

