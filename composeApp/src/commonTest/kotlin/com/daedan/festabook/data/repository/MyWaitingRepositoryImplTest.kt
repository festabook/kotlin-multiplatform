package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_MY_WAITING_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_WAITING_EXIST_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_WAITING_NOT_EXIST_RESPONSE
import com.daedan.festabook.domain.model.MyWaitingWithPlace
import com.daedan.festabook.domain.model.WaitingStatus
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var waitingLocalDataSource: WaitingLocalDataSource
    private lateinit var repository: MyWaitingRepositoryImpl

    @BeforeTest
    fun setUp() {
        waitingRemoteDataSource = mock()
        waitingLocalDataSource = mock()
        repository = MyWaitingRepositoryImpl(
            waitingRemoteDataSource = waitingRemoteDataSource,
            waitingLocalDataSource = waitingLocalDataSource,
        )
    }

    @Test
    fun `웨이팅이 존재하고 WAITING 상태면 MyWaitingWithPlace를 반환한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_EXIST_RESPONSE)
            everySuspend { waitingRemoteDataSource.fetchMyWaiting() } returns
                ApiResult.Success(FAKE_MY_WAITING_RESPONSE)
            every { waitingLocalDataSource.getPlaceId() } returns flowOf(1L)

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            val withPlace = result.getOrNull()
            assertIs<MyWaitingWithPlace>(withPlace)
            assertEquals(WaitingStatus.WAITING, withPlace.myWaiting.waitingStatus)
            assertEquals(1L, withPlace.placeId)
        }

    @Test
    fun `서버가 exists=false를 반환하면 null을 반환하고 로컬 placeId를 클리어한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_NOT_EXIST_RESPONSE)
            every { waitingLocalDataSource.getPlaceId() } returns flowOf(1L)
            everySuspend { waitingLocalDataSource.clearPlaceId() } returns Unit

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.clearPlaceId() }
        }

    @Test
    fun `서버가 CANCELED 상태를 반환하면 null을 반환하고 placeId를 클리어한다`() =
        runTest {
            // given
            val canceledResponse = FAKE_MY_WAITING_RESPONSE.copy(
                waitingStatus = WaitingStatus.CANCELED,
            )
            everySuspend { waitingRemoteDataSource.fetchMyWaitingExist() } returns
                ApiResult.Success(FAKE_WAITING_EXIST_RESPONSE)
            everySuspend { waitingRemoteDataSource.fetchMyWaiting() } returns
                ApiResult.Success(canceledResponse)
            everySuspend { waitingLocalDataSource.clearPlaceId() } returns Unit

            // when
            val result = repository.getMyWaiting()

            // then
            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.clearPlaceId() }
        }

    @Test
    fun `cancelWaiting 성공 시 placeId를 클리어한다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.cancelWaiting(any()) } returns ApiResult.Success(Unit)
            everySuspend { waitingLocalDataSource.clearPlaceId() } returns Unit

            // when
            val result = repository.cancelWaiting(waitingId = 1L)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.clearPlaceId() }
        }

    @Test
    fun `cancelWaiting 실패 시 placeId를 클리어하지 않는다`() =
        runTest {
            // given
            everySuspend { waitingRemoteDataSource.cancelWaiting(any()) } returns
                ApiResult.ServerError(500, null, null)

            // when
            val result = repository.cancelWaiting(waitingId = 1L)

            // then
            assertTrue(result.isFailure)
            verifySuspend(VerifyMode.not) { waitingLocalDataSource.clearPlaceId() }
        }
}
