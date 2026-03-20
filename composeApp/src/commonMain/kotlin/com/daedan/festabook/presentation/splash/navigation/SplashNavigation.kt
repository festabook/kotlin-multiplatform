package com.daedan.festabook.presentation.splash.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.splash.component.SplashScreen
import com.daedan.festabook.presentation.splash.platform.AppVersionManager

fun NavGraphBuilder.splashNavGraph(
    appGraph: FestabookAppGraph,
    appVersionManager: AppVersionManager,
    onNavigateToExplore: () -> Unit,
    onNavigateToMain: (Long) -> Unit,
    onFinishApp: () -> Unit,
) {
    composable<FestabookRoute.Splash> {
        SplashScreen(
            viewModel = viewModel(factory = appGraph.metroViewModelFactory),
            appVersionManager = appVersionManager,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToMain = onNavigateToMain,
            onFinishApp = onFinishApp,
        )
    }
}
