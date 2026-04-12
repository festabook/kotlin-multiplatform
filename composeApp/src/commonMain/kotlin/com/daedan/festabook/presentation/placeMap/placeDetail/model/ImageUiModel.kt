package com.daedan.festabook.presentation.placeMap.placeDetail.model

import com.daedan.festabook.domain.model.PlaceDetailImage
import kotlinx.serialization.Serializable

@Serializable
data class ImageUiModel(
    val url: String? = null,
    val id: Long = -1,
) {
    companion object {}
}

fun PlaceDetailImage.toUiModel() =
    ImageUiModel(
        url = imageUrl,
        id = id,
    )
