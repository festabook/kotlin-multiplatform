package com.daedan.festabook.presentation.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun formatFestivalPeriod(
    start: LocalDate,
    end: LocalDate,
): String =
    if (start.year == end.year) {
        "${start.year}년 ${start.month.number}월 ${start.day}일 ~ " +
            "${end.month.number}월 ${end.day}일"
    } else {
        "${start.year}년 ${start.month.number}월 ${start.day}일 ~ " +
            "${end.year}년 ${end.month.number}월 ${end.day}일"
    }
