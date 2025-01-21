package com.example.mindstone.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        // 감정 선택 버튼 클릭 이벤트 설정
        binding.homeHappyIv.setOnClickListener { navigateToEmotion("행복", R.color.happyColor, true) }
        binding.homeExcitedIv.setOnClickListener { navigateToEmotion("설렘", R.color.excitedColor, true) }
        binding.homeJoyIv.setOnClickListener { navigateToEmotion("기쁨", R.color.joyColor, true) }
        binding.homeCalmIv.setOnClickListener { navigateToEmotion("평온", R.color.calmColor, true) }

        binding.homeAngryIv.setOnClickListener { navigateToEmotion("화남", R.color.angryColor, false) }
        binding.homeDepressedIv.setOnClickListener { navigateToEmotion("우울", R.color.depressedColor, false) }
        binding.homeSadIv.setOnClickListener { navigateToEmotion("슬픔", R.color.sadColor, false) }
    }


    private fun navigateToEmotion(emotion: String, colorResId: Int, isPositive: Boolean) {
        val fragment = EmotionIntensityFragment.newInstance(emotion, colorResId, isPositive)
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