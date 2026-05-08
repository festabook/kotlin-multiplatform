package com.daedan.festabook.presentation.explore

import com.daedan.festabook.presentation.explore.model.SearchResultUiModel

data class ExploreUiState(
    val query: String = "",
    val searchState: SearchUiState = SearchUiState.Idle,
    val hasFestivalId: Boolean = false,
    val recentSearches: List<SearchResultUiModel> = emptyList(),
)
