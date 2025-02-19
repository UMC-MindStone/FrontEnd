package com.example.mindstone.ui.home.emotion.negative

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionActionTimeBinding
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionActionTimeFragment : Fragment() {

    private var _binding: FragmentEmotionActionTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel

    private var selectedAction: String? = null // 선택한 행동 저장
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionActionTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 시스템 바(상태바, 네비게이션바) 공간 자동 조정
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // viewModel 가져오기
        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)

        // 이전 Fragment에서 전달받은 선택한 행동 가져오기
        selectedAction = arguments?.getString("SELECTED_ACTION")

        // 행동을 time_question_tv에 적용
        selectedAction?.let { binding.timeQuestionTv.text = "$it 을(를) 했어요." }

        setupHourPicker()
        setupMinutePicker()

        // 상태바 업데이트 (감정 비율 기반)
        viewModel.emotionRatios.observe(viewLifecycleOwner) { updateStatusBar(it) }

        // 캐릭터 업데이트 (최근 감정 기반)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { updateCharacter(it) }

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.timeFl.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

//        // "확인" 버튼 클릭 시 -> 데이터 저장 + Fragment 이동
//        binding.timeConfirmTv.setOnClickListener {
//            val selectedHour = binding.timeHourNp.value
//            val selectedMinute = binding.timeMinuteNp.displayedValues[binding.timeMinuteNp.value].toInt()
//            // ✅ 사용자가 선택한 행동 시간 저장
//            saveStressDuration(formatDuration(selectedHour, selectedMinute))
//            // ViewModel에 저장
//            viewModel.setActivityTime(selectedHour, selectedMinute)
//            // 데이터 전달하면서 Fragment 이동
//            navigateToTime2(selectedHour, selectedMinute)
//        }

        // ✅ "확인" 버튼 클릭 시 데이터 저장 + Fragment 이동
        binding.timeConfirmTv.setOnClickListener {
            selectedHour = binding.timeHourNp.value
            selectedMinute = binding.timeMinuteNp.displayedValues[binding.timeMinuteNp.value].toInt()

            val duration = formatDuration(selectedHour, selectedMinute)
            saveStressDuration(duration) // ✅ 관리 행동 지속 시간 저장

            // ✅ ViewModel에 저장
            viewModel.setActivityTime(selectedHour, selectedMinute)

            // ✅ Fragment 이동
            navigateToTime2()
        }
    }

    // ✅ SharedPreferences에 사용자가 설정한 행동 시간 저장
    private fun saveStressDuration(duration: String) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("stress_duration", duration).apply()
    }

    // ✅ 시간 형식 변환 (예: "1시간 30분", "45분")
    private fun formatDuration(hour: Int, minute: Int): String {
        return when {
            hour > 0 && minute > 0 -> "${hour}시간 ${minute}분"
            hour > 0 -> "${hour}시간"
            else -> "${minute}분"
        }
    }


    // 상태바 업데이트 (감정 비율에 따른 색상 적용)
    private fun updateStatusBar(emotionRatios: Map<String, Float>) {
        val sortedRatios = viewModel.getSortedEmotionRatios()
        val sortedColors = sortedRatios.mapNotNull { (emotion, _) ->
            viewModel.getEmotionColor(emotion)?.let { ContextCompat.getColor(requireContext(), it) }
        }
        if (sortedColors.isNotEmpty()) {
            val dominantColor = sortedColors.first()
            binding.statusBar.setColorFilter(dominantColor, PorterDuff.Mode.SRC_IN)
        }
    }

    // 최근 감정 기반 캐릭터 변경
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion)
        binding.iconIv.setImageResource(characterResId)
    }


//    private fun navigateToTime2(selectedHour: Int, selectedMinute: Int) {
//        // ✅ 지속 시간을 SharedPreferences에 저장
//        saveStressDuration("$selectedHour 시간 $selectedMinute 분")
//
//        val fragment = EmotionActionTimeFragment2().apply {
//            arguments = Bundle().apply {
//                putInt("HOUR", selectedHour)
//                putInt("MINUTE", selectedMinute)
//                putString("SELECTED_ACTION", selectedAction)
//            }
//        }
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

    // ✅ Fragment 이동
    private fun navigateToTime2() {
        val fragment = EmotionActionTimeFragment2().apply {
            arguments = Bundle().apply {
                putInt("HOUR", selectedHour)
                putInt("MINUTE", selectedMinute)
                putString("SELECTED_ACTION", selectedAction)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setupHourPicker() {
        binding.timeHourNp.apply {
            minValue = 0
            maxValue = 24
            wrapSelectorWheel = false
        }
    }
    private fun setupMinutePicker() {
        val minuteValues = arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
        binding.timeMinuteNp.apply {
            minValue = 0
            maxValue = minuteValues.size - 1
            displayedValues = minuteValues
            wrapSelectorWheel = true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}