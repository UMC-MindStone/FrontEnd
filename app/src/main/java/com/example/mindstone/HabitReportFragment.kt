package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentHabitReportBinding

class HabitReportFragment : Fragment() {
    private lateinit var binding: FragmentHabitReportBinding
    private var currentYear = arguments?.getInt("currentYear") ?: 2025
    private var currentMonth = arguments?.getInt("currentMonth") ?: 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitReportBinding.inflate(inflater, container, false)
        currentYear = arguments?.getInt("currentYear") ?: 2025
        currentMonth = arguments?.getInt("currentMonth") ?: 1
        setUpDateText()
        setUpHabitText()

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


        return binding.root
    }

    private fun showYearMonthPickerDialog() {
        val dialog = YearMonthPickerDialog()
        dialog.onDateSelected = { selectedYear, selectedMonth ->
            // 선택된 년도와 월로 업데이트
            currentYear = selectedYear
            currentMonth = selectedMonth
            setUpDateText()
            setUpHabitText()
        }

        dialog.show(parentFragmentManager, "YearMonthPickerDialog")
    }

    private fun setUpDateText(){
        binding.habitReportDateTv.text = "${currentYear} ${currentMonth}월"
    }

    private fun setUpHabitText(){
        binding.habitReportTitleTv.text = "${currentMonth}월 습관 요약"
        binding.habitReportSuccessTv.text = "${currentMonth}월 한달 동안 습관 달성률은 40%입니다."
        binding.habitReportUpTv.text = "${currentMonth}월 초에 비해 ${currentMonth}월 말에 습관 달성률이 50% 증가했습니다."
    }

}