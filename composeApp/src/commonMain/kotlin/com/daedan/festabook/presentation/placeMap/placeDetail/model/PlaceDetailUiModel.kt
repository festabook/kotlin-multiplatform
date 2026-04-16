package com.daedan.festabook.presentation.placeMap.placeDetail.model

import com.daedan.festabook.domain.model.PlaceDetail
import com.daedan.festabook.presentation.common.format.toFormattedString
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.news.notice.model.toUiModel
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.model.toUiModel

data class PlaceDetailUiModel(
    val place: PlaceUiModel,
    val notices: List<NoticeUiModel>,
    val host: String?,
    val startTime: String?,
    val endTime: String?,
    val images: List<ImageUiModel>,
    val isWaitingActive: Boolean = false,
)

fun PlaceDetail.toUiModel() =
    PlaceDetailUiModel(
        place = place.toUiModel(),
        notices = sortedNotices.map { it.toUiModel() },
        host = host,
        startTime = startTime.toFormattedString(),
        endTime = endTime.toFormattedString(),
        images = sortedImages.map { it.toUiModel() },
        isWaitingActive = isWaitingActive,
    )
