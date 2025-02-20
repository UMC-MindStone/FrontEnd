package com.example.mindstone.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.EmotionStatusBar
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentTodayFinishBinding
import com.example.mindstone.ui.home.diary.DiaryLoadingFragment
import com.example.mindstone.ui.home.emotion.view.EmotionModel
import java.time.LocalDate


class TodayFinishFragment : Fragment() {

    private var _binding: FragmentTodayFinishBinding? = null
    private val binding get() = _binding!!

    private lateinit var emotionStatusBar: EmotionStatusBar
    private lateinit var viewModel: EmotionModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        initClicker()
    }

    private val date = LocalDate.now()

    private fun initClicker(){
        binding.todayFinishRecordTv.setOnClickListener{
            val fragment = HomeFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putString("date", date.toString())
                }
                Log.d("TodayFinishFragment", date.toString())
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }

        binding.todayFinishDiaryTv.setOnClickListener{
            val fragment = DiaryLoadingFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putString("date", date.toString())
                }
                Log.d("TodayFinishFragment", date.toString())
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }
    }

    // 감정 캐릭터 업데이트
    private fun updateCharacter(emotion: String) {
        val characterResId = viewModel.getCharacterForEmotion(emotion) ?: R.drawable.ic_calm_charac
        binding.iconIv.setImageResource(characterResId)
    }


}