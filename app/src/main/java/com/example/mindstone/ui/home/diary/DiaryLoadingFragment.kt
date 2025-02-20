package com.example.mindstone.ui.home.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.example.mindstone.MainActivity
import com.example.mindstone.R
import com.example.mindstone.data.remote.DiaryCreateRequest
import com.example.mindstone.databinding.FragmentDiaryLoadingBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DiaryLoadingFragment : Fragment() {
    private var _binding : FragmentDiaryLoadingBinding? = null
    private val binding get() = _binding!!

    private var currentYear: Int = 2025
    private var currentMonth: Int = 1
    private var currentDay: Int = 1
    private var date: String = "2025-01-01"

    private val diaryViewModel: DiaryViewModel by activityViewModels()

    private var fragment : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString("date", "2025-01-01") // 기본값 설정
            fragment = it.getString("fragment", null)
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
        _binding= FragmentDiaryLoadingBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val bundle = bundleOf(
        "fragment" to fragment,
        "date" to date
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(fragment == "today"){
            //오늘의 날짜 띄우기
            updateEmotion()
            updateDateUI()
        } else{
            updateDateUI()
        }

        binding.diaryLoadingCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        sendCreateDiaryRequest()
        diaryViewModel.diaryCreated.observe(viewLifecycleOwner) { isCreated ->
            if (isCreated) {
                changeScreen()
            }
        }
    }
    private fun sendCreateDiaryRequest() {
        val diaryRequest = DiaryCreateRequest(".", date)

        val context = requireContext() // Context 가져오기
        val title = "자동 생성된 일기" // 기본 제목 (필요하면 변경 가능)

        diaryViewModel.createDiary(
            context,
            date,
            title,
            diaryRequest,
            onFailure = { error ->
                Toast.makeText(requireContext(), "일기 생성 실패: $error", Toast.LENGTH_SHORT).show()
            }
        )
        Log.d("DiaryLoadingFragment", "일기 생성 요청 보냄")
    }
    private fun updateDateUI() {
        val date = LocalDate.of(currentYear, currentMonth, currentDay)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

        binding.diaryDateTv.text = "${currentMonth}월 ${currentDay}일 $dayOfWeek"
    }

    private fun changeScreen() {
        // ✅ 다음 프래그먼트로 이동
        val fragment = DiaryHomeFragment().apply {
            arguments = Bundle().apply {
                putString("fragment", fragment)
                putString("date", date)
            }

        }
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, fragment)
            .commit()

    }

    private fun updateEmotion(){
        // EmotionViewmodel에 있는 actualEmotionRatios 를 diaryViewmodel.fetchEmotion에 넣어주기
    }

}