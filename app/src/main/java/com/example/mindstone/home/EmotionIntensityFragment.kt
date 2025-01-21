package com.example.mindstone.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionIntensityBinding

class EmotionIntensityFragment : Fragment() {

    private var _binding: FragmentEmotionIntensityBinding? = null
    private val binding get() = _binding!!

    private var emotion: String? = null
    private var colorResId: Int = 0
    private var isPositive: Boolean = true
    private var intensity: Int = 10  // 기본 값 10

    companion object {
        fun newInstance(emotion: String, colorResId: Int, isPositive: Boolean): EmotionIntensityFragment {
            val fragment = EmotionIntensityFragment()
            val args = Bundle()
            args.putString("emotion", emotion)
            args.putInt("colorResId", colorResId)
            args.putBoolean("isPositive", isPositive)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            emotion = it.getString("emotion")
            colorResId = it.getInt("colorResId")
            isPositive = it.getBoolean("isPositive")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionIntensityBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // UI 업데이트
//        binding.emotionText.text = "얼마나 ${emotion}한가요?"
//        binding.emotionBubble.setBackgroundColor(requireContext().getColor(colorResId))
//        binding.emotionBubble.text = "+ $intensity"
//
//        // 강도 조절 버튼
//        binding.increaseButton.setOnClickListener {
//            if (intensity < 100) {
//                intensity += 10
//                binding.emotionBubble.text = "+ $intensity"
//            }
//        }
//
//        binding.decreaseButton.setOnClickListener {
//            if (intensity > 10) {
//                intensity -= 10
//                binding.emotionBubble.text = "+ $intensity"
//            }
//        }
//
//        // 확인 버튼 클릭 시 결과 페이지로 이동
//        binding.confirmButton.setOnClickListener {
//            val fragment = EmotionResultFragment.newInstance(emotion!!, colorResId, isPositive, intensity)
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container, fragment)
//                .addToBackStack(null)
//                .commit()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}