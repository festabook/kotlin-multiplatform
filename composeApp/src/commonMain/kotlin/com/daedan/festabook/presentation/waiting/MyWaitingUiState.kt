package com.daedan.festabook.presentation.waiting

import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.placeMap.model.PlaceCategoryUiModel

sealed interface MyWaitingUiState {
    data object Loading : MyWaitingUiState

    data object Empty : MyWaitingUiState

    data class Success(
        val waitingId: Long,
        val placeId: Long?,
        val place: MyWaitingPlaceUiModel?,
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

data class MyWaitingPlaceUiModel(
    val title: String,
    val category: PlaceCategoryUiModel,
    val imageUrl: String?,
    val location: String?,
    val host: String?,
    val operatingTime: String?,
)