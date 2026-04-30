package com.daedan.festabook.data.datasource.remote.festival

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.response.FestivalSearchResponse
import com.daedan.festabook.data.model.response.festival.FestivalResponse
import com.daedan.festabook.data.service.FestivalService
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_FESTIVAL_RESPONSE =
    FestivalResponse(
        id = 1L,
        organizationId = 1L,
        organizationName = "대학교",
        festivalImages =
            listOf(
                FestivalResponse.FestivalImage(
                    id = 100L,
                    imageUrl = "https://image1.com",
                    sequence = 1,
                ),
                FestivalResponse.FestivalImage(
                    id = 101L,
                    imageUrl = "https://image2.com",
                    sequence = 2,
                ),
            ),
        festivalName = "봄 축제",
        startDate = "2026-04-01",
        endDate = "2026-04-10",
        festivalSponsors = emptyList(),
        instagramLink = null,
        homepageLink = null,
    )

@Suppress("UNCHECKED_CAST")
private val FAKE_FESTIVAL_HTTP_RESPONSE: Response<FestivalResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_FESTIVAL_RESPONSE,
    ) as Response<FestivalResponse>

private val FAKE_FESTIVAL_SEARCH_RESPONSES =
    listOf(
        FestivalSearchResponse(
            festivalId = 1L,
            organizationName = "대학교",
            festivalName = "축제1",
            startDate = "2024-04-01",
            endDate = "2024-04-10",
        ),
        FestivalSearchResponse(
            festivalId = 2L,
            organizationName = "대학교2",
            festivalName = "축제2",
            startDate = "2024-06-01",
            endDate = "2024-06-07",
        ),
    )

@Suppress("UNCHECKED_CAST")
private val FAKE_FESTIVAL_SEARCH_HTTP_RESPONSE: Response<List<FestivalSearchResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_FESTIVAL_SEARCH_RESPONSES,
    ) as Response<List<FestivalSearchResponse>>

class FestivalRemoteDataSourceTest {
    private lateinit var festivalService: FestivalService
    private lateinit var festivalRemoteDataSource: FestivalRemoteDataSource

    @BeforeTest
    fun setUp() {
        festivalService = mock()
        festivalRemoteDataSource = FestivalRemoteDataSourceImpl(festivalService)
    }

    @Test
    fun `축제 데이터를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { festivalService.fetchOrganization() } returns FAKE_FESTIVAL_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_FESTIVAL_HTTP_RESPONSE }
            val result = festivalRemoteDataSource.fetchFestival()

            // then
            verifySuspend { festivalService.fetchOrganization() }
            assertEquals(expected, result)
        }

    @Test
    fun `키워드로 축제 검색 결과를 가져올 수 있다`() =
        runTest {
            // given
            val keyword = "대학"

            everySuspend {
                festivalService.findFestivalsByKeyword(keyword)
            } returns FAKE_FESTIVAL_SEARCH_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_FESTIVAL_SEARCH_HTTP_RESPONSE }
            val result = festivalRemoteDataSource.findFestivalsByKeyword(keyword)

            // then
            verifySuspend {
                festivalService.findFestivalsByKeyword(keyword)
            }
            assertEquals(expected, result)
        }
}
