package com.daedan.festabook.presentation.home

sealed interface WaitingBarUiState {
    data object Hidden : WaitingBarUiState

    data class Visible(
        val order: Int,
        val estimatedWaitTime: Int,
    ) : WaitingBarUiState
}