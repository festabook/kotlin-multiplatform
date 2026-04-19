package com.daedan.festabook.presentation.placeMap.waitingRegister.model

import com.daedan.festabook.domain.model.PlaceDetail
import com.daedan.festabook.presentation.common.format.toFormattedString
import com.daedan.festabook.presentation.placeMap.model.PlaceCategoryUiModel
import com.daedan.festabook.presentation.placeMap.model.toUiModel

data class WaitingPlaceSummaryUiModel(
    val placeId: Long,
    val title: String?,
    val description: String?,
    val category: PlaceCategoryUiModel,
    val location: String?,
    val host: String?,
    val startTime: String?,
    val endTime: String?,
    val imageUrl: String?,
)

fun PlaceDetail.toWaitingPlaceSummaryUiModel(): WaitingPlaceSummaryUiModel =
    WaitingPlaceSummaryUiModel(
        placeId = place.id,
        title = place.title,
        description = place.description,
        category = place.category.toUiModel(),
        location = place.location,
        host = host,
        startTime = startTime.toFormattedString(),
        endTime = endTime.toFormattedString(),
        imageUrl = sortedImages.firstOrNull()?.imageUrl,
    )
