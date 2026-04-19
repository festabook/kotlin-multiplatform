package com.daedan.festabook.domain.model

data class MyWaiting(
    val waitingId: Long,
    val waitingOrderFromZero: Int,
    val partySize: Int,
    val waitingStatus: WaitingStatus,
    val totalWaitingTeams: Int,
    val estimatedWaitTime: Int,
    val phoneNumber: String,
    val placeId: Long,
) {
    val waitingOrder = waitingOrderFromZero + 1
}
