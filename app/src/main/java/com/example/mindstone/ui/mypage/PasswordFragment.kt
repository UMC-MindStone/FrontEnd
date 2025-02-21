package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mindstone.R
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.databinding.FragmentPasswordBinding
import com.example.mindstone.domain.entity.PasswordUpdateRequest
import com.example.mindstone.domain.entity.PasswordUpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private var isEditing = false

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

            // ✅ "완료" 버튼일 때만 API 호출하도록 변경
            if (!isEditing) {
                updatePassword()
            }
        }

        // 각 EditText의 텍스트 변화 감지
        binding.etPassword1.addTextChangedListener(createTextWatcher { validatePassword1() })
        binding.etPassword2.addTextChangedListener(createTextWatcher { validatePassword2() })
        binding.etPassword3.addTextChangedListener(createTextWatcher { validatePassword3() })

        // 뒤로 가기 버튼
        binding.ibBackPassword.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    // 입력 가능 상태 전환
    private fun setEditingEnabled(enabled: Boolean) {
        binding.etPassword1.isEnabled = enabled
        binding.etPassword2.isEnabled = enabled
        binding.etPassword3.isEnabled = enabled
    }

    private fun updatePassword() {
        val oldPassword = binding.etPassword1.text.toString().trim()
        val newPassword = binding.etPassword2.text.toString().trim()
        val confirmPassword = binding.etPassword3.text.toString().trim()

        if (!validatePassword1() || !validatePassword2() || !validatePassword3()) {
            return
        }

        val request = PasswordUpdateRequest(oldPassword, newPassword)
        RetrofitClient.myPageService.updatePassword(request)
            .enqueue(object : Callback<PasswordUpdateResponse> {
                override fun onResponse(call: Call<PasswordUpdateResponse>, response: Response<PasswordUpdateResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.isSuccess) {
                                showToast("비밀번호가 변경되었습니다.")
                                parentFragmentManager.popBackStack() // ✅ 비밀번호 변경 후 이전 화면으로 돌아가기
                            } else {
                                showToast("비밀번호 변경 실패: ${it.message}")
                            }
                        }
                    } else {
                        showToast("서버 오류: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PasswordUpdateResponse>, t: Throwable) {
                    showToast("네트워크 오류: ${t.message}")
                }
            })
    }

    // 기존 비밀번호 검증
    private fun validatePassword1(): Boolean {
        val input = binding.etPassword1.text.toString()
        return if (input.isEmpty()) {
            showError(binding.etPassword1, binding.tvPassword1Error, "기존 비밀번호를 입력하세요.")
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
            showError(binding.etPassword2, binding.tvPassword2Error, "영문, 숫자, 특수문자 포함 8자 이상 입력하세요.")
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
