package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.FragmentWeekStatBinding

class WeakStatFragment : Fragment() {

    private var _binding: FragmentWeekStatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekStatBinding.inflate(inflater, container, false)

        val testValues1 = listOf(0f,2f,3f,4f,5f,6f,7f)
        binding.weekStatChart1Bc.setValues(testValues1)

        val testValues2 = listOf(4f,4f,4f,4f,5f,7f,6f)
        binding.weekStatChart2Bc.setValues(testValues2)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
