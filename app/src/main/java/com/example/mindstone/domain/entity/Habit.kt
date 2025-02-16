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
