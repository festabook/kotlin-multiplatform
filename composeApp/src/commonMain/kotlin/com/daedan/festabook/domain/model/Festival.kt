package com.daedan.festabook.domain.model

import kotlinx.datetime.LocalDate

data class Festival(
    val id: Long,
    val festivalName: String,
    val festivalImages: List<Poster>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val festatingVisible: Boolean,
    val sponsors: List<FestivalSponsor>,
    val instagramLink: String?,
    val homepageLink: String?,
)
