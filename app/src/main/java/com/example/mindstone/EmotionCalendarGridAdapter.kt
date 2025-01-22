package com.example.mindstone

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class EmotionCalendarGridAdapter(
    private val context: Context,
    private val dates: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Any = dates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val dateText = view.findViewById<TextView>(R.id.dateText)
        val dateIcon = view.findViewById<ImageView>(R.id.dateIcon)

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
                dateIcon.visibility = View.VISIBLE // 모든 날짜에 기본 아이콘 표시
                dateIcon.setImageResource(R.drawable.btn_empty_emotion_normal) // 기본 아이콘 설정
            }
        }

        return view
    }
}
