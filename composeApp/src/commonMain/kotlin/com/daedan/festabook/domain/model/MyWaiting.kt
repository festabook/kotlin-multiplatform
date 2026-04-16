package com.daedan.festabook.domain.model

data class MyWaiting(
    val waitingId: Long,
    val waitingOrder: Int,
    val partySize: Int,
    val waitingStatus: WaitingStatus,
    val totalWaitingTeams: Int,
    val estimatedWaitTime: Int,
    val phoneNumber: String,
    val placeId: Long? = null,
)
