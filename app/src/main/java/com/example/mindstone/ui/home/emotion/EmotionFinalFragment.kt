package com.example.mindstone.ui.home.emotion

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
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionFinalBinding
import com.example.mindstone.ui.home.HomeFragment
import com.example.mindstone.ui.home.emotion.negative.EmotionManageChoiceFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel
import com.example.mindstone.ui.home.emotion.view.EmotionNoteViewModel
import com.example.mindstone.ui.search.SurveyViewModel
import kotlinx.coroutines.launch

class EmotionFinalFragment : Fragment() {

    private var _binding: FragmentEmotionFinalBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel
    private val emotionNoteViewModel: EmotionNoteViewModel by viewModels()

    private var userName: String = "사용자"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)

        // 시스템 바(상태바, 네비게이션바) 공간 자동 조정
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // SharedPreferences에서 사용자 이름 불러오기
        userName = getUserNickname()

        // 최신 감정 데이터를 다시 설정 (업데이트가 보장되도록)
        val selectedEmotion = viewModel.emotion.value ?: return
        val intensity = viewModel.intensity.value ?: 0
        val colorResId = viewModel.colorResId.value ?: R.color.calmColor // 기본 색상 설정
        val isPositive = viewModel.isPositive.value ?: true

        // ✅ 기존 옵저버 제거 후 새로 추가
        emotionNoteViewModel.emotionNoteResponse.removeObservers(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            emotionNoteViewModel.emotionNoteResponse.observe(viewLifecycleOwner) { response ->
                Log.d("EmotionFinalFragment", "📥 API 응답 감지됨")

                if (response?.isSuccess == true) {
                    val emotionId = response.result?.id ?: return@observe
                    Log.d("EmotionFinalFragment", "✅ 감정 데이터 저장 성공. 저장된 ID: $emotionId")

                    // ✅ SharedPreferences에 감정 ID 저장 (stressReason_id)
                    saveStressReasonId(emotionId)
                } else {
                    Log.e("EmotionFinalFragment", "❌ 감정 데이터 저장 실패: ${response?.message}")
                }
            }
        }


//        // ✅ 감정을 처음 선택한 경우 무조건 EmotionNote API 호출
//        if (!isAfterManagingNegativeEmotion()) {
//            saveEmotionData()  // ✅ 감정 저장 실행
//        } else {
//            Log.d("EmotionFinalFragment", "🚫 감정 선택이 아닌, 관리 행동 후 감정 평가 단계이므로 저장하지 않음.")
//        }

        // ✅ API 응답 처리
        observeApiResponse()

        saveEmotionData()


        // 상태바 색상 업데이트
        viewModel.emotionRatios.removeObservers(viewLifecycleOwner)
        viewModel.emotionRatios.observe(viewLifecycleOwner) { emotionRatios ->
            updateStatusBar(emotionRatios)
        }

        // 캐릭터 변경 (최근 감정 기준)
        viewModel.recentEmotion.removeObservers(viewLifecycleOwner)
        viewModel.recentEmotion.observe(viewLifecycleOwner) { recentEmotion ->
            updateCharacter(recentEmotion)
        }

        // 감정에 맞는 상태 업데이트
        viewModel.emotion.removeObservers(viewLifecycleOwner)
        viewModel.emotion.observe(viewLifecycleOwner) { emotion ->
            binding.finalStatusTv.text = getEmotionStatus(emotion)
        }

        viewModel.selectEmotion(selectedEmotion, colorResId, isPositive)
        hideAllEmotionViews()

        // 부정적 감정이면 finalStatus2Tv 보이기
        if (selectedEmotion in listOf("화남", "우울", "슬픔")) {
            binding.finalStatus2Tv.visibility = View.VISIBLE
        }

        // 감정별로 말풍선 및 텍스트 보여주기
        when (selectedEmotion) {
            "화남" -> animateSelectedEmotion(binding.finalAngryIv, binding.finalAngryLl, binding.angryIntensityTv, intensity, false)
            "우울" -> animateSelectedEmotion(binding.finalDepressedIv, binding.finalDepressedLl, binding.depressedIntensityTv, intensity, false)
            "슬픔" -> animateSelectedEmotion(binding.finalSadIv, binding.finalSadLl, binding.sadIntensityTv, intensity, false)
            "평온" -> animateSelectedEmotion(binding.finalCalmIv, binding.finalCalmLl, binding.calmIntensityTv, intensity, true)
            "행복" -> animateSelectedEmotion(binding.finalHappyIv, binding.finalHappyLl, binding.happyIntensityTv, intensity, true)
            "기쁨" -> animateSelectedEmotion(binding.finalJoyIv, binding.finalJoyLl, binding.joyIntensityTv, intensity, true)
            "설렘" -> animateSelectedEmotion(binding.finalExcitedIv, binding.finalExcitedLl, binding.excitedIntensityTv, intensity, true)
        }

    }

    // ✅ API 응답 처리 (중복 호출 방지)
    private fun observeApiResponse() {
        emotionNoteViewModel.emotionNoteResponse.removeObservers(viewLifecycleOwner)
        emotionNoteViewModel.emotionNoteResponse.observe(viewLifecycleOwner) { response ->
            if (response?.isSuccess == true) {
                Log.d("EmotionNoteAPI", "✅ 감정 데이터 저장 성공: $response")
                val emotionId = response.result?.id ?: return@observe
                saveStressReasonId(emotionId)
            } else {
                Log.e("EmotionNoteAPI", "❌ 감정 데이터 저장 실패: ${response?.message}")
            }
        }
    }

    // ✅ 일반 감정 저장 (EmotionNote API)
    private fun saveEmotionData() {
        val token = getUserToken()
        val emotionKorean = viewModel.emotion.value ?: return
        val intensity = viewModel.intensity.value ?: return
        val reason = viewModel.emotionReason.value ?: ""
        val emotionEnglish = convertEmotionToEnglish(emotionKorean)

        Log.d("EmotionFinalFragment", "📩 EmotionNote API 호출 시작")
        Log.d("EmotionFinalFragment", "📨 요청 데이터 - 감정: $emotionEnglish, 강도: $intensity, 이유: $reason")

        emotionNoteViewModel.postEmotionNote(token, emotionEnglish, intensity, reason)

        Log.d("EmotionFinalFragment", "📩 EmotionNote API 호출 완료 후 observe 실행 예정")

//        // ✅ API 응답을 받아 ID 저장
//        emotionNoteViewModel.emotionNoteResponse.observe(viewLifecycleOwner) { response ->
//            Log.d("EmotionFinalFragment", "📥 API 응답 감지됨")
//
//            if (response?.isSuccess == true) {
//                val emotionId = response.result?.id ?: return@observe
//                Log.d("EmotionFinalFragment", "✅ 감정 데이터 저장 성공. 저장된 ID: $emotionId")
//
//                // ✅ SharedPreferences에 감정 ID 저장 (stressReason_id)
//                saveStressReasonId(emotionId)
//            } else {
//                Log.e("EmotionFinalFragment", "❌ 감정 데이터 저장 실패: ${response?.message}")
//            }
//        }
    }

    // ✅ SharedPreferences에 EmotionNote의 id 저장
    private fun saveStressReasonId(id: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("stress_reason_id", id).apply()
        Log.d("EmotionFinalFragment", "✅ SharedPreferences에 stress_reason_id 저장됨: $id")
    }

    // ✅ SharedPreferences에서 EmotionNote의 id 불러오기
    private fun getStressReasonId(): Int {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("stress_reason_id", -1) // 기본값 -1
    }



    // ✅ `EmotionNote` API 호출 여부 판단 함수 수정
    private fun isAfterManagingNegativeEmotion(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains("stress_action") && sharedPreferences.contains("stress_duration")
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

    private fun getUserToken(): String {
        val sharedPreferences = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }


    // 상태바 색상 업데이트 (감정 비율 기반)
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
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }


    private fun animateSelectedEmotion(
        imageView: ImageView, layout: LinearLayout, textView: TextView, intensity: Int, isPositive: Boolean
    ) {
        // 선택된 뷰 보이기
        imageView.visibility = View.VISIBLE
        layout.visibility = View.VISIBLE

        // 강도 텍스트 설정
        textView.text = if (isPositive) "+$intensity" else "-$intensity"

        // 1초간 정지 후 애니메이션 실행
        Handler(Looper.getMainLooper()).postDelayed({
            animateEmotionBubble(imageView, layout)
        }, 1000)
    }


    // 감정 말풍선 애니메이션 적용
    private fun animateEmotionBubble(imageView: ImageView, layout: LinearLayout) {
        val duration = 1500L // 애니메이션 지속 시간

        imageView.animate()
            .translationY(-100f) // 위로 이동
            .alpha(0f) // 점점 사라짐
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator()) // 부드러운 감속
            .withEndAction {
                imageView.visibility = View.GONE // 애니메이션 종료 후 숨김
                checkEmotionAndNavigate() // 감정에 따라 Fragment 이동
            }
            .start()
        layout.animate()
            .translationY(-100f)
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                layout.visibility = View.GONE
                checkEmotionAndNavigate()
            }
            .start()
    }


    // 감정에 따라 적절한 Fragment로 이동
    private fun checkEmotionAndNavigate() {
        val selectedEmotion = viewModel.emotion.value ?: return
        if (selectedEmotion in listOf("화남", "우울", "슬픔")) {
            navigateToEmotionManageChoiceFragment()
        } else {
            navigateToHomeFragment()
        }
    }
    // 부정적 감정이면 EmotionManageChoiceFragment로 이동
    private fun navigateToEmotionManageChoiceFragment() {
        val fragment = EmotionManageChoiceFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    // 긍정적 감정이면 HomeFragment로 이동
    private fun navigateToHomeFragment() {
        val fragment = HomeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }


    // ✅ SharedPreferences에서 닉네임 가져오기
    private fun getUserNickname(): String {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_nickname", "사용자") ?: "사용자"
    }

    // ✅ SharedPreferences 초기화
    private fun resetManagedNegativeEmotionFlag() {
        val sharedPreferences = requireContext().getSharedPreferences("emotion_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove("stress_action")
            .remove("stress_duration")
            .remove("original_emotion")
            .apply()
    }




    // 감정에 맞는 상태 메시지를 반환하는 함수
    private fun getEmotionStatus(emotion: String?): String {
        return when (emotion) {
            "행복" -> "${userName}님은 지금 행복한 상태에요!"
            "설렘" -> "${userName}님은 지금 설레는 상태에요!"
            "기쁨" -> "${userName}님은 지금 기쁜 상태에요!"
            "평온" -> "${userName}님은 지금 평온한 상태에요!"
            "화남" -> "${userName}님은 지금 화나는 상태에요."
            "우울" -> "${userName}님은 지금 우울한 상태에요."
            "슬픔" -> "${userName}님은 지금 슬픈 상태에요."
            else -> ""
        }
    }

    // 모든 감정 말풍선 숨기기
    private fun hideAllEmotionViews() {
        binding.finalStatus2Tv.visibility = View.GONE
        binding.finalAngryIv.visibility = View.GONE
        binding.finalAngryLl.visibility = View.GONE
        binding.finalDepressedIv.visibility = View.GONE
        binding.finalDepressedLl.visibility = View.GONE
        binding.finalSadIv.visibility = View.GONE
        binding.finalSadLl.visibility = View.GONE
        binding.finalCalmIv.visibility = View.GONE
        binding.finalCalmLl.visibility = View.GONE
        binding.finalJoyIv.visibility = View.GONE
        binding.finalJoyLl.visibility = View.GONE
        binding.finalHappyIv.visibility = View.GONE
        binding.finalHappyLl.visibility = View.GONE
        binding.finalExcitedIv.visibility = View.GONE
        binding.finalExcitedLl.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}