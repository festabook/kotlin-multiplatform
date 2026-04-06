package com.daedan.festabook.domain.model

data class MyWaiting(
    val waitingId: Long,
    val waitingRegisterInfo: WaitingRegisterInfo,
    val currentWaitingOrder: Int,
    val totalWaitingAmount: Int,
    val estimatedWaitingTime: Int,
    val waitingStatus: WaitingStatus,
)
