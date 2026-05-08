package com.daedan.festabook.setting

import com.daedan.festabook.domain.repository.FestivalNotificationRepository
import com.daedan.festabook.observeEvent
import com.daedan.festabook.presentation.setting.SettingViewModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var settingViewModel: SettingViewModel

    private lateinit var festivalNotificationRepository: FestivalNotificationRepository

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        festivalNotificationRepository = mock(MockMode.autofill)
        everySuspend { festivalNotificationRepository.syncFestivalNotificationIsAllow() } returns
            Result.success(false)
        settingViewModel = SettingViewModel(festivalNotificationRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `알림 허용을 클릭했을 때 알림이 허용이 안되있다면 권한 요청 이벤트를 발생시킨다`() =
        runTest {
            // given
            everySuspend { festivalNotificationRepository.getFestivalNotificationIsAllow() } returns flowOf(false)
            everySuspend { festivalNotificationRepository.syncFestivalNotificationIsAllow() } returns
                Result.success(false)

            settingViewModel = SettingViewModel(festivalNotificationRepository)
            val event = observeEvent(settingViewModel.permissionCheckEvent)

            // when
            settingViewModel.notificationAllowClick()
            advanceUntilIdle()

            // then
            val actual = event.await()
            advanceUntilIdle()
            assertEquals(Unit, actual)
        }

    @Test
    fun `알림 허용을 클릭했을 때 알림이 허용이 되있다면 알림id를 삭제한다`() =
        runTest {
            // given
            everySuspend { festivalNotificationRepository.getFestivalNotificationIsAllow() } returns flowOf(true)
            everySuspend { festivalNotificationRepository.syncFestivalNotificationIsAllow() } returns
                Result.success(true)
            everySuspend { festivalNotificationRepository.deleteFestivalNotification() } returns
                Result.success(Unit)

            // when
            settingViewModel = SettingViewModel(festivalNotificationRepository)
            advanceUntilIdle()

            settingViewModel.notificationAllowClick()
            advanceUntilIdle()

            // then
            val result = settingViewModel.isAllowed.value
            verifySuspend { festivalNotificationRepository.saveFestivalNotificationIsAllow(false) }
            verifySuspend { festivalNotificationRepository.deleteFestivalNotification() }
            assertFalse(result)
        }

    @Test
    fun `알림 허용을 클릭했을 때 서버에 알림 정보 저장에 실패하면 이전 상태로 원복한다`() =
        runTest {
            // given
            everySuspend { festivalNotificationRepository.getFestivalNotificationIsAllow() } returns flowOf(true)
            everySuspend { festivalNotificationRepository.syncFestivalNotificationIsAllow() } returns
                Result.success(true)
            everySuspend { festivalNotificationRepository.deleteFestivalNotification() } returns
                Result.failure(
                    Throwable(),
                )

            // when
            settingViewModel = SettingViewModel(festivalNotificationRepository)
            advanceUntilIdle()
            settingViewModel.notificationAllowClick()
            advanceUntilIdle()

            // then
            val result = settingViewModel.isAllowed.value
            verifySuspend { festivalNotificationRepository.saveFestivalNotificationIsAllow(true) }
            assertTrue(result)
        }

    @Test
    fun `알림을 허용했을 때 서버에 알림 정보 삭제에 실패하면 이전 상태로 원복한다`() =
        runTest {
            // given
            everySuspend { festivalNotificationRepository.saveFestivalNotification() } returns
                Result.failure(
                    Throwable(),
                )

            // when
            settingViewModel.saveNotificationId()
            advanceUntilIdle()

            // then
            val result = settingViewModel.isAllowed.value
            verifySuspend { festivalNotificationRepository.saveFestivalNotificationIsAllow(false) }
            assertFalse(result)
        }
}
