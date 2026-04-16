package com.daedan.festabook.presentation.common.format

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

val placeTimeFormat =
    LocalTime.Format {
        hour()
        char(':')
        minute()
    }

fun LocalTime?.toFormattedString(): String? = this?.let { placeTimeFormat.format(it) }
