package com.example.mindstone.ui.habit

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.mindstone.R
import com.example.mindstone.databinding.GridHabitItemBinding

class HabitCalendarGridAdapter(
    private val context: Context,
    private val dates: List<Pair<String, Int>>, // 날짜 + 완료한 습관 개수
    private val onDateClick: (String) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = GridHabitItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val dateProgress = binding.datetextFl
        val dateText = binding.dateText
        val dateProgressBar = binding.dateProgress
        val subText = binding.subText

        val (date, completedHabits) = dates[position]
        dateText.text = date

        if (position < 7) { // ✅ 첫 7개는 요일 헤더
            dateText.setTextColor(ContextCompat.getColor(context, R.color.black))
            dateText.setTypeface(null, Typeface.BOLD)
            dateProgressBar.visibility = View.GONE
            subText.visibility = View.GONE // 요일 헤더는 보조 텍스트 없음
        } else {
            if (date.isEmpty()) { // ✅ 빈 칸 처리
                dateText.visibility = View.INVISIBLE
                dateProgress.visibility = View.GONE
                subText.visibility = View.GONE
            } else {
                val totalHabits = getTotalHabit() // ✅ 하루에 해야 할 총 습관 개수
                val doneHabits = completedHabits // ✅ 완료한 습관 개수

                if (doneHabits == 0) {
                    dateProgressBar.visibility = View.GONE
                    subText.visibility = View.VISIBLE
                    subText.text = "$doneHabits/$totalHabits"
                } else {
                    // ✅ 진행률 계산 (float으로 변환 후 %)
                    val progress = (doneHabits.toFloat() / totalHabits.toFloat()) * 100
                    dateProgressBar.progress = progress.toInt()
                    subText.visibility = View.VISIBLE
                    subText.text = "$doneHabits/$totalHabits"
                }
            }
        }

        binding.root.setOnClickListener {
            if (date.isNotEmpty()) { // ✅ 빈 날짜 무시
                onDateClick(date)
            }
        }

        return binding.root
    }

    // ✅ 하루에 해야 할 총 습관 개수 (이 값을 서버에서 가져올 수도 있음)
    fun getTotalHabit(): Int {
        return 3 // 예제: 하루 3개의 습관 수행 목표
    }
}
