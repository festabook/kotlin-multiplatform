package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.place.PlaceDataSource
import com.daedan.festabook.data.model.response.place.TimeTagResponse
import com.daedan.festabook.data.repository.fixture.FAKE_FESTIVAL_GEOGRAPHY_RESPONSE
import com.daedan.festabook.data.repository.fixture.FAKE_PLACE_GEOGRAPHY_RESPONSE
import com.daedan.festabook.data.repository.fixture.FAKE_PLACE_LIST_RESPONSE
import com.daedan.festabook.domain.repository.PlaceListRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaceListRepositoryTest {
    private lateinit var placeDataSource: PlaceDataSource
    private lateinit var placeListRepository: PlaceListRepository

    @BeforeTest
    fun setUp() {
        placeDataSource = mock()
        placeListRepository = PlaceListRepositoryImpl(placeDataSource)
    }

    @Test
    fun `타임 태그 목록을 가져올 수 있다`() =
        runTest {
            // given
            val expected =
                listOf(
                    TimeTagResponse(name = "오전", timeTagId = 1L),
                    TimeTagResponse(name = "오후", timeTagId = 2L),
                    TimeTagResponse(name = "저녁", timeTagId = 3L),
                )

            everySuspend { placeDataSource.fetchTimeTag() } returns ApiResult.Success(expected)

            // when
            val result = placeListRepository.getTimeTags()

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(3, actual.size)
            assertEquals("오전", actual.first().name)
            assertEquals(1L, actual.first().timeTagId)
        }

    @Test
    fun `플레이스 목록을 성공적으로 가져올 수 있다`() =
        runTest {
            // given
            val expected = FAKE_PLACE_LIST_RESPONSE

            everySuspend { placeDataSource.fetchPlaces() } returns ApiResult.Success(expected)

            // when
            val result = placeListRepository.getPlaces()

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(2, actual.size)
            assertEquals("부스 1", actual.first().title)
            assertEquals("A구역", actual.first().location)
        }

    @Test
    fun `조직 지리 정보를 성공적으로 가져올 수 있다`() =
        runTest {
            // given
            val expected = FAKE_FESTIVAL_GEOGRAPHY_RESPONSE

            everySuspend { placeDataSource.fetchOrganizationGeography() } returns ApiResult.Success(
                expected
            )

            // when
            val result = placeListRepository.getOrganizationGeography()

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(15, actual.zoom)
            assertEquals(37.5665, actual.initialCenter.latitude)
            assertEquals(126.9780, actual.initialCenter.longitude)
            assertEquals(2, actual.polygonHoleBoundary.size)
        }

    @Test
    fun `플레이스 지리 정보 목록을 성공적으로 가져올 수 있다`() =
        runTest {
            // given
            val expected = FAKE_PLACE_GEOGRAPHY_RESPONSE

            everySuspend { placeDataSource.fetchPlaceGeographies() } returns ApiResult.Success(
                expected
            )

            // when
            val result = placeListRepository.getPlaceGeographies()

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(2, actual.size)
            assertEquals("부스 1", actual.first().title)
            assertEquals(37.5665, actual.first().markerCoordinate.latitude)
            assertEquals(126.9780, actual.first().markerCoordinate.longitude)
        }
}
