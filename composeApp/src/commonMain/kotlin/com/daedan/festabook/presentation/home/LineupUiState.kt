package com.daedan.festabook.presentation.home

import com.daedan.festabook.presentation.home.model.LineUpItemOfDayUiModel

sealed interface LineupUiState {
    data object Loading : LineupUiState

    data class Success(
        val lineups: List<LineUpItemOfDayUiModel>,
    ) : LineupUiState

    data class Error(
        val throwable: Throwable,
    ) : LineupUiState
}
