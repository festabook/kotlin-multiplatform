package com.daedan.festabook.presentation.placeMap.waitingRegister.model

sealed interface WaitingRegisterUiState {
    data object Loading : WaitingRegisterUiState

    data class Success(
        val waitingRegister: WaitingRegisterUiModel,
    ) : WaitingRegisterUiState

    data class Error(
        val throwable: Throwable,
    ) : WaitingRegisterUiState
}