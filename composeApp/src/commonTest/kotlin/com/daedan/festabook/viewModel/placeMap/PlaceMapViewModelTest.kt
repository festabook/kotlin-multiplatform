package com.daedan.festabook.placeMap

import com.daedan.festabook.di.placeMapHandler.PlaceMapHandlerGraph
import com.daedan.festabook.domain.model.TimeTag
import com.daedan.festabook.domain.repository.PlaceListRepository
import com.daedan.festabook.observeEvent
import com.daedan.festabook.presentation.placeMap.PlaceMapViewModel
import com.daedan.festabook.presentation.placeMap.intent.event.FilterEvent
import com.daedan.festabook.presentation.placeMap.intent.event.MapControlEvent
import com.daedan.festabook.presentation.placeMap.intent.event.SelectEvent
import com.daedan.festabook.presentation.placeMap.intent.sideEffect.PlaceMapSideEffect
import com.daedan.festabook.presentation.placeMap.intent.state.ListLoadState
import com.daedan.festabook.presentation.placeMap.intent.state.LoadState
import com.daedan.festabook.presentation.placeMap.model.toUiModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceMapViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val handlerGraphFactory = mock<PlaceMapHandlerGraph.Factory>(MockMode.autofill)
    private lateinit var placeListRepository: PlaceListRepository
    private lateinit var placeMapViewModel: PlaceMapViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        placeListRepository = mock(MockMode.autofill)
        everySuspend { placeListRepository.getPlaces() } returns Result.success(FAKE_PLACES)
        everySuspend { placeListRepository.getPlaceGeographies() } returns
            Result.success(
                FAKE_PLACE_GEOGRAPHIES,
            )
        everySuspend { placeListRepository.getOrganizationGeography() } returns
            Result.success(
                FAKE_ORGANIZATION_GEOGRAPHY,
            )
        everySuspend { placeListRepository.getTimeTags() } returns
            Result.success(
                listOf(
                    FAKE_TIME_TAG,
                ),
            )

        placeMapViewModel =
            PlaceMapViewModel(
                placeListRepository,
                handlerGraphFactory,
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `뷰모델을 생성했을 때 전체 타임태그와 선택된 타임태그를 불러올 수 있다`() =
        runTest {
            // given - when
            placeMapViewModel =
                PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val uiState = placeMapViewModel.uiState.value
            val actualAllTimeTag = uiState.timeTags
            val actualSelectedTimeTag = uiState.selectedTimeTag
            assertEquals(
                LoadState.Success(listOf(FAKE_TIME_TAG)),
                actualAllTimeTag,
            )
            assertEquals(
                LoadState.Success(FAKE_TIME_TAG),
                actualSelectedTimeTag,
            )
        }

    @Test
    fun `뷰모델을 생성했을 때 모든 플레이스 정보를 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { placeListRepository.getPlaces() } returns Result.success(FAKE_PLACES)

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val expected = FAKE_PLACES.map { it.toUiModel() }
            val uiState = placeMapViewModel.uiState.value
            val actual = uiState.places
            verifySuspend { placeListRepository.getPlaces() }
            assertEquals(ListLoadState.PlaceLoaded(expected), actual)
        }

    @Test
    fun `뷰모델을 생성했을 때 타임 태그가 없다면 빈 리스트와 Empty타임 태그를 불러온다`() =
        runTest {
            // given
            everySuspend {
                placeListRepository.getTimeTags()
            } returns Result.success(emptyList())

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val uiState = placeMapViewModel.uiState.value
            val actualAllTimeTag = uiState.timeTags
            val actualSelectedTimeTag = uiState.selectedTimeTag
            assertEquals(
                LoadState.Success(emptyList<TimeTag>()),
                actualAllTimeTag,
            )
            assertEquals(LoadState.Empty, actualSelectedTimeTag)
        }

    @Test
    fun `뷰모델을 생성했을 때 모든 플레이스의 지도 좌표 정보를 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { placeListRepository.getPlaceGeographies() } returns
                Result.success(
                    FAKE_PLACE_GEOGRAPHIES,
                )

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val expected = FAKE_PLACE_GEOGRAPHIES.map { it.toUiModel() }
            val uiState = placeMapViewModel.uiState.value
            val actual = uiState.placeGeographies
            verifySuspend { placeListRepository.getPlaceGeographies() }
            assertEquals(LoadState.Success(expected), actual)
        }

    @Test
    fun `뷰모델을 생성했을 때 초기 학교 지리 정보를 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { placeListRepository.getOrganizationGeography() } returns
                Result.success(
                    FAKE_ORGANIZATION_GEOGRAPHY,
                )

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val expected = FAKE_ORGANIZATION_GEOGRAPHY.toUiModel()
            val uiState = placeMapViewModel.uiState.value
            val actual = uiState.initialMapSetting
            assertEquals(LoadState.Success(expected), actual)
        }

    @Test
    fun `뷰모델을 생성했을 때 정보 로드에 실패하면 독립적으로 에러 상태를 표시한다`() =
        runTest {
            // given
            val exception = Throwable("테스트")
            everySuspend { placeListRepository.getPlaces() } returns Result.failure(exception)
            everySuspend { placeListRepository.getOrganizationGeography() } returns
                Result.success(
                    FAKE_ORGANIZATION_GEOGRAPHY,
                )
            everySuspend { placeListRepository.getPlaceGeographies() } returns
                Result.success(
                    FAKE_PLACE_GEOGRAPHIES,
                )

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            advanceUntilIdle()

            // then
            val uiState = placeMapViewModel.uiState.value
            assertEquals(
                LoadState.Success(FAKE_ORGANIZATION_GEOGRAPHY.toUiModel()),
                uiState.initialMapSetting,
            )
            assertEquals(ListLoadState.Error(exception), uiState.places)
        }

    @Test
    fun `특정 액션을 받으면 액션 핸들러가 호출된다`() =
        runTest {
            // given
            val fakeHandlerGraph = mock<PlaceMapHandlerGraph>(MockMode.original)
            every { fakeHandlerGraph.filterEventHandler } returns mock(MockMode.autofill)
            every { fakeHandlerGraph.selectEventHandler } returns mock(MockMode.autofill)
            every { fakeHandlerGraph.mapControlEventHandler } returns mock(MockMode.autofill)
            every {
                handlerGraphFactory.create(any())
            } returns fakeHandlerGraph

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            placeMapViewModel.onPlaceMapEvent(SelectEvent.UnSelectPlace)
            placeMapViewModel.onPlaceMapEvent(FilterEvent.OnPlaceLoad)
            placeMapViewModel.onPlaceMapEvent(MapControlEvent.OnMapDrag)
            advanceUntilIdle()

            // then
            verify(VerifyMode.exactly(1)) { fakeHandlerGraph.filterEventHandler }
            verify(VerifyMode.exactly(1)) { fakeHandlerGraph.selectEventHandler }
            verify(VerifyMode.exactly(1)) { fakeHandlerGraph.mapControlEventHandler }
        }

    @Test
    fun `메뉴 아이템 재클릭 이벤트를 발송할 수 있다`() =
        runTest {
            // given
            val event = observeEvent(placeMapViewModel.placeMapSideEffect)

            // when
            placeMapViewModel.onMenuItemReClicked()
            val result = event.await()
            advanceUntilIdle()

            // then
            assertIs<PlaceMapSideEffect.MenuItemReClicked>(result)
        }

    @Test
    fun `LoadState가 하나라도 에러가 있다면 에러 이벤트를 발송할 수 있다`() =
        runTest {
            // given
            val throwable = Throwable()
            everySuspend { placeListRepository.getPlaceGeographies() } returns
                Result.failure(
                    throwable,
                )

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            val event = observeEvent(placeMapViewModel.placeMapSideEffect)
            advanceUntilIdle()

            // then
            val result = event.await()
            advanceUntilIdle()

            assertEquals(
                PlaceMapSideEffect.ShowErrorSnackBar(LoadState.Error(throwable)),
                result,
            )
        }

    @Test
    fun `ListLoadState가 하나라도 에러가 있다면 에러 이벤트를 발송할 수 있다`() =
        runTest {
            // given
            val throwable = Throwable()
            everySuspend { placeListRepository.getPlaces() } returns Result.failure(throwable)

            // when
            placeMapViewModel = PlaceMapViewModel(placeListRepository, handlerGraphFactory)
            val event = observeEvent(placeMapViewModel.placeMapSideEffect)
            advanceUntilIdle()

            // then
            val result = event.await()
            advanceUntilIdle()

            assertEquals(
                PlaceMapSideEffect.ShowErrorSnackBar(LoadState.Error(throwable)),
                result,
            )
        }
}
