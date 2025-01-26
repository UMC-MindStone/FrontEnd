package com.example.mindstone

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.mindstone.databinding.GridHabitItemBinding

class HabitCalendarGridAdapter(
    private val context: Context,
    private val dates: List<String>

) : BaseAdapter() {
    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = GridHabitItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val dateProgress = binding.datetextFl
        val dateText = binding.dateText
        val dateProgessBar = binding.dateProgress
        val subText = binding.subText

        val text = dates[position]
        dateText.text = text

        if (position < 7) { // 첫 7개는 요일 헤더
            dateText.setTextColor(ContextCompat.getColor(context, R.color.black))
            dateText.setTypeface(null, Typeface.BOLD)
            dateProgessBar.visibility = View.GONE
            subText.visibility = View.GONE // 요일 헤더는 보조 텍스트 없음
        } else {
            if (text.isEmpty()) {
                dateText.visibility = View.INVISIBLE
                dateProgress.visibility = View.GONE
                subText.visibility = View.GONE
            } else {
                // 보조 텍스트 추가
                subText.text = "1/3" // 원하는 텍스트로 설정
                subText.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}