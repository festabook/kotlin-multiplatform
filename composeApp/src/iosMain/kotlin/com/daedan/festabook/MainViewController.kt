package com.daedan.festabook

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.common.component.FestabookSnackbar
import com.daedan.festabook.presentation.common.component.rememberAppSnackbarManager
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.setting.component.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.platform.rememberOpenAppSettings
import com.daedan.festabook.presentation.theme.FestabookTheme
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

private val iosAppGraph = createGraph<IosAppGraph>()
private val metroVmf = iosAppGraph.metroViewModelFactory
private val notificationPermissionManagerFactory =
    iosAppGraph.notificationPermissionManagerFactory

@Suppress("ktlint:standard:function-naming")
@Inject
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarManager = rememberAppSnackbarManager(snackbarHostState)
                val openAppSettings = rememberOpenAppSettings()
                Scaffold(snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        FestabookSnackbar(data)
                    }
                }) { innerPadding ->
                    SettingRoute(
                        notificationPermissionManager =
                            rememberNotificationPermissionManager(
                                notificationPermissionManagerFactory = notificationPermissionManagerFactory,
                                onPermissionGrant = {},
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
        }
    }
