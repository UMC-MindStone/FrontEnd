package com.example.mindstone.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mindstone.databinding.DialogLoginBinding

object LoginDialogUtil {

    fun showCustomDialog(
        context: Context,
        message: String,
        positiveText: String,
        negativeText: String?,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        val binding = DialogLoginBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)  // 배경 투명 설정
        dialog.setCancelable(false)  // 다이얼로그 바깥 클릭으로 닫히지 않도록 설정

        binding.tvMessage.text = message
        binding.btnAction1.text = positiveText
        binding.btnAction1.setOnClickListener {
            onPositiveClick?.invoke()
            dialog.dismiss()
        }

        if (negativeText != null) {
            binding.btnAction2.text = negativeText
            binding.btnAction2.setOnClickListener {
                onNegativeClick?.invoke()
                dialog.dismiss()
            }
        } else {
            binding.btnAction2.visibility = View.GONE  // 두 번째 버튼이 필요 없을 때 숨김
        }

        dialog.show()
    }
}
