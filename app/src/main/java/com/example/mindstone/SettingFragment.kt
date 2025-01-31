package com.example.mindstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.databinding.DialogTimePickerBinding
import com.example.mindstone.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val timeList = mutableListOf<String>() // 알림 시간 리스트
    private lateinit var adapter: TimeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        adapter = TimeListAdapter(
            times = timeList,
            onTimeClick = { position, time ->
                showEditTimePickerDialog(position, time)
            },
            onAddClick = {
                showAddTimePickerDialog()
            }
        )
        binding.rvAlarmTimes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAlarmTimes.adapter = adapter

        // 전체 알림 Switch 동작 설정
        binding.swAlarm1Setting.setOnCheckedChangeListener { _, isChecked ->
            setAllAlarmsEnabled(isChecked)
        }
    }

    private fun setAllAlarmsEnabled(isEnabled: Boolean) {
        binding.swAlarm2Setting.isEnabled = isEnabled
        binding.swAlarm3Setting.isEnabled = isEnabled
        binding.llTimeSetting.isEnabled = isEnabled
    }

    private fun showAddTimePickerDialog() {
        val dialogBinding = DialogTimePickerBinding.inflate(LayoutInflater.from(requireContext()))

        initializeNumberPickers(dialogBinding)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnAddTime.text = "추가"
        dialogBinding.btnAddTime.setOnClickListener {
            val formattedTime = getSelectedTime(dialogBinding)
            addTime(formattedTime)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditTimePickerDialog(position: Int, time: String) {
        val dialogBinding = DialogTimePickerBinding.inflate(LayoutInflater.from(requireContext()))

        initializeNumberPickers(dialogBinding, time)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnAddTime.text = "수정"
        dialogBinding.btnAddTime.setOnClickListener {
            val updatedTime = getSelectedTime(dialogBinding)
            updateTime(position, updatedTime)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initializeNumberPickers(dialogBinding: DialogTimePickerBinding, time: String? = null) {
        dialogBinding.npAmPm.minValue = 0
        dialogBinding.npAmPm.maxValue = 1
        dialogBinding.npAmPm.displayedValues = arrayOf("오전", "오후")

        dialogBinding.npHour.minValue = 1
        dialogBinding.npHour.maxValue = 12

        val minutesArray = Array(12) { i -> String.format("%02d", i * 5) }
        dialogBinding.npMinute.minValue = 0
        dialogBinding.npMinute.maxValue = minutesArray.size - 1
        dialogBinding.npMinute.displayedValues = minutesArray

        time?.let {
            val parts = it.split(" ")
            val amPmIndex = if (parts[0] == "오전") 0 else 1
            val hour = parts[1].split(":")[0].toInt()
            val minute = parts[1].split(":")[1].toInt()

            dialogBinding.npAmPm.value = amPmIndex
            dialogBinding.npHour.value = hour
            dialogBinding.npMinute.value = minute / 5
        }
    }

    private fun getSelectedTime(dialogBinding: DialogTimePickerBinding): String {
        val amPm = dialogBinding.npAmPm.displayedValues[dialogBinding.npAmPm.value]
        val hour = dialogBinding.npHour.value
        val minute = dialogBinding.npMinute.displayedValues[dialogBinding.npMinute.value]
        return "$amPm $hour:$minute"
    }

    private fun addTime(time: String) {
        timeList.add(time)
        adapter.notifyDataSetChanged()
    }

    private fun updateTime(position: Int, updatedTime: String) {
        timeList[position] = updatedTime
        adapter.notifyItemChanged(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
