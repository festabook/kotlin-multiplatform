package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface PlaceDetailUiState {
    data object Loading : PlaceDetailUiState

    data class Success(
        val placeDetail: PlaceDetailUiModel,
        val waiting: WaitingUiState = WaitingUiState.Loading,
    ) : PlaceDetailUiState

    data class Error(
        val throwable: Throwable,
    ) : PlaceDetailUiState
}

sealed interface WaitingUiState {
    data object Loading : WaitingUiState

    data class Active(
        val totalTeams: Int,
        val estimatedMinutes: Int,
    ) : WaitingUiState

    data class Closed(
        val totalTeams: Int,
    ) : WaitingUiState

    data object Inactive : WaitingUiState
}
