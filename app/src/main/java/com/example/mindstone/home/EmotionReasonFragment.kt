package com.example.mindstone.home

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentEmotionReasonBinding
import com.example.mindstone.model.EmotionModel

class EmotionReasonFragment : Fragment() {

    private var _binding: FragmentEmotionReasonBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EmotionModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmotionReasonBinding.inflate(inflater, container, false)
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

        viewModel = ViewModelProvider(requireActivity()).get(EmotionModel::class.java)

        // 감정에 따른 말풍선 색상 적용
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.reasonBubbleIv.setColorFilter(
                ContextCompat.getColor(requireContext(), colorResId),
                PorterDuff.Mode.SRC_IN
            )
        }


        binding.reasonEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.reasonEt.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val newWidth = binding.reasonEt.measuredWidth + 50
                val newHeight = binding.reasonEt.measuredHeight + 30

                // FrameLayout (reason_bubble_container) 크기 조정
                binding.reasonBubbleContainer.layoutParams.width = newWidth
                binding.reasonBubbleContainer.layoutParams.height = newHeight
                binding.reasonBubbleContainer.requestLayout()

                // 말풍선 배경 크기도 같이 조정
                binding.reasonBubbleIv.layoutParams.width = newWidth
                binding.reasonBubbleIv.layoutParams.height = newHeight
                binding.reasonBubbleIv.requestLayout()
            }

            override fun afterTextChanged(s: Editable?) {}
        })




//        // 확인 버튼 클릭 시 애니메이션 실행 및 다음 화면 이동
//        binding.reasonConfirmTv.setOnClickListener {
//            val reasonText = binding.reasonEt.text.toString()
//            if (reasonText.isNotEmpty()) {
//                showEmotionReason(reasonText) // 입력값을 보여주고 애니메이션 실행
//            }
//        }
    }


//    // 감정 최종 반영 프래그먼트 (EmotionFinalFrgament) 로 이동
//    private fun navigateToFinalFragment() {
//        // EmotionFinalFragment로 이동 (이유 데이터 전달)
//        val fragment = EmotionFinalFragment.newInstance("reason", "extraData")
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, fragment) // 메인 컨테이너에 새로운 프래그먼트 교체
//            .addToBackStack(null) // 뒤로가기 버튼을 눌렀을 때 이전 화면으로 돌아갈 수 있도록 추가
//            .commit()
//    }

    companion object {
        fun newInstance(emotion: String) =
            EmotionReasonFragment().apply {
                arguments = Bundle().apply {
                    putString("emotion", emotion)
                }
            }
    }
}
