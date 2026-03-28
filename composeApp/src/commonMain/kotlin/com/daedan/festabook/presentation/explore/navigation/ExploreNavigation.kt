package com.daedan.festabook.presentation.explore.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.explore.component.ExploreScreen
import com.daedan.festabook.presentation.main.FestabookRoute
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

fun NavGraphBuilder.exploreNavGraph(
    onBackClick: () -> Unit,
    onNavigateToMain: () -> Unit,
) {
    composable<FestabookRoute.Explore> {
        val viewModelFactory = LocalMetroViewModelFactory.current
        ExploreScreen(
            viewModel = viewModel(factory = viewModelFactory),
            onBackClick = onBackClick,
            onNavigateToMain = { onNavigateToMain() },
        )
    }
}
