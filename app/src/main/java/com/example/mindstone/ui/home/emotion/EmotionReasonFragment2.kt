package com.example.mindstone.ui.home.emotion

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
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionReason2Binding
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionReasonFragment2 : Fragment() {

    private var _binding: FragmentEmotionReason2Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionReason2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 시스템 바(상태바, 네비게이션 바) 공간 자동 조정
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

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.reasonTv.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // ViewModel에서 감정 이유 가져와 적용
        viewModel.emotionReason.observe(viewLifecycleOwner) { reasonText ->
            if (!reasonText.isNullOrEmpty()) {
                binding.reasonTv.text = reasonText
            }
        }

        // 1초간 정지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateReasonBubble()
        }, 1000)
    }


    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    private fun animateReasonBubble() {
        binding.reasonTv.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 투명해짐
            .setDuration(1000) // 1초 동안 애니메이션 실행
            .withEndAction {
                navigateToEmotionFinalFragment() // 애니메이션 끝나면 다음 EmotionFinalFragment로 이동
            }
            .start()
    }

    private fun navigateToEmotionFinalFragment() {
        val fragment = EmotionFinalFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}