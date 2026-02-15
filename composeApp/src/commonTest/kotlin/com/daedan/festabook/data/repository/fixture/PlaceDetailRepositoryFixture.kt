package com.daedan.festabook.data.repository.fixture

import com.daedan.festabook.data.model.response.place.PlaceDetailResponse
import com.daedan.festabook.data.model.response.place.PlaceGeographyResponse
import com.daedan.festabook.data.model.response.place.TimeTagResponse

fun fakePlaceDetailResponse(placeId: Long) = PlaceDetailResponse(
    id = placeId,
    category = PlaceGeographyResponse.PlaceCategory.BOOTH,
    description = "테스트 장소 설명",
    startTime = "10:00",
    endTime = "18:00",
    host = "주최자",
    location = "테스트 위치",
    placeAnnouncements = emptyList(),
    placeImages =
        listOf(
            PlaceDetailResponse.PlaceImage(
                id = 1L,
                imageUrl = "https://example.com/image.jpg",
                sequence = 1,
            ),
        ),
    title = "테스트 장소",
    timeTags =
        listOf(
            TimeTagResponse(
                name = "오전",
                timeTagId = 1L,
            ),
        ),
)

fun fakePlaceDetailResponseWithNotice(placeId: Long) =  PlaceDetailResponse(
    id = placeId,
    category = PlaceGeographyResponse.PlaceCategory.STAGE,
    description = null,
    startTime = null,
    endTime = null,
    host = null,
    location = null,
    placeAnnouncements =
        listOf(
            PlaceDetailResponse.PlaceAnnouncement(
                id = 1L,
                title = "공지사항 제목1",
                content = "공지사항 내용1",
                createdAt = "2024-01-01T10:00:00",
            ),
            PlaceDetailResponse.PlaceAnnouncement(
                id = 2L,
                title = "공지사항 제목2",
                content = "공지사항 내용2",
                createdAt = "2024-01-02T10:00:00",
            )
        ),
    placeImages = emptyList(),
    title = null,
    timeTags = emptyList(),
)

fun fakePlaceDetailResponseWithImage(placeId: Long) =
    PlaceDetailResponse(
        id = placeId,
        category = PlaceGeographyResponse.PlaceCategory.FOOD_TRUCK,
        description = null,
        startTime = null,
        endTime = null,
        host = null,
        location = null,
        placeAnnouncements = emptyList(),
        placeImages =
            listOf(
                PlaceDetailResponse.PlaceImage(
                    id = 1L,
                    imageUrl = "https://example.com/image1.jpg",
                    sequence = 1,
                ),
                PlaceDetailResponse.PlaceImage(
                    id = 2L,
                    imageUrl = "https://example.com/image2.jpg",
                    sequence = 2,
                ),
            ),
        title = null,
        timeTags = emptyList(),
    )
