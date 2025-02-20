package com.example.mindstone

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

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
    private var currentRatios: Map<String, Float> = emptyMap()

    init {
        setBackgroundResource(R.drawable.ic_status_bar) // ✅ 배경을 코드에서 강제 적용
        clipToOutline = true // ✅ 배경 외곽을 유지
    }


//    // 감정 데이터를 업데이트하는 함수 (비율 기반으로 조정)
//    fun updateEmotions(emotionMap: Map<String, Float>) {
//        val total = emotionMap.values.sum()
//        emotionRatios = if (total > 0) {
//            emotionOrder.map { emotionMap[it] ?: 0f } // ✅ 비율 계산
//        } else {
//            List(emotionOrder.size) { 0f }
//        }
//        Log.d("EmotionStatusBar", "Updated UI Ratios: $emotionRatios")
//        invalidate() // UI 다시 그리기 요청
//    }
// ✅ UI 업데이트 함수 (100% 기준 변환된 감정 비율을 바로 적용)
    fun updateEmotions(normalizedRatios: Map<String, Float>) {
        currentRatios = normalizedRatios // ✅ 직접 저장 (onDraw에서 바로 사용)
        Log.d("EmotionStatusBar", "🎨 Updated UI Ratios: $currentRatios")
        invalidate() // UI 다시 그리기 요청
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barWidth = width.toFloat()
        val barHeight = height.toFloat()
        var startX = 0f

        val total = currentRatios.values.sum()
        if (total == 0f) return // ✅ 감정 데이터가 없으면 그리지 않음

        // ✅ 최신 `normalizedRatios`를 반영하여 UI를 그림
        for (emotion in emotionOrder) {
            val ratio = currentRatios[emotion] ?: 0f // ✅ 감정별 비율 적용 (없으면 0)

            if (ratio > 0) { // 감정 비율이 0보다 클 때만 그림
                val paint = Paint().apply {
                    color = emotionColorMap[emotion] ?: Color.BLACK
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                val widthFraction = barWidth * (ratio / total) // ✅ 비율에 맞게 너비 조정
                canvas.drawRect(startX, 0f, startX + widthFraction, barHeight, paint)
                startX += widthFraction
            }
        }
    }


}


