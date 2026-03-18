package com.daedan.festabook.domain.model

data class FestivalSearchItem(
    val festivalId: Long,
    val organizationName: String,
    val festivalName: String,
    val startDate: String,
    val endDate: String,
)
