package com.daedan.festabook.data.model.response

import com.daedan.festabook.domain.model.FestivalSearchItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FestivalSearchResponse(
    @SerialName("festivalId")
    val festivalId: Long,
    @SerialName("universityName")
    val organizationName: String,
    @SerialName("festivalName")
    val festivalName: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
)

fun FestivalSearchResponse.toDomain() =
    FestivalSearchItem(
        festivalId = festivalId,
        organizationName = organizationName,
        festivalName = festivalName,
        startDate = startDate,
        endDate = endDate,
    )
