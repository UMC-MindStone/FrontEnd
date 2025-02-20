package com.example.mindstone

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

    init {
        setBackgroundResource(R.drawable.ic_status_bar) // ✅ 배경을 코드에서 강제 적용
        clipToOutline = true // ✅ 배경 외곽을 유지
    }


    // 감정 데이터를 업데이트하는 함수 (비율 기반으로 조정)
    fun updateEmotions(emotionMap: Map<String, Float>) {
        val total = emotionMap.values.sum()
        emotionRatios = if (total > 0) {
            emotionOrder.map { emotionMap[it] ?: 0f }.map { it / total } // ✅ 비율 계산
        } else {
            List(emotionOrder.size) { 0f }
        }
        invalidate() // UI 다시 그리기 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barWidth = width.toFloat()
        val barHeight = height.toFloat()
        var startX = 0f

        // ✅ 정확한 비율을 반영하여 감정 상태바를 그림
        for (i in emotionRatios.indices) {
            val emotion = emotionOrder[i]
            val ratio = emotionRatios[i] // ✅ 감정별 비율 적용

            if (ratio > 0) { // 감정 비율이 0보다 클 때만 그림
                val paint = Paint().apply {
                    color = emotionColorMap[emotion] ?: Color.BLACK
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                val widthFraction = barWidth * ratio // ✅ 비율에 맞게 너비 조정
                canvas.drawRect(startX, 0f, startX + widthFraction, barHeight, paint)
                startX += widthFraction
            }
        }
    }


}


