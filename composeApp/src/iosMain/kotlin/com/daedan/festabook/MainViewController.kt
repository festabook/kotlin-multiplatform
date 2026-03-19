package com.daedan.festabook

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.platform.rememberAppVersionManager
import com.daedan.festabook.presentation.splash.component.SplashScreen
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.skydoves.landscapist.coil3.LocalCoilImageLoader
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import platform.posix.exit

private val festabookAppGraph = createGraph<IosAppGraph>()
private val metroVmf = festabookAppGraph.metroViewModelFactory

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides metroVmf,
                LocalCoilImageLoader provides SingletonImageLoader.get(LocalPlatformContext.current),
            ) {
                Scaffold { innerPadding ->
                    SplashScreen(
                        viewModel = viewModel(factory = metroVmf),
                        appVersionManager =
                            rememberAppVersionManager(
                                appGraph = festabookAppGraph,
                                onUpdateFailure = {},
                                onUpdateSuccess = {},
                            ),
                        onNavigateToMain = {},
                        onNavigateToExplore = {},
                        onFinishApp = { exit(0) },
                    )
                }
            }
        }
    }
