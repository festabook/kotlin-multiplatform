package com.daedan.festabook.presentation.home

import com.daedan.festabook.presentation.home.model.OrganizationUiModel

sealed interface FestivalUiState {
    data object Loading : FestivalUiState

    data class Success(
        val organization: OrganizationUiModel,
    ) : FestivalUiState

    data class Error(
        val throwable: Throwable,
    ) : FestivalUiState
}
