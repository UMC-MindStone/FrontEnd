package com.example.mindstone.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mindstone.MainActivity
import com.example.mindstone.databinding.FragmentHomeQuestion1Binding

class HomeQuestionFragment1 : Fragment() {
    private var _binding: FragmentHomeQuestion1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeQuestion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initClicker()
        statusChange()
        editing()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClicker(){
        binding.homeCompleteTv.setOnClickListener {
            hideKeyboard()
            Handler(Looper.getMainLooper()).postDelayed({
                (activity as MainActivity).replaceFragment(HomeFragment())
            }, 3000)
        }
    }

    private fun statusChange(){
        // 상태바 띄우기 관련 항목 넘긴거 받고 띄우기
    }

    fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun editing(){
        // 현재 텍스트가 작성되면 아래에 나타나는 글자 "확인"의 visibility를 gone으로 해두었습니다.
        // id: overlayEditText에 내용이 단 한글자라도 들어오면 확인 텍스트가 visible로 바뀌고 눌렀을때 다음 화면으로 넘어가도록 합니다.
        if (!binding.overlayEditText.text.isNullOrEmpty()) {
            binding.homeCompleteTv.visibility = View.VISIBLE
        } else {
            binding.homeCompleteTv.visibility = View.GONE
        }

    }
}