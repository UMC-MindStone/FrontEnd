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
    val result: List<HabitHistoryPatch>
)

data class HabitHistoryTest(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Long
)

data class HabitHistoryPost(
    var habitId: Long? = null,
    var comment: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var habitColor: String? = null
)

data class HabitHistoryPatch(
    var habitHistoryId: Long? = null,
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

//습관 리포트
data class HabitReportResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: HabitReport
)

data class HabitReport(
    val recordPercentage: Double,
    val achievementGrowth: Double,
    val topHabit: String,
    val weeklyAchievementRates: List<WeeklyData>,
    val weeklyActiveTime: List<WeeklyData>,
    val weeklyHabitCounts: List<WeeklyData>
)

data class WeeklyData(
    val week: Int,
    val habitId: Long,
    val value: Long
)


//habitHistory PATCH
data class NewHabitHistoryPatch(
    val habitHistoryId: Long? = null,
    val excutionId: Long? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val habitColor: String? = null,
    val comment: String? = null
)

data class NewHabitHistoryPatchResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

//habitHistory time POST
data class NewHabitHistoryTimePost(
    val habitHistoryId: Long,
    val startTime: String,
    val endTime: String
)

data class NewHabitHistoryTimePostResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Long
)