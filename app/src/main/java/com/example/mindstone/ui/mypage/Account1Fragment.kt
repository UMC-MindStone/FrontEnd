package com.example.mindstone.ui.mypage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.RetrofitClient.authService
import com.example.mindstone.databinding.FragmentAccount1Binding
import com.example.mindstone.domain.entity.DeleteAccountRequest
import com.example.mindstone.domain.entity.DeleteAccountResponse
import com.example.mindstone.ui.auth.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Account1Fragment : Fragment() {

    private var _binding: FragmentAccount1Binding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Account1OptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccount1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = listOf(
            "비밀번호 설정",
            "프로필 설정",
            "기본 알림 설정",
            "감정 관리 방법 설정",
            "습관 설정",
            "로그아웃",
            "계정 탈퇴"
        )

        adapter = Account1OptionAdapter(settings) { selectedSetting ->
            when (selectedSetting) {
                "비밀번호 설정" -> {
                    findNavController().navigate(R.id.action_account1Fragment_to_passwordFragment)
                }

                "프로필 설정" -> {
                    findNavController().navigate(R.id.action_account1Fragment_to_account2Fragment)
                }

                "기본 알림 설정" -> {
                    findNavController().navigate(R.id.action_account1Fragment_to_settingFragment)
                }

                "감정 관리 방법 설정" -> {
                    findNavController().navigate(R.id.action_account1Fragment_to_manageFragment)
                }

                "습관 설정" -> {
                    findNavController().navigate(R.id.action_account1Fragment_to_hsettingFragment)
                }

                "로그아웃" -> {
                    showLogoutDialog() // ✅ 로그아웃 다이얼로그 실행
                }

                "계정 탈퇴" -> {
                    showSecessionDialog()
                }
            }
        }

            // RecyclerView 설정
            binding.rvAccount1Option.layoutManager = LinearLayoutManager(requireContext())
            binding.rvAccount1Option.adapter = adapter

            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
            binding.rvAccount1Option.addItemDecoration(dividerItemDecoration)
        }

    // ✅ 로그아웃 다이얼로그 띄우기
    private fun showLogoutDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout) // ✅ 커스텀 다이얼로그 XML 적용

        val btnLogout = dialog.findViewById<Button>(R.id.btn_logout)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_logout_cancel)

        btnLogout.setOnClickListener {
            handleLogout() // ✅ 로그아웃 실행
            dialog.dismiss() // 다이얼로그 닫기
        }

        btnCancel.setOnClickListener {
            dialog.dismiss() // 다이얼로그 닫기
        }

        dialog.show()
    }

    // ✅ 로그아웃 처리
    private fun handleLogout() {
        // 🔹 SharedPreferences에서 토큰 삭제 (자동 로그인 해제)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", 0)
        sharedPreferences.edit().remove("accessToken").apply()

        // 🔹 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 백스택 제거
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ✅ 계정 탈퇴 다이얼로그 띄우기
    private fun showSecessionDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_secession) // ✅ 계정 탈퇴 다이얼로그 XML 적용

        val btnSecession = dialog.findViewById<Button>(R.id.btn_secession)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_secession_cancel)

        btnSecession.setOnClickListener {
            handleSecession() // ✅ 계정 탈퇴 실행
            dialog.dismiss() // 다이얼로그 닫기
        }

        btnCancel.setOnClickListener {
            dialog.dismiss() // 다이얼로그 닫기
        }

        dialog.show()
    }

    // ✅ 계정 탈퇴 처리
    private fun handleSecession() {
        val refreshToken = PreferenceManager.getRefreshToken() // 🔹 저장된 refreshToken 가져오기
        if (refreshToken.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "리프레시 토큰이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = DeleteAccountRequest(refreshToken)

        authService.deleteAccount(request).enqueue(object : Callback<DeleteAccountResponse> {
            override fun onResponse(call: Call<DeleteAccountResponse>, response: Response<DeleteAccountResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        Toast.makeText(requireContext(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                        // 🔹 SharedPreferences에서 토큰 삭제 (자동 로그인 해제)
                        PreferenceManager.clearAccessToken()
                        PreferenceManager.clearRefreshToken()

                        // 🔹 로그인 화면으로 이동
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 백스택 제거
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "계정 삭제 실패: ${body?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "계정 삭제 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}