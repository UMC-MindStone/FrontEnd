package com.example.mindstone.ui.emotion

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.mindstone.R
import com.example.mindstone.databinding.GridEmotionItemBinding

class EmotionCalendarGridAdapter(
    private val context: Context,
    private val dates: List<String>
) : BaseAdapter() {
    interface onDateClickListener{
        fun onDateClick(date: String, isRecord: Boolean)
    }

    var listener: onDateClickListener? = null
    private var currentYear = 2025 // 기본값 설정
    private var currentMonth = 1
    private var emotionData: Map<String, String> = emptyMap()

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

                val formattedDate = "$currentYear-${String.format("%02d", currentMonth)}-${String.format("%02d", text.toInt())}"
                updateCurrentYearMonth(parent)
                // API에서 해당 날짜에 대한 감정 상태 또는 이미지를 받아와서 아이콘 설정
                val emotion = emotionData[formattedDate] ?: "." // API 호출 예시
                val isRecord = emotion != "."

                setEmotionIcon(emotion, dateIcon)

                dateIcon.visibility = View.VISIBLE // 아이콘 표시

                binding.root.setOnClickListener {
                    listener?.onDateClick(formattedDate, isRecord)
                    Log.d("Adapter", "Is It Recorded?: $isRecord")
                }
            }
        }

        return binding.root // 바인딩된 루트 뷰 반환
    }


    fun setEmotionData(data: Map<String, String>) {
        emotionData = data
        notifyDataSetChanged() // 데이터 변경 시 새로고침
    }

    private fun setEmotionIcon(emotion: String, imageView: ImageView) {
        val sizeInPixels = (24 * context.resources.displayMetrics.density).toInt()
        imageView.layoutParams.width = sizeInPixels
        imageView.layoutParams.height = sizeInPixels

        when (emotion) {
            "HAPPINESS" -> imageView.setImageResource(R.drawable.ic_happy)
            "SAD" -> imageView.setImageResource(R.drawable.ic_sad)
            "CALM" -> imageView.setImageResource(R.drawable.ic_calm_charac)
            "ANGER" -> imageView.setImageResource(R.drawable.ic_angry)
            "DEPRESSION" -> imageView.setImageResource(R.drawable.ic_depression)
            "JOY" -> imageView.setImageResource(R.drawable.ic_joy)
            "THRILL" -> imageView.setImageResource(R.drawable.ic_romance)
            else -> imageView.setImageResource(R.drawable.btn_empty_emotion_normal)
        }
        imageView.visibility = View.VISIBLE
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

