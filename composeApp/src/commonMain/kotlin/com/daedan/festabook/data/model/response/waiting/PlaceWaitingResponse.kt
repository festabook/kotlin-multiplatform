package com.daedan.festabook.data.model.response.waiting

import com.daedan.festabook.domain.model.PlaceWaiting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceWaitingResponse(
    @SerialName("totalWaitingAmount")
    val totalWaitingAmount: Int,
    @SerialName("estimatedWaitingTime")
    val estimatedWaitingTime: Int,
    @SerialName("isWaitingAvailable")
    val isWaitingAvailable: Boolean,
    @SerialName("hasMyWaiting")
    val hasMyWaiting: Boolean,
)

fun PlaceWaitingResponse.toDomain() = PlaceWaiting(
    totalWaitingAmount = totalWaitingAmount,
    estimatedWaitingTime = estimatedWaitingTime,
    isWaitingAvailable = isWaitingAvailable,
    hasMyWaiting = hasMyWaiting,
)
