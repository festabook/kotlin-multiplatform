package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

sealed interface Lost {
    data class Item(
        val lostItemId: Long,
        val imageUrl: String,
        val storageLocation: String,
        val status: LostItemStatus,
        val createdAt: LocalDateTime,
    ) : Lost

    data class Guide(
        val guide: String,
    ) : Lost
}

private val MIN_DATE = LocalDateTime(0, 1, 1, 0, 0)

fun String.toLocalDateTime(): LocalDateTime =
    runCatching {
        Instant.parse(this).toLocalDateTime(TimeZone.UTC)
    }.onFailure {
//        Timber.e(it, "LostItem: 날짜 파싱 실패:${it.message}")
    }.getOrElse {
        MIN_DATE
    }
