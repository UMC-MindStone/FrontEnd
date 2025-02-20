package com.example.mindstone.ui.home.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.text.intl.Locale
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.data.remote.DiaryCreateRequest
import com.example.mindstone.databinding.FragmentDiaryHomeBinding
import java.time.LocalDate
import java.time.format.TextStyle

class DiaryHomeFragment : Fragment() {
    private var _binding: FragmentDiaryHomeBinding? = null
    private val binding get() = _binding!!
    private val diaryViewModel: DiaryViewModel by activityViewModels()

    private var fragment : String ?= null
    private var date = "2025-01-01"

    private var currentYear: Int = 2025
    private var currentMonth: Int = 1
    private var currentDay: Int = 1


    val bundle = bundleOf(
        "fragment" to "today"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragment = it.getString("fragment", null)
            date = it.getString("date", "2025-01-01")
        }

        val dateParts = date.split("-")
        if (dateParts.size == 3) {
            currentYear = dateParts[0].toIntOrNull() ?: 2025
            currentMonth = dateParts[1].toIntOrNull() ?: 1
            currentDay = dateParts[2].toIntOrNull() ?: 1
        }
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

//        diaryViewModel.images.observe(viewLifecycleOwner) { images ->
//            val imageViews = listOf(binding.diaryImg1Iv, binding.diaryImg2Iv, binding.diaryImg3Iv, binding.diaryImg4Iv)
//            for (i in imageViews.indices) {
//                if (i < images.size) {
//                    imageViews[i].setImageURI(images[i])
//                } else {
//                    imageViews[i].setImageDrawable(null)
//                }
//            }
//            changeComponent()
//        }
        diaryViewModel.images.observe(viewLifecycleOwner){ imgUris ->
            val img = listOf(binding.diaryImg1Iv, binding.diaryImg2Iv, binding.diaryImg3Iv, binding.diaryImg4Iv)
            for (i in imgUris.indices) {
                if (i < imgUris.size){
                    Glide.with(this)
                        .load(imgUris[i])
                        .into(img[i])
                } else {
                    img[i].setImageDrawable(null)
                }
            }
        }

        binding.diaryTextEditIv.setOnClickListener {
            val fragment = DiaryEditFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putInt("currentYear", currentYear)
                    putInt("currentMonth", currentMonth)
                    putInt("currentDay", currentDay)
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }
        binding.diaryBlankTextIv.setOnClickListener {
            val fragment = DiaryEditFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putInt("currentYear", currentYear)
                    putInt("currentMonth", currentMonth)
                    putInt("currentDay", currentDay)
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }
        binding.diaryImgAddTransIv.setOnClickListener {
            val fragment = DiaryImgFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putInt("currentYear", currentYear)
                    putInt("currentMonth", currentMonth)
                    putInt("currentDay", currentDay)
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }
        binding.diaryImgAddBlankIv.setOnClickListener {
            val fragment = DiaryImgFragment().apply{
                arguments = Bundle().apply {
                    putString("fragment", "today")
                    putInt("currentYear", currentYear)
                    putInt("currentMonth", currentMonth)
                    putInt("currentDay", currentDay)
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }

        binding.diaryRecreateIv.setOnClickListener {
            val diaryRequest = DiaryCreateRequest(diaryViewModel.diaryText.toString(), date)
            diaryViewModel.recreateDiary(requireContext(), date, "일기 재생성", diaryRequest,
                onFailure = { error ->
                Toast.makeText(requireContext(), "일기 생성 실패: $error", Toast.LENGTH_SHORT).show()
                }
            )
        }

//        if(fragment =="today"){
//            //오늘의 날짜 띄우기
//            val today = LocalDate.now()
//            val year= today.year
//            val month = today.monthValue
//            val day = today.dayOfMonth
//            val date = LocalDate.of( year, month, day)
//            // 요일 가져오기
//            val dayOfWeek = date.dayOfWeek
//            // 요일을 문자열로 변환 (한글 출력 가능)
//            val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, java.util.Locale.KOREAN)
//
//            binding.diaryDateTv.text = "${month}월 ${day}일 $dayName"
//        } else {
//
//        }

        val date = LocalDate.of(currentYear, currentMonth, currentDay)
        val dayOfWeek = date.dayOfWeek
        val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, java.util.Locale.KOREAN)
        binding.diaryDateTv.text = "${currentMonth}월 ${currentDay}일 $dayName"
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