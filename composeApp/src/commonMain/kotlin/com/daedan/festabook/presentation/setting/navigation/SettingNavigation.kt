package com.daedan.festabook.presentation.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.waitinginfo.WaitingInfoViewModel
import com.daedan.festabook.presentation.waitinginfo.component.AddWaitingInfoRoute

fun NavGraphBuilder.settingNavGraph(
    homeViewModel: HomeViewModel,
    settingViewModel: SettingViewModel,
    waitingInfoViewModel: WaitingInfoViewModel,
    notificationPermissionManager: NotificationPermissionManager,
    onShowSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit,
    onNavigateToAddWaitingInfo: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<MainTabRoute.Setting> {
        SettingRoute(
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
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
