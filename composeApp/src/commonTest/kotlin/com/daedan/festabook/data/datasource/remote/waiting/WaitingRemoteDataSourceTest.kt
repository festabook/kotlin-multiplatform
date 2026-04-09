package com.daedan.festabook.data.datasource.remote.waiting

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.service.PlaceService
import com.daedan.festabook.data.service.WaitingService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WaitingRemoteDataSourceTest {
    private lateinit var waitingService: WaitingService
    private lateinit var placeService: PlaceService
    private lateinit var waitingRemoteDataSource: WaitingRemoteDataSource

    @BeforeTest
    fun setUp() {
        waitingService = mock()
        placeService = mock()
        waitingRemoteDataSource = WaitingRemoteDataSourceImpl(waitingService, placeService)
    }

    @Test
    fun `내 웨이팅 정보를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { waitingService.fetchMyWaiting() } returns FAKE_MY_WAITING_RESPONSE_WRAPPED

            // when
            val expected = ApiResult.toApiResult { FAKE_MY_WAITING_RESPONSE_WRAPPED }
            val result = waitingRemoteDataSource.fetchMyWaiting()

            // then
            verifySuspend { waitingService.fetchMyWaiting() }
            assertEquals(expected, result)
        }

    @Test
    fun `플레이스 웨이팅 현황을 가져올 수 있다`() =
        runTest {
            // given
            val placeId = 10L
            everySuspend { placeService.fetchPlaceWaiting(placeId) } returns FAKE_PLACE_WAITING_RESPONSE_WRAPPED

            // when
            val expected = ApiResult.toApiResult { FAKE_PLACE_WAITING_RESPONSE_WRAPPED }
            val result = waitingRemoteDataSource.fetchPlaceWaiting(placeId)

            // then
            verifySuspend { placeService.fetchPlaceWaiting(placeId) }
            assertEquals(expected, result)
        }

    @Test
    fun `웨이팅을 취소할 수 있다`() =
        runTest {
            // given
            val waitingId = 1L
            everySuspend { waitingService.cancelWaiting(waitingId) } returns FAKE_CANCEL_WAITING_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_CANCEL_WAITING_RESPONSE }
            val result = waitingRemoteDataSource.cancelWaiting(waitingId)

            // then
            verifySuspend { waitingService.cancelWaiting(waitingId) }
            assertEquals(expected, result)
        }

    @Test
    fun `웨이팅을 등록할 수 있다`() =
        runTest {
            // given
            val placeId = 10L
            everySuspend {
                placeService.registerWaiting(placeId, FAKE_WAITING_REGISTER_REQUEST)
            } returns FAKE_MY_WAITING_RESPONSE_WRAPPED

            // when
            val expected = ApiResult.toApiResult { FAKE_MY_WAITING_RESPONSE_WRAPPED }
            val result = waitingRemoteDataSource.registerWaiting(placeId, FAKE_WAITING_REGISTER_REQUEST)

            // then
            verifySuspend { placeService.registerWaiting(placeId, FAKE_WAITING_REGISTER_REQUEST) }
            assertEquals(expected, result)
        }

    @Test
    fun `웨이팅이 존재하면 exists가 true인 응답을 반환한다`() =
        runTest {
            // given
            everySuspend { waitingService.fetchMyWaitingExist() } returns FAKE_WAITING_EXIST_RESPONSE_WRAPPED

            // when
            val result = waitingRemoteDataSource.fetchMyWaitingExist()

            // then
            verifySuspend { waitingService.fetchMyWaitingExist() }
            assertEquals(ApiResult.Success(FAKE_WAITING_EXIST_RESPONSE), result)
        }

    @Test
    fun `웨이팅이 존재하지 않으면 exists가 false인 응답을 반환한다`() =
        runTest {
            // given
            everySuspend { waitingService.fetchMyWaitingExist() } returns FAKE_WAITING_NOT_EXIST_RESPONSE_WRAPPED

            // when
            val result = waitingRemoteDataSource.fetchMyWaitingExist()

            // then
            verifySuspend { waitingService.fetchMyWaitingExist() }
            assertEquals(ApiResult.Success(FAKE_WAITING_NOT_EXIST_RESPONSE), result)
        }
}
