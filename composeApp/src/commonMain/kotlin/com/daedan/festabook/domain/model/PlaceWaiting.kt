package com.daedan.festabook.domain.model

data class PlaceWaiting(
    val totalWaitingAmount: Int,
    val estimatedWaitingTime: Int,
    val isWaitingAvailable: Boolean,
    val hasMyWaiting: Boolean,
)
