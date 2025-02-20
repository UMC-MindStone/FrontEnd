package com.example.mindstone.data.local

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("birthDate") var birthDate: String = "",
    @SerializedName("job") var job: String = "",
    @SerializedName("mbti") var mbti: String = "",
    @SerializedName("stressManagement") var stressManagement: List<String> = listOf(),  // ✅ 다시 List로 변경!
    @SerializedName("hobbies") var hobbies: List<String> = listOf(),  // ✅ 다시 List로 변경!
    @SerializedName("specialSkills") var specialSkills: List<String> = listOf(),  // ✅ 다시 List로 변경!
    @SerializedName("habits") var habits: List<Habit>? = null // ✅ null 허용
) : Parcelable {

    @Parcelize
    data class Habit(
        val title: String,
        val dayOfWeek: String,
        val alarmTime: String,
        val targetTime: Int,
        val isActive: Boolean,
        val habitColor: String
    ) : Parcelable

}

