package com.daedan.festabook.presentation.waiting.model

import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel

data class MyWaitingUiModel(
    val waitingId: Long,
    val placeId: Long,
    val placeDetail: PlaceDetailUiModel?,
    val order: Int,
    val partySize: Int,
    val phoneNumber: String,
    val totalWaitingTeams: Int,
    val estimatedWaitTime: Int,
    val status: WaitingStatus,
    val isRefreshing: Boolean = false,
    val isCanceling: Boolean = false,
)
