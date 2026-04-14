package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface WaitingUiState {
    data object Loading : WaitingUiState

    data class Active(
        val totalTeams: Int,
        val estimatedMinutes: Int,
    ) : WaitingUiState

    data class Closed(
        val totalTeams: Int,
    ) : WaitingUiState
}
