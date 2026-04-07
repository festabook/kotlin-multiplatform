package com.daedan.festabook.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhoneRegisterRequest(
    @SerialName("phoneNumber")
    val phoneNumber: String,
)
