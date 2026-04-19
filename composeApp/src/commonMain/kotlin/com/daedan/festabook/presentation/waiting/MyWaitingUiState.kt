package com.daedan.festabook.presentation.waiting

import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel

sealed interface MyWaitingUiState {
    data object Loading : MyWaitingUiState

    data object Empty : MyWaitingUiState

    data class Success(
        val waitingId: Long,
        val placeId: Long?,
        val placeDetail: PlaceDetailUiModel?,
        val order: Int,
        val partySize: Int,
        val phoneNumber: String,
        val totalWaitingTeams: Int,
        val estimatedWaitTime: Int,
        val status: WaitingStatus,
        val isRefreshing: Boolean = false,
        val isCanceling: Boolean = false,
    ) : MyWaitingUiState

    data class Error(val throwable: Throwable) : MyWaitingUiState
}