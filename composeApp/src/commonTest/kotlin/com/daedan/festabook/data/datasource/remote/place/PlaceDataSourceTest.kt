package com.daedan.festabook.data.datasource.remote.place

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.service.FestivalService
import com.daedan.festabook.data.service.PlaceService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PlaceDataSourceTest {
    private lateinit var placeService: PlaceService
    private lateinit var festivalService: FestivalService
    private lateinit var placeDataSource: PlaceDataSource

    @BeforeTest
    fun setUp() {
        placeService = mock()
        festivalService = mock()
        placeDataSource = PlaceDataSourceImpl(placeService, festivalService)
    }

    @Test
    fun `플레이스 목록을 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { placeService.fetchPlaces() } returns FAKE_PLACES_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_PLACES_RESPONSE }
            val result = placeDataSource.fetchPlaces()

            // then
            verifySuspend { placeService.fetchPlaces() }
            assertEquals(expected, result)
        }

    @Test
    fun `타임 태그를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { placeService.fetchTimeTag() } returns FAKE_TIME_TAG_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_TIME_TAG_RESPONSE }
            val result = placeDataSource.fetchTimeTag()

            // then
            verifySuspend { placeService.fetchTimeTag() }
            assertEquals(expected, result)
        }

    @Test
    fun `특정 플레이스의 상세 정보를 가져올 수 있다`() =
        runTest {
            // given
            val placeId = 1L
            everySuspend { placeService.fetchPlaceDetail(placeId) } returns FAKE_PLACE_DETAIL_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_PLACE_DETAIL_RESPONSE }
            val result = placeDataSource.fetchPlaceDetail(placeId)

            // then
            verifySuspend { placeService.fetchPlaceDetail(placeId) }
            assertEquals(expected, result)
        }

    @Test
    fun `조직 지리 정보를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { festivalService.fetchOrganizationGeography() } returns FAKE_FESTIVAL_GEOGRAPHY_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_FESTIVAL_GEOGRAPHY_RESPONSE }
            val result = placeDataSource.fetchOrganizationGeography()

            // then
            verifySuspend { festivalService.fetchOrganizationGeography() }
            assertEquals(expected, result)
        }

    @Test
    fun `플레이스 지리 정보 목록을 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { placeService.fetchPlaceGeographies() } returns FAKE_PLACE_GEOGRAPHIES_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_PLACE_GEOGRAPHIES_RESPONSE }
            val result = placeDataSource.fetchPlaceGeographies()

            // then
            verifySuspend { placeService.fetchPlaceGeographies() }
            assertEquals(expected, result)
        }
}
