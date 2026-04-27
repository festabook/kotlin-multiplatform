package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.Festival
import kotlinx.datetime.LocalDate

data class FestivalUiModel(
    val id: Long,
    val festivalName: String,
    val festivalImages: List<FestivalPosterUiModel>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val festatingVisible: Boolean,
)

fun Festival.toUiModel(): FestivalUiModel =
    FestivalUiModel(
        id = id,
        festivalName = festivalName,
        festivalImages = festivalImages.map { it.toUiModel() },
        startDate = startDate,
        endDate = endDate,
        festatingVisible = festatingVisible,
    )
