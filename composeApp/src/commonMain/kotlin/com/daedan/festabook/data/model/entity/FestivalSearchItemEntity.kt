package com.daedan.festabook.data.model.entity

import com.daedan.festabook.domain.model.FestivalSearchItem
import kotlinx.serialization.Serializable

@Serializable
data class FestivalSearchItemEntity(
    val festivalId: Long,
    val organizationName: String,
    val festivalName: String,
    val startDate: String,
    val endDate: String,
)

fun FestivalSearchItemEntity.toDomain() =
    FestivalSearchItem(
        festivalId = festivalId,
        organizationName = organizationName,
        festivalName = festivalName,
        startDate = startDate,
        endDate = endDate,
    )

fun FestivalSearchItem.toEntity() =
    FestivalSearchItemEntity(
        festivalId = festivalId,
        organizationName = organizationName,
        festivalName = festivalName,
        startDate = startDate,
        endDate = endDate,
    )
