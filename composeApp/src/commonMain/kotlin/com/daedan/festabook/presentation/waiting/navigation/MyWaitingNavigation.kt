package com.daedan.festabook.presentation.waiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.waiting.MyWaitingViewModel
import com.daedan.festabook.presentation.waiting.component.MyWaitingRoute
import dev.zacsweers.metrox.viewmodel.metroViewModel

fun NavGraphBuilder.myWaitingNavGraph(
    onBack: () -> Unit,
    onNavigateToPlaceDetail: (Long) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    composable<FestabookRoute.MyWaiting> {
        MyWaitingRoute(
            viewModel = metroViewModel<MyWaitingViewModel>(),
            onBack = onBack,
            onNavigateToPlaceDetail = onNavigateToPlaceDetail,
            onShowSnackbar = onShowSnackbar,
            onShowErrorSnackbar = onShowErrorSnackbar,
        )
    }
}
