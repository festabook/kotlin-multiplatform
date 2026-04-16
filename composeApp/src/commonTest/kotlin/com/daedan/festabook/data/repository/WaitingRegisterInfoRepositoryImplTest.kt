package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.waiting.FAKE_MY_WAITING_RESPONSE
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import androidx.datastore.core.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WaitingRegisterInfoRepositoryImplTest {
    private lateinit var waitingRemoteDataSource: WaitingRemoteDataSource
    private lateinit var waitingLocalDataSource: WaitingLocalDataSource
    private lateinit var repository: WaitingRegisterInfoRepositoryImpl

    private val placeId = 42L
    private val partySize = 2

    @BeforeTest
    fun setUp() {
        waitingRemoteDataSource = mock()
        waitingLocalDataSource = mock()
        repository = WaitingRegisterInfoRepositoryImpl(
            waitingRemoteDataSource = waitingRemoteDataSource,
            waitingLocalDataSource = waitingLocalDataSource,
        )
    }

    @Test
    fun `웨이팅 등록 성공 시 placeId를 저장하고 Result_success를 반환한다`() =
        runTest {
            // given
            everySuspend {
                waitingRemoteDataSource.registerWaiting(placeId, WaitingRegisterRequest(partySize))
            } returns ApiResult.Success(FAKE_MY_WAITING_RESPONSE)
            everySuspend { waitingLocalDataSource.savePlaceId(placeId) } returns Unit

            // when
            val result = repository.registerWaiting(placeId, partySize)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.savePlaceId(placeId) }
        }

    @Test
    fun `웨이팅 등록 성공 후 placeId 저장 IOException 발생 시에도 Result_success를 반환한다`() =
        runTest {
            // given
            everySuspend {
                waitingRemoteDataSource.registerWaiting(placeId, WaitingRegisterRequest(partySize))
            } returns ApiResult.Success(FAKE_MY_WAITING_RESPONSE)
            everySuspend { waitingLocalDataSource.savePlaceId(placeId) } throws IOException("disk full")

            // when
            val result = repository.registerWaiting(placeId, partySize)

            // then — 서버 등록은 성공했으므로 Result.success
            assertTrue(result.isSuccess)
        }

    @Test
    fun `서버 오류 시 Result_failure를 반환하고 placeId를 저장하지 않는다`() =
        runTest {
            // given
            everySuspend {
                waitingRemoteDataSource.registerWaiting(placeId, WaitingRegisterRequest(partySize))
            } returns ApiResult.ServerError(500, null, null)

            // when
            val result = repository.registerWaiting(placeId, partySize)

            // then
            assertTrue(result.isFailure)
            verifySuspend(VerifyMode.not) { waitingLocalDataSource.savePlaceId(any()) }
        }
}
