package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_MY_WAITING_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WaitingRegisterInfoRepositoryImplTest {
    private lateinit var waitingRemoteDataSource: WaitingRemoteDataSource
    private lateinit var repository: WaitingRegisterInfoRepositoryImpl

    private val placeId = 42L
    private val partySize = 2

    @BeforeTest
    fun setUp() {
        waitingRemoteDataSource = mock()
        repository =
            WaitingRegisterInfoRepositoryImpl(
                waitingRemoteDataSource = waitingRemoteDataSource,
            )
    }

    @Test
    fun `웨이팅 등록 성공 시 Result_success를 반환한다`() =
        runTest {
            // given
            everySuspend {
                waitingRemoteDataSource.registerWaiting(placeId, WaitingRegisterRequest(partySize))
            } returns ApiResult.Success(FAKE_MY_WAITING_RESPONSE)

            // when
            val result = repository.registerWaiting(placeId, partySize)

            // then
            assertTrue(result.isSuccess)
        }

    @Test
    fun `서버 오류 시 Result_failure를 반환한다`() =
        runTest {
            // given
            everySuspend {
                waitingRemoteDataSource.registerWaiting(placeId, WaitingRegisterRequest(partySize))
            } returns ApiResult.ServerError(500, null, null)

            // when
            val result = repository.registerWaiting(placeId, partySize)

            // then
            assertTrue(result.isFailure)
        }
}
