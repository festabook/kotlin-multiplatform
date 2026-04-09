package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
import com.daedan.festabook.domain.model.WaitingInfo
import dev.mokkery.answering.returns
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WaitingInfoRepositoryImplTest {
    private lateinit var deviceRemoteDataSource: DeviceRemoteDataSource
    private lateinit var waitingLocalDataSource: WaitingLocalDataSource
    private lateinit var deviceLocalDataSource: DeviceLocalDataSource
    private lateinit var repository: WaitingInfoRepositoryImpl

    private val deviceId = 1L
    private val phoneNumber = "010-1234-5678"
    private val waitingInfo = WaitingInfo(phoneNumber = phoneNumber)

    @BeforeTest
    fun setUp() {
        deviceRemoteDataSource = mock()
        waitingLocalDataSource = mock()
        deviceLocalDataSource = mock()
        repository =
            WaitingInfoRepositoryImpl(
                deviceRemoteDataSource = deviceRemoteDataSource,
                waitingLocalDataSource = waitingLocalDataSource,
                deviceLocalDataSource = deviceLocalDataSource,
            )
    }

    // getWaitingInfo

    @Test
    fun `로컬에 전화번호가 있으면 WaitingInfo를 반환한다`() =
        runTest {
            // given
            everySuspend { waitingLocalDataSource.getPhoneNumber() } returns flowOf(phoneNumber)

            // when
            val result = repository.getWaitingInfo()

            // then
            assertEquals(WaitingInfo(phoneNumber = phoneNumber), result.getOrNull())
        }

    @Test
    fun `로컬에 전화번호가 없으면 null을 반환한다`() =
        runTest {
            // given
            everySuspend { waitingLocalDataSource.getPhoneNumber() } returns flowOf(null)

            // when
            val result = repository.getWaitingInfo()

            // then
            assertNull(result.getOrNull())
        }

    // saveWaitingInfo

    @Test
    fun `deviceId가 없으면 저장에 실패한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(null)

            // when
            val result = repository.saveWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isFailure)
            verifySuspend(VerifyMode.not) { waitingLocalDataSource.savePhoneNumber(any()) }
        }

    @Test
    fun `서버 저장 성공 시 로컬에도 저장하고 success를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend { deviceRemoteDataSource.registerPhone(deviceId, phoneNumber) } returns ApiResult.Success(Unit)
            everySuspend { waitingLocalDataSource.savePhoneNumber(phoneNumber) } returns Unit

            // when
            val result = repository.saveWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.savePhoneNumber(phoneNumber) }
        }

    @Test
    fun `서버 409 응답 시 로컬에 저장하고 success를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend {
                deviceRemoteDataSource.registerPhone(deviceId, phoneNumber)
            } returns ApiResult.ClientError(409, null, null)
            everySuspend { waitingLocalDataSource.savePhoneNumber(phoneNumber) } returns Unit

            // when
            val result = repository.saveWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.savePhoneNumber(phoneNumber) }
        }

    @Test
    fun `서버 500 응답 시 로컬에 저장하지 않고 failure를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend {
                deviceRemoteDataSource.registerPhone(deviceId, phoneNumber)
            } returns ApiResult.ServerError(500, null, null)

            // when
            val result = repository.saveWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isFailure)
            verifySuspend(VerifyMode.not) { waitingLocalDataSource.savePhoneNumber(any()) }
        }

    // updateWaitingInfo

    @Test
    fun `업데이트 성공 시 로컬에도 저장하고 success를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend { deviceRemoteDataSource.updatePhone(deviceId, phoneNumber) } returns ApiResult.Success(Unit)
            everySuspend { waitingLocalDataSource.savePhoneNumber(phoneNumber) } returns Unit

            // when
            val result = repository.updateWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.savePhoneNumber(phoneNumber) }
        }

    @Test
    fun `업데이트 409 응답 시 로컬에 저장하고 success를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend {
                deviceRemoteDataSource.updatePhone(deviceId, phoneNumber)
            } returns ApiResult.ClientError(409, null, null)
            everySuspend { waitingLocalDataSource.savePhoneNumber(phoneNumber) } returns Unit

            // when
            val result = repository.updateWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isSuccess)
            verifySuspend(VerifyMode.exactly(1)) { waitingLocalDataSource.savePhoneNumber(phoneNumber) }
        }

    @Test
    fun `업데이트 서버 500 응답 시 로컬에 저장하지 않고 failure를 반환한다`() =
        runTest {
            // given
            everySuspend { deviceLocalDataSource.getDeviceId() } returns flowOf(deviceId)
            everySuspend {
                deviceRemoteDataSource.updatePhone(deviceId, phoneNumber)
            } returns ApiResult.ServerError(500, null, null)

            // when
            val result = repository.updateWaitingInfo(waitingInfo)

            // then
            assertTrue(result.isFailure)
            verifySuspend(VerifyMode.not) { waitingLocalDataSource.savePhoneNumber(any()) }
        }
}
