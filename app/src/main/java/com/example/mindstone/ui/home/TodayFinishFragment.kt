package com.example.mindstone.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.MainActivity
import com.example.mindstone.databinding.FragmentTodayFinishBinding
import com.example.mindstone.ui.home.diary.DiaryLoadingFragment


class TodayFinishFragment : Fragment() {

    private lateinit var viewModel: TodayFinishViewModel

    private var _binding: FragmentTodayFinishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayFinishBinding.inflate(inflater, container, false)
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
    }

    private fun initClicker(){
        binding.todayFinishRecordTv.setOnClickListener{
            (activity as MainActivity).replaceFragment(HomeFragment())
        }

        binding.todayFinishDiaryTv.setOnClickListener{
            (activity as MainActivity).replaceFragment(DiaryLoadingFragment())
        }
    }


}