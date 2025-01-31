package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentAccount2Binding

class Account2Fragment : Fragment() {

    private var _binding: FragmentAccount2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccount2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 상태: 완료 버튼 비활성화
        binding.tvComplete.isEnabled = false
        binding.tvComplete.alpha = 0.5f // 비활성화 상태로 시각적 표시

        // 닉네임 입력 감지
        binding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 여부에 따라 완료 버튼 활성화/비활성화
                binding.tvComplete.isEnabled = !s.isNullOrEmpty()
                binding.tvComplete.alpha = if (s.isNullOrEmpty()) 0.5f else 1.0f
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 완료 버튼 클릭 시 동작
        binding.tvComplete.setOnClickListener {
            // Navigation Component를 사용해 account1Fragment로 이동
            findNavController().navigate(R.id.action_account2Fragment_to_account1Fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
