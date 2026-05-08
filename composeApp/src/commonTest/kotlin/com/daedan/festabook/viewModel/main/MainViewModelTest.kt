package com.daedan.festabook.viewModel.main

import com.daedan.festabook.domain.repository.FestivalRepository
import com.daedan.festabook.observeMultipleEvent
import com.daedan.festabook.presentation.main.MainViewModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var festivalRepository: FestivalRepository
    private lateinit var mainViewModel: MainViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        festivalRepository = mock(MockMode.autofill)
        every { festivalRepository.getIsFirstVisit() } returns flowOf(false)
        mainViewModel = MainViewModel(festivalRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `뒤로 가기를 두 번 빠르게 두 번 클릭했을 때 종료 이벤트가 발생한다`() =
        runTest {
            // given
            val events = mutableListOf<Boolean>()
            observeMultipleEvent(mainViewModel.backPressEvent, events)

            // when
            mainViewModel.onBackPressed()
            mainViewModel.onBackPressed()
            advanceUntilIdle()

            // then
            assertTrue(events.last())
        }

    @Test
    fun `뒤로 가기를 한 번만 클릭했을 때 종료 이벤트가 발생하지 않는다`() =
        runTest {
            // given
            val events = mutableListOf<Boolean>()
            observeMultipleEvent(mainViewModel.backPressEvent, events)

            // when
            mainViewModel.onBackPressed()
            advanceUntilIdle()

            // then
            assertFalse(events.last())
        }

    @Test
    fun `축제 페이지의 첫 방문 여부를 확인할 수 있다`() =
        runTest {
            // given
            every { festivalRepository.getIsFirstVisit() } returns flowOf(true)

            // when
            mainViewModel = MainViewModel(festivalRepository)
            advanceUntilIdle()

            // then
            assertTrue(mainViewModel.isFirstVisit.value)
        }
}
