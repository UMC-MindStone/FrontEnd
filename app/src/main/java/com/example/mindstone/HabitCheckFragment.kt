package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentHabitCheckBinding
import com.example.mindstone.databinding.FrameHabitCheckBinding

class HabitCheckFragment : Fragment() {
    private var selectedYear: Int? = null
    private var selectedMonth: Int? = null
    private var selectedDay: Int? = null
    private var selectedDayOfWeek: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전달받은 데이터 가져오기
        arguments?.let {
            selectedYear = it.getInt("year")
            selectedMonth = it.getInt("month")
            selectedDay = it.getInt("day")
            selectedDayOfWeek = it.getString("dayOfWeek")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitCheckBinding.inflate(inflater, container, false)
        binding.habitCheckDateTv.text = "${selectedMonth}월 ${selectedDay}일 ${selectedDayOfWeek}"

        val count = 3

        val habitCheckContainerLL = binding.habitCheckContainerLl
        val context = requireContext()

        for (i in 0 until count) {
            val frameLayoutBinding = FrameHabitCheckBinding.inflate(LayoutInflater.from(context))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                380
                //LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // 각 FrameLayout 사이에 top margin 16dp 설정
            layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.padding_top)

            // LayoutParams를 root에 적용
            frameLayoutBinding.root.layoutParams = layoutParams

            // LinearLayout에 FrameLayout 추가
            habitCheckContainerLL.addView(frameLayoutBinding.root)
        }



        return binding.root
    }
}
