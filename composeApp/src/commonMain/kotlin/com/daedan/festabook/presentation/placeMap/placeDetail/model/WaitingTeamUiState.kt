package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface WaitingTeamUiState {
    data object Loading : WaitingTeamUiState

    data class Refresh(
        val totalTeams: Int,
    ) : WaitingTeamUiState

    data object InActive : WaitingTeamUiState

    data class Success(
        val totalTeams: Int,
    ) : WaitingTeamUiState

    data class Error(
        val throwable: Throwable,
    ) : WaitingTeamUiState
}
