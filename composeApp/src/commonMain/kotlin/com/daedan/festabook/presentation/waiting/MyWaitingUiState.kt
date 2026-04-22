package com.daedan.festabook.presentation.waiting

import com.daedan.festabook.presentation.waiting.model.MyWaitingUiModel

sealed interface MyWaitingUiState {
    data object Loading : MyWaitingUiState

    data object Empty : MyWaitingUiState

    data class Success(
        val myWaiting: MyWaitingUiModel,
    ) : MyWaitingUiState

    data class Error(
        val throwable: Throwable,
    ) : MyWaitingUiState
}
