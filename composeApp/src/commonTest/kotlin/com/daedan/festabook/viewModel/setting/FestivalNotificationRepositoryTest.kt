package com.daedan.festabook.setting

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalNotificationLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.festival.FestivalNotificationRemoteDataSource
import com.daedan.festabook.data.model.response.festival.FestivalNotificationResponse
import com.daedan.festabook.data.model.response.festival.RegisteredFestivalNotificationResponse
import com.daedan.festabook.data.repository.FestivalNotificationRepositoryImpl
import com.daedan.festabook.domain.repository.FestivalNotificationRepository
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FestivalNotificationRepositoryTest {
    private lateinit var festivalNotificationRepository: FestivalNotificationRepository
    private val festivalNotificationDataSource: FestivalNotificationRemoteDataSource =
        mock(MockMode.autofill)
    private val deviceLocalDataSource: DeviceLocalDataSource =
        mock(MockMode.autofill)
    private val festivalNotificationLocalDataSource: FestivalNotificationLocalDataSource =
        mock(MockMode.autofill)
    private val festivalLocalDataSource: FestivalLocalDataSource =
        mock(MockMode.autofill)

    @BeforeTest
    fun setup() {
        everySuspend {
            deviceLocalDataSource.getDeviceId()
        } returns flowOf(1)
        everySuspend {
            festivalLocalDataSource.getFestivalId()
        } returns flowOf(1)

        festivalNotificationRepository =
            FestivalNotificationRepositoryImpl(
                festivalNotificationDataSource,
                deviceLocalDataSource,
                festivalNotificationLocalDataSource,
                festivalLocalDataSource,
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Notification ID를 저장할 수 있다 `() =
        runTest {
            // given
            everySuspend {
                festivalNotificationDataSource.saveFestivalNotification(any(), any())
            } returns
                ApiResult.Success(
                    FestivalNotificationResponse(10),
                )

            // when
            festivalNotificationRepository.saveFestivalNotification()

            // then
            verifySuspend(VerifyMode.exactly(1)) {
                festivalNotificationDataSource.saveFestivalNotification(1, 1)
                festivalNotificationLocalDataSource.saveFestivalNotificationId(1, 10)
            }
        }

    @Test
    fun `Notification ID를 삭제할 수 있다`() =
        runTest {
            // given
            everySuspend {
                festivalNotificationLocalDataSource.getFestivalNotificationId(1)
            } returns flowOf(10)
            everySuspend {
                festivalNotificationDataSource.deleteFestivalNotification(10)
            } returns ApiResult.Success(Unit)

            // when
            festivalNotificationRepository.deleteFestivalNotification()

            // then
            verifySuspend(VerifyMode.exactly(1)) {
                festivalNotificationDataSource.deleteFestivalNotification(10)
                festivalNotificationLocalDataSource.deleteFestivalNotificationId(1)
            }
        }

    @Test
    fun `서버에서 Notification ID 삭제에 실패하면 로컬에 ID를 삭제하지 않는다`() =
        runTest {
            // given
            everySuspend {
                festivalNotificationLocalDataSource.getFestivalNotificationId(1)
            } returns flowOf(10)
            everySuspend {
                festivalNotificationDataSource.deleteFestivalNotification(10)
            } returns ApiResult.ServerError(500, "", "")

            // when
            festivalNotificationRepository.deleteFestivalNotification()

            // then
            verifySuspend(VerifyMode.exactly(0)) {
                festivalNotificationLocalDataSource.deleteFestivalNotificationId(1)
            }
        }

    @Test
    fun `서버에서 Notification ID 저장에 실패하면 로컬에 ID를 저장하지 않는다`() =
        runTest {
            // given
            everySuspend {
                festivalNotificationDataSource.saveFestivalNotification(any(), any())
            } returns
                ApiResult.ServerError(500, "", "")

            // when
            festivalNotificationRepository.saveFestivalNotification()

            // then
            verifySuspend(VerifyMode.exactly(0)) {
                festivalNotificationLocalDataSource.saveFestivalNotificationId(1, 10)
            }
        }

    @Test
    fun `서버에 알람이 등록되어있지 않다면 로컬에 Notification 정보를 삭제할 수 있다`() =
        runTest {
            // given
            everySuspend {
                festivalNotificationLocalDataSource.getFestivalNotificationId(1)
            } returns flowOf(1)

            everySuspend {
                festivalNotificationDataSource.getFestivalNotification(1)
            } returns ApiResult.Success(listOf())

            // when
            val result = festivalNotificationRepository.syncFestivalNotificationIsAllow()

            // then
            assertEquals(false, result.getOrNull())
            verifySuspend(VerifyMode.exactly(1)) {
                festivalNotificationLocalDataSource.saveFestivalNotificationIsAllowed(1, false)
                festivalNotificationLocalDataSource.deleteFestivalNotificationId(1)
            }
        }

    @Test
    fun `서버에 알람이 등록되어 있다면 로컬에 Notification 정보를 저장할 수 있다`() =
        runTest {
            // given
            everySuspend {
                festivalNotificationLocalDataSource.getFestivalNotificationId(1)
            } returns flowOf(-1)

            everySuspend {
                festivalNotificationDataSource.getFestivalNotification(1)
            } returns
                ApiResult.Success(
                    listOf(
                        RegisteredFestivalNotificationResponse(
                            festivalNotificationId = 10,
                            festivalId = 1,
                            organizationName = "test",
                            festivalName = "test",
                        ),
                    ),
                )

            // when
            val result = festivalNotificationRepository.syncFestivalNotificationIsAllow()

            // then
            assertEquals(true, result.getOrNull())
            verifySuspend(VerifyMode.exactly(1)) {
                festivalNotificationLocalDataSource.saveFestivalNotificationIsAllowed(1, true)
                festivalNotificationLocalDataSource.saveFestivalNotificationId(1, 10)
            }
        }
}
