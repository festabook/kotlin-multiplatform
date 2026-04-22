package com.daedan.festabook.presentation.home.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    settingViewModel: SettingViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    onShowSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    onSubscriptionConfirm: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToMyWaiting: () -> Unit,
) {
    composable<MainTabRoute.Home>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        val isFirstVisit by mainViewModel.isFirstVisit.collectAsStateWithLifecycle()
        if (isFirstVisit) {
            FirstVisitDialog(
                onConfirm = { onSubscriptionConfirm() },
                onDecline = { mainViewModel.declineAlert() },
            )
        }
        HomeScreen(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            onShowErrorSnackbar = onShowErrorSnackbar,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToMyWaiting = onNavigateToMyWaiting,
            settingViewModel = settingViewModel,
            notificationPermissionManager = notificationPermissionManager,
            onShowSnackBar = onShowSnackbar,
        )
    }
}
