package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentWriteDiaryBinding


class WriteDiaryFragment : Fragment() {
    private var _binding = FragmentWriteDiaryBinding.inflate(layoutInflater)
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        turnPage()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun initData(){
        // binding.writeDateTv <- 여기에 완료 다음 다시 돌아가는 거 구현

    }

    private fun turnPage(){
        //binding.writeFinishBtn 프래그먼트 넘기는거 구현
    }


}