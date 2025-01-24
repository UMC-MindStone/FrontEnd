package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentDiaryHomeBinding

class DiaryHomeFragment : Fragment() {
    private var _binding: FragmentDiaryHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun changeCompoenent(){
        // 이미지 첨부 여부 + 텍스트 유무에 따라서 각 컴포넌트의 visibility와 constraintlayout 연결을 설정 해줘야 함.

        // 1. 텍스트 있음 + 이미지 있음

        // 2. 텍스트 없음 + 이미지 없음

        // 3. 텍스트 있음 + 이미지 없음

        // 4. 텍스트 없음 + 이미지 있음
    }


}