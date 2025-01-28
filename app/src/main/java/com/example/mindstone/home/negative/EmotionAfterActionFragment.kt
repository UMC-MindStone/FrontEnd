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
import com.example.mindstone.databinding.FragmentEmotionAfterActionBinding
import com.example.mindstone.home.EmotionIntensityFragment
import com.example.mindstone.model.EmotionModel

class EmotionAfterActionFragment : Fragment() {

    private var _binding: FragmentEmotionAfterActionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionAfterActionBinding.inflate(inflater, container, false)
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

        // ViewModel 가져오기
        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)

        // 상태바 업데이트 (감정 비율 기반)
        viewModel.emotionRatios.observe(viewLifecycleOwner) { updateStatusBar(it) }

        // 캐릭터 업데이트 (최근 감정 기반)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { updateCharacter(it) }

        setupEmotionSelection()
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

    private fun setupEmotionSelection() {
        // 각 감정 버튼 클릭 이벤트 설정
        binding.afterAngryIv.setOnClickListener { navigateToIntensity("화남") }
        binding.afterDepressedIv.setOnClickListener { navigateToIntensity("우울") }
        binding.afterSadIv.setOnClickListener { navigateToIntensity("슬픔") }
        binding.afterCalmIv.setOnClickListener { navigateToIntensity("평온") }
        binding.afterHappyIv.setOnClickListener { navigateToIntensity("행복") }
        binding.afterJoyIv.setOnClickListener { navigateToIntensity("기쁨") }
        binding.afterExcitedIv.setOnClickListener { navigateToIntensity("설렘") }
    }


    private fun navigateToIntensity(selectedEmotion: String) {
        // ViewModel에 감정 저장
        viewModel.setAfterActionEmotion(selectedEmotion)

        // EmotionIntensityFragment로 이동 (isAfterAction 플래그 전달)
        val fragment = EmotionIntensityFragment().apply {
            arguments = Bundle().apply {
                putBoolean("isAfterAction", true)
                putString("selectedEmotion", selectedEmotion)
            }
        }
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