package com.daedan.festabook.data.model.response.waiting

import com.daedan.festabook.domain.model.PlaceWaiting
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceWaitingResponse(
    @SerialName("totalWaitingTeams")
    val totalWaitingTeams: Int,
    @SerialName("estimatedWaitTime")
    val estimatedWaitTime: Int,
)

fun PlaceWaitingResponse.toDomain() =
    PlaceWaiting(
        totalWaitingTeams = totalWaitingTeams,
        estimatedWaitTime = estimatedWaitTime,
    )
