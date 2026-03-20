package com.daedan.festabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.daedan.festabook.presentation.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.theme.FestabookTheme
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    private val androidAppGraph by lazy { (application as FestabookApp).androidAppGraph }
    private val metroVmf by lazy { androidAppGraph.metroViewModelFactory }
    private val notificationPermissionManagerFactory by lazy {
        androidAppGraph.notificationPermissionManagerFactory
    }
    private val contextFactory by lazy {
        androidAppGraph.contextFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            FestabookTheme {
                CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                    SettingRoute(
                        notificationPermissionManager =
                            rememberNotificationPermissionManager(
                                contextFactory = contextFactory,
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
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    FestabookTheme {}
}
