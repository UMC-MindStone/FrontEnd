package com.example.mindstone.ui.home.emotion

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
import com.example.mindstone.databinding.FragmentEmotionIntensityBinding
import com.example.mindstone.ui.home.emotion.viewmodel.EmotionModel

class EmotionIntensityFragment : Fragment() {

    private var _binding: FragmentEmotionIntensityBinding? = null
    private val binding get() = _binding!!

    private var isAfterAction = false
    private var selectedEmotion: String? = null

    private lateinit var viewModel: EmotionModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionIntensityBinding.inflate(inflater, container, false)
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

        // ViewModel을 Activity 범위에서 가져오기 (HomeFragment에서 설정한 데이터 사용)
        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)


        // `isAfterAction` 여부 확인
        isAfterAction = arguments?.getBoolean("isAfterAction", false) ?: false
        selectedEmotion = arguments?.getString("selectedEmotion")
        if (isAfterAction && selectedEmotion != null) {
            viewModel.setEmotionData(selectedEmotion!!, viewModel.getEmotionColor(selectedEmotion!!) ?: 0, true)
            viewModel.resetIntensity()
        }


        // 상태바 & 캐릭터 업데이트
        viewModel.emotionRatios.observe(viewLifecycleOwner) { updateStatusBar(it) }
        viewModel.recentEmotion.observe(viewLifecycleOwner) { updateCharacter(it) }


        // 감정에 맞는 질문 업데이트
        viewModel.emotion.observe(viewLifecycleOwner) { emotion ->
            binding.intensityStatusTv.text = getEmotionQuestion(emotion)
        }

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.intensityBubble.setColorFilter(
                ContextCompat.getColor(requireContext(), colorResId),
                PorterDuff.Mode.SRC_IN
            )
        }

        // 감정 정도 값 업데이트
        viewModel.intensity.observe(viewLifecycleOwner) { intensity ->
            binding.intensityTv.text = intensity.toString()
        }

        // '+' 버튼 클릭 시 강도 증가
        binding.intensityPlus.setOnClickListener { viewModel.increaseIntensity() }
        // '-' 버튼 클릭 시 강도 감소
        binding.intensityMinus.setOnClickListener { viewModel.decreaseIntensity() }

        // '취소' 버튼 클릭 시 이전 화면으로 돌아감
        binding.intensityCancel.setOnClickListener {
            viewModel.resetIntensity() // 감정 강도 초기화
            requireActivity().supportFragmentManager.popBackStack()
        }

        // '확인' 버튼 클릭 시 -> Intensity2 으로 넘어감
        binding.intensityConfirm.setOnClickListener {
            viewModel.setIntensity(viewModel.intensity.value ?: 10) // 감정 강도를 ViewModel에 저장
            navigateToIntensity2()
        }
    }


//    // 새로운 감정 선택 시 감정 강도 초기화 (감정 비율과 최근 감정은 유지)
//    private fun resetEmotionIntensity() {
//        viewModel.resetIntensity() // 감정 강도만 초기화 (기존 데이터 유지)
//    }

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

    private fun navigateToIntensity2() {
        val fragment = EmotionIntensityFragment2().apply {
            arguments = Bundle().apply {
                putBoolean("isAfterAction", isAfterAction)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    // 감정에 맞는 질문을 반환하는 함수
    private fun getEmotionQuestion(emotion: String?): String {
        return when (emotion) {
            "행복" -> "얼마나 행복한가요?"
            "설렘" -> "얼마나 설레나요?"
            "기쁨" -> "얼마나 기쁜가요?"
            "평온" -> "얼마나 평온한가요?"
            "화남" -> "얼마나 화나나요?"
            "우울" -> "얼마나 우울한가요?"
            "슬픔" -> "얼마나 슬픈가요?"
            else -> "감정을 선택해주세요."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}