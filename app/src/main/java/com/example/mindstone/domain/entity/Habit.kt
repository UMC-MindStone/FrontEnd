package com.example.mindstone.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.NotActiveException

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

data class HabitHistoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<HabitHistory>
)

data class HabitHistoryTest(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Int
)

data class HabitHistory(
    var habitId: Long? = null,
    var comment: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var habitColor: String? = null
)

data class HabitTotalResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<HabitTotal>
)

data class HabitTotal(
    val habitId: Long,
    val title: String,
    val dayOfWeek: String,
    val alarmTime: String,
    val targetTime: Int,
    val isActive: Boolean,
    val habitColor: String
)

