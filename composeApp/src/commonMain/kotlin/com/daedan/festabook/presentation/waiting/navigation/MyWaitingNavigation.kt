package com.daedan.festabook.presentation.waiting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.waiting.MyWaitingViewModel
import com.daedan.festabook.presentation.waiting.component.MyWaitingRoute
import dev.zacsweers.metrox.viewmodel.metroViewModel

fun NavGraphBuilder.myWaitingNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    composable<FestabookRoute.MyWaiting> {
        val viewModel = metroViewModel<MyWaitingViewModel>()
        MyWaitingRoute(
            viewModel = viewModel,
            onBack = onBack,
            onShowSnackbar = onShowSnackbar,
            onShowErrorSnackbar = onShowErrorSnackbar,
        )
    }
}