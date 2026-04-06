package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daedan.festabook.presentation.explore.ExploreSideEffect
import com.daedan.festabook.presentation.explore.ExploreUiState
import com.daedan.festabook.presentation.explore.ExploreViewModel
import com.daedan.festabook.presentation.explore.SearchUiState
import com.daedan.festabook.presentation.explore.model.SearchResultUiModel
import com.daedan.festabook.presentation.theme.FestabookTheme
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.explore_festabook_logo
import festabookkmp.composeapp.generated.resources.logo_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExploreScreen(
    onNavigateToMain: (SearchResultUiModel) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ExploreViewModel,
) {
    val exploreUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    val latestNavigateToMain by rememberUpdatedState(onNavigateToMain)
    val latestKeyboardController by rememberUpdatedState(keyboardController)

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ExploreSideEffect.NavigateToMain -> {
                    latestKeyboardController?.hide()
                    latestNavigateToMain(effect.searchResult)
                }
            }
        }
    }

    if (exploreUiState.hasFestivalId) {
        ExploreSearchScreen(
            query = exploreUiState.query,
            exploreUiState = exploreUiState,
            onQueryChange = viewModel::onTextInputChanged,
            onUniversitySelect = viewModel::onUniversitySelected,
            onBackClick = onBackClick,
            onClearRecentSearches = viewModel::onClearRecentSearches,
            onUniversityDelete = viewModel::onRecentSearchDelete,
        )
    } else {
        ExploreLandingScreen(
            query = exploreUiState.query,
            exploreUiState = exploreUiState,
            onQueryChange = viewModel::onTextInputChanged,
            onUniversitySelect = viewModel::onUniversitySelected,
            onClearRecentSearches = viewModel::onClearRecentSearches,
            onUniversityDelete = viewModel::onRecentSearchDelete,
        )
    }
}

@Composable
fun ExploreSearchScreen(
    query: String,
    exploreUiState: ExploreUiState,
    onQueryChange: (String) -> Unit,
    onUniversitySelect: (SearchResultUiModel) -> Unit,
    onUniversityDelete: (SearchResultUiModel) -> Unit,
    onClearRecentSearches: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            ExploreBackHeader(
                onBackClick = onBackClick,
                modifier = Modifier.statusBarsPadding(),
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
        ) {
            ExploreSearchContent(
                query = query,
                exploreUiState = exploreUiState,
                onQueryChange = onQueryChange,
                onUniversitySelect = onUniversitySelect,
                onClearRecentSearches = onClearRecentSearches,
                onUniversityDelete = onUniversityDelete,
            )
        }
    }
}

@Composable
fun ExploreLandingScreen(
    query: String,
    exploreUiState: ExploreUiState,
    onQueryChange: (String) -> Unit,
    onUniversitySelect: (SearchResultUiModel) -> Unit,
    onUniversityDelete: (SearchResultUiModel) -> Unit,
    onClearRecentSearches: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isError = exploreUiState.searchState.shouldShowErrorUi

    val isSearchMode = query.isNotBlank()

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .clickable(
                        onClick = {
                            keyboardController?.hide()
                        },
                        indication = null,
                        interactionSource = null,
                    ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    modifier =
                        Modifier
                            .weight(0.3f),
                )

                Image(
                    painter = painterResource(Res.drawable.logo_title),
                    contentDescription = stringResource(Res.string.explore_festabook_logo),
                    modifier = Modifier.height(24.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))
                ExploreLandingContent(
                    query = query,
                    onQueryChange = onQueryChange,
                    keyboardController = keyboardController,
                    isError = isError,
                    isSearchMode = isSearchMode,
                    exploreUiState = exploreUiState,
                    onUniversitySelect = onUniversitySelect,
                    onClearRecentSearches = onClearRecentSearches,
                    onUniversityDelete = onUniversityDelete,
                )

                Spacer(modifier = Modifier.weight(0.7f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreSearchScreenPreview() {
    FestabookTheme {
        ExploreSearchScreen(
            query = "서울",
            exploreUiState = ExploreUiState(searchState = SearchUiState.Idle),
            onQueryChange = {},
            onUniversitySelect = {},
            onBackClick = {},
            onClearRecentSearches = {},
            onUniversityDelete = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreLandingScreenPreview() {
    FestabookTheme {
        ExploreLandingScreen(
            query = "ㅇㄹㅇ",
            exploreUiState = ExploreUiState(searchState = SearchUiState.Idle),
            onQueryChange = {},
            onUniversitySelect = {},
            onClearRecentSearches = {},
            onUniversityDelete = {},
        )
    }
}
