package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_MY_WAITING_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_WAITING_EXIST_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_WAITING_NOT_EXIST_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.WaitingStatus
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MyWaitingRepositoryImplTest {
    private lateinit var waitingRemoteDataSource: WaitingRemoteDataSource
    private lateinit var repository: MyWaitingRepositoryImpl

    @BeforeTest
    fun setUp() {
        waitingRemoteDataSource = mock()
        repository =
            MyWaitingRepositoryImpl(
                waitingRemoteDataSource = waitingRemoteDataSource,
            )
    }

    @Test
    fun `웨이팅이 존재하고 WAITING 상태면 placeId를 포함한 MyWaiting을 반환한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_EXIST_RESPONSE)
            everySuspend { waitingRemoteDataSource.fetchMyWaiting() } returns
                ApiResult.Success(FAKE_MY_WAITING_RESPONSE)

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            val myWaiting = result.getOrNull()
            assertIs<MyWaiting>(myWaiting)
            assertEquals(WaitingStatus.WAITING, myWaiting.waitingStatus)
            assertEquals(FAKE_MY_WAITING_RESPONSE.placeId, myWaiting.placeId)
        }

    @Test
    fun `서버가 exists=false를 반환하면 null을 반환한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_NOT_EXIST_RESPONSE)

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
        }

    @Test
    fun `서버가 CANCELED 상태를 반환하면 null을 반환한다`() =
        runTest {
            // given
            val canceledResponse =
                FAKE_MY_WAITING_RESPONSE.copy(
                    waitingStatus = WaitingStatus.CANCELED,
                )
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_EXIST_RESPONSE)
            everySuspend { waitingRemoteDataSource.fetchMyWaiting() } returns
                ApiResult.Success(canceledResponse)

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
        }

    @Test
    fun `cancelWaiting 성공 시 Result_success를 반환한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.cancelWaiting(any()) } returns ApiResult.Success(Unit)

            // when
            val result = repository.cancelWaiting(waitingId = 1L)

            // then
            assertTrue(result.isSuccess)
        }

    @Test
    fun `cancelWaiting 실패 시 Result_failure를 반환한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.cancelWaiting(any()) } returns
                ApiResult.ServerError(500, null, null)

            // when
            val result = repository.cancelWaiting(waitingId = 1L)

            // then
            assertTrue(result.isFailure)
        }
}
