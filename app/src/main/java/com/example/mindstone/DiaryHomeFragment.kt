package com.example.mindstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import com.example.mindstone.databinding.FragmentDiaryHomeBinding

class DiaryHomeFragment : Fragment() {
    private var _binding: FragmentDiaryHomeBinding? = null
    private val binding get() = _binding!!
    private val diaryViewModel: DiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        diaryViewModel.diaryText.observe(viewLifecycleOwner){ text ->
            binding.diaryTextTv.text = text
            changeCompoenent()
        }

        diaryViewModel.images.observe(viewLifecycleOwner){ images ->
            binding.diaryImg1Iv.setImageURI(images[0])
            binding.diaryImg2Iv.setImageURI(images[1])
            binding.diaryImg3Iv.setImageURI(images[2])
            binding.diaryImg4Iv.setImageURI(images[3])
            changeCompoenent()
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }

    private fun changeCompoenent() {
        // 텍스트와 이미지 상태 확인
        val textExist = !binding.diaryTextTv.text.isNullOrBlank()
        val imageExist = diaryViewModel.images.value?.isNotEmpty() == true

        when {
            // 1. 텍스트 있음 + 이미지 있음
            textExist && imageExist -> {
                binding.diaryBlankTextCl.visibility = View.GONE
                binding.diaryTextCl.visibility = View.VISIBLE
                binding.diaryHomeAddimgCl.visibility = View.GONE
                binding.diaryImgCl.visibility = View.VISIBLE

                // 텍스트 상단, 이미지 하단 연결
                val paramsText = binding.diaryTextCl.layoutParams as ConstraintLayout.LayoutParams
                val paramsImage = binding.diaryImgCl.layoutParams as ConstraintLayout.LayoutParams

                paramsText.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsImage.topToBottom = binding.diaryTextCl.id

                binding.diaryTextCl.layoutParams = paramsText
                binding.diaryImgCl.layoutParams = paramsImage
            }

            // 2. 텍스트 있음 + 이미지 없음
            textExist && !imageExist -> {
                binding.diaryBlankTextCl.visibility = View.GONE
                binding.diaryTextCl.visibility = View.VISIBLE
                binding.diaryHomeAddimgCl.visibility = View.VISIBLE
                binding.diaryImgCl.visibility = View.GONE

                // 텍스트 상단, 이미지 첨부 안내 아래 연결
                val paramsText = binding.diaryTextCl.layoutParams as ConstraintLayout.LayoutParams
                val paramsAddImg = binding.diaryHomeAddimgCl.layoutParams as ConstraintLayout.LayoutParams

                paramsText.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsAddImg.topToBottom = binding.diaryTextCl.id

                binding.diaryTextCl.layoutParams = paramsText
                binding.diaryHomeAddimgCl.layoutParams = paramsAddImg
            }

            // 3. 텍스트 없음 + 이미지 있음
            !textExist && imageExist -> {
                binding.diaryBlankTextCl.visibility = View.VISIBLE
                binding.diaryTextCl.visibility = View.GONE
                binding.diaryHomeAddimgCl.visibility = View.GONE
                binding.diaryImgCl.visibility = View.VISIBLE

                // 빈 텍스트 안내 상단, 이미지 하단 연결
                val paramsBlankText = binding.diaryBlankTextCl.layoutParams as ConstraintLayout.LayoutParams
                val paramsImage = binding.diaryImgCl.layoutParams as ConstraintLayout.LayoutParams

                paramsBlankText.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsImage.topToBottom = binding.diaryBlankTextCl.id

                binding.diaryBlankTextCl.layoutParams = paramsBlankText
                binding.diaryImgCl.layoutParams = paramsImage
            }

            // 4. 텍스트 없음 + 이미지 없음
            !textExist && !imageExist -> {
                binding.diaryBlankTextCl.visibility = View.VISIBLE
                binding.diaryTextCl.visibility = View.GONE
                binding.diaryHomeAddimgCl.visibility = View.VISIBLE
                binding.diaryImgCl.visibility = View.GONE

                // 빈 텍스트 안내 상단, 이미지 첨부 안내 하단 연결
                val paramsBlankText = binding.diaryBlankTextCl.layoutParams as ConstraintLayout.LayoutParams
                val paramsAddImg = binding.diaryHomeAddimgCl.layoutParams as ConstraintLayout.LayoutParams

                paramsBlankText.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsAddImg.topToBottom = binding.diaryBlankTextCl.id

                binding.diaryBlankTextCl.layoutParams = paramsBlankText
                binding.diaryHomeAddimgCl.layoutParams = paramsAddImg
            }
        }
    }




}