package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindstone.databinding.FragmentBeforeDiaryBinding


class BeforeDiaryFragment : Fragment() {
    private var _binding : FragmentBeforeDiaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeforeDiaryBinding.inflate(inflater,container,false)
        return binding.root

    }

    private fun initButtonState(){
        // 현재 자동제작 버튼의 활성화 버튼은 visible상태로 놓여있고 비활성 버튼은 gone으로 둔 상태입니다.
        // 넘어오는 정보에 맞추어 두 버튼의 보임 여부를 설정해주시면 됩니다.
    }

}