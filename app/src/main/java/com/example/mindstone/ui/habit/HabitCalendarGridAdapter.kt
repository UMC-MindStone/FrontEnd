package com.example.mindstone.ui.habit

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.mindstone.R
import com.example.mindstone.databinding.GridHabitItemBinding
import com.example.mindstone.domain.entity.DailyRecord

class HabitCalendarGridAdapter(
    private val context: Context,
    private val dates: List<Any>, // ✅ 요일 헤더 + DailyRecord 리스트
    private val onDateClick: (String) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = GridHabitItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val dateText = binding.dateText
        val dateProgressBar = binding.dateProgress
        val subText = binding.subText

        val item = dates[position]

        if (item is String) { // ✅ 요일 헤더 처리
            dateText.text = item
            dateText.setTextColor(ContextCompat.getColor(context, R.color.black))
            dateText.setTypeface(null, Typeface.BOLD)
            dateProgressBar.visibility = View.GONE
            subText.visibility = View.GONE
        } else if (item is DailyRecord) { // ✅ 날짜 처리
            val day = item.day.toString()
            val completedHabits = item.completedHabits
            val totalHabits = item.totalHabits

            if (item.day == 0) { // ✅ 빈 칸 처리
                dateText.visibility = View.INVISIBLE
                dateProgressBar.visibility = View.GONE
                subText.visibility = View.GONE
            } else {
                dateText.text = day
                if (totalHabits == 0) {
                    dateProgressBar.visibility = View.GONE
                    subText.visibility = View.VISIBLE
                    subText.text = "0/0"
                } else {
                    val progress = (completedHabits.toFloat() / totalHabits.toFloat()) * 100
                    dateProgressBar.progress = progress.toInt()
                    subText.visibility = View.VISIBLE
                    Log.d("Calendar", "$completedHabits / $totalHabits")
                    subText.text = "$completedHabits/$totalHabits"
                }
            }

            binding.root.setOnClickListener {
                if (item.day != 0) {
                    onDateClick(day)
                }
            }
        }

        return binding.root
    }
}

