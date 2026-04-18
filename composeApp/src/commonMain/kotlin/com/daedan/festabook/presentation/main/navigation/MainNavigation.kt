package com.daedan.festabook.presentation.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.main.FestabookNavigator
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.main.component.MainScreen
import com.daedan.festabook.presentation.placeMap.platform.LocationSource
import dev.zacsweers.metrox.viewmodel.metroViewModel

fun NavGraphBuilder.mainNavGraph(
    onAppFinish: () -> Unit,
    appGraph: FestabookAppGraph,
    locationSource: LocationSource,
    festabookNavigator: FestabookNavigator,
) {
    composable<FestabookRoute.Main> {
        val mainBackEntry =
            festabookNavigator.navController.getBackStackEntry<FestabookRoute.Main>()
        MainScreen(
            appGraph = appGraph,
            locationSource = locationSource,
            onAppFinish = onAppFinish,
            festabookNavigator = festabookNavigator,
            homeViewModel = metroViewModel(mainBackEntry),
            scheduleViewModel = metroViewModel(mainBackEntry),
            placeMapViewModel = metroViewModel(mainBackEntry),
            settingViewModel = metroViewModel(mainBackEntry),
            waitingInfoViewModel = metroViewModel(mainBackEntry),
            mainViewModel = metroViewModel(mainBackEntry),
            newsViewModel = metroViewModel(mainBackEntry),
        )
    }
}
