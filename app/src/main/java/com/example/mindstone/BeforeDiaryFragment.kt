package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentBeforeDiaryBinding
import com.example.mindstone.databinding.FragmentDiaryCompleteBinding


class BeforeDiaryFragment : Fragment() {
    private var _binding : FragmentBeforeDiaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeforeDiaryBinding.inflate(inflater,container,false)
        return binding.root

    }

}