package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daedan.festabook.logging.ScreenViewLogger
import com.daedan.festabook.logging.logClick
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.festating.component.FestatingPoster
import com.daedan.festabook.presentation.home.FestivalUiState
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.home.LineupUiState
import com.daedan.festabook.presentation.home.WaitingBarUiState
import com.daedan.festabook.presentation.home.model.FestivalPosterUiModel
import com.daedan.festabook.presentation.home.model.FestivalUiModel
import com.daedan.festabook.presentation.home.model.LineUpItemGroupUiModel
import com.daedan.festabook.presentation.home.model.LineupItemUiModel
import com.daedan.festabook.presentation.home.model.OrganizationUiModel
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.format_festival_period_date
import festabookkmp.composeapp.generated.resources.format_festival_period_year
import festabookkmp.composeapp.generated.resources.setting_notice_enabled
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    settingViewModel: SettingViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    onNavigateToExplore: () -> Unit,
    onNavigateToFestating: () -> Unit,
    onNavigateToMyWaiting: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenViewLogger("HomeScreen")

    val festivalUiState by homeViewModel.festivalUiState.collectAsStateWithLifecycle()
    val lineupUiState by homeViewModel.lineupUiState.collectAsStateWithLifecycle()
    val waitingBarUiState by homeViewModel.waitingBarUiState.collectAsStateWithLifecycle()
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    val settingEnabledText = stringResource(Res.string.setting_notice_enabled)

    ObserveAsEvents(flow = settingViewModel.permissionCheckEvent) {
        notificationPermissionManager.requestPermission()
    }

    ObserveAsEvents(flow = settingViewModel.success) {
        onShowSnackBar(settingEnabledText)
    }

    ObserveAsEvents(flow = settingViewModel.error) {
        currentOnShowErrorSnackbar(it)
    }

    ObserveAsEvents(flow = homeViewModel.navigateToMyWaitingEvent) {
        onNavigateToMyWaiting()
    }

    LaunchedEffect(Unit) {
        homeViewModel.loadWaitingBar()
    }

    LaunchedEffect(festivalUiState) {
        when (val state = festivalUiState) {
            is FestivalUiState.Error -> {
                currentOnShowErrorSnackbar(state.throwable)
            }

            else -> {}
        }
    }

    val loggedOnNavigateToExplore =
        logClick(
            identifier = "navigate_to_explore",
            screenName = "HomeScreen",
            onClick = onNavigateToExplore,
        )
    val loggedOnNavigateToFestating =
        logClick(
            identifier = "navigate_to_festating",
            screenName = "HomeScreen",
            onClick = onNavigateToFestating,
        )
    val loggedOnNavigateToSchedule =
        logClick(
            identifier = "schedule_tab_click",
            screenName = "HomeScreen",
            onClick = homeViewModel::navigateToScheduleClick,
        )
    val loggedOnNavigateToMyWaiting =
        logClick(
            identifier = "waiting_bar_click",
            screenName = "HomeScreen",
            onClick = homeViewModel::navigateToMyWaitingClick,
        )

    when (val state = festivalUiState) {
        is FestivalUiState.Loading -> {
            LoadingStateScreen(modifier = modifier)
        }

        is FestivalUiState.Error -> {
            ErrorStateScreen()
        }

        is FestivalUiState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                HomeContent(
                    festivalUiState = state,
                    lineupUiState = lineupUiState,
                    showWaitingBar = waitingBarUiState is WaitingBarUiState.Visible,
                    onNavigateToExplore = loggedOnNavigateToExplore,
                    onNavigateToSchedule = loggedOnNavigateToSchedule,
                    onFestatingClick = loggedOnNavigateToFestating,
                )
                when (val waitingBarUiState = waitingBarUiState) {
                    is WaitingBarUiState.Visible -> {
                        HomeWaitingBar(
                            order = waitingBarUiState.order,
                            estimatedMinutes = waitingBarUiState.estimatedWaitTime,
                            status = waitingBarUiState.status,
                            onClick = loggedOnNavigateToMyWaiting,
                            modifier =
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(
                                        start = festabookSpacing.paddingBody4,
                                        end = festabookSpacing.paddingBody4,
                                        bottom = festabookSpacing.paddingBody4,
                                    ),
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    festivalUiState: FestivalUiState.Success,
    lineupUiState: LineupUiState,
    showWaitingBar: Boolean,
    onNavigateToExplore: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onFestatingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val universityName = festivalUiState.organization.organizationName

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestabookColor.white,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            HomeHeader(
                universityName = universityName,
                onTitleClick = onNavigateToExplore,
                modifier =
                    Modifier.padding(
                        top = festabookSpacing.paddingTitleHorizontal,
                        bottom = festabookSpacing.paddingBody3,
                    ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize(),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                // 포스터 리스트
                item {
                    val posterUrls =
                        festivalUiState.organization.festival.festivalImages
                            .sortedBy { it.sequence }
                            .map { it.imageUrl }

                    HomePosterList(
                        posterUrls = posterUrls,
                        modifier = Modifier.padding(vertical = festabookSpacing.paddingBody3),
                    )
                }

                // 축제 정보
                item {
                    val festival = festivalUiState.organization.festival
                    HomeFestivalInfo(
                        festivalName = festival.festivalName,
                        festivalDate =
                            formatFestivalPeriod(
                                festival.startDate,
                                festival.endDate,
                            ),
                        instagramLink = festival.instagramLink,
                        homepageLink = festival.homepageLink,
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody4),
                    )
                }

                // 구분선
                item {
                    HorizontalDivider(
                        thickness = 4.dp,
                        color = FestabookColor.gray200,
                        modifier =
                            Modifier
                                .padding(top = festabookSpacing.paddingBody4),
                    )
                }
                // 페스타팅 포스터
                if (festivalUiState.organization.festival.festatingVisible) {
                    item {
                        FestatingPoster(
                            onClick = onFestatingClick,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(
                                        vertical = festabookSpacing.paddingBody4,
                                        horizontal = festabookSpacing.paddingBody5,
                                    ),
                        )
                    }

                    // 구분선
                    item {
                        HorizontalDivider(
                            thickness = 4.dp,
                            color = FestabookColor.gray200,
                        )
                    }
                }

                // 라인업 헤더
                item {
                    HomeLineupHeader(
                        onScheduleClick = onNavigateToSchedule,
                    )
                }

                // 라인업 리스트
                when (lineupUiState) {
                    is LineupUiState.Success -> {
                        items(
                            items = lineupUiState.lineups,
                            key = { it.id },
                        ) { lineupItem ->
                            HomeLineupItem(uiModel = lineupItem)
                        }
                    }

                    is LineupUiState.Loading -> {
                        // 로딩 시 동작 논의 후 추가
                    }

                    is LineupUiState.Error -> {
                        // 에러 표시
                    }
                }

                // 후원사 배너
                val sponsors = festivalUiState.organization.festival.sponsors
                if (sponsors.isNotEmpty()) {
                    item {
                        HorizontalDivider(
                            thickness = 4.dp,
                            color = FestabookColor.gray200,
                            modifier = Modifier.padding(vertical = festabookSpacing.paddingBody4),
                        )
                    }
                    item {
                        HomeSponsorBanner(
                            sponsors = sponsors,
                        )
                    }
                }

                // 하단 여백 추가
                item {
                    Spacer(
                        modifier =
                            Modifier.padding(
                                bottom = if (showWaitingBar) 80.dp else festabookSpacing.paddingBody5,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun formatFestivalPeriod(
    start: LocalDate,
    end: LocalDate,
): String {
    val startYear = stringResource(Res.string.format_festival_period_year, start.year)
    val endYear =
        if (start.year == end.year) {
            ""
        } else {
            stringResource(
                Res.string.format_festival_period_year,
                end.year,
            )
        }

    val startDate =
        stringResource(Res.string.format_festival_period_date, start.month.number, start.day)
    val endDate = stringResource(Res.string.format_festival_period_date, end.month.number, end.day)
    return "$startYear$startDate ~ $endYear$endDate"
}

@Preview(showBackground = true)
@Composable
private fun FestivalOverviewPreview() {
    val sampleFestival =
        OrganizationUiModel(
            id = 1,
            organizationName = "가천대학교",
            festival =
                FestivalUiModel(
                    id = 1L,
                    festivalName = "2025 가천 Water Festival\n: AQUA WAVE",
                    startDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                    endDate =
                        Clock.System
                            .todayIn(TimeZone.currentSystemDefault())
                            .plus(DatePeriod(days = 1)),
                    festivalImages =
                        listOf(
                            FestivalPosterUiModel(1, "sample", 1),
                            FestivalPosterUiModel(2, "sample", 2),
                        ),
                    festatingVisible = true,
                    sponsors = emptyList(),
                    instagramLink = null,
                    homepageLink = null,
                ),
        )

    val sampleLineups =
        LineUpItemGroupUiModel(
            group =
                mapOf(
                    Clock.System.todayIn(TimeZone.currentSystemDefault()) to
                        listOf(
                            LineupItemUiModel(
                                1,
                                "sample",
                                "실리카겔",
                                Clock.System
                                    .now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                            ),
                            LineupItemUiModel(
                                2,
                                "sample",
                                "아이유",
                                Clock.System
                                    .now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                            ),
                        ),
                    Clock.System
                        .todayIn(TimeZone.currentSystemDefault())
                        .plus(DatePeriod(days = 1)) to
                        listOf(
                            LineupItemUiModel(
                                3,
                                "sample",
                                "뉴진스",
                                Clock.System
                                    .now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                            ),
                        ),
                ),
        )

    HomeContent(
        festivalUiState = FestivalUiState.Success(sampleFestival),
        lineupUiState = LineupUiState.Success(sampleLineups.getLineupItems()),
        showWaitingBar = false,
        onNavigateToExplore = {},
        onNavigateToSchedule = {},
        onFestatingClick = {},
    )
}
