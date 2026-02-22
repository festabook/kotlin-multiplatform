package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.place.PlaceDataSource
import com.daedan.festabook.data.model.response.place.PlaceDetailResponse
import com.daedan.festabook.data.model.response.place.PlaceGeographyResponse
import com.daedan.festabook.data.model.response.place.TimeTagResponse
import com.daedan.festabook.data.repository.fixture.fakePlaceDetailResponse
import com.daedan.festabook.data.repository.fixture.fakePlaceDetailResponseWithImage
import com.daedan.festabook.data.repository.fixture.fakePlaceDetailResponseWithNotice
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlaceDetailRepositoryTest {
    private lateinit var placeDataSource: PlaceDataSource
    private lateinit var placeDetailRepository: PlaceDetailRepository

    @BeforeTest
    fun setUp() {
        placeDataSource = mock()
        placeDetailRepository = PlaceDetailRepositoryImpl(placeDataSource)
    }

    @Test
    fun `플레이스 상세 정보를 성공적으로 가져올 수 있다`() =
        runTest {
            // given
            val placeId = 1L
            val expected = fakePlaceDetailResponse(placeId)

            everySuspend { placeDataSource.fetchPlaceDetail(placeId) } returns ApiResult.Success(
                expected
            )

            // when
            val result = placeDetailRepository.getPlaceDetail(placeId)

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(placeId, actual.id)
            assertEquals("테스트 장소", actual.place.title)
            assertEquals("주최자", actual.host)
        }

    @Test
    fun `플레이스 상세 정보의 공지사항을 시간 순으로 가져온다`() =
        runTest {
            // given
            val placeId = 1L
            val expected = fakePlaceDetailResponseWithNotice(placeId)

            everySuspend { placeDataSource.fetchPlaceDetail(placeId) } returns ApiResult.Success(
                expected
            )

            // when
            val result = placeDetailRepository.getPlaceDetail(placeId)

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(2, actual.sortedNotices.size)
            assertEquals("공지사항 제목2", actual.sortedNotices.first().title)
            assertEquals("공지사항 제목1", actual.sortedNotices.last().title)
        }

    @Test
    fun `플레이스 상세 정보의 이미지를 특정 순서로 가져온다`() =
        runTest {
            // given
            val placeId = 1L
            val expected = fakePlaceDetailResponseWithImage(placeId)

            everySuspend { placeDataSource.fetchPlaceDetail(placeId) } returns ApiResult.Success(
                expected
            )

            // when
            val result = placeDetailRepository.getPlaceDetail(placeId)

            // then
            assertTrue(result.isSuccess)
            val actual = result.getOrThrow()
            assertEquals(2, actual.sortedImages.size)

            assertEquals(
                "https://example.com/image1.jpg",
                actual.sortedImages.first().imageUrl
            )
            assertEquals(1, actual.sortedImages.first().sequence)
            assertEquals(2, actual.sortedImages.last().sequence)
        }
}
