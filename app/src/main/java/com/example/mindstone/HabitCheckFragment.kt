package com.example.mindstone

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentHabitCheckBinding
import com.example.mindstone.databinding.FrameHabitCheckBinding
import java.util.*

class HabitCheckFragment : Fragment() {
    private var selectedYear: Int? = null
    private var selectedMonth: Int? = null
    private var selectedDay: Int? = null
    private var selectedDayOfWeek: String? = null
    private var isEditing = false

    private lateinit var habitCheckContainerLL: LinearLayout
    private lateinit var binding: FragmentHabitCheckBinding

    // 각 프레임의 timeNum을 관리할 Map
    private val frameTimeNums = mutableMapOf<Int, Int>()

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
            closeFragment()
        }

        binding.habitCheckEditTv.setOnClickListener {
            toggleEditMode()
        }
        return binding.root
    }



    // 날짜 업데이트 함수
    private fun updateDateView() {
        binding.habitCheckDateTv.text = "${selectedMonth}월 ${selectedDay}일 ${selectedDayOfWeek}"

        // 기존 뷰들을 초기화하지 않고 유지
        val count = 3 // 예시: 3개의 FrameLayout 추가
        for (i in 0 until count) {
            // 이미 뷰가 추가되어 있다면, 해당 뷰를 업데이트하도록 변경
            if (habitCheckContainerLL.childCount > i) {
                val frameLayoutBinding = FrameHabitCheckBinding.bind(habitCheckContainerLL.getChildAt(i))
                updateHabitCheckView(frameLayoutBinding, i)
            } else {
                createHabitCheckView(i)
            }
        }
    }

    private fun createHabitCheckView(index: Int) {
        val context = requireContext()
        val frameLayoutBinding = FrameHabitCheckBinding.inflate(LayoutInflater.from(context))

        // 각 FrameLayout에 LayoutParams 설정
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            380
        )
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.padding_top)
        frameLayoutBinding.root.layoutParams = layoutParams

        val editText = frameLayoutBinding.frameHabitCheckCustomEt
        val imageView = frameLayoutBinding.frameHabitCheckBubbleIv
        val timeTextViews: List<TextView> = listOf(
            frameLayoutBinding.frameHabitCheckTime1Tv,
            frameLayoutBinding.frameHabitCheckTime2Tv,
            frameLayoutBinding.frameHabitCheckTime3Tv,
            frameLayoutBinding.frameHabitCheckTime4Tv
        )

        timeTextViews.forEach { it.visibility = View.GONE }

        // 각 프레임에 대한 고유한 timeNum을 관리
        var timeNum = frameTimeNums.getOrDefault(index, 1)
        if (isEditing) {
            setEditMode(timeNum, timeTextViews, index, frameLayoutBinding)
        } else {
            setNonEditMode(timeNum, timeTextViews, index)
        }

        setupEditTextListener(editText, imageView, frameLayoutBinding)
        setupIconClickListener(frameLayoutBinding)

        habitCheckContainerLL.addView(frameLayoutBinding.root)
    }

    private fun updateHabitCheckView(frameLayoutBinding: FrameHabitCheckBinding, index: Int) {
        val timeTextViews: List<TextView> = listOf(
            frameLayoutBinding.frameHabitCheckTime1Tv,
            frameLayoutBinding.frameHabitCheckTime2Tv,
            frameLayoutBinding.frameHabitCheckTime3Tv,
            frameLayoutBinding.frameHabitCheckTime4Tv
        )
        val editText = frameLayoutBinding.frameHabitCheckCustomEt
        val imageView = frameLayoutBinding.frameHabitCheckBubbleIv
        setupEditTextListener(editText, imageView, frameLayoutBinding)
        // 각 프레임에 대한 고유한 timeNum을 관리
        var timeNum = frameTimeNums.getOrDefault(index, 1)
        if (isEditing) {
            setEditMode(timeNum, timeTextViews, index, frameLayoutBinding)
        } else {
            setNonEditMode(timeNum, timeTextViews, index)
        }
    }

    private fun setEditMode(timeNum: Int, timeTextViews: List<TextView>, index: Int, frameLayoutBinding: FrameHabitCheckBinding) {
        for (j in 0 until timeNum) {
            timeTextViews[j].visibility = View.VISIBLE
            timeTextViews[j].setOnClickListener { textView ->
                showTimePickerDialog(frameLayoutBinding) { selectedTime ->
                    (textView as TextView).text = selectedTime
                    timeTextViews[j].setTextColor(Color.BLACK)
                    frameLayoutBinding.frameHabitCheckHabitTv.setTextColor(Color.BLACK)
                    frameLayoutBinding.frameHabitCheckCustomEt.setTextColor(Color.BLACK)
                    // timeNum을 증가시키되 최대값은 4로 제한
                    var updatedTimeNum = timeNum
                    if (timeNum < (j + 2).coerceAtMost(5)) {
                        updatedTimeNum = (j + 2).coerceAtMost(5)
                    }

                    // 각 프레임에 대해 timeNum을 업데이트
                    frameTimeNums[index] = updatedTimeNum

                    if (updatedTimeNum <= 4) {
                        setEditMode(updatedTimeNum, timeTextViews, index, frameLayoutBinding)
                    }
                }
            }
        }
    }

    private fun setNonEditMode(timeNum: Int, timeTextViews: List<TextView>, index: Int) {
        // 설정된 시간까지만 보이도록 처리
        for (j in 0 until timeNum-1) {
            timeTextViews[j].visibility = View.VISIBLE
        }

        // 설정되지 않은 시간들은 숨기기
        for (j in timeNum-1 until timeTextViews.size) {
            timeTextViews[j].visibility = View.GONE
        }
    }

    private fun setupEditTextListener(editText: EditText, imageView: ImageView, frameLayoutBinding: FrameHabitCheckBinding) {
        editText.isEnabled = isEditing

        // EditText에 포커스가 있는 동안 배경을 변경
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // EditText에 포커스가 있으면 배경 변경
                frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)
            } else {
                // 포커스가 없으면 배경 원상복구
                frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, after: Int) {
                if (isEditing) {
                    handleTextLength(charSequence, editText, imageView)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }


    private fun handleTextLength(charSequence: CharSequence, editText: EditText, imageView: ImageView) {
        val maxWidth = resources.getDimensionPixelSize(R.dimen.max_image_width)
        val textLength = charSequence.length
        val maxCharacters = maxWidth / 20

        if (textLength > maxCharacters) {
            val truncatedText = charSequence.substring(0, maxCharacters)
            editText.setText(truncatedText)
            editText.setSelection(truncatedText.length)
        }

        val newWidth = calculateWidthBasedOnTextLength(charSequence.length)
        val params = imageView.layoutParams
        params.width = newWidth.coerceAtMost(maxWidth)
        imageView.layoutParams = params
    }

    private fun setupIconClickListener(frameLayoutBinding: FrameHabitCheckBinding) {
        frameLayoutBinding.frameHabitCheckIconIv.setOnClickListener {
            if (isEditing) {
                val dialog = ColorPickerFragment().apply {
                    onColorSelected = { colorIndex ->
                        colorIndex?.let {
                            val iconRes = when (it) {
                                1 -> R.drawable.ic_depression
                                2 -> R.drawable.ic_angry
                                3 -> R.drawable.ic_sad
                                4 -> R.drawable.ic_calm
                                5 -> R.drawable.ic_joy
                                6 -> R.drawable.ic_happy
                                7 -> R.drawable.ic_romance
                                else -> R.drawable.btn_nothing_normal
                            }
                            frameLayoutBinding.frameHabitCheckIconIv.setImageResource(iconRes)
                        }
                    }

                    // 다이얼로그가 열릴 때 UI 변경 (테두리 강조)
                    onDialogOpened = {
                        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)
                    }

                    // 다이얼로그가 닫힐 때 UI 복구 (테두리 원래대로)
                    onDialogClosed = {
                        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
                    }
                }

                dialog.show(parentFragmentManager, "ColorPickerFragment")
            }
        }
    }


    private fun showTimePickerDialog(frameLayoutBinding: FrameHabitCheckBinding, onTimeSelected: (String) -> Unit) {
        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)

        val dialog = TimePickerDialogFragment { selectedTime ->
            onTimeSelected(selectedTime)

            frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
        }

        dialog.onDismissListener = {
            frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
        }
        dialog.show(parentFragmentManager, "TimePickerDialog")
    }

    private fun calculateWidthBasedOnTextLength(textLength: Int): Int {
        val minWidth = resources.getDimensionPixelSize(R.dimen.min_image_width)
        val maxWidth = resources.getDimensionPixelSize(R.dimen.max_image_width)

        val calculatedWidth = minWidth + (textLength * 10)
        return calculatedWidth.coerceIn(minWidth, maxWidth)
    }

    private fun changeDate(dayOffset: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(selectedYear ?: 2025, selectedMonth?.minus(1) ?: 0, selectedDay ?: 1)
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset)

        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH) + 1
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        selectedDayOfWeek = getDayOfWeekString(dayOfWeek)

        updateDateView()
    }

    private fun getDayOfWeekString(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }
    }

    private fun closeFragment() {
        val fragment = HabitCalendarFragment()
        val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.habit_check_container_fl, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun toggleEditMode() {
        isEditing = !isEditing
        binding.habitCheckEditTv.text = if (isEditing) "완료" else "편집"
        updateDateView()
    }
}
