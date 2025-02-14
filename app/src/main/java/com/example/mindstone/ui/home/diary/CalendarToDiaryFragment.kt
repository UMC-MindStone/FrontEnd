package com.example.mindstone.ui.home.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mindstone.ColorPickerFragment
import com.example.mindstone.MainActivity
import com.example.mindstone.MonthDayPickerFragment
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentCalendarToDiaryBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class CalendarToDiaryFragment : Fragment() {

    private var _bindng: FragmentCalendarToDiaryBinding? = null
    private val binding get() = _bindng!!
    private val diaryViewModel : DiaryViewModel by activityViewModels()

    // 일단 캘린더 프래그먼트에서 년, 월, 일 꼭 넘겨줘야 함.
    private var currentYear = arguments?.getInt("currentYear")?: 2025
    private var currentMonth = arguments?.getInt("currentMonth")?: 1
    private var currentDay= arguments?.getInt("currentDay")?: 1
    private var isRecord = arguments?.getBoolean("isRecord")?: false


    val bundle = bundleOf(
        "currentYear" to currentYear,
        "currentMonth" to currentMonth,
        "currentDay" to currentDay,
        "fragment" to "calendar"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindng = FragmentCalendarToDiaryBinding.inflate(inflater, container, false)
        return binding.root


        // date select down 버튼 클릭시 dialog 호출


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        changeComponent()

        diaryViewModel.diaryText.observe(viewLifecycleOwner){ text ->
            binding.diaryTextTv.text = text
            changeComponent()
        }
        diaryViewModel.emotionIcon.observe(viewLifecycleOwner){ iconResId ->
            binding.diaryCharacterIv.setImageResource(iconResId)
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

        binding.diaryDateSelectIv.setOnClickListener {
            showDateSelector()
        }

        if(!isRecord){
            binding.diaryCharacterIv.setOnClickListener {
                setColorPicker()
            }
        }

        binding.diaryEditTextBlankIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryEditFragment(), bundle)
        }
        binding.diaryEditTextIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryEditFragment(), bundle)
        }
        binding.diaryImgAddIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryImgFragment(), bundle)
        }
        binding.diaryGalleryIv.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(DiaryImgFragment(), bundle)
        }




    }

    private fun showDateSelector(){
        val dialog = MonthDayPickerFragment()
        dialog.onDateSelected = { selectedMonth , selectedDay ->
            currentMonth = selectedMonth
            currentDay = selectedDay

            val date = LocalDate.of( currentYear, currentMonth, currentDay)
            // 요일 가져오기
            val dayOfWeek = date.dayOfWeek
            // 요일을 문자열로 변환 (한글 출력 가능)
            val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

            binding.diaryDateTv.text = "${currentMonth}월 ${currentDay}일 $dayName"

            //  여기서 데이터 호출 해야하나?
        }
        dialog.show(parentFragmentManager, "MonthDayPickerFragment")

    }

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
                    diaryViewModel.updateEmotionIcon(iconRes)
                }
            }
        }
        dialog.show(parentFragmentManager, "ColorPickerFragment")

    }
    private fun changeComponent() {
        // 텍스트와 이미지 상태 확인
        val textExist = !diaryViewModel.diaryText.value.isNullOrBlank()
        val imageExist = diaryViewModel.images.value?.isNotEmpty() == true
        binding.diaryCharacterIv.visibility = View.VISIBLE

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

                paramsText.topToBottom = binding.diaryCharacterIv.id
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

                paramsText.topToBottom = binding.diaryCharacterIv.id
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

                paramsBlankText.topToBottom = binding.diaryCharacterIv.id
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

                paramsBlankText.topToBottom = binding.diaryCharacterIv.id
                paramsAddImg.topToBottom = binding.diaryBlankTextCl.id

                binding.diaryBlankTextCl.layoutParams = paramsBlankText
                binding.diaryHomeAddimgCl.layoutParams = paramsAddImg

            }
        }
    }

}