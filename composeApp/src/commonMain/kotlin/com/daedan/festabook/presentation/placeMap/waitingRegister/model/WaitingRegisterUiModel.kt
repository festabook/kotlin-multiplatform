package com.daedan.festabook.presentation.placeMap.waitingRegister.model

data class WaitingRegisterUiModel(
    val placeSummary: WaitingPlaceSummaryUiModel,
    val partySize: Int = MIN_PARTY_SIZE,
    val isServiceAgreed: Boolean = false,
    val isSubmitting: Boolean = false,
    val canDecreasePartySize: Boolean = false,
    val canIncreasePartySize: Boolean = true,
    val canSubmit: Boolean = false,
) {
    companion object {
        const val MIN_PARTY_SIZE = 1
        const val MAX_PARTY_SIZE = 8
    }
}