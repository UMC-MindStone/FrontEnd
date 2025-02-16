package com.example.mindstone.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurveyResponse(
    val code: String,
    val message: String,
    val httpStatus: String,
    val success: Boolean
) : Parcelable
