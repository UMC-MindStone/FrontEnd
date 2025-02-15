package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentAccount1Binding

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
            "친구 설정",
            "로그아웃",
            "계정 탈퇴"
        )

        adapter = Account1OptionAdapter(settings) { selectedSetting ->
            when (selectedSetting) {
                "비밀번호 설정" -> {
                //    findNavController().navigate(R.id.action_accountSettingFragment_to_passwordSettingFragment)
                }
                "프로필 설정" -> {
                //    findNavController().navigate(R.id.action_accountSettingFragment_to_profileSettingFragment)
                "로그아웃" -> {
                    // 로그아웃 처리
                }
                "계정 탈퇴" -> {
                    // 계정 탈퇴 처리
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
