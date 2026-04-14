package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface WaitingStatusUiState {
    data object Loading : WaitingStatusUiState

    data object InActive : WaitingStatusUiState

    data class Active(
        val estimatedMinutes: Int,
    ) : WaitingStatusUiState

    data class Closed(
        val estimatedMinutes: Int,
    ) : WaitingStatusUiState
}
