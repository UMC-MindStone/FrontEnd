package com.example.mindstone.ui.home.emotion.negative

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionActionTime2Binding
import com.example.mindstone.domain.entity.EmotionNoteStressRequest
import com.example.mindstone.ui.home.emotion.view.EmotionModel
import com.example.mindstone.ui.home.emotion.view.EmotionNoteViewModel

class EmotionActionTimeFragment2 : Fragment() {

    private var _binding: FragmentEmotionActionTime2Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private val emotionNoteViewModel: EmotionNoteViewModel by viewModels()

    private var selectedAction: String? = null
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionActionTime2Binding.inflate(inflater, container, false)
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

        // 전달받은 데이터 가져오기
        selectedAction = arguments?.getString("SELECTED_ACTION")
        selectedHour = arguments?.getInt("HOUR", 0) ?: 0
        selectedMinute = arguments?.getInt("MINUTE", 0) ?: 0

        // 행동을 time_question_tv에 적용
        selectedAction?.let { binding.timeQuestionTv.text = "$it 을(를) 했어요." }

        // ✅ EmotionNoteStress API 호출
        saveEmotionStressData()



        val hour = arguments?.getInt("HOUR", 0) ?: 0
        val minute = arguments?.getInt("MINUTE", 0) ?: 0

        // 상태바 업데이트 (감정 비율 기반)
        viewModel.emotionRatios.observe(viewLifecycleOwner) { updateStatusBar(it) }

        // 캐릭터 업데이트 (최근 감정 기반)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { updateCharacter(it) }

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.timeTv.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // 받아온 시간으로 TextView 업데이트
        binding.timeTv.text = String.format("%d시간 %02d분", hour, minute)

        // 1초간 정지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateActionBubble()
        }, 1000)
    }

    // ✅ EmotionNoteStress API 호출 (관리 행동 저장)
    private fun saveEmotionStressData() {
        val token = getUserToken()
        val emotion = viewModel.emotion.value ?: return
        val intensity = viewModel.intensity.value ?: return
        val duration = "$selectedHour 시간 $selectedMinute 분"
        val emotionEnglish = convertEmotionToEnglish(emotion)

        // ✅ EmotionFinalFragment에서 저장한 감정 ID 가져오기
        val stressReasonId = getStressReasonId()

        Log.d("EmotionActionTimeFragment2", "📩 부정 감정 관리 저장 요청: 감정=$emotionEnglish, 강도=$intensity, 행동=$selectedAction, 시간=$duration, 원인ID=$stressReasonId")

        // ✅ API 호출 (stressReason_id 포함)
        emotionNoteViewModel.postEmotionNoteStress(
            token = token,
            emotion = emotionEnglish,
            emotionFigure = intensity,
            content = selectedAction ?: "",
            time = duration,
            stressReason_id = stressReasonId,
            recommend = true
        )

        // ✅ API 응답 처리
        emotionNoteViewModel.emotionNoteStressResponse.observe(viewLifecycleOwner) { response ->
            if (response?.isSuccess == true) {
                Log.d("EmotionActionTimeFragment2", "✅ 부정 감정 관리 데이터 저장 성공. ID: ${response.result?.id}")
            } else {
                Log.e("EmotionActionTimeFragment2", "❌ 부정 감정 관리 저장 실패: ${response?.message}")
            }
        }

        // ✅ SharedPreferences 초기화
        resetManagedNegativeEmotionFlag()
    }

    // ✅ 저장된 stressReasonId를 가져오는 함수
    private fun getStressReasonId(): Int {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("stress_reason_id", -1) // 기본값 -1
    }


    // ✅ 부정적 감정 관리 ID 저장
    private fun saveStressReasonId(id: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("stressReason_id", id).apply()
    }


    // ✅ 부정 감정 관리 플래그 초기화 (기록 유지 필요할 경우 주석 처리 가능)
    private fun resetManagedNegativeEmotionFlag() {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove("stress_action")
            .remove("stress_duration")
            .remove("original_emotion")
            .remove("stress_reason_id")
            .apply()
    }

    private fun getUserToken(): String {
        val sharedPreferences = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    // 감정 종류 변환 (한글 → 영어)
    private fun convertEmotionToEnglish(emotion: String): String {
        return when (emotion) {
            "화남" -> "ANGER"
            "우울" -> "DEPRESSION"
            "슬픔" -> "SAD"
            "평온" -> "CALM"
            "기쁨" -> "JOY"
            "설렘" -> "THRILL"
            "행복" -> "HAPPINESS"
            else -> "CALM" // 기본값 (예외 방지)
        }
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

    private fun animateActionBubble() {
        binding.timeTv.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 투명해짐
            .setDuration(1000) // 1초 동안 애니메이션 실행
            .withEndAction {
                navigateToEmotionAfterFragment() // 애니메이션 끝나면 다음 EmotionAfterActionFragment로 이동
            }
            .start()
    }

    private fun navigateToEmotionAfterFragment() {
        val fragment = EmotionAfterActionFragment()
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