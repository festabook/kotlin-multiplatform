package com.daedan.festabook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.presentation.common.component.FestabookSnackbar
import com.daedan.festabook.presentation.common.component.rememberAppSnackbarManager
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.SettingRoute
import com.daedan.festabook.presentation.setting.component.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.platform.rememberOpenAppSettings
import com.daedan.festabook.presentation.theme.FestabookTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    private val androidAppGraph by lazy { (application as FestabookApp).androidAppGraph }
    private val notificationPermissionManagerFactory by lazy {
        androidAppGraph.notificationPermissionManagerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            FestabookTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarManager = rememberAppSnackbarManager(snackbarHostState)
                val openAppSettings = rememberOpenAppSettings()
                Scaffold(snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { data ->
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
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    FestabookTheme {}
}
