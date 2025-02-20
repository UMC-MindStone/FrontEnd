package com.example.mindstone.ui.mypage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.RetrofitClient.authService
import com.example.mindstone.databinding.DialogLogoutBinding
import com.example.mindstone.databinding.DialogSecessionBinding
import com.example.mindstone.databinding.FragmentAccount1Binding
import com.example.mindstone.domain.entity.DeleteAccountRequest
import com.example.mindstone.domain.entity.DeleteAccountResponse
import com.example.mindstone.domain.entity.LogoutRequest
import com.example.mindstone.domain.entity.LogoutResponse
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
                "비밀번호 설정" -> replaceFragment(PasswordFragment())
                "프로필 설정" -> replaceFragment(Account2Fragment())
                "기본 알림 설정" -> replaceFragment(SettingFragment())
                "감정 관리 방법 설정" -> replaceFragment(ManageFragment())
                "습관 설정" -> replaceFragment(HsettingFragment())
                "로그아웃" -> showLogoutDialog()
                "계정 탈퇴" -> showSecessionDialog()
            }
        }

        binding.rvAccount1Option.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAccount1Option.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        binding.rvAccount1Option.addItemDecoration(dividerItemDecoration)
    }

    // ✅ 🔹 **Fragment 전환 함수**
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // ✅ 로그아웃 다이얼로그 띄우기 (ViewBinding 적용)
    private fun showLogoutDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)

        // 다이얼로그 크기 조정
        dialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialogBinding.btnLogout.setOnClickListener {
            handleLogout()
            dialog.dismiss()
        }

        dialogBinding.btnLogoutCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // ✅ 로그아웃 처리
    private fun handleLogout() {
        val refreshToken = PreferenceManager.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "로그아웃 실패: 리프레시 토큰 없음", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LogoutRequest(refreshToken)

        authService.logout(request).enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show()

                    // 🔹 SharedPreferences에서 토큰 삭제 (자동 로그인 해제)
                    PreferenceManager.clearAccessToken()
                    PreferenceManager.clearRefreshToken()

                    // 🔹 로그인 화면으로 이동
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    requireContext().startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "로그아웃 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ✅ 계정 탈퇴 다이얼로그 띄우기 (ViewBinding 적용)
    private fun showSecessionDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogSecessionBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)

        // 다이얼로그 크기 조정
        dialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialogBinding.btnSecession.setOnClickListener {
            handleSecession()
            dialog.dismiss()
        }

        dialogBinding.btnSecessionCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // ✅ 계정 탈퇴 처리
    private fun handleSecession() {
        val refreshToken = PreferenceManager.getRefreshToken()
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

                        // 🔹 SharedPreferences에서 토큰 삭제
                        PreferenceManager.clearAccessToken()
                        PreferenceManager.clearRefreshToken()

                        // 🔹 로그인 화면으로 이동
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        requireContext().startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "계정 삭제 실패: ${body?.message ?: "알 수 없는 오류"}", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
