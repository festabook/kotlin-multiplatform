package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.Festating

data class FestatingUiModel(
    val organizationId: Long,
    val festivalId: Long,
    val deviceId: Long,
)

fun Festating.toUiModel(): FestatingUiModel =
    FestatingUiModel(
        organizationId = organizationId,
        festivalId = festivalId,
        deviceId = deviceId,
    )
