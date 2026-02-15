package com.daedan.festabook.data.repository.fixture

import com.daedan.festabook.data.model.response.festival.FestivalGeographyResponse
import com.daedan.festabook.data.model.response.place.PlaceGeographyResponse
import com.daedan.festabook.data.model.response.place.PlaceResponse
import com.daedan.festabook.data.model.response.place.TimeTagResponse

val FAKE_PLACE_LIST_RESPONSE = listOf(
    PlaceResponse(
        id = 1L,
        title = "부스 1",
        category = PlaceGeographyResponse.PlaceCategory.BOOTH,
        description = "부스 설명",
        imageUrl = "https://example.com/booth1.jpg",
        location = "A구역",
        timeTags =
            listOf(
                TimeTagResponse(name = "오전", timeTagId = 1L),
            ),
    ),
    PlaceResponse(
        id = 2L,
        title = "푸드트럭 1",
        category = PlaceGeographyResponse.PlaceCategory.FOOD_TRUCK,
        description = "푸드트럭 설명",
        imageUrl = "https://example.com/foodtruck1.jpg",
        location = "B구역",
        timeTags =
            listOf(
                TimeTagResponse(name = "오후", timeTagId = 2L),
            ),
    ),
)

val FAKE_FESTIVAL_GEOGRAPHY_RESPONSE = FestivalGeographyResponse(
    zoom = 15,
    centerCoordinate =
        FestivalGeographyResponse.CenterCoordinate(
            latitude = 37.5665,
            longitude = 126.9780,
        ),
    polygonHoleBoundary =
        listOf(
            FestivalGeographyResponse.PolygonHoleBoundary(
                latitude = 37.5660,
                longitude = 126.9775,
            ),
            FestivalGeographyResponse.PolygonHoleBoundary(
                latitude = 37.5670,
                longitude = 126.9785,
            ),
        ),
)

val FAKE_PLACE_GEOGRAPHY_RESPONSE = listOf(
    PlaceGeographyResponse(
        category = PlaceGeographyResponse.PlaceCategory.BOOTH,
        id = 1L,
        markerCoordinate =
            PlaceGeographyResponse.MarkerCoordinate(
                latitude = 37.5665,
                longitude = 126.9780,
            ),
        title = "부스 1",
        timeTags =
            listOf(
                TimeTagResponse(name = "오전", timeTagId = 1L),
            ),
    ),
    PlaceGeographyResponse(
        category = PlaceGeographyResponse.PlaceCategory.STAGE,
        id = 2L,
        markerCoordinate =
            PlaceGeographyResponse.MarkerCoordinate(
                latitude = 37.5670,
                longitude = 126.9785,
            ),
        title = "메인 스테이지",
        timeTags =
            listOf(
                TimeTagResponse(name = "저녁", timeTagId = 3L),
            ),
    ),
)
