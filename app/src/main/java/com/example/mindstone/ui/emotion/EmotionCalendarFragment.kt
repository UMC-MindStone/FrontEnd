package com.example.mindstone.ui.emotion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.YearMonthPickerDialog
import com.example.mindstone.databinding.FragmentEmotionCalendarBinding
import com.example.mindstone.domain.entity.EmotionReportResponse
import com.example.mindstone.domain.entity.EmotionReportResponseDTO
import com.example.mindstone.ui.emotion.viewmodel.EmotionCalendarViewModel
import com.example.mindstone.ui.home.diary.CalendarToDiaryFragment
import com.example.mindstone.ui.home.diary.DiaryViewModel
import java.util.Calendar

class EmotionCalendarFragment : Fragment(), EmotionCalendarGridAdapter.onDateClickListener{

    private var _binding: FragmentEmotionCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: EmotionCalendarVPAdapter
    var currentYear = 2025 // 초기 년도 설정
    var currentMonth = 1   // 초기 월 설정 (1월)

    private lateinit var calendarViewModel : EmotionCalendarViewModel
    private lateinit var adapter : EmotionCalendarGridAdapter

    private val diaryViewModel : DiaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmotionCalendarBinding.inflate(inflater, container, false)
        calendarViewModel = ViewModelProvider(this).get(EmotionCalendarViewModel::class.java)

        val calendarData = generateCalendarData(currentYear, currentMonth)

        // GridView와 어댑터 연결
        adapter = EmotionCalendarGridAdapter(requireContext(), calendarData)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.getEmotionCalendar(currentYear, currentMonth)
        //리포트 받아오기 실행


        calendarViewModel.emotionMap.observe(viewLifecycleOwner){ emotion ->
            adapter.setEmotionData(emotion)
        }

        calendarViewModel.emotionReportResponse.observe(viewLifecycleOwner){response ->
            if(response?.isSuccess == true){
                Log.d("EmotionReport", "observer 가동")
                response.result?.let { updateViewPagerFragments(it,currentMonth) }
            }else{
                Log.d("EmotionReport", "observer 가동 실패 : ${response?.message}")
            }

        }

        calendarViewModel.getEmotionCalendarReport(currentYear, currentMonth)
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
        adapter = EmotionCalendarGridAdapter(requireContext(), calendarData)
        binding.emotionCalendarCalendarGv.adapter = adapter
        adapter.listener = this
        // 날짜 표시: 2025 1월 형식으로 설정
        binding.emotionCalendarDateTv.text = "${currentYear} ${currentMonth}월"
    }

    override fun onDateClick(date: String, isRecord: Boolean) {
        diaryViewModel.fetchDiary(date)

        diaryViewModel.diaryExists.observe(viewLifecycleOwner) { exists ->
            val fragment = if (exists) {
                // CalendarToDiaryFragment 로 이동
                CalendarToDiaryFragment().apply{
                    arguments = Bundle().apply{
                        putString("date", date)
                        putBoolean("isRecord", true)
                    }
                    Log.d("date", date)
                }
            } else {
                // ✅ Fragment 2 (새로운 일기 작성 화면)으로 이동
                BeforeDiaryFragment().apply{
                    arguments = Bundle().apply{
                        putString("date", date)
                        putBoolean("isRecord", false)
                    }
                    Log.d("date", date)
                }

            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        }
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

        //updateVIewPager대신 이제 observe가 감지하고 내용을 바꿀거임!
        calendarViewModel.getEmotionCalendarReport(currentYear, currentMonth)
        //updateViewPagerFragments()
        calendarViewModel.getEmotionCalendar(currentYear, currentMonth)
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
            calendarViewModel.getEmotionCalendarReport(currentYear, currentMonth)
            //updateViewPagerFragments()
        }
        dialog.show(parentFragmentManager, "YearMonthPickerDialog")
    }

    private fun updateViewPagerFragments(response: EmotionReportResponseDTO, month: Int) {
        viewPagerAdapter.updateFragment(0, MonthStatFragment.newInstance(response.totalReport))
        viewPagerAdapter.updateFragment(2, MonthSummaryFragment.newInstance(month,response.totalSummary))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
