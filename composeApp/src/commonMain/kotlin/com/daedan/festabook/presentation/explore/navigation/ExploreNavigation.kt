package com.daedan.festabook.presentation.explore.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.explore.component.ExploreScreen
import com.daedan.festabook.presentation.main.FestabookRoute
import dev.zacsweers.metrox.viewmodel.metroViewModel

fun NavGraphBuilder.exploreNavGraph(
    onBackClick: () -> Unit,
    onNavigateToMain: () -> Unit,
) {
    composable<FestabookRoute.Explore> {
        ExploreScreen(
            viewModel = metroViewModel(),
            onBackClick = onBackClick,
            onNavigateToMain = { onNavigateToMain() },
        )
    }
}
