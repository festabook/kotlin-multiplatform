package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.Organization

data class OrganizationUiModel(
    val id: Long,
    val organizationName: String,
    val festival: FestivalUiModel,
)

fun Organization.toUiModel(): OrganizationUiModel =
    OrganizationUiModel(
        id = id,
        organizationName = organizationName,
        festival = festival.toUiModel(),
    )
