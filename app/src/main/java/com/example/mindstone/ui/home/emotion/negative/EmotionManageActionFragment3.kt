package com.example.mindstone.ui.home.emotion.negative

import android.graphics.PorterDuff
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
import com.example.mindstone.databinding.FragmentEmotionManageAction3Binding
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionManageActionFragment3 : Fragment() {

    private var _binding: FragmentEmotionManageAction3Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionManageAction3Binding.inflate(inflater, container, false)
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

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.actionTv.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // 사용자가 입력한 행동을 가져와 @+id/action_tv 에 표시
        viewModel.userAction.observe(viewLifecycleOwner) { action ->
            binding.actionTv.text = action ?: "알 수 없음"
        }

        // 1초간 정지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateActionBubble()
        }, 1000)
    }


    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    private fun animateActionBubble() {
        binding.actionTv.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 투명해짐
            .setDuration(1000) // 1초 동안 애니메이션 실행
            .withEndAction {
                navigateToEmotionTimeFragment() // 애니메이션 끝나면 다음 EmotionFinalFragment로 이동
            }
            .start()
    }

    private fun navigateToEmotionTimeFragment() {
        val fragment = EmotionActionTimeFragment()
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