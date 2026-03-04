package com.daedan.festabook.presentation.schedule.model

import com.daedan.festabook.domain.model.ScheduleDate
import kotlinx.datetime.LocalDate

data class ScheduleDateUiModel(
    val id: Long,
    val date: LocalDate,
)

fun ScheduleDate.toUiModel(): ScheduleDateUiModel =
    ScheduleDateUiModel(
        id = id,
        date = date,
    )
