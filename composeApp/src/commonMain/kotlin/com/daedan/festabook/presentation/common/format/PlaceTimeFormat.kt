package com.daedan.festabook.presentation.common.format

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalTime?.toFormattedString(): String? {
    val format =
        LocalTime.Format {
            byUnicodePattern("HH:mm")
        }
    return this?.format(format)
}
