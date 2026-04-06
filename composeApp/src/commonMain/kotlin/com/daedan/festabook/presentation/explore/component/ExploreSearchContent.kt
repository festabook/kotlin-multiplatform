package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.explore.ExploreUiState
import com.daedan.festabook.presentation.explore.SearchUiState
import com.daedan.festabook.presentation.explore.model.SearchResultUiModel
import com.daedan.festabook.presentation.theme.FestabookTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExploreSearchContent(
    query: String,
    exploreUiState: ExploreUiState,
    onQueryChange: (String) -> Unit,
    onUniversitySelect: (SearchResultUiModel) -> Unit,
    onClearRecentSearches: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isError = exploreUiState.searchState.shouldShowErrorUi

    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            ExploreSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { keyboardController?.hide() },
                isError = isError,
            )
        }

        ExploreSearchResultList(
            exploreUiState = exploreUiState,
            onUniversitySelect = onUniversitySelect,
            onClearRecentSearches = onClearRecentSearches,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreSearchContentPreview() {
    FestabookTheme {
        ExploreSearchContent(
            query = "서울",
            exploreUiState =
                ExploreUiState(
                    searchState =
                        SearchUiState.Success(
                            listOf(
                                SearchResultUiModel(1, "서울시립대학교", "2024 대동제"),
                                SearchResultUiModel(2, "서울대학교", "2024 봄축제"),
                            ),
                        ),
                ),
            onQueryChange = {},
            onUniversitySelect = {},
            onClearRecentSearches = {},
        )
    }
}
