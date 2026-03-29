package com.daedan.festabook.schedule

import com.daedan.festabook.domain.repository.ScheduleRepository
import com.daedan.festabook.presentation.schedule.ScheduleEventsUiState
import com.daedan.festabook.presentation.schedule.ScheduleUiState
import com.daedan.festabook.presentation.schedule.ScheduleViewModel
import com.daedan.festabook.presentation.schedule.model.toUiModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val dateId = 1L
    private lateinit var scheduleRepository: ScheduleRepository
    private lateinit var scheduleViewModel: ScheduleViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        scheduleRepository = mock()

        everySuspend { scheduleRepository.fetchAllScheduleDates() } returns
            Result.success(FAKE_SCHEDULE_DATES)
        everySuspend { scheduleRepository.fetchScheduleEventsById(dateId) } returns
            Result.success(FAKE_SCHEDULE_EVENTS)

        scheduleViewModel = ScheduleViewModel(scheduleRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ViewModel이 생성되면 해당 날짜를 불러온다`() =
        runTest {
            // given
            advanceUntilIdle()

            // then
            val stateResult = scheduleViewModel.scheduleUiState.value.content
            val expectedDate = FAKE_SCHEDULE_DATES.map { it.toUiModel() }

            verifySuspend { scheduleRepository.fetchAllScheduleDates() }
            verifySuspend { scheduleRepository.fetchScheduleEventsById(dateId) }
            assertTrue(stateResult is ScheduleUiState.Content.Success)
            assertEquals(expectedDate, stateResult.dates)
        }

    @Test
    fun `ViewModel이 생성되면 날짜에 해당하는 일정들을 불러온다`() =
        runTest {
            // given
            advanceUntilIdle()

            // when
            val state = scheduleViewModel.scheduleUiState.value.content

            // then
            val successState =
                state as? ScheduleUiState.Content.Success
                    ?: fail("ScheduleUiState.Success 가 아님: $state")

            val eventsState =
                successState.eventsUiStateByPosition[0]?.content as? ScheduleEventsUiState.Content.Success
                    ?: fail("ScheduleEventsUiState.Success 가 아님")

            verifySuspend { scheduleRepository.fetchAllScheduleDates() }
            verifySuspend { scheduleRepository.fetchScheduleEventsById(dateId) }
            assertEquals(FAKE_SCHEDULE_EVENTS_UI_MODELS, eventsState.events)
        }

    @Test
    fun `현재 진행중인 날짜의 인덱스를 불러올 수 있다`() =
        runTest {
            // given
            advanceUntilIdle()

            // when
            val state = scheduleViewModel.scheduleUiState.value.content

            // then
            val successState =
                state as? ScheduleUiState.Content.Success
                    ?: fail("ScheduleUiState.Success 가 아님: $state")

            verifySuspend { scheduleRepository.fetchAllScheduleDates() }
            verifySuspend { scheduleRepository.fetchScheduleEventsById(dateId) }
            assertEquals(0, successState.currentDatePosition)
        }

    @Test
    fun `현재 진행중인 일정의 인덱스를 불러올 수 있다`() =
        runTest {
            // given
            advanceUntilIdle()

            // when
            val state = scheduleViewModel.scheduleUiState.value.content

            // then
            val successState =
                state as? ScheduleUiState.Content.Success
                    ?: fail("ScheduleUiState.Success 가 아님: $state")
            val eventsState =
                successState.eventsUiStateByPosition[0]?.content as? ScheduleEventsUiState.Content.Success
                    ?: fail("ScheduleEventsUiState.Success 가 아님")

            verifySuspend { scheduleRepository.fetchAllScheduleDates() }
            verifySuspend { scheduleRepository.fetchScheduleEventsById(dateId) }
            assertEquals(0, eventsState.currentEventPosition)
        }
}
