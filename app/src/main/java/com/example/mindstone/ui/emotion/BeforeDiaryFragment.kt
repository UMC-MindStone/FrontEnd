package com.example.mindstone.ui.emotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentBeforeDiaryBinding
import com.example.mindstone.ui.home.diary.CalendarToDiaryFragment
import com.example.mindstone.ui.home.diary.DiaryLoadingFragment
import com.example.mindstone.ui.home.diary.DiaryViewModel


class BeforeDiaryFragment : Fragment() {
    private var _binding : FragmentBeforeDiaryBinding? = null
    private val binding get() = _binding!!

    private var isRecord: Boolean = false
    private var date : String? = null


    private val diaryViewModel : DiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString("date", "2025-01-01") // 기본값 설정
            isRecord = it.getBoolean("isRecord", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeforeDiaryBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        initButtonState()

        binding.beforeWriteBtn.setOnClickListener {
            val fragment = CalendarToDiaryFragment().apply{
                arguments = Bundle().apply {
                    putString("date", date)
                    putBoolean("isRecord", isRecord)
                }
            }

            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()
        }

        binding.beforeAutocreateAbleBtn.setOnClickListener {
            val fragment = DiaryLoadingFragment().apply{
                arguments = Bundle().apply {
                    putString("date", date)
                }
            }
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, fragment)
                .commit()

        }

    }

    private fun initButtonState(){
        if(isRecord){
            binding.beforeAutocreateAbleBtn.visibility = View.VISIBLE
            binding.beforeAutocreateDisabledBtn.visibility = View.GONE
        }else{
            binding.beforeAutocreateDisabledBtn.visibility = View.VISIBLE
            binding.beforeAutocreateAbleBtn.visibility = View.GONE
        }
    }

}