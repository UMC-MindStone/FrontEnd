package com.example.mindstone.home.negative

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
import com.example.mindstone.databinding.FragmentEmotionManageChoiceBinding
import com.example.mindstone.viewmodel.EmotionModel

class EmotionManageChoiceFragment : Fragment() {

    private var _binding: FragmentEmotionManageChoiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel

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


        // 기존 감정 데이터 즉시 반영 (앱 실행 시 초기화)
        viewModel.emotionRatios.value?.let { updateStatusBar(it) }
        viewModel.recentEmotion.value?.let { updateCharacter(it) }

        // 상태바 업데이트 (감정 비율 기반)
        viewModel.emotionRatios.observe(viewLifecycleOwner) { ratios ->
            updateStatusBar(ratios)
        }

        // 캐릭터 업데이트 (최근 선택한 감정 반영)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { recentEmotion ->
            updateCharacter(recentEmotion)
        }

        // 감정에 따라 말풍선 배경 색 변경
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.manageBubbleO.backgroundTintList = ContextCompat.getColorStateList(requireContext(), colorResId)
            binding.manageBubbleX.backgroundTintList = ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // 'O' 클릭 시 -> EmotionManageActionFragment로 이동
        binding.manageBubbleO.setOnClickListener {
            viewModel.setManageChoice("O") // 선택 데이터 저장
            navigateToEmotionManageActionFragment()
        }
    }

    // 상태바 색상 업데이트 (감정 비율 기반)
    private fun updateStatusBar(emotionRatios: Map<String, Float>) {
        val sortedRatios = viewModel.getSortedEmotionRatios()
        val sortedColors = sortedRatios.mapNotNull { (emotion, _) ->
            viewModel.getEmotionColor(emotion)?.let { ContextCompat.getColor(requireContext(), it) }
        }
        if (sortedColors.isNotEmpty()) {
            // 🎨 가장 비율이 높은 감정의 색상을 선택하여 필터 적용
            val dominantColor = sortedColors.first()

            binding.statusBar.setColorFilter(dominantColor, PorterDuff.Mode.SRC_IN)
        }
    }


    // 최근 감정에 맞는 캐릭터 변경
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm
        binding.iconIv.setImageResource(characterResId)
    }

    // EmotionManageActionFragment로 이동하는 함수
    private fun navigateToEmotionManageActionFragment() {
        val fragment = EmotionManageActionFragment()
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