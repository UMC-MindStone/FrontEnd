package com.example.mindstone.ui.home.emotion.negative

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
import com.example.mindstone.databinding.FragmentEmotionAfterActionBinding
import com.example.mindstone.ui.home.emotion.EmotionIntensityFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionAfterActionFragment : Fragment() {

    private var _binding: FragmentEmotionAfterActionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar


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

        emotionStatusBar = binding.statusBar // ✅ EmotionStatusBar 연결

        // ✅ 감정 비율을 실시간으로 감지하여 상태바 업데이트
        viewModel.normalizedEmotionRatios.observe(viewLifecycleOwner) { normalizedRatios ->
            emotionStatusBar.updateEmotions(normalizedRatios) // ✅ 바로 최신 비율 적용
        }

        // 캐릭터 업데이트
        viewModel.dominantEmotion.observe(viewLifecycleOwner) { dominantEmotion ->
            updateCharacter(dominantEmotion)
        }


        setupEmotionSelection()
    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
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