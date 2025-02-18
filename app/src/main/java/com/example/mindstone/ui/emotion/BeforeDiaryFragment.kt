package com.example.mindstone.ui.emotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import com.example.mindstone.MonthDayPickerFragment
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentBeforeDiaryBinding
import com.example.mindstone.ui.home.diary.CalendarToDiaryFragment
import com.example.mindstone.ui.home.diary.DiaryLoadingFragment
import com.example.mindstone.ui.home.diary.DiaryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class BeforeDiaryFragment : Fragment() {
    private var _binding : FragmentBeforeDiaryBinding? = null
    private val binding get() = _binding!!

    private var isRecord: Boolean = false
    private var date : String = "2025-01-01"
    private var currentYear: Int = 2025
    private var currentMonth: Int = 1
    private var currentDay: Int = 1

    private val diaryViewModel : DiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString("date", "2025-01-01") // 기본값 설정
            isRecord = it.getBoolean("isRecord", false)
        }
        val dateParts = date.split("-")
        if (dateParts.size == 3) {
            currentYear = dateParts[0].toIntOrNull() ?: 2025
            currentMonth = dateParts[1].toIntOrNull() ?: 1
            currentDay = dateParts[2].toIntOrNull() ?: 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeforeDiaryBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initButtonState()
        initClicker()
        updateDateUI()

    }
    private fun initClicker(){
        binding.beforeDateSelectIv.setOnClickListener {
            showDateSelector()
        }

        binding.beforeWriteBtn.setOnClickListener {
            val fragment = CalendarToDiaryFragment().apply{
                arguments = Bundle().apply {
                    putString("date", date)
                    putBoolean("isRecord", isRecord)
                }
            }

            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }

        binding.beforeAutocreateAbleBtn.setOnClickListener {
            val fragment = DiaryLoadingFragment().apply{
                arguments = Bundle().apply {
                    putString("date", date)
                    putString("fragment", "loading")
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()

        }
    }

    private fun initButtonState(){
        if(isRecord){
            binding.beforeAutocreateAbleBtn.visibility = View.VISIBLE
            binding.beforeAutocreateDisabledBtn.visibility = View.GONE
        }else{
            binding.beforeAutocreateDisabledBtn.visibility = View.VISIBLE
            binding.beforeAutocreateAbleBtn.visibility = View.GONE
        }
    }

    private fun showDateSelector(){
        val dialog = MonthDayPickerFragment()
        dialog.onDateSelected = { selectedMonth , selectedDay ->
            currentMonth = selectedMonth
            currentDay = selectedDay

            val date = LocalDate.of( currentYear, currentMonth, currentDay)
            // 요일 가져오기
            val dayOfWeek = date.dayOfWeek
            // 요일을 문자열로 변환 (한글 출력 가능)
            val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

            binding.beforeDateTv.text = "${currentMonth}월 ${currentDay}일 $dayName"

            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            // 날짜 기준 일기 요청
            diaryViewModel.fetchDiary(formattedDate)


        }
        dialog.show(parentFragmentManager, "MonthDayPickerFragment")

    }
    private fun updateDateUI() {
        val date = LocalDate.of(currentYear, currentMonth, currentDay)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        binding.beforeDateTv.text = "${currentMonth}월 ${currentDay}일 $dayOfWeek"
    }

}