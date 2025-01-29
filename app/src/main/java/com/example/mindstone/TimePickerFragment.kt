package com.example.mindstone

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mindstone.databinding.FragmentTimePickerBinding

class TimePickerDialogFragment(
    private val onTimeSelected: (timeText: String) -> Unit
) : DialogFragment() {

    private var _binding: FragmentTimePickerBinding? = null
    private val binding get() = _binding!!

    private var startTime: String = ""
    private var endTime: String = ""

    var onDismissListener: (() -> Unit)? = null  // ⬅ 다이얼로그 닫힐 때 실행할 리스너 추가

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePickerNextTv.visibility = View.VISIBLE
        binding.timePickerCheckTv.visibility = View.GONE
        val noon = binding.timePickerNoonNp
        val hour = binding.timePickerHourNp
        val minute = binding.timePickerMinuteNp

        noon.minValue = 0
        noon.maxValue = 1
        noon.displayedValues = arrayOf("오전", "오후")

        hour.minValue = 0
        hour.maxValue = 11

        minute.minValue = 0
        minute.maxValue = 59

        var startNoon = 0
        var startHour = 0
        var startMinute = 0
        var endNoon = 0
        var endHour = 0
        var endMinute = 0

        // Next 버튼 클릭 리스너
        binding.timePickerNextTv.setOnClickListener {
            startNoon = binding.timePickerNoonNp.value
            startHour = binding.timePickerHourNp.value
            startMinute = binding.timePickerMinuteNp.value

            // 12시간 형식을 24시간 형식으로 변환
            startHour = convertTo24Hour(startNoon, startHour)
            // 시작 시간 저장
            startTime = String.format("%02d:%02d", startHour, startMinute)

            // 버튼 전환
            binding.timePickerCheckTv.visibility = View.VISIBLE
        }

        // Check 버튼 클릭 리스너
        binding.timePickerCheckTv.setOnClickListener {
            endNoon = binding.timePickerNoonNp.value
            endHour = binding.timePickerHourNp.value
            endMinute = binding.timePickerMinuteNp.value

            // 12시간 형식을 24시간 형식으로 변환
            endHour = convertTo24Hour(endNoon, endHour)

            if (startHour > endHour || (startHour == endHour && startMinute > endMinute)) {
                Toast.makeText(context, "시작 시간이 끝나는 시간보다 늦을 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                // 끝나는 시간 저장
                endTime = String.format("%02d:%02d", endHour, endMinute)

                val timeText = "$startTime-$endTime"
                onTimeSelected(timeText)

                // 다이얼로그 닫기
                dismiss()
            }
        }
    }

    private fun convertTo24Hour(noon: Int, hour: Int): Int {
        return if (noon == 0) {
            hour
        } else {
            hour + 12
        }
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
