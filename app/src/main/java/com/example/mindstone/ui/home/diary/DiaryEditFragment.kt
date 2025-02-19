package com.example.mindstone.ui.home.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.mindstone.databinding.FragmentDiaryEditBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DiaryEditFragment : Fragment() {
    private var _binding : FragmentDiaryEditBinding? = null
    private val binding get() = _binding!!
    private val diaryViewModel: DiaryViewModel by activityViewModels()

    private var beforeFragment : String = ""
    private var currentYear : Int = 2025
    private var currentMonth :Int = 1
    private var currentDay : Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            beforeFragment = bundle.getString("fragment", "Unknown")
            currentYear = bundle.getInt("currentYear")
            currentMonth = bundle.getInt("currentMonth")
            currentDay = bundle.getInt("currentDay")
        }
        Log.d("date", "$currentYear, $currentMonth, $currentDay")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryEditBinding.inflate(inflater,container,false)
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

        when (beforeFragment) {
            "today" -> {
                binding.diaryEditAutoTv.visibility = View.VISIBLE
                binding.diaryEditDateTv.visibility = View.GONE
            }
            "calendar" -> {
                binding.diaryEditAutoTv.visibility = View.GONE
                binding.diaryEditDateTv.visibility = View.VISIBLE
            }
        }

        initClicker()
        setObserver()



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var tempText: String = "" // 확인 버튼 누르기 전 임시 텍스트
    private fun setObserver(){
        // diaryHome 에 있는 텍스트 가져오기
        diaryViewModel.diaryText.observe(viewLifecycleOwner){ text ->
            tempText = text ?: ""
            binding.diaryEditTextTv.setText(tempText)
        }

    }

    private fun initClicker(){
        binding.diaryEditCompleteIv.setOnClickListener{
            Log.d("Upload Debug","✅ 저장 버튼 클릭됨")
            val updatedText = binding.diaryEditTextTv.text.toString()
            diaryViewModel.updateDiaryText(updatedText)

            val date = LocalDate.of( currentYear, currentMonth, currentDay)
            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            Log.d("date", formattedDate)

            diaryViewModel.saveOrUpdateDiary(requireContext(),formattedDate, updatedText)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.diaryEditCloseIv.setOnClickListener{
            binding.diaryEditTextTv.setText(tempText)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}