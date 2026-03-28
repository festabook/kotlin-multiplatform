package com.daedan.festabook.splash

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.presentation.splash.SplashUiState
import com.daedan.festabook.presentation.splash.SplashViewModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var festivalLocalDataSource: FestivalLocalDataSource
    private lateinit var splashViewModel: SplashViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        festivalLocalDataSource = mock(MockMode.autofill)
        splashViewModel = SplashViewModel(festivalLocalDataSource, iODispatcher = testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `앱 업데이트가 있다면 업데이트 다이얼로그를 표시한다`() =
        runTest {
            // given
            val updateResult = Result.success(true)

            // when
            splashViewModel.handleVersionCheckResult(updateResult)

            // then
            assertEquals(SplashUiState.ShowUpdateDialog, splashViewModel.uiState.value)
        }

    @Test
    fun `앱 업데이트 확인에 실패하면 네트워크 에러 다이얼로그를 표시한다`() =
        runTest {
            // given
            val updateResult = Result.failure<Boolean>(Exception("Network Error"))

            // when
            splashViewModel.handleVersionCheckResult(updateResult)

            // then
            assertEquals(SplashUiState.ShowNetworkErrorDialog, splashViewModel.uiState.value)
        }

    @Test
    fun `앱 업데이트가 없고 접속한 대학교가 있다면 MainActivity로 이동한다`() =
        runTest {
            every { festivalLocalDataSource.getFestivalId() } returns flowOf(1L)
            val updateResult = Result.success(false)

            // when
            splashViewModel.handleVersionCheckResult(updateResult)

            // then
            assertEquals(SplashUiState.NavigateToMain(1L), splashViewModel.uiState.value)
            verify(VerifyMode.exactly(1)) { festivalLocalDataSource.getFestivalId() }
        }

    @Test
    fun `앱 업데이트가 없고 접속한 대학교가 없다면 ExploreActivity로 이동한다`() =
        runTest {
            // given
            every { festivalLocalDataSource.getFestivalId() } returns flowOf(null)
            val updateResult = Result.success(false)

            // when
            splashViewModel.handleVersionCheckResult(updateResult)

            // then
            assertEquals(SplashUiState.NavigateToExplore, splashViewModel.uiState.value)
            verify(VerifyMode.exactly(1)) { festivalLocalDataSource.getFestivalId() }
        }
}
