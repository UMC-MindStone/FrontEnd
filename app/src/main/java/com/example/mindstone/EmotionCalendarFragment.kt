package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentEmotionCalendarBinding

class EmotionCalendarFragment : Fragment() {

    private lateinit var binding: FragmentEmotionCalendarBinding
    private lateinit var viewPagerAdapter: EmotionCalendarVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmotionCalendarBinding.inflate(inflater, container, false)
        viewPagerAdapter = EmotionCalendarVPAdapter(this)
        binding.emotionCalendarStatVp.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(MonthStatFragment())
        viewPagerAdapter.addFragment(WeakStatFragment())
        viewPagerAdapter.addFragment(MonthSummaryFragment())

        return binding.root
    }
}