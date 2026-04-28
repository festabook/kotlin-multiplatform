package com.daedan.festabook.data.model.response.festival

import com.daedan.festabook.domain.model.FestivalSponsor
import kotlinx.serialization.Serializable

@Serializable
data class FestivalSponsorsResponse(
    val bannerUrl: String,
    val festivalSponsorId: Int,
    val sequence: Int,
)

fun FestivalSponsorsResponse.toDomain() =
    FestivalSponsor(
        id = festivalSponsorId,
        bannerUrl = bannerUrl,
        sequence = sequence,
    )
