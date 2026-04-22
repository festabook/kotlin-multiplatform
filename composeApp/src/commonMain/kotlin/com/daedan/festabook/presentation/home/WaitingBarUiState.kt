package com.daedan.festabook.presentation.home

import com.daedan.festabook.domain.model.WaitingStatus

sealed interface WaitingBarUiState {
    data object Hidden : WaitingBarUiState

    data class Visible(
        val order: Int,
        val estimatedWaitTime: Int,
        val status: WaitingStatus,
    ) : WaitingBarUiState
}
