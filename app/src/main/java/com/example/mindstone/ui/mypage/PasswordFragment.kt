package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentPasswordBinding

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private var isEditing = false
    private val correctOldPassword = "password123" // 기존 비밀번호 (예제용)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        // 초기 상태: 비활성화
        setEditingEnabled(false)

        // 편집 버튼 클릭 시 동작
        binding.tvEdit.setOnClickListener {
            isEditing = !isEditing
            setEditingEnabled(isEditing)
            binding.tvEdit.text = if (isEditing) "완료" else "편집"
        }

        // 각 EditText의 텍스트 변화 감지
        binding.etPassword1.addTextChangedListener(createTextWatcher { validatePassword1() })
        binding.etPassword2.addTextChangedListener(createTextWatcher { validatePassword2() })
        binding.etPassword3.addTextChangedListener(createTextWatcher { validatePassword3() })
    }

    // 입력 가능 상태 전환
    private fun setEditingEnabled(enabled: Boolean) {
        binding.etPassword1.isEnabled = enabled
        binding.etPassword2.isEnabled = enabled
        binding.etPassword3.isEnabled = enabled
    }

    // 기존 비밀번호 검증
    private fun validatePassword1(): Boolean {
        val input = binding.etPassword1.text.toString()
        return if (input != correctOldPassword) {
            showError(
                binding.etPassword1,
                binding.tvPassword1Error,
                "비밀번호가 일치하지 않습니다."
            )
            false
        } else {
            hideError(binding.etPassword1, binding.tvPassword1Error)
            true
        }
    }

    // 새로운 비밀번호 검증
    private fun validatePassword2(): Boolean {
        val input = binding.etPassword2.text.toString()
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
        return if (!regex.matches(input)) {
            showError(
                binding.etPassword2,
                binding.tvPassword2Error,
                "영문, 숫자, 특수문자 포함 8자 이상."
            )
            false
        } else {
            hideError(binding.etPassword2, binding.tvPassword2Error)
            true
        }
    }

    // 비밀번호 확인 검증
    private fun validatePassword3(): Boolean {
        val password = binding.etPassword2.text.toString()
        val confirmPassword = binding.etPassword3.text.toString()
        return if (password != confirmPassword) {
            showError(
                binding.etPassword3,
                binding.tvPassword3Error,
                "비밀번호가 일치하지 않습니다."
            )
            false
        } else {
            hideError(binding.etPassword3, binding.tvPassword3Error)
            true
        }
    }

    // 에러 표시 (테두리 및 텍스트뷰)
    private fun showError(editText: View, textView: TextView, message: String) {
        if (editText.background != null) {
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.border_shape_error)
        }
        textView.text = message
        textView.visibility = View.VISIBLE
    }

    // 에러 숨기기 (테두리 및 텍스트뷰)
    private fun hideError(editText: View, textView: TextView) {
        if (editText.background != null) {
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.border_shape)
        }
        textView.visibility = View.GONE
    }

    // TextWatcher 생성
    private fun createTextWatcher(validationFunction: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validationFunction()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
