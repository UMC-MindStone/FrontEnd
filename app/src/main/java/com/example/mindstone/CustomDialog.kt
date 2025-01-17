package com.example.mindstone

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.example.mindstone.databinding.DialogLoginBinding

// 재활용 가능한 다이얼로그
class CustomDialog(
    context: Context,
    private val message: String,
    private val button1Text: String,
    private val button2Text: String?,
    private val button1Action: (() -> Unit)? = null,
    private val button2Action: (() -> Unit)? = null
) : Dialog(context) {

    private lateinit var binding: DialogLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 처리

        // 다이얼로그 메시지 설정
        binding.tvMessage.text = message

        // 첫 번째 버튼
        binding.btnAction1.text = button1Text
        binding.btnAction1.setOnClickListener {
            button1Action?.invoke()
            dismiss() // 다이얼로그 닫기
        }

        // 두 번째 버튼 (옵션)
        if (button2Text != null) {
            binding.btnAction2.text = button2Text
            binding.btnAction2.setOnClickListener {
                button2Action?.invoke()
                dismiss()
            }
        } else {
            binding.btnAction2.visibility = View.GONE // 버튼이 필요 없으면 숨김
        }
    }
}
