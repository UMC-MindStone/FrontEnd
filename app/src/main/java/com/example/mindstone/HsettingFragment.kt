package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.databinding.FragmentHsettingBinding

class HsettingFragment : Fragment() {

    private lateinit var binding: FragmentHsettingBinding
    private lateinit var adapter: HsettingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHsettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val habitList = listOf(
            Habit("운동 1시간", "1시간", "월 화 수", "07:00 / 19:00", true),
            Habit("아침 6시 기상", "즉시", "매일", "06:00", true),
            Habit("토익 1시간 공부", "1시간", "월 수 금", "13:00 / 20:00", true)
        )

        // onSwitchToggle 람다 전달
        adapter = HsettingAdapter(habitList) { habit, isEnabled ->
            // Switch 상태 변경 시 동작 정의
            habit.isEnabled = isEnabled
            // 추가적인 동작이 필요하면 여기에 작성
        }

        binding.rvHabitList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabitList.adapter = adapter
    }
}
