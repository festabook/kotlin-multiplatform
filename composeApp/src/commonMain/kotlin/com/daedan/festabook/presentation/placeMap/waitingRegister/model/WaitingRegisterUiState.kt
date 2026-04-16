package com.daedan.festabook.presentation.placeMap.waitingRegister.model

sealed interface WaitingRegisterUiState {
    data object Loading : WaitingRegisterUiState

    data class Success(
        val placeSummary: WaitingPlaceSummaryUiModel,
        val partySize: Int = MIN_PARTY_SIZE,
        val isServiceAgreed: Boolean = false,
        val isSubmitting: Boolean = false,
        val canDecreasePartySize: Boolean = false,
        val canIncreasePartySize: Boolean = true,
        val canSubmit: Boolean = false,
    ) : WaitingRegisterUiState

    data class Error(val throwable: Throwable) : WaitingRegisterUiState

    companion object {
        const val MIN_PARTY_SIZE = 1
        const val MAX_PARTY_SIZE = 8
    }
}
