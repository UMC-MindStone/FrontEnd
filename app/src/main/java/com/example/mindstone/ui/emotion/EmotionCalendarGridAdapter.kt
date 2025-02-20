package com.example.mindstone.ui.emotion

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.GridEmotionItemBinding

class EmotionCalendarGridAdapter(
    private val context: Context,
    private val dates: List<String>,
) : BaseAdapter() {
    interface onDateClickListener {
        fun onDateClick(date: String, isRecord: Boolean)
    }

    var listener: onDateClickListener? = null
    private var currentYear = 2025 // 기본값 설정
    private var currentMonth = 1

    // 날짜별 감정을 저장할 맵
    private val emotionMap = mutableMapOf<String, String>()

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = GridEmotionItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val dateText = binding.dateText
        val dateIcon = binding.dateIcon

        val text = dates[position]
        dateText.text = text

        if (position < 7) { // 첫 7개는 요일 헤더
            dateText.setTextColor(ContextCompat.getColor(context, R.color.black))
            dateText.setTypeface(null, Typeface.BOLD)
            dateIcon.visibility = View.GONE // 헤더에는 아이콘 숨김
        } else {
            if (text.isEmpty()) {
                dateText.visibility = View.INVISIBLE // 빈 칸 숨김
                dateIcon.visibility = View.GONE
            } else {
                dateText.visibility = View.VISIBLE
                dateText.setTextColor(ContextCompat.getColor(context, R.color.black))

                updateCurrentYearMonth(parent)

                val formattedDate = "$currentYear-${String.format("%02d", currentMonth)}-${String.format("%02d", text.toInt())}"
                val emotion = emotionMap[formattedDate] ?: "neutral" // 감정 값 가져오기
                val isRecord = emotion != "neutral"

                // 감정 상태에 따른 아이콘 설정
                setEmotionIcon(emotion, binding)

                binding.root.setOnClickListener {
                    listener?.onDateClick(formattedDate, isRecord)
                }
            }
        }
        return binding.root
    }

    // 감정 상태에 따라 아이콘 설정
    private fun setEmotionIcon(emotion: String, binding: GridEmotionItemBinding) {
        val dateIcon = binding.dateIcon
        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()

        when (emotion) {
            "happy" -> dateIcon.setImageResource(R.drawable.ic_happy)
            "sad" -> dateIcon.setImageResource(R.drawable.ic_sad)
            "calm" -> dateIcon.setImageResource(R.drawable.ic_calm_charac)
            "angry" -> dateIcon.setImageResource(R.drawable.ic_angry)
            "depression" -> dateIcon.setImageResource(R.drawable.ic_depression)
            "joy" -> dateIcon.setImageResource(R.drawable.ic_joy)
            "romance" -> dateIcon.setImageResource(R.drawable.ic_romance)
            else -> dateIcon.setImageResource(R.drawable.btn_empty_emotion_normal) // 기본 아이콘
        }

        dateIcon.layoutParams.width = sizeInPixels
        dateIcon.layoutParams.height = sizeInPixels
        dateIcon.visibility = View.VISIBLE
    }

    // 특정 날짜의 감정을 설정하는 함수
    fun updateEmotionForDate(date: String, emotion: String) {
        emotionMap[date] = emotion // 날짜에 감정 저장
        notifyDataSetChanged() // UI 갱신
    }

    private fun updateCurrentYearMonth(parent: ViewGroup?) {
        val fragment = (parent?.context as? FragmentActivity)?.supportFragmentManager?.fragments?.find {
            it is EmotionCalendarFragment
        } as? EmotionCalendarFragment

        fragment?.let {
            currentYear = it.currentYear
            currentMonth = it.currentMonth
        }
    }
}

