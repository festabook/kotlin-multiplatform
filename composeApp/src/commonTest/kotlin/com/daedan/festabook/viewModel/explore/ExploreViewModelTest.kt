package com.daedan.festabook.viewModel.explore

import com.daedan.festabook.domain.model.FestivalSearchItem
import com.daedan.festabook.domain.repository.ExploreRepository
import com.daedan.festabook.presentation.explore.ExploreSideEffect
import com.daedan.festabook.presentation.explore.ExploreViewModel
import com.daedan.festabook.presentation.explore.SearchUiState
import com.daedan.festabook.presentation.explore.model.SearchResultUiModel
import com.daedan.festabook.presentation.explore.model.toUiModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class ExploreViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var exploreRepository: ExploreRepository
    private lateinit var exploreViewModel: ExploreViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        exploreRepository = mock(MockMode.autofill)
        everySuspend { exploreRepository.search(any()) } returns Result.success(emptyList())
        everySuspend { exploreRepository.getRecentFestivalSearches() } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `뷰모델을 생성하면 저장된 축제 id가 있는지 확인한다`() =
        runTest {
            // given
            everySuspend { exploreRepository.getFestivalId() } returns 1L
            // when
            exploreViewModel = ExploreViewModel(exploreRepository)

            // then
            verifySuspend { exploreRepository.getFestivalId() }
            assertTrue(exploreViewModel.uiState.value.hasFestivalId)
        }

    @Test
    fun `검색어가 변경되면 query 상태가 업데이트된다`() =
        runTest {
            // given
            exploreViewModel = ExploreViewModel(exploreRepository)
            val query = "테스트"

            // when
            exploreViewModel.onTextInputChanged(query)

            // then
            assertEquals(query, exploreViewModel.uiState.value.query)
        }

    @Test
    fun `검색어가 변경되고 일정 시간이 지나면 검색을 수행하고 성공 시 결과를 업데이트한다`() =
        runTest {
            // given
            exploreViewModel = ExploreViewModel(exploreRepository)
            val query = "테스트"
            val universities =
                listOf(
                    FestivalSearchItem(
                        festivalId = 1L,
                        organizationName = "테스트대학교",
                        festivalName = "테스트축제",
                        startDate = "2024-05-01",
                        endDate = "2024-05-03",
                    ),
                    FestivalSearchItem(
                        festivalId = 2L,
                        organizationName = "테스트대학교2",
                        festivalName = "테스트축제2",
                        startDate = "2024-09-01",
                        endDate = "2024-09-03",
                    ),
                )
            val uiModels = universities.map { it.toUiModel() }

            everySuspend { exploreRepository.search(query) } returns Result.success(universities)

            // when
            exploreViewModel.onTextInputChanged(query)
            advanceTimeBy(500L)

            // then
            verifySuspend { exploreRepository.search(query) }
            val searchState = exploreViewModel.uiState.value.searchState
            assertIs<SearchUiState.Success>(searchState)
            assertEquals(uiModels, searchState.universitiesFound)
        }

    @Test
    fun `검색어가 비어있으면 Idle 상태로 변경된다`() =
        runTest {
            // given
            exploreViewModel = ExploreViewModel(exploreRepository)
            exploreViewModel.onTextInputChanged("이전검색어")
            advanceTimeBy(500L)

            // when
            exploreViewModel.onTextInputChanged("")
            advanceTimeBy(500L)

            // then
            assertEquals(SearchUiState.Idle, exploreViewModel.uiState.value.searchState)
        }

    @Test
    fun `검색 실패 시 Error 상태로 업데이트된다`() =
        runTest {
            // given
            exploreViewModel = ExploreViewModel(exploreRepository)
            val query = "에러발생"
            val exception = Exception("Network Error")
            everySuspend { exploreRepository.search(query) } returns Result.failure(exception)

            // when
            exploreViewModel.onTextInputChanged(query)
            advanceTimeBy(500L)

            // then
            verifySuspend { exploreRepository.search(query) }
            val searchState = exploreViewModel.uiState.value.searchState
            assertIs<SearchUiState.Error>(searchState)
            assertEquals(exception, searchState.throwable)
        }

    @Test
    fun `대학교가 선택되었을 때 축제 Id를 저장하고 Main으로 이동하는 이벤트를 발생시킨다`() =
        runTest {
            // given
            exploreViewModel = ExploreViewModel(exploreRepository)
            val searchResult =
                SearchResultUiModel(
                    1L,
                    "테스트대학교",
                    "테스트축제",
                )
            val collectedSideEffects = mutableListOf<ExploreSideEffect>()

            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    exploreViewModel.sideEffect.collect {
                        collectedSideEffects.add(it)
                    }
                }

            // when
            exploreViewModel.onUniversitySelected(searchResult)

            // then
            verifySuspend { exploreRepository.saveFestivalId(searchResult.festivalId) }
            assertEquals(1, collectedSideEffects.size)
            assertEquals(
                ExploreSideEffect.NavigateToMain(searchResult),
                collectedSideEffects.first(),
            )

            job.cancel()
        }
}
