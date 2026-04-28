package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.FestivalSponsor

data class FestivalSponsorUiModel(
    val id: Int,
    val bannerUrl: String,
    val sequence: Int,
)

fun FestivalSponsor.toUiModel(): FestivalSponsorUiModel =
    FestivalSponsorUiModel(
        id = id,
        bannerUrl = bannerUrl,
        sequence = sequence,
    )