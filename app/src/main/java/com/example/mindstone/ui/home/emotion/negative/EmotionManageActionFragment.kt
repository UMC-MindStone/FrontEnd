package com.example.mindstone.ui.home.emotion.negative

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
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
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.local.PreferenceManager.getAccessToken
import com.example.mindstone.databinding.FragmentEmotionManageActionBinding
import com.example.mindstone.ui.home.emotion.view.EmotionManageActionViewModel
import com.example.mindstone.ui.home.emotion.view.EmotionModel

class EmotionManageActionFragment : Fragment() {

    private var _binding: FragmentEmotionManageActionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private lateinit var emotionStatusBar: EmotionStatusBar
    private lateinit var viewModel2: EmotionManageActionViewModel  // API 연동 관련 뷰모델

    private var otherActionClickCount = 0 // '다른 방법 찾기' 버튼 클릭 횟수


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionManageActionBinding.inflate(inflater, container, false)
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
        viewModel2 = ViewModelProvider(this).get(EmotionManageActionViewModel::class.java)

        emotionStatusBar = binding.statusBar // ✅ EmotionStatusBar 연결

        // ✅ 감정 비율을 실시간으로 감지하여 상태바 업데이트
        viewModel.normalizedEmotionRatios.observe(viewLifecycleOwner) { normalizedRatios ->
            emotionStatusBar.updateEmotions(normalizedRatios) // ✅ 바로 최신 비율 적용
        }

        // 캐릭터 업데이트
        viewModel.dominantEmotion.observe(viewLifecycleOwner) { dominantEmotion ->
            updateCharacter(dominantEmotion)
        }

        // API 호출을 위한 최신 accessToken 가져오기
        fetchAndCallAPI()


        // 감정에 따라 말풍선 배경 색 변경
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            val colorStateList = ContextCompat.getColorStateList(requireContext(), colorResId)
            binding.actionBubbleCenter.backgroundTintList = colorStateList
            binding.actionBubbleLeft.backgroundTintList = colorStateList
            binding.actionBubbleRight.backgroundTintList = colorStateList
        }

        // 말풍선 행동 추천 업데이트
        viewModel2.recommendations.observe(viewLifecycleOwner) { recommendations ->
            updateUI(recommendations)
        }

        // '다른 방법 찾기' 버튼 클릭 이벤트
        binding.otherAction.setOnClickListener {
            otherActionClickCount++
            if (otherActionClickCount >= 3) {
                navigateToFragment(EmotionManageActionFragment2()) // 4회 클릭 시 이동
            } else {
                val newAccessToken = PreferenceManager.getAccessToken() // 새로 가져오기
                if (!newAccessToken.isNullOrEmpty()) {
                    val newAuthToken = "Bearer $newAccessToken"
                    viewModel2.fetchStressRecommendations(newAuthToken) // 1~3회 클릭 시 새로운 추천 요청
                } else {
                    Log.e("TOKEN_ERROR", "Access token is null or empty")
                }
            }
        }


        // 'AI 추천' 버튼 클릭 이벤트 추가
        binding.aiAction.setOnClickListener {
            fetchAndCallGPTAPI() // 최신 토큰으로 AI 추천 API 호출
        }

        // API 응답을 받아 말풍선 UI 업데이트
        viewModel2.aiRecommendations.observe(viewLifecycleOwner) { recommendations ->
            updateAIUI(recommendations)
        }


        // 말풍선 클릭 시 선택한 행동을 EmotionActionTimeFragment로 전달, 이동
        listOf(binding.actionBubbleCenter, binding.actionBubbleLeft, binding.actionBubbleRight).forEach { bubble ->
            bubble.setOnClickListener {
                val selectedAction = bubble.text.toString() // 선택한 행동 가져오기
                saveStressAction(selectedAction) // ✅ 선택한 행동을 저장
                navigateToTimeFragment(selectedAction)
            }
        }

    }


    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }

    // ✅ SharedPreferences에 사용자가 선택한 행동 저장
    private fun saveStressAction(action: String) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("stress_action", action).apply()
    }


    private fun navigateToTimeFragment(action: String) {

        // ✅ 관리 행동을 SharedPreferences에 저장
        saveStressAction(action)

        val fragment = EmotionActionTimeFragment().apply {
            arguments = Bundle().apply {
                putString("SELECTED_ACTION", action) // 선택한 행동 전달
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    // 최신 토큰을 가져와 API 호출
    private fun fetchAndCallAPI() {
        val accessToken = PreferenceManager.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            val authToken = "Bearer $accessToken"
            viewModel2.fetchStressRecommendations(authToken)
        } else {
            Log.e("TOKEN_ERROR", "Access token is null or empty")
        }
    }

    // 최신 토큰을 가져와 AI 추천 API 호출
    private fun fetchAndCallGPTAPI() {
        val accessToken = PreferenceManager.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            val previousRecommand = binding.actionBubbleCenter.text.toString()
            viewModel2.fetchAIStressRecommendations("Bearer $accessToken", previousRecommand)
        } else {
            Log.e("TOKEN_ERROR", "Access token is null or empty")
        }
    }

    // 말풍선 - 사용자 관리 행동 추천
    private fun updateUI(recommendations: List<String>) {
        if (recommendations.size >= 3) {
            binding.actionBubbleCenter.text = recommendations[0]
            binding.actionBubbleLeft.text = recommendations[1]
            binding.actionBubbleRight.text = recommendations[2]
        }
    }
    // 말풍선 - AI 행동 추천 3가지
    private fun updateAIUI(recommendations: List<String>) {
        if (recommendations.size >= 3) {
            binding.actionBubbleCenter.text = recommendations[0]
            binding.actionBubbleLeft.text = recommendations[1]
            binding.actionBubbleRight.text = recommendations[2]
        }
    }

    // Fragment 전환 함수
    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel2.recommendations.removeObservers(viewLifecycleOwner)
        viewModel2.aiRecommendations.removeObservers(viewLifecycleOwner)
    }
}