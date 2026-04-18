package com.daedan.festabook.data.model.response.waiting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaitingExistResponse(
    @SerialName("exists")
    val exists: Boolean,
)
