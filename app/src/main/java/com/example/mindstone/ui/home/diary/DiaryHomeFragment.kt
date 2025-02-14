package com.example.mindstone.ui.home.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentDiaryHomeBinding

class DiaryHomeFragment : Fragment() {
    private var _binding: FragmentDiaryHomeBinding? = null
    private val binding get() = _binding!!
    private val diaryViewModel: DiaryViewModel by activityViewModels()


    val bundle = bundleOf(
        "fragment" to "today"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        diaryViewModel.diaryText.observe(viewLifecycleOwner){ text ->
            binding.diaryTextTv.text = text
            changeComponent()
        }

        diaryViewModel.images.observe(viewLifecycleOwner) { images ->
            val imageViews = listOf(binding.diaryImg1Iv, binding.diaryImg2Iv, binding.diaryImg3Iv, binding.diaryImg4Iv)
            for (i in imageViews.indices) {
                if (i < images.size) {
                    imageViews[i].setImageURI(images[i])
                } else {
                    imageViews[i].setImageDrawable(null)
                }
            }
            changeComponent()
        }

        binding.diaryTextEditIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryEditFragment(), bundle)
        }
        binding.diaryBlankTextIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryEditFragment(), bundle)
        }
        binding.diaryImgAddTransIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryImgFragment(), bundle)
        }
        binding.diaryImgAddBlankIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryImgFragment(), bundle)
        }

    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }


    private fun changeComponent() {
        // 텍스트와 이미지 상태 확인
        val textExist = !diaryViewModel.diaryText.value.isNullOrBlank()
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

                paramsText.topToTop = binding.diaryCharacterIv.id
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

                paramsText.topToTop = binding.diaryCharacterIv.id
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

                paramsBlankText.topToTop = binding.diaryCharacterIv.id
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

                paramsBlankText.topToTop = binding.diaryCharacterIv.id
                paramsAddImg.topToBottom = binding.diaryBlankTextCl.id

                binding.diaryBlankTextCl.layoutParams = paramsBlankText
                binding.diaryHomeAddimgCl.layoutParams = paramsAddImg

            }
        }
    }




}