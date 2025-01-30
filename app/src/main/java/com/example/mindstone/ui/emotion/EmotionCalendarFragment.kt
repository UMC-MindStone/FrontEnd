package com.example.mindstone.ui.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindstone.YearMonthPickerDialog
import com.example.mindstone.databinding.FragmentEmotionCalendarBinding
import java.util.Calendar

class EmotionCalendarFragment : Fragment() {

    private var _binding: FragmentEmotionCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: EmotionCalendarVPAdapter
    private var currentYear = 2025 // 초기 년도 설정
    private var currentMonth = 1   // 초기 월 설정 (1월)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmotionCalendarBinding.inflate(inflater, container, false)

        setupViewPager()
        setupCalendar() // 캘린더 설정 호출

        // 오른쪽 버튼 클릭 시 한 달 후로 이동
        binding.emotionCalendarRightIv.setOnClickListener {
            changeMonth(1) // 1은 한 달 후로 이동
        }

        // 왼쪽 버튼 클릭 시 한 달 전으로 이동
        binding.emotionCalendarLeftIv.setOnClickListener {
            changeMonth(-1) // -1은 한 달 전으로 이동
        }

        // 년도/월 선택 버튼 클릭 시 다이얼로그 호출
        binding.emotionCalendarDownIv.setOnClickListener {
            showYearMonthPickerDialog()
        }

        return binding.root
    }

    // 뷰페이저 설정
    private fun setupViewPager() {
        viewPagerAdapter = EmotionCalendarVPAdapter(this)
        binding.emotionCalendarStatVp.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(MonthStatFragment())
        viewPagerAdapter.addFragment(WeakStatFragment())
        viewPagerAdapter.addFragment(MonthSummaryFragment())

        binding.emotionCalendarStatCi.setViewPager(binding.emotionCalendarStatVp)
    }

    // 캘린더 데이터를 생성하고 GridView에 어댑터를 설정하는 메서드
    private fun setupCalendar() {
        // 캘린더 데이터 생성
        val calendarData = generateCalendarData(currentYear, currentMonth)

        // GridView와 어댑터 연결
        val adapter = EmotionCalendarGridAdapter(requireContext(), calendarData)
        binding.emotionCalendarCalendarGv.adapter = adapter

        // 날짜 표시: 2025 1월 형식으로 설정
        binding.emotionCalendarDateTv.text = "${currentYear} ${currentMonth}월"
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

    // 월을 변경하는 메서드
    private fun changeMonth(delta: Int) {
        currentMonth += delta

        if (currentMonth > 12) {
            currentMonth = 1
            currentYear++
        } else if (currentMonth < 1) {
            currentMonth = 12
            currentYear--
        }

        // 캘린더 갱신
        setupCalendar()
        updateViewPagerFragments()
    }

    // 년도와 월 선택 다이얼로그 표시
    private fun showYearMonthPickerDialog() {
        val dialog = YearMonthPickerDialog()
        dialog.onDateSelected = { selectedYear, selectedMonth ->
            // 선택된 년도와 월로 업데이트
            currentYear = selectedYear
            currentMonth = selectedMonth

            // 캘린더 갱신
            setupCalendar()
            updateViewPagerFragments()
        }
        dialog.show(parentFragmentManager, "YearMonthPickerDialog")
    }

    private fun updateViewPagerFragments() {
        viewPagerAdapter.updateFragment(0, MonthStatFragment.newInstance(currentMonth))
        viewPagerAdapter.updateFragment(2, MonthSummaryFragment.newInstance(currentMonth))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
