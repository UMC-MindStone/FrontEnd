package com.example.mindstone

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CustomBarChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint: Paint = Paint()
    private val values: MutableList<Float> = mutableListOf()

    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL

        // 7개의 예시 데이터
        values.add(50f)
        values.add(100f)
        values.add(150f)
        values.add(200f)
        values.add(250f)
        values.add(300f)
        values.add(350f)
    }

    fun setValues(newValues: List<Float>) {
        if (newValues.size == 7) {  // 7개의 값만 받도록 설정
            values.clear()
            values.addAll(newValues)
            invalidate()  // 값이 변경되면 다시 그리기
        } else {
            throw IllegalArgumentException("There must be exactly 7 values")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 그래프의 폭과 높이 구하기
        val width = width
        val height = height

        // 양쪽 끝 공백을 위한 여유 공간 설정
        val padding = width * 0.1f // 화면 폭의 10%를 양쪽 끝 공백으로 설정
        val availableWidth = width - 2 * padding // 공백을 제외한 너비

        // 막대의 두께 설정: 공백을 제외한 너비를 막대 수로 나누고, 조금의 간격을 위해 값을 줄임
        val barWidth = (availableWidth / values.size.toFloat()) * 0.9f  // 0.9배로 크기 약간 줄이기 (틈을 위한)

        val maxValue = values.maxOrNull() ?: 0f

        val colors = listOf(
            ContextCompat.getColor(context, R.color.depression),
            ContextCompat.getColor(context, R.color.angry),
            ContextCompat.getColor(context, R.color.sad),
            ContextCompat.getColor(context, R.color.calm),
            ContextCompat.getColor(context, R.color.joy),
            ContextCompat.getColor(context, R.color.happy),
            ContextCompat.getColor(context, R.color.romance)
        )

        // 막대 그래프 그리기
        for (i in values.indices) {
            paint.color = colors[i]
            val value = values[i]
            // 높이를 비례적으로 조정
            val barHeight = (value / maxValue) * height

            // 막대 위치 계산 (양쪽 끝에 공백을 적용)
            val left = (i * (barWidth / 0.9f)) + padding // 간격을 고려하여 위치 조정
            val top = (height - barHeight)
            val right = (left + barWidth)
            val bottom = height.toFloat()

            // 모서리를 둥글게 그리기 위해 RectF 객체 생성
            val rectF = RectF(left, top, right, bottom)

            // 막대 그리기 (모서리를 둥글게)
            canvas.drawRoundRect(rectF, 5f, 5f, paint) // 20f는 둥근 정도를 나타냄
        }
    }
}


