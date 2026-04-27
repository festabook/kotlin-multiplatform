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
    val festivalId: Long,
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
    @SerialName("festatingVisible")
    val festatingVisible: Boolean,
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
        id = 1L, // TODO 우선 하드 코딩 서버 변경되면 연결 필요
        organizationName = organizationName,
        festival =
            Festival(
                id = festivalId,
                festivalImages = festivalImages.map { it.toDomain() },
                festivalName = festivalName,
                startDate = startDate.toLocalDate(),
                endDate = endDate.toLocalDate(),
                festatingVisible = festatingVisible,
            ),
    )

fun FestivalResponse.FestivalImage.toDomain() =
    Poster(
        id = id,
        imageUrl = imageUrl,
        sequence = sequence,
    )
