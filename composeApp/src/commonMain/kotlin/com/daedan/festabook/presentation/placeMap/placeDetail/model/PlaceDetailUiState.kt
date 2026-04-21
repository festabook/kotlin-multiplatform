package com.daedan.festabook.presentation.placeMap.placeDetail.model

sealed interface PlaceDetailUiState {
    data object Loading : PlaceDetailUiState

    data class Success(
        val placeDetail: PlaceDetailUiModel,
        val waitingTeam: WaitingTeamUiState = WaitingTeamUiState.Loading,
        val waitingStatus: WaitingStatusUiState = WaitingStatusUiState.Loading,
    ) : PlaceDetailUiState

    data class Error(
        val throwable: Throwable,
    ) : PlaceDetailUiState
}
