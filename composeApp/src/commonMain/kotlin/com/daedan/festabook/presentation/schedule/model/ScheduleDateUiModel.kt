package com.daedan.festabook.presentation.schedule.model

import com.daedan.festabook.domain.model.ScheduleDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

data class ScheduleDateUiModel(
    val id: Long,
    val date: String,
)

fun ScheduleDate.toUiModel(): ScheduleDateUiModel =
    ScheduleDateUiModel(
        id = id,
        date = date.toFormattedDateString(),
    )

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDate.toFormattedDateString(): String {
    val format =
        LocalDate.Format {
            byUnicodePattern("M/dd(E)")
        }
    return this.format(format)
}
