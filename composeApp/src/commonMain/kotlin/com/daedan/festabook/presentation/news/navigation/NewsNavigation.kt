package com.daedan.festabook.presentation.news.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.news.NewsViewModel
import com.daedan.festabook.presentation.news.component.NewsScreen

fun NavGraphBuilder.newsNavGraph(
    innerPadding: PaddingValues,
    viewModel: NewsViewModel,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    composable<MainTabRoute.News>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        NewsScreen(
            modifier = Modifier.padding(innerPadding),
            newsViewModel = viewModel,
            onShowErrorSnackbar = onShowErrorSnackbar,
        )
    }
}
