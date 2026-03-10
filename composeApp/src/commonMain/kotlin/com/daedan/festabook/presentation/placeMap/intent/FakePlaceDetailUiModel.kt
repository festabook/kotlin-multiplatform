package com.daedan.festabook.presentation.placeMap.intent

import com.daedan.festabook.domain.model.PlaceDetail
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.model.toUiModel

// 의존성 충돌 방지용 임시 model입니다
data class FakePlaceDetailUiModel(
    val place: PlaceUiModel,
)

fun PlaceDetail.toUiModel(): FakePlaceDetailUiModel =
    FakePlaceDetailUiModel(
        place = this.place.toUiModel(),
    )
