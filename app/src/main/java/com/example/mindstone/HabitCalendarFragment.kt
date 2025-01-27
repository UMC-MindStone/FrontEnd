package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentHabitCalendarBinding
import java.util.Calendar

class HabitCalendarFragment : Fragment() {
    private var _binding: FragmentHabitCalendarBinding? = null
    private val binding get() = _binding!!

    private var currentYear = 2025 // 초기 년도 설정
    private var currentMonth = 1   // 초기 월 설정 (1월)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitCalendarBinding.inflate(inflater, container, false)

        setupCalendar()

        binding.habitCalendarRightIv.setOnClickListener {
            changeMonth(1) // 1은 한 달 후로 이동
        }

        // 왼쪽 버튼 클릭 시 한 달 전으로 이동
        binding.habitCalendarLeftIv.setOnClickListener {
            changeMonth(-1) // -1은 한 달 전으로 이동
        }

        // 년도/월 선택 버튼 클릭 시 다이얼로그 호출
        binding.habitCalendarDownIv.setOnClickListener {
            showYearMonthPickerDialog()
        }

        binding.habitCalendarGraphIv.setOnClickListener {
            val fragment = HabitReportFragment()
            val bundle = Bundle()
            bundle.putInt("currentYear", currentYear)
            bundle.putInt("currentMonth", currentMonth)
            fragment.arguments = bundle

            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.habit_calendar_container_fl, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    private fun setupCalendar() {
        // 캘린더 데이터 생성
        val calendarData = generateCalendarData(currentYear, currentMonth)

        // GridView와 어댑터 연결
        val adapter = HabitCalendarGridAdapter(requireContext(), calendarData) { date ->
            onDateClicked(date)
        }
        binding.habitCalendarCalendarGv.adapter = adapter

        // 날짜 표시: 2025 1월 형식으로 설정
        binding.habitCalendarDateTv.text = "${currentYear} ${currentMonth}월"
    }

    private fun onDateClicked(date: String) {
        val day = date.toInt()

        val calendar = Calendar.getInstance().apply {
            set(currentYear, currentMonth - 1, day) // currentMonth는 1부터 시작하므로 -1 필요
        }

        val year = calendar.get(Calendar.YEAR) // 연도 가져오기
        val month = calendar.get(Calendar.MONTH) + 1 // 월 (0부터 시작하므로 +1 필요)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) // 일
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 요일 (1: 일요일, 7: 토요일)

        // 요일을 문자열로 변환
        val dayOfWeekStr = when (dayOfWeek) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }

        val fragment = HabitCheckFragment()

        val bundle = Bundle().apply {
            putInt("year", year)
            putInt("month", month)
            putInt("day", dayOfMonth)
            putString("dayOfWeek", dayOfWeekStr)
        }
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.habit_calendar_container_fl, fragment)
            .addToBackStack(null)
            .commit()
    }



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

    private fun showYearMonthPickerDialog() {
        val dialog = YearMonthPickerDialog()
        dialog.onDateSelected = { selectedYear, selectedMonth ->
            // 선택된 년도와 월로 업데이트
            currentYear = selectedYear
            currentMonth = selectedMonth

            // 캘린더 갱신
            setupCalendar()
        }


        dialog.show(parentFragmentManager, "YearMonthPickerDialog")
    }

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
    }
}