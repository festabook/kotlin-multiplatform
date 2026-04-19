package com.daedan.festabook.placeMap.placeDetail

import com.daedan.festabook.domain.repository.MyWaitingRepository
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.news.FAKE_NOTICES
import com.daedan.festabook.observeEvent
import com.daedan.festabook.placeMap.FAKE_PLACES
import com.daedan.festabook.presentation.news.notice.model.toUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.PlaceDetailViewModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingStatusUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingTeamUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.toUiModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
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
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var placeDetailRepository: PlaceDetailRepository
    private lateinit var waitingRegisterInfoRepository: WaitingRegisterInfoRepository
    private lateinit var myWaitingRepository: MyWaitingRepository
    private lateinit var placeDetailViewModel: PlaceDetailViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        placeDetailRepository = mock()
        waitingRegisterInfoRepository = mock()
        myWaitingRepository = mock()
        everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
            Result.success(
                FAKE_PLACE_DETAIL,
            )
        everySuspend { waitingRegisterInfoRepository.getPlaceWaiting(any()) } returns
            Result.success(FAKE_PLACE_WAITING)
        everySuspend { myWaitingRepository.getMyWaiting() } returns Result.success(null)
        everySuspend { myWaitingRepository.cancelWaiting(any()) } returns Result.success(Unit)
        placeDetailViewModel =
            PlaceDetailViewModel(
                placeDetailRepository,
                waitingRegisterInfoRepository,
                myWaitingRepository,
                FAKE_PLACES.first().id,
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `플레이스 상세 정보를 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
                Result.success(
                    FAKE_PLACE_DETAIL,
                )

            // when
            placeDetailViewModel.loadPlaceDetail(1)
            advanceUntilIdle()

            // then
            val expected =
                PlaceDetailUiState.Success(
                    placeDetail = FAKE_PLACE_DETAIL.toUiModel(),
                    waitingTeam = WaitingTeamUiState.Success(totalTeams = FAKE_PLACE_WAITING.totalWaitingTeams),
                    waitingStatus = WaitingStatusUiState.Active(estimatedMinutes = FAKE_PLACE_WAITING.estimatedWaitTime),
                )
            val actual = placeDetailViewModel.placeDetail.value
            verifySuspend { placeDetailRepository.getPlaceDetail(FAKE_PLACES.first().id) }
            assertEquals(expected, actual)
        }

    @Test
    fun `플레이스 상세 정보 로드에 실파하면 에러 상태를 표시한다`() =
        runTest {
            // given
            val exception = Throwable("테스트")
            everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
                Result.failure(
                    exception,
                )

            // then
            placeDetailViewModel.loadPlaceDetail(
                placeId = 1,
            )
            advanceUntilIdle()

            // then
            val expected = PlaceDetailUiState.Error(exception)
            val actual = placeDetailViewModel.placeDetail.value
            verifySuspend { placeDetailRepository.getPlaceDetail(FAKE_PLACES.first().id) }
            assertEquals(expected, actual)
        }

    @Test
    fun `뷰모델 생성 시마다 플레이스를 로드한다`() =
        runTest {
            // given
            val expected = FAKE_PLACE_DETAIL.toUiModel()
            val placeDetailRepository = mock<PlaceDetailRepository>(MockMode.autofill)

            // when
            placeDetailViewModel =
                PlaceDetailViewModel(
                    placeDetailRepository,
                    waitingRegisterInfoRepository,
                    myWaitingRepository,
                    expected.place.id,
                )
            advanceUntilIdle()

            // then
            verifySuspend(VerifyMode.exactly(1)) { placeDetailRepository.getPlaceDetail(any()) }
        }

    @Test
    fun `플레이스 공지사항을 펼칠 수 있다`() =
        runTest {
            // given
            val noticeItem = FAKE_NOTICES.first().toUiModel()
            val expected =
                listOf(
                    noticeItem.copy(isExpanded = true),
                    FAKE_NOTICES[1].toUiModel(),
                )

            // when
            placeDetailViewModel.toggleNoticeExpanded(noticeItem)

            // then
            val actual =
                placeDetailViewModel.placeDetail
                    .value
                    .let {
                        (it as? PlaceDetailUiState.Success)
                            ?: fail("PlaceDetailUiState 가 성공 상태가 아님")
                    }.placeDetail
                    .notices

            assertEquals(expected, actual)
        }

    @Test
    fun `onRegisterWaitingClick 호출 시 등록된 웨이팅이 없으면 navigateToWaitingRegisterEvent 를 발행한다`() =
        runTest {
            // given
            advanceUntilIdle()
            everySuspend { myWaitingRepository.getMyWaiting() } returns Result.success(null)

            // when
            val event = observeEvent(placeDetailViewModel.navigateToWaitingRegisterEvent)
            placeDetailViewModel.onRegisterWaitingClick()
            advanceUntilIdle()

            // then
            assertEquals(FAKE_PLACES.first().id, event.await())
        }

    @Test
    fun `onRegisterWaitingClick 호출 시 이미 등록된 웨이팅이 있으면 showDuplicateWaitingBottomSheetEvent 를 발행한다`() =
        runTest {
            // given
            advanceUntilIdle()
            everySuspend { myWaitingRepository.getMyWaiting() } returns
                Result.success(
                    FAKE_MY_WAITING,
                )

            // when
            val event = observeEvent(placeDetailViewModel.showDuplicateWaitingBottomSheetEvent)
            placeDetailViewModel.onRegisterWaitingClick()
            advanceUntilIdle()

            // then
            assertEquals(FAKE_MY_WAITING.waitingId, event.await())
        }

    @Test
    fun `cancelAndRegister 성공 시 cancelWaiting 호출 후 navigateToWaitingRegisterEvent 를 발행한다`() =
        runTest {
            // given
            advanceUntilIdle()
            everySuspend { myWaitingRepository.cancelWaiting(any()) } returns Result.success(Unit)

            // when
            val event = observeEvent(placeDetailViewModel.navigateToWaitingRegisterEvent)
            placeDetailViewModel.cancelAndRegister(FAKE_MY_WAITING.waitingId)
            advanceUntilIdle()

            // then
            verifySuspend { myWaitingRepository.cancelWaiting(FAKE_MY_WAITING.waitingId) }
            assertEquals(FAKE_PLACES.first().id, event.await())
        }

    @Test
    fun `cancelAndRegister 실패 시 cancelWaitingFailureEvent 를 발행한다`() =
        runTest {
            // given
            advanceUntilIdle()
            val exception = Throwable("웨이팅 취소 실패")
            everySuspend { myWaitingRepository.cancelWaiting(any()) } returns
                Result.failure(
                    exception,
                )

            // when
            val event = observeEvent(placeDetailViewModel.cancelWaitingFailureEvent)
            placeDetailViewModel.cancelAndRegister(FAKE_MY_WAITING.waitingId)
            advanceUntilIdle()

            // then
            assertEquals(exception, event.await())
        }
}
