package com.daedan.festabook.placeMap.placeDetail

import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.Place
import com.daedan.festabook.domain.model.PlaceCategory
import com.daedan.festabook.domain.model.PlaceDetail
import com.daedan.festabook.domain.model.PlaceDetailImage
import com.daedan.festabook.domain.model.PlaceWaiting
import com.daedan.festabook.domain.model.TimeTag
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.news.FAKE_NOTICES
import com.daedan.festabook.placeMap.FAKE_PLACES
import kotlinx.datetime.LocalTime

val FAKE_PLACE_DETAIL =
    PlaceDetail(
        id = 1,
        place = FAKE_PLACES.first(),
        notices = FAKE_NOTICES,
        host = "테스트 1",
        startTime = LocalTime(9, 0, 0),
        endTime = LocalTime(18, 0, 0),
        images =
            listOf(
                PlaceDetailImage(
                    id = 1,
                    imageUrl = "",
                    sequence = 1,
                ),
            ),
        isWaitingActive = true,
    )

val FAKE_PLACE_WAITING =
    PlaceWaiting(
        totalWaitingTeams = 5,
        estimatedWaitTime = 30,
    )

val FAKE_ETC_PLACE_DETAIL =
    PlaceDetail(
        id = 1,
        place =
            Place(
                id = 1,
                title = "화장실",
                category = PlaceCategory.TOILET,
                imageUrl = null,
                description = null,
                location = null,
                timeTags =
                    listOf(
                        TimeTag(
                            timeTagId = 1,
                            name = "테스트1",
                        ),
                    ),
            ),
        notices = emptyList(),
        host = null,
        startTime = null,
        endTime = null,
        images = emptyList(),
        isWaitingActive = false,
    )

val FAKE_MY_WAITING =
    MyWaiting(
        waitingId = 42L,
        waitingOrderFromZero = 3,
        partySize = 2,
        waitingStatus = WaitingStatus.WAITING,
        totalWaitingTeams = 10,
        estimatedWaitTime = 15,
        phoneNumber = "010-1234-5678",
        placeId = 1,
    )
