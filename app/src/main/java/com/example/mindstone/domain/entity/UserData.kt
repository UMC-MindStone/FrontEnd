package com.example.mindstone.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val title: String,
    val dayOfWeek: String,  // "1010101" 형태의 문자열
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
    var stressManagement: ArrayList<String> = arrayListOf(),
    var hobbies: ArrayList<String> = arrayListOf(),
    var specialSkills: ArrayList<String> = arrayListOf(),
    var habits: ArrayList<Habit> = arrayListOf()
) : Parcelable
