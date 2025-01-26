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
    private val dates: List<String>,
    private val onDateClick: (String) -> Unit
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
                val denominator = getTotalHabit()
                val numerator = getDoneHabit(text)

                if (numerator == 0) {
                    dateProgessBar.visibility = View.GONE
                    subText.visibility = View.VISIBLE
                    subText.text = "${numerator}/${denominator}"
                } else {
                    // float으로 계산
                    val progress = (numerator.toFloat() / denominator.toFloat()) * 100
                    dateProgessBar.progress = progress.toInt()
                    subText.visibility = View.VISIBLE
                    subText.text = "${numerator}/${denominator}"
                }
            }
        }
        binding.root.setOnClickListener {
            //빈 날짜는 무시
            if (text.isNotEmpty()) {
                onDateClick(text)
            }
        }

        return binding.root
    }

    // 총 해야 할 일
    fun getTotalHabit(): Int {
        val denominator = 3 //예시 3
        return denominator
    }

    // 해당 날짜에 완료한 일
    fun getDoneHabit(date: String): Int {
        val dateNumber = date.padStart(2, '0')
        val fullDate = "2025-01-$dateNumber"


        //예시 데이터
        return when (fullDate) {
            "2025-01-01" -> 1
            "2025-01-02" -> 2
            "2025-01-03" -> 3
            "2025-01-04" -> 1
            "2025-01-05" -> 2
            "2025-01-06" -> 3
            "2025-01-07" -> 1
            else -> 0
        }
    }
}
