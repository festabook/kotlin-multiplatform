package com.daedan.festabook.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaitingRegisterRequest(
    @SerialName("headCount")
    val headCount: Int,
    @SerialName("phoneNumber")
    val phoneNumber: String,
)
