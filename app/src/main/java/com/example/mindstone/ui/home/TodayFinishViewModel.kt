package com.example.mindstone.ui.home

import androidx.lifecycle.ViewModel
import com.example.mindstone.MainActivity
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.Calendar

class TodayFinishViewModel : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun scheduleFragmentAtSpecificTime(activity: MainActivity, targetHour: Int, targetMinute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
        }

        val targetTimeMillis = calendar.timeInMillis
        val currentTimeMillis = System.currentTimeMillis()
        var delay = targetTimeMillis - currentTimeMillis

        if (delay < 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            delay = calendar.timeInMillis - currentTimeMillis
        }

        runnable = Runnable {
            activity.showTargetFragment()// 특정 시간에 Fragment 변경
        }

        runnable?.let { handler.postDelayed(it, delay) }

        Log.d("FragmentScheduler", "Fragment will be shown at: ${calendar.time}")
    }

    fun cancelScheduledFragment() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    override fun onCleared() {
        super.onCleared()
        cancelScheduledFragment() // ViewModel이 사라질 때 핸들러도 해제
    }
}
