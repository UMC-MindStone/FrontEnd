package com.example.mindstone.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.YearMonthPickerDialog
import com.example.mindstone.databinding.FragmentHabitReportBinding
import com.example.mindstone.ui.habit.viewmodel.HabitCalendarViewModel

class HabitReportFragment : Fragment() {
    private lateinit var binding: FragmentHabitReportBinding
    private var currentYear = arguments?.getInt("currentYear") ?: 2025
    private var currentMonth = arguments?.getInt("currentMonth") ?: 1

    private lateinit var viewModel: HabitCalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitReportBinding.inflate(inflater, container, false)
        currentYear = arguments?.getInt("currentYear") ?: 2025
        currentMonth = arguments?.getInt("currentMonth") ?: 1
        setUpDateText()
        //setUpHabitText()

        viewModel = ViewModelProvider(this).get(HabitCalendarViewModel::class.java)
        viewModel.getHabitReport(currentYear, currentMonth)


        binding.habitReportDownIv.setOnClickListener {
            showYearMonthPickerDialog()
        }

        binding.habitReportCloseIv.setOnClickListener {
            val fragment = HabitCalendarFragment()
            val bundle = Bundle()
            bundle.putInt("currentYear", currentYear)
            bundle.putInt("currentMonth", currentMonth)
            fragment.arguments = bundle

            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.habit_report_container_fl, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.habitCalendarRightIv.setOnClickListener {
            changeMonth(1) // 1은 한 달 후로 이동
        }

        // 왼쪽 버튼 클릭 시 한 달 전으로 이동
        binding.habitCalendarLeftIv.setOnClickListener {
            changeMonth(-1) // -1은 한 달 전으로 이동
        }


        return binding.root
    }

    private fun showYearMonthPickerDialog() {
        val dialog = YearMonthPickerDialog()
        dialog.onDateSelected = { selectedYear, selectedMonth ->
            // 선택된 년도와 월로 업데이트
            currentYear = selectedYear
            currentMonth = selectedMonth
            setUpDateText()
            //viewModel.getHabitReport(currentYear, currentMonth)
            //setUpHabitText()
        }

        dialog.show(parentFragmentManager, "YearMonthPickerDialog")
    }

    private fun setUpDateText(){
        binding.habitReportDateTv.text = "${currentYear} ${currentMonth}월"
    }

    private fun setUpHabitText(recordPercentage: Double, achievementGrowth: Double, topHabit: String){
        binding.habitReportTitleTv.text = "${currentMonth}월 습관 요약"
        binding.habitReportSuccessTv.text = "${currentMonth}월 한달 동안 습관 달성률은 ${recordPercentage}%입니다."
        binding.habitReportUpTv.text = "${currentMonth}월 초에 비해 ${currentMonth}월 말에 습관 달성률이 ${achievementGrowth}% 증가했습니다."
        binding.habitReportHighestTv.text = "달성률이 가장 높은 습관은 ‘${topHabit}’ 입니다."
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
        setUpDateText()
        //viewModel.getHabitReport(currentYear, currentMonth)
        //setUpHabitText()
    }

}