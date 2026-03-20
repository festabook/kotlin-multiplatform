package com.daedan.festabook.presentation.splash.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.splash.component.SplashScreen
import com.daedan.festabook.presentation.splash.platform.AppVersionManager
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

fun NavGraphBuilder.splashNavGraph(
    appVersionManager: AppVersionManager,
    onNavigateToExplore: () -> Unit,
    onNavigateToMain: (Long) -> Unit,
    onFinishApp: () -> Unit,
) {
    composable<FestabookRoute.Splash> {
        val viewModelFactory = LocalMetroViewModelFactory.current
        SplashScreen(
            viewModel = viewModel(factory = viewModelFactory),
            appVersionManager = appVersionManager,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToMain = onNavigateToMain,
            onFinishApp = onFinishApp,
        )
    }
}
