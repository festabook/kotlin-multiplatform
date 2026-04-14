package com.daedan.festabook.placeMap.placeDetail

import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.news.FAKE_NOTICES
import com.daedan.festabook.placeMap.FAKE_PLACES
import com.daedan.festabook.presentation.news.notice.model.toUiModel
import com.daedan.festabook.presentation.placeMap.model.toUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.PlaceDetailViewModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingStatusUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingTeamUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.toUiModel
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
    private lateinit var placeDetailViewModel: PlaceDetailViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        placeDetailRepository = mock()
        waitingRegisterInfoRepository = mock()
        everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
            Result.success(
                FAKE_PLACE_DETAIL,
            )
        everySuspend { waitingRegisterInfoRepository.getPlaceWaiting(any()) } returns
            Result.success(FAKE_PLACE_WAITING)
        placeDetailViewModel =
            PlaceDetailViewModel(
                placeDetailRepository,
                waitingRegisterInfoRepository,
                FAKE_PLACES.first().toUiModel(),
                null,
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
    fun `처음 뷰모델 생성 시에 플레이스 상세 정보가 있다면 서버에 요청하지 않는다`() =
        runTest {
            // given
            val expected = FAKE_PLACE_DETAIL.toUiModel()
            val placeDetailRepository = mock<PlaceDetailRepository>()

            // when
            placeDetailViewModel =
                PlaceDetailViewModel(placeDetailRepository, waitingRegisterInfoRepository, null, expected)
            advanceUntilIdle()

            // then
            verifySuspend(VerifyMode.exactly(0)) { placeDetailRepository.getPlaceDetail(any()) }
            val actual = placeDetailViewModel.placeDetail.value
            assertEquals(
                PlaceDetailUiState.Success(
                    placeDetail = expected,
                    waitingTeam = WaitingTeamUiState.Success(totalTeams = FAKE_PLACE_WAITING.totalWaitingTeams),
                    waitingStatus = WaitingStatusUiState.Active(estimatedMinutes = FAKE_PLACE_WAITING.estimatedWaitTime),
                ),
                actual,
            )
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
}
