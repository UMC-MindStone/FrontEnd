package com.example.mindstone

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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

        binding.habitCheckCloseIv.setOnClickListener {

        }

        binding.habitCheckEditTv.setOnClickListener {
            if(binding.habitCheckEditTv.text == "편집"){
                binding.habitCheckEditTv.text = "완료"
            } else {
                binding.habitCheckEditTv.text = "편집"
            }
        }

        binding.habitCheckCloseIv.setOnClickListener {
            val fragment = HabitCalendarFragment()

            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.habit_check_container_fl, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
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

            val editText = frameLayoutBinding.frameHabitCheckCustomEt  // EditText
            val imageView = frameLayoutBinding.frameHabitCheckBubbleIv    // ImageView

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, after: Int) {
                    val maxWidth = resources.getDimensionPixelSize(R.dimen.max_image_width)  // 이미지 뷰 최대 너비
                    val textLength = charSequence.length
                    val maxCharacters = maxWidth / 20  // 글자 하나당 10dp로 가정하여 최대 글자 수 계산

                    if (textLength > maxCharacters) {
                        // 텍스트가 초과되었을 경우 자르기
                        val truncatedText = charSequence.substring(0, maxCharacters)
                        editText.setText(truncatedText)
                        editText.setSelection(truncatedText.length)  // 커서를 맨 뒤로 이동
                    }

                    // 텍스트에 맞춰 이미지 뷰 너비 조정
                    val newWidth = calculateWidthBasedOnTextLength(charSequence.length)
                    val params = imageView.layoutParams
                    params.width = newWidth.coerceAtMost(maxWidth)  // 최대 너비 제한 적용
                    imageView.layoutParams = params
                }

                override fun afterTextChanged(editable: Editable) {}
            })





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

    fun calculateWidthBasedOnTextLength(textLength: Int): Int {
        val minWidth = resources.getDimensionPixelSize(R.dimen.min_image_width)  // 최소 너비 (예: 100dp)
        val maxWidth = resources.getDimensionPixelSize(R.dimen.max_image_width)  // 최대 너비 (예: 300dp)

        // 텍스트 길이에 따라 너비 계산 (여기서는 길이가 증가할수록 너비가 비례적으로 늘어남)
        val calculatedWidth = minWidth + (textLength * 10)  // 텍스트 길이에 비례하여 너비 증가 (10px씩 증가)

        // 계산된 너비가 최소, 최대 범위 내에 있도록 조정
        return calculatedWidth.coerceIn(minWidth, maxWidth)
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
