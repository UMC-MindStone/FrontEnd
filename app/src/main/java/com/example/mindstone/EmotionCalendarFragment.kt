package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentEmotionCalendarBinding
import java.util.Calendar

class EmotionCalendarFragment : Fragment() {

    private var _binding: FragmentEmotionCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: EmotionCalendarVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmotionCalendarBinding.inflate(inflater, container, false)

        setupViewPager()
        setupCalendar() // 캘린더 설정 호출

        return binding.root
    }

    // 뷰페이저 설정
    private fun setupViewPager() {
        viewPagerAdapter = EmotionCalendarVPAdapter(this)
        binding.emotionCalendarStatVp.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(MonthStatFragment())
        viewPagerAdapter.addFragment(WeakStatFragment())
        viewPagerAdapter.addFragment(MonthSummaryFragment())
    }

    // 캘린더 데이터를 생성하고 GridView에 어댑터를 설정하는 메서드
    private fun setupCalendar() {
        val year = 2025 // 표시할 년도
        val month = 1   // 표시할 월 (1월)

        // 캘린더 데이터 생성
        val calendarData = generateCalendarData(year, month)

        // GridView와 어댑터 연결
        val adapter = EmotionCalendarGridAdapter(requireContext(), calendarData)
        binding.emotionCalendarCalendarGv.adapter = adapter
    }

    // 캘린더 데이터를 생성하는 유틸리티 함수
    private fun generateCalendarData(year: Int, month: Int): List<String> {
        val calendar = Calendar.getInstance().apply {
            set(year, month - 1, 1) // month는 0부터 시작
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1: 일요일, 7: 토요일

        val calendarData = mutableListOf<String>()

        // 요일 헤더 추가
        val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
        calendarData.addAll(daysOfWeek)

        // 빈 칸 추가 (첫 번째 요일 이전)
        for (i in 1 until firstDayOfWeek) {
            calendarData.add("") // 빈 칸
        }

        // 날짜 추가
        for (day in 1..daysInMonth) {
            calendarData.add(day.toString())
        }

        return calendarData
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}
