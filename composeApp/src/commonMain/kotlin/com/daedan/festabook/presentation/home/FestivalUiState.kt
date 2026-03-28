package com.daedan.festabook.presentation.home

import com.daedan.festabook.domain.model.Organization

sealed interface FestivalUiState {
    data object Loading : FestivalUiState

    data class Success(
        val organization: Organization,
    ) : FestivalUiState

    data class Error(
        val throwable: Throwable,
    ) : FestivalUiState
}
