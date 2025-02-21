package com.example.mindstone.ui.habit

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mindstone.ColorPickerFragment
import com.example.mindstone.R
import com.example.mindstone.TimePickerDialogFragment
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.HabitCalendarService
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.databinding.FragmentHabitCheckBinding
import com.example.mindstone.databinding.FrameHabitCheckBinding
import com.example.mindstone.domain.entity.HabitHistory
import com.example.mindstone.domain.entity.HabitHistoryPatch
import com.example.mindstone.domain.entity.HabitHistoryPost
import com.example.mindstone.domain.entity.HabitHistoryResponse
import com.example.mindstone.domain.entity.HabitTotal
import com.example.mindstone.domain.entity.NewHabitHistoryPatch
import com.example.mindstone.domain.entity.NewHabitHistoryTimePost
import com.example.mindstone.ui.habit.viewmodel.HabitCalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class HabitCheckFragment : Fragment() {
    val token = PreferenceManager.getAccessToken() ?: ""

    private var selectedYear: Int? = null
    private var selectedMonth: Int? = null
    private var selectedDay: Int? = null
    private var selectedDayOfWeek: String? = null
    private var isEditing = false
    private var numOfFrame = 0

    private var editTextJob: Job? = null

    private var habitTotal: List<HabitTotal>? = null

    private val viewModel: HabitCalendarViewModel by viewModels() //뷰모델

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

        // 습관 정보
        viewModel.fetchTotalHabit()
        observeViewModel()


        viewModel.habitCheckData.observe(viewLifecycleOwner) { habitHistoryList ->
            updateDateView(habitHistoryList)
        }

        // 초기 날짜 세팅
        viewModel.fetchCheckHabit(formattedDate())

        // 날짜 변경 버튼 클릭 처리
        binding.habitCheckLeftIv.setOnClickListener {
            binding.habitCheckContainerLl.removeAllViews()
            changeDate(-1)  // 하루 전으로 변경
            viewModel.fetchCheckHabit(formattedDate())
        }

        binding.habitCheckRightIv.setOnClickListener {
            binding.habitCheckContainerLl.removeAllViews()
            changeDate(1)  // 하루 후로 변경
            viewModel.fetchCheckHabit(formattedDate())
        }

        binding.habitCheckCloseIv.setOnClickListener {
            closeFragment()
        }

        binding.habitCheckEditTv.setOnClickListener {
            toggleEditMode()
        }

        binding.habitCheckReportBtnIv.setOnClickListener {
            Log.d("HabitCheck", "Report button clicked")

            val index = habitCheckContainerLL.childCount
            if (index < (habitTotal?.size ?: 0)) {
                val dialog = HabitPickerFragment { habitId, selectedHabit ->
                    // ✅ 확인 버튼을 눌렀을 때만 실행됨
                    binding.habitCheckStoneIv.visibility = View.GONE
                    binding.habitCheckNoHabitIv.visibility = View.GONE
                    Log.d("HabitId", "${habitId}")

                    val dateT = "${selectedYear}-${String.format("%02d", selectedMonth)}-${String.format("%02d", selectedDay)}"


                    val habitHistory = HabitHistoryPost(
                        habitId = habitId,
                        date = dateT,
                        comment = "한줄 소감",
                        startTime = null,
                        endTime = null,
                        habitColor = null
                    )
                    viewModel.postCheckHabit(habitHistory)

                    viewModel.habitHistoryId.observe(viewLifecycleOwner, Observer { habitHistoryId ->
                        habitHistoryId?.let {
                            createHabitCheckView(index, selectedHabit, it)  // habitHistoryId 전달
                        }
                    })
                }.apply {
                    onDismissListener = {
                        // ✅ Picker가 닫혔지만 선택되지 않은 경우 → 아무 동작도 하지 않음
                        Log.d("HabitPicker", "Picker 닫힘 (프레임 생성 안 함)")
                    }
                }

                dialog.show(parentFragmentManager, "HabitPickerDialog")
            }
        }
        return binding.root
    }

    private fun observeViewModel() {
        // ✅ LiveData 감지하여 UI 업데이트
        viewModel.habitTotalData.observe(viewLifecycleOwner) { data ->
            Log.d("HabitCheck", "dataLoadSuccess: $data")
            if (data != null) {
                Log.d("HabitCheck", "dataLoadFailed: $data")
                habitTotal = data.result
            }
        }

        viewModel.errorMessageTotal.observe(viewLifecycleOwner) { error ->
            Log.e("HabitCheck", "에러 발생: $error")
            Toast.makeText(requireContext(), "데이터 불러오기 실패", Toast.LENGTH_SHORT).show()
        }
    }


    private fun formattedDate(): String {
        val year = selectedYear ?: 2025
        val month = selectedMonth ?: 1
        val day = selectedDay ?: 1

        val date = LocalDate.of(year, month, day)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(formatter)
    }


    // 날짜 업데이트 함수
    private fun updateDateView(habitHistoryResponse: List<HabitHistory>) {
        binding.habitCheckDateTv.text = "${selectedMonth}월 ${selectedDay}일 ${selectedDayOfWeek}"

        numOfFrame = habitHistoryResponse?.size ?: 0

        if(habitHistoryResponse.isEmpty()){
            binding.habitCheckStoneIv.visibility = View.VISIBLE
            binding.habitCheckNoHabitIv.visibility = View.VISIBLE
        } else {
            binding.habitCheckStoneIv.visibility = View.GONE
            binding.habitCheckNoHabitIv.visibility = View.GONE
        }
        for (i in 0 until numOfFrame) {
            // 이미 뷰가 추가되어 있다면, 해당 뷰를 업데이트하도록 변경
            if (habitCheckContainerLL.childCount > i) {
                val frameLayoutBinding = FrameHabitCheckBinding.bind(habitCheckContainerLL.getChildAt(i))
                updateHabitCheckView(frameLayoutBinding, i)
            } else {
                createHabitCheckViewAPI(i, habitHistoryResponse[i])
            }
        }
    }

    private fun updateDateViews() {
        binding.habitCheckDateTv.text = "${selectedMonth}월 ${selectedDay}일 ${selectedDayOfWeek}"

        numOfFrame = habitCheckContainerLL.childCount

        if(numOfFrame == 0){
            binding.habitCheckStoneIv.visibility = View.VISIBLE
            binding.habitCheckNoHabitIv.visibility = View.VISIBLE
        } else {
            binding.habitCheckStoneIv.visibility = View.GONE
            binding.habitCheckNoHabitIv.visibility = View.GONE
        }
        for (i in 0 until numOfFrame) {
            val frameLayoutBinding = FrameHabitCheckBinding.bind(habitCheckContainerLL.getChildAt(i))
            updateHabitCheckView(frameLayoutBinding, i)
        }
    }

    private fun createHabitCheckView(index: Int, selectedHabit: String, habitId: Long) {
        val context = requireContext()
        val frameLayoutBinding = FrameHabitCheckBinding.inflate(LayoutInflater.from(context))

        // ✅ 선택된 습관 이름을 표시
        frameLayoutBinding.frameHabitCheckHabitTv.text = selectedHabit
        frameLayoutBinding.frameHabitCheckIdContainerTv.text = "$habitId"

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
        setupHabitPickerClickListener(frameLayoutBinding)

        habitCheckContainerLL.addView(frameLayoutBinding.root)
        habitCheckContainerLL.requestLayout()

        //setupHabitPicker(frameLayoutBinding)
    }

    private fun createHabitCheckViewAPI(index: Int, habitHistory: HabitHistory) {
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

        val habitMap = habitTotal?.associate { it.habitId to it.title }

        Log.d("GGGG","$habitMap")
        val title = habitMap?.get(habitHistory.habitHistoryId)
        Log.d("GGGG","${habitHistory.habitId},${habitHistory.habitHistoryId}")

        frameLayoutBinding.frameHabitCheckHabitTv.text = title
        Log.d("GGGG","$title")

        val colorIconMap = mapOf(
            "PURPLE" to R.drawable.ic_depression,
            "ORANGE" to R.drawable.ic_angry,
            "BLUE" to R.drawable.ic_sad,
            "GRAY" to R.drawable.ic_calm_charac,
            "GREEN" to R.drawable.ic_joy,
            "YELLOW" to R.drawable.ic_happy,
            "PINK" to R.drawable.ic_romance
        )

// null이나 매칭되지 않는 값에 대한 기본 아이콘
        val defaultIcon = R.drawable.btn_nothing_normal

        val apiColor: String? = habitHistory.habitColor
        val iconResId = colorIconMap[apiColor] ?: defaultIcon

        frameLayoutBinding.frameHabitCheckIconIv.setImageResource(iconResId)

        val commentText = if (habitHistory.comment?.isNotEmpty() == true) habitHistory.comment else "하지 않았어요"
        editText.setText(commentText)

        if(commentText != "한줄 소감"){
            editText.setTextColor(Color.BLACK)
        }



        val imageView = frameLayoutBinding.frameHabitCheckBubbleIv
        val timeTextViews: List<TextView> = listOf(
            frameLayoutBinding.frameHabitCheckTime1Tv,
            frameLayoutBinding.frameHabitCheckTime2Tv,
            frameLayoutBinding.frameHabitCheckTime3Tv,
            frameLayoutBinding.frameHabitCheckTime4Tv
        )
        val timeId: List<TextView> = listOf(
            frameLayoutBinding.timeText1,
            frameLayoutBinding.timeText2,
            frameLayoutBinding.timeText3,
            frameLayoutBinding.timeText4
        )

        timeTextViews.forEach { it.visibility = View.GONE }

        // 각 프레임에 대한 고유한 timeNum을 관리
        var timeNum = frameTimeNums.getOrDefault(index, 1)
        for (j in 0 until (habitHistory.executions?.size ?: 0)) {
            val startTimeRaw = habitHistory.executions?.getOrNull(j)?.startTime
            val endTimeRaw = habitHistory.executions?.getOrNull(j)?.endTime

            val startTimeFormatted = startTimeRaw?.substring(11, 16) ?: "00:00"
            val endTimeFormatted = endTimeRaw?.substring(11, 16) ?: "00:00"

            timeId[j].text = habitHistory.executions?.getOrNull(j)?.id?.toString() ?: ""
            timeTextViews[j].text = "$startTimeFormatted-$endTimeFormatted"
            timeTextViews[j].visibility = View.VISIBLE
            timeTextViews[j].setTextColor(Color.BLACK)
        }

// timeNum을 executions의 크기로 설정
        timeNum = (habitHistory.executions?.size ?: 0) + 1
        frameTimeNums[index] = timeNum
        Log.d("API","$timeNum")

        //habitHistoryId
        frameLayoutBinding.frameHabitCheckIdContainerTv.text = habitHistory.habitHistoryId.toString()


        if (isEditing) {
            setEditMode(timeNum, timeTextViews, index, frameLayoutBinding)
        } else {
            setNonEditMode(timeNum, timeTextViews, index)
        }

        setupEditTextListener(editText, imageView, frameLayoutBinding)
        setupIconClickListener(frameLayoutBinding)
        setupHabitPickerClickListener(frameLayoutBinding)

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
        val timeId = listOf(
            frameLayoutBinding.timeText1,
            frameLayoutBinding.timeText2,
            frameLayoutBinding.timeText3,
            frameLayoutBinding.timeText4
        )

        for (j in 0 until timeNum) {
            timeTextViews[j].visibility = View.VISIBLE
            if(j+1 == timeNum) {
                timeTextViews[j].setOnClickListener { textView ->
                    showTimePickerDialog(frameLayoutBinding) { starttime, endtime ->
                        val selectedTime = "${starttime}:${endtime}"

                        (textView as TextView).text = selectedTime
                        val habitHabit = NewHabitHistoryTimePost(
                            habitHistoryId = frameLayoutBinding.frameHabitCheckIdContainerTv.text.toString().toLongOrNull() ?: 0L,
                            startTime = createTime(starttime),
                            endTime = createTime(endtime)
                        )
                        viewModel.postHabitTime(habitHabit) { result ->
                            if (result != null) {
                                Log.d("HabitPost", "Post 성공! ID: $result")
                                // 받아온 habitHistoryId를 UI에 반영
                                timeId[j].text = result.toString()
                            } else {
                                Log.e("HabitPost", "Post 실패!")
                            }
                        }
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
            } else {
                timeTextViews[j].setOnClickListener { textView ->
                    showTimePickerDialog(frameLayoutBinding) { starttime, endtime ->
                        val selectedTime = "${starttime}:${endtime}"

                        val habitHabit =  NewHabitHistoryPatch(
                            habitHistoryId = frameLayoutBinding.frameHabitCheckIdContainerTv.text.toString().toLongOrNull() ?: 0L,
                            excutionId = timeId[j].text.toString().toLongOrNull() ?: 0L,
                            startTime = createTime(starttime),
                            endTime = createTime(endtime),
                            habitColor = null,
                            comment = null
                        )

                        viewModel.patchCheckHabit(habitHabit)

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

            override fun afterTextChanged(editable: Editable) {
                editTextJob?.cancel() // 기존 작업 취소 (Debounce)

                editTextJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500) // 0.5초 대기 (Debounce)

                    val tComment = editText.text.toString()
                    val tHabitId: Long? = frameLayoutBinding.frameHabitCheckIdContainerTv.text.toString().toLongOrNull()

                    val habitHistory = NewHabitHistoryPatch(
                        habitHistoryId = tHabitId,
                        excutionId = null,
                        startTime = null,
                        endTime = null,
                        habitColor = null,
                        comment = tComment
                    )

                    viewModel.patchCheckHabit(habitHistory)
                }
            }
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
                                4 -> R.drawable.ic_calm_charac
                                5 -> R.drawable.ic_joy
                                6 -> R.drawable.ic_happy
                                7 -> R.drawable.ic_romance
                                else -> R.drawable.btn_nothing_normal
                            }
                            frameLayoutBinding.frameHabitCheckIconIv.setImageResource(iconRes)

                            // 🎨 colorIndex → habitColor 변환
                            val habitColor = when (it) {
                                1 -> "PURPLE"
                                2 -> "ORANGE"
                                3 -> "BLUE"
                                4 -> "GRAY"
                                5 -> "GREEN"
                                6 -> "YELLOW"
                                7 -> "PINK"
                                else -> null
                            }
                            val tHabitId: Long? = frameLayoutBinding.frameHabitCheckIdContainerTv.text.toString().toLongOrNull()

                            // ✅ PATCH 요청 전송
                            val habitHistory = NewHabitHistoryPatch(
                                habitHistoryId = tHabitId,
                                excutionId = null,
                                startTime = null,
                                endTime = null,
                                habitColor = habitColor,
                                comment = null
                            )
                            viewModel.patchCheckHabit(habitHistory)
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


    private fun setupHabitPickerClickListener(frameLayoutBinding: FrameHabitCheckBinding) {
        frameLayoutBinding.frameHabitCheckHabitTv.setOnClickListener {
            if (isEditing) {
                val dialog = HabitPickerFragment { habitId, selectedHabit ->
                    // 🔹 선택된 습관을 텍스트뷰에 반영
                    frameLayoutBinding.frameHabitCheckHabitTv.text = selectedHabit
                }.apply {
                    // 🔹 다이얼로그가 열릴 때 테두리 강조
                    onDismissListener = {
                        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
                    }
                }

                // 🔹 다이얼로그 표시 전에 테두리 강조
                frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)
                dialog.show(parentFragmentManager, "HabitPickerDialog")
            }
        }
    }

    private fun setupHabitPicker(frameLayoutBinding: FrameHabitCheckBinding) {
        val dialog = HabitPickerFragment { habitId, selectedHabit ->
            Log.d("HabitPicker", "선택된 습관 ID: $habitId, 이름: $selectedHabit")
            frameLayoutBinding.frameHabitCheckHabitTv.text = selectedHabit
        }.apply {
            onDismissListener = {
                frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
            }
        }
        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)
        dialog.show(parentFragmentManager, "HabitPickerDialog")
    }



    private fun showTimePickerDialog(frameLayoutBinding: FrameHabitCheckBinding, onTimeSelected: (String, String) -> Unit) {
        frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_red)

        val dialog = TimePickerDialogFragment { startTime, endTime  ->
            onTimeSelected(startTime,endTime)
            frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
        }

        dialog.onDismissListener = {
            frameLayoutBinding.root.setBackgroundResource(R.drawable.background_radius_gray)
        }
        dialog.show(parentFragmentManager, "TimePickerDialog")
    }

    fun createTime(startTime: String): String {
        // startTime은 항상 "00:00" 형식이라고 가정하고, 이를 기반으로 시간을 설정합니다.

        val year = selectedYear ?: 0
        val month = selectedMonth ?: 1
        val day = selectedDay ?: 0

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)  // 0부터 시작하는 월을 조정
            set(Calendar.DAY_OF_MONTH, day)

            // "00:00"에서 시간과 분을 설정
            val timeParts = startTime.split(":")
            set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())  // 24시간 형식
            set(Calendar.MINUTE, timeParts[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // SimpleDateFormat을 이용해 원하는 포맷으로 변환합니다.
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")  // UTC 시간대로 설정

        return dateFormat.format(calendar.time)  // 포맷에 맞게 변환된 시간 리턴
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

        updateDateViews()
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
        updateDateViews()
    }

}