package com.example.mindstone.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentHomeQuestionBinding
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class HomeQuestionFragment : Fragment() {

    private var _binding: FragmentHomeQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var emotionStatusBar: EmotionStatusBar
    private lateinit var viewModel: EmotionModel

    private var questionIndex = 0 // 현재 질문 인덱스
    private val questions = listOf(
        "오늘 가장 인상 깊었던 일은?",
        "오늘 하루 중 추억(기록)하고 싶은 일은?",
        "오늘 느꼈던 새로운 감정은?",
        "감정의 이유는?"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // viewModel 가져오기
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

        resetUI() // 초기 상태 설정

        // 홈 버튼 클릭 시 초기화 상태 유지
        binding.homeToHomefragmentTv.setOnClickListener {
            (activity as MainActivity).replaceFragment(HomeFragment())
        }

        // homeQuestionmarkIv 버튼 클릭 시 편집 UI 활성화
        binding.homeQuestionmarkIv.setOnClickListener {
            binding.homeQuestionmarkIv.visibility = View.GONE
            binding.overlayEditText.visibility = View.VISIBLE
            binding.homeQuestionEditTextIv.visibility = View.VISIBLE
        }

        // 글자가 입력되면 homeCompleteTv 버튼 보이기
        binding.overlayEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.homeCompleteTv.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 버튼 클릭 시 3초 후 HomeFragment로 전환
        binding.homeCompleteTv.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                (activity as MainActivity).replaceFragment(HomeFragment())
            }, 3000)
        }

        // 4. "다른 질문 보기" 클릭 시 질문 변경
        binding.homeAnotherQuestionTv.setOnClickListener {
            questionIndex = (questionIndex + 1) % 4 // 0~3 순환

            if (questionIndex == 3) {
//                // 4번째 질문일 때 상태바에서 랜덤 감정 선택 (이전 감정 제외)
//                val emotions = listOf("행복", "슬픔", "화남", "설렘", "피곤") // 예제 감정 리스트
//                val selectedEmotion = emotions.random()
//                binding.homeStatusTv.text = "왜 \"$selectedEmotion\" 감정을 느꼈나요?"
            } else {
                binding.homeStatusTv.text = questions[questionIndex]
            }
        }
    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // UI 초기화
    private fun resetUI() {
        binding.homeQuestionmarkIv.visibility = View.VISIBLE
        binding.overlayEditText.visibility = View.GONE
        binding.homeQuestionEditTextIv.visibility = View.GONE
        binding.homeCompleteTv.visibility = View.GONE
        questionIndex = 0 // 질문 인덱스 초기화
        binding.homeStatusTv.text = "지금 어떤 상태인가요?" // 기본 질문
    }
}
