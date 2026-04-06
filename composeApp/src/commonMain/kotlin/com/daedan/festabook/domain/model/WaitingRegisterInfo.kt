package com.daedan.festabook.domain.model

data class WaitingRegisterInfo(
    val placeId: Long,
    val placeName: String,
    val headCount: Int,
    val waitingInfo: WaitingInfo,
)
