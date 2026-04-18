package com.daedan.festabook.presentation.placeMap.model

import com.daedan.festabook.domain.model.Place

data class PlaceUiModel(
    val id: Long,
    val imageUrl: String?,
    val category: PlaceCategoryUiModel,
    val title: String?,
    val description: String?,
    val location: String?,
    val isBookmarked: Boolean = false,
    val timeTagId: List<Long>,
)

fun Place.toUiModel(): PlaceUiModel =
    PlaceUiModel(
        id = id,
        imageUrl = imageUrl,
        category = category.toUiModel(),
        title = title,
        description = description,
        location = location,
        timeTagId =
            timeTags.map {
                it.timeTagId
            },
    )
