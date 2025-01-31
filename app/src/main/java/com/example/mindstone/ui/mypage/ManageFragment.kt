package com.example.mindstone.ui.mypage

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.mindstone.R
import com.example.mindstone.databinding.DialogDatePickerBinding
import com.example.mindstone.databinding.DialogJobPickerBinding
import com.example.mindstone.databinding.DialogMbtiPickerBinding
import com.example.mindstone.databinding.FragmentManageBinding

class ManageFragment : Fragment() {

    private var _binding: FragmentManageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 생년월일 클릭 이벤트
        binding.tvBirth2Manage.setOnClickListener {
            showBirthDialog(binding.tvBirth2Manage.text.toString()) { selectedBirth ->
                binding.tvBirth2Manage.text = selectedBirth
            }
        }

        // MBTI 클릭 이벤트
        binding.tvMbti2Manage.setOnClickListener {
            showMBTIDialog(binding.tvMbti2Manage.text.toString()) { selectedMBTI ->
                binding.tvMbti2Manage.text = selectedMBTI
            }
        }

        // 직업 클릭 이벤트
        binding.tvJob2Manage.setOnClickListener {
            showJobPickerDialog(binding.tvJob2Manage.text.toString()) { selectedJob ->
                binding.tvJob2Manage.text = selectedJob
            }
        }
    }

    private fun showBirthDialog(currentBirth: String, onBirthSelected: (String) -> Unit) {
        val dialogBinding = DialogDatePickerBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.npYear.apply {
            minValue = 1900
            maxValue = 2025
            value = currentBirth.split(".")[0].toIntOrNull() ?: 2000
        }
        dialogBinding.npMonth.apply {
            minValue = 1
            maxValue = 12
            value = currentBirth.split(".")[1].toIntOrNull() ?: 1
        }
        dialogBinding.npDay.apply {
            minValue = 1
            maxValue = 31
            value = currentBirth.split(".")[2].toIntOrNull() ?: 1
        }

        dialogBinding.btnFixBirth.setOnClickListener {
            val selectedBirth = "${dialogBinding.npYear.value}.${dialogBinding.npMonth.value.toString().padStart(2, '0')}.${dialogBinding.npDay.value.toString().padStart(2, '0')}"
            onBirthSelected(selectedBirth)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showMBTIDialog(currentMBTI: String, onMBTISelected: (String) -> Unit) {
        val dialogBinding = DialogMbtiPickerBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        val mbtiOptions = listOf("E", "I", "N", "S", "F", "T", "P", "J")
        val textViews = listOf(
            dialogBinding.tvMbtiE, dialogBinding.tvMbtiI, dialogBinding.tvMbtiN,
            dialogBinding.tvMbtiS, dialogBinding.tvMbtiF, dialogBinding.tvMbtiT,
            dialogBinding.tvMbtiP, dialogBinding.tvMbtiJ
        )

        textViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                onMBTISelected(mbtiOptions[index])
                dialog.dismiss()
            }
        }

        dialogBinding.btnFixMbti.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showJobPickerDialog(currentJob: String, onJobSelected: (String) -> Unit) {
        val dialogBinding = DialogJobPickerBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        val jobOptions = listOf(
            "중학생", "고등학생", "대학생 / 대학원생", "경영 / 관리직", "자유 / 전문직",
            "사무 / 기술직", "기능 / 작업 / 단순노무직", "판매 / 서비스직", "농 / 림 / 어 / 축산업",
            "자영업", "무직", "기타"
        )

        val gridJobs = dialogBinding.gridJobs
        val selectedJobIndex = jobOptions.indexOf(currentJob)

        jobOptions.forEachIndexed { index, job ->
            val textView = TextView(requireContext()).apply {
                text = job
                textSize = 14f
                setPadding(16, 8, 16, 8)
                gravity = Gravity.CENTER
                background = ContextCompat.getDrawable(
                    requireContext(),
                    if (index == selectedJobIndex) R.drawable.background_radius_red else R.drawable.background_radius_gray
                )
                setOnClickListener {
                    gridJobs.children.forEach { it.background = ContextCompat.getDrawable(requireContext(),
                        R.drawable.background_radius_gray
                    ) }
                    background = ContextCompat.getDrawable(requireContext(),
                        R.drawable.background_radius_red
                    )
                    onJobSelected(job)
                }
            }
            gridJobs.addView(textView)
        }

        dialogBinding.btnConfirmJob.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
