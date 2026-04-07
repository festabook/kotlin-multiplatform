package com.daedan.festabook.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class WaitingStatus {
    WAITING,
    CALLED,
    ARRIVED,
    NO_SHOW,
    CANCELED,
}
