package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mindstone.databinding.FragmentYearMonthPickerBinding // 자동 생성된 바인딩 클래스 이름

class YearMonthPickerDialog : DialogFragment() {

    private var _binding: FragmentYearMonthPickerBinding? = null
    private val binding get() = _binding!!

    var onDateSelected: ((year: Int, month: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYearMonthPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Year Picker 설정
        binding.yearMonthYearNp.minValue = 2020
        binding.yearMonthYearNp.maxValue = 2030
        binding.yearMonthYearNp.value = 2025 // 기본값 (현재 연도)

        // Month Picker 설정
        binding.yearMonthMonthNp.minValue = 1
        binding.yearMonthMonthNp.maxValue = 12
        binding.yearMonthMonthNp.value = 1 // 기본값 (1월)

        // 확인 버튼 클릭 이벤트
        binding.yearMonthCheckTv.setOnClickListener {
            val selectedYear = binding.yearMonthYearNp.value
            val selectedMonth = binding.yearMonthMonthNp.value
            onDateSelected?.invoke(selectedYear, selectedMonth)
            dismiss() // 다이얼로그 닫기
        }
    }

    override fun onStart() {
        super.onStart()
        // Dialog 크기 설정
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
