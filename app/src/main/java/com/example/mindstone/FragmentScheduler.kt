package com.example.mindstone

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.ref.WeakReference
import java.util.Calendar

class FragmentScheduler(private val activity: WeakReference<MainActivity>) {

    private val handler = Handler(Looper.getMainLooper()) // 메인 스레드에서 실행
    private var runnable: Runnable? = null // 실행할 작업 저장

    fun scheduleFragmentAtSpecificTime(targetHour: Int, targetMinute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour) // 24시간제 사용 (예: 13 = 오후 1시)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0) // 초는 0으로 설정
        }

        val targetTimeMillis = calendar.timeInMillis
        val currentTimeMillis = System.currentTimeMillis()
        var delay = targetTimeMillis - currentTimeMillis // 실행까지 남은 시간 계산

        // 만약 시간이 이미 지났다면, 다음 날 같은 시간으로 설정
        if (delay < 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1) // 하루 추가
            delay = calendar.timeInMillis - currentTimeMillis
        }

        // Runnable 정의 (Fragment 변경 작업)
        runnable = Runnable {
            activity.get()?.let { mainActivity ->
                mainActivity.showTargetFragment() // 특정 시간에 Fragment 변경
            }
        }

        // 지정된 시간에 실행되도록 Handler 설정
        runnable?.let { handler.postDelayed(it, delay) }

        Log.d("FragmentScheduler", "Fragment will be shown at: ${calendar.time}")
    }

    fun cancelScheduledFragment() {
        runnable?.let { handler.removeCallbacks(it) }
    }
}
