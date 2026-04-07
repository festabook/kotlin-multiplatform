package com.daedan.festabook.placeMap.handler

import com.daedan.festabook.domain.model.TimeTag
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.observeEvent
import com.daedan.festabook.placeMap.FAKE_TIME_TAG
import com.daedan.festabook.placeMap.placeDetail.FAKE_ETC_PLACE_DETAIL
import com.daedan.festabook.placeMap.placeDetail.FAKE_PLACE_DETAIL
import com.daedan.festabook.presentation.placeMap.intent.event.SelectEvent
import com.daedan.festabook.presentation.placeMap.intent.handler.EventHandlerContext
import com.daedan.festabook.presentation.placeMap.intent.handler.SelectEventHandlerImpl
import com.daedan.festabook.presentation.placeMap.intent.sideEffect.MapControlSideEffect
import com.daedan.festabook.presentation.placeMap.intent.sideEffect.PlaceMapSideEffect
import com.daedan.festabook.presentation.placeMap.intent.state.LoadState
import com.daedan.festabook.presentation.placeMap.intent.state.PlaceMapUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.toUiModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SelectActionHandlerTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var selectActionHandler: SelectEventHandlerImpl

    private lateinit var uiState: MutableStateFlow<PlaceMapUiState>

    private lateinit var placeDetailRepository: PlaceDetailRepository

    private val mapControlUiEvent: Channel<MapControlSideEffect> =
        Channel(
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    private val placeMapUiEvent: Channel<PlaceMapSideEffect> =
        Channel(
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        placeDetailRepository = mock()
        uiState = MutableStateFlow(PlaceMapUiState())

        selectActionHandler =
            SelectEventHandlerImpl(
                EventHandlerContext(
                    mapControlSideEffect = mapControlUiEvent,
                    placeMapSideEffect = placeMapUiEvent,
                    uiState = uiState,
                    onUpdateState = { uiState.update(it) },
                    scope = CoroutineScope(testDispatcher),
                    cachedPlaces = MutableStateFlow(emptyList()),
                    cachedPlaceByTimeTag = MutableStateFlow(emptyList()),
                    onUpdateCachedPlace = {},
                ),
                filterActionHandler = mock(MockMode.autofill),
                placeDetailRepository = placeDetailRepository,
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `플레이스의 아이디와 카테고리가 있으면 플레이스 상세를 선택할 수 있다`() =
        runTest {
            // given
            everySuspend { placeDetailRepository.getPlaceDetail(1) } returns
                Result.success(
                    FAKE_PLACE_DETAIL,
                )
            val eventResult = observeEvent(mapControlUiEvent.receiveAsFlow())

            // when
            selectActionHandler(SelectEvent.OnPlaceClick(1))
            advanceUntilIdle()

            // then
            verifySuspend { placeDetailRepository.getPlaceDetail(1) }

            val event = eventResult.await()
            advanceUntilIdle()

            val expected = LoadState.Success(FAKE_PLACE_DETAIL.toUiModel())
            assertEquals(expected, uiState.value.selectedPlace)
            assertEquals(MapControlSideEffect.SelectMarker(expected), event)
        }

    @Test
    fun `카테고리가 기타시설일 떄에도 플레이스 상세를 선택할 수 있다`() =
        runTest {
            // given
            everySuspend { placeDetailRepository.getPlaceDetail(1) } returns
                Result.success(
                    FAKE_ETC_PLACE_DETAIL,
                )
            val eventResult = observeEvent(mapControlUiEvent.receiveAsFlow())

            // when
            selectActionHandler(SelectEvent.OnPlaceClick(1))
            advanceUntilIdle()

            // then
            val event = eventResult.await()
            advanceUntilIdle()

            val expected = LoadState.Success(FAKE_ETC_PLACE_DETAIL.toUiModel())
            assertEquals(expected, uiState.value.selectedPlace)
            assertEquals(MapControlSideEffect.SelectMarker(expected), event)
        }

    @Test
    fun `플레이스 상세 선택을 해제할 수 있다`() =
        runTest {
            // given
            everySuspend { placeDetailRepository.getPlaceDetail(1) } returns
                Result.success(
                    FAKE_PLACE_DETAIL,
                )
            selectActionHandler(SelectEvent.OnPlaceClick(1))
            val eventResult = observeEvent(mapControlUiEvent.receiveAsFlow())
            advanceUntilIdle()

            // when
            selectActionHandler(SelectEvent.UnSelectPlace)
            advanceUntilIdle()

            // then
            val event = eventResult.await()
            advanceUntilIdle()

            assertEquals(LoadState.Empty, uiState.value.selectedPlace)
            assertEquals(MapControlSideEffect.UnselectMarker, event)
        }

    @Test
    fun `학교로 돌아가기 버튼이 나타나지 않는 임계값을 넣을 수 있다`() =
        runTest {
            // given
            val isExceededMaxLength = true

            // when
            selectActionHandler(SelectEvent.ExceededMaxLength(isExceededMaxLength))
            advanceUntilIdle()

            // then
            assertEquals(isExceededMaxLength, uiState.value.isExceededMaxLength)
        }

    @Test
    fun `현재 플레이스를 선택 후에 플레이스 상세로 이벤트를 발생시킬 수 있다`() =
        runTest {
            // given
            everySuspend {
                placeDetailRepository.getPlaceDetail(FAKE_PLACE_DETAIL.id)
            } returns Result.success(FAKE_PLACE_DETAIL)

            val eventResult = observeEvent(placeMapUiEvent.receiveAsFlow())
            val expected = LoadState.Success(FAKE_PLACE_DETAIL.toUiModel())
            uiState.update {
                it.copy(
                    selectedPlace = expected,
                    selectedTimeTag = LoadState.Success(FAKE_TIME_TAG),
                )
            }

            // when
            selectActionHandler(
                SelectEvent.OnPlacePreviewClick(expected),
            )
            advanceUntilIdle()

            // then
            val event = eventResult.await()
            advanceUntilIdle()

            assertEquals(PlaceMapSideEffect.StartPlaceDetail(expected), event)
        }

    @Test
    fun `타임태그가 선택되었음을 알리는 이벤트를 발생시킬 수 있다`() =
        runTest {
            // given
            val expected = TimeTag(1, "테스트1")

            // when
            selectActionHandler(SelectEvent.OnTimeTagClick(expected))
            advanceUntilIdle()

            // then
            assertEquals(LoadState.Success(expected), uiState.value.selectedTimeTag)
        }

    @Test
    fun `뒤로가기가 클릭되었을 때 선택 해제 이벤트를 발생시킬 수 있다`() =
        runTest {
            // given
            val eventResult = observeEvent(mapControlUiEvent.receiveAsFlow())

            // when
            selectActionHandler(SelectEvent.OnBackPress)

            // then
            val event = eventResult.await()
            advanceUntilIdle()

            assertEquals(MapControlSideEffect.UnselectMarker, event)
        }
}
