package com.daedan.festabook.presentation.home.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.home.component.HomeScreen
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.main.MainViewModel
import com.daedan.festabook.presentation.main.component.FirstVisitDialog
import com.daedan.festabook.presentation.setting.SettingViewModel

fun NavGraphBuilder.homeNavGraph(
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    settingViewModel: SettingViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    onShowSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    onSubscriptionConfirm: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToFestating: () -> Unit,
) {
    composable<MainTabRoute.Home> {
        val isFirstVisit by mainViewModel.isFirstVisit.collectAsStateWithLifecycle()
        if (isFirstVisit) {
            FirstVisitDialog(
                onConfirm = { onSubscriptionConfirm() },
                onDecline = { mainViewModel.declineAlert() },
            )
        }
        HomeScreen(
            homeViewModel = homeViewModel,
            onShowErrorSnackbar = onShowErrorSnackbar,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToFestating = onNavigateToFestating,
            settingViewModel = settingViewModel,
            notificationPermissionManager = notificationPermissionManager,
            onShowSnackBar = onShowSnackbar,
        )
    }
}
