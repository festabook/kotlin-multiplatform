package com.daedan.festabook.data.datasource.remote.lineup

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.response.lineup.LineupResponse
import com.daedan.festabook.data.service.FestivalLineupService
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_LINEUP: List<LineupResponse> =
    listOf(
        LineupResponse(
            lineupId = 1,
            imageUrl = "url",
            name = "name",
            performanceAt = "date"
        ),
        LineupResponse(
            lineupId = 2,
            imageUrl = "url",
            name = "name",
            performanceAt = "date"
        ),
    )

@Suppress("UNCHECKED_CAST")
private val FAKE_LINEUP_HTTP_RESPONSE: Response<List<LineupResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_LINEUP,
    ) as Response<List<LineupResponse>>

class LineupDataSourceTest {

    private lateinit var festivalLineupService: FestivalLineupService
    private lateinit var lineupDataSource: LineupDataSource

    @BeforeTest
    fun setUp() {
        festivalLineupService = mock()
        lineupDataSource = LineupDataSourceImpl(festivalLineupService)
    }

    @Test
    fun `라인업 데이터를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { festivalLineupService.fetchLineup() } returns FAKE_LINEUP_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_LINEUP_HTTP_RESPONSE }
            val result = lineupDataSource.fetchLineup()

            // then
            verifySuspend { festivalLineupService.fetchLineup() }
            assertEquals(expected, result)
        }
}