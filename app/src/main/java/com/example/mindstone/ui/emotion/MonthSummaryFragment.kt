package com.example.mindstone.ui.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentMonthSummaryBinding

class MonthSummaryFragment : Fragment() {
    lateinit var binding : FragmentMonthSummaryBinding

    companion object {
        private const val ARG_MONTH = "month"

        // Month 정보를 전달받아 프래그먼트를 생성하는 메서드
        fun newInstance(month: Int): MonthSummaryFragment {
            val fragment = MonthSummaryFragment()
            val args = Bundle()
            args.putInt(ARG_MONTH, month)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthSummaryBinding.inflate(inflater, container, false)
        val month = arguments?.getInt(ARG_MONTH) ?: 1 // 기본값: 1월

        val text1 = "${month}월 감정 요약"
        val text2 = "${month}월 초에 비해 ${month}월 말에 감정 관리가 잘 되었습니다."

        binding.monthSummaryMonthTv.text = text1
        binding.monthSummaryCompareTv.text = text2

        return binding.root
    }
}