package com.daedan.festabook.presentation.festating.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.daedan.festabook.presentation.festating.component.FestatingScreen
import com.daedan.festabook.presentation.main.FestabookRoute

fun NavGraphBuilder.festatingNavGraph() {
    composable<FestabookRoute.Festating> { backStackEntry ->
        val festivalId = backStackEntry.toRoute<FestabookRoute.Festating>().festivalId
        FestatingScreen(festivalId = festivalId)
    }
}
