package com.daedan.festabook

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.SettingRoute
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
                SettingRoute(
                    notificationPermissionManager =
                        rememberNotificationPermissionManager(
                            notificationPermissionManagerFactory = notificationPermissionManagerFactory,
                            onPermissionGrant = {},
                            onPermissionDeny = {},
                        ),
                    onShowSnackBar = {},
                    onShowErrorSnackBar = {},
                )
            }
        }
    }
