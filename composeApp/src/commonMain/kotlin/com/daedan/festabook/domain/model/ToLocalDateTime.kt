package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

private val MIN_DATE = LocalDateTime(0, 1, 1, 0, 0)

@OptIn(ExperimentalTime::class)
fun String.toLocalDateTime(): LocalDateTime =
    runCatching {
        LocalDateTime.parse(this)
    }.onFailure {
//        Timber.e(it, "LostItem: 날짜 파싱 실패:${it.message}")
    }.getOrElse {
        MIN_DATE
    }
