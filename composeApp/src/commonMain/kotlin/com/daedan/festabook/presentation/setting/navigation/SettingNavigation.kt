package com.daedan.festabook.presentation.setting.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.setting.waitinginfo.WaitingInfoViewModel
import com.daedan.festabook.presentation.setting.waitinginfo.component.AddWaitingInfoRoute

fun NavGraphBuilder.settingNavGraph(
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel,
    settingViewModel: SettingViewModel,
    waitingInfoViewModel: WaitingInfoViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    onShowSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit,
    onNavigateToAddWaitingInfo: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<MainTabRoute.Setting>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        SettingRoute(
            modifier = Modifier.padding(innerPadding),
            homeViewModel = homeViewModel,
            settingViewModel = settingViewModel,
            waitingInfoViewModel = waitingInfoViewModel,
            notificationPermissionManager = notificationPermissionManager,
            onShowSnackBar = onShowSnackBar,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onPhoneNumberClick = onNavigateToAddWaitingInfo,
        )
    }
    composable<FestabookRoute.AddWaitingInfo> {
        AddWaitingInfoRoute(
            viewModel = waitingInfoViewModel,
            onBackClick = onBackClick,
            onShowSnackBar = onShowSnackBar,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
