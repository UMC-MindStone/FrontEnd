package com.example.mindstone.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentHomeQuestion1Binding

class HomeQuestionFragment1 : Fragment() {
    private var _binding: FragmentHomeQuestion1Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initClicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeQuestion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClicker(){

    }

    private fun statusChange(){
        // 상태바 변경 로직 작성
    }

    private fun editing(){
        // 현재 텍스트가 작성되면 아래에 나타나는 글자 "확인"의 visibility를 gone으로 해두었습니다.
        // id: overlayEditText에 내용이 단 한글자라도 들어오면 확인 텍스트가 visible로 바뀌고 눌렀을때 다음 화면으로 넘어가도록 합니다.
    }
}