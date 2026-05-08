package com.daedan.festabook.presentation.explore.model

import com.daedan.festabook.domain.model.FestivalSearchItem

data class SearchResultUiModel(
    val festivalId: Long,
    val universityName: String,
    val festivalName: String,
)

fun FestivalSearchItem.toUiModel(): SearchResultUiModel =
    SearchResultUiModel(
        festivalId = festivalId,
        universityName = organizationName,
        festivalName = festivalName.replace("\n", " "),
    )

fun SearchResultUiModel.toDomain(): FestivalSearchItem =
    FestivalSearchItem(
        festivalId = festivalId,
        organizationName = universityName,
        festivalName = festivalName,
        startDate = "",
        endDate = "",
    )
