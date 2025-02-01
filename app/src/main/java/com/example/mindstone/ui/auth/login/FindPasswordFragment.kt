package com.example.mindstone.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mindstone.databinding.FragmentFindPasswordBinding
import com.example.mindstone.util.LoginDialogUtil

class FindPasswordFragment : Fragment() {

    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindPasswordViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "이메일 찾기" 버튼 클릭 시 -> FindEmailFragment로 전환
        binding.findEmailTabLl.setOnClickListener {
            (activity as? LoginActivity2)?.replaceFragment(FindEmailFragment())
        }

        // 뒤로 가기 "<" 버튼 클릭 시 -> LoginActivity로 이동
        binding.backIv.setOnClickListener {
            activity?.finish() // LoginActivity2 종료 → LoginActivity로 자동 이동
        }

        // "비밀번호 재설정" 버튼 클릭 시 -> 다이얼로그
        binding.findPWIv.setOnClickListener {
            val email = binding.findPWEmailEt.text.toString().trim()
            if (email.isEmpty()) {
                Log.d("FindPasswordFragment", "이메일 입력값이 비어 있음.")
                return@setOnClickListener
            }

            Log.d("FindPasswordFragment", "비밀번호 재설정 요청 시작 - 이메일: $email")

            viewModel.sendTempPassword(email,
                onSuccess = {
                    Log.d("FindPasswordFragment", "비밀번호 재설정 성공 - 이메일 전송 완료")
                    showCustomDialog()
                },
                onError = {
                    Log.e("FindPasswordFragment", "비밀번호 재설정 실패 - 오류 발생")
                    showCustomDialog()
                }
            )
        }

    }

    private fun showCustomDialog() {
        LoginDialogUtil.showCustomDialog(
            requireContext(),
            "입력하신 이메일로\n임시 비밀번호가 전송되었습니다.",
            "확인",
            null,
            onPositiveClick = {
                Log.d("FindPasswordFragment", "사용자가 다이얼로그 확인 버튼 클릭")
                // 다이얼로그만 닫힘
            }
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}