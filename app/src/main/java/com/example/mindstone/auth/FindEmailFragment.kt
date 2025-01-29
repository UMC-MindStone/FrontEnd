package com.example.mindstone.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentFindEmailBinding

class FindEmailFragment : Fragment() {

    private var _binding: FragmentFindEmailBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_find_email, container, false)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}