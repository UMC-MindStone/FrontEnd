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

class DiaryLoadingFragment : Fragment() {
    private var _binding : FragmentDiaryLoadingBinding? = null
    private val binding get() = _binding!!


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
        "fragment" to "today"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.diaryLoadingCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun changeScreen(){
        //서버에서 자동 완성된 일기가 넘어오는 순간 홈-자동일기완성 화면으로 넘어가는 것 구현
    }

}