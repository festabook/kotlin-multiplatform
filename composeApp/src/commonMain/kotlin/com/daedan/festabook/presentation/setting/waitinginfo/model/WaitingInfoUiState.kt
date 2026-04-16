package com.daedan.festabook.presentation.setting.waitinginfo.model

sealed interface WaitingInfoUiState {
    data object Loading : WaitingInfoUiState

    data class Registered(
        val phoneNumber: String,
    ) : WaitingInfoUiState

    data object NotRegistered : WaitingInfoUiState

    data class Error(
        val throwable: Throwable,
    ) : WaitingInfoUiState
}
