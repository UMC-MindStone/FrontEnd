package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.databinding.FragmentMypageBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mindstone.R
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.domain.entity.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUserInfo() // ✅ 유저 정보 가져오기 호출 추가

        // 리스트 데이터
        val options = listOf("계정 설정", "공지사항", "고객센터", "정보")

        // RecyclerView 설정
        val adapter = MyPageOptionAdapter(options) { selectedOption ->
            when (selectedOption) {
                "계정 설정" -> {
                    replaceFragment(Account1Fragment())
                }
                "공지사항" -> {
                    //    replaceFragment(NoticeFragment())
                }
                "고객센터" -> {
                    //    replaceFragment(CustomerServiceFragment())
                }
                "정보" -> {
                    //    replaceFragment(InfoFragment())
                }
            }
        }

        binding.rvMypageOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMypageOptions.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        binding.rvMypageOptions.addItemDecoration(dividerItemDecoration)
    }

    private fun fetchUserInfo() {
        RetrofitClient.myPageService.getUserInfo().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("API_RESPONSE", "서버 응답: ${it.result}") // 🔥 서버 응답 확인용 로그

                        if (it.isSuccess) {
                            if (isAdded) { // ✅ 프래그먼트가 살아있는지 확인
                                binding.tvNicknameMypage.text = it.result.nickname
                                binding.tvEmailMypage.text = it.result.email
                            }
                        } else {
                            showToast("유저 정보 불러오기 실패")
                        }
                    }
                } else {
                    showToast("서버 오류: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
