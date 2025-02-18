package com.example.mindstone.ui.home.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.mindstone.MainActivity
import com.example.mindstone.databinding.FragmentDiaryLoadingBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DiaryLoadingFragment : Fragment() {
    private var _binding : FragmentDiaryLoadingBinding? = null
    private val binding get() = _binding!!

    private var currentYear: Int = 2025
    private var currentMonth: Int = 1
    private var currentDay: Int = 1
    private var date: String? = null

    private var fragment : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString("date", "2025-01-01") // 기본값 설정
            fragment = it.getString("fragment", null)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDiaryLoadingBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val bundle = bundleOf(
        "fragment" to fragment,
        "date" to date
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(fragment == "today"){
            //오늘의 날짜 띄우기
            val today = LocalDate.now()
            val year= today.year
            val month = today.monthValue
            val day = today.dayOfMonth
            val date = LocalDate.of( year, month, day)
            // 요일 가져오기
            val dayOfWeek = date.dayOfWeek
            // 요일을 문자열로 변환 (한글 출력 가능)
            val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, java.util.Locale.KOREAN)

            binding.diaryDateTv.text = "${month}월 ${day}일 $dayName"
        } else{
            updateDateUI()
        }

        binding.diaryLoadingCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun changeScreen(){
        //서버에서 자동 완성된 일기가 넘어오는 순간 홈-자동일기완성 화면으로 넘어가는 것 구현
    }

    private fun updateDateUI() {
        val date = LocalDate.of(currentYear, currentMonth, currentDay)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        binding.diaryDateTv.text = "${currentMonth}월 ${currentDay}일 $dayOfWeek"
    }

}