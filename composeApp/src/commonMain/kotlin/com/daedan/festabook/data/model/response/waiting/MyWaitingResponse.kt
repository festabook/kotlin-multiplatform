package com.daedan.festabook.data.model.response.waiting

import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.model.WaitingRegisterInfo
import com.daedan.festabook.domain.model.WaitingStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyWaitingResponse(
    @SerialName("waitingId")
    val waitingId: Long,
    @SerialName("waitingStatus")
    val waitingStatus: String,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("headCount")
    val headCount: Int,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("currentWaitingOrder")
    val currentWaitingOrder: Int,
    @SerialName("totalWaitingAmount")
    val totalWaitingAmount: Int,
    @SerialName("estimatedWaitingTime")
    val estimatedWaitingTime: Int,
)

fun MyWaitingResponse.toDomain() = MyWaiting(
    waitingId = waitingId,
    waitingRegisterInfo = WaitingRegisterInfo(
        placeId = placeId,
        placeName = placeName,
        headCount = headCount,
        waitingInfo = WaitingInfo(phoneNumber = phoneNumber),
    ),
    currentWaitingOrder = currentWaitingOrder,
    totalWaitingAmount = totalWaitingAmount,
    estimatedWaitingTime = estimatedWaitingTime,
    waitingStatus = runCatching { WaitingStatus.valueOf(waitingStatus) }
        .getOrDefault(WaitingStatus.WAITING),
)
