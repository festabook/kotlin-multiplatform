package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.Poster

data class FestivalPosterUiModel(
    val id: Long,
    val imageUrl: String,
    val sequence: Int,
)

fun Poster.toUiModel(): FestivalPosterUiModel =
    FestivalPosterUiModel(
        id = id,
        imageUrl = imageUrl,
        sequence = sequence,
    )
