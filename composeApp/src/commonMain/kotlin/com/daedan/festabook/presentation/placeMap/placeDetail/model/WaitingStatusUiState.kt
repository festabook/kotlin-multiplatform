package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface WaitingStatusUiState {
    data object Loading : WaitingStatusUiState

    data class Active(
        val estimatedMinutes: Int,
    ) : WaitingStatusUiState

    data object Closed : WaitingStatusUiState
}
