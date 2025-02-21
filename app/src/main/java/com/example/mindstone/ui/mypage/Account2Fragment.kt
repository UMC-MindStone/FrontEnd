package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindstone.R
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.databinding.FragmentAccount2Binding
import com.example.mindstone.domain.entity.NicknameUpdateRequest
import com.example.mindstone.domain.entity.NicknameUpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // 완료 버튼 클릭 시 닉네임 수정 API 호출
        binding.tvComplete.setOnClickListener {
            val newNickname = binding.etNickname.text.toString().trim()
            if (newNickname.isNotEmpty()) {
                updateNickname(newNickname)
            } else {
                showToast("닉네임을 입력해주세요.")
            }
        }

        // 뒤로 가기 버튼
        binding.ibBackAccount2.setOnClickListener {
            parentFragmentManager.popBackStack() // ✅ 현재 Fragment를 종료하고 이전 Fragment로 돌아가기
        }
    }

    private fun updateNickname(nickname: String) {
        val request = NicknameUpdateRequest(nickname)
        RetrofitClient.myPageService.updateNickname(request)
            .enqueue(object : Callback<NicknameUpdateResponse> {
                override fun onResponse(call: Call<NicknameUpdateResponse>, response: Response<NicknameUpdateResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.isSuccess) {
                                showToast("닉네임이 변경되었습니다.")

                                // ✅ 닉네임 변경 후 이전 Fragment(즉, Account1Fragment)로 돌아가기
                                parentFragmentManager.popBackStack()
                            } else {
                                showToast("닉네임 변경 실패: ${it.message}")
                            }
                        }
                    } else {
                        showToast("서버 오류: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<NicknameUpdateResponse>, t: Throwable) {
                    showToast("네트워크 오류: ${t.message}")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
