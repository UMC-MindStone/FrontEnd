package com.example.mindstone.ui.habit

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.databinding.FragmentHabitPickerBinding
import com.example.mindstone.ui.habit.viewmodel.HabitCalendarViewModel

class HabitPickerFragment(
    private val onHabitSelected: (habitId: Long, habitName: String) -> Unit
) : DialogFragment() {

    private var _binding: FragmentHabitPickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitViewModel: HabitCalendarViewModel
    private var selectedHabitId: Long = -1  // ✅ 선택된 습관 ID 저장
    private var selectedHabit: String = ""  // ✅ 선택된 습관 이름 저장

    var onDismissListener: (() -> Unit)? = null

    companion object {
        fun newInstance(onHabitSelected: (habitId: Long, habitName: String) -> Unit): HabitPickerFragment {
            return HabitPickerFragment(onHabitSelected)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitViewModel = ViewModelProvider(requireActivity()).get(HabitCalendarViewModel::class.java)

        // ✅ habitTotalResponse 데이터 구독
        habitViewModel.habitTotalData.observe(viewLifecycleOwner) { habitTotalResponse ->
            habitTotalResponse?.let {
                val habitList = it.result  // ✅ 습관 리스트 가져오기
                val habitTitles = habitList.map { habit -> habit.title }.toTypedArray()

                val habitPicker = binding.habitPickerHabitNp
                habitPicker.minValue = 0
                habitPicker.maxValue = habitTitles.size - 1
                habitPicker.displayedValues = habitTitles
                habitPicker.wrapSelectorWheel = false  // ✅ 무한 스크롤 방지

                // ✅ 초기값 설정 (첫 번째 습관 ID & 이름)
                selectedHabitId = habitList[0].habitId
                selectedHabit = habitTitles[0]

                // ✅ 선택된 값 변경 감지
                habitPicker.setOnValueChangedListener { _, _, newVal ->
                    selectedHabitId = habitList[newVal].habitId
                    selectedHabit = habitTitles[newVal]
                }

                // ✅ 확인 버튼 클릭 시 선택된 값 전달 (ID & 이름)
                binding.habitPickerCheckTv.setOnClickListener {
                    onHabitSelected(selectedHabitId, selectedHabit)
                    dismiss()
                }
            }
        }

        // ✅ 데이터 로딩 시작
        habitViewModel.fetchTotalHabit()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
