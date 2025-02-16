package com.example.mindstone.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val title: String,
    val duration: String,
    val alarmDays: String,
    val alarmTimes: String,
    var isEnabled: Boolean // ✅ Switch 상태 저장
) : Parcelable

data class HabitCalendarResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: HabitCalendarResult?
)

data class HabitCalendarResult(
    val recordPercentage: Int,
    val fullAchievementCount: Int,
    val dailyRecords: List<DailyRecord>
)

data class DailyRecord(
    val day: Int,
    val completedHabits: Int,
    val totalHabits: Int
)