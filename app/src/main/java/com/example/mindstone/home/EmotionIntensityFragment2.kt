package com.example.mindstone.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionIntensity2Binding
import com.example.mindstone.model.EmotionModel

class EmotionIntensityFragment2 : Fragment() {

    private var _binding: FragmentEmotionIntensity2Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionIntensity2Binding.inflate(inflater, container, false)
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

        // 감정에 맞는 질문 업데이트
        viewModel.emotion.observe(viewLifecycleOwner) { emotion ->
            binding.resultStatusTv.text = getEmotionQuestion(emotion)
        }

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.resultBubble.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorResId))
        }

        // 감정 강도 표시 (+숫자 또는 -숫자)
        viewModel.intensity.observe(viewLifecycleOwner) { intensity ->
            val intensityText = if (viewModel.isPositive.value == true) "+$intensity" else "-$intensity"
            binding.resultBubbleTv.text = intensityText
        }

        // 1초간 정지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateResultBubble()
        }, 1000)
    }


    private fun animateResultBubble() {
        binding.resultBubble.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 투명해짐
            .setDuration(1000) // 1초 동안 애니메이션 실행
            .withEndAction {
                navigateToEmotionReasonFragment() // 애니메이션 끝나면 다음 Fragment로 이동
            }
            .start()
        binding.resultBubbleTv.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 투명해짐
            .setDuration(1000) // 1초 동안 애니메이션 실행
            .withEndAction {
                navigateToEmotionReasonFragment() // 애니메이션 끝나면 다음 Fragment로 이동
            }
            .start()
    }

    private fun navigateToEmotionReasonFragment() {
        val fragment = EmotionReasonFragment()
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

