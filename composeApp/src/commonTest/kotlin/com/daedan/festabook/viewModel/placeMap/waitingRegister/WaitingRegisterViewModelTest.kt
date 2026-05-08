package com.daedan.festabook.viewModel.placeMap.waitingRegister

import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.observeEvent
import com.daedan.festabook.placeMap.placeDetail.FAKE_MY_WAITING
import com.daedan.festabook.placeMap.placeDetail.FAKE_PLACE_DETAIL
import com.daedan.festabook.presentation.placeMap.waitingRegister.WaitingRegisterViewModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiState
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.toWaitingPlaceSummaryUiModel
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
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WaitingRegisterViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var placeDetailRepository: PlaceDetailRepository
    private lateinit var waitingInfoRepository: WaitingInfoRepository
    private lateinit var waitingRegisterInfoRepository: WaitingRegisterInfoRepository
    private lateinit var viewModel: WaitingRegisterViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        placeDetailRepository = mock()
        waitingInfoRepository = mock()
        waitingRegisterInfoRepository = mock()
        everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
            Result.success(
                FAKE_PLACE_DETAIL,
            )
        everySuspend { waitingInfoRepository.getWaitingInfo() } returns
            Result.success(
                WaitingInfo(
                    phoneNumber = "010-1234-5678",
                ),
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): WaitingRegisterViewModel =
        WaitingRegisterViewModel(
            placeDetailRepository = placeDetailRepository,
            waitingInfoRepository = waitingInfoRepository,
            waitingRegisterInfoRepository = waitingRegisterInfoRepository,
            placeId = FAKE_PLACE_DETAIL.place.id,
        )

    @Test
    fun `전화번호 미등록 상태에서 초기화 시 NeedsPhoneRegistration 상태가 된다`() =
        runTest {
            // given
            everySuspend { waitingInfoRepository.getWaitingInfo() } returns Result.success(null)

            // when
            viewModel = createViewModel()
            advanceUntilIdle()

            // then
            assertEquals(WaitingRegisterUiState.NeedsPhoneRegistration, viewModel.uiState.value)
        }

    @Test
    fun `WaitingInfo 로드에 실패하면 Error 상태가 되고 PlaceDetail 을 조회하지 않는다`() =
        runTest {
            // given
            val exception = Throwable("정보 조회 실패")
            everySuspend { waitingInfoRepository.getWaitingInfo() } returns Result.failure(exception)

            // when
            viewModel = createViewModel()
            advanceUntilIdle()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Error>(state)
            assertEquals(exception, state.throwable)
            verifySuspend(VerifyMode.not) {
                placeDetailRepository.getPlaceDetail(any())
            }
        }

    @Test
    fun `초기화 시 PlaceDetail 로드에 성공하면 Success 상태가 된다`() =
        runTest {
            // when
            viewModel = createViewModel()
            advanceUntilIdle()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(
                FAKE_PLACE_DETAIL.toWaitingPlaceSummaryUiModel(),
                state.waitingRegister.placeSummary,
            )
        }

    @Test
    fun `초기화 시 PlaceDetail 로드에 실패하면 Error 상태가 된다`() =
        runTest {
            // given
            val exception = Throwable("로드 실패")
            everySuspend { placeDetailRepository.getPlaceDetail(any()) } returns
                Result.failure(
                    exception,
                )

            // when
            viewModel = createViewModel()
            advanceUntilIdle()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Error>(state)
            assertEquals(exception, state.throwable)
        }

    @Test
    fun `increasePartySize 호출 시 partySize 가 1 증가한다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()

            // when
            viewModel.increasePartySize()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(WaitingRegisterUiModel.MIN_PARTY_SIZE + 1, state.waitingRegister.partySize)
        }

    @Test
    fun `partySize 가 MAX 일 때 increasePartySize 를 호출해도 변경되지 않는다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            repeat(WaitingRegisterUiModel.MAX_PARTY_SIZE - WaitingRegisterUiModel.MIN_PARTY_SIZE) {
                viewModel.increasePartySize()
            }

            // when
            viewModel.increasePartySize()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(WaitingRegisterUiModel.MAX_PARTY_SIZE, state.waitingRegister.partySize)
        }

    @Test
    fun `decreasePartySize 호출 시 partySize 가 1 감소한다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            viewModel.increasePartySize()

            // when
            viewModel.decreasePartySize()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(WaitingRegisterUiModel.MIN_PARTY_SIZE, state.waitingRegister.partySize)
        }

    @Test
    fun `partySize 가 MIN 일 때 decreasePartySize 를 호출해도 변경되지 않는다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()

            // when
            viewModel.decreasePartySize()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(WaitingRegisterUiModel.MIN_PARTY_SIZE, state.waitingRegister.partySize)
        }

    @Test
    fun `toggleServiceAgreement 호출 시 isServiceAgreed 가 토글된다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()

            // when
            viewModel.toggleServiceAgreement()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertTrue(state.waitingRegister.isServiceAgreed)
        }

    @Test
    fun `약관 미동의 상태에서 submitWaitingRegister 를 호출해도 API 를 호출하지 않는다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()

            // when - isServiceAgreed 가 false 인 기본 상태
            viewModel.submitWaitingRegister()
            advanceUntilIdle()

            // then
            verifySuspend(VerifyMode.not) {
                waitingRegisterInfoRepository.registerWaiting(any(), any())
            }
        }

    @Test
    fun `약관 동의 후 submitWaitingRegister 성공 시 registerSuccessEvent 를 발행한다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            everySuspend { waitingRegisterInfoRepository.registerWaiting(any(), any()) } returns
                Result.success(FAKE_MY_WAITING)
            viewModel.toggleServiceAgreement()

            // when
            val event = observeEvent(viewModel.registerSuccessEvent)
            viewModel.submitWaitingRegister()
            advanceUntilIdle()

            // then
            assertEquals(Unit, event.await())
        }

    @Test
    fun `submitWaitingRegister 성공 후 Success 상태가 유지되고 isSubmitting 이 false 가 된다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            everySuspend { waitingRegisterInfoRepository.registerWaiting(any(), any()) } returns
                Result.success(FAKE_MY_WAITING)
            viewModel.toggleServiceAgreement()

            // when
            viewModel.submitWaitingRegister()
            advanceUntilIdle()

            // then
            val state = viewModel.uiState.value
            assertIs<WaitingRegisterUiState.Success>(state)
            assertEquals(false, state.waitingRegister.isSubmitting)
        }

    @Test
    fun `partySize 를 변경하면 registerWaiting 에 변경된 값이 전달된다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            everySuspend { waitingRegisterInfoRepository.registerWaiting(any(), any()) } returns
                Result.success(FAKE_MY_WAITING)
            viewModel.increasePartySize()
            viewModel.increasePartySize()
            viewModel.toggleServiceAgreement()

            // when
            viewModel.submitWaitingRegister()
            advanceUntilIdle()

            // then
            verifySuspend {
                waitingRegisterInfoRepository.registerWaiting(
                    placeId = FAKE_PLACE_DETAIL.place.id,
                    partySize = WaitingRegisterUiModel.MIN_PARTY_SIZE + 2,
                )
            }
        }

    @Test
    fun `submitWaitingRegister 실패 시 registerFailureEvent 를 발행하고 Success 상태를 유지한다`() =
        runTest {
            // given
            viewModel = createViewModel()
            advanceUntilIdle()
            val exception = Throwable("등록 실패")
            everySuspend { waitingRegisterInfoRepository.registerWaiting(any(), any()) } returns
                Result.failure(exception)
            viewModel.toggleServiceAgreement()

            // when
            val event = observeEvent(viewModel.registerFailureEvent)
            viewModel.submitWaitingRegister()
            advanceUntilIdle()

            // then
            assertEquals(exception, event.await())
            assertIs<WaitingRegisterUiState.Success>(viewModel.uiState.value)
        }
}
