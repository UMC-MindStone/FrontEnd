package com.example.mindstone.domain.entity

data class EmotionNoteStressRequest(
    val emotion: String,       // "ANGER", "DEPRESSION", "SAD"
    val steressReson_id: Int,  // 나중에 스웨거 오타 변경되면 수정 필요 (stressReason_id)
    val emotionFigure: Int,
    val content: String,
    val time: String,
    val recommend: Boolean
)

