package com.daedan.festabook.presentation.schedule.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.schedule.ScheduleViewModel
import com.daedan.festabook.presentation.schedule.component.ScheduleScreen

fun NavGraphBuilder.scheduleNavGraph(
    innerPadding: PaddingValues,
    viewModel: ScheduleViewModel,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    composable<MainTabRoute.Schedule> {
        ScheduleScreen(
            modifier = Modifier.padding(innerPadding),
            scheduleViewModel = viewModel,
            onShowErrorSnackbar = onShowErrorSnackbar,
        )
    }
}
