package com.daedan.festabook.presentation.explore.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.explore.ExploreUiState
import com.daedan.festabook.presentation.explore.model.SearchResultUiModel

@Composable
fun ExploreLandingContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onUniversitySelect: (SearchResultUiModel) -> Unit,
    onUniversityDelete: (SearchResultUiModel) -> Unit,
    onClearRecentSearches: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    isError: Boolean,
    isSearchMode: Boolean,
    exploreUiState: ExploreUiState,
) {
    ExploreSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { keyboardController?.hide() },
        isError = isError,
        modifier = Modifier.padding(horizontal = 20.dp),
    )

    AnimatedContent(
        targetState = isSearchMode,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(tween(200)),
                initialContentExit = fadeOut(tween(200)),
            )
        },
    ) { searching ->
        if (searching) {
            ExploreSearchResultList(
                exploreUiState = exploreUiState,
                onUniversitySelect = onUniversitySelect,
                onClearRecentSearches = onClearRecentSearches,
                onUniversityDelete = onUniversityDelete,
            )
        }
    }
}
