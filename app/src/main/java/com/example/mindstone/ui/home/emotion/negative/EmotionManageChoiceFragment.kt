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
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionManageChoiceBinding
import com.example.mindstone.ui.home.HomeQuestionFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionManageChoiceFragment : Fragment() {

    private var _binding: FragmentEmotionManageChoiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionManageChoiceBinding.inflate(inflater, container, false)
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

        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)

        emotionStatusBar = binding.statusBar // ✅ EmotionStatusBar 연결

        // ✅ 감정 비율을 실시간으로 감지하여 상태바 업데이트
        viewModel.normalizedEmotionRatios.observe(viewLifecycleOwner) { normalizedRatios ->
            emotionStatusBar.updateEmotions(normalizedRatios) // ✅ 바로 최신 비율 적용
        }

        // 캐릭터 업데이트
        viewModel.dominantEmotion.observe(viewLifecycleOwner) { dominantEmotion ->
            updateCharacter(dominantEmotion)
        }


        // 감정에 따라 말풍선 배경 색 변경
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.manageBubbleO.backgroundTintList = ContextCompat.getColorStateList(requireContext(), colorResId)
            binding.manageBubbleX.backgroundTintList = ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // 'O' 클릭 시 -> EmotionManageActionFragment로 이동
        binding.manageBubbleO.setOnClickListener {
            val originalEmotion = viewModel.emotion.value ?: ""
            val stressReasonId = viewModel.emotionReason.value ?: -1 // 감정을 유발한 원인 ID

            // ✅ 사용자가 관리하려는 원래 감정을 저장 (EmotionFinalFragment에서 참조)
//            saveOriginalEmotion(originalEmotion)
//            saveStressReasonId(stressReasonId)

            // ✅ 관리 행동 O 선택 시 recommend = true 저장
            saveRecommendChoice(true)

            viewModel.setManageChoice("O") // 선택 데이터 저장
            navigateToEmotionManageActionFragment()
        }

        binding.manageBubbleX.setOnClickListener {
            // ✅ 관리 행동 X 선택 시 recommend = false 저장
            saveRecommendChoice(false)
            navigateToQuestion()
        }

    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    // ✅ SharedPreferences에 recommend 값 저장
    private fun saveRecommendChoice(isRecommended: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("recommend", isRecommended).apply()
    }



    // EmotionManageActionFragment로 이동하는 함수
    private fun navigateToEmotionManageActionFragment() {
        val fragment = EmotionManageActionFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 행동 질문 화면으로 이동 (HomeQuestionFragment)
    private fun navigateToQuestion() {
        val fragment = HomeQuestionFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}