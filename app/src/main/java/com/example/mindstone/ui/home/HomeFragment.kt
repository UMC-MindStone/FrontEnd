package com.example.mindstone.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentHomeBinding
import com.example.mindstone.ui.home.emotion.EmotionIntensityFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        // ViewModel을 Activity 범위에서 가져옴 (여러 Fragment에서 공유 가능)
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


        // 새로운 감정을 선택할 때 기존 데이터 초기화 (기존 감정 비율 & 최근 감정 유지)
        resetEmotionData()

        // 캐릭터 변경 (최근 감정 기준)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { updateCharacter(it) }

        // 감정 선택 버튼 클릭 이벤트 설정
        binding.homeHappyIv.setOnClickListener { navigateToIntensity("행복", R.color.happinessColor, true) }
        binding.homeExcitedIv.setOnClickListener { navigateToIntensity("설렘", R.color.thrillColor, true) }
        binding.homeJoyIv.setOnClickListener { navigateToIntensity("기쁨", R.color.joyColor, true) }
        binding.homeCalmIv.setOnClickListener { navigateToIntensity("평온", R.color.calmColor, true) }

        binding.homeAngryIv.setOnClickListener { navigateToIntensity("화남", R.color.angerColor, false) }
        binding.homeDepressedIv.setOnClickListener { navigateToIntensity("우울", R.color.depressionColor, false) }
        binding.homeSadIv.setOnClickListener { navigateToIntensity("슬픔", R.color.sadColor, false) }
    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.homeIconIv.setImageResource(characterResId)
    }

    // 새로운 감정 선택 시 기존 데이터 초기화 (기존 감정 비율 & 최근 감정 유지)
    private fun resetEmotionData() {
        viewModel.setEmotionData("", 0, false) // 감정 기본값 초기화
        viewModel.resetIntensity() // 감정 강도를 기본값(10)으로 초기화
        viewModel.setEmotionReason("") // 감정 이유 초기화
    }


    private fun navigateToIntensity(emotion: String, colorResId: Int, isPositive: Boolean) {
        // ViewModel에 데이터 저장 (Fragment 간 데이터 공유)
        viewModel.setEmotionData(emotion, colorResId, isPositive)
        viewModel.resetIntensity() // 감정 강도를 기본값(10)으로 초기화

        val fragment = EmotionIntensityFragment()
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