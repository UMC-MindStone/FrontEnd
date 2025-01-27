package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentHabitCheckBinding
import com.example.mindstone.databinding.FrameHabitCheckBinding
import java.util.*

class HabitCheckFragment : Fragment() {
    private var selectedYear: Int? = null
    private var selectedMonth: Int? = null
    private var selectedDay: Int? = null
    private var selectedDayOfWeek: String? = null

    private lateinit var habitCheckContainerLL: LinearLayout
    private lateinit var binding: FragmentHabitCheckBinding

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
        binding = FragmentHabitCheckBinding.inflate(inflater, container, false)
        habitCheckContainerLL = binding.habitCheckContainerLl

        // 초기 날짜 세팅
        updateDateView()

        // 날짜 변경 버튼 클릭 처리
        binding.habitCheckLeftIv.setOnClickListener {
            changeDate(-1)  // 하루 전으로 변경
        }

        binding.habitCheckRightIv.setOnClickListener {
            changeDate(1)  // 하루 후로 변경
        }

        binding.habitCheckEditTv.setOnClickListener {
            if(binding.habitCheckEditTv.text == "편집"){
                binding.habitCheckEditTv.text = "완료"
            } else {
                binding.habitCheckEditTv.text = "편집"
            }
        }

        return binding.root
    }

    // 날짜 업데이트 함수
    private fun updateDateView() {
        binding.habitCheckDateTv.text = "${selectedMonth}월 ${selectedDay}일 ${selectedDayOfWeek}"

        // 기존의 FrameLayout 제거
        habitCheckContainerLL.removeAllViews()

        // 새로 FrameLayout 추가
        val count = 3 // 예시: 3개의 FrameLayout 추가
        val context = requireContext()

        for (i in 0 until count) {
            val frameLayoutBinding = FrameHabitCheckBinding.inflate(LayoutInflater.from(context))

            // 각 FrameLayout에 LayoutParams 설정
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                380
            )
            layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.padding_top)
            frameLayoutBinding.root.layoutParams = layoutParams

            frameLayoutBinding.frameHabitCheckIconIv.setOnClickListener {
                if (binding.habitCheckEditTv.text == "완료") {
                    val dialog = ColorPickerFragment()
                    dialog.onColorSelected = { colorIndex ->
                        colorIndex?.let {
                            when (it) {
                                1 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_depression)
                                2 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_angry)
                                3 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_sad)
                                4 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_calm)
                                5 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_joy)
                                6 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_happy)
                                7 -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.ic_romance)
                                else -> frameLayoutBinding.frameHabitCheckIconIv.setImageResource(R.drawable.btn_nothing_normal)
                            }
                        }
                    }
                    dialog.show(parentFragmentManager, "ColorPickerFragment")
                }
            }

            habitCheckContainerLL.addView(frameLayoutBinding.root)
        }
    }

    // 날짜 변경 함수
    private fun changeDate(dayOffset: Int) {
        // Calendar 객체를 사용하여 날짜 계산
        val calendar = Calendar.getInstance()

        // 현재 선택된 날짜로 설정
        calendar.set(selectedYear ?: 2025, selectedMonth?.minus(1) ?: 0, selectedDay ?: 1)

        // 날짜에 하루를 더하거나 빼기
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset)

        // 연도, 월, 날짜 업데이트
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH) + 1 // 0부터 시작하므로 1을 더해줌
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        // 요일 계산
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        selectedDayOfWeek = when (dayOfWeek) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }

        // UI 업데이트
        updateDateView()
    }


}
