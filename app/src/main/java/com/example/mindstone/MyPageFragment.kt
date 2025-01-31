package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.databinding.FragmentMypageBinding
import androidx.recyclerview.widget.DividerItemDecoration

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리스트 데이터
        val options = listOf("계정 설정", "공지사항", "고객센터", "정보")

        // RecyclerView 설정
        val adapter = MyPageOptionAdapter(options) { selectedOption ->
            // 옵션 클릭 시 동작
            when (selectedOption) {
                "계정 설정" -> {
                    // 계정 설정 화면 이동
                }
                "공지사항" -> {
                    // 공지사항 화면 이동
                }
                "고객센터" -> {
                    // 고객센터 화면 이동
                }
                "정보" -> {
                    // 정보 화면 이동
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
