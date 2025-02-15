package com.example.mindstone.ui.home.emotion.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class EmotionStatusBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    // 감정별 색상
    private val emotionColorMap = mapOf(
        "화남" to Color.parseColor("#FF8E84"),
        "우울" to Color.parseColor("#EEBFFF"),
        "슬픔" to Color.parseColor("#AEE7FF"),
        "평온" to Color.parseColor("#D9D9D9"),
        "기쁨" to Color.parseColor("#C3FFB7"),
        "설렘" to Color.parseColor("#FF93AC"),
        "행복" to Color.parseColor("#FFF08F")
    )

    // 감정 순서 (부정 → 긍정 순)
    private val emotionOrder = listOf("화남", "우울", "슬픔", "평온", "기쁨", "설렘", "행복")

    // 감정 비율을 저장할 리스트
    private var emotionRatios: List<Float> = emptyList()

    // 감정 데이터를 업데이트하는 함수
    fun updateEmotions(emotionMap: Map<String, Float>) {
        val total = emotionMap.values.sum()
        emotionRatios = emotionOrder.map { emotionMap[it] ?: 0f }.map { it / total }
        invalidate() // 다시 그리기 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barWidth = width.toFloat()
        val barHeight = height.toFloat()
        var startX = 0f

        for (i in emotionRatios.indices) {
            val emotion = emotionOrder[i]
            val paint = Paint().apply {
                color = emotionColorMap[emotion] ?: Color.BLACK // 감정에 맞는 색상 적용
                style = Paint.Style.FILL
            }
            val widthFraction = barWidth * emotionRatios[i]
            canvas.drawRect(startX, 0f, startX + widthFraction, barHeight, paint)
            startX += widthFraction
        }
    }
}

