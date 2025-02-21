package com.example.mindstone.ui.emotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentMonthStatBinding

class MonthStatFragment : Fragment() {
    private lateinit var binding: FragmentMonthStatBinding

    //우선 첫째 장 String으로 넘겨주기 때문에 변경하였음.
    companion object {
        private const val ARG_MONTH = "month"

        // Month 정보를 전달받아 프래그먼트를 생성하는 메서드
        fun newInstance(string: String): MonthStatFragment {
            val fragment = MonthStatFragment()
            val args = Bundle()
            args.putString(ARG_MONTH, string)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthStatBinding.inflate(inflater, container, false)

        // 전달받은 month 값 가져오기
        val data = arguments?.getString(ARG_MONTH) ?: "값을 받아오는 중이에요" // 기본값: 1월

        // 동적으로 텍스트와 이미지를 설정
        val imageResId = R.drawable.ic_happy // 예: 감정에 따라 이미지를 변경할 수 있음

        binding.monthStatMonthTv.text = data
        binding.monthStatIconIv.setImageResource(imageResId)

        return binding.root
    }
}
