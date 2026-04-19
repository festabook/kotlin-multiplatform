package com.daedan.festabook.data.model.response.waiting

import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.WaitingStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyWaitingResponse(
    @SerialName("waitingId")
    val waitingId: Long,
    @SerialName("waitingOrder")
    val waitingOrder: Int,
    @SerialName("partySize")
    val partySize: Int,
    @SerialName("waitingStatus")
    val waitingStatus: WaitingStatus,
    @SerialName("totalWaitingTeams")
    val totalWaitingTeams: Int,
    @SerialName("estimatedWaitTime")
    val estimatedWaitTime: Int,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("placeId")
    val placeId: Long,
)

fun MyWaitingResponse.toDomain() =
    MyWaiting(
        waitingId = waitingId,
        waitingOrder = waitingOrder,
        partySize = partySize,
        waitingStatus = waitingStatus,
        totalWaitingTeams = totalWaitingTeams,
        estimatedWaitTime = estimatedWaitTime,
        phoneNumber = phoneNumber,
        placeId = placeId,
    )
