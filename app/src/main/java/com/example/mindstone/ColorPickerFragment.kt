package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mindstone.databinding.FragmentColorPickerBinding

class ColorPickerFragment : DialogFragment() {
    private var _binding: FragmentColorPickerBinding? = null
    private val binding get() = _binding!!

    private var selectedColorIndex: Int? = null // 선택된 색상 저장 변수 (정수 값)
    var onColorSelected: ((colorIndex: Int?) -> Unit)? = null // 선택된 색상 인덱스를 전달할 콜백

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColorPickerBinding.inflate(inflater, container, false)

        // 버튼-링 클릭 리스너 초기화
        setupColorPickerListeners()

        binding.colorPickerCheckTv.setOnClickListener {
            onColorSelected?.invoke(selectedColorIndex) // 선택된 색상 인덱스를 전달
            dismiss() // 다이얼로그 닫기
        }

        return binding.root
    }

    private fun setupColorPickerListeners() {
        // 각 색상에 대해 고유한 정수 인덱스를 할당
        val colorButtonMap = mapOf(
            binding.colorPickerPurpleIv to 1,   // Purple 색상
            binding.colorPickerRedIv to 2,      // Red 색상
            binding.colorPickerSkyblueIv to 3,  // Skyblue 색상
            binding.colorPickerGrayIv to 4,     // Gray 색상
            binding.colorPickerGreenIv to 5,    // Green 색상
            binding.colorPickerYellowIv to 6,   // Yellow 색상
            binding.colorPickerPinkIv to 7      // Pink 색상
        )

        colorButtonMap.forEach { (button, index) ->
            button.setOnClickListener {
                // 선택된 색상 인덱스를 저장
                selectedColorIndex = index

                // 버튼에 해당하는 링만 보이게 하기
                buttonRingVisibility(button)
            }
        }
    }

    private fun buttonRingVisibility(selectedButton: View) {
        // 모든 링을 숨기고, 선택된 버튼의 링만 보이게 함
        val rings = listOf(
            binding.colorPickerPurpleRingIv,
            binding.colorPickerRedRingIv,
            binding.colorPickerSkyblueRingIv,
            binding.colorPickerGrayRingIv,
            binding.colorPickerGreenRingIv,
            binding.colorPickerYellowRingIv,
            binding.colorPickerPinkRingIv
        )

        rings.forEach { it.visibility = View.GONE }

        when (selectedButton) {
            binding.colorPickerPurpleIv -> binding.colorPickerPurpleRingIv.visibility = View.VISIBLE
            binding.colorPickerRedIv -> binding.colorPickerRedRingIv.visibility = View.VISIBLE
            binding.colorPickerSkyblueIv -> binding.colorPickerSkyblueRingIv.visibility = View.VISIBLE
            binding.colorPickerGrayIv -> binding.colorPickerGrayRingIv.visibility = View.VISIBLE
            binding.colorPickerGreenIv -> binding.colorPickerGreenRingIv.visibility = View.VISIBLE
            binding.colorPickerYellowIv -> binding.colorPickerYellowRingIv.visibility = View.VISIBLE
            binding.colorPickerPinkIv -> binding.colorPickerPinkRingIv.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
