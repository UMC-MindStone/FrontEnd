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
        private const val ARG_DATA = "data"

        // Month 정보를 전달받아 프래그먼트를 생성하는 메서드
        fun newInstance(month: Int, data:String): MonthSummaryFragment {
            val fragment = MonthSummaryFragment()
            val args = Bundle()
            args.putInt(ARG_MONTH, month)
            args.putString(ARG_DATA, data)
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

        val data = arguments?.getString(ARG_DATA) ?: "로딩중입니다."
        val text1 = "${month}월 리포트 요약"
        val text2 = data

        binding.monthSummaryMonthTv.text = text1
        binding.monthSummaryCompareTv.text = text2

        return binding.root
    }
}