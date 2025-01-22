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
        paint.style = Paint.Style.FILL
        // 초기 값들 (예시로 7개의 값)
        values.add(1f)
        values.add(2f)
        values.add(3f)
        values.add(4f)
        values.add(5f)
        values.add(6f)
        values.add(7f)
    }

    // 수치를 외부에서 설정할 수 있는 함수
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

        // 최대값 7로 고정 (이 값이 기준)
        val maxValue = 7f

        // colors.xml에 정의된 색상 목록을 ContextCompat로 가져옴
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
            val value = values[i]

            // 값에 비례한 막대 높이 계산 (maxValue = 7로 고정)
            val barHeight = (value / maxValue) * height

            // 막대 색상 설정
            paint.color = colors[i]

            // 막대 위치 계산 (양쪽 끝에 공백을 적용)
            val left = (i * (barWidth / 0.9f)) + padding
            val top = (height - barHeight)
            val right = (left + barWidth)
            val bottom = height.toFloat()

            // 모서리를 둥글게 그리기 위해 RectF 객체 생성
            val rectF = RectF(left, top, right, bottom)

            // 막대 그리기 (모서리를 둥글게)
            canvas.drawRoundRect(rectF, 5f, 5f, paint)
        }
    }
}


