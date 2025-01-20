package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentMonthStatBinding

class MonthStatFragment : Fragment() {
    private lateinit var binding: FragmentMonthStatBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthStatBinding.inflate(inflater, container, false)

        val text = "12월에는 50% 기록했고\n행복 감정이 가장 많았어요."
        val imageResId = R.drawable.ic_happy

        binding.monthStatMonthTv.text = text
        binding.monthStatIconIv.setImageResource(imageResId)

        return binding.root
    }
}