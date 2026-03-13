package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDateTime

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
