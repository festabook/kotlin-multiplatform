package com.daedan.festabook.presentation.festating

import com.daedan.festabook.presentation.home.model.FestatingUiModel

sealed interface FestatingUiState {
    data object Loading : FestatingUiState

    data class Success(
        val festating: FestatingUiModel,
    ) : FestatingUiState

    data class Error(
        val throwable: Throwable,
    ) : FestatingUiState
}
