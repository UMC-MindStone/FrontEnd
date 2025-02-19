package com.example.mindstone.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentHabitReminderBinding
import com.example.mindstone.ui.habit.HabitCheckFragment

class HabitReminderFragment : Fragment() {

    private var _binding: FragmentHabitReminderBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 시스템 바(상태바, 네비게이션바) 공간 자동 조정
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.reminderBubbleX.setOnClickListener { navigateToHomeFragment() }

        binding.reminderBubbleO.setOnClickListener { navigateToRecord() }

    }

    // 습관 기록 화면으로 이동
    private fun navigateToRecord() {
        val fragment = HabitCheckFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 가능
            .commit()
    }

    private fun navigateToHomeFragment() {
        val fragment = HomeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 가능
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}