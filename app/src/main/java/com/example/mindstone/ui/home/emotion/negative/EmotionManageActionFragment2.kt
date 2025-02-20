package com.example.mindstone.ui.home.emotion.negative

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionManageAction2Binding
import com.example.mindstone.ui.home.HomeQuestionFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionManageActionFragment2 : Fragment() {

    private var _binding: FragmentEmotionManageAction2Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionManageAction2Binding.inflate(inflater, container, false)
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
            binding.actionContainer.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // '다른 방법 찾기' 클릭 시 EmotionManageActionFragment로 이동
        binding.otherAction.setOnClickListener {
            val fragment = EmotionManageActionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        }



        // '확인' 버튼 클릭 시 -> 입력한 행동을 EmotionActionTimeFragment로 전달 후 이동
        binding.actionConfirmTv.setOnClickListener {
            val userAction = binding.actionBubbleEt.text.toString().trim()
            if (userAction.isNotEmpty()) {
                viewModel.setUserAction(userAction) // 뷰모델에 행동 저장
                saveStressAction(userAction) // ✅ 입력한 행동 저장
                navigateToTimeFragment(userAction)
            } else {
                Toast.makeText(requireContext(), "행동을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        // '취소' 클릭 -> 행동 질문 화면으로 이동
        binding.actionCancel.setOnClickListener { navigateToQuestion() }


        // 글자 수 제한 (최대 10줄까지)
        binding.actionBubbleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lineCount = binding.actionBubbleEt.lineCount
                if (lineCount > 10) {
                    // 문자열을 Editable로 변환하여 설정
                    binding.actionBubbleEt.setText(binding.actionBubbleEt.text?.substring(0, binding.actionBubbleEt.selectionStart - 1))
                    binding.actionBubbleEt.setSelection(binding.actionBubbleEt.text?.length ?: 0) // 커서 위치 조정
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    // ✅ SharedPreferences에 사용자가 입력한 행동 저장
    private fun saveStressAction(action: String) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("stress_action", action).apply()
    }


    private fun navigateToTimeFragment(action: String) {

        // ✅ 관리 행동을 SharedPreferences에 저장
        saveStressAction(action)

        val fragment = EmotionActionTimeFragment().apply {
            arguments = Bundle().apply {
                putString("SELECTED_ACTION", action) // 직접 입력한 행동 전달
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // EmotionManageActionFragment3로 이동하는 함수
    private fun navigateToEmotionManageActionFragment3() {
        val fragment = EmotionManageActionFragment3()
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