package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.databinding.FragmentMypageBinding
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mindstone.R

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
            when (selectedOption) {
                "계정 설정" -> {
                    replaceFragment(Account1Fragment()) // ✅ "계정 설정" 클릭 시 이동
                }
                "공지사항" -> {
                //    replaceFragment(NoticeFragment()) // ✅ "공지사항" 클릭 시 이동
                }
                "고객센터" -> {
                //    replaceFragment(CustomerServiceFragment()) // ✅ "고객센터" 클릭 시 이동
                }
                "정보" -> {
                //    replaceFragment(InfoFragment()) // ✅ "정보" 클릭 시 이동
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

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null) // ✅ 뒤로가기 버튼을 눌렀을 때 이전 화면으로 돌아갈 수 있도록 설정
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
