package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDateTime

data class LineupItem(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val performanceAt: LocalDateTime,
)
