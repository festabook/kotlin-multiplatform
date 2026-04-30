package com.daedan.festabook.viewModel.home

import com.daedan.festabook.domain.model.Festival
import com.daedan.festabook.domain.model.LineupItem
import com.daedan.festabook.domain.model.Organization
import com.daedan.festabook.domain.model.Poster
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

val FAKE_ORGANIZATION =
    Organization(
        id = 1,
        organizationName = "하버드 대학교",
        festival =
            Festival(
                id = 1,
                festivalName = "하버드 대동제",
                festivalImages =
                    listOf(
                        Poster(
                            id = 1,
                            imageUrl = "",
                            sequence = 1,
                        ),
                    ),
                startDate = LocalDate(2025, 1, 1),
                endDate = LocalDate(2025, 1, 3),
                sponsors = emptyList(),
                instagramLink = null,
                homepageLink = null,
                festatingVisible = true,
            ),
    )

val FAKE_LINEUP =
    listOf(
        LineupItem(
            id = 1L,
            imageUrl = "aa.com",
            name = "SINGER",
            performanceAt = LocalDateTime(2025, 9, 16, 0, 0, 0),
        ),
    )
