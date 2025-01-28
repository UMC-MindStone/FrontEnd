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

        // 감정에 따라 말풍선 배경 색 변경
        viewModel.colorResId.observe(viewLifecycleOwner) { colorResId ->
            binding.reasonContainer.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), colorResId)
        }

        // 글자 수 제한 (최대 10줄까지)
        binding.reasonBubbleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lineCount = binding.reasonBubbleEt.lineCount
                if (lineCount > 10) {
                    // 문자열을 Editable로 변환하여 설정
                    binding.reasonBubbleEt.setText(binding.reasonBubbleEt.text?.substring(0, binding.reasonBubbleEt.selectionStart - 1))
                    binding.reasonBubbleEt.setSelection(binding.reasonBubbleEt.text?.length ?: 0) // 커서 위치 조정
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // '확인' 버튼 클릭 시 ViewModel에 이유 저장 후 Fragment 이동
        binding.reasonConfirmTv.setOnClickListener {
            val reasonText = binding.reasonBubbleEt.text.toString().trim()
            if (reasonText.isNotEmpty()) {
                // ViewModel에 이유 저장
                viewModel.setEmotionReason(reasonText)
                // EmotionReasonFragment2로 이동
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_container, EmotionReasonFragment2())
                transaction.addToBackStack(null) // 뒤로 가기 지원
                transaction.commit()
            }
        }
    }
}