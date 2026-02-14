package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FcmDataSource
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DeviceRepositoryTest {
    private lateinit var deviceRemoteDataSource: DeviceRemoteDataSource
    private lateinit var deviceLocalDataSource: DeviceLocalDataSource
    private lateinit var fcmDataSource: FcmDataSource
    private lateinit var deviceRepository: DeviceRepository

    @BeforeTest
    fun setUp() {
        deviceRemoteDataSource = mock()
        deviceLocalDataSource = mock()
        fcmDataSource = mock()
    }

    @Test
    fun `FcmToken을 캐싱할 수 있다`() =
        runTest {
            // given
            val expected = "제이"
            everySuspend { fcmDataSource.getFcmToken() } returns flowOf(expected)
            everySuspend { deviceLocalDataSource.getUuid() } returns flowOf("")

            deviceRepository =
                DeviceRepositoryImpl(
                    deviceRemoteDataSource = deviceRemoteDataSource,
                    deviceLocalDataSource = deviceLocalDataSource,
                    fcmDataSource = fcmDataSource,
                    coroutineScope = backgroundScope,
                )

            // when
            val firstResult = deviceRepository.getFcmToken()
            val secondResult = deviceRepository.getFcmToken()

            // then
            verifySuspend(VerifyMode.exactly(1)) { fcmDataSource.getFcmToken() }
            assertEquals(expected, firstResult)
            assertEquals(expected, secondResult)
        }

    @Test
    fun `Uuid를 캐싱할 수 있다`() =
        runTest {
            // given
            val expected = "제이"
            everySuspend { fcmDataSource.getFcmToken() } returns flowOf("")
            everySuspend { deviceLocalDataSource.getUuid() } returns flowOf(expected)

            deviceRepository =
                DeviceRepositoryImpl(
                    deviceRemoteDataSource = deviceRemoteDataSource,
                    deviceLocalDataSource = deviceLocalDataSource,
                    fcmDataSource = fcmDataSource,
                    coroutineScope = backgroundScope,
                )

            // when
            val firstResult = deviceRepository.getUuid()
            val secondResult = deviceRepository.getUuid()

            // then
            verifySuspend(VerifyMode.exactly(1)) { deviceLocalDataSource.getUuid() }
            assertEquals(expected, firstResult)
            assertEquals(expected, secondResult)
        }
}
