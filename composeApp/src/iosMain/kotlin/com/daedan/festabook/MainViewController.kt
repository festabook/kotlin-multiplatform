package com.daedan.festabook

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.common.component.FestabookSnackbar
import com.daedan.festabook.presentation.common.component.rememberAppSnackbarManager
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.setting.component.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.platform.rememberOpenAppSettings
import com.daedan.festabook.presentation.theme.FestabookTheme
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.metroViewModel

private val iosAppGraph = createGraph<IosAppGraph>()
private val notificationPermissionManagerFactory = iosAppGraph.notificationPermissionManagerFactory

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarManager = rememberAppSnackbarManager(snackbarHostState)
            val openAppSettings = rememberOpenAppSettings()
            Scaffold(snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    FestabookSnackbar(data)
                }
            }) { innerPadding ->
                val viewModel = metroViewModel<SettingViewModel>()
                SettingRoute(
                    notificationPermissionManager =
                        rememberNotificationPermissionManager(
                            notificationPermissionManagerFactory = notificationPermissionManagerFactory,
                            onPermissionGrant = {
                                viewModel.saveNotificationId()
                            },
                            onPermissionDeny = {
                                snackbarManager.showPermissionDeniedSnackbar(
                                    openAppSettings,
                                )
                            },
                        ),
                    onShowSnackBar = {},
                    onShowErrorSnackBar = {},
                )
            }
        }
//            FestabookScreen(
//                onAppFinish = { exit(0) },
//            )
    }
