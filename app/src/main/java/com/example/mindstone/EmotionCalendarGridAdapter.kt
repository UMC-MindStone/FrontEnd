package com.example.mindstone

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.mindstone.databinding.GridEmotionItemBinding

class EmotionCalendarGridAdapter(
    private val context: Context,
    private val dates: List<String>
) : BaseAdapter() {

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

                // API에서 해당 날짜에 대한 감정 상태 또는 이미지를 받아와서 아이콘 설정
                val emotion = getEmotionForDate(text) // API 호출 예시

                // 감정 상태에 따른 아이콘 설정
                when (emotion) {
                    "happy" -> {
                        dateIcon.setImageResource(R.drawable.ic_happy)
                        // 감정 아이콘일 경우 크기를 24x24로 설정
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "sad" -> {
                        dateIcon.setImageResource(R.drawable.ic_sad)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "calm" -> {
                        dateIcon.setImageResource(R.drawable.ic_calm)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "angry" -> {
                        dateIcon.setImageResource(R.drawable.ic_angry)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "depression" -> {
                        dateIcon.setImageResource(R.drawable.ic_depression)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "joy" -> {
                        dateIcon.setImageResource(R.drawable.ic_joy)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    "romance" -> {
                        dateIcon.setImageResource(R.drawable.ic_romance)
                        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
                        dateIcon.layoutParams.width = sizeInPixels
                        dateIcon.layoutParams.height = sizeInPixels
                    }

                    else -> {
                        dateIcon.setImageResource(R.drawable.btn_empty_emotion_normal) // 기본 아이콘
                    }
                }
                dateIcon.visibility = View.VISIBLE // 아이콘 표시
            }
        }

        return binding.root // 바인딩된 루트 뷰 반환
    }

    // 예시: 특정 날짜에 감정 상태를 받아오는 함수
    fun getEmotionForDate(date: String): String {
        // 일자가 한 자릿수일 때 두 자릿수로 변환
        val dateNumber = date.padStart(2, '0')  // 예: "1" -> "01"
        val fullDate = "2025-01-$dateNumber"    // 예: "2025-01-01"

        val emotion = when (fullDate) {
            "2025-01-01" -> "happy"
            "2025-01-02" -> "sad"
            "2025-01-03" -> "calm"
            "2025-01-04" -> "angry"
            "2025-01-05" -> "depression"
            "2025-01-06" -> "joy"
            "2025-01-07" -> "romance"
            else -> "neutral"
        }
        return emotion
    }


}

