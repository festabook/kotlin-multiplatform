package com.daedan.festabook.presentation.festating.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.festating.component.FestatingScreen
import com.daedan.festabook.presentation.main.FestabookRoute

fun NavGraphBuilder.festatingNavGraph() {
    composable<FestabookRoute.Festating> {
        FestatingScreen()
    }
}
