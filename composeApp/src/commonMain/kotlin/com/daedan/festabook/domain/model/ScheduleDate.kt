package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDate

data class ScheduleDate(
    val id: Long,
    val date: LocalDate,
)

fun String.toLocalDate(): LocalDate = LocalDate.parse(this)
