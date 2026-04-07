package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.PlaceWaiting

interface WaitingRegisterInfoRepository {
    suspend fun getPlaceWaiting(placeId: Long): Result<PlaceWaiting>

    suspend fun registerWaiting(
        placeId: Long,
        partySize: Int,
    ): Result<MyWaiting>
}
