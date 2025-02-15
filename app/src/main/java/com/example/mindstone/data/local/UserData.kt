package com.example.mindstone.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val title: String,
    val dayOfWeek: List<String>,
    val alarmTime: String,
    val targetTime: Int,
    val isActive: Boolean,
    val habitColor: String
) : Parcelable


@Parcelize
data class UserData(
    var nickname: String = "",
    var birthDate: String = "",
    var job: String = "",
    var mbti: String = "",
    var stressManagement: ArrayList<String> = arrayListOf(),  // 스트레스 관리법 (Array)
    var hobbies: ArrayList<String> = arrayListOf(),          // 취미 (Array)
    var specialSkills: ArrayList<String> = arrayListOf(),    // 특기 (Array)
    var habits: ArrayList<Habit> = arrayListOf()            // 습관 리스트 (Array)
) : Parcelable

