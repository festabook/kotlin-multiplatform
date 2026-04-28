package com.daedan.festabook.domain.model

data class Organization(
    val id: Long,
    val organizationName: String,
    val festival: Festival,
)
