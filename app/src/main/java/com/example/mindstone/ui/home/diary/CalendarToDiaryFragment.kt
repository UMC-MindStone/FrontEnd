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
import com.example.mindstone.ColorPickerFragment
import com.example.mindstone.MyApplication
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentCalendarToDiaryBinding

class CalendarToDiaryFragment : Fragment() {

    private var _bindng: FragmentCalendarToDiaryBinding? = null
    private val binding get() = _bindng!!
    private val diaryViewModel : DiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindng = FragmentCalendarToDiaryBinding.inflate(inflater, container, false)
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

        diaryViewModel.images.observe(viewLifecycleOwner){ images ->
            binding.diaryImg1Iv.setImageURI(images[0])
            binding.diaryImg2Iv.setImageURI(images[1])
            binding.diaryImg3Iv.setImageURI(images[2])
            binding.diaryImg4Iv.setImageURI(images[3])
            changeComponent()
        }


        //여기에 이전에 따로 감정을 기록하지 않았을 때! 에 라는 조건이 추가 되어야 합니다.
        binding.diaryCharacterIv.setOnClickListener {
            setColorPicker()
        }


    }
    val bundle = bundleOf(
        "fragment" to "calendar"
    )

    private fun setColorPicker(){
        val dialog = ColorPickerFragment().apply{
            onColorSelected = { colorIndex ->
                colorIndex?.let{
                    val iconRes = when(it){
                        1 -> R.drawable.ic_depression
                        2 -> R.drawable.ic_angry
                        3 -> R.drawable.ic_sad
                        4 -> R.drawable.ic_calm
                        5 -> R.drawable.ic_joy
                        6 -> R.drawable.ic_happy
                        7 -> R.drawable.ic_romance
                        else -> R.drawable.btn_nothing_normal
                    }
                    binding.diaryCharacterIv.setImageResource(iconRes)
                }
            }
        }
        dialog.show(parentFragmentManager, "ColorPickerFragment")

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

                paramsText.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                paramsImage.topToBottom = binding.diaryTextCl.id

                binding.diaryTextCl.layoutParams = paramsText
                binding.diaryImgCl.layoutParams = paramsImage

                binding.diaryEditTextIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryEdit, bundle)
                }
                binding.diaryGalleryIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryImgEdit, bundle)
                }
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

                binding.diaryEditTextIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryEdit, bundle)
                }
                binding.diaryImgAddIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryImgEdit, bundle)
                }

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

                binding.diaryEditTextBlankIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryEdit, bundle)
                }
                binding.diaryGalleryIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryImgEdit, bundle)
                }

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

                binding.diaryEditTextBlankIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryEdit, bundle)
                }
                binding.diaryImgAddIv.setOnClickListener {
                    findNavController().navigate(R.id.diaryHome_to_diaryImgEdit, bundle)
                }
            }
        }
    }



}