package com.daedan.festabook.data.model.response.festival

import com.daedan.festabook.domain.model.Festival
import com.daedan.festabook.domain.model.Organization
import com.daedan.festabook.domain.model.Poster
import com.daedan.festabook.domain.model.toLocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FestivalResponse(
    @SerialName("festivalId")
    val id: Long,
    @SerialName("organizationId")
    val organizationId: Long,
    @SerialName("organizationName")
    val organizationName: String,
    @SerialName("festivalImages")
    val festivalImages: List<FestivalImage>,
    @SerialName("festivalName")
    val festivalName: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("festivalSponsors")
    val festivalSponsors: List<FestivalSponsorsResponse>,
    @SerialName("instagramLink")
    val instagramLink: String?,
    @SerialName("homepageLink")
    val homepageLink: String?,
) {
    @Serializable
    data class FestivalImage(
        @SerialName("festivalImageId")
        val id: Long,
        @SerialName("imageUrl")
        val imageUrl: String,
        @SerialName("sequence")
        val sequence: Int,
    )
}

fun FestivalResponse.toDomain() =
    Organization(
        id = id,
        organizationName = organizationName,
        festival =
            Festival(
                festivalImages = festivalImages.map { it.toDomain() },
                festivalName = festivalName,
                startDate = startDate.toLocalDate(),
                endDate = endDate.toLocalDate(),
                sponsors = festivalSponsors.map { it.toDomain() },
            ),
        instagramLink = instagramLink,
        homepageLink = homepageLink,
    )

fun FestivalResponse.FestivalImage.toDomain() =
    Poster(
        id = id,
        imageUrl = imageUrl,
        sequence = sequence,
    )
