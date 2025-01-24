package com.example.mindstone.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionFinalBinding
import com.example.mindstone.model.EmotionModel

class EmotionFinalFragment : Fragment() {

    private var _binding: FragmentEmotionFinalBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmotionFinalBinding.inflate(inflater, container, false)
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

        // 감정과 강도 UI 업데이트
        viewModel.emotion.observe(viewLifecycleOwner) { emotion ->
            viewModel.intensity.observe(viewLifecycleOwner) { intensity ->
                updateEmotionBubble(emotion, intensity)
            }
        }
    }

    private fun updateEmotionBubble(emotion: String?, intensity: Int) {
        val intensityText = if (viewModel.isPositive.value == true) "+$intensity" else "-$intensity"
        when (emotion) {
            "화남" -> showEmotionBubble(binding.finalAngryIv, binding.angryIntensityTv, intensityText)
            "행복" -> showEmotionBubble(binding.finalHappyIv, binding.happyIntensityTv, intensityText)
            "우울" -> showEmotionBubble(binding.finalDepressedIv, binding.depressedIntensityTv, intensityText)
            "설렘" -> showEmotionBubble(binding.finalExcitedIv, binding.excitedIntensityTv, intensityText)
            "슬픔" -> showEmotionBubble(binding.finalSadIv, binding.sadIntensityTv, intensityText)
            "평온" -> showEmotionBubble(binding.finalCalmIv, binding.calmIntensityTv, intensityText)
            "기쁨" -> showEmotionBubble(binding.finalJoyIv, binding.joyIntensityTv, intensityText)
        }
    }

    private fun showEmotionBubble(imageView: ImageView, textView: TextView, intensityText: String) {
        textView.text = intensityText
        imageView.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE

        // 1초간 유지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateEmotionBubble(imageView, textView)
        }, 1000)
    }

    private fun animateEmotionBubble(imageView: ImageView, textView: TextView) {
        val animDuration = 1000L

        imageView.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 점점 사라짐
            .setDuration(animDuration)
            .withEndAction { imageView.visibility = View.GONE }
            .start()

        textView.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 점점 사라짐
            .setDuration(animDuration)
            .withEndAction { textView.visibility = View.GONE }
            .start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}