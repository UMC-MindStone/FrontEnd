package com.example.mindstone.ui.auth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mindstone.databinding.FragmentFindEmailBinding
import com.example.mindstone.ui.auth.login.FindPasswordFragment
import com.example.mindstone.ui.auth.login.LoginActivity2
import com.example.mindstone.util.LoginDialogUtil
import kotlinx.coroutines.flow.collectLatest

class FindEmailFragment : Fragment() {

    private var _binding: FragmentFindEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FindEmailViewModel by viewModels()

    private var retryCount = 0
    private lateinit var sharedPreferences: SharedPreferences  // 제한 시간 저장


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindEmailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "비밀번호 재설정" 클릭 시 -> FindPasswordFragment로 전환
        binding.findPWTabLl.setOnClickListener {
            (activity as? LoginActivity2)?.replaceFragment(FindPasswordFragment())
        }

        // 뒤로 가기 "<" 버튼 클릭 시 -> LoginActivity로 이동
        binding.backIv.setOnClickListener {
            activity?.finish() // LoginActivity2 종료 → LoginActivity로 자동 이동
        }


        binding.findEmailIv.setOnClickListener {
            val email = binding.findEmailEmailEt.text.toString()
            if (email.isBlank()) {
                return@setOnClickListener
            }
            if (isTimeLimitActive()) {
                showTimeLimitDialog()
                return@setOnClickListener
            }
            if (retryCount >= 3) {
                startFiveMinuteLimit()
                showLimitDialog()
                return@setOnClickListener
            }
            viewModel.findEmail(email)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.emailResult.collectLatest { state ->
                when (state) {
                    is FindEmailResultState.Loading -> { /* 로딩 처리 가능 */ }
                    is FindEmailResultState.Success -> {
                        showSuccessDialog(state.email)
                        retryCount = 0
                    }
                    is FindEmailResultState.Failure -> {
                        retryCount++
                        if (retryCount >= 3) {
                            startFiveMinuteLimit()
                            showLimitDialog()
                        } else {
                            showFailureDialog(retryCount)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 이메일 찾기 성공 다이얼로그
    private fun showSuccessDialog(email: String) {
        LoginDialogUtil.showCustomDialog(
            requireContext(),
            "일치하는 이메일 계정을 찾았습니다.\n$email",
            "로그인",
            "비밀번호 재설정",
            onPositiveClick = {
                navigateToLogin()   // 로그인 화면으로 이동
            },
            onNegativeClick = {
                (activity as? LoginActivity2)?.replaceFragment(FindPasswordFragment())   // 비밀번호 재설정 화면 이동
            }
        )
    }

    // 이메일 찾기 실패 다이얼로그
    private fun showFailureDialog(retryCount: Int) {
        LoginDialogUtil.showCustomDialog(
            requireContext(),
            "입력하신 이메일을 찾을 수 없습니다.\n재시도하거나 고객센터에 문의하세요.\n(시도 횟수: $retryCount/3)",
            "재시도",
            "고객센터 문의",
            onPositiveClick = {
                // 재시도 (다이얼로그만 닫힘)
            },
            onNegativeClick = {
                // 고객센터 문의 화면으로 이동
            }
        )
    }

    // 입력 제한 다이얼로그 (3회 실패 시)
    private fun showLimitDialog() {
        LoginDialogUtil.showCustomDialog(
            requireContext(),
            "입력 시도가 제한되었습니다.\n이메일 찾기 기능이 5분간 잠깁니다.\n잠시 후 재시도하거나 고객센터에 문의하세요.\n(시도 횟수: 3/3)",
            "로그인",
            "고객센터 문의",
            onPositiveClick = {
                navigateToLogin() // 로그인 화면으로 이동
            },
            onNegativeClick = {
                // 고객센터 문의 화면으로 이동
            }
        )
    }

    // 5분 제한 시작 (현재 시간 저장)
    private fun startFiveMinuteLimit() {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().putLong("limit_start_time", currentTime).apply()
    }

    // 5분 제한 체크 (현재 시간이 저장된 시간 + 5분을 초과했는지 확인)
    private fun isTimeLimitActive(): Boolean {
        val limitStartTime = sharedPreferences.getLong("limit_start_time", 0)
        val currentTime = System.currentTimeMillis()
        return (currentTime - limitStartTime) < (5 * 60 * 1000)  // 5분 제한 (300,000ms)
    }


    // 5분 제한 다이얼로그 (입력 시도 제한 이후)
    private fun showTimeLimitDialog() {
        LoginDialogUtil.showCustomDialog(
            requireContext(),
            "이메일 찾기가 5분간 제한됩니다.\n잠시 후 다시 시도하세요.",
            "확인",
            "고객센터 문의",
            onPositiveClick = {
                // 사용자가 확인을 눌렀을 때 추가 동작 없음. 다이얼로그만 닫힘.
            },
            onNegativeClick = {
                // 고객센터 문의 화면으로 이동
            }
        )
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()  // 현재 액티비티 종료
    }
}