package com.daedan.festabook.presentation.festating.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.daedan.festabook.presentation.festating.component.FestatingScreen
import com.daedan.festabook.presentation.main.FestabookRoute

fun NavGraphBuilder.festatingNavGraph() {
    composable<FestabookRoute.Festating> { backStackEntry ->
        val route = backStackEntry.toRoute<FestabookRoute.Festating>()
        FestatingScreen(
            organizationId = route.organizationId,
            festivalId = route.festivalId,
            deviceId = route.deviceId,
        )
    }
}
