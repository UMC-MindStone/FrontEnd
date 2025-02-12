package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mindstone.databinding.FragmentMonthDayPickerBinding

class MonthDayPickerFragment : DialogFragment() {

    private var _binding : FragmentMonthDayPickerBinding? = null
    private val binding get() = _binding!!

    var onDateSelected: ((month: Int, day: Int)-> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthDayPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // month picker 설정
        binding.monthNp.minValue = 1
        binding.monthNp.maxValue = 12
        binding.monthNp.value = 1



        // day picker 설정
        binding.dayNp.value = 1
        binding.dayNp.minValue = 1
        setDaybyMonth()


        //확인 버튼 클릭 이벤트
        binding.monthDayCheckTv.setOnClickListener{
            val selectedMonth = binding.monthNp.value
            val selectedDay= binding.dayNp.value
            onDateSelected?.invoke(selectedMonth, selectedDay)
            dismiss()
        }
    }

    override fun onStart(){
        super.onStart()

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

    private fun setDaybyMonth(){
        var month = binding.monthNp.value
        if( month ==2){
            binding.dayNp.maxValue = 28
        }
        else if(month <8 ){
            if( month % 2 ==1){
                binding.dayNp.maxValue= 31
            } else {
                binding.dayNp.maxValue= 30
            }
        } else {
            if( month %2 ==1){
                binding.dayNp.maxValue = 30
            } else {
                binding.dayNp.maxValue = 31
            }
        }
    }


}