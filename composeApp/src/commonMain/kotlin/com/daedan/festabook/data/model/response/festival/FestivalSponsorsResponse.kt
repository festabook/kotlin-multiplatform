package com.daedan.festabook.data.model.response.festival

import com.daedan.festabook.domain.model.FestivalSponsor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FestivalSponsorsResponse(
    @SerialName("bannerUrl")
    val bannerUrl: String,
    @SerialName("festivalSponsorId")
    val festivalSponsorId: Int,
    @SerialName("sequence")
    val sequence: Int,
)

fun FestivalSponsorsResponse.toDomain() =
    FestivalSponsor(
        id = festivalSponsorId,
        bannerUrl = bannerUrl,
        sequence = sequence,
    )
