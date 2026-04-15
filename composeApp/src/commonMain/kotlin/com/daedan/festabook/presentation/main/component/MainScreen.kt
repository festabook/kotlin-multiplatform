package com.daedan.festabook.presentation.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.compose.NavHost
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.FestabookSnackbar
import com.daedan.festabook.presentation.common.component.SnackbarManager
import com.daedan.festabook.presentation.common.component.rememberAppSnackbarManager
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.home.navigation.homeNavGraph
import com.daedan.festabook.presentation.main.FestabookMainTab
import com.daedan.festabook.presentation.main.FestabookNavigator
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.main.MainViewModel
import com.daedan.festabook.presentation.main.rememberFestabookNavigator
import com.daedan.festabook.presentation.news.NewsViewModel
import com.daedan.festabook.presentation.news.navigation.newsNavGraph
import com.daedan.festabook.presentation.placeMap.PlaceMapViewModel
import com.daedan.festabook.presentation.placeMap.component.PlaceMapRoute
import com.daedan.festabook.presentation.placeMap.intent.event.SelectEvent
import com.daedan.festabook.presentation.placeMap.navigation.placeMapNavGraph
import com.daedan.festabook.presentation.placeMap.platform.LocationSource
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import com.daedan.festabook.presentation.platform.Intent
import com.daedan.festabook.presentation.platform.RememberDeepLinkHandler
import com.daedan.festabook.presentation.schedule.ScheduleViewModel
import com.daedan.festabook.presentation.schedule.navigation.scheduleNavGraph
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.platform.rememberOpenAppSettings
import com.daedan.festabook.presentation.setting.navigation.settingNavGraph
import com.daedan.festabook.presentation.waitinginfo.WaitingInfoViewModel
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.back_press_exit_message
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Suppress("ktlint:compose:vm-forwarding-check")
fun MainScreen(
    appGraph: FestabookAppGraph,
    locationSource: LocationSource,
    onAppFinish: () -> Unit,
    festabookNavigator: FestabookNavigator,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    scheduleViewModel: ScheduleViewModel,
    placeMapViewModel: PlaceMapViewModel,
    newsViewModel: NewsViewModel,
    settingViewModel: SettingViewModel,
    waitingInfoViewModel: WaitingInfoViewModel,
    modifier: Modifier = Modifier,
) {
    val mainNavigator = rememberFestabookNavigator(MainTabRoute.Home)
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarManager = rememberAppSnackbarManager(snackbarHostState)
    val backPressExitMessage = stringResource(Res.string.back_press_exit_message)
    val openAppSettings = rememberOpenAppSettings()
    val state = rememberNavigationEventState(NavigationEventInfo.None)

    val notificationPermissionManager =
        rememberNotificationPermissionManager(
            notificationPermissionManagerFactory = appGraph.notificationPermissionManagerFactory,
            onPermissionGrant = { settingViewModel.saveNotificationId() },
            onPermissionDeny = { snackbarManager.showPermissionDeniedSnackbar(openAppSettings) },
        )

    ObserveAsEvents(flow = mainViewModel.navigateNewsEvent) {
        mainNavigator.navigateToMainTab(FestabookMainTab.NEWS)
    }
    ObserveAsEvents(flow = mainViewModel.backPressEvent) { isDoublePress ->
        if (isDoublePress) {
            onAppFinish()
        } else {
            snackbarManager.show(backPressExitMessage)
        }
    }
    ObserveAsEvents(flow = homeViewModel.navigateToScheduleEvent) {
        mainNavigator.navigateToMainTab(FestabookMainTab.SCHEDULE)
    }

    NavigationBackHandler(
        state = state,
    ) {
        mainViewModel.onBackPressed()
    }

    RememberDeepLinkHandler { intent ->
        handleNavigation(intent, newsViewModel, mainViewModel)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.imePadding(),
            ) { data ->
                FestabookSnackbar(data)
            }
        },
        bottomBar = {
            if (mainNavigator.shouldShowBottomBar) {
                FestabookBottomNavigationBar(
                    currentTab = mainNavigator.currentTab,
                    onTabSelect = { mainNavigator.navigateToMainTab(it) },
                    onTabReSelect = { tab ->
                        when (tab) {
                            FestabookMainTab.SCHEDULE -> {
                                scheduleViewModel.loadSchedules()
                            }

                            FestabookMainTab.PLACE_MAP -> {
                                placeMapViewModel.onPlaceMapEvent(SelectEvent.UnSelectPlace)
                                placeMapViewModel.onMenuItemReClicked()
                            }

                            else -> {
                                Unit
                            }
                        }
                    },
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        val isVisible = mainNavigator.currentTab == FestabookMainTab.PLACE_MAP
        PlaceMapRoute(
            modifier =
                Modifier
                    .graphicsLayer {
                        alpha = if (isVisible) 1f else 0f
                    }.padding(innerPadding)
                    .pointerInput(isVisible) {
                        if (!isVisible) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent(PointerEventPass.Initial)
                                        .changes
                                        .forEach { it.consume() }
                                }
                            }
                        }
                    },
            placeMapViewModel = placeMapViewModel,
            locationSource = locationSource,
//            logger = appGraph.defaultFirebaseLogger,
            onShowErrorSnackBar = snackbarManager::showError,
            onStartPlaceDetail = {
                mainNavigator.navigate(
                    FestabookRoute.PlaceDetail(
                        placeDetailUiModel = it.placeDetail.value,
                    ),
                )
            },
        )
        FestabookNavHost(
            innerPadding = innerPadding,
            festabookNavigator = festabookNavigator,
            navigator = mainNavigator,
            mainViewModel = mainViewModel,
            homeViewModel = homeViewModel,
            scheduleViewModel = scheduleViewModel,
            settingViewModel = settingViewModel,
            waitingInfoViewModel = waitingInfoViewModel,
            newsViewModel = newsViewModel,
            notificationPermissionManager = notificationPermissionManager,
            snackbarManager = snackbarManager,
        )
    }
}

@Composable
private fun FestabookNavHost(
    innerPadding: PaddingValues,
    navigator: FestabookNavigator,
    festabookNavigator: FestabookNavigator,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    scheduleViewModel: ScheduleViewModel,
    newsViewModel: NewsViewModel,
    settingViewModel: SettingViewModel,
    waitingInfoViewModel: WaitingInfoViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    snackbarManager: SnackbarManager,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        startDestination = navigator.startRoute,
        navController = navigator.navController,
    ) {
        homeNavGraph(
            innerPadding = innerPadding,
            viewModel = homeViewModel,
            mainViewModel = mainViewModel,
            onNavigateToExplore = { festabookNavigator.navigate(FestabookRoute.Explore) },
            onSubscriptionConfirm = {
                settingViewModel.notificationAllowClick()
                mainViewModel.declineAlert()
            },
            onShowSnackbar = snackbarManager::show,
            onShowErrorSnackbar = snackbarManager::showError,
            settingViewModel = settingViewModel,
            notificationPermissionManager = notificationPermissionManager,
        )
        scheduleNavGraph(
            innerPadding = innerPadding,
            viewModel = scheduleViewModel,
            onShowErrorSnackbar = snackbarManager::showError,
        )
        placeMapNavGraph(
            innerPadding = innerPadding,
            onBackToPreviousClick = { navigator.popBackStack() },
            onShowErrorSnackbar = snackbarManager::showError,
        )
        newsNavGraph(
            innerPadding = innerPadding,
            viewModel = newsViewModel,
            onShowErrorSnackbar = snackbarManager::showError,
        )
        settingNavGraph(
            innerPadding = innerPadding,
            homeViewModel = homeViewModel,
            settingViewModel = settingViewModel,
            waitingInfoViewModel = waitingInfoViewModel,
            notificationPermissionManager = notificationPermissionManager,
            onShowSnackBar = snackbarManager::show,
            onShowErrorSnackBar = snackbarManager::showError,
            onNavigateToAddWaitingInfo = { navigator.navigate(FestabookRoute.AddWaitingInfo) },
            onBackClick = { navigator.popBackStack() },
        )
    }
}

private fun handleNavigation(
    intent: Intent,
    newsViewModel: NewsViewModel,
    mainViewModel: MainViewModel,
) {
    val noticeIdToExpand =
        intent.getLongExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND, DeepLinkKeys.INITIALIZED_ID)
    if (noticeIdToExpand != DeepLinkKeys.INITIALIZED_ID) newsViewModel.expandNotice(noticeIdToExpand)
    val canNavigateToNews = intent.getBooleanExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS, false)
    if (canNavigateToNews) mainViewModel.navigateToNews()
}
