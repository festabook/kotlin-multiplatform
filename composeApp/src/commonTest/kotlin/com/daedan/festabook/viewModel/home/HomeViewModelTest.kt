package com.daedan.festabook.home

import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.domain.repository.FestivalRepository
import com.daedan.festabook.domain.repository.MyWaitingRepository
import com.daedan.festabook.presentation.home.FestivalUiState
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.home.LineUpItemOfDayUiModel
import com.daedan.festabook.presentation.home.LineupUiState
import com.daedan.festabook.presentation.home.WaitingBarUiState
import com.daedan.festabook.presentation.home.toUiModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var festivalRepository: FestivalRepository
    private lateinit var myWaitingRepository: MyWaitingRepository

    private val fakeMyWaiting = MyWaiting(
        waitingId = 1L,
        waitingOrder = 3,
        partySize = 2,
        waitingStatus = WaitingStatus.WAITING,
        totalWaitingTeams = 10,
        estimatedWaitTime = 15,
        phoneNumber = "010-1234-5678",
        placeId = 1L,
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        festivalRepository = mock()
        myWaitingRepository = mock()
        everySuspend { festivalRepository.getFestivalInfo() } returns
            Result.success(
                FAKE_ORGANIZATION,
            )
        everySuspend { festivalRepository.getLineUpGroupByDate() } returns
            Result.success(
                mapOf(
                    FAKE_LINEUP[0].performanceAt.date to FAKE_LINEUP,
                ),
            )
        everySuspend { myWaitingRepository.getMyWaiting() } returns Result.success(null)

        homeViewModel = HomeViewModel(festivalRepository, myWaitingRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `축제 정보를 불러올 수 있다`() =
        runTest {
            // given
            val expect = FestivalUiState.Success(FAKE_ORGANIZATION)

            // when
            homeViewModel.loadFestival()
            advanceUntilIdle()

            // then
            val actual = homeViewModel.festivalUiState.value
            assertIs<FestivalUiState.Success>(actual)
            assertEquals(expect, actual)
        }

    @Test
    fun `연예인 정보를 불러올 수 있다`() =
        runTest {
            // given
            val expectedLineup =
                listOf(
                    LineUpItemOfDayUiModel(
                        id = 0,
                        date = FAKE_LINEUP[0].performanceAt.date,
                        isDDay = false,
                        lineupItems = FAKE_LINEUP.map { it.toUiModel() },
                    ),
                )

            // when
            advanceUntilIdle()

            // then
            val actual = homeViewModel.lineupUiState.value
            assertIs<LineupUiState.Success>(actual)

            val actualItems = actual.lineups
            assertEquals(expectedLineup.size, actualItems.size)
            expectedLineup.zip(actualItems).forEach { (expected, actualItem) ->
                assertEquals(expected.date, actualItem.date)
                assertEquals(expected.isDDay, actualItem.isDDay)
                assertEquals(expected.lineupItems, actualItem.lineupItems)
            }
        }

    @Test
    fun `축제 정보를 불러오는 동안은 Loading 상태로 전환한다`() =
        runTest {
            // given
            val results = mutableListOf<FestivalUiState>()
            val job =
                launch(UnconfinedTestDispatcher()) {
                    homeViewModel.festivalUiState.collect { results.add(it) }
                }

            // when
            homeViewModel.loadFestival()

            // then
            testScheduler.runCurrent()
            assertTrue(FestivalUiState.Loading in results)

            advanceUntilIdle()
            assertIs<FestivalUiState.Success>(results.last())

            job.cancel()
        }

    @Test
    fun `축제 정보를 불러오는 데 실패하면 Error 상태로 전환한다`() =
        runTest {
            // given
            val exception = Throwable("Network Error")
            everySuspend { festivalRepository.getFestivalInfo() } returns Result.failure(exception)

            // when
            homeViewModel.loadFestival()
            advanceUntilIdle()

            // then
            assertIs<FestivalUiState.Error>(homeViewModel.festivalUiState.value)
        }

    @Test
    fun `스케줄 이동 이벤트를 발생시킬 수 있다`() =
        runTest {
            // given
            val events = mutableListOf<Unit>()
            val job =
                launch(UnconfinedTestDispatcher()) {
                    homeViewModel.navigateToScheduleEvent.collect { events.add(it) }
                }

            // when
            homeViewModel.navigateToScheduleClick()

            // then
            assertEquals(1, events.size)

            job.cancel()
        }

    @Test
    fun `웨이팅이 있으면 WaitingBarUiState가 Visible이 된다`() =
        runTest {
            // given
            everySuspend { myWaitingRepository.getMyWaiting() } returns
                Result.success(fakeMyWaiting)

            // when
            homeViewModel.loadWaitingBar()
            advanceUntilIdle()

            // then
            val actual = homeViewModel.waitingBarUiState.value
            assertIs<WaitingBarUiState.Visible>(actual)
            assertEquals(fakeMyWaiting.waitingOrder, actual.order)
            assertEquals(fakeMyWaiting.estimatedWaitTime, actual.estimatedWaitTime)
        }

    @Test
    fun `웨이팅이 없으면 WaitingBarUiState가 Hidden이 된다`() =
        runTest {
            // given
            everySuspend { myWaitingRepository.getMyWaiting() } returns Result.success(null)

            // when
            homeViewModel.loadWaitingBar()
            advanceUntilIdle()

            // then
            assertIs<WaitingBarUiState.Hidden>(homeViewModel.waitingBarUiState.value)
        }

    @Test
    fun `웨이팅 조회 실패 시 WaitingBarUiState가 Hidden이 된다`() =
        runTest {
            // given
            everySuspend { myWaitingRepository.getMyWaiting() } returns
                Result.failure(Throwable("Network Error"))

            // when
            homeViewModel.loadWaitingBar()
            advanceUntilIdle()

            // then
            assertIs<WaitingBarUiState.Hidden>(homeViewModel.waitingBarUiState.value)
        }
}
