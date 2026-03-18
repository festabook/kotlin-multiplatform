package com.daedan.festabook.data.datasource.remote.place

import com.daedan.festabook.data.model.response.festival.FestivalGeographyResponse
import com.daedan.festabook.data.model.response.place.PlaceDetailResponse
import com.daedan.festabook.data.model.response.place.PlaceGeographyResponse
import com.daedan.festabook.data.model.response.place.PlaceResponse
import com.daedan.festabook.data.model.response.place.TimeTagResponse
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

val FAKE_HTTP_RESPONSE =
    mock<HttpResponse> {
        every { status } returns HttpStatusCode.OK
    }

val FAKE_TIME_TAGS: List<TimeTagResponse> =
    listOf(
        TimeTagResponse(
            name = "오전",
            timeTagId = 1L,
        ),
        TimeTagResponse(
            name = "오후",
            timeTagId = 2L,
        ),
        TimeTagResponse(
            name = "저녁",
            timeTagId = 3L,
        ),
    )

val FAKE_PLACE_DETAIL: PlaceDetailResponse =
    PlaceDetailResponse(
        id = 1L,
        category = PlaceGeographyResponse.PlaceCategory.BOOTH,
        description = "테스트 부스 설명",
        startTime = "10:00",
        endTime = "18:00",
        host = "주최자",
        location = "A구역",
        placeAnnouncements =
            listOf(
                PlaceDetailResponse.PlaceAnnouncement(
                    id = 1L,
                    title = "공지사항",
                    content = "공지 내용",
                    createdAt = "2024-01-01T10:00:00",
                ),
            ),
        placeImages =
            listOf(
                PlaceDetailResponse.PlaceImage(
                    id = 1L,
                    imageUrl = "https://example.com/image.jpg",
                    sequence = 1,
                ),
            ),
        title = "테스트 부스",
        timeTags =
            listOf(
                TimeTagResponse(
                    name = "오전",
                    timeTagId = 1L,
                ),
            ),
    )

val FAKE_PLACES: List<PlaceResponse> =
    listOf(
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

val FAKE_FESTIVAL_GEOGRAPHY: FestivalGeographyResponse =
    FestivalGeographyResponse(
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

val FAKE_PLACE_GEOGRAPHIES: List<PlaceGeographyResponse> =
    listOf(
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

// ========== Mock Responses ==========

@Suppress("UNCHECKED_CAST")
val FAKE_TIME_TAG_RESPONSE: Response<List<TimeTagResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_TIME_TAGS,
    ) as Response<List<TimeTagResponse>>

@Suppress("UNCHECKED_CAST")
val FAKE_PLACE_DETAIL_RESPONSE: Response<PlaceDetailResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_PLACE_DETAIL,
    ) as Response<PlaceDetailResponse>

@Suppress("UNCHECKED_CAST")
val FAKE_PLACES_RESPONSE: Response<List<PlaceResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_PLACES,
    ) as Response<List<PlaceResponse>>

@Suppress("UNCHECKED_CAST")
val FAKE_FESTIVAL_GEOGRAPHY_RESPONSE: Response<FestivalGeographyResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_FESTIVAL_GEOGRAPHY,
    ) as Response<FestivalGeographyResponse>

@Suppress("UNCHECKED_CAST")
val FAKE_PLACE_GEOGRAPHIES_RESPONSE: Response<List<PlaceGeographyResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_PLACE_GEOGRAPHIES,
    ) as Response<List<PlaceGeographyResponse>>
